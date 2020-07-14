package com.kakaoPay.kakaoPayTemp.common;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ComUtil {

	private static ObjectMapper objMapper = new ObjectMapper();

	private static ComMessage comMessage = new ComMessage();
 
	public ComUtil() {
		
	}
	
	/**
	 * get Error Message
	 * @param errCd
	 * @return
	 */
	public static String getMessage(String errCd) {
		return (String)comMessage.getMessage(errCd);
	}

	/**
	 * Convert Object to JSON Formatted String
	 * @param obj
	 * @return
	 */
	public static String convertToJsonString(Object obj) {
		String jsonString = "";
		try {
			jsonString = objMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (Exception e) {
			System.err.println(e);
		}
		
		return jsonString;
	}

	/**
	 * Convert JSON String to Map
	 * @param strJson
	 * @return
	 */
	public static Map<String, Object> convertToMap(String strJson) {
		Map<String, Object> convertedMap = null;
		try {
			convertedMap = objMapper.readValue(strJson, Map.class);
 
		} catch (Exception e) {
			System.err.println(e);
		}
		
		return convertedMap;
	}
	
	/**
	 * Convert JSON String to User Class Type
	 * @param strJson
	 * @return
	 */
	public static Object convertToClass(String strJson, Class valueType) {
		Object convertedObj = null;
		try {
			convertedObj = objMapper.readValue(strJson, valueType);
 
		} catch (Exception e) {
			System.err.println(e);
		}
		
		return convertedObj;
	}
	
	/**
	* Map을 Vo로 변환
	* @param map
	* @param obj
	* @return
	*/
		public static Object convertMapToObject(Map<?, ?> map, Object objClass){
			String keyAttribute = null;
			String setMethodString = "set";
			String methodString = null;
			Iterator<?> itr = map.keySet().iterator();
			while(itr.hasNext()){
				keyAttribute = (String) itr.next();
				methodString = setMethodString+keyAttribute.substring(0,1).toUpperCase()+keyAttribute.substring(1);
				try {
					Method[] methods = objClass.getClass().getDeclaredMethods();
					for(int i=0;i<=methods.length-1;i++){
						if(methodString.equals(methods[i].getName())){
							System.out.println("invoke : "+methodString);
							methods[i].invoke(objClass, map.get(keyAttribute));
						}
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			return objClass;
		}


	
	/**
	* Vo를 Map으로 변환
	* @param obj
	* @return
	*/
	public static Map<String, Object> convertObjectToMap(Object obj){
	    Map<String, Object> map = new HashMap<String, Object>();
	    Field[] fields = obj.getClass().getDeclaredFields();
	    for(int i=0; i <fields.length; i++){
	        fields[i].setAccessible(true);
	        try{
	        	if(fields[i].get(obj) != null && !"".equals(fields[i].get(obj))) {
	        		map.put(fields[i].getName(), fields[i].get(obj));
	        	}
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	    }
	    return map;
	}


	/**
	 * 문자열 Left Padding처리
	 * @param inStr
	 * @param size 
	 * @return
	 */
	public static String leftPad(String inStr, int size) {
		return leftPad(inStr, size, "");
	}
	/**
	 * 문자열 Left Padding처리
	 * @param inStr
	 * @param size
	 * @param padStr
	 * @return
	 */
	public static String leftPad(String inStr, int size, String padStr) {
		String rtnStr = StringUtils.isEmpty(inStr) ? "" : inStr.trim();
		rtnStr = StringUtils.leftPad(rtnStr, size, StringUtils.isEmpty(padStr) ? "" : padStr.trim());
		return rtnStr;
	}
	
	/**
	 * 문자열 Right Padding처리
	 * @param inStr
	 * @param size 
	 * @return
	 */
	public static String rightPad(String inStr, int size) {
		return rightPad(inStr, size, "");
	}
	/**
	 * 문자열 Right Padding처리
	 * @param inStr
	 * @param size
	 * @param padStr
	 * @return
	 */
	public static String rightPad(String inStr, int size, String padStr) {
		String rtnStr = StringUtils.isEmpty(inStr) ? "" : inStr.trim();	 
		rtnStr = StringUtils.rightPad(rtnStr, size, StringUtils.isEmpty(padStr) ? "" : padStr.trim());
		return rtnStr;
	}
	
}
