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

import beans.UserBean;

import com.ClsTime;
import com.ado.SqlADO;
import com.tools.ToolBox;

/**
 * Servlet implementation class jsonLogin
 */
@WebServlet("/jsonLogin")
public class jsonLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public jsonLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter pWriter=response.getWriter();
		
		String username=ToolBox.filter(request.getParameter("username"));
		String pwd=request.getParameter("password");
		if(pwd==null)
		{
			pwd="";
		}
		if (username.equals("")||pwd.equals(""))
		{
			pWriter.print("用户名和密码不能为空！");
			return;
		}else 
		{
			//查询数据库
			UserBean ub=UserBean.getUserBean(username, ToolBox.getMd5(pwd));
			System.out.println(username);
			System.out.println(pwd);
			
			if(ub==null)
			{
				pWriter.print("用户名或密码错误！请联系管理员！");
				return;
			}else if(!ub.AccessAble(UserBean.FUNID_CAN_ACCESS_WEB))
			{
				pWriter.print("您没有登录或无权访问！请联系管理员！");
				return;
			}else
			{
				String ip=request.getRemoteAddr();
				ub.setLastloginip(ip);
				ub.setLastLoginTime(new Timestamp(ClsTime.SystemTime()));
				UserBean.updateUser(ub);
				
				if(ub.AccessAble(UserBean.FUNID_ACCESS_ALL_VENDER))
				{
					ub.setCanAccessSellerid(SqlADO.VenderBeanID());
				}
				HttpSession session=request.getSession();
				session.setMaxInactiveInterval(18000);
				session.setAttribute("usermessage",ub);
				pWriter.print("ok");
				return;
			}
		}
	}

}
