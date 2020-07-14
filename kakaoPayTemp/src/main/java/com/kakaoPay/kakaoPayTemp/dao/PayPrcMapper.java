package com.kakaoPay.kakaoPayTemp.dao;

import org.apache.ibatis.annotations.Mapper;

import com.kakaoPay.kakaoPayTemp.vo.PayPrcVo;
 

@Mapper
public interface PayPrcMapper {

	
	int selectPayCnt(PayPrcVo inPayPrcVo) throws Exception;
 	void insertPayment(PayPrcVo inPayPrcVo) throws Exception;
	int updatekpayCrdTrMng(PayPrcVo inPayPrcVo) throws Exception;

	int selectKpayCrdBaseMngCnt(PayPrcVo inPayPrcVo) throws Exception;
	PayPrcVo selectKpayCrdBaseMng(PayPrcVo inPayPrcVo) throws Exception;
	PayPrcVo selectKpayCrdInfo(PayPrcVo inPayPrcVo) throws Exception;
	int insertKpayCrdBaseMng(PayPrcVo inPayPrcVo) throws Exception;
	
	int insertOttCrdTrmgt(PayPrcVo inPayPrcVo) throws Exception;
	

}
