package com;

import java.util.Vector;

import com.ado.SqlADO;
import com.tools.ToolBox;


public class clsEvent 
{
	
	
	public static final int GT_EVENT_RESET_KEY = 0;
	public static final int GT_EVENT_UPDATE_PARA = 1;
	public static final int GT_EVENT_UPDATE_FLASH=2;
	
	
	
	public static final int FLG_SET_COLLIST=0;
	public static final int FLG_SET_MDB_DEVICE_DATA=1;
	public static final int FLG_SET_MECHINE_DATA=2;
	public static final int FLG_UPDATE_FLASH=3;
	public static final int FLG_SET_COL_NAME = 4;
	
	
	public static final int GT_EVENT=1;
	public static final int VMC_EVENT=2;
	

	public byte getFunction_flg() {
		return function_flg;
	}

	public void setFunction_flg(byte function_flg) {
		this.function_flg = function_flg;
	}

	public short getGprs_event_flg() {
		return gprs_event_flg;
	}

	public void setGprs_event_flg(short gprs_event_flg) {
		this.gprs_event_flg = gprs_event_flg;
	}

	public int getFlags1() {
		return flags1;
	}

	public void setFlags1(int flags1) {
		this.flags1 = flags1;
	}

	public int getFlags2() {
		return flags2;
	}

	public void setFlags2(int flags2) {
		this.flags2 = flags2;
	}



	private byte function_flg;
	
	private short gprs_event_flg;
	
	private int flags1;
	
	private int flags2;
	
	private int vid;
	private Object object;
	
	private static Vector<clsEvent> lst=null;
	
	
	public clsEvent(int vid) {
		flags1=0;
		flags2=0;
		function_flg=0;
		gprs_event_flg=0;
		object=new Object();
		this.vid=vid;
	}

	public static void ClsEventIni() 
	{
		if(lst==null)
		{
			lst=new Vector<clsEvent>();
		}
		else 
		{
			
		}
	}

	public static clsEvent FindClsEvent(int vid)
	{
		synchronized (lst) {
			for (clsEvent e : lst) 
			{
				if(e.vid==vid)
				{
					return e;
				}
			}
				
			if(SqlADO.getVenderBeanByid(vid)!=null)
			{			
				clsEvent obj=new clsEvent(vid);
				lst.add(obj);
				return obj;
			}
			else 
			{
				return null;
			}
		}
	}
	
	public void WriteVmcEventFlg(int event_id) 
	{
		synchronized (object) {
			if(event_id<32)
			{
				flags1|=1<<event_id;
			}
			else if((event_id>=32)&&(event_id<64))
			{
				flags2|=1<<(event_id-32);
			}
		}
		System.out.print("flags1=");
		ToolBox.printX(flags1);
		System.out.println();
		System.out.print("flags2=");
		ToolBox.printX(flags2);
		System.out.println();
	}
	
	
	
	public void WriteGTEventFlg(int event_id) 
	{
		synchronized (object) {
		gprs_event_flg=(short)(1<<event_id);
		}
	}

	public static void WriteMCFlg(int SellerId, int event_id) {
		clsEvent e= clsEvent.FindClsEvent(SellerId);
		if(e!=null)
		{
			e.WriteVmcEventFlg(event_id);
		}
	}

	public static void ClrEventFlg(int mid,int event_id) 
	{
		clsEvent e= clsEvent.FindClsEvent(mid);
		if(e!=null)
		{
			e.ClrVmcEventFlg(event_id);
		}
	}

	private void ClrVmcEventFlg(int event_id) {
		synchronized (object) {
			if(event_id<32)
			{
				flags1&=~(1<<event_id);
			}
			else if((event_id>=32)&&(event_id<64))
			{
				flags2&=~(1<<(event_id-32));
			}
		}
	}

	public static void WriteGTFlg(int SellerId, int event_id) {
		clsEvent e= clsEvent.FindClsEvent(SellerId);
		if(e!=null)
		{
			e.WriteGtEventFlg(event_id);
		}
	}

	private void WriteGtEventFlg(int event_id) 
	{
		synchronized (object) 
		{
			if(event_id<16)
			{
				gprs_event_flg|=(1<<event_id);
			}
		}
		System.out.print("gprs_event_flg=");
		ToolBox.printX(gprs_event_flg);
		System.out.println();
	}
	
	private void ClrGtEventFlg(int event_id) {
		synchronized (object) 
		{
			if(event_id<16)
			{
				gprs_event_flg&=~(1<<event_id);
			}
		}
	}
	
	public static void ClrGtFlg(int mid,int event_id) 
	{
		clsEvent e= clsEvent.FindClsEvent(mid);
		if(e!=null)
		{
			e.ClrGtEventFlg(event_id);
		}
	}
}
