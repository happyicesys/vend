package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tools.ToolBox;

import beans.CustomerBean;
import beans.CustomerChargeLogBean;
import beans.UserBean;

/**
 * Servlet implementation class CustomerCharge
 */
@WebServlet("/CustomerCharge")
public class CustomerCharge extends HttpServlet {

		private static final long serialVersionUID = 1L;
	       
	    /**
	     * @see HttpServlet#HttpServlet()
	     */
	    public CustomerCharge() {
	        super();
	    }

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

			if(!ub.AccessAble(UserBean.FUNID_CAN_CHARGE_FOR_CUSTOMER))
			{
				request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_CHARGE_FOR_CUSTOMER]);
				request.setAttribute("LAST_URL", "index.jsp");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}



			String cardinfo= ToolBox.filter(request.getParameter("cardinfo"));
			if(cardinfo.equals(""))
			{
				request.setAttribute("message", "Card number cannot be blank, top up failure");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}
			
			if(cardinfo.length()<8)
			{
				request.setAttribute("message", "Card number must be greater than 8 digits, top up failure");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}
			CustomerBean tem_ub = CustomerBean.getCustomerBeanByCardID(cardinfo);
			
			if(tem_ub==null)
			{
				request.setAttribute("message", "Card is not member, top up failure");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}
			
			int groupid=ub.getGroupid();

			if(tem_ub.getGroupid()!=groupid)
			{
				request.setAttribute("message", "该会员卡不在您的集团之下，本次充值失败");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}
			
			double dou_amount=ToolBox.filterDble(request.getParameter("amount"));
			//System.out.println(dou_amount);
			int amount=(int)(dou_amount*100);

			if(amount>100000)
			{
				request.setAttribute("message", "单次充值金额最大1000元，本次充值失败");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}
			
			int new_val= tem_ub.get_user_amount()+amount;
			
			if(new_val>10000000)
			{
				request.setAttribute("message", "卡片内金额最大100000元，本次充值失败");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}
			tem_ub.set_user_amount(new_val);
			tem_ub.setLast_charge_amount(amount);
			tem_ub.setLast_charge_time(new Timestamp(System.currentTimeMillis()));
			tem_ub.Update();
		
			
			CustomerChargeLogBean log=new CustomerChargeLogBean();
			log.setAdminname(ub.getId()+"-"+ub.getAdminusername());

			log.set_user_id_string(tem_ub.get_user_id_string());
			
			log.setAmount(amount);
			log.setAmount_after_charge(new_val);
			log.setCardinfo(cardinfo);
			log.setCustomername(tem_ub.get_user_name());
			log.setGmt(new Timestamp(System.currentTimeMillis()));
			log.setGroupid(groupid);
			log.setTradeid(ToolBox.MakeRPID());
			log.Add();
			
			response.sendRedirect("customercharge.jsp");
		}
		
		

	}


