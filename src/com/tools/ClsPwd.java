package com.tools;

import com.clsConst;
import com.ado.SqlADO;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class ClsPwd 
{
	
	public static byte[] MakeKey() {
		int width =16;
		int i=0;
		byte[] key=new byte[width];
		for(i=0;i<width;i++)
		{
			key[i]= (byte)(Math.random()*255);
		}
		return key;
	}
	
	public static byte[] getPublicKey(int mid) 
	{
		return ToolBox.getFromBASE64(SqlADO.getPublicKey(mid));
	}
	
	public static byte[] getPrivateKey(int mid) 
	{
		return ToolBox.getFromBASE64(SqlADO.GetVendLinkKey(mid));
	}
	
	public static void SetPublicKey(byte[] key,int mid) 
	{
		SqlADO.SetVendPublicKey(mid, ToolBox.getBASE64(key));
		
	}
	
	public static void SetPrivateKey(byte[] key,int mid) 
	{
		SqlADO.SetVendLinkKey(mid, ToolBox.getBASE64(key));
	}	
	
	private static int[] NotEncryptCmd=new int[]{clsConst.HTTP_KEY_DATA,
												clsConst.HTTP_UPDATE_GT_FIRM,
												clsConst.HTTP_DOWN_FILE
												};
	
	public static boolean NeedEncrypt(int cmd)
	{
		for (int cmd1 : NotEncryptCmd) {
			if(cmd==cmd1)
			{
				return false;
			}
		}
		return true;
	}
	
}
