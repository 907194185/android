package com.gykj.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;




/**
 * MD5编码工具类
 * 
 * @author xianyl
 */
public class DigestUtils {

	/**用于构建 十六进制值**/
	private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static final String ALGORITHM_MD5 = "MD5"; // 定义MD5加密算法

	
	/**
	 * 通过MD5\3des编码
	 * 
	 * @param str
	 * @return String
	 */
	public static String md5FromString(String str) {
		if (str == null) return null;
		return md5FromByte(str.getBytes());
	}
	/**
	 * 通过MD5编码
	 * 
	 * @param str
	 * @return String
	 */
	public static String md5FromByte(byte[] input) {
		if (input == null) return null;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM_MD5);
			messageDigest.update(input);
			return new String(encodeHex(messageDigest.digest()));
		} catch (Exception e) {
			return null;
		}
	}
	
	
	

	/**
	 * 通过SHA-1编码
	 * 
	 * @param str
	 * @return String
	 */
	public static String md5FromFile(File file) {
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			byte[] buffer = new byte[8192];
	        MessageDigest md = MessageDigest.getInstance(ALGORITHM_MD5);
	        int len;
	        while((len = in.read(buffer)) != -1){
	        	md.update(buffer, 0, len);
	        }
	        in.close();
	        //获取文件的md5编码
	        return new String(encodeHex(md.digest()));
		} catch (Exception e) {
			if(in != null){
				try {
					in.close();
				} catch (IOException e1) {
				}
			}
			return null;
		}		
	}

	/**
	 * 将字节数组转换成一个数组的字符代表每个字节的十六进制值。 返回的数组将传递的数组长度的两倍,因为它需要两个字符代表十六进制值。
	 * 
	 * @param data 转换为十六进制字符的字节数组
	 * @return A char[] 包含十六进制字符
	 */
	private static char[] encodeHex(final byte[] data) {
		final int l = data.length;
		final char[] out = new char[l << 1];
		// 两个字符的十六进制值。
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS_LOWER[0x0F & data[i]];
		}
		return out;
	}
	
	
	
	//3des   des 解密
	
	/**
	 * 密钥，長度必須為8的倍數
	 */
	private static final String DES_KEY = "kFR0a_38";
	private static final String DES = "DES";

	private static final String TRIPLE_DES_KEY = "Wiy=@6%$#?!kOPFDRFDG65DO";
	private static final String TRIPLE_DES = "TripleDES";

	//密鑰
	private static final String DES_KEY_FILE = "C:\\des.key";
	private static final String TRIPLE_DES_KEY_FILE = "C:\\tripledes.key";
	public static DESKeySpec    DESKeySpec;
	public static SecretKeySpec tripleDESKeySpec;
	
	//取得DES Key
	public static DESKeySpec getDESKeySpec() 
		throws IOException, NoSuchAlgorithmException, InvalidKeyException {
		//DES 128位
		byte[] bytes = new byte[8];
		File f = new File(DES_KEY_FILE);
		SecretKey key = null;
		DESKeySpec spec = null;
		
		//如果存在Key文件，讀取
		if (f.exists()) {
			new FileInputStream(f).read(bytes);
		} 
		//如果不存在，重新生成Key，并寫入Key文件
		else {
			KeyGenerator kgen = KeyGenerator.getInstance(DES);
			kgen.init(56);
			key = kgen.generateKey();
			bytes = key.getEncoded();
			new FileOutputStream(f).write(bytes);
		}
		
		spec = new DESKeySpec(bytes);
		
		return spec;
  	}
	
	//取得Triple DES Key
	public static SecretKeySpec getTripleDESKeySpec() 
		throws IOException, NoSuchAlgorithmException {
		//Triple DES
		byte[] bytes = new byte[24];
		File f = new File(TRIPLE_DES_KEY_FILE);
		SecretKey key = null;
		SecretKeySpec spec = null;
		
		//如果存在Key文件，讀取
		if (f.exists()) {
			new FileInputStream(f).read(bytes);
		} 
		//如果不存在，重新生成Key，并寫入Key文件
		else {
			KeyGenerator kgen = KeyGenerator.getInstance(TRIPLE_DES);
			kgen.init(168);
			key = kgen.generateKey();
			bytes = key.getEncoded();
			new FileOutputStream(f).write(bytes);
		}
		
		spec = new SecretKeySpec(bytes,TRIPLE_DES);
		
		return spec;
  	}
	
	//字節到十六進制串轉換
	public static String byte2hex(byte[] b){
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n ++){
			stmp = Integer.toHexString(b[n] & 0xFF);
			if (stmp.length() == 1)
				hs += ("0" + stmp);
			else
				hs += stmp;
		}
		return hs.toUpperCase();
	}
	
	//十六進制串到字節轉換
	public static byte[] hex2byte(byte[] b){
		if ((b.length % 2) != 0)
			 throw new IllegalArgumentException("长度不是偶数!");
	  
		byte[] b2 = new byte[b.length / 2];
	  
		for (int n = 0; n < b.length; n += 2){
			String item = new String(b, n, 2);
			b2[n/2] = (byte)Integer.parseInt(item, 16);
		}
		return b2;
	}
	
	//從字符串生成DES Key
	public static DESKeySpec getDESKeySpecFromString(String strKey) 
		throws NoSuchAlgorithmException, InvalidKeyException{
		DESKeySpec spec = new DESKeySpec(strKey.getBytes());
		return spec;
	}
	
	//從十六進制字節串生成DES Key
	public static DESKeySpec getDESKeySpecFromBytes(String strBytes) 
		throws NoSuchAlgorithmException, InvalidKeyException{
		DESKeySpec spec = new DESKeySpec(hex2byte(strBytes.getBytes()));
		return spec;
	}
	
	//從字符串生成DES Key
	public static DESKeySpec getDESKeySpecFromOCString(String strKey) 
		throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException{
		String strKeyString = new String(hex2byte(strKey.getBytes()));
		DESKeySpec spec = new DESKeySpec(strKeyString.getBytes());
		return spec;
	}
	
	//從十六進制字節串生成Triple DES Key
	public static SecretKeySpec getTripleDESKeySpecFromBytes(String strBytes) 
		throws NoSuchAlgorithmException{
		SecretKeySpec spec = new SecretKeySpec(hex2byte(strBytes.getBytes()),TRIPLE_DES);
		return spec;
	}
	
	//從字符串生成Triple DES Key
	public static SecretKeySpec getTripleDESKeySpecFromString(String strKey) 
		throws NoSuchAlgorithmException{
		//SecretKeySpec spec = new SecretKeySpec(strKey.getBytes(),TRIPLE_DES);
		SecretKeySpec spec = new SecretKeySpec(strKey.getBytes(),"DESede");
		return spec;
	}
	
	public static SecretKeySpec getJSTripleDESKeySpecFromString(String strKey) 
		throws NoSuchAlgorithmException{
		SecretKeySpec spec = new SecretKeySpec(strKey.getBytes(),TRIPLE_DES);
		return spec;
	}
	
	//取得DES Key對應的字符串,此為傳給Objective C的密碼字串
	public static String getDESKeyString(){
		return byte2hex(DESKeySpec.getKey());
	}
	
	//取得Triple DES Key對應的字符串,此為傳給Objective C的密碼字串
	public static String getTripleDESKeyString(){
		return byte2hex(tripleDESKeySpec.getEncoded());
	}
	
	/**
	 * 加密
	 * 
	 * @param src
	 *            明文(字节)
	 * @param key
	 *            密钥，长度必须是8的倍数
	 * @return 密文(字节)
	 * @throws Exception
	 */

	public static byte[] encrypt(byte[] src) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();

		// 创建一个密匙工厂，然后用它把DESKeySpec转换成
    	// 一个SecretKey对象
    	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
    	SecretKey securekey = keyFactory.generateSecret(DESKeySpec);
    	
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(DES);
    	//Cipher cipher = Cipher.getInstance("DES/CBC/PKCS7Padding", new BouncyCastleProvider());

		//AlgorithmParameterSpec iv = new IvParameterSpec("abcdefgh".getBytes("UTF-8"));
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

		// 现在，获取数据并加密
		// 正式执行加密操作
		return cipher.doFinal(src);
	}

	public static byte[] tripleDESEncrypt(byte[] src)
			throws Exception {
		//SecureRandom sr = new SecureRandom();
		
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(TRIPLE_DES);

		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, tripleDESKeySpec);

		// 现在，获取数据并加密
		// 正式执行加密操作
		return cipher.doFinal(src);
	}

	/**
	 * 解密
	 * 
	 * @param src
	 *            密文(字节)
	 * @param key
	 *            密钥，长度必须是8的倍数
	 * @return 明文(字节)
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();

		// 创建一个密匙工厂，然后用它把DESKeySpec转换成
    	// 一个SecretKey对象
    	//SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    	SecretKey securekey = keyFactory.generateSecret(DESKeySpec);
    	
		// Cipher对象实际完成解密操作
    	//AlgorithmParameterSpec iv = new IvParameterSpec("abcdefgh".getBytes("UTF-8"));
		//Cipher cipher = Cipher.getInstance("DES/CBC/PKCS7Padding", new BouncyCastleProvider());
    	Cipher cipher = Cipher.getInstance(DES);
    	
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

		// 现在，获取数据并解密
		// 正式执行解密操作
		return cipher.doFinal(src);
	}

	public static byte[] tripleDESDecrypt(byte[] src)
		throws Exception {
		//SecureRandom sr = new SecureRandom();
		
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(TRIPLE_DES);

		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, tripleDESKeySpec);

		// 现在，获取数据并解密
		// 正式执行解密操作
		return cipher.doFinal(src);
	}
	
	/**
	 * 加密
	 * 
	 * @param src
	 *            明文(字符串)
	 * @return 密文(16进制字符串)
	 * @throws Exception
	 */
	public final static String encrypt(String src) {
		try {
			return byte2hex(encrypt(src.getBytes()));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public final static String tripleDESEncrypt(String src) {
		try {
			return byte2hex(tripleDESEncrypt(src.getBytes()));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param src
	 *            密文(字符串)
	 * @return 明文(字符串)
	 * @throws Exception
	 */
	public final static String decrypt(String src) {
		try {
			return new String(decrypt(hex2byte(src.getBytes())));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public final static String tripleDESDecrypt(String src) {
		try {
			return new String(tripleDESDecrypt(hex2byte(src.getBytes())));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// ============================把文件进行解密加密===================================
	public static File encryptFile(File file, String path, boolean isTripleDES) {
		File EncFile = new File(path);
		if (!EncFile.exists()){
			try {
				EncFile.createNewFile();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			FileInputStream fin = new FileInputStream(file);
			ByteArrayOutputStream bout = new ByteArrayOutputStream(fin.available());
			byte b[] = new byte[fin.available()];

			while ((fin.read(b)) != -1) {
				byte temp[];
				if(isTripleDES){
					temp = tripleDESEncrypt(b);
				}
				else{
					temp = encrypt(b);
				}
				bout.write(temp, 0, temp.length);
			}
			fin.close();
			bout.close();
			FileOutputStream fout = new FileOutputStream(EncFile);
			BufferedOutputStream buffout = new BufferedOutputStream(fout);

			buffout.write(bout.toByteArray());
			buffout.close();
			fout.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return EncFile;
	}

	public static File decryptFile(File file, String path, boolean isTripleDES) {
		File desFile = new File(path);
		if (!desFile.exists()){
			try {
				desFile.createNewFile();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			FileInputStream fin = new FileInputStream(file);
			int i = fin.available() - fin.available() % 8;

			ByteArrayOutputStream bout = new ByteArrayOutputStream(i);
			byte b[] = new byte[i];
			
			while (fin.read(b) != -1) {
				byte temp[];
				if (isTripleDES){
					temp = tripleDESDecrypt(b);
				}
				else{
					temp = decrypt(b);
				}
				bout.write(temp, 0, temp.length);
			}

			fin.close();
			bout.close();
			FileOutputStream fout = new FileOutputStream(desFile);
			BufferedOutputStream buffout = new BufferedOutputStream(fout);
			buffout.write(bout.toByteArray());
			buffout.close();
			fout.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		return desFile;
	}

	
	//測試JS生成的DES加密的文本用Java解密
	public static String desDecrypt(String message) {
		String plainText = "";
		try {
			if(DESKeySpec == null)
				DESKeySpec = getDESKeySpecFromString(DES_KEY);
		} catch (NoSuchAlgorithmException e) {
		} catch (InvalidKeyException e) {
		}
		if(DESKeySpec == null) return plainText;
		String cipherText = message.toUpperCase().substring(0,message.length() - 16) + getRandomSuffix();
		plainText = decrypt(cipherText);
		//System.out.println("明文："+plainText);
		return plainText.trim();
	}
	
	//測試JS生成的3DES加密的文本用Java解密
	public static String des3Decrypt(String message) {
		String plainText = "";
		try {
			if(tripleDESKeySpec == null)
				tripleDESKeySpec = getJSTripleDESKeySpecFromString(TRIPLE_DES_KEY);
		} catch (NoSuchAlgorithmException e) {
		}
		if(tripleDESKeySpec == null) return plainText;
		String cipherText = message.toUpperCase().substring(0,message.length() - 16) + get3DESRandomSuffix();
		plainText = tripleDESDecrypt(cipherText);
		//System.out.println("明文："+plainText);
		return plainText.trim();
	}
	
	
	
	//ECB模式
	//因為DES和3DES算法算法，javascript和java加密的結果只有最后十六位不同，
	//javascript可以解密java加密的結果，只是需要剔除最后的不可見字符
	//但是java不能解密javascript的加密結果
	//所以，javascript加密時，明文后補充8個空格，生成密文，
	//java解密時，用java加密8個空字符的密文最后16位替換javascript密文的最后16位
	//最后對解密的密文剔除結尾的空格
	public static String getRandomSuffix(){
		String strRandom = encrypt("        ");
		return strRandom.substring(strRandom.length() - 16);
	}

	public static String get3DESRandomSuffix(){
		String strRandom = tripleDESEncrypt("        ");
		//System.out.println(strRandom);
		return strRandom.substring(strRandom.length() - 16);
	}
	
	
}
