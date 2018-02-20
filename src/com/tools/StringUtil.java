package com.tools;

import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.http.HttpServletRequest;

import sun.misc.BASE64Decoder;


public class StringUtil {
	public static String getString(String str) {
		if (str == null) {
			str = "";
		} else {
			str = str.trim();
			if (str.equals("null")) {
				str = "";
			}
		}
		return str;
	}

	public static boolean isBlank(String value) {
		return isEmpty(value);
	}

	public static boolean isEmpty(String value) {
		return ((null == value) || (value.length() <= 0));
	}

	public static Integer getRandom() {

		int[] array = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		Random rand = new Random();
		for (int i = 10; i > 1; i--) {
			int index = rand.nextInt(i);
			int tmp = array[index];
			array[index] = array[i - 1];
			array[i - 1] = tmp;
		}
		int result = 0;
		for (int i = 0; i < 6; i++) {
			int num = array[i];
			if (num == 0) {
				num = 1;
			}
			result = (result * 10) + num;
		}
		return result;
	}

	public static Integer getRandomX8() {

		int[] array = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		Random rand = new Random();
		for (int i = 10; i > 1; i--) {
			int index = rand.nextInt(i);
			int tmp = array[index];
			array[index] = array[i - 1];
			array[i - 1] = tmp;
		}
		int result = 0;
		for (int i = 0; i < 8; i++) {
			int num = array[i];
			if (num == 0) {
				num = 1;
			}
			result = (result * 10) + num;
		}
		return result;
	}

	/**
	 * 手机号验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	/**
	 * 电话号码验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isPhone(String str) {
		Pattern p1 = null, p2 = null;
		Matcher m = null;
		boolean b = false;
		p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$"); // 验证带区号的
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
		if (str.length() > 9) {
			m = p1.matcher(str);
			b = m.matches();
		} else {
			m = p2.matcher(str);
			b = m.matches();
		}
		return b;
	}

	/**
	 * 验证输入的邮箱格式是否符合
	 * 
	 * @param email
	 * @return 是否合法
	 */
	public static boolean emailFormat(String email) {
		boolean tag = true;
		final String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		final Pattern pattern = Pattern.compile(pattern1);
		final Matcher mat = pattern.matcher(email);
		if (!mat.find()) {
			tag = false;
		}
		return tag;
	}

	// 客户端的真实ＩＰ
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if ((ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if ((ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if ((ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	// 将 s 进行 BASE64 编码
	public static String getBASE64(byte[] s) {
		if (s == null) {
			return null;
		}
		return (new sun.misc.BASE64Encoder()).encode(s);
	}

	/**
	 * 获取一个字符串的MD5码
	 * 
	 * @author Administrator
	 * @param plainText 源字符串
	 * @return 返回字符串的MD5码
	 * @exception InvalidParameterException
	 */
	public static String getMd5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (byte element : b) {
				i = element;
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			String TemStr = buf.toString().toUpperCase();
			// System.out.println("md5code of "+plainText+":"+TemStr);
			return TemStr;
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	public static String getBASE64(String s) {
		if (s == null) {
			return null;
		}
		try {
			return (new sun.misc.BASE64Encoder()).encode(s.getBytes("GBK"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int String2Integer(String str) {
		try {
			return Integer.parseInt(str);

		} catch (Exception e) {
			return 0;
		}
	}

	// 将 BASE64 编码的字符串 s 进行解码
	public static byte[] getFromBASE64(String s) {
		if (s == null) {
			return null;
		}

		BASE64Decoder decoder = new BASE64Decoder();
		try {
			return decoder.decodeBuffer(s);
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * 补全二位数
	 * 
	 * @param num
	 */
	public static String fillNumX2(int num) {
		String str = String.valueOf(num);
		int length = str.length();
		for (int i = 0; i < (2 - length); i++) {
			str = "0" + str;
		}
		return str;
	}

	/**
	 * 补全四位数
	 * 
	 * @param num
	 */
	public static String fillNumX4(int num) {
		String str = String.valueOf(num);
		int length = str.length();
		for (int i = 0; i < (4 - length); i++) {
			str = "0" + str;
		}
		return str;
	}

	public static void main(String[] args) {
		System.out.println(StringUtil.fillNumX4(3));
		System.out.println(StringUtil.fillNumX4(32));
		System.out.println(StringUtil.fillNumX4(333));
		System.out.println(StringUtil.fillNumX4(4333));
	}

	public static String isStringLegal(String str) throws PatternSyntaxException {
		// 只允许字母和数字
		// String regEx = "[^a-zA-Z0-9]";
		// 清除掉所有特殊字符
		String regEx = "[`~!#$^&*()+=|{}':;',\\[\\].<>/?~！#￥%……&*（）——+|{}【】‘；：”“’。，、？\"]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}
}
