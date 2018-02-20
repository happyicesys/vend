package com;

import java.util.ArrayList;
import java.util.Iterator;


public class clsFid {

	private static ArrayList<clsFid> fidlst=new ArrayList<clsFid>();
	/*记录消息存活期
	 * 
	 * 当收到一条消息时，首先检测是否在一定时间内收到过这个ID号，
	 * 如果收到过，表明这是终端重复发送的信息，
	 * 那么如果是上传数据，则该内容可以不用处理，以免重复存储，直接回复OK即可
	 * 
	 * 如果是下载内容，则表明终端收取数据有误，或者根本没有收到数据，则必须重发数据，回复终端
	 * 
	 * */
	
	private int Mid;
	private int Fid;
	
	private long timestamp;
	
	private final static int TTL=2*60*1000; 
	
	
	
	public clsFid(int mid2, int fid2) {
		Mid=mid2;
		Fid=fid2;
		timestamp=System.currentTimeMillis();
	}

	public static boolean ChkFidExist(int mid,int fid) 
	{
		boolean is_exist=false;
		
		for (Iterator<clsFid> it2 = fidlst.iterator();it2.hasNext();)
		{ 
			clsFid f =it2.next(); 
			if(f!=null)
			{
				if((System.currentTimeMillis()-f.timestamp<TTL))
				{				
					if((f.Fid==fid)&&(f.Mid==mid))
					{
						is_exist=true;
						break;
					}
				}
				else
				{
					it2.remove();
				}
			}
        } 

		if(!is_exist)
		{
			clsFid fid2=new clsFid(mid,fid);
			fid2.addFid();
		}
		return is_exist;
	}
	
	private void addFid() {
		fidlst.add(this);
	}
}
