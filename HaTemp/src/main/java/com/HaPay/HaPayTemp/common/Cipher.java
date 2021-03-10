package com.HaPay.HaPayTemp.common;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class Cipher {

	/** 암복화 엔진 */
	private static StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
	/** 암복화 방식 */
	private static final String _ALGORITHM = "PBEWithMD5AndDES";
	/** 패스워드 */
	private static final String _PASSWORD = "PASS0078!";

	/**
	 * 암호화
	 * @param origingStr
	 * @return
	 */
	public static String encrypt(String origingStr) {
		if(!encryptor.isInitialized()) {// 초기화 안된경우
			encryptor.setAlgorithm(_ALGORITHM);
			encryptor.setPassword(_PASSWORD); 
		} 
		return encryptor.encrypt(origingStr);
	}

	/**
	 * 복호화
	 * @param encStr
	 * @return
	 */
	public static String decrypt(String encStr) {
		if(!encryptor.isInitialized()) {// 초기화 안된경우
			encryptor.setAlgorithm(_ALGORITHM);
			encryptor.setPassword(_PASSWORD); 
		}  
		return encryptor.decrypt(encStr);
	}

}
