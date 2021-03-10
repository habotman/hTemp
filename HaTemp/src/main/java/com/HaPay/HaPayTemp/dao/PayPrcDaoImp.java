/**
 * 
 */
package com.HaPay.HaPayTemp.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.HaPay.HaPayTemp.vo.PayPrcVo;

/**
 * @author H
 *
 */

@Repository
public class PayPrcDaoImp implements PayPrcDao {

	@Autowired
	PayPrcMapper payPrcMapper;
	
	@Override
	public int selectPayCnt(PayPrcVo inPayPrcVo) throws Exception {
		
		int rtnCnt = payPrcMapper.selectPayCnt(inPayPrcVo);
		
		return rtnCnt;
	}


	@Override
	public String insertPayment(PayPrcVo inPayPrcVo) throws Exception {
		
		//void :select key 처리
		payPrcMapper.insertPayment(inPayPrcVo);
		
		// before select 처리
		if(inPayPrcVo.getTlmCrno() != null && !"".equals(inPayPrcVo.getTlmCrno())) {
			return inPayPrcVo.getTlmCrno();
		}else {
			return null;
		}
	}

	@Override
	public int updatekpayCrdTrMng(PayPrcVo inPayPrcVo) throws Exception {
		
		int rtnCnt = payPrcMapper.updatekpayCrdTrMng(inPayPrcVo);
		return rtnCnt;
	}
	
	@Override
	public int selectKpayCrdBaseMngCnt(PayPrcVo inPayPrcVo) throws Exception {
		
		int rtnInt = payPrcMapper.selectKpayCrdBaseMngCnt(inPayPrcVo);
		return rtnInt;
	}
	@Override
	public PayPrcVo selectKpayCrdBaseMng(PayPrcVo inPayPrcVo) throws Exception {
		
		PayPrcVo rtnVo = payPrcMapper.selectKpayCrdBaseMng(inPayPrcVo);
		return rtnVo;
	}
	@Override
	public PayPrcVo selectKpayCrdInfo(PayPrcVo inPayPrcVo) throws Exception {
		
		PayPrcVo rtnVo = payPrcMapper.selectKpayCrdInfo(inPayPrcVo);
//		if(null != rtnVo && null != rtnVo.getCrdno() && !rtnVo.getCrdno().equals("")) {
//
//			String sEncCrdWrt = rtnVo.getEncCrdWrt();
//			sEncCrdWrt = Cipher.decrypt(sEncCrdWrt);
//			rtnVo.setEncCrdWrt(sEncCrdWrt);
//		}
		return rtnVo;
	}
	@Override
	public int insertKpayCrdBaseMng(PayPrcVo inPayPrcVo) throws Exception {

		int rtnCnt = payPrcMapper.insertKpayCrdBaseMng(inPayPrcVo);
		return rtnCnt;
	}
	
	@Override
	public int insertOttCrdTrmgt(PayPrcVo inPayPrcVo) throws Exception {
		int rtnCnt = payPrcMapper.insertOttCrdTrmgt(inPayPrcVo);
		return rtnCnt;
	}


}
