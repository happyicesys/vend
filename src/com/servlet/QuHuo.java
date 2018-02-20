package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.clsConst;
import com.ado.SqlADO;
import com.tools.ToolBox;

import beans.CustomerBean;
import beans.TradeBean;
import beans.UserBean;

/**
 * Servlet implementation class QuHuo
 */
@WebServlet("/QuHuo")
public class QuHuo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuHuo() {
        super();
        // TODO Auto-generated constructor stub
    }

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
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter pw=response.getWriter();
		
		UserBean ub=(UserBean)request.getSession().getAttribute("usermessage");	
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
		
		if(!ub.AccessAble(UserBean.FUNID_CAN_SWIPE_QUHUO))
		{
			request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_SWIPE_QUHUO]);
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}
		double dou_price=ToolBox.filterDble(request.getParameter("price"));;
		int price=(int)(dou_price*100.0);
		int count=ToolBox.filterInt(request.getParameter("count"));
		
		String cardinfo= ToolBox.filter(request.getParameter("cardinfo"));
		
		CustomerBean c=CustomerBean.getCustomerBeanByCardID(cardinfo);
		
		ArrayList<String> lastRecord=(ArrayList<String>)request.getSession().getAttribute("lastRecord");
		if(lastRecord==null)
		{
			lastRecord=new ArrayList<String>();
			request.getSession().setAttribute("lastRecord", lastRecord);
		}
		
		Integer liushuiid=(Integer)request.getSession().getAttribute("liushuiid");
		if(liushuiid==null)
		{
			liushuiid=1;
		}
		String retsting="";
		if(c==null)
		{
			retsting="无效卡，扣款失败";
		}
		else
		{
			System.out.println(price*count);
			int ret=c.Cusume2(price*count);
			
			if(ret==CustomerBean.CUSTOMER_OK)
			{
				TradeBean tbBean=new TradeBean();
				tbBean.setCardinfo(cardinfo);
				tbBean.setChangestatus(1);
				tbBean.setGoodmachineid(1000);
				tbBean.setGoodroadid(0);
				tbBean.setGoodsName("学生奶");
				tbBean.setPaystatus(1);
				tbBean.setPrice(price);
				tbBean.setReceivetime(new Timestamp(System.currentTimeMillis()));
				tbBean.setSendstatus(1);
				tbBean.setTradetype(clsConst.TRADE_TYPE_CARD);
				
				
				for(int i=0;i<count;i++)
				{
					tbBean.setOrderid(ToolBox.MakeOutTradeID(1000));
					tbBean.setLiushuiid(String.format("%d", ToolBox.MakeRandom()));
					//创建定单记录
					SqlADO.insertTradeObj(tbBean);
				}
				lastRecord.add(String.format("卡号：%s,姓名:%s,数量:%d", cardinfo,c.get_user_name(),count));
				if(lastRecord.size()>4)
				{
					lastRecord.remove(0);
				}
				retsting="扣款成功，请取货。";
			}
			else if(ret==CustomerBean.CUSTOMER_CREDIT_OVER_LIMIT)
			{
				retsting="刷卡金额超过限制，扣款失败。";
			}
			else if(ret==CustomerBean.CUSTOMER_CREDIT_VALUE_ERR)
			{
				retsting="金额无效，扣款失败。";
			}
			else if(ret==CustomerBean.CUSTOMER_DISABLE)
			{
				retsting="会员已被禁止使用，扣款失败。";
			}
			else if(ret==CustomerBean.CUSTOMER_INVALID_CARD)
			{
				retsting="无效卡，扣款失败。";
			}
			else if(ret==CustomerBean.CUSTOMER_NOT_ENOUGH_BALANCE)
			{
				retsting="卡上余额不足，扣款失败。";
			}
			else if(ret==CustomerBean.CUSTOMER_NOT_ENOUGH_TIMES)
			{
				retsting="当日刷卡次数超限，扣款失败。";	
			}
			else
			{
				retsting="无效卡，扣款失败。";
			}
			retsting+=String.format("卡上余额%1.2f元", c.get_user_amount()/100.0);
			
			//RequestDispatcher dispatcher = request.getRequestDispatcher("quhuo.jsp");    // 使用req对象获取RequestDispatcher对象
	        
		}
		response.sendRedirect("quhuo.jsp?price="+ price/100.0 +"&ret="+retsting);
	}
}
