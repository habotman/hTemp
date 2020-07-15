package com.kakaoPay.kakaoPayTemp.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kakaoPay.kakaoPayTemp.common.Cipher;
import com.kakaoPay.kakaoPayTemp.common.ComUtil;
import com.kakaoPay.kakaoPayTemp.dao.PayPrcDao;
import com.kakaoPay.kakaoPayTemp.vo.PayPrcVo;

 
@Service
public class PayPrcServiceImpl implements PayPrcService {

	private static final Log log = LogFactory.getLog(PayPrcServiceImpl.class);
	
	@Autowired
	PayPrcDao payPrcDao;
	
	@Override
	public PayPrcVo funcPayment(PayPrcVo inPayPrcVo) throws Exception {
		
		/**
		 * 결제 처리 서비스 PAYMENT
		 * 1. 결제 정보 정합성 체크 -중복 진입, 미처리, 관리사항 체크 DB 조회
		 * 2. 입력값 검증 정제 -결제금액, 부가세처리
		 * 3. 관리테이블 정보 적재 - 관리번호 채번
		 * 4. 욉부 카드사 전송 처리 -H2 DB 적제 성공가정 - 관리정보  관리 상태 코드 : I:입력(내부관리상태), C:성공, F:실패  - 수정처리
		 * 5. 결제/취소 정보 테이블 Row 단위 적재 처리.
		 */

		log.debug("@Service : funcPayment start ================");
		
		PayPrcVo inVo = inPayPrcVo;
		
		inVo.setDataFlgcd  ( "PAYMENT" ); //결제취소구분 PAYMENT/CANCEL
		inVo.setTlmIfFlg("I"); //관리정보 상태 코드
		inVo.setTlmIfMsg("결제신청: 결제신청 저장처리~!!!");
		
		/*
		 * 정합성 체크 
		 * 기존정보 카드번호 or 관리번호 체크 관리정보 상태  C : 완료상태 이외는 체크
		 * 방어로직 구현 : 해당 처리가 되어지지 않은건은 시스템 처리 중으로 간주 진행 되지 않게.
		 * TO-DO : 관리상태상의 break 사항 구현사항
		 * 
		 *  ex :testCd : TEST 인 경우  아래 sleep break, 추가 진입시에 체크
		 */
		int rtnCnt = payPrcDao.selectPayCnt(inVo);
		if( rtnCnt > 0 ) {
			throw new Exception("해당 카드번호에 [I/F 관리체크] 미처리된 건이 존재합니다.(TEST CASE) - 해당 카드번호 미결처리건 해소후 정상가능.");
		}
		
		/*
		 * 입력받은 결제금액, 부가세 검증 정제 처리 
		 * 부가세 부분 과제요건 내용적용 
		 */
		if(inVo.getTrAmt() == null || "".equals(inVo.getTrAmt())) {
			throw new Exception("거래금액 없음.");
		}
		BigDecimal steAmt = BigDecimal.ZERO;
		BigDecimal trAmt = new BigDecimal(inVo.getTrAmt());
		if(inVo.getSteAmt() != null && !"".equals(inVo.getSteAmt())) {
			//set
			inVo.setSteAmt( inVo.getSteAmt() );
		}else {
			// 소수점 절사 반옴림처리
			steAmt = trAmt.divide(new BigDecimal("11"), RoundingMode.HALF_EVEN);
			//set
			inVo.setSteAmt( steAmt.toPlainString());
			
		}
		
		/*
		 * 관리테이블 적재 
		 * 시퀀스 20자리 uniqre id 생성 :selectKey ( YYYYMMDD+ lad 시퀀스)
		 * 관리테이블 생성후 키 리턴 받는다/
		 */
		String tlmCrno = payPrcDao.insertPayment(inVo);
		log.debug("관리번호 tlmCrno :>" + tlmCrno);
		if(tlmCrno == null || "".equals(tlmCrno)) {
			throw new Exception("관리번호 조회/등록 실패!!");
		}
		
		//TODO : 동시처리 부분 방어. 사전 break~!
		//testCd : TEST 인 경우 break
		if("TEST".equals(inVo.getTestCd())) {
			log.debug("TEST sleep millis : ############################## BREAK   ############################## ");
			log.debug("TEST sleep millis :100000 millis after ########################");
			Thread.sleep(100000);
			log.debug("TEST sleep millis :########## start ########################");
			
		}
		
		/*  관리번호 unique */
		//in set
		inVo.setTlmCrno(tlmCrno); //관리번호 채번 셋팅
		
		//암호화 카드정보 ex. encrypt(카드정보|유효기간|cvc)
		String encStr = Cipher.encrypt( inVo.getCrdno()
				.concat("|").concat(inVo.getCrdLimt())
				.concat("|").concat(inVo.getCvc()) );
		inVo.setEncCrdWrt(encStr);
		log.debug("encStr : setEncCrdWrt :" + inVo.getEncCrdWrt());
		
		/*
		 * 저장 성공시에 I/f : 전문통신 대체 별도 Table insert
		 * 결제/취소 외부 저장 카드사 전문 성공 H2 테이블 대체
		 * 관리테이블의 상태 외부전문 성공으로 완료 업데이트.
		 */
		PayPrcVo inOttVo = ottSendFnc(inVo, tlmCrno);
		
		
		/* 
		 * 최종 결제정보 이력 적재
		 * 결제/취소 처리 정보 레코드 적재.
		 * 암호화 관련 정보 이력 적재
		 */
		payPrcDao.insertKpayCrdBaseMng(inVo);
		
		//return 
		PayPrcVo rtnVo = new PayPrcVo();
		rtnVo.setTlmCrno(inOttVo.getTlmCrno());
		rtnVo.setDataDtl(inOttVo.getDataDtl());
		
		log.debug("@Service : funcPayment end ================");
		
		return rtnVo;
		
	}


	
	@Override
	public PayPrcVo funcCancel(PayPrcVo inPayPrcVo) throws Exception {

		/**
		 * 취소 처리 서비스 CANCEL
		 * 1. 결제 정보 정합성 체크 -중복 진입, 미처리, 관리사항 체크 DB 조회
		 * 2. 입력값 검증 정제 -거래번호의 결제정보, 결제금액, 부가세처리 대상 취소요건 처리
		 * 3. 관리테이블 정보 적재 - 관리번호 채번
		 * 4. 욉부 카드사 전송 처리 -H2 DB 적제 성공가정 - 관리정보  관리 상태 코드 : I:입력(내부관리상태), C:성공, F:실패  - 수정처리
		 * 5. 결제/취소 정보 테이블 Row 단위 적재 처리.
		 */
		
		log.debug("@Service : funcCancel start ================");
		PayPrcVo inVo = inPayPrcVo;
		
		inVo.setDataFlgcd  ( "CANCEL" );
		inVo.setGbTrMgtno(inVo.getTlmCrno() ); //원거래관리번호
		inVo.setTlmIfFlg("I");
		inVo.setTlmIfMsg("취소신청: 취소신청 저장처리~!!!");
		
		/*
		 * 정합성 체크 
		 * 기존정보 카드번호 or 관리번호 체크 관리정보 상태  C : 완료상태 이외는 체크
		 * 방어로직 구현 : 해당 처리가 되어지지 않은건은 시스템 처리 중으로 간주 진행 되지 않게.
		 * TO-DO : 관리상태상의 break 사항 구현사항
		 * 
		 *  ex :testCd : TEST 인 경우  아래 sleep break, 추가 진입시에 체크
		 */
		int rtnCnt = payPrcDao.selectPayCnt(inVo);
		if( rtnCnt > 0 ) {
			throw new Exception("해당 카드번호에 [I/F 관리체크] 미처리된 건이 존재합니다.(TEST CASE) - 해당 카드번호 미결처리건 해소후 정상가능.");
		}
		
		/*
		 * 관리번호 by 결제대상 선택 체크  : 미사용가능
		 */
		int bCnt = payPrcDao.selectKpayCrdBaseMngCnt(inVo);
		log.debug("bCnt:" + bCnt);
		if( bCnt < 1 ) {
			throw new Exception("결제내역이 없음.취소대상건 없음.");
		}
		
		/*
		 * 입력받은 결제금액, 부가세 검증 정제 처리 
		 * 부가세 부분 과제요건 내용적용 
		 */
		if(inVo.getTrAmt() == null || "".equals(inVo.getTrAmt())) {
			throw new Exception("거래금액 없음.");
		}
		
		/*
		 * 결제정보 대상 금액정보 조회
		 * 결제 대상인 결제금액 합, 부가가치세 합 을 구한다. 결제금액 - 취소금액 = 잔존 대상금액 / 결제부가가치세 - 취소부가가치세 = 잔존 부가가치세
		 * */
		PayPrcVo rtnBaseMng = payPrcDao.selectKpayCrdBaseMng(inVo);
		
		/*
		 * 입력받은 결제금액, 부가세 검증 정제 처리 
		 * 부가세 부분 과제요건 내용적용 
		 */
		//결제 금액가져오기
		BigDecimal totTrAmt  = new BigDecimal(rtnBaseMng.getAtrAmt());
		BigDecimal totSteAmt = new BigDecimal(rtnBaseMng.getAsteAmt());
		
		//부가가치세 체크 - 결제금액이 존재 하면서 부가가치세가 없으면 자동계산
		BigDecimal steAmt = BigDecimal.ZERO;
		BigDecimal trAmt = new BigDecimal(inVo.getTrAmt());
		if(inVo.getSteAmt() == null || "".equals(inVo.getSteAmt())) {
			//부가가치세 남음금액 비교처리 마지막 떨이 작업
			if(totTrAmt.compareTo(trAmt) == 0){
				steAmt = totSteAmt;
			}else {
				// 소수점 절사 반옴림처리
				steAmt = trAmt.divide(new BigDecimal("11"), RoundingMode.HALF_EVEN);
			}
		}else {
			//set
			steAmt =  new BigDecimal( inVo.getSteAmt() );
		}
		
		//결제 금액비교
		if(trAmt.compareTo(BigDecimal.ZERO) <= 0) {
			throw new Exception("결제금액이 없어서 실패");
		}
		//결제 금액비교
		if(trAmt.compareTo(totTrAmt) > 0) {
			throw new Exception("남은 결제금액보다 커서 실패");
		}
		//부가가치세비교
		if(steAmt.compareTo(totSteAmt) > 0) {
			throw new Exception("남은 부가가치세보다 취소요청 부가가치세가 커서 실패");
		}
		
		//남은 부가가치세 상이
		if(totTrAmt.compareTo(trAmt) == 0){
			if(totSteAmt.compareTo(steAmt) != 0){
				throw new Exception("남은 부가가치세가 달라서 실패");
			}
		}
		
		//set
		inVo.setSteAmt( steAmt.toPlainString());
		
		/*
		 * 관리테이블 적재 
		 * 시퀀스 20자리 uniqre id 생성 :selectKey ( YYYYMMDD+ lad 시퀀스)
		 * 관리테이블 생성후 키 리턴 받는다/
		 */
		String tlmCrno = payPrcDao.insertPayment(inVo);
		log.debug("tlmCrno ::>" + tlmCrno);
		if(tlmCrno == null || "".equals(tlmCrno)) {
			throw new Exception("관리번호 조회/등록 실패!!");
		}

		//TODO : 동시처리 부분 방어. 사전 break~!
		//testCd : TEST 인 경우 break
		if("TEST".equals(inVo.getTestCd())) {
			log.debug("TEST sleep millis : ############################## BREAK   ############################## ");
			log.debug("TEST sleep millis :100000 millis after ########################");
			Thread.sleep(100000);
			log.debug("TEST sleep millis :########## start ########################");
			
		}
		
		/*  관리번호 unique */
		//in set 
		inVo.setTlmCrno(tlmCrno); //관리번호 채번 셋팅
		
		//암호화 카드정보 ex. encrypt(카드정보|유효기간|cvc)
		String encStr = Cipher.encrypt( inVo.getCrdno()
				.concat("|").concat(inVo.getCrdLimt())
				.concat("|").concat(inVo.getCvc()) );
		inVo.setEncCrdWrt(encStr);
		log.debug("encStr : setEncCrdWrt :" + inVo.getEncCrdWrt());

		/*
		 * 저장 성공시에 I/f : 전문통신 대체 별도 Table insert
		 * 결제/취소 외부 저장 카드사 전문 성공 H2 테이블 대체
		 * 관리테이블의 상태 외부전문 성공으로 완료 업데이트.
		 */
		PayPrcVo inOttVo = ottSendFnc(inVo, tlmCrno);
		
		/* 
		 * 최종 결제정보 이력 적재
		 * 결제/취소 처리 정보 레코드 적재.
		 * 암호화 관련 정보 이력 적재
		 */
		payPrcDao.insertKpayCrdBaseMng(inVo);
		
		//return
		PayPrcVo rtnVo = new PayPrcVo();
		rtnVo.setTlmCrno(inOttVo.getTlmCrno());
		rtnVo.setTrAmt(inVo.getTrAmt());
				
		return rtnVo;
	}

	@Override
	public PayPrcVo funcSelect(PayPrcVo inPayPrcVo) throws Exception {

		/** 
		 * 조회 처리 서비스 - DB에 저장된 데이터를 조회
		 * 1. 입력값 검증 - 관리번호 null check
		 * 2. 결제정보테이블 대상 조회.
		 * 3. out data 정제처리,  과제요건 - 카드정보 복호화처리 포함.
		 *  dataFlgcd - 결제취소구분 - PAYMENT/CANCEL
		 *  추가정보 필드 : 작성일시
		 */
		
		log.debug("@Service : funcSelect start ================");
		
		PayPrcVo inVo = inPayPrcVo;
		
		/*
		 * 관리번호 null check
		 */
		if(inVo.getTlmCrno() == null || "".equals(inVo.getTlmCrno())) {
			throw new Exception("TlmCrno is null : 관리번호 없음.");
		}
		/*
		 * 결제정보테이블 대상조회
		 */
		PayPrcVo infoVo = payPrcDao.selectKpayCrdInfo(inVo);
		if( infoVo == null ) {
			throw new Exception("정보가 존재하지 않습니다.");
		}
		
		//outdata
		PayPrcVo rtnInfoVo = new PayPrcVo();
		
		rtnInfoVo.setTlmCrno(infoVo.getTlmCrno());
		
		//카드정보 복호화  encrypt(카드정보|유효기간|cvc) 
		String decStr = Cipher.decrypt(infoVo.getEncCrdWrt());
		
		String[] decSarr = decStr.split("\\|");
		String decCrdno = decSarr[0]; //카드번호
		String decCrdLimt = decSarr[1]; //유효기간
		String decCvc = decSarr[2]; //CVC
		
		//카드번호 : 앞 6자리와 뒤 3자리를 제외한 나머지를 마스킹처리
		String cStr = decCrdno.substring(0, 6);
		int len = decCrdno.length()-9;
		String cStr1 = "";
		for(int i=0; i<=len; i++) {
			cStr1+="*";
		}
		String cStr2 = decCrdno.substring(decCrdno.length()-3, decCrdno.length());
		
		rtnInfoVo.setCrdno(cStr.concat(cStr1).concat(cStr2));
		rtnInfoVo.setCrdLimt(decCrdLimt);
		rtnInfoVo.setCvc(decCvc);
		rtnInfoVo.setDataFlgcd(infoVo.getDataFlgcd());
		rtnInfoVo.setTrAmt(infoVo.getTrAmt());
		rtnInfoVo.setSteAmt(infoVo.getSteAmt());
		rtnInfoVo.setWrtdt(infoVo.getWrtdt());
		
		return rtnInfoVo;
	}
	
	

	/**
	 * 결제/취소 성공시 처리
	 * 결제요청 외부 카드사 전문 처리 H2 DB 처리. 
	 * 최종 결제정보 테이블에 처리 내용 적재 (결제완료/취소완료)
	 * 이후 내부 관리정보 테이블에 상태를 수정.
	 * 
	 * @param inVo
	 * @param tlmCrno
	 * @return
	 * @throws Exception
	 */
	private PayPrcVo ottSendFnc(PayPrcVo inVo, String tlmCrno) throws Exception {
		
		log.debug("@Service private: ottSendFnc start ================");
		
		PayPrcVo inOttVo = new PayPrcVo();
		inOttVo.setTlmCrno(inVo.getTlmCrno());
		inOttVo.setDataFlgcd(inVo.getDataFlgcd());

		StringBuffer dataSb = new StringBuffer();
		
		/*
		 * 전송하는 string 데이터 를 공통헤더부문과 데이터부문을 합쳐 하나의 string(450자리)
		 */
		
		//heder 
		dataSb.append(ComUtil.rightPad(inVo.getDataFlgcd(), 10, "_")); //데이터구분 : 문자
		dataSb.append(ComUtil.rightPad(tlmCrno, 20, "_")); //관리번호 : 문자
		
		//data
		dataSb.append(ComUtil.rightPad(inVo.getCrdno(), 20, "_")); //카드번호  : 숫자(L)
		dataSb.append(ComUtil.leftPad(inVo.getAtMnt(), 2, "0")); //할부개월수  : //숫자(0)
		
		//카드유효번호 월(2자리), 년도(2자리) ex) 0125 -> 2025년
		String clStr = inVo.getCrdLimt();
		String clStrCa = clStr.substring(2, 4).concat(clStr.substring(0, 2));
		dataSb.append(ComUtil.rightPad(clStrCa, 4, "_")); //카드유효기간  : 숫자(L)
		dataSb.append(ComUtil.rightPad(inVo.getCvc(), 3, "_")); //cvc  : 숫자(L)
		dataSb.append(ComUtil.leftPad(inVo.getTrAmt(), 10, "_")); //거래금액  : 숫자
		dataSb.append(ComUtil.leftPad(inVo.getSteAmt(), 10, "0")); //부가가치세  : //숫자(0)
		dataSb.append(ComUtil.rightPad(inVo.getGbTrMgtno(), 20, "_")); //원거래관리번호 : 문자
		dataSb.append(ComUtil.rightPad(inVo.getEncCrdWrt(), 300, "_")); //암호화된카드정보 : 문자
		dataSb.append(ComUtil.rightPad(inVo.getEctWrt(), 47, "_")); //예비필드 : 문자
		
		//heder insert
		int lenSb = dataSb.length();
		dataSb.insert(0, ComUtil.leftPad( String.valueOf(lenSb) , 4, "_")); //데이터 길이 :숫자
		
		inOttVo.setDataLen(String.valueOf(lenSb));
		
		log.debug("dataSb : lenSb :" + lenSb);
		log.debug("dataSb : :" + dataSb.toString());
		inOttVo.setDataDtl(dataSb.toString());
		
		/*
		 *  결제요청 외부 카드사 전문 처리 H2 DB 처리. 외부 - 카드사로 전송하는 모든 요청은 성공이라고 가정
		 */
		int rtnOttCnt = payPrcDao.insertOttCrdTrmgt(inOttVo);
		
		/*
		 * 추가: 외부 카드사 처리가 최종이라고 가정 (성공) 이후 내부 관리정보 테이블에 상태를 수정 .
		 * 내부 DB 시스템으로 정합성 체크 사항으로 처리 가능. 유효성 증대
		 * 관리 상태 코드 : I:입력(내부관리상태), C:성공, F:실패 
		 * 관리 상태 비고 : 내용
		 */
		//실패 대비 
		if(rtnOttCnt < 1) {
			//카드 전문 통신 상태 update : 실패 F
			inVo.setTlmIfFlg("F");
			inVo.setTlmIfMsg("카드사 전문 I/F 실패: connection refuse");
			
			//처리 상태코드 
			payPrcDao.updatekpayCrdTrMng(inVo);
			throw new Exception("카드사 전문 I/F 실패");
		}
		
		//카드 전문 통신 상태 update : 완료 C
		inVo.setTlmIfFlg("C");
		inVo.setTlmIfMsg("성공: 카드사 I/F 응답완료!!");
		payPrcDao.updatekpayCrdTrMng(inVo);
		
		
		log.debug("@Service private: ottSendFnc end ================");
		
		return inOttVo;
	}


}
