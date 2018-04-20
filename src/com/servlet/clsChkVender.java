package com.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.TextMsgSendProcess;
import com.clsConst;
import com.clsEvent;
import com.ado.SqlADO;
import com.alipay.AlipayQrcode;
import com.alipay.AlipayRefund;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.brian.sendmail.SampleMail;
import com.tools.ToolBox;

import beans.CustomerBean;
import beans.PortBean;
import beans.TradeBean;
import beans.UserBean;
import beans.VenderBean;
import beans.clsFeeBackParaBean;
import sun.awt.windows.ThemeReader;
import weixin.popular.bean.clsWxPara;
import weixin.popular.util.PayUtil;
import weixin.popular.util.WxCoporTransfor;
import weixin.popular.util.WxRefund;


/**
 * Servlet implementation class clsChkVender
 */
public class clsChkVender extends HttpServlet {

	private static final long serialVersionUID = 1L;
       
	
	public void init() throws ServletException {

		if(MyThread.instance==null)
		{
			clsEvent.ClsEventIni();
			MyThread.instance=new MyThread();
			MyThread.instance.start();
			
		}
		
		TextMsgSendProcess sendProcess=new TextMsgSendProcess();
		sendProcess.start();
		
		if(ChkTradeStatusThread.instance==null)
		{
			ChkTradeStatusThread.instance=new ChkTradeStatusThread();
			ChkTradeStatusThread.instance.start();
		}
		
		if(FeeBackThread.instance==null)
		{
			FeeBackThread.instance=new FeeBackThread();
			FeeBackThread.instance.start();
		}
		
		if(ThreadForSendOfflineMail.instance==null)
		{
			ThreadForSendOfflineMail.instance=new ThreadForSendOfflineMail();
			ThreadForSendOfflineMail.instance.start();
		}
	}
}
class FeeBackThread extends Thread
{

	public static FeeBackThread instance=null;
	private static final int POLL_INTERVAL=5000; /*10分钟退一次款*/
	public void run() 
	{
		while(true)
		{
			try {
				sleep(POLL_INTERVAL);
				Vector<clsFeeBackParaBean> v=clsFeeBackParaBean.getLst();
				if(v.size()>0)
				{
					clsFeeBackParaBean obj=v.get(0);
					v.remove(0);
					TradeBean tradeBean =obj.getTrade();
					VenderBean vb=obj.getVender();
					
					if(tradeBean.getStatus()==0)
					{
						//这个情况的是需要退款的
						try {
							
							if(tradeBean .getTradetype()==clsConst.TRADE_TYPE_AL_QR)
							{
								System.out.println(AlipayRefund.PayBack(tradeBean,null,vb.getGroupid(),obj.getReason()).getMsg());
							}
							else if(tradeBean.getTradetype()==clsConst.TRADE_TYPE_WX_QR)
							{
								System.out.println(WxRefund.PayBack(tradeBean, null, vb.getGroupid(), obj.getReason()).getMsg());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					//
					SqlADO.insertTradeObj(tradeBean);
					SqlADO.DeleteFromTemp(tradeBean);
				}
				
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}
	}
}

class ChkTradeStatusThread extends Thread
{
	private static final int POLL_INTERVAL=60*1000; /*10分钟退一次款*/
	static ChkTradeStatusThread instance=null;
	public void run() 
	{
		while(true)
		{
			try {
				sleep(POLL_INTERVAL);
				
				/*获取允许退款的机器*/
				ArrayList<VenderBean> venderLst=SqlADO.getVenderBeanList();
				
				/*获取没有交易成功的定单,条件是:已经支付，且交易时间已经超过5分钟，status=0的商品*/
				ArrayList<TradeBean> FeeBaskTradeLst= TradeBean.getTradeByVenderLst(1000*60*2,0);
				
				for (TradeBean tradeBean : FeeBaskTradeLst) {
					if(tradeBean!=null)
					{
						VenderBean vb=null;
						for (VenderBean venderBean : venderLst) {
							if(venderBean.getId()==tradeBean.getGoodmachineid())
							{
								vb=venderBean;
								break;
							}
						}
						if(vb!=null)
						{
							if(vb.getAuto_refund()==1)
							{
								clsFeeBackParaBean.getLst().add(new clsFeeBackParaBean(tradeBean,vb,"处理超时退款"));
							}
							else
							{
				 				SqlADO.insertTradeObj(tradeBean);
				 				SqlADO.DeleteFromTemp(tradeBean);
							}
						}
					}
					else
					{
						
					}
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

class MyThread extends Thread 
{
	private static final int POLL_INTERVAL=20*1000;
	static MyThread instance=null;
	private Calendar old_cal;
	private Calendar new_cal;
	

	
	
	public void run() 
	{
		old_cal=Calendar.getInstance();
		while(true)
		{
			try {
				sleep(POLL_INTERVAL);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			SqlADO.AddOffLineTimes(POLL_INTERVAL);
			SqlADO.SetTerminalOffLine();

			
			/*每日更新剩余取货次数*/
			
			new_cal=Calendar.getInstance();
			if(new_cal.get(Calendar.DAY_OF_YEAR)!=old_cal.get(Calendar.DAY_OF_YEAR))
			{
				old_cal=Calendar.getInstance();
				CustomerBean.SetRestTimesToMaxLimitTimes();
				
				//启动每日分帐功能
				WxCoporTransfor.AutoTransfer();
			}
		}
	}
	
	
}

class ThreadForSendOfflineMail extends Thread
{
	public static ThreadForSendOfflineMail instance=null;
	private static ArrayList<Integer> VenderIdLst=new ArrayList();
	private void ProcessToSendMail()
	{
		ArrayList<VenderBean> lst=SqlADO.getVenderBeanList();
		for(VenderBean obj :lst)
		{
			if(obj.isIsOnline())
			{
				VenderIdLst.remove(obj.getId());
				System.out.println(String.format("%d has remove!", obj.getId()));
			}
			else
			{
				for(int i=0; i < VenderIdLst.size(); i++) {
					if(VenderIdLst.get(i) == obj.getId()) {
						SampleMail.Send(String.format("Offline Notification for Vending: %d - %s", obj.getId(), obj.getTerminalName()), String.format("Last Active Time: %1$te %1$tm %1$tY", obj.getTemperUpdateTime()));
					}
					
				}
			}
		}
		
	}
	
	public void run()
	{
		while(true)
		{
			try {
				sleep(120*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			try
			{
			ProcessToSendMail();
			}
			catch (Exception e	)
			{	
				e.printStackTrace();
			}
		}
	}
}

