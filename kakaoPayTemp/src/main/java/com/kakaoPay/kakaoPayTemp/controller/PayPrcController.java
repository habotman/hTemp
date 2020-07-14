package com.kakaoPay.kakaoPayTemp.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kakaoPay.kakaoPayTemp.common.ComUtil;
import com.kakaoPay.kakaoPayTemp.service.PayPrcService;
import com.kakaoPay.kakaoPayTemp.service.PayPrcServiceImpl;
import com.kakaoPay.kakaoPayTemp.vo.PayPrcVo;

/**
 * @author H
 *
 */
@RestController
public class PayPrcController {
	private static final Log log = LogFactory.getLog(PayPrcController.class);
	
	@Autowired
	PayPrcService payPrcServiceImpl;

	@RequestMapping(value = "/payment", method = RequestMethod.POST)
	@ResponseBody
	public String payment(@RequestBody Map<String, Object> param) {
		log.debug("param : " + param.toString());
		
		Map<String, Object> rtnMap = new HashMap<String, Object>()  ;
		PayPrcVo rtnVo = new PayPrcVo();
		try {
			
			/*
				카드번호(10 ~ 16자리 숫자)
				유효기간(4자리 숫자, mmyy)
				cvc(3자리 숫자)
				할부개월수 : 0(일시불), 1 ~ 12
				결제금액(100원 이상, 10억원 이하, 숫자)
			*/
			//json -> map -> vo
			//결제
			
			PayPrcVo inPayPrcVo = new PayPrcVo();
			inPayPrcVo = (PayPrcVo) ComUtil.convertMapToObject(param, inPayPrcVo);
			rtnVo = payPrcServiceImpl.funcPayment(inPayPrcVo);
			rtnMap.put("status", "success");
			rtnMap.put("message", "success");
			rtnMap.put("data", ComUtil.convertObjectToMap(rtnVo));
			
		} catch (Exception e) {
			rtnMap.put("status", "Exception");
			rtnMap.put("message", e.getMessage());
			e.printStackTrace();
		}
		
		return ComUtil.convertToJsonString(rtnMap);
		
	}
	
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	@ResponseBody
	public String cancel(@RequestBody Map<String, Object> param) {
		log.debug("param : " + param.toString());
		
		Map<String, Object> rtnMap = new HashMap<String, Object>()  ;
		PayPrcVo rtnVo = new PayPrcVo();
		try {
			
			/*
				관리번호(unique id, 20자리)
				취소금액
			*/
			
			//취소
			PayPrcVo inPayPrcVo = new PayPrcVo();
			inPayPrcVo = (PayPrcVo) ComUtil.convertMapToObject(param, inPayPrcVo);
			rtnVo = payPrcServiceImpl.funcCancel(inPayPrcVo);
			
			rtnMap.put("status", "success");
			rtnMap.put("message", "success");
			rtnMap.put("data", ComUtil.convertObjectToMap(rtnVo));
			
		} catch (Exception e) {
			rtnMap.put("status", "Exception");
			rtnMap.put("message", e.getMessage());
			e.printStackTrace();
		}
		
		return ComUtil.convertToJsonString(rtnMap);
		
	}
	
	@RequestMapping(value = "/select", method = RequestMethod.POST)
	@ResponseBody
	public String select(@RequestBody Map<String, Object> param) {
		log.debug("param : " + param.toString());
		
		Map<String, Object> rtnMap = new HashMap<String, Object>()  ;
		PayPrcVo rtnVo = new PayPrcVo();
		try {
			/*
				관리번호(unique id, 20자리)
			 */
			
			//취소
			PayPrcVo inPayPrcVo = new PayPrcVo();
			inPayPrcVo = (PayPrcVo) ComUtil.convertMapToObject(param, inPayPrcVo);
			rtnVo = payPrcServiceImpl.funcSelect(inPayPrcVo);
			
			
			rtnMap.put("status", "success");
			rtnMap.put("message", "success");
			rtnMap.put("data", ComUtil.convertObjectToMap(rtnVo));
			
		} catch (Exception e) {
			rtnMap.put("status", "Exception");
			rtnMap.put("message", e.getMessage());
			e.printStackTrace();
		}
		
		return ComUtil.convertToJsonString(rtnMap);
		
	}

//	/**
//	 * Parameter
//	 * - HttpServletRequest에서 Parameter를 꺼내 Map형태로 변경
//	 * @param request
//	 * @return
//	 */
//	private Map<String, Object> buildParameters(HttpServletRequest request) {
//		Enumeration<?> enumeration = request.getParameterNames();      
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//        String key = null;
//        String[] values = null;
//        while (enumeration.hasMoreElements()) {
//            key = (String) enumeration.nextElement();
//            values = request.getParameterValues(key);
//            if (values != null) {
//            	paramMap.put(key, (values.length > 1) ? values : values[0]);
//            	log.debug("["+key+"] : "+((values.length > 1) ? values : values[0])+"");
//            }
//        }
//
//		log.debug("getParameterMap : " + ComUtil.convertToJsonString(paramMap));
//        return paramMap;
//	}
	
	
//
//	/**
//	 * Async 카드결제 요청
//	 * @param code
//	 * @return
//	 */
//	@RequestMapping(value = "/reqSettlement")
//	public @ResponseBody Map<String, Object> requestSettlement(HttpServletRequest req) {
//		Map<?, ?> prmMap = buildParameters(req);
//		
//		String card1 = (String) prmMap.get("card1");
//		String encCard1 = Cipher.encrypt(card1);
//		log.debug("encrypt card1 : " + encCard1);
//		log.debug("decrypt card1 : " + Cipher.decrypt(encCard1));
//		
//		
//		Map<String, Object> hm = new HashMap<>();
//		hm.put("store_id", "9000");
//		hm.put("RTN_CD"  , "SUCC"); 
//		return hm;
//	
//	}
//	
//	
//	/**
//	 * 결제 취소 -진행중
//	 * @param code
//	 * @return
//	 */
//	@RequestMapping(value = "/reqCacel")
//	public @ResponseBody Map<String, Object> requestCancel(HttpServletRequest req) {
//		Map<?, ?> prmMap = req.getParameterMap();
//		log.debug("getParameterMap : " + ComUtil.convertToJsonString(prmMap));
//		 
//		Map<String, Object> hm = new HashMap<>();
//		hm.put("store_id", "9000");
//
//		hm.put("RTN_CD", "SUCC"); 
//		return hm;
//	
//	}
}
