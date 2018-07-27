package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.clsConst;
import com.ado.SqlADO;
import com.tools.ToolBox;

import beans.ClsSaleStatisticData;
import beans.JsonMsgBean;
import beans.UserBean;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class HomePageSaleStatistic
 */
@WebServlet("/HomePageSaleStatistic")
public class HomePageSaleStatistic extends HttpServlet {
	private static final long serialVersionUID = 1L;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomePageSaleStatistic() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
	    request.setCharacterEncoding("UTF-8");
	    response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		UserBean ub=(UserBean)session.getAttribute("usermessage");
		PrintWriter pw= response.getWriter();
		JSONObject json=null;
		JsonMsgBean jsmb=new JsonMsgBean();
		if(ub==null)
		{
			jsmb.setMsg(1);
			jsmb.setDetail("您没有登录或无权访问！请联系管理员！");
			pw.print(JSONObject.fromObject(jsmb).toString());
			return;
		}
		
		if(!ub.AccessAble(UserBean.FUNID_CAN_ACCESS_WEB))
		{
			jsmb.setMsg(1);
			jsmb.setDetail("不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ACCESS_WEB]+"，您没有权限");
			pw.print(JSONObject.fromObject(jsmb).toString());
			return;
		}
	    
	    
//	    if(!ub.AccessAble(UserBean.FUNID_CAN_DELTE_VENDER))
//		{
//			jsmb.setMsg(1);
//			jsmb.setDetail("不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_DELTE_VENDER]+"，您没有权限");
//			pw.print(JSONObject.fromObject(jsmb).toString());
//			return;
//		}

			jsmb.setMsg(0);
			jsmb.setDetail("ok");
			json=JSONObject.fromObject(jsmb);
			
			
			Date edata=null;
		 	java.sql.Date beginDate=ToolBox.filteDate(request.getParameter("sdate"));
		    
		 	if(beginDate==null)
		 	{
		 		beginDate=new Date(System.currentTimeMillis());
		 	}
	
			Calendar c = Calendar.getInstance();
			
			c.setTimeInMillis(beginDate.getTime());
			//c.add(Calendar.DAY_OF_MONTH, 1);
			edata=new Date(c.getTimeInMillis());
			
			ClsSaleStatisticData salestatistic_all= SqlADO.getSalesStatisticDataFromDb(
					beginDate,edata,ub.getVenderLimite(),clsConst.TRADE_TYPE_NO_LIMIT,0);
			ClsSaleStatisticData salestatistic_al= SqlADO.getSalesStatisticDataFromDb(
					beginDate,edata,ub.getVenderLimite(),clsConst.TRADE_TYPE_AL_QR,0);
			ClsSaleStatisticData salestatistic_wx= SqlADO.getSalesStatisticDataFromDb(
					beginDate,edata,ub.getVenderLimite(),clsConst.TRADE_TYPE_WX_QR,0);
			ClsSaleStatisticData salestatistic_card= SqlADO.getSalesStatisticDataFromDb(
					beginDate,edata,ub.getVenderLimite(),clsConst.TRADE_TYPE_CARD,0);
			ClsSaleStatisticData salestatistic_cash= SqlADO.getSalesStatisticDataFromDb(
					beginDate,edata,ub.getVenderLimite(),clsConst.TRADE_TYPE_CASH,0);
			ClsSaleStatisticData salestatistic_bank= SqlADO.getSalesStatisticDataFromDb(
					beginDate,edata,ub.getVenderLimite(),clsConst.TRADE_TYPE_BANK,0);
			ClsSaleStatisticData salestatistic_freevend= SqlADO.getSalesStatisticDataFromDb(
					beginDate,edata,ub.getVenderLimite(),clsConst.TRADE_TYPE_COCO,0);
			//json.put("_title", "123");
	
			json.put("all_count",salestatistic_all.getM_count());
			json.put("all_credit",String.format("%1.2f", salestatistic_all.getM_credit()/100.0));
	    	json.put("cash_count",salestatistic_cash.getM_count());
	    	json.put("cash_credit",String.format("%1.2f",salestatistic_cash.getM_credit()/100.0));
	    	json.put("freevend_count",salestatistic_freevend.getM_count());
	    	json.put("freevend_credit",String.format("%1.2f",salestatistic_freevend.getM_credit()/100.0));	    	
	    	json.put("wx_count",salestatistic_wx.getM_count());
	    	json.put("wx_credit",String.format("%1.2f",salestatistic_wx.getM_credit()/100.0));
	    	json.put("al_count",salestatistic_al.getM_count());
	    	json.put("al_credit",String.format("%1.2f",salestatistic_al.getM_credit()/100.0));
	    	json.put("card_count",salestatistic_card.getM_count());
	    	json.put("card_credit",String.format("%1.2f",salestatistic_card.getM_credit()/100.0));
	    	json.put("bank_count",salestatistic_bank.getM_count());
	    	json.put("bank_credit",String.format("%1.2f",salestatistic_bank.getM_credit()/100.0));
	    	pw.print(json.toString());

	}

}
