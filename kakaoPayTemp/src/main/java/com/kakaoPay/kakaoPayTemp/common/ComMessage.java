package com.kakaoPay.kakaoPayTemp.common;

import java.util.HashMap;
import java.util.Map;

public class ComMessage {
	private Map<String, String> mapMessage = new HashMap<String, String>();
	public ComMessage() {
		mapMessage.put("SUCC", "성공");
		mapMessage.put("FAIL", "실패");

		
		
		mapMessage.put("ERR_01", "오류가 발생하였습니다.");
		//헐... 시간 나면 넣고 빼고 씹고 쓰자.. 
	}

	public String getMessage(String msgCd) {		
		return (String)mapMessage.get(msgCd.toUpperCase());
	}
}
