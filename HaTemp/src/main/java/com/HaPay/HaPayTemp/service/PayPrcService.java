package com.HaPay.HaPayTemp.service;

import com.HaPay.HaPayTemp.vo.PayPrcVo;

/**
 * 페이처리 Interface
 * @author H
 *
 */
public interface PayPrcService {

	/**
	 * 결제
	 * @throws Exception
	 */
	public PayPrcVo funcPayment(PayPrcVo inPayPrcVo) throws Exception;
	
	/**
	 * 결제취소
	 * @throws Exception
	 */
	public PayPrcVo funcCancel(PayPrcVo inPayPrcVo) throws Exception;
	
	/**
	 * 데이터조회
	 * @throws Exception
	 */
	public PayPrcVo funcSelect(PayPrcVo inPayPrcVo) throws Exception;

}