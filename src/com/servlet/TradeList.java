package com.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
//import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import beans.TradeBean;
import beans.UserBean;
import beans.clsGoodsBean;

import com.ClsTime;
import com.clsConst;
import com.ado.SqlADO;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import com.tools.StringUtil;
import com.tools.ToolBox;

/**
 * Servlet implementation class EditGoods
 */
public class TradeList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TradeList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		HttpSession session=request.getSession();
	    request.setCharacterEncoding("UTF-8");
	    response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		UserBean ub=(UserBean)session.getAttribute("usermessage");
		PrintWriter out= response.getWriter();
		
	    if(ub==null)
	    {
	    	request.setAttribute("message", "您没有登录或无权访问！请联系管理员！");
	    	request.setAttribute("LAST_URL", "index.jsp");
	    	request.getRequestDispatcher("message.jsp").forward(request, response);
	    	return;
	    }
	    
	    if(!ub.AccessAble(UserBean.FUNID_CAN_ACCESS_WEB))
	    {
	    	request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ACCESS_WEB]);
	    	request.setAttribute("LAST_URL", "index.jsp");
	    	request.getRequestDispatcher("message.jsp").forward(request, response);
	    	return;
	    }
	    
	    
//	    if(!ub.AccessAble(UserBean.FUNID_CAN_UPDATE_GOODS))
//	    {
//	    	request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_UPDATE_GOODS]);
//	    	request.getRequestDispatcher("message.jsp").forward(request, response);
//	    	return;
//	    }

		
		int count_per_page =ub.getPagecount();
		ArrayList<TradeBean> gblst = null;
		int RsCount=0;

		BufferedReader bufr = 
	            new BufferedReader(
	                new InputStreamReader(request.getInputStream(),"UTF-8"));
	        StringBuilder sBuilder = new StringBuilder("");
	        String temp = "";
	        while((temp = bufr.readLine()) != null){
	            sBuilder.append(temp);
	        }
	        bufr.close();
	        String json = sBuilder.toString();
	        JSONObject json1 = JSONObject.fromObject(json);
	        String abcd = json1.getString("sdate");
	        Date StartDate=ToolBox.filteDate(json1.getString("sdate"));
		    Date EndDate=ToolBox.filteDate(json1.getString("edate"));
		if((StartDate==null) && (EndDate==null))
		{
			StartDate=new Date(ClsTime.SystemTime());
			EndDate=new Date(ClsTime.SystemTime());
		}else 
		{
			if (EndDate.before(StartDate))
			{
				Date t=EndDate;
				EndDate=StartDate;
				StartDate=t;
			}
		}
			int pageindex=ToolBox.filterInt(request.getParameter("offset"));
			int offset = ToolBox.filterInt(json1.getString("offset"));
			int limit = ToolBox.filterInt(json1.getString("limit"));
			
			if(offset !=0){
				pageindex = offset/limit;
			}
			pageindex+= 1;
			String orderId=ToolBox.filter(json1.getString("orderid"));
			String  SellerId =ToolBox.filter(json1.getString("sellerid"));
			String PortId= ToolBox.filter(json1.getString("portid"));
			String CardNumber=ToolBox.filter(json1.getString("CardNumber"));
			int Success=ToolBox.filterInt(json1.getString("success"));
			int jiesuan=ToolBox.filterInt(json1.getString("jiesuan"));
			/*int PaySuccess=ToolBox.filterInt(json1.getString("paysuccess"));*/
			int maxrows=ToolBox.filterInt(json1.getString("maxrows"));
			
			int tradetype=ToolBox.filterInt(json1.getString("tradetype"));
			if(maxrows<=0)
			{
				maxrows=100;
			}
			String sql="";
			if(!orderId.equals(""))
			{
				sql+=" traderecordinfo.orderid like '%"+orderId+"%' and";
			}
			if(!CardNumber.equals(""))
			{
				sql+=" traderecordinfo.cardinfo like '%"+CardNumber+"%' and";
			}
			if(!SellerId.equals(""))
			{
				SellerId=SellerId.replaceAll("，",",");
				String[] seller;
				seller=SellerId.split(",",0);
				SellerId="";
				for(int i=0;i<seller.length;i++)
				{
					int vid=ToolBox.filterInt(seller[i]);
					//检查所输入的是否全部位数字
					if(vid>0)
					{
						if(ub.CanAccessSeller(vid))
						{
							SellerId+=vid+",";
						}
					}
				}
				if(SellerId.endsWith(",")) 
				{
					SellerId=SellerId.substring(0,SellerId.length()-1);
				}

				sql+=" (traderecordinfo.goodmachineid in("+SellerId+")) and";
			}
			else
			{
				sql+=" (traderecordinfo.goodmachineid in("+ub.getCanAccessSellerid()+")) and";
			}
				
			if(!PortId.equals(""))
			{
				PortId=PortId.replaceAll("，",",");
				sql+=" (traderecordinfo.goodroadid in("+PortId+")) and";
			}
			if(Success==1)//成功的
			{
				sql+=" traderecordinfo.sendstatus=1 and";
			}else if(Success==2)//不成功的
			{
				sql+=" traderecordinfo.sendstatus=0 and";
			}
			
			if(jiesuan==1)//已经结算的
			{
				sql+=" traderecordinfo.has_jiesuan=1 and";
			}else if(jiesuan==2)//没有结算的
			{
				sql+=" traderecordinfo.has_jiesuan=0 and";
			}
			
			if(tradetype!=clsConst.TRADE_TYPE_NO_LIMIT)
			{
				sql+=" traderecordinfo.tradetype="+ tradetype +" and";
			}
			sql+=" CAST(traderecordinfo.receivetime AS datetime) between '"+StartDate+"' and '"+ToolBox.addDate(EndDate,1)+"' and liushuiid<>''" ;
		gblst = SqlADO.getTradeList(sql,pageindex,maxrows);
		
		JSONArray jsonData=new JSONArray();
		JSONObject jo=null;
		String tradetypeString = null;
		for (int i=0,len=gblst.size();i<len;i++) 
		{
			TradeBean tb = gblst.get(i);
			if(tb==null)
			{
				continue;
			}
			jo=new JSONObject();
			jo.put("id",  i+1);
			jo.put("liushuiid", tb.getLiushuiid());
			/*jo.put("cardinfo", tb.getCardinfo());*/
			jo.put("price", String.format("%1.2f",tb.getPrice()/100.0));
			jo.put("mobilephone", tb.getMobilephone());
			jo.put("receivetime", ToolBox.getYMDHMS(tb.getReceivetime()));
			int tradetype1 = tb.getTradetype();
			if(tradetype1 == 0){
				tradetypeString = "Cash";
			}else if(tradetype1 == 1){
				tradetypeString = "Card";
			}
			jo.put("tradetype", clsConst.TRADE_TYPE_DES[tb.getTradetype()]);
			jo.put("tradetypeid",tb.getTradetype());
			jo.put("changestatus", (tb.getChangestatus()!=0)?"Success":"Fail");
			jo.put("sendstatus", (tb.getSendstatus()!=0)?"Success":"Fail");
			jo.put("bill_credit", String.format("%1.2f",tb.getBill_credit()/100.0));
			jo.put("coin_credit", String.format("%1.2f",tb.getCoin_credit()/100.0));
			jo.put("changes",String.format("%1.2f",tb.getChanges()/100.0));
			jo.put("goodmachineid",String.format("%03d",tb.getGoodmachineid()));
			jo.put("goodroadid", tb.getGoodroadid());
			jo.put("SmsContent", tb.getSmsContent());
			jo.put("orderid", tb.getOrderid());
			jo.put("goodsName", tb.getGoodsName());
			
			jo.put("inneridname", tb.getInneridname());
			//jo.put("xmlstr", tb.getXmlstr());
			jo.put("cardinfo", tb.getCardinfo());
			jo.put("tradeid", tb.getTradeid());
			if(tb.getHas_jiesuan()==0)
			{
				if(tb.getTradetype()==clsConst.TRADE_TYPE_CASH)
				{
					jo.put("opbut", "");
				}
				else if(tb.getTradetype()==clsConst.TRADE_TYPE_AL_QR)
				{
					jo.put("opbut", ub.AccessAble(UserBean.FUNID_CAN_REFUND)?"<a href='feeback.jsp?id="+ tb.getId() +"' >可退款</a>":"");
				}
				else if(tb.getTradetype()==clsConst.TRADE_TYPE_WX_QR)
				{
					jo.put("opbut", ub.AccessAble(UserBean.FUNID_CAN_REFUND)?"<a href='feeback.jsp?id="+ tb.getId() +"' >可退款</a>":"");
				}
				else 
				{
					jo.put("opbut", "");
				}
			}
			else
			{
				if(tb.getHas_feeback()==1)
				{
					jo.put("opbut", "已退款");
				}
				else
				{
					jo.put("opbut", "已结算");
				}
			}
			jsonData.add(jo);
		}

		request.setAttribute("RsCount", RsCount);
		request.setAttribute("gblst", gblst);
		request.setAttribute("count_per_page", count_per_page);
		request.setAttribute("pageindex", pageindex);
		
		/*RequestDispatcher dispatcher = request.getRequestDispatcher("goodsList.jsp");    // 使用req对象获取RequestDispatcher对象
        dispatcher.forward(request, response);       */
		int TotalCount=SqlADO.getTradeRowsCount(sql);// 使用RequestDispatcher对象在服务器端向目的路径跳转
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("rows", jsonData);
		jsonObject.put("total",TotalCount);
		out.print(jsonObject.toString());
//		response.sendRedirect("goodsList.jsp");
		
	}

}
