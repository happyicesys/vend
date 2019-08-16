package com.servlet;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.clsConst;
import com.ado.SqlADO;
import com.alipay.AlipayQrcode;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.tools.ToolBox;

import beans.PortBean;
import beans.TradeBean;
import beans.VenderBean;
import beans.clsFeeBackParaBean;
import beans.clsFromGprs;
import beans.clsGoodsBean;
import beans.clsGroupBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import wx.pay.util.PayUtil;

/**
 * Servlet implementation class SetParaByPC
 */
@WebServlet("/SetParaByPC")
public class SetParaByPC extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetParaByPC() {
        super();
        // TODO Auto-generated constructor stub
    }

	final String CHAR_CODE="GBK";
	//final  String START_LETTER="{\"Type\":\"";
	final  String START_LETTER="{\"";
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding(CHAR_CODE);
		response.setCharacterEncoding(CHAR_CODE);
		response.setContentType("text/html; charset=GBK");
		PrintWriter pw=response.getWriter();
		
		InputStream is = request.getInputStream(); 
		DataInputStream input = new DataInputStream(is); 
		
		byte[] strb=new byte[40960];
		int poststrlen=0;

		int need_len=request.getContentLength();
		while (poststrlen<need_len) 
		{
			poststrlen+=input.read(strb,poststrlen,need_len-poststrlen); 
		}
		
		String ret_str="1";

		String poststr=new String(strb,0,poststrlen,CHAR_CODE);
		System.out.println("接收到一个数据请求："+request.getRemoteAddr()+"@"+ ToolBox.getDateTimeString()+","+poststr);
		String[] arrstr=poststr.split("&",0);
		
		Hashtable<String, String> hash=new Hashtable<String, String>(2,(float)0.8);		
		for (String string : arrstr) {
			String[] subarrstr=string.split("=",2);
			if(subarrstr.length>=2)
			{
				hash.put(subarrstr[0], subarrstr[1]);
			}
			else if(subarrstr.length==1)
			{
				hash.put(subarrstr[0], null);
			}
		}
		
		int f=ToolBox.filterInt(hash.get("f"));/*帧编号，服务器上保存1条最近的编号信息，如果收到的是重复的信息，那么不处理数据，仅仅回传上次发送过的数据*/
		int t=ToolBox.filterInt(hash.get("t"));/*数据类型*/
		int machineid=ToolBox.filterInt(hash.get("m"));/*机器编号，必须填写*/
		int gprs=ToolBox.filterInt(hash.get("g"));/*GPRS信号*/
		String p=hash.get("p");
		
		p=p.replaceFirst("!", "=");

		clsFromGprs  gprsdata=new clsFromGprs(machineid, f, t, gprs, p);
		
		System.out.println(gprsdata.getStr_content());
		VenderBean vb=VenderBean.ChkVender(machineid);
		if(vb!=null)
		{
			String last_ret= vb.ChkFrameRepeat(f, t);
			if(last_ret!=null)
			{
				pw.write(last_ret);
				return;
			}
		}
		else
		{
			vb=SqlADO.getVenderBeanByid(machineid);
			if(vb!=null)
			{
				VenderBean.AddVender(vb);
			}
		}
		if(vb==null)
		{
			return;
		}
		
			if(gprsdata.getStr_content().startsWith(START_LETTER))
			{
				JSONObject obj=JSONObject.fromObject(gprsdata.getStr_content());
				/*json对象*/
				if(vb!=null)
				{
					SqlADO.ClrOffLineTimes(vb.getId());
					vb.setGprs_Sign(gprsdata.getGprs());
					if(obj!=null)
					{
						if(obj.getString("Type").equals("VENDER"))
						{
							/*保存售货机状态数据*/
							SetVend(vb,obj);
						}
						else if(obj.getString("Type").equals("TRADE"))
						{
							/*保存交易记录*/
							SaveTradeObj(vb,obj);
						}
						else if(obj.getString("Type").equals("H1"))
						{
							/*获取已经支付的交易订单*/
							/*交易编号,货道号,商品编号,用户名称,支付方式*/
							TradeBean tradeBean= SqlADO.getSingleTrade(vb.getId(),0,1);
							if(tradeBean!=null)
							{
								ret_str=String.format("%s,%d,%d", 
										tradeBean.getOrderid(),
										tradeBean.getGoodroadid(),
										tradeBean.getTradetype()
										);
							}
							else
							{
								ret_str=String.format("%d",vb.getFlags1());
							}
						}
						else if(obj.get("Type").equals("RELOAD"))
						{
							/*终端已经更新货道*/
							
						}						
						else if(obj.get("Type").equals("SLOT"))
						{
							/*获取单个货道*/
							ret_str=GetSlotData(vb,obj);
							
						}
						else if(obj.get("Type").equals("REQQR"))
						{
							/*请求二维码数据*/
							ret_str=GetQrCodeData(vb,obj);

						}
						else if(obj.get("Type").equals("REQSQR"))
						{
							/*请求二维码数据*/
							ret_str=GetSingleQrCodeData(vb,obj);

						}
						else if(obj.get("Type").equals("TIME"))
						{
							/*同步时间*/
							ret_str="TIME"+ToolBox.getDateTimeString();
						}
						else if(obj.get("Type").equals("STATE"))
						{
							ret_str=GetTradeState(vb,obj);
						}
					  else if(obj.get("Type").equals("GOODS"))
					  {
						  /*请求商品数据*/
						  ret_str=GetgoodsData(vb,obj);
					  }
					  else if(obj.get("Type").equals("FETCH"))
					  {
					   /*请求商品数据*/
						  ret_str=GetVerifyTrade(vb,obj);
					  }
					}
				}
			}
			else
			{
				/*字节对象*/
				byte[] b=gprsdata.getContent();
				if(b!=null)
				{

					int i=0;
					if(vb!=null)
					{
						SqlADO.ClrOffLineTimes(vb.getId());
						//vb.setGprs_Sign(gprsdata.getGprs());
						if(b[i]=='S')
						{
							/*货道数据*/
							try
							{
								SaveColListData(b,vb);
							}
							catch(Exception e)
							{
								System.out.println(e);
							}
						}
					}
				}
			}
		
			String base64_str=ToolBox.getBASE64(ret_str);
			base64_str=base64_str.replace("\r", "");
			base64_str=base64_str.replace("\n", "");
			String str=String.format("%d,%d,%s",f,base64_str.length(), base64_str);
		vb.setM_cmdType(t);
		vb.setLast_fid(f);
		vb.setLast_send_string(str);
		System.out.println(str);
		pw.write(str);

	}

	private String GetSingleQrCodeData(VenderBean vb, JSONObject obj) {
		 String ret_str=null;
		 
		 int sid= obj.getInt("SId") ;
		 int price= obj.getInt("PRICE") ;
		 int cnt=obj.getInt("CNT") ;
		 int paytype=obj.getInt("PAYTP") ;
		 
		 String alqrcode="",wxqrcode="",altrade="",wxtrade="";
		 boolean needfresh_qrcode=false;
		 /*到支付宝*/
		 
		 PortBean pb=SqlADO.getPortBean(vb.getId(), sid);
		 if(pb==null)
		 {
			 ret_str+=",,";
			 return ret_str;
		 }
		 
		 pb.setAmount(cnt);
		 if(pb.getPrice()!=price)
		 {
			 pb.setPrice(price);
		 }

		 needfresh_qrcode=true;
		 clsGroupBean group=clsGroupBean.getGroup(vb.getGroupid());
			/*每个货道的二维码都刷新一次*/
			altrade= ToolBox.MakeRPID(pb.getMachineid(), pb.getInnerid());
			wxtrade=altrade;
			 if(paytype==clsConst.TRADE_TYPE_AL_QR)
			 {

	
				AlipayTradePrecreateResponse res=AlipayQrcode.MakeQrcode(pb,altrade,group);
				
				if(res!=null)
				{
					if(res.isSuccess())
					{
						alqrcode=res.getQrCode();
						pb.setAl_trade(altrade);
						pb.setQrcode(alqrcode);
					}
				}
				ret_str=String.format("QRCODE%s,%s,%d", alqrcode,altrade,paytype);
			 }
			 else
			 {
				String qrcode=PayUtil.MakeWxQrcode(pb,altrade,group);
				if(qrcode!=null)
				{
					wxqrcode=qrcode;
					pb.setWx_qrcode(wxqrcode);
					pb.setWx_trade(wxtrade);
				}
				ret_str=String.format("QRCODE%s,%s,%d", wxqrcode,altrade,paytype);
			 }

		

			//创建订单
		 TradeBean tb=new TradeBean();
		 tb.setOrderid(altrade);
		 tb.setGoodmachineid(pb.getMachineid());
		 tb.setGoodroadid(pb.getInnerid());
		 tb.setInneridname(pb.getInneridname());
		 tb.setPrice(price);
		 SqlADO.insertTradeObj(tb);
		 SqlADO.UpdateGoodsPortAllQrCode(pb);
		 
		return ret_str;
	}

	private String GetSlotData(VenderBean vb, JSONObject obj) 
	{
		    String ret_str = "";
			/*查询数据库记录，看看是否当前有支付宝或者微信的交易记录*/
			int sid=obj.getInt("SId");
			
			PortBean pb=SqlADO.getPortBean(vb.getId(), sid);
			clsGoodsBean gb=clsGoodsBean.getGoodsBean(pb.getGoodsid());
			String Goodsname="";
			String Picname="";
			if(gb!=null)
			{
				Goodsname=gb.getGoodsname();
				Picname=gb.getPicname();
			}
			
			ret_str=String.format("SLOT,%d,%d,%d,%d,%s,%s", pb.getInnerid(),pb.getPrice(),
					pb.getAmount(),pb.getCapacity(),Goodsname,Picname);
			
			return ret_str;
	}

//	private String GetTradeState(VenderBean vb, JSONObject obj) 
//	  {
//		    String ret_str;
//			/*查询数据库记录，看看是否当前有支付宝或者微信的交易记录*/
//			String tradeid=obj.getString("TRADE");
//			
//			/*查询交易是否已经支付完成*/
//			TradeBean tb=SqlADO.getTradeBean(tradeid);
//			int paid=0;/*设置为没有支付的状态*/
//			int paytype=99;
//		
//			if(tb!=null)
//			{
//				if(tb.getSendstatus()==0)
//				{
//					if(tb.getChangestatus()==1)
//					{
//						//ret_str="TRADE1"++tradeid;/*支付完成*/
//						paid=1;
//						paytype=tb.getTradetype();
//						
//					}
//				}
//				else
//				{
//					//ret_str="TRADE3"+tradeid;/*已经出货的交易*/
//					paid=3;
//				}
//			}
//			else
//			{
//				//ret_str="TRADE2"+tradeid;/*不存在的交易*/
//				paid=2;
//			}
//			
//			ret_str=String.format("TRADE%d%02d%s",paid,paytype,tradeid);
//			return ret_str;
//	}
	

	private String GetTradeState(VenderBean vb, JSONObject obj) 
	  {
		    String ret_str;
			/*查询数据库记录，看看是否当前有支付宝或者微信的交易记录*/
			String tradeid=obj.getString("TRADE");
			
			/*查询交易是否已经支付完成traderecordinfo_tem*/
			TradeBean tb=SqlADO.getTradeBeanFromTemp(tradeid);
			int paid=0;/*设置为没有支付的状态*/
			int paytype=99;
		
			if(tb!=null)
			{
				if(tb.getSendstatus()==0)
				{
					if(tb.getChangestatus()==1)
					{
						//ret_str="TRADE1"++tradeid;/*支付完成*/
						paid=1;
						paytype=tb.getTradetype();
						
						/*只要有过来获取交易状态的,将status字段改一下,*/
						tb.setStatus(-2);
						SqlADO.updateTradeBeanTemp(tb);
					}
				}
				else
				{
					//ret_str="TRADE3"+tradeid;/*已经出货的交易*/
					paid=3;
				}
			}
			else
			{
				//ret_str="TRADE2"+tradeid;/*不存在的交易*/
				paid=2;
			}
			
			ret_str=String.format("TRADE%d%02d%s",paid,paytype,tradeid);
			return ret_str;
	}


	  private String GetVerifyTrade(VenderBean vb, JSONObject obj) 
	  {
		    String ret_str;
			/*查询数据库记录，看看是否当前有支付宝或者微信的交易记录*/
			String tradeid=obj.getString("VERIFY");
			
			/*查询交易是否已经支付完成*/
			TradeBean tb=SqlADO.getTradeBean(tradeid);
			int paid=0;/*设置为没有支付的状态*/
			int paytype=99;
			int slotid=0;
			if(tb!=null)
			{
				if(tb.getSendstatus()==0)
				{
					if(tb.getChangestatus()==1)
					{
						//ret_str="TRADE1"++tradeid;/*支付完成*/
						paid=1;
						paytype=tb.getTradetype();
						slotid=ToolBox.filterHexInt(tb.getInneridname());
					}
				}
				else
				{
					//ret_str="TRADE3"+tradeid;/*已经出货的交易*/
					paid=3;
				}
			}
			else
			{
				//ret_str="TRADE2"+tradeid;/*不存在的交易*/
				paid=2;
			}
			
			ret_str=String.format("TRADE%d%02d%03d%s",paid,paytype,slotid,tradeid);
			return ret_str;
	}

		private String GetgoodsData(VenderBean vb,JSONObject obj) {
		      int page=-1;
			  if(obj.containsKey("Page"))
			  {
				  page =obj.getInt("Page");
			  }
			  ArrayList<clsGoodsBean> goodslst= clsGoodsBean.getGoodsBeanLst(vb.getGroupid());
			  
			  JSONObject json=new JSONObject();
			  json.put("GOODS", goodslst);
			  return json.toString();
			 }

	 private String GetQrCodeData(VenderBean vb, JSONObject obj) {
		 String ret_str=null;
		 
		 int sid= obj.getInt("SId") ;
		 int price= obj.getInt("PRICE") ;
		 int cnt=obj.getInt("CNT") ;
		 
		 String alqrcode="",wxqrcode="",altrade="",wxtrade="";
		 boolean needfresh_qrcode=false;
		 /*到支付宝*/
		 PortBean pb=null;
		 if(sid!=88888)
		 {
			 pb=SqlADO.getPortBean(vb.getId(), sid);
			 if(pb==null)
			 {
				 ret_str+=",,";
				 return ret_str;
			 }
			 
			 pb.setAmount(cnt);
			 if(pb.getPrice()!=price)
			 {
				 pb.setPrice(price);
			 }
		 }
		 else
		 {
			 pb=new PortBean();
			 pb.setInnerid(sid);
			 pb.setInneridname(String.format("%d", sid));
			 pb.setGoodroadname("购物车订单");
			 pb.setMachineid(vb.getId());
			 pb.setGoodsid(sid); 
			 pb.setAmount(cnt);
			 pb.setPrice(price);
		 }
		 

		 needfresh_qrcode=true;
		 clsGroupBean group=clsGroupBean.getGroup(vb.getGroupid());
		 if(needfresh_qrcode)
		 {
			/*每个货道的二维码都刷新一次*/
			altrade= ToolBox.MakeRPID(pb.getMachineid(), pb.getInnerid());
			wxtrade=altrade;

			AlipayTradePrecreateResponse res=AlipayQrcode.MakeQrcode(pb,altrade,group);
			
			if(res!=null)
			{
				if(res.isSuccess())
				{
					alqrcode=res.getQrCode();
					pb.setAl_trade(altrade);
					pb.setQrcode(alqrcode);
				}
			}

			
			String qrcode=PayUtil.MakeWxQrcode(pb,altrade,group);
			if(qrcode!=null)
			{
				wxqrcode=qrcode;
				pb.setWx_qrcode(wxqrcode);
				pb.setWx_trade(wxtrade);
			}
		 }

		ret_str=String.format("QRCODE%s,%s,%s,", alqrcode,wxqrcode,altrade);

			//创建订单
		 TradeBean tb=new TradeBean();
		 tb.setOrderid(altrade);
		 tb.setGoodmachineid(pb.getMachineid());
		 tb.setGoodroadid(pb.getInnerid());
		 tb.setInneridname(pb.getInneridname());
		 tb.setPrice(price);
		 //SqlADO.insertTradeObj(tb);
		 SqlADO.insertTradeObjToTemp(tb);
		 //SqlADO.UpdateGoodsPortAllQrCode(pb);
		 
		return ret_str;
	}


		/**
		 * @param vid
		 * @param trade
		 */
		void SaveTradeObj2(int vid,JSONObject trade)
		 {
		 	System.out.println("收到一条交易记录"+trade.toString());
		 	TradeBean tbBean=new TradeBean();

		 	tbBean.setGoodmachineid(vid);
		 
		 	tbBean.setPrice(trade.getInt("Price"));
		 	
		 	tbBean.setCoin_credit(trade.getInt("COINS"));
		 	
		 	tbBean.setBill_credit(trade.getInt("BILLS"));

		 	
		 	
		 	tbBean.setGoodroadid(trade.getInt("SId") );
		 	tbBean.setInneridname(String.format(clsConst.SLOT_FORMAT, tbBean.getGoodroadid()));
		 	
		 	tbBean.setChanges(trade.getInt("CHANEGS"));
		 	
		 	int lid=trade.getInt("TId");
		 	if(trade.containsKey("ORDRID"))
		 	{
		 		//timestr=trade.getString("TIME");
		 		tbBean.setOrderid(trade.getString("ORDRID"));
		 	}
		 	else
		 	{
		 		tbBean.setOrderid(ToolBox.MakeTradeID(vid,lid));
		 	}
		 	tbBean.setLiushuiid(String.format("%08d", lid));

		 	//tbBean.setGoodroadid(trade.getInt("SId"));
		 	
		 	if(trade.containsKey("PAY_TYPE"))//
		 	{
		 		tbBean.setTradetype(trade.getInt("PAY_TYPE"));	/*支付类型*/
		 		
		 	}
		 	else
		 	{
		 		tbBean.setTradetype(clsConst.TRADE_TYPE_CASH);	/*支付类型*/
		 	}
		 	

		 	//tbBean.setPaystatus(1);/*支付状态*/
		 	tbBean.setChangestatus(1);/*支付状态*/
		 	
		 	tbBean.setSendstatus(1/*trade.getInt("ISOK")*/);/*出货状态*/
//		 	if(tbBean.getSendstatus()!=0)
//		 	{
//		 		/*transfor success*/
//		 		/*uodate slot*/
//		 		SqlADO.SubPortGoods(vid, tbBean.getInneridname(),1);
//		 		
//		 	}
		 	
		 	
//		 	int errcode=trade.getInt("SErr");/*写入货道故障*/
//		 	if(errcode!=0)
//		 	{
//		 		/**/
//		 		SqlADO.updatePortFault(vid, tbBean.getGoodroadid(), 1);
//		 	}
		 	
		 	/*
	[
	    {
	        "Goods": {
	            "DiscountPrice": 0,
	            "Amount": 2,
	            "id": 567,
	            "goodsname": "\u7f8e\u6c41\u6e90\u679c\u7c92\u6a59450g",
	            "price": 400,
	            "picname": "201607251006044167.JPG",
	            "picname2": "",
	            "des1": "",
	            "des2": "",
	            "des3": "",
	            "groupid": 0
	        },
	        "GoodsCount": 0,
	        "GoodsTransforCount": 1,
	        "GoodsNotTransforCount": 0
	    },
	    {
	        "Goods": {
	            "DiscountPrice": 0,
	            "Amount": 5,
	            "id": 539,
	            "goodsname": "\u519c\u592b\u5c71\u6cc9550ml",
	            "price": 200,
	            "picname": "201607221633462922.JPG",
	            "picname2": "",
	            "des1": "",
	            "des2": "",
	            "des3": "",
	            "groupid": 0
	        },
	        "GoodsCount": 0,
	        "GoodsTransforCount": 1,
	        "GoodsNotTransforCount": 0
	    }
	]	 	 */
		 	if(trade.containsKey("GDSLst"))
		 	{
		 		try
		 		{
		 		JSONArray gooslst=trade.getJSONArray("GDSLst");
		 		
		 		//tbBean.setRetmes(gooslst.toString());
		 		StringBuilder sb=new StringBuilder();
		 		for (int index=0;index<gooslst.size();index++) 
		 		{
		 			JSONObject obj= (JSONObject)gooslst.get(index);
		 			JSONObject goods=obj.getJSONObject("Goods");
		 			String goodsname=goods.getString("goodsname");
		 			int count=obj.getInt("GoodsTransforCount");
		 			sb.append(goodsname);
		 			sb.append(":");
		 			sb.append(count);
		 			sb.append(";");
				}
		 		tbBean.setRetmes(sb.toString());
		 		}
		 		catch(Exception e1)
		 		{
		 			e1.printStackTrace();
		 		}
		 	}
		 	
		 	if(trade.containsKey("GOODS"))
		 	{
		 		//timestr=trade.getString("TIME");
		 		tbBean.setGoodsName(trade.getString("GOODS"));
		 	}
		 	else
		 	{
		 	
			 	 PortBean pb=SqlADO.getPortBean(vid, tbBean.getGoodroadid());
			 	 if(pb!=null)
			 	 {
				 	int gid=pb.getGoodsid();
				 	if(gid==0)
				 	{
				 		tbBean.setGoodsName("未知名称");
				 	}
				 	else
				 	{
				 		clsGoodsBean g=clsGoodsBean.getGoodsBean(gid);
				 		if(g==null)
				 		{
				 			tbBean.setGoodsName("未知名称");
				 		}
				 		else
				 		{
				 			tbBean.setGoodsName(g.getGoodsname());
				 		}
				 	}
			 	 }
		 	}
		 	String timestr=null;
		 	if(trade.containsKey("TIME"))
		 	{
		 		timestr=trade.getString("TIME");
		 	}
		 	else
		 	{
		 		timestr=ToolBox.getDateTimeString();
		 	}
		 	
		 	
		 	tbBean.setReceivetime(Timestamp.valueOf(timestr));

		 	
		 	TradeBean temtrade=SqlADO.getTradeBean(tbBean.getOrderid());
		 	
		 	if(temtrade==null)
		 	{
		 		SqlADO.insertTradeObj(tbBean);
		 	}
		 	else
		 	{
		 		if(tbBean.getTradetype()!=clsConst.TRADE_TYPE_CASH)
		 		{
			 		temtrade.setLiushuiid(tbBean.getLiushuiid());
			 		temtrade.setSendstatus(tbBean.getSendstatus());
			 		temtrade.setGoodsName(tbBean.getGoodsName());
			 		temtrade.setRetmes(tbBean.getRetmes());/*设置购买的产品信息*/
			 		SqlADO.updateTradeBean(temtrade);
		 		}
		 	}
		 }
		


	void SaveTradeObj(VenderBean vb, JSONObject trade)
	 {
		int vid=vb.getId();
	 	System.out.println("收到一条交易记录"+trade.toString());
	 	TradeBean tbBean=new TradeBean();

	 	tbBean.setGoodmachineid(vid);
	 
	 	tbBean.setPrice(trade.getInt("Price"));
	 	
	 	tbBean.setCoin_credit(trade.getInt("COINS"));
	 	
	 	tbBean.setBill_credit(trade.getInt("BILLS"));

	 	
	 	
	 	tbBean.setGoodroadid(trade.getInt("SId") );
	 	
	 	PortBean pb=SqlADO.getPortBean(vid, tbBean.getGoodroadid());
	 	if(pb!=null)
	 	{
	 		tbBean.setInneridname(pb.getInneridname());
	 	}
	 	
	 	tbBean.setChanges(trade.getInt("CHANEGS"));
	 	
	 	int lid=trade.getInt("TId");
	 	if(trade.containsKey("ORDRID"))
	 	{
	 		//timestr=trade.getString("TIME");
	 		tbBean.setOrderid(trade.getString("ORDRID"));
	 	}
	 	else
	 	{
	 		tbBean.setOrderid(ToolBox.MakeTradeID(vid,lid));
	 	}
	 	tbBean.setLiushuiid(String.format("%08d", lid));

	 	//tbBean.setGoodroadid(trade.getInt("SId"));
	 	
	 	if(trade.containsKey("PAY_TYPE"))//
	 	{
	 		tbBean.setTradetype(trade.getInt("PAY_TYPE"));	/*支付类型*/
	 	}
	 	else
	 	{
	 		tbBean.setTradetype(clsConst.TRADE_TYPE_CASH);	/*支付类型*/
	 	}
	 	
	 	
	 	//tbBean.setPaystatus(1);/*支付状态*/
	 	tbBean.setChangestatus(1);/*支付状态*/
	 	
	 	tbBean.setSendstatus(trade.getInt("ISOK"));/*出货状态*/
	 	if(tbBean.getSendstatus()!=0)
	 	{
	 		/*transfor success*/
	 		/*uodate slot*/
	 		SqlADO.SubPortGoods(vid, tbBean.getInneridname(),1);
	 		
	 	}
	 	int errcode=trade.getInt("SErr");/*写入货道故障*/
	 	if(errcode!=0)
	 	{
	 		/**/
	 		SqlADO.updatePortFault(vid, tbBean.getGoodroadid(), 1);
	 	}
	 	if(trade.containsKey("GOODS"))
	 	{
	 		//timestr=trade.getString("TIME");
	 		tbBean.setGoodsName(trade.getString("GOODS"));
	 	}
	 	else
	 	{
	 	
		 	 //PortBean pb=SqlADO.getPortBean(vid, tbBean.getGoodroadid());
		 	 if(pb!=null)
		 	 {
			 	int gid=pb.getGoodsid();
			 	if(gid==0)
			 	{
			 		tbBean.setGoodsName("未知名称");
			 	}
			 	else
			 	{
			 		clsGoodsBean g=clsGoodsBean.getGoodsBean(gid);
			 		if(g==null)
			 		{
			 			tbBean.setGoodsName("未知名称");
			 		}
			 		else
			 		{
			 			tbBean.setGoodsName(g.getGoodsname());
			 		}
			 	}
		 	 }
	 	}
	 	String timestr=null;
	 	if(trade.containsKey("TIME"))
	 	{
	 		timestr=trade.getString("TIME");
	 	}
	 	else
	 	{
	 		timestr=ToolBox.getDateTimeString();
	 	}
	 	
	 	
	 	tbBean.setReceivetime(Timestamp.valueOf(timestr));

	 	
	 	//TradeBean temtrade=SqlADO.getTradeBean(tbBean.getOrderid());
	 	TradeBean temtrade=SqlADO.getTradeBeanFromTemp(tbBean.getOrderid());
//	 	if(temtrade==null)
//	 	{
//	 		tbBean.setStatus(clsConst.TRADE_STATUS_COPED);
//	 		SqlADO.insertTradeObj(tbBean);
//	 	}
//	 	else
//	 	{
//	 		if(tbBean.getTradetype()!=clsConst.TRADE_TYPE_CASH)
//	 		{
//		 		temtrade.setLiushuiid(tbBean.getLiushuiid());
//		 		temtrade.setSendstatus(tbBean.getSendstatus());
//		 		temtrade.setGoodsName(tbBean.getGoodsName());
//		 		temtrade.setStatus(clsConst.TRADE_STATUS_COPED);
//		 		SqlADO.updateTradeBean(temtrade);
//		 		
//	 		}
//	 	}
	 	
	 	int sourceFlg=0;
	 	if(temtrade==null)
	 	{
	 		temtrade=SqlADO.getTradeBean(tbBean.getOrderid());
	 		sourceFlg=1;
	 	}
	 	if(temtrade==null)
	 	{
	 		SqlADO.insertTradeObj(tbBean);
	 		sourceFlg=2;
	 	}
	 	else
	 	{
	 		if(tbBean.getTradetype()!=clsConst.TRADE_TYPE_CASH)
	 		{
		 		temtrade.setLiushuiid(tbBean.getLiushuiid());
		 		temtrade.setSendstatus(tbBean.getSendstatus());
		 		temtrade.setGoodsName(tbBean.getGoodsName());
		 		SqlADO.updateTradeBeanTemp(temtrade);
		 		if(vb.getAuto_refund()==1)
		 		{
		 			if(tbBean.getSendstatus()==0)
		 			{
		 				clsFeeBackParaBean.getLst().add(new clsFeeBackParaBean(temtrade, vb, "出货故障自动退款"));
		 			}
		 			else
		 			{
		 				if(sourceFlg==0)
		 				{
		 					//如果数据来源是临时表格，那么就添加一个记录到正式交易表
		 					SqlADO.insertTradeObj(temtrade);
		 				}
		 				else
		 				{
		 					//如果数据来源是正式交易表，那么就直接更新正式交易表
		 					SqlADO.updateTradeBean(temtrade);
		 				}
		 				SqlADO.DeleteFromTemp(temtrade);
		 			}
		 		}
		 		else
		 		{
	 				if(sourceFlg==0)
	 				{
	 					//如果数据来源是临时表格，那么就添加一个记录到正式交易表
	 					SqlADO.insertTradeObj(temtrade);
	 				}
	 				else
	 				{
	 					//如果数据来源是正式交易表，那么就直接更新正式交易表
	 					SqlADO.updateTradeBean(temtrade);
	 				}
		 			SqlADO.DeleteFromTemp(temtrade);
		 		}
	 		}
	 	}
	 	
	 }
	


	private void SetVend( VenderBean vb, JSONObject venderobj) 
	{
		if(venderobj==null)
		{
			return;
		}
		if(vb!=null)
		{
			int bill_stat=venderobj.getInt("BILLStat");
			int coin_stat=venderobj.getInt("CHGEStat");			
			
			int mdb_flg=0;
			if((bill_stat&(1<<1))==0x02)
			{
				mdb_flg|=VenderBean.MDB_COMMUNICATION_BILL;
			}
			
			if((coin_stat&(1<<1))==0x02)
			{
				mdb_flg|=VenderBean.MDB_COMMUNICATION_COIN;
			}
			vb.setMdbDeviceStatus(mdb_flg);								/*1B,MDB设备状态信息*/

			int fun_flg=0;

			if((bill_stat&(1<<0))==0x01)
			{
				fun_flg|=VenderBean.FUNC_IS_MDB_BILL_VALID;
			}
			
			if((coin_stat&(1<<0))==0x01)
			{
				fun_flg|=VenderBean.FUNC_IS_MDB_COIN_VALID;
			}

			vb.setFunction_flg(fun_flg);	/*4B,包含机器状态，设备有效性标示*/

			vb.setIRErrCnt(venderobj.getInt("IRErrCnt"));
			vb.setLstSltE(venderobj.getInt("LstSltE"));
			
			vb.setCoinAttube(venderobj.getInt("CoinCnt"));		/*4B 硬币桶钱币总额*/
			
			if(venderobj.containsKey("Ver"))
			{
			
				vb.setCode_ver(venderobj.getInt("Ver"));		/*4B 硬币桶钱币总额*/
			}
			if(venderobj.containsKey("BllCnt"))
			{
			
				vb.setBills(venderobj.getInt("BllCnt"));		
			}
			
			SqlADO.UpdateMechinePara(vb);
		}
	}
	
	 void SaveColListData(byte[] b,VenderBean vb) throws Exception
	 {
		 int i=0,j;
		 ArrayList<PortBean> plst=new ArrayList<PortBean>();
		 
		 if(b==null)
		 {
			  throw new Exception("无效参数，null");
		 }
		 int count=(b.length-5)/PortBean.SLOT_OBJ_SIZE;//ToolBox.arrbyteToint_Little(b, i, 4);
		 if((b.length-5)!=count* PortBean.SLOT_OBJ_SIZE)
		 {
			  throw new Exception("无效参数,lenth="+(b.length-5));
		 }
		 i=5;
		 for(j=0;j<count;j++)
		 {
			 PortBean p=new PortBean();
			 
			 p.setMachineid(vb.getId());
			 
			 /*货道编号*/
			 p.setInnerid(ToolBox.arrbyteToint_Little(b, i, 2));
			 //System.out.println(p.getInnerid());
			 i+=2;
			 p.setInneridname(String.format(clsConst.SLOT_FORMAT, p.getInnerid()));
			 /*货道故障编号*/
			 p.setError_id(b[i++]);
			 /*货道容量*/
			 p.setCapacity(b[i++]);
			 /*货道库存数量*/
			 p.setAmount(b[i++]);
			 /*货道价格*/
			 p.setPrice(ToolBox.arrbyteToint_Little(b, i, 4));
			 i+=4;
			 /*货道绑定的商品编号*/
			 p.setGoodsid(ToolBox.arrbyteToint_Little(b, i, 2));
			 i+=2;
			 plst.add(p);
		 }
		 if(vb.getM_AllowUpdateGoodsByPc()==1)
		 {
			 SqlADO.UpdatePortAndGoodsid(plst);
		 }
		 else
		 {
			 /*看售货机是否允许在下位机设置*/
			 SqlADO.UpdatePort(plst);
		 }
	 }
}


