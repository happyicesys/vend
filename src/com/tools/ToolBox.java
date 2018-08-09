package com.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.zip.CRC32;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.naming.java.javaURLContextFactory;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.omg.IOP.Encoding;

import com.ClsTime;
import com.clsConst;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.xml.internal.security.keys.content.RetrievalMethod;

import beans.clsGoodsBean;
import sun.awt.AWTCharset.Encoder;
import sun.misc.BASE64Decoder;
import sun.misc.CRC16;
import sun.security.krb5.internal.crypto.Crc32CksumType;
import sun.security.provider.SecureRandom;

public class ToolBox {
	

	
	public final static String HOMEPAGE="MainHome.jsp";
	public final static String VERSTRING="Happy Ice Vending";
	public final static String WEB_NAME=VERSTRING;
	
	public final static String NOLOGIN="<script>alert(\"Please login again!\");parent.location.href=\"index.jsp\";</script>";
	public final static String GUESTUSER="<script>alert(\"Your access level is insufficient, please contact admin!\");history.go(-1);</script>";
	public final static String CANNTACCESS="Access Denied.";
	public final static String ERRPARAMETER="<script>alert(\"Parameters Error!\");history.go(-1);</script>";
	public final static String CANNTLOOK="<script>alert(\"Your access level is insufficient!\");history.go(-1);</script>";
	public final static String CANNTMODIFY="<script>alert(\"Your access level is insufficient!\");history.go(-1);</script>";
	public final static String USERNOTEXSIT="<script type=\"text/javascript\">alert(\"Your credential is incorrect, please try again\");location.href='index.jsp';</script>";
	public final static String USERDENY="<script type=\"text/javascript\">alert(\"Your access has been denied by admin\");location.href='index.jsp';</script>";
	public final static String EMPTY="<script type=\"text/javascript\">alert(\"Please fill in both username and password\");location.href='index.jsp';</script>";
	public final static String ERR_CHKCODE="<script type=\"text/javascript\">alert(\"Verification code is incorrect\");location.href='index.jsp';</script>";
	



	public static String ErrRequest(String errmes) {
		StringBuilder out=new StringBuilder();
		
		out.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.append("<HTML>");
		out.append("  <HEAD>");
		out.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
		out.append("<TITLE>");
		out.append(WEB_NAME);

		out.append("</TITLE><link href=\"./css/styles.css\" rel=\"stylesheet\" type=\"text/css\" /></HEAD>");
		out.append("  <BODY>");
		out.append("<div class=\"exitbox\">There's an error<br>");
		out.append(errmes);	
		out.append("<a href=\"javascript:void(0);\" onclick=\"history.go(-1);\">[Back]</a></div>");
		out.append("  </BODY>");
		out.append("</HTML>");

		return out.toString();
	}

	public static String TipRequest(String Tipmes) {
		StringBuilder out=new StringBuilder();
		
		out.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.append("<HTML>");
		out.append("  <HEAD>");
		out.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
		out.append("<TITLE>");
		out.append(WEB_NAME);

		out.append("</TITLE><link href=\"./css/styles.css\" rel=\"stylesheet\" type=\"text/css\" /></HEAD>");
		out.append("  <BODY>");
		out.append("<div class=\"exitbox\">Completed<br>");
		out.append(Tipmes);	
		out.append("<br><a href=\"javascript:void(0);\" onclick=\"history.go(-1);\">[Back]</a></div>");
		out.append("  </BODY>");
		out.append("</HTML>");

		return out.toString();
	}
	
	
	/**
     * 字符串转换成十六进制值
     * @param bin String 我们看到的要转换成十六进制的字符串
     * @return 
     */
    public static String bin2hex(String bin) {
        char[] digital = "0123456789ABCDEF".toCharArray();
        StringBuffer sb = new StringBuffer("");
        byte[] bs = bin.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(digital[bit]);
            bit = bs[i] & 0x0f;
            sb.append(digital[bit]);
        }
        return sb.toString();
    }

    /**
     * 十六进制转换字符串
     * @param hex String 十六进制
     * @return String 转换后的字符串
     */
    public static String hex2bin(String hex) {
        String digital = "0123456789ABCDEF";
        char[] hex2char = hex.toCharArray();
        byte[] bytes = new byte[hex.length() / 2];
        int temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = digital.indexOf(hex2char[2 * i]) * 16;
            temp += digital.indexOf(hex2char[2 * i + 1]);
            bytes[i] = (byte) (temp & 0xff);
        }
        return new String(bytes);
    }

    /** 
     * java字节码转字符串 
     * @param b 
     * @return 
     */
    public static String byte2hex(byte[] b) { //一个字节的数，

        // 转成16进制字符串

        String hs = "";
        String tmp = "";
        for (int n = 0; n < b.length; n++) {
            //整数转成十六进制表示

            tmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (tmp.length() == 1) {
                hs = hs + "0" + tmp;
            } else {
                hs = hs + tmp;
            }
        }
        tmp = null;
        return hs.toUpperCase(); //转成大写

    }

    /**
     * 字符串转java字节码
     * @param b
     * @return
     */
    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节

            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        b = null;
        return b2;
    }
	
	public static String getYMD(Timestamp dt)
	{
		return String.format("%1$tF",dt);
	}
	public static String getYMD(java.util.Date  dt)
	{
		return String.format("%1$tF",dt);
	}
	
	public static String getMD(java.util.Date dt)
	{
		return (new java.text.SimpleDateFormat("MM-dd")).format(dt);
	}
	
	public static String getD(java.util.Date dt)
	{
		return (new java.text.SimpleDateFormat("dd")).format(dt);
	}	
	
	public static String getHMS(Timestamp dt)
	{
		return String.format("%1$tT",dt);
	}

	public static String getYMDHMS(Timestamp dt)
	{
		return String.format("%1$tF %1$tT",dt);
	}
	public static String getYMDHM(Timestamp dt)
	{
		return String.format("%1$tF %1$tH:%1$tM",dt);
	}
	
	public static String filter(String str)
	{
		if(str==null) return "";
		String tem=str.trim();
		tem=tem.toUpperCase();
		if(tem.contains("SELECT ")||tem.contains("SELECT ")||tem.contains("INSERT ")||tem.contains("CREATE ")||tem.contains("WHERE ")||tem.contains("UPDATE "))
		{
			tem=tem.replaceAll("SELECT", "");
			tem=tem.replaceAll("INSERT", "");
			tem=tem.replaceAll("CREATE", "");
			tem=tem.replaceAll("WHERE", "");
			tem=tem.replaceAll("UPDATE", "");
			tem=tem.replaceAll("<", "");
			tem=tem.replaceAll("=", "");
			return tem;
		}
		else
		{
			return str.trim();
		}

	}
	public static int filterInt(String str)
	{
		try
		{
			return Integer.parseInt(filter(str));
		}
		catch (NumberFormatException e)
		{
			return 0;
		}
	}
	public static int filterHexInt(String hexstr)
	{
		try
		{
			return Integer.parseInt(filter(hexstr));
		}
		catch (NumberFormatException e)
		{
			return 0;
		}
	}
	public static float filterFlt(String str)
	{
		try
		{
			return Float.parseFloat(filter(str));
		}
		catch (NumberFormatException e)
		{
			return 0;
		}
	}
	public static double filterDble(String str)
	{
		try
		{
			return Double.parseDouble(filter(str));
		}
		catch (NumberFormatException e)
		{
			return 0;
		}
	}
	
	public static boolean filterBol(String str)
	{
		return Boolean.parseBoolean(filter(str));
	}	
	public static Timestamp filterTime(String str)
	{
		try
		{
			return Timestamp.valueOf(filter(str));
		}
		catch(Exception e)
		{
			return null;
		}
	}	
	public static Date filteDate(String str)
	{
		try
		{
			return Date.valueOf(filter(str));
		}
		catch(Exception e)
		{
			return null;
		}
	}		

	public static Date addDate(Date ori,int amount)
	{
		Calendar c=Calendar.getInstance();
		c.setTime(ori);
		c.add(Calendar.DAY_OF_MONTH, amount);
		
		return new Date(c.getTimeInMillis());
	}
	

	
	public static boolean isInt(String str)
	{
		return str.matches(("-?\\d{1,9}"));
	}

	/**
	 * 取得时间字符串，形如：20090831150534
	 * @param 无
	 * @return 返回形如"yyyyMMddHHmmss"的字符串
	 */	
	public static String getTimeString()
	{
		return getTimeString(new java.util.Date());
	}
	public static String getShortTimeString()
	{
		return getShortTimeString(new java.util.Date());
	}	
	/**
	 * 取得时间字符串，形如：20090831150534
	 * @param 无
	 * @return 返回形如"yyyyMMddHHmmss"的字符串
	 */	
	public static String getTimeString(java.util.Date date)
	{
		String TemString=null; 
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		TimeZone timezone = TimeZone.getTimeZone("Asia/Shanghai"); 
		sdf.setTimeZone(timezone);
		TemString = sdf.format(date);
		return TemString;
	}
	public static String getShortTimeString(java.util.Date date)
	{
		String TemString=null; 
		SimpleDateFormat sdf=new SimpleDateFormat("MMddHHmmss");
		TimeZone timezone = TimeZone.getTimeZone("Asia/Shanghai"); 
		sdf.setTimeZone(timezone);
		TemString = sdf.format(date);
		return TemString;
	}
	
	public static String getShortStdDateString(java.util.Date date)
	{
		String TemString=null; 
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		TimeZone timezone = TimeZone.getTimeZone("Asia/Shanghai"); 
		sdf.setTimeZone(timezone);
		TemString = sdf.format(date);
		return TemString;
	}
	/**
	 * 取得时间字符串，形如：20090831150534
	 * @param 无
	 * @return 返回形如"yyyyMMddHHmmss"的字符串
	 */	
	public static String getTimeLongString(java.util.Date date)
	{
		String TemString=null; 
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TimeZone timezone = TimeZone.getTimeZone("Asia/Shanghai"); 
		sdf.setTimeZone(timezone);
		TemString = sdf.format(date);
		return TemString;
	}
	
	public static String getTimeLongString(Timestamp date)
	{
		if(date==null)
		{
			return "";
		}
		String TemString=null; 
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TimeZone timezone = TimeZone.getTimeZone("Asia/Shanghai"); 
		sdf.setTimeZone(timezone);
		TemString = sdf.format(date);
		return TemString;
	}
	
	public static String getTimeString(Timestamp date)
	{
		if(date==null)
		{
			return "";
		}
		String TemString=null; 
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		TimeZone timezone = TimeZone.getTimeZone("Asia/Shanghai"); 
		sdf.setTimeZone(timezone);
		TemString = sdf.format(date);
		return TemString;
	}
	
	/**
	 * 取得日期字符串，形如：2009-8-31
	 * @param 无
	 * @return
	 * 日期字符串，形如：2009-8-31
	 */
	public static String getDateString()
	{
		String TemString=null; 
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		TimeZone timezone = TimeZone.getTimeZone("Asia/Shanghai"); 
		sdf.setTimeZone(timezone);
		TemString = sdf.format(new java.util.Date());
		return TemString;
	}
	

	
	public final static int TIME_OFFSET=0;
	public static long LocalTime()
	{
		return System.currentTimeMillis()+TIME_OFFSET;
	}
	/**
	 * 取得日期时间字符串，形如：2009-8-31 16:11:03
	 * @param 无
	 * @return
	 * 日期时间字符串，形如：2009-08-31 16:11:03
	 */	
	public static String getDateTimeString()
	{
		String TemString=null; 
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//TimeZone timezone = TimeZone.getTimeZone("Asia/Shanghai"); 
		//sdf.setTimeZone(timezone);
		TemString = sdf.format(new java.util.Date(LocalTime()));
		return TemString;
	}

	/**
	 * @return
	 * <br/>20090912
	 */
	public static String  getDateStr()
	{
		String TemString=null; 
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		TimeZone timezone = TimeZone.getTimeZone("Asia/Shanghai"); 
		sdf.setTimeZone(timezone);
		TemString = sdf.format(new java.util.Date(LocalTime()));
		return TemString;	
	}
	
	/**
	 * @return
	 * <br/>150956
	 */
	public static String  getTimeStr()
	{
		String TemString=null; 
		SimpleDateFormat sdf=new SimpleDateFormat("HHmmss");
		TimeZone timezone = TimeZone.getTimeZone("Asia/Shanghai"); 
		sdf.setTimeZone(timezone);
		TemString = sdf.format(new java.util.Date(LocalTime()));
		return TemString;	
		
	}	
	
	/**
	 * 检查一个byte数组是否全为0
	 * @author Administrator
	 *@param arr_byt，一个字节数组
	 *@return 如果全为0，返回false，否则，返回true
	 */
	public static boolean chkValid(byte[] arr_byt)
	{
		int i;
		for(i=0;i<arr_byt.length;i++)
		{
			if(arr_byt[i]!=0)return true;
		}
		return false;
	}
	/**
	 * 检查一个byte数组是否包含与第二个参数完全匹配的子数组
	 * @author Administrator
	 *@param arr_byt_Parent
	 *		 母数组
	 @param arr_byt_Sub
	 *		子数组
	 *
	 *@return 如果存在，返回第一个相匹配的子数组的索引（从0开始），否则，返回-1
	 */
	public static int getSubArrIndex(byte[] arr_byt_Parent,byte[] arr_byt_Sub)
	{
		int i,j;
		for(i=0;i<=arr_byt_Parent.length-arr_byt_Sub.length;i++)
		{
			for(j=0;j<arr_byt_Sub.length;j++)
			{
				if(arr_byt_Parent[i+j]!=arr_byt_Sub[j])
				{
					break;
				}
			}
			if (j==arr_byt_Sub.length) return i;
		}
		return -1;
	}
	/**
	 * 检查一个byte数组段（指定开始索引和结束索引）是否包含与第二个参数完全匹配的子数组
	 * 当结束索引大于可查询的索引时，结束索引等于最大的可查询索引
	 * @author Administrator
	 *@param arr_byt_Parent
	 *		 母数组
	 *@param arr_byt_Sub
	 *		 子数组
	 *@param int_StartIndex
	 *		 开始索引
	 *@param int_EndIndex 
	 *		 结束索引
	 *@return 如果存在，返回第一个相匹配的子数组的索引（从0开始），否则，返回-1
	 *@exception InvalidParameterException
	 */
	public static int getSubArrIndex(byte[] arr_byt_Parent,
			                         byte[] arr_byt_Sub,
			                         int int_StartIndex,
			                         int int_EndIndex)
	{
		if (int_StartIndex>int_EndIndex)
		{
			throw new InvalidParameterException("开始位置大于结束位置！");
		}
		if(int_EndIndex>arr_byt_Parent.length-1)
		{
			int_EndIndex=arr_byt_Parent.length-1;
		}
		int i,j;
		for(i=int_StartIndex;i<=int_EndIndex-arr_byt_Sub.length+1;i++)
		{
			for(j=0;j<arr_byt_Sub.length;j++)//比较每一个字符
			{
				if(arr_byt_Parent[i+j]!=arr_byt_Sub[j])
				{
					break;
				}
			}
			if (j==arr_byt_Sub.length) return i;
		}
		return -1;
	}
	
	/**
	 * 获取一个字符串的MD5码
	 * @author Administrator
	 *@param plainText 源字符串
	 *@return 返回字符串的MD5码
	 *@exception InvalidParameterException
	 */
	public static String getMd5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i+= 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			String TemStr=buf.toString().toUpperCase();
			//System.out.println("md5code of "+plainText+":"+TemStr);
			return TemStr;
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	/**
	 * 获取交易流水号
	 * @param SellerId
	 * @return
	 * 返回交易流水号 形如：090903104822009XXXX，其中009时售货机编号，最后为4位随机码
	 */
	public static String MakeTradeID(int SellerId)
	{
		return  getTimeString()+String.format("%06d", SellerId)+String.format("%06d", getRandomNumber());
	}
	
	public static String MakeTradeID(int SellerId,int rnd)
	{
		return  getTimeString()+String.format("%03d", SellerId)+String.format("%06d", rnd);
	}
	public static String MakeOutTradeID(int SellerId)
	{
		return  getTimeString()+String.format("%04d", SellerId)+String.format("%06d", getRandomNumber());
	}
	
	/**
	* 字符串转换成日期
	* @param str
	* @return date
	*/
	public static java.util.Date StrToDate(String str) {
	  
	   SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	   java.util.Date  date = null;
	   try {
	    date = format.parse(str);
	   } catch (ParseException e) {
	    e.printStackTrace();
	   }
	   return date;
	}
	
	/**
	 * @return
	 * 返回报文头流水号的源，是一个整形值
	 */
	public static int MakeFlowID()
	{
		int TemRnd=(int)(Math.random()*1000);
		return (int)(ClsTime.SystemTime()+TemRnd);
	}
	
	public static int MakeRandom()
	{
		return (int)(Math.random()*1000);
		
	}
	
	
	/**
	 * @return
	 * 返回RPID；形如090903104822XXXX
	 */
	public static String MakeRPID()
	{
		String TemStr=String.format("%04d",(int)(Math.random()*10000));
		return getTimeString()+TemStr;
	}
	
	public static String MakeRPID(int machineid,int slotid)
	{
		String TemStr=String.format("%02d%05d%04d",(int)(Math.random()*100),machineid,slotid);
		return getTimeString()+TemStr;
	}
	
	/**
	 * @param temobj
	 * @return
	 * 返回数组的全部内容
	 */
	public static String ByteToString(byte[] temobj) 
	{
		return Arrays.toString(temobj);
	}
	
	/**
	 * @param temobj
	 * @return
	 * 返回数组的全部内容，每个字节都转化为两个字符
	 * 1A2B3C
	 */
	public static String Byte2String(byte[] b) {

		if(b==null)
		{
			return null;
		}
		String str=null;		
		for (byte c : b) {
			str+=String.format("%2X", c);
		}
		return str;
	}
	
	/**
	 * 获取0~999999之间的一个整数
	 * @return
	 * 一个不超过999999的整数
	 */
	public static int getRandomNumber()
	{
		return (int)(Math.random()*1000000);
	}
	
	/**
	 * 将源数组追加到目标数组
	 * @param Sou1：<br>原数组1
	 * @param Sou2：<br>原数组2
	 * @return:<br>返回一个新的数组，包括了原数组1和原数组2
	 */
	public static byte[] ArrayAppend(byte[] Sou1,byte[] Sou2)
	{
		return ArrayAppend(Sou1,Sou1.length,Sou2,Sou2.length);
	}
	
	/**
	 * 将源数组追加到目标数组,需要指定两个数组将要转移的字节的长度
	 * <br>
	 * @param Sou1
	 * @param len1
	 * @param Sou2
	 * @param len2
	 * @return
	 */
	public static byte[] ArrayAppend(byte[] Sou1,int len1,byte[] Sou2,int len2)
	{
		if (Sou2.length==0) 
			throw new ArrayIndexOutOfBoundsException("The array is empty!");
		if (Sou1!=null)
		{
			if (len1<1)
			{
				len1=Sou1.length;
			}
		}
		else
		{
			len1=0;
		}
		if (len2==0)
		{
			throw new InvalidParameterException("The Second Array can't be empty!");
		}
		byte[] temByte=new byte[len1 +len2];
		if(Sou1!=null)
		{
			System.arraycopy(Sou1, 0, temByte, 0, len1);
		}
		System.arraycopy(Sou2, 0, temByte, len1, len2);		
		return temByte;
	}
	
	/**
	 * 带开关的输出设备
	 * @param Str
	 */
	public static void write(String Str,boolean on)
	{
		if(on)
		{
			System.out.println(Str);
		}
	}
	/**
	 * 带开关的输出设备
	 * @param Str
	 */
	public static void write(int Str,boolean on)
	{
		if(on)
		{
			System.out.println(Str);
		}
	}	
	/**
	 * 带开关的输出设备
	 * @param Str
	 */
	public static  void write(long Str,boolean on)
	{
		if(on)
		{
			System.out.println(Str);
		}
	}
	
	public static int arrbyteToint_big(byte[] a,int startIndex,int len)
	{
		if (a==null) return 0;
		if(len==0) return 0;
		if(a.length<startIndex+len)
		{
			return 0;
		}	
		int tem=0;
		for(int i=startIndex;i<startIndex+len;i++)
		{
			tem<<=8;
			tem|=a[i]&0xff;
		}
		return tem;
	}
	
	public static int arrbyteToint_Little(byte[] a,int startIndex,int len)
	{
		if (a==null) return 0;
		if(len==0) return 0;
		if(a.length<startIndex+len)
		{
			return 0;
		}	
		int tem=0;
		for(int i=startIndex+len-1;i>=startIndex;i--)
		{
			tem<<=8;
			tem|=a[i]&0xff;
		}
		return tem;
	}	
	
	public static int short2bytes_little(byte[] b,Integer index,short temi) 
	{
		b[index++]=(byte)temi;
		b[index++]=(byte)(temi>>8);
		return index;
	}
	public static int int2bytes_little(byte[] b,int index,int temi) 
	{
		int i;
		for(i=0;i<4;i++)
		{
			b[index++]=(byte)temi;
			temi>>=8;
		}
		return index;
	}
	
	public static short[] parseStrToShort(String str)
	{
		String[] a=str.split(",",0);
		short[] b=new short[a.length];
		for(int i=0;i<a.length ;i++)
		{
			if(isInt(a[i]))
			{
				b[i]=Short.parseShort(a[i]);
			}
			else
			{
				b[i]=0;
			}
		}
		return b;
	}
	
	public static int parseHexStringToInt(String str)
	{
		try {
			return Integer.parseInt(str, 16);
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static short parseHexStringToShort(String str)
	{
		return (short)parseHexStringToInt(str);
	}
	
	
	public static int[] parseStrToInt(String str)
	{
		String[] a=str.split(",",0);
		int[] b=new int[a.length];
		for(int i=0;i<a.length ;i++)
		{
			if(isInt(a[i]))
			{
				b[i]=Short.parseShort(a[i]);
			}else
			{
				b[i]=0;
			}
		}
		return b;
	}
	
	public static int findItem(short[] arr,short key)
	{
		for(int i=0;i<arr.length;i++)
		{
			if(arr[i]==key)
				return i;
		}
		return -1;
	}
	
	

	public static String iso8859toutf8(String parameter) {

		try {
			byte[] b;
			b = parameter.getBytes("ISO-8859-1");
			String addr=new String(b,"UTF-8");	
			return addr;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String GBKtoutf8(String parameter) {

		try {
			byte[] b;
			b = parameter.getBytes("GBK");
			
			String addr=new String(b,"UTF-8");	
			return addr;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}	
	
	public static String getpages(String urlpar,String acolor,int page,int pagecount,int rscount)
	{
		int totalpages;
		String url;
		totalpages=(rscount-1+pagecount)/pagecount;
	
		if(page > totalpages)
		{
			page = totalpages;
		}
		
		
		if(urlpar==null) 
		{
			url="?page=";
		}
		else
		{
			url='?' + urlpar + "&page=";
		}
	
		String pagestr = "Total <font color=\"#FF0000\">" + rscount + "</font> entries&nbsp;|&nbsp;";
		String First = "Main"; 
		String Previous = "Prev"; 
		String Next = "Next"; 
		String Last = "Last";
		String Records = "<font color=\"#FF0000\">" + pagecount + "</font> entries/ page&nbsp;|&nbsp;";
		String sDpage = "Total <font color=\"#FF0000\">" + totalpages + "</font> page(s)\n";
		sDpage += "<select name=\"spage\" id=\"spage\" OnChange=\"window.open(this.options[this.selectedIndex].value,\'_self\')\">\n";
		for (int i=1;i<=totalpages;i++)
		{
			if (i!=page){
				sDpage += "<option value=" +url + i +">" +i + " page</option>\n";
			}else{
				sDpage += "<option selected value='"+url +i +"'>" + i + " page</option>\n";
			}
		}
		sDpage += "</select>";
	
		if(page <= 1) 
		{
			pagestr += "<font color=\"" + acolor + "\">" + First + "</font>&nbsp;|&nbsp;" ;
		}
		else
		{
		    pagestr += "<a href=\"" + url + "1\">" + First + "</a>&nbsp;|&nbsp;\n"; 
		}
		
		if(page <= 1)
		{
			pagestr += "<font color=\"" + acolor + "\">" + Previous + "</font>&nbsp;|&nbsp;";
		}
		else
		{
			pagestr += "<a href=\"" + url + (page-1) + "\">" + Previous + "</a>&nbsp;|&nbsp;\n"; 
		}
		
		if(page >= totalpages)
		{
			pagestr += "<font color=\"" + acolor + "\">" + Next + "</font>&nbsp;|&nbsp;";
		}
		else
		{
			pagestr += "<a href=\"" + url + (page+1) + "\">" + Next + "</a>&nbsp;|&nbsp;\n"; 
		}
		if(page >= totalpages )
		{
			pagestr += "<font color=\"" + acolor + "\">" + Last + "</font>&nbsp;|&nbsp;";
		}else
		{
			pagestr += "<a href=\"" + url + totalpages + "\">" + Last + "</a>&nbsp;|&nbsp;\n";
		}
	
		pagestr += Records + sDpage;
		return pagestr;
	}
	

	      
	    public static String getCRC32(String str){   
	        int[] table = {   
	        0x00000000, 0x77073096, 0xee0e612c, 0x990951ba, 0x076dc419, 0x706af48f, 0xe963a535, 0x9e6495a3,   
	        0x0edb8832, 0x79dcb8a4, 0xe0d5e91e, 0x97d2d988, 0x09b64c2b, 0x7eb17cbd, 0xe7b82d07, 0x90bf1d91,   
	        0x1db71064, 0x6ab020f2, 0xf3b97148, 0x84be41de, 0x1adad47d, 0x6ddde4eb, 0xf4d4b551, 0x83d385c7,   
	        0x136c9856, 0x646ba8c0, 0xfd62f97a, 0x8a65c9ec, 0x14015c4f, 0x63066cd9, 0xfa0f3d63, 0x8d080df5,   
	        0x3b6e20c8, 0x4c69105e, 0xd56041e4, 0xa2677172, 0x3c03e4d1, 0x4b04d447, 0xd20d85fd, 0xa50ab56b,   
	        0x35b5a8fa, 0x42b2986c, 0xdbbbc9d6, 0xacbcf940, 0x32d86ce3, 0x45df5c75, 0xdcd60dcf, 0xabd13d59,   
	        0x26d930ac, 0x51de003a, 0xc8d75180, 0xbfd06116, 0x21b4f4b5, 0x56b3c423, 0xcfba9599, 0xb8bda50f,   
	        0x2802b89e, 0x5f058808, 0xc60cd9b2, 0xb10be924, 0x2f6f7c87, 0x58684c11, 0xc1611dab, 0xb6662d3d,   
	        0x76dc4190, 0x01db7106, 0x98d220bc, 0xefd5102a, 0x71b18589, 0x06b6b51f, 0x9fbfe4a5, 0xe8b8d433,   
	        0x7807c9a2, 0x0f00f934, 0x9609a88e, 0xe10e9818, 0x7f6a0dbb, 0x086d3d2d, 0x91646c97, 0xe6635c01,   
	        0x6b6b51f4, 0x1c6c6162, 0x856530d8, 0xf262004e, 0x6c0695ed, 0x1b01a57b, 0x8208f4c1, 0xf50fc457,   
	        0x65b0d9c6, 0x12b7e950, 0x8bbeb8ea, 0xfcb9887c, 0x62dd1ddf, 0x15da2d49, 0x8cd37cf3, 0xfbd44c65,   
	        0x4db26158, 0x3ab551ce, 0xa3bc0074, 0xd4bb30e2, 0x4adfa541, 0x3dd895d7, 0xa4d1c46d, 0xd3d6f4fb,   
	        0x4369e96a, 0x346ed9fc, 0xad678846, 0xda60b8d0, 0x44042d73, 0x33031de5, 0xaa0a4c5f, 0xdd0d7cc9,   
	        0x5005713c, 0x270241aa, 0xbe0b1010, 0xc90c2086, 0x5768b525, 0x206f85b3, 0xb966d409, 0xce61e49f,   
	        0x5edef90e, 0x29d9c998, 0xb0d09822, 0xc7d7a8b4, 0x59b33d17, 0x2eb40d81, 0xb7bd5c3b, 0xc0ba6cad,   
	        0xedb88320, 0x9abfb3b6, 0x03b6e20c, 0x74b1d29a, 0xead54739, 0x9dd277af, 0x04db2615, 0x73dc1683,   
	        0xe3630b12, 0x94643b84, 0x0d6d6a3e, 0x7a6a5aa8, 0xe40ecf0b, 0x9309ff9d, 0x0a00ae27, 0x7d079eb1,   
	        0xf00f9344, 0x8708a3d2, 0x1e01f268, 0x6906c2fe, 0xf762575d, 0x806567cb, 0x196c3671, 0x6e6b06e7,   
	        0xfed41b76, 0x89d32be0, 0x10da7a5a, 0x67dd4acc, 0xf9b9df6f, 0x8ebeeff9, 0x17b7be43, 0x60b08ed5,   
	        0xd6d6a3e8, 0xa1d1937e, 0x38d8c2c4, 0x4fdff252, 0xd1bb67f1, 0xa6bc5767, 0x3fb506dd, 0x48b2364b,   
	        0xd80d2bda, 0xaf0a1b4c, 0x36034af6, 0x41047a60, 0xdf60efc3, 0xa867df55, 0x316e8eef, 0x4669be79,   
	        0xcb61b38c, 0xbc66831a, 0x256fd2a0, 0x5268e236, 0xcc0c7795, 0xbb0b4703, 0x220216b9, 0x5505262f,   
	        0xc5ba3bbe, 0xb2bd0b28, 0x2bb45a92, 0x5cb36a04, 0xc2d7ffa7, 0xb5d0cf31, 0x2cd99e8b, 0x5bdeae1d,   
	        0x9b64c2b0, 0xec63f226, 0x756aa39c, 0x026d930a, 0x9c0906a9, 0xeb0e363f, 0x72076785, 0x05005713,   
	        0x95bf4a82, 0xe2b87a14, 0x7bb12bae, 0x0cb61b38, 0x92d28e9b, 0xe5d5be0d, 0x7cdcefb7, 0x0bdbdf21,   
	        0x86d3d2d4, 0xf1d4e242, 0x68ddb3f8, 0x1fda836e, 0x81be16cd, 0xf6b9265b, 0x6fb077e1, 0x18b74777,   
	        0x88085ae6, 0xff0f6a70, 0x66063bca, 0x11010b5c, 0x8f659eff, 0xf862ae69, 0x616bffd3, 0x166ccf45,   
	        0xa00ae278, 0xd70dd2ee, 0x4e048354, 0x3903b3c2, 0xa7672661, 0xd06016f7, 0x4969474d, 0x3e6e77db,   
	        0xaed16a4a, 0xd9d65adc, 0x40df0b66, 0x37d83bf0, 0xa9bcae53, 0xdebb9ec5, 0x47b2cf7f, 0x30b5ffe9,   
	        0xbdbdf21c, 0xcabac28a, 0x53b39330, 0x24b4a3a6, 0xbad03605, 0xcdd70693, 0x54de5729, 0x23d967bf,   
	        0xb3667a2e, 0xc4614ab8, 0x5d681b02, 0x2a6f2b94, 0xb40bbe37, 0xc30c8ea1, 0x5a05df1b, 0x2d02ef8d,   
	        };   
	        byte[] bytes = str.getBytes();   
	        int crc = 0xffffffff;   
	        for (byte b : bytes) {   
	        crc = (crc >>>8 ^ table[(crc ^ b) & 0xff]);   
	        System.out.println(Integer.toHexString(crc));
	        }   
	        crc = crc ^ 0xffffffff;   
	        return Integer.toHexString(crc);   
	    }  
	        public static void main2(String[] args) {   
	              
	            String str = "ksdjfkasjdfiwr;fdshfj;lhsadjfhkjadshfliwu';fhkjadshfjsadgf";  
	            System.out.println("CRC32:"+getCRC32(str));  
	              
	            java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();  
	            crc32.update(str.getBytes());  
	            System.out.println("CRC32:"+Long.toHexString(crc32.getValue()));  
	        }  
	
	
	public static void main(String[] args) 
	{
//		//byte[] in="12321".getBytes();
//		byte[] in=new byte[]{45,89,34,44,2,4,6,8,3,12,2,3,45,56,5,67,89,34,44,2,4,6,8,3,12};
//		byte[] out;
//		
//		byte[] out2;
//		
//		//byte[] key=new byte[]{(byte)0XC3, (byte)0XF9, (byte)0XDB ,0X76 ,0X15 ,(byte)0XF2 ,0X07 ,0X49 ,0X07, 
//				//0X57 ,0X06 ,0X4E ,0X05 ,(byte)0X8C ,0X59 ,(byte)0XBC};
//		//byte[] key="@ABCDEFGHIJKLMNO".getBytes();
//		//byte[] key=new byte[]{0x43,0x42,0x41,0x40,0x47,0x46,0x45,0x44,0x4b,0x4a,0x49,0x48,0x4f,0x4e,0x4d,0x4c};
//		//out=AES_Encode(in, key);
//		
		
		//java.util.Date d= StrToDate("20141030133525");
		//System.out.println(ToolBox.getTimeLongString(d));
//eyJUeXBlIjoiVkVOREVSIiwiVmlkIjoxMDAxLCJDSEdFU3RhdCI6MCwiQklMTFN0YXQiOjMsIklSRXJyQ250IjowLCJMc3RTbHRFIjowLCJDb2luQ250IjozNTAwMCwiQmxsQ250IjoyMzAwMCwiVmVyIjozOTJ9
		//byte[] file=ToolBox.getFromBASE64("eyJUeXBlIjoiVkVOREVSIiwiVmlkIjoxLCJDSEdFU3RhdCI6MCwiQklMTFN0YXQiOjAsIklSRXJyQ250IjowLCJMc3RTbHRFIjowLCJDb2luQ250IjowLCJWZXIiOjM4Nn0=");

//		byte[] file=ToolBox.getFromBASE64("eyJUeXBlIjoiVkVOREVSIiwiVmlkIjoxMDAxLCJDSEdFU3RhdCI6MCwiQklMTFN0YXQiOjMsIklSRXJyQ250IjowLCJMc3RTbHRFIjowLCJDb2luQ250IjozNTAwMCwiQmxsQ250IjoyMzAwMCwiVmVyIjozOTJ9");
//		//byte[] file=readFileByRandomAccess("c:\\clean.txt",0,1024);
//		
//		printX(file,file.length);
		
//		byte b=(byte)-4;
//		
//		System.out.println(b&0xFF);
		
//		byte[] b=new byte[]{0x31,0x32,0x33,0x34,'2','0','0','8','0','8','0','8','1','2','1','0','1','2',0x35,0x36,0x37,0x38,0x31,0x32,0x31,0x32,0x31,0x35,0};
//		
//		System.out.println(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//		try {
//			String str=AscByte2StringByEndIndex(b, 4, 4+15);
//			System.out.println(str);
//			java.util.Date date = sdf.parse(str);
//			Calendar c=Calendar.getInstance();
//			c.setTimeInMillis(date.getTime());
//			System.out.println(getTimeLongString(date));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		ArrayList<clsGoodsBean> lst = clsGoodsBean.getGoodsBeanLst(2, 50, 3);
		
		for(clsGoodsBean gb :lst)
		{
			System.out.println(gb.getId()+" "+gb.getGoodsname());
		}
		
	}

	/**
	 * 将一个asc数据流转化成一个字符串
	 * @param b
	 * @param start_index
	 * @param end_index
	 * @return
	 */
	public static String AscByte2StringByEndIndex(byte[] b,int start_index,int end_index)
	{
		if(b==null)
		{
			return "";
		}
		
		try {
			return new String(b,start_index,end_index-start_index,"GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return "";
	}
	
    public static byte[] encrypt2(byte[] content, byte[] password) {  
        try {  
                SecretKeySpec key = new SecretKeySpec(password, "AES");  
                Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");  
                byte[] byteContent = content;  
                
                IvParameterSpec ivSpec = new IvParameterSpec("0000000000000000".getBytes());
                try {
					cipher.init(Cipher.ENCRYPT_MODE, key,ivSpec);
				} catch (InvalidAlgorithmParameterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 初始化  
                
                byte[] result = cipher.doFinal(byteContent);  
                return result; // 加密  
        } catch (NoSuchAlgorithmException e) {  
                e.printStackTrace();  
        } catch (NoSuchPaddingException e) {  
                e.printStackTrace();  
        } catch (InvalidKeyException e) {  
                e.printStackTrace();  
        } catch (IllegalBlockSizeException e) {  
                e.printStackTrace();  
        } catch (BadPaddingException e) {  
                e.printStackTrace();  
        }  
        return null;  
    } 
	
    public static byte[] decrypt2(byte[] content, byte[] password) {  
        try {  
                SecretKeySpec key = new SecretKeySpec(password, "AES");  
                Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");  
                byte[] byteContent = content;  
                
                IvParameterSpec ivSpec = new IvParameterSpec("0000000000000000".getBytes());
                try {
					cipher.init(Cipher.DECRYPT_MODE, key,ivSpec);
				} catch (InvalidAlgorithmParameterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 初始化  
                
                byte[] result = cipher.doFinal(byteContent);  
                return result; // 加密  
        } catch (NoSuchAlgorithmException e) {  
                e.printStackTrace();  
        } catch (NoSuchPaddingException e) {  
                e.printStackTrace();  
        } catch (InvalidKeyException e) {  
                e.printStackTrace();  
        } catch (IllegalBlockSizeException e) {  
                e.printStackTrace();  
        } catch (BadPaddingException e) {  
                e.printStackTrace();  
        }  
        return null;  
    } 
	
	public static short String2Short(String str) 
	{
		try {
			return Short.parseShort(str);
			
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static int String2Integer(String str) 
	{
		try {
			return Integer.parseInt(str);
			
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static double String2Double(String str) 
	{
		try {
			return Double.parseDouble(str);
			
		} catch (Exception e) {
			return 0.0;
		}
	}
	
	public static int[] StringToIntArray(String str) {
			if(str==null) return null;
			if(str.trim()=="") return null;
			
			String[] bstr=str.split(",", 0);
			int[] b= new int[bstr.length];
			int i;
			for(i=0;i<bstr.length;i++)
			{
				b[i]=String2Integer(bstr[i]);
			}
			return b;

	}
	// 将 s 进行 BASE64 编码 
	public static String getBASE64(byte[] s) 
	{ 
	   if (s == null)
	   {
	   		return null; 
	   }
	   return (new sun.misc.BASE64Encoder()).encode(s); 
	} 
	
	public static String getBASE64(String s) 
	{ 
		if (s == null)
		{
			return null; 
		}
		try 
		{
			return (new sun.misc.BASE64Encoder()).encode(s.getBytes("GBK"));
		} catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
			return null;
		} 
	} 

	// 将 BASE64 编码的字符串 s 进行解码 
	public static byte[] getFromBASE64(String s) 
	{ 
	   if (s == null) 
		{
		   return null; 
		}
	   
	   BASE64Decoder decoder = new BASE64Decoder(); 
	   try 
	   { 
	      return decoder.decodeBuffer(s); 
	   } 
	   catch (Exception e) 
	   { 
	      return null; 
	   } 
	}
	
	public static String getStringFromBASE64(String s) 
	{ 
		try {
			return new String( getFromBASE64(s),"GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	public static void printX(short x)
	{
		System.out.print(String.format("0X%04X ",x));
	}
	
	public static void printX(int x)
	{
		System.out.print(String.format("0X%08X,",x));
	}
	public static void printX(byte x)
	{
		System.out.print(String.format("0X%02X,",x));
	}
	
	public static void printX(byte[] b,int len) {
		int i;
		for(i=0;i<len;i++)
		{
			printX(b[i]);
		}
		System.out.println();
	}
	

    /**
     * 加密
     * 
     * @param content 需要加密的内容
     * @param password  加密密码
     * @return
     */
	private static byte[] encrypt(byte[] byteContent, byte[] password) {
    	if(byteContent==null)
    	{
    		return null;
    	}
            try {           
                    KeyGenerator kgen = KeyGenerator.getInstance("AES");
                    kgen.init(128, new java.security.SecureRandom(password));
                    SecretKey secretKey = kgen.generateKey();
                    byte[] enCodeFormat = secretKey.getEncoded();
                    SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
                    Cipher cipher = Cipher.getInstance("AES");// 创建密码器
                    cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
                    byte[] result = cipher.doFinal(byteContent);
                    return result; // 加密
            } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
            } catch (InvalidKeyException e) {
                    e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
            } catch (BadPaddingException e) {
                    e.printStackTrace();
            }
            return null;
    }
    
	public static byte[] AES_Encode(byte[] ming,byte[] key) 
	{
		if(ming==null)
		{
			return null;
		}
    	int length=ming.length+4;
    	length=(length+15)/16;
    	length*=16;
    	byte[] content=new byte[length];
    	int i=0;
    	content[i++]=clsConst.START_SIGN1;
    	content[i++]=clsConst.START_SIGN2;
		i=short2bytes_little(content, i, (short)ming.length);
		System.arraycopy(ming,0,content,i,ming.length);
		return encrypt2(content, key);
	}
	
	public static byte[] AES_Decode(byte[] mi,byte[] key) throws Exception 
	{
		byte[] content=null;
		if (mi==null) 
		{
			return content;
		}
		byte[] ret=decrypt2(mi, key);
		if(ret!=null)
		{
			printX(ret, ret.length);
			int i=0;
			if((ret[i]==clsConst.START_SIGN1)&&(ret[i+1]==clsConst.START_SIGN2))
			{
				i+=2;
				int length=arrbyteToint_Little(ret, i, 2);
				if(length==0)
				{
					
				}
				else if(length<=clsConst.MAX_DATA_LENGTH)
				{
					i+=2;
					content=new byte[length];
					System.arraycopy(ret,i,content,0,length);
				}
				else 
				{
					throw new Exception("Data length too big!");
				}
			}
			else
			{
				throw new Exception("Data Invalid!");
			}
		}
		return content;
	}
    
    
    /**解密
     * @param content  待解密内容
     * @param password 解密密钥
     * @return
     */
    private static byte[] decrypt(byte[] content, byte[] password) {
    	if(content==null)
    	{
    		return null;
    	}

            try {
                     KeyGenerator kgen = KeyGenerator.getInstance("AES");
                     kgen.init(128, new java.security.SecureRandom(password));
                     SecretKey secretKey = kgen.generateKey();
                     byte[] enCodeFormat = secretKey.getEncoded();
                     SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");            
                     Cipher cipher = Cipher.getInstance("AES");// 创建密码器
                    cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
                    byte[] result = cipher.doFinal(content);
                    return result; // 加密
            } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
            } catch (InvalidKeyException e) {
                    e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
            } catch (BadPaddingException e) {
                    e.printStackTrace();
            } 
            return null;
    }


	
    public static byte[] readFileByRandomAccess(String fileName,int block,int block_size) 
    {
        RandomAccessFile randomFile = null;
        byte[] bytes=null;
        try {
            //System.out.println("随机读取一段文件内容：");
            // 打开一个随机访问文件流，按只读方式
            randomFile = new RandomAccessFile(fileName, "r");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 读文件的起始位置
            int beginIndex = block*block_size;
            // 将读文件的开始位置移到beginIndex位置。
            randomFile.seek(beginIndex);
            if(fileLength-beginIndex>=block_size)
            {
            	bytes = new byte[block_size];
            }
            else
            {
            	bytes = new byte[(int)(fileLength-beginIndex)];
            }
            if((randomFile.read(bytes)) == -1) 
            {
            	bytes= null;
            }
        } catch (IOException e) 
        {
            e.printStackTrace();
        	bytes= null;
        } finally 
        {
            if (randomFile != null) 
            {
                try 
                {
                    randomFile.close();
                } catch (IOException e1) 
                {
                	
                }
            }
        }
        return bytes;
    }

	public static byte[] AES_Encode(String content, byte[] key,String ccode) {
		// TODO Auto-generated method stub
		try {
			return AES_Encode(content.getBytes(ccode), key);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*获取CRC32校验以及文件长度*/
	public static byte[] getFileInfo(String gt_bin_filename) {
		byte[] b=new byte[8];
		File file=new File(gt_bin_filename);
		int len=(int)file.length();
		byte[] f_b=new byte[len];
		int i=0;
		FileInputStream fs=null;
		try {
			fs=new FileInputStream(file);
			fs.read(f_b);
			CRC32 crc32=new CRC32();
			crc32.reset();
			crc32.update(f_b);
			i=int2bytes_little(b, i,len);
			i=int2bytes_little(b, i, (int)crc32.getValue());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			if(fs!=null)
			{
				try {
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return b;
	}

	public static String IntArr2String(int[] arr) {
		String tem="";
		int i=0;
		if(arr!=null)
		{
			for( i=0;i<arr.length-1;i++)
			{
				tem+=arr[i]+",";
			}
			tem+=arr[i];
			
		}
		
		return tem;
	}
	
	public static String AscByte2StringTerminalByNull(byte[] b,int start_index)
	{
		if(b==null)
		{
			return "";
		}
		int end_index=start_index;
		for(;end_index<b.length;end_index++)
		{
			if(b[end_index]==0)
			{
				break;
			}
		}
		
		try {
			return new String(b,start_index,end_index-start_index,"GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
}
