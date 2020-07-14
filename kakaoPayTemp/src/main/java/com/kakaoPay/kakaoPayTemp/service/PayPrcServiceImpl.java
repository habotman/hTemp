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
		
		log.debug("@Service : funcPayment start ================");
		
		/* 1.기본 정보 체크 
			카드번호(10 ~ 16자리 숫자)
			유효기간(4자리 숫자, mmyy)
			cvc(3자리 숫자)
			할부개월수 : 0(일시불), 1 ~ 12
			결제금액(100원 이상, 10억원 이하, 숫자)
			optional
			부가가치세 
			optional 데이터이므로 값을 받지 않은 경우, 자동계산 합니다.
			자동계산 수식 : 결제금액 / 11, 소수점이하 반올림
			결제금액이 1,000원일 경우, 자동계산된 부가가치세는 91원입니다.
			부가가치세는 결제금액보다 클 수 없습니다.
			결제금액이 1,000원일 때, 부가가치세는 0원일 수 있습니다.
		 */
		PayPrcVo inVo = inPayPrcVo;
		
		inVo.setDataFlgcd  ( "PAYMENT" );
		inVo.setTlmIfFlg("I");
		inVo.setTlmIfMsg("결제신청: 결제신청 저장처리~!!!");
		
		
		//기존정보 있는경우 처리 2.관리정보체크  , 없으면 3번으로 바로~ 고
		int rtnCnt = payPrcDao.selectPayCnt(inVo);
		if( rtnCnt > 0 ) {
			//기존 정보가 있네? 여기서 방어로직을 해볼까?
			throw new Exception("해당 카드번호에 [I/F 관리체크] 미처리된 건이 존재합니다.(TEST CASE) - 해당 카드번호 미결처리건 해소후 정상가능.");
		}
		
	
		//3.저장 후 카드사 전달 - 내부 관리저장 dao
		// -카드정보과 금액정보를 입력받아서 카드사와 협의된 string 데이터로 DB에 저장합니다.
		
		//부가가치세 체크 - 결제금액이 존재 하면서 부가가치세가 없으면 자동계산
		
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
		
		
		String tlmCrno = payPrcDao.insertPayment(inVo);
		log.debug("tlmCrno ::>" + tlmCrno);
		if(tlmCrno == null || "".equals(tlmCrno)) {
			throw new Exception("관리번호 조회/등록 실패!!");
		}
		
		//TODO : 동시처리 부분 방어. 사전 블럭킹~!
		if("TEST".equals(inVo.getTestCd())) {
			log.debug("TEST : >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			Thread.sleep(10000);
		}
		
		
		//in set
		inVo.setTlmCrno(tlmCrno);
		
		
		//암호화 카드정보 ex. encrypt(카드정보|유효기간|cvc)
		String encStr = Cipher.encrypt( inVo.getCrdno()
				.concat("|").concat(inVo.getCrdLimt())
				.concat("|").concat(inVo.getCvc()) );
		inVo.setEncCrdWrt(encStr);
		log.debug("encStr : setEncCrdWrt :" + inVo.getEncCrdWrt());
		
		//저장 성공시에 I/f : 전문통신 대체 별도 Table insert
		//4.외부 저장 카드사 전문 성공 H2 테이블 dao
		PayPrcVo inOttVo = ottSendFnc(inVo, tlmCrno);
		
		
		/** 5.최종완료 카드처리 관리 원장에 insert 처리한다.
		 */
		payPrcDao.insertKpayCrdBaseMng(inVo);
		
		//리턴 정의
		//관리번호(unique id, 20자리)
		//카드사에 전달한 string 데이터 : "공통 헤더부문" + "데이터 부문"
		PayPrcVo rtnVo = new PayPrcVo();
		rtnVo.setTlmCrno(inOttVo.getTlmCrno());
		rtnVo.setDataDtl(inOttVo.getDataDtl());
		
		
		return rtnVo;
		
	}


	
	@Override
	public PayPrcVo funcCancel(PayPrcVo inPayPrcVo) throws Exception {

		/**
		 * 취소는 결제랑 같은데 
		 * 결제에 대한 전체 취소 1번만 모두다 취소 함. -> 이후 취소 되면 머가 없으니 취소할께 없다고 리턴
		 * 부가가치세 계산 오바 되면 리턴 실패처리
		 * 부분 취소는 결제 금액 비교 부가가치세 비교 실패시 처리
		 * 
		 * 취소 설계 기본 결제와 동일 패턴으로 정합성 추가 리턴 실패처리
		 * 전체 취소시는 한번에
		 * 결제금 부가가치세 비교는 한번에 결국 취소와 부분 취소는 동일한 프로세스로..
		 * 마무리 취소 플래그로 저장처리 함.
		 */

		log.debug("@Service : funcCancel start ================");
		PayPrcVo inVo = inPayPrcVo;
		
		inVo.setDataFlgcd  ( "CANCEL" );
		inVo.setGbTrMgtno(inVo.getTlmCrno() ); //원거래관리번호
		inVo.setTlmIfFlg("I");
		inVo.setTlmIfMsg("취소신청: 취소신청 저장처리~!!!");
		
		/* 1.기본 정보 체크 
			카드번호(10 ~ 16자리 숫자)
			유효기간(4자리 숫자, mmyy)
			cvc(3자리 숫자)
			할부개월수 : 0(일시불), 1 ~ 12
			결제금액(100원 이상, 10억원 이하, 숫자)
			optional
			부가가치세 
			optional 데이터이므로 값을 받지 않은 경우, 자동계산 합니다.
			자동계산 수식 : 결제금액 / 11, 소수점이하 반올림
			결제금액이 1,000원일 경우, 자동계산된 부가가치세는 91원입니다.
			부가가치세는 결제금액보다 클 수 없습니다.
			결제금액이 1,000원일 때, 부가가치세는 0원일 수 있습니다.
		*/
		
		int rtnCnt = payPrcDao.selectPayCnt(inVo);
		if( rtnCnt > 0 ) {

			//기존 정보가 있네? 여기서 방어로직을 해볼까?
			throw new Exception("I/F 관리체크 미처리된건이 존재합니다.");
			
		}
		
		//기존정보 있는경우 처리 2.관리정보체크  , 없으면 3번으로 바로~ 고
		int bCnt = payPrcDao.selectKpayCrdBaseMngCnt(inVo);
		log.debug("bCnt:" + bCnt);
		if( bCnt < 1 ) {

			//기존 정보가 있네? 여기서 방어로직을 해볼까?
			throw new Exception("결제내역이 없음.취소대상건 없음");
			
		}
		
		
		//3.저장 후 카드사 전달 - 내부 관리저장 dao
		// -카드정보과 금액정보를 입력받아서 카드사와 협의된 string 데이터로 DB에 저장합니다.
		

		/**부가가치세 sql 로 결제금액과 부분취소 금액 계산 남은 결제 금액 비교 처리 하기.
			optional 데이터이므로 값을 받지 않은 경우, 자동계산 합니다.
			자동계산 수식 : 결제금액 / 11, 소수점이하 반올림
			결제금액이 1,000원일 경우, 자동계산된 부가가치세는 91원입니다.
			부가가치세는 결제금액보다 클 수 없습니다.
			결제금액이 1,000원일 때, 부가가치세는 0원일 수 있습니다.
			
			!추가 : 결제인경우 부가가치세는 계산해서 넣으면 되지만 , 본 프로그램에서 처리 이외에 data 및 참조 대내 등 부가가치세 증가 사항이 고려 되지 않으므로
			결제처리에서 부가가치세 정합성은 모두 성공이다.
		*/
		//결제 대상 조회 ( 결제상태인 금액 부분 상세) 조회 쿼리로 구하여  결과 처리 한다.
		// 주요 결제상태인 금액 구함, 결제상태인 부가가치세 구함. <--- 별도 데이터 적재할 필요성이 있나? <원장 카드사가 아니라 필요없다는 판단!!!!>
		PayPrcVo rtnBaseMng = payPrcDao.selectKpayCrdBaseMng(inVo);
		
		//결제 금액가져오기
		BigDecimal totTrAmt  = new BigDecimal(rtnBaseMng.getAtrAmt());
		BigDecimal totSteAmt = new BigDecimal(rtnBaseMng.getAsteAmt());
		
		//부가가치세 체크 - 결제금액이 존재 하면서 부가가치세가 없으면 자동계산
		BigDecimal steAmt = BigDecimal.ZERO;
		BigDecimal trAmt = new BigDecimal(inVo.getTrAmt());
		System.out.print(">>>>>>>>>>>>>>>>>>>>>>>" + ComUtil.convertToJsonString(inVo));
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
		
		String tlmCrno = payPrcDao.insertPayment(inVo);
		log.debug("tlmCrno ::>" + tlmCrno);
		if(tlmCrno == null || "".equals(tlmCrno)) {
			throw new Exception("관리번호 조회/등록 실패!!");
		}

		//in set
		inVo.setTlmCrno(tlmCrno);
		
		//암호화 카드정보 ex. encrypt(카드정보|유효기간|cvc)
		String encStr = Cipher.encrypt( inVo.getCrdno()
				.concat("|").concat(inVo.getCrdLimt())
				.concat("|").concat(inVo.getCvc()) );
		inVo.setEncCrdWrt(encStr);
		log.debug("encStr : setEncCrdWrt :" + inVo.getEncCrdWrt());
		//저장 성공시에 I/f : 전문통신 대체 별도 Table insert
		//4.외부 저장 카드사 전문 성공 H2 테이블 dao
		PayPrcVo inOttVo = ottSendFnc(inVo, tlmCrno);
		
		/** 5.최종완료 카드처리 관리 원장에 insert 처리한다.
		*/
		
		payPrcDao.insertKpayCrdBaseMng(inVo);
		
		
		//리턴 정의
		//관리번호(unique id, 20자리)
		//취소금액
		PayPrcVo rtnVo = new PayPrcVo();
		rtnVo.setTlmCrno(inOttVo.getTlmCrno());
		rtnVo.setTrAmt(inVo.getTrAmt());
				
		return rtnVo;
	}

	@Override
	public PayPrcVo funcSelect(PayPrcVo inPayPrcVo) throws Exception {

		log.debug("@Service : funcSelect start ================");
		
		PayPrcVo inVo = inPayPrcVo;
		
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
	 * 외부 카드사 전문 처리 H2 DB 처리. 
	 * @param inVo
	 * @param tlmCrno
	 * @return
	 * @throws Exception
	 */
	private PayPrcVo ottSendFnc(PayPrcVo inVo, String tlmCrno) throws Exception {
		PayPrcVo inOttVo = new PayPrcVo();
		inOttVo.setTlmCrno(inVo.getTlmCrno());
		inOttVo.setDataFlgcd(inVo.getDataFlgcd());
		
		
		/*
		ComUtil.leftPad(tlmCrno, 20, "_") //숫자
		ComUtil.leftPad(tlmCrno, 20, "0")  //숫자(0)
		ComUtil.rightPad(tlmCrno, 20, "_")  //숫자(L)
		ComUtil.rightPad(tlmCrno, 20, "_")  //문자
		*/
		StringBuffer dataSb = new StringBuffer();
		
		//heder 
		dataSb.append(ComUtil.rightPad(inVo.getDataFlgcd(), 10, "_")); //데이터구분 : 문자
		dataSb.append(ComUtil.rightPad(tlmCrno, 20, "_")); //관리번호 : 문자
		
		//data
		dataSb.append(ComUtil.rightPad(inVo.getCrdno(), 20, "_")); //카드번호  : 숫자(L)
		dataSb.append(ComUtil.leftPad(inVo.getAtMnt(), 2, "0")); //할부개월수  : //숫자(0)
		dataSb.append(ComUtil.rightPad(inVo.getCrdLimt(), 4, "_")); //카드유효기간  : 숫자(L)
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
		
		int rtnOttCnt = payPrcDao.insertOttCrdTrmgt(inOttVo);
		
		//실패
		if(rtnOttCnt < 1) {
			//카드 전문 통신 상태 update : 실패 F
			inVo.setTlmIfFlg("F");
			inVo.setTlmIfMsg("카드사 전문 I/F 실패: connection refuse");
			
			payPrcDao.updatekpayCrdTrMng(inVo);
			throw new Exception("카드사 전문 I/F 실패");
		}
		
		//카드 전문 통신 상태 update : 완료 C
		inVo.setTlmIfFlg("C");
		inVo.setTlmIfMsg("성공: 카드사 I/F 응답완료!!");
		payPrcDao.updatekpayCrdTrMng(inVo);
		return inOttVo;
	}


}
