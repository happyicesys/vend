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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import beans.CustomerChargeLogBean;
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
 * Servlet implementation class ChargeList
 */
@WebServlet("/ChargeList")

/**
 * Servlet implementation class EditGoods
 */
public class ChargeList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChargeList() {
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
	    	request.setAttribute("message", "You don't have permission to access this page, please try again");
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
	   

		
		int count_per_page =ub.getPagecount();
		ArrayList<CustomerChargeLogBean> lst = null;
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
	        //System.out.println(json);
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
			String cardinfo=ToolBox.filter(json1.getString("cardinfo"));
			int maxrows=ToolBox.filterInt(json1.getString("maxrows"));
			if(maxrows<=0)
			{
				maxrows=100;
			}
			String sql="groupid="+ub.getGroupid() +" and ";
			
			String tradeid=ToolBox.filter(json1.getString("tradeid"));
			String id_string=ToolBox.filter(json1.getString("id_string"));
			if(!tradeid.equals(""))
			{
				sql+=" tradeid like '%"+tradeid+"%' and";
			}
			
			if(!cardinfo.equals(""))
			{
				sql+=" cardinfo like '%"+cardinfo+"%' and";
			}
			if(!id_string.equals(""))
			{
				sql+=" _user_id_string like '%"+id_string+"%' and";
			}
		sql+=" CAST(gmt AS datetime) between '"+StartDate+"' and '"+ToolBox.addDate(EndDate,1)+"'" ;
		lst = CustomerChargeLogBean.getCustomerBeanLst(sql,pageindex,maxrows);
		
		JSONArray jsonData=new JSONArray();
		JSONObject jo=null;
		for (int i=0,len=lst.size();i<len;i++) 
		{
			CustomerChargeLogBean tb = lst.get(i);
			if(tb==null)
			{
				continue;
			}
			jo=new JSONObject();
			jo.put("id", i+1);
			jo.put("amount", String.format("%1.2f",tb.getAmount()/100.0));
			jo.put("adminname", tb.getAdminname());
			jo.put("gmt", ToolBox.getTimeLongString(tb.getGmt()));

			jo.put("cardinfo", tb.getCardinfo());
			jo.put("tradeid", tb.getTradeid());
			jo.put("id_string", tb.get_user_id_string());
			jo.put("customername", tb.getCustomername());
			jo.put("amount_after_charge",String.format("%1.2f",tb.getAmount_after_charge()/100.0));			
			jsonData.add(jo);
		}

		request.setAttribute("RsCount", RsCount);
		//request.setAttribute("gblst", gblst);
		request.setAttribute("count_per_page", count_per_page);
		request.setAttribute("pageindex", pageindex);
		
		/*RequestDispatcher dispatcher = request.getRequestDispatcher("goodsList.jsp");    // 使用req对象获取RequestDispatcher对象
        dispatcher.forward(request, response);       */
		int TotalCount=CustomerChargeLogBean.getLogRowsCount(sql);// 使用RequestDispatcher对象在服务器端向目的路径跳转
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("rows", jsonData);
		jsonObject.put("total",TotalCount);
		out.print(jsonObject.toString());
//		response.sendRedirect("goodsList.jsp");
		
	}

}
