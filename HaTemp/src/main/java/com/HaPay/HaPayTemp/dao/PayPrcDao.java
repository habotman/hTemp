/**
 * 
 */
package com.HaPay.HaPayTemp.dao;

import com.HaPay.HaPayTemp.vo.PayPrcVo;

/**
 * @author H
 *
 */
public interface PayPrcDao {

	/**
	 * 
	 * @param inPayPrcVo
	 * @return
	 * @throws Exception
	 */
	public int selectPayCnt(PayPrcVo inPayPrcVo) throws Exception;
	
	
	/**
	 * 
	 * @param inPayPrcVo
	 * @return
	 * @throws Exception
	 */
	public String insertPayment(PayPrcVo inPayPrcVo) throws Exception;
	
	/**
	 * 
	 * @param inPayPrcVo
	 * @return
	 * @throws Exception
	 */
	public int updatekpayCrdTrMng(PayPrcVo inPayPrcVo) throws Exception;
	
	/**
	 * 
	 * @param inPayPrcVo
	 * @return
	 * @throws Exception
	 */
	public int  selectKpayCrdBaseMngCnt(PayPrcVo inPayPrcVo) throws Exception;
	
	/**
	 * 
	 * @param inPayPrcVo
	 * @return
	 * @throws Exception
	 */
	public PayPrcVo selectKpayCrdBaseMng(PayPrcVo inPayPrcVo) throws Exception;
	/**
	 * 
	 * @param inPayPrcVo
	 * @return
	 * @throws Exception
	 */
	public PayPrcVo selectKpayCrdInfo(PayPrcVo inPayPrcVo) throws Exception;
	/**
	 * 
	 * @param inPayPrcVo
	 * @return
	 * @throws Exception
	 */
	public int insertKpayCrdBaseMng(PayPrcVo inPayPrcVo) throws Exception;
	
	/**
	 * 
	 * @param inPayPrcVo
	 * @return
	 * @throws Exception
	 */
	public int insertOttCrdTrmgt(PayPrcVo inPayPrcVo) throws Exception;
	

}
