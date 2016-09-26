package com.xpc.imlibrary.util.decryption.aes;


import com.xpc.imlibrary.util.MyLog;
import com.xpc.imlibrary.util.decryption.base64.AESType;
import com.xpc.imlibrary.util.decryption.base64.Base64;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES公共类
 * 
 * @author qiaocb
 * @time 2016-1-14上午11:58:06
 */
public class AESUtils {
	private static String WAYS = "AES";
	private static String MODE = "";
	private static String IV = "1234567890123456";
	private static int AES_SIZE = 128;
	private static int pwdLength = 16;
	private static String defaultPwd = null;
	private static boolean isPwd = false;
	private static String ModeCode = "PKCS5Padding";

	private static int type = 0;// Ĭ��

	/**
	 * 选择模式
	 * @param type
	 * @return
	 */
	public static String selectMod(int type) {
		// ECB("ECB", "0"), CBC("CBC", "1"), CFB("CFB", "2"), OFB("OFB", "3");
		switch (type) {
		case 0:
			isPwd = false;
			MODE = WAYS + "/" + AESType.ECB.key() + "/" + ModeCode;

			break;
		case 1:
			isPwd = true;
			MODE = WAYS + "/" + AESType.CBC.key() + "/" + ModeCode;
			break;
		case 2:
			isPwd = true;
			MODE = WAYS + "/" + AESType.CFB.key() + "/" + ModeCode;
			break;
		case 3:
			isPwd = true;
			MODE = WAYS + "/" + AESType.OFB.key() + "/" + ModeCode;
			break;

		}

		return MODE;

	}

	/**
	 * 转为key
	 * @param password
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static SecretKeySpec toKey(String password)
			throws UnsupportedEncodingException {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance(WAYS);

			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			return new SecretKeySpec(enCodeFormat, WAYS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 加密	
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String content) throws Exception {

		SecretKeySpec skeySpec = toKey(defaultPwd);
		Cipher cipher = Cipher.getInstance(selectMod(type));
		IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
		if (isPwd == false) {
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		} else {
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		}
		byte[] encrypted = cipher.doFinal(content.getBytes());
		return Base64.encode(new String(encrypted));
	}

	/**
	 * 解密
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String content) throws Exception {
		try {
			SecretKeySpec skeySpec = toKey(defaultPwd);
			Cipher cipher = Cipher.getInstance(selectMod(type));
			IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
			if (isPwd == false) {
				cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			} else {
				cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			}
			byte[] encrypted1 = Base64.decode(content, "UTF-8").getBytes();
			try {
				byte[] original = cipher.doFinal(encrypted1);
				String originalString = new String(original);
				return originalString;
			} catch (Exception e) {
				MyLog.e("Error:"+e.getMessage());
				return null;
			}
		} catch (Exception ex) {
			MyLog.e("Error:"+ex.getMessage());
			return null;
		}
	}

	/**
	 * byte转16进制
	 * @param buf
	 * @return
	 */
	private static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase(Locale.getDefault()));
		}
		return sb.toString();
	}

	/**
	 * 16进制转byte
	 * @param hexStr
	 * @return
	 */
	private static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1) {
			return null;
		} else {
			byte[] result = new byte[hexStr.length() / 2];
			for (int i = 0; i < hexStr.length() / 2; i++) {
				int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1),
						16);
				int low = Integer.parseInt(
						hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
				result[i] = (byte) (high * 16 + low);
			}
			return result;
		}
	}
}