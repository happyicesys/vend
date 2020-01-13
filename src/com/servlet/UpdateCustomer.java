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
import beans.UserBean;
import beans.clsGroupBean;

/**
 * Servlet implementation class UpdateCustomer
 */
@WebServlet("/UpdateCustomer")
public class UpdateCustomer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	    /**
	     * @see HttpServlet#HttpServlet()
	     */
	    public UpdateCustomer() {
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
			
			if(!ub.AccessAble(UserBean.FUNID_CAN_CHANGE_CUSTOMER))
			{
				request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_CHANGE_CUSTOMER]);
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}
			
			
			String cardinfo=ToolBox.filter(request.getParameter("cardinfo"));
			CustomerBean bean=CustomerBean.getCustomerBeanByCardID(cardinfo);
		    int id=ToolBox.filterInt(request.getParameter("id"));
		    if(bean!=null)
		    {
		    	if(bean.getId()!=id)
		    	{
					request.setAttribute("message", "该卡片已经与其他会员绑定");
					request.getRequestDispatcher("message.jsp").forward(request, response);
					return;
		    	}
		    }


		    if(id==0)
		    {
				request.setAttribute("message", "参数错误");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
		    }
		    
		    bean=CustomerBean.getCustomerBeanById(id);
		    if(bean==null)
		    {
				request.setAttribute("message", "参数错误");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
		    }
		    
		    
		    
		    
		    bean.set_user_address(ToolBox.filter(request.getParameter("address")));
		    
			java.sql.Timestamp t=ToolBox.filterTime(request.getParameter("birthday")+ " 0:0:0");
			bean.set_user_birthday(t);
		    
		    bean.set_user_disable(ToolBox.filterInt(request.getParameter("_user_disable")));
		    bean.set_user_xiaofei_times_en(ToolBox.filterInt(request.getParameter("is_times_limit")));
		    bean.set_user_xiaofei_times(ToolBox.filterInt(request.getParameter("custerm_times")));
		    bean.set_user_max_credit_limit((int)(100*ToolBox.filterDble(request.getParameter("_user_max_credit_limit"))));
			bean.set_user_id_card(cardinfo);
		    bean.set_user_tel(ToolBox.filter(request.getParameter("mobiletel")));
		    String pwd=ToolBox.filter(request.getParameter("pwd"));
		    if(!pwd.equals(""))
		    {
		    	bean.set_user_pwd(ToolBox.filter(ToolBox.getMd5(pwd)));
		    }
		    bean.setSex_type(ToolBox.filter(request.getParameter("sextype")));
		    
		    bean.Update();
		    
			response.sendRedirect("customerMod.jsp?id="+id);
		}

	}

