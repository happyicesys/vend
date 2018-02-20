package com;


import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import com.tools.ToolBox;

public class ClsTime 
{
	//public final static tim
	
	private static final int OBJ_SIZE = 0;

	public static long SystemTime()
	{
		return ToolBox.LocalTime();
	}
	
	public static byte[] ToBytes() 
	{
		Calendar c=Calendar.getInstance();
		c.setTimeInMillis(ToolBox.LocalTime());
		
		byte[] b=new byte[clsConst.TIME_OBJ_SIZE]; 
		int i=0;
		
		i=ToolBox.short2bytes_little(b, i, (short)c.get(Calendar.YEAR));/*年*/
		
		b[i++]=(byte)c.get(Calendar.MONTH);/*月*/
		
		b[i++]=(byte)c.get(Calendar.DAY_OF_MONTH);/*日*/
		
		b[i++]=(byte)c.get(Calendar.HOUR_OF_DAY);/*时*/
		
		b[i++]=(byte)c.get(Calendar.MINUTE);/*分*/
		
		b[i++]=(byte)c.get(Calendar.SECOND);/*秒*/
		
		b[i++]=(byte)c.get(Calendar.DAY_OF_WEEK);/*星期*/
		return b;
	}
}
