package com.servlet;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.ClsGprsCfg;
import beans.PortBean;
import beans.TradeBean;
import beans.VenderBean;
import beans.VenderLogBean;
import beans.clsFromGprs;
import beans.clsGoodsBean;
import beans.clsGroupBean;
import net.sf.json.JSONObject;
import wx.pay.util.PayUtil;

import com.ClsTime;
import com.clsConst;
import com.clsEvent;
import com.clsFid;
import com.ado.SqlADO;
import com.alipay.AlipayQrcode;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.tools.ClsPwd;
import com.tools.ToolBox;

/**
 * Servlet implementation class SetPara3
 */
public class SetPara3 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetPara3() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setCharacterEncoding(CHAR_CODE);
		response.setCharacterEncoding(CHAR_CODE);
		response.setContentType("text/html; charset=GBK");
		PrintWriter pw=response.getWriter();
		pw.print("testOK");
		
	}

	final String CHAR_CODE="GBK";
	final  String START_LETTER="{\"";
	
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
		
		byte[] strb=new byte[20480];
		int poststrlen=0;
		int need_len=request.getContentLength();
		while (poststrlen<need_len) 
		{
			poststrlen+=input.read(strb,poststrlen,need_len-poststrlen); 
			//System.out.println(poststrlen);
		}
		String ret_str="1";

		String poststr=new String(strb,0,poststrlen,CHAR_CODE);
		System.out.println("??????????????????????????????"+request.getRemoteAddr()+"@"+ ToolBox.getDateTimeString()+","+poststr);
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
		
		int f=ToolBox.filterInt(hash.get("f"));/*??????????????????????????????1???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????*/
		int t=ToolBox.filterInt(hash.get("t"));/*????????????*/
		int machineid=ToolBox.filterInt(hash.get("m"));/*???????????????????????????*/
		int gprs=ToolBox.filterInt(hash.get("g"));/*GPRS??????*/
		String p=hash.get("p");
		
		p=p.replaceFirst("!", "=");

		clsFromGprs  gprsdata=new clsFromGprs(machineid, f, t, gprs, p);
		
		VenderLogBean logBean=new VenderLogBean(gprsdata.getStr_content(),null,machineid,poststr,f,t);
		
		logBean.add();/*????????????????????????*/
		
		VenderBean vb=VenderBean.ChkVender(machineid);
		if(vb!=null)
		{
			String last_ret= vb.ChkFrameRepeat(f, t);
			if(last_ret!=null)
			{
				pw.write(last_ret);
				logBean.setResponse(last_ret);
				logBean.updateResponse();
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
				/*json??????*/
				if(vb!=null)
				{
					SqlADO.ClrOffLineTimes(vb.getId());
					vb.setGprs_Sign(gprsdata.getGprs());
					if(obj!=null)
					{
						if(obj.getString("Type").equals("VENDER"))
						{
							/*???????????????????????????*/
							SetVend(vb,obj);
						}
						else if(obj.getString("Type").equals("TRADE"))
						{
							/*??????????????????*/
							SaveTradeObj(vb,obj);
						}
						else if(obj.getString("Type").equals("H"))
						{
							/*?????????????????????????????????*/
							/*????????????,?????????,????????????,????????????,????????????*/
							TradeBean tradeBean= SqlADO.getSingleTrade(vb.getId(),0,1);

							if(tradeBean!=null)
							{
								tradeBean.setSendstatus(-1);
								SqlADO.updateTradeBean(tradeBean);
								ret_str=String.format("HEART%s,%d,%d", 
										tradeBean.getOrderid(),
										5,/*??????????????????*/
										tradeBean.getTradetype());

							}
							else
							{
								ret_str=String.format("HEART%d",vb.getFlags1());
							}
						}
						else if(obj.get("Type").equals("RELOAD"))
						{
							/*????????????????????????*/
						}
						else if(obj.get("Type").equals("REQQR"))
						{
							/*?????????????????????*/
							ret_str=GetQrCodeData(vb,obj);
						}
						else if(obj.get("Type").equals("TIME"))
						{
							/*????????????*/
							ret_str="TIME"+ToolBox.getDateTimeString();
							//VenderBean.freshVenderPara();
						}
						else if(obj.get("Type").equals("STATE"))
						{
							/*?????????????????????????????????*/
							ret_str=GetTradeState(vb,obj);
						}
						else if(obj.get("Type").equals("GOODS"))
						{
							/*??????????????????*/
							ret_str=GetgoodsData(vb);
						}
						else if(obj.get("Type").equals("CNCL"))
						{
							/*??????????????????,??????????????????*/
							ret_str=CancelTrade(vb,obj);
						}
						else
						{
							
						}
					}
				}
			}
			else
			{
				/*????????????*/
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
							/*????????????*/
							try
							{
								SaveColListData(b,vb.getId());
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
		logBean.setResponse(str);
		logBean.updateResponse();
		pw.write(str);
	}
	
	private String CancelTrade(VenderBean vb,JSONObject obj) 
	{
		String ret_str="1";
					/*?????????????????????????????????????????????????????????????????????????????????*/
		String tradeid=obj.getString("TRADE");
		
		TradeBean tb=SqlADO.getTradeBean(tradeid);
		
		if(tb!=null)
		{
			/*???????????????????????????*/
			
			
			/*?????????????????????????????????*/
			
			/*????????????????????????????????????*/
		}
		
		return ret_str;
	}



	private String GetTradeState(VenderBean vb, JSONObject obj) 
	  {
		    String ret_str;
			/*?????????????????????????????????????????????????????????????????????????????????*/
			String tradeid=obj.getString("TRADE");
			
			/*????????????????????????????????????*/
			TradeBean tb=SqlADO.getTradeBean(tradeid);
			int paid=0;/*??????????????????????????????*/
			int paytype=99;
		
			if(tb!=null)
			{
				if(tb.getSendstatus()==0)
				{
					if(tb.getChangestatus()==1)
					{
						//ret_str="TRADE1"++tradeid;/*????????????*/
						paid=1;
						paytype=tb.getTradetype();
						
					}
				}
				else
				{
					//ret_str="TRADE3"+tradeid;/*?????????????????????*/
					paid=3;
				}
			}
			else
			{
				//ret_str="TRADE2"+tradeid;/*??????????????????*/
				paid=2;
			}
			
			ret_str=String.format("TRADE%d%02d%s",paid,paytype,tradeid);
			return ret_str;
	}

	  private String GetVerifyTrade(VenderBean vb, JSONObject obj) 
	  {
		    String ret_str;
			/*?????????????????????????????????????????????????????????????????????????????????*/
			String tradeid=obj.getString("VERIFY");
			
			/*????????????????????????????????????*/
			TradeBean tb=SqlADO.getTradeBean(tradeid);
			int paid=0;/*??????????????????????????????*/
			int paytype=99;
			int slotid=0;
			if(tb!=null)
			{
				if(tb.getSendstatus()==0)
				{
					if(tb.getChangestatus()==1)
					{
						//ret_str="TRADE1"++tradeid;/*????????????*/
						paid=1;
						paytype=tb.getTradetype();
						slotid=ToolBox.filterHexInt(tb.getInneridname());
					}
				}
				else
				{
					//ret_str="TRADE3"+tradeid;/*?????????????????????*/
					paid=3;
				}
			}
			else
			{
				//ret_str="TRADE2"+tradeid;/*??????????????????*/
				paid=2;
			}
			
			ret_str=String.format("TRADE%d%02d%03d%s",paid,paytype,slotid,tradeid);
			return ret_str;
	}

	private String GetgoodsData(VenderBean vb) {
		  ArrayList<clsGoodsBean> goodslst= clsGoodsBean.getGoodsBeanLst(vb.getGroupid());
		  JSONObject json=new JSONObject();
		  json.put("GOODS", goodslst);
		  return json.toString();
		 }

	 private String GetQrCodeData(VenderBean vb, JSONObject obj) {
		 String ret_str=null;
		 
		 int sid= obj.getInt("SId") ;/*??????*/
		 int price= obj.getInt("PRICE");/*??????*/
		 int cnt=obj.getInt("CNT") ;/*?????????*/
		 String alqrcode="",wxqrcode="",altrade="",wxtrade="";
		 boolean needfresh_qrcode=false;
		 //int payType=obj.getInt("ptype");
		 /*????????????*/
		 

		 

		 clsGroupBean groupBean=clsGroupBean.getGroup(vb.getGroupid());

			/*???????????????????????????????????????*/
			altrade= ToolBox.MakeRPID(vb.getId(), sid);
			wxtrade=altrade;
			PortBean pb=new PortBean();
			pb.setPrice(price);
			pb.setGoodroadname(String.format("??????%f??????", sid/1000.0));
			pb.setInnerid(1);
			pb.setInneridname("1");
			AlipayTradePrecreateResponse res=AlipayQrcode.MakeQrcode(pb,altrade,groupBean);
			if(res!=null)
			{
				if(res.isSuccess())
				{
					//SqlADO.UpdateGoodsPortQrCode(pb.getMachineid(),pb.getGoodsid(),res.getQrCode(),null);
					alqrcode=res.getQrCode();
					pb.setAl_trade(altrade);
					pb.setQrcode(alqrcode);
				}
			}

			
			String qrcode=PayUtil.MakeWxQrcode(pb,altrade,groupBean);
			if(qrcode!=null)
			{
				//SqlADO.UpdateGoodsPortWxQrCode(pb.getMachineid(),pb.getGoodsid(),qrcode,qrcode_img_url);
				wxqrcode=qrcode;
				pb.setWx_qrcode(wxqrcode);
				pb.setWx_trade(wxtrade);
			}
		 

			 ret_str=String.format("QRCODE%s,%s,%s,", alqrcode,wxqrcode,altrade);
			 //System.out.println(ret_str);

			//????????????
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



	void SaveTradeObj(VenderBean vb,JSONObject trade)
	 {
	 	System.out.println("????????????????????????"+trade.toString());
	 	TradeBean tbBean=new TradeBean();
	 	int vid=vb.getId();
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
	 		tbBean.setTradetype(trade.getInt("PAY_TYPE"));	/*????????????*/
	 	}
	 	else
	 	{
	 		tbBean.setTradetype(clsConst.TRADE_TYPE_CASH);	/*????????????*/
	 	}
	 	
	 	
	 	//tbBean.setPaystatus(1);/*????????????*/
	 	tbBean.setChangestatus(1);/*????????????*/
	 	
	 	tbBean.setSendstatus(trade.getInt("ISOK"));/*????????????*/
	 	if(tbBean.getSendstatus()!=0)
	 	{
	 		/*transfor success*/
	 		/*uodate slot*/
	 		SqlADO.SubPortGoods(vid, tbBean.getInneridname(),1);
	 		
	 	}
	 	int errcode=trade.getInt("SErr");/*??????????????????*/
	 	if(errcode!=0)
	 	{
	 		/**/
	 		SqlADO.updatePortFault(vid, tbBean.getGoodroadid(), 1);
	 	}
	 	
	 	// TODO ?????????????????????????????????
	 	
	 	if(vb.getAuto_refund()==1)
	 	{
	 		
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
			 		tbBean.setGoodsName("????????????");
			 	}
			 	else
			 	{
			 		clsGoodsBean g=clsGoodsBean.getGoodsBean(gid);
			 		if(g==null)
			 		{
			 			tbBean.setGoodsName("????????????");
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
		 		SqlADO.updateTradeBean(temtrade);
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
			vb.setMdbDeviceStatus(mdb_flg);								/*1B,MDB??????????????????*/

			int fun_flg=0;

			if((bill_stat&(1<<0))==0x01)
			{
				fun_flg|=VenderBean.FUNC_IS_MDB_BILL_VALID;
			}
			
			if((coin_stat&(1<<0))==0x01)
			{
				fun_flg|=VenderBean.FUNC_IS_MDB_COIN_VALID;
			}

			vb.setFunction_flg(fun_flg);	/*4B,??????????????????????????????????????????*/

			vb.setIRErrCnt(venderobj.getInt("IRErrCnt"));
			vb.setLstSltE(venderobj.getInt("LstSltE"));
			
			vb.setCoinAttube(venderobj.getInt("CoinCnt"));		/*4B ?????????????????????*/
			
			if(venderobj.containsKey("Ver"))
			{
			
				vb.setCode_ver(venderobj.getInt("Ver"));		/*4B ?????????????????????*/
			}
			if(venderobj.containsKey("BllCnt"))
			{
			
				vb.setBills(venderobj.getInt("BllCnt"));		
			}
			
			SqlADO.UpdateMechinePara(vb);
		}
	}
	
	 void SaveColListData(byte[] b,int mid) throws Exception
	 {
		 int i=0,j;
		 ArrayList<PortBean> plst=new ArrayList<PortBean>();
		 
		 if(b==null)
		 {
			  throw new Exception("???????????????null");
		 }
		 int count=(b.length-5)/PortBean.SLOT_OBJ_SIZE;//ToolBox.arrbyteToint_Little(b, i, 4);
		 if((b.length-5)!=count* PortBean.SLOT_OBJ_SIZE)
		 {
			  throw new Exception("????????????,lenth="+(b.length-5));
		 }
		 i=5;
		 for(j=0;j<count;j++)
		 {
			 PortBean p=new PortBean();
			 
			 p.setMachineid(mid);
			 
			 /*????????????*/
			 p.setInnerid(ToolBox.arrbyteToint_Little(b, i, 2));
			 //System.out.println(p.getInnerid());
			 i+=2;
			 p.setInneridname(String.format(clsConst.SLOT_FORMAT, p.getInnerid()));
			 /*??????????????????*/
			 p.setError_id(b[i++]);
			 /*????????????*/
			 p.setCapacity(b[i++]);
			 /*??????????????????*/
			 p.setAmount(b[i++]);
			 /*????????????*/
			 p.setPrice(ToolBox.arrbyteToint_Little(b, i, 4));
			 i+=4;
			 /*???????????????????????????*/
			 p.setGoodsid(ToolBox.arrbyteToint_Little(b, i, 2));
			 i+=2;
			 plst.add(p);
		 }
		 
		 SqlADO.UpdatePort(plst);
	 }
}
