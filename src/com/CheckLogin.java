package com;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.Session;

import com.tools.ToolBox;
import com.ado.SqlADO;

import beans.UserBean;

/**
 * Servlet implementation class CheckLogin
 */
public class CheckLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckLogin() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("index.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		
		String username=ToolBox.filter(request.getParameter("username"));
		String pwd=request.getParameter("password");
		String random=request.getParameter("random");
		
		if(random==null)
		{
			random="";
		}

		
		if(pwd==null)
		{
			pwd="";
		}
		if (username.equals("")||pwd.equals(""))
		{
			request.setAttribute("message", "用户名和密码不能为空！");
			request.setAttribute("LAST_URL", "index.jsp");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}else 
		{
			//查询数据库
			UserBean ub=UserBean.getUserBean(username, ToolBox.getMd5(pwd));

			if(ub==null)
			{
				request.setAttribute("message", "用户名或密码错误！请联系管理员！");
				request.setAttribute("LAST_URL", "index.jsp");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}else if(!ub.AccessAble(UserBean.FUNID_CAN_ACCESS_WEB))
			{
				request.setAttribute("message", "您没有登录或无权访问！请联系管理员！");
				request.setAttribute("LAST_URL", "index.jsp");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}else
			{
				String ip=request.getRemoteAddr();
				ub.setLastloginip(ip);
				ub.setLastLoginTime(new Timestamp(ClsTime.SystemTime()));
				UserBean.updateUser(ub);
				
				//System.out.println(ub.getLastloginip());
				
				if(ub.AccessAble(UserBean.FUNID_ACCESS_ALL_VENDER))
				{
					ub.setCanAccessSellerid(SqlADO.VenderBeanID());
				}
				HttpSession session=request.getSession();
				session.setAttribute("currentpage",null);
				session.setMaxInactiveInterval(1800);
				session.setAttribute("usermessage",ub);
				response.sendRedirect("admin.jsp");
			}
		}
	}
}
