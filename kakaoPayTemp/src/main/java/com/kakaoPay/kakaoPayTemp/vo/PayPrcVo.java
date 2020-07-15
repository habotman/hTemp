package com.kakaoPay.kakaoPayTemp.vo;

import org.springframework.stereotype.Component;

@Component
public class PayPrcVo {
	
	
	String tlmCrno ; 	 //관리번호
	String wrtdt ; 	 //	작성일자
	String recSeqno ;	 //	레코드순번
	String dataLen ;	 //	데이터길이
	String dataFlgcd ;	 //	결제/취소구분 -데이터구분: PAYMENT/CANCEL
	String crdno ;	 //	카드번호
	String atMnt ;	 //	할부개월
	String crdLimt ;	 //	카드번호유효기간
	String cvc ;	 //	CVC
	String trAmt ;	 //	거래금액
	String steAmt ;	 //	부가가치세
	String gbTrMgtno ;	 //	원거래관리번호
	String encCrdWrt ;	 //	암호화된카드정보
	String ectWrt ;	 //	예비필드
	String inpUsrId ;	 //	입력사용자ID
	String inpDthms ;	 //	입력일시
	String mdfUsrId ;	 //	수정사용자ID
	String mdfDthms ;	 //	수정일시
	
	String dataDtl ; //	데이터내용
	
	String tlmIfFlg ; // 거래관리상태구분 : I:입력(내부관리상태), C:성공, F:실패 
	String tlmIfMsg ; // 관리정보 (비고) 메세지
	
	String atrAmt ; //총결제금액
	String asteAmt; //총부가가치세금액

	String testCd ;
	
//	
//	/** 아무 매개변수 없이 default값을 생성해주는 생성자 */
//    public  PayPrcVo(){
//      this.trAmt = "0";
//      //this.steAmt = "0";
//     }


	public String getTlmCrno() {
		return tlmCrno;
	}


	public void setTlmCrno(String tlmCrno) {
		this.tlmCrno = tlmCrno;
	}


	public String getWrtdt() {
		return wrtdt;
	}


	public void setWrtdt(String wrtdt) {
		this.wrtdt = wrtdt;
	}


	public String getRecSeqno() {
		return recSeqno;
	}


	public void setRecSeqno(String recSeqno) {
		this.recSeqno = recSeqno;
	}


	public String getDataLen() {
		return dataLen;
	}


	public void setDataLen(String dataLen) {
		this.dataLen = dataLen;
	}


	public String getDataFlgcd() {
		return dataFlgcd;
	}


	public void setDataFlgcd(String dataFlgcd) {
		this.dataFlgcd = dataFlgcd;
	}


	public String getCrdno() {
		return crdno;
	}


	public void setCrdno(String crdno) {
		this.crdno = crdno;
	}


	public String getAtMnt() {
		return atMnt;
	}


	public void setAtMnt(String atMnt) {
		this.atMnt = atMnt;
	}


	public String getCrdLimt() {
		return crdLimt;
	}


	public void setCrdLimt(String crdLimt) {
		this.crdLimt = crdLimt;
	}


	public String getCvc() {
		return cvc;
	}


	public void setCvc(String cvc) {
		this.cvc = cvc;
	}


	public String getTrAmt() {
		return trAmt;
	}


	public void setTrAmt(String trAmt) {
		this.trAmt = trAmt;
	}


	public String getSteAmt() {
		return steAmt;
	}


	public void setSteAmt(String steAmt) {
		this.steAmt = steAmt;
	}


	public String getGbTrMgtno() {
		return gbTrMgtno;
	}


	public void setGbTrMgtno(String gbTrMgtno) {
		this.gbTrMgtno = gbTrMgtno;
	}


	public String getEncCrdWrt() {
		return encCrdWrt;
	}


	public void setEncCrdWrt(String encCrdWrt) {
		this.encCrdWrt = encCrdWrt;
	}


	public String getEctWrt() {
		return ectWrt;
	}


	public void setEctWrt(String ectWrt) {
		this.ectWrt = ectWrt;
	}


	public String getInpUsrId() {
		return inpUsrId;
	}


	public void setInpUsrId(String inpUsrId) {
		this.inpUsrId = inpUsrId;
	}


	public String getInpDthms() {
		return inpDthms;
	}


	public void setInpDthms(String inpDthms) {
		this.inpDthms = inpDthms;
	}


	public String getMdfUsrId() {
		return mdfUsrId;
	}


	public void setMdfUsrId(String mdfUsrId) {
		this.mdfUsrId = mdfUsrId;
	}


	public String getMdfDthms() {
		return mdfDthms;
	}


	public void setMdfDthms(String mdfDthms) {
		this.mdfDthms = mdfDthms;
	}


	public String getDataDtl() {
		return dataDtl;
	}


	public void setDataDtl(String dataDtl) {
		this.dataDtl = dataDtl;
	}


	public String getTlmIfFlg() {
		return tlmIfFlg;
	}


	public void setTlmIfFlg(String tlmIfFlg) {
		this.tlmIfFlg = tlmIfFlg;
	}


	public String getTlmIfMsg() {
		return tlmIfMsg;
	}


	public void setTlmIfMsg(String tlmIfMsg) {
		this.tlmIfMsg = tlmIfMsg;
	}


	public String getAtrAmt() {
		return atrAmt;
	}


	public void setAtrAmt(String atrAmt) {
		this.atrAmt = atrAmt;
	}


	public String getAsteAmt() {
		return asteAmt;
	}


	public void setAsteAmt(String asteAmt) {
		this.asteAmt = asteAmt;
	}


	public String getTestCd() {
		return testCd;
	}


	public void setTestCd(String testCd) {
		this.testCd = testCd;
	}
 
    



}
