package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.jmx.snmp.Timestamp;
import com.tools.ToolBox;

import beans.CustomerBean;
import beans.UserBean;
import beans.clsGroupBean;

/**
 * Servlet implementation class AddCustomer
 */
@WebServlet("/AddCustomer")
public class AddCustomer extends HttpServlet {
		private static final long serialVersionUID = 1L;
	       
	    /**
	     * @see HttpServlet#HttpServlet()
	     */
	    public AddCustomer() {
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

			if(!ub.AccessAble(UserBean.FUNID_CAN_ADD_CUSTOMER))
			{
				request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ADD_CUSTOMER]);
				request.setAttribute("LAST_URL", "index.jsp");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}

			


			String pwd=request.getParameter("pwd");	
			if(pwd==null)
			{
				pwd="";
			}
			if(pwd.length()<6)
			{
				request.setAttribute("message", "密码参数至少6位");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}

			String tem_username=request.getParameter("username");
			if(tem_username==null)
			{
				tem_username="";
			}
			if(tem_username.equals(""))
			{
				request.setAttribute("message", "会员名不能为空");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}
			tem_username=tem_username.trim();
			CustomerBean tem_ub=CustomerBean.getCustomerBeanByUserName(tem_username);
			if(tem_ub!=null)
			{
				request.setAttribute("message", "会员名已经存在，请使用其他会员名");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}
			String cardinfo= ToolBox.filter(request.getParameter("cardinfo"));
			if(cardinfo.equals(""))
			{
				request.setAttribute("message", "卡号不能为空");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}
			
			if(cardinfo.length()<8)
			{
				request.setAttribute("message", "卡号必须大于8位");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}
			tem_ub=CustomerBean.getCustomerBeanByCardID(cardinfo);
			
			if(tem_ub!=null)
			{
				request.setAttribute("message", "卡号已经被使用,请填写其他卡号");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}
			
			int jine=(int)(100*ToolBox.filterDble(request.getParameter("jine")));
			if(jine>100000)
			{
				request.setAttribute("message", "预充值金额最大1000元");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}
			
			CustomerBean beans=new CustomerBean();
			int groupid=ub.getGroupid();
			beans.set_user_pwd(ToolBox.getMd5(pwd.trim()));
			beans.set_user_name(tem_username.trim());
			beans.setGroupid(groupid);
			
			beans.set_user_id_card(cardinfo);
			beans.set_user_amount(jine);
			beans.set_user_xiaofei_times_en(ToolBox.filterInt(request.getParameter("is_times_limit")));
			beans.set_user_xiaofei_times(ToolBox.filterInt(request.getParameter("custerm_times")));
			beans.set_user_tel(ToolBox.filter(request.getParameter("mobiletel")));
			beans.set_user_id_string(ToolBox.MakeTradeID(groupid));
			beans.set_user_max_credit_limit((int)(100*ToolBox.filterDble(request.getParameter("_user_max_credit_limit"))));
			java.sql.Timestamp t=ToolBox.filterTime(request.getParameter("birthday")+ " 0:0:0");
			beans.set_user_birthday(t);
			beans.setSex_type(ToolBox.filter(request.getParameter("sextype")));			
			

			beans.set_user_address(ToolBox.filter(request.getParameter("address")));
			beans.set_user_builder_date(new java.sql.Timestamp(System.currentTimeMillis()));
			
			beans.Add();
			
			response.sendRedirect("AddCustomer.jsp");
		}
		
		

	}

