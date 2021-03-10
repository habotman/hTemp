package com.HaPay.HaPayTemp.controller;

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

import com.HaPay.HaPayTemp.common.ComUtil;
import com.HaPay.HaPayTemp.service.PayPrcService;
import com.HaPay.HaPayTemp.vo.PayPrcVo;

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
		
		log.debug("@RestController : payment Start ================");
		log.debug("param : " + param.toString());
		
		Map<String, Object> rtnMap = new HashMap<String, Object>()  ;
		PayPrcVo rtnVo = new PayPrcVo();
		try {
			
			/**
				required
				카드번호(10 ~ 16자리 숫자)
				유효기간(4자리 숫자, mmyy)
				cvc(3자리 숫자)
				할부개월수 : 0(일시불), 1 ~ 12
				결제금액(100원 이상, 10억원 이하, 숫자)
				optional
				부가가치세 
			 */
			
			PayPrcVo inPayPrcVo = new PayPrcVo();
			inPayPrcVo = (PayPrcVo) ComUtil.convertMapToObject(param, inPayPrcVo);
			rtnVo = payPrcServiceImpl.funcPayment(inPayPrcVo);
			rtnMap.put("status", "SUCC");
			rtnMap.put("message", ComUtil.getMessage("SUCC"));
			rtnMap.put("data", ComUtil.convertObjectToMap(rtnVo));
			
		} catch (Exception e) {
			rtnMap.put("status", "FAIL");
			rtnMap.put("message", ComUtil.getMessage("FAIL"));
			rtnMap.put("data", e.getMessage());
			e.printStackTrace();
		}
		
		log.debug("rtnMap : " + ComUtil.convertToJsonString(rtnMap).toString());
		log.debug("@RestController : payment end ================");
		
		return ComUtil.convertToJsonString(rtnMap);
		
	}
	
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	@ResponseBody
	public String cancel(@RequestBody Map<String, Object> param) {
		log.debug("@RestController : cancel Start ================");
		log.debug("param : " + param.toString());
		
		Map<String, Object> rtnMap = new HashMap<String, Object>()  ;
		PayPrcVo rtnVo = new PayPrcVo();
		try {
			
			/**
				required
				관리번호(unique id, 20자리)
				취소금액
				optional
				부가가치세
			 */
			
			//취소
			PayPrcVo inPayPrcVo = new PayPrcVo();
			inPayPrcVo = (PayPrcVo) ComUtil.convertMapToObject(param, inPayPrcVo);
			rtnVo = payPrcServiceImpl.funcCancel(inPayPrcVo);
			
			rtnMap.put("status", "SUCC");
			rtnMap.put("message", ComUtil.getMessage("SUCC"));
			rtnMap.put("data", ComUtil.convertObjectToMap(rtnVo));
			
		} catch (Exception e) {
			rtnMap.put("status", "FAIL");
			rtnMap.put("message", ComUtil.getMessage("FAIL"));
			rtnMap.put("data", e.getMessage());
			e.printStackTrace();
		}
		
		log.debug("rtnMap : " + ComUtil.convertToJsonString(rtnMap).toString());
		log.debug("@RestController : cancel end ================");
		
		return ComUtil.convertToJsonString(rtnMap);
		
	}
	
	@RequestMapping(value = "/select", method = RequestMethod.POST)
	@ResponseBody
	public String select(@RequestBody Map<String, Object> param) {
		log.debug("@RestController : select Start ================");
		log.debug("param : " + param.toString());
		
		Map<String, Object> rtnMap = new HashMap<String, Object>()  ;
		PayPrcVo rtnVo = new PayPrcVo();
		try {
			
			/**
				required
				관리번호(unique id, 20자리)
			 */
			
			//취소
			PayPrcVo inPayPrcVo = new PayPrcVo();
			inPayPrcVo = (PayPrcVo) ComUtil.convertMapToObject(param, inPayPrcVo);
			rtnVo = payPrcServiceImpl.funcSelect(inPayPrcVo);
			
			rtnMap.put("status", "SUCC");
			rtnMap.put("message", ComUtil.getMessage("SUCC"));
			rtnMap.put("data", ComUtil.convertObjectToMap(rtnVo));
			
		} catch (Exception e) {
			rtnMap.put("status", "FAIL");
			rtnMap.put("message", ComUtil.getMessage("FAIL"));
			rtnMap.put("data", e.getMessage());
			e.printStackTrace();
		}
		
		log.debug("rtnMap : " + ComUtil.convertToJsonString(rtnMap).toString());
		log.debug("@RestController : select end ================");
		
		return ComUtil.convertToJsonString(rtnMap);
		
	}
	
}
