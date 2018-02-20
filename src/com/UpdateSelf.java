package com;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.UserBean;

import com.ado.SqlADO;
import com.tools.ToolBox;

/**
 * Servlet implementation class UpdateUser
 */
public class UpdateSelf extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateSelf() {
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
		
		String pwd=request.getParameter("pwd");	
		if(pwd==null)
		{
			pwd="";
		}
		pwd=pwd.trim();
		System.out.println(pwd);
		if (pwd.equals(""))
		{
			ub.setAdminpassword(null);
		}
		else
		{
			if(pwd.length()<6)
			{
				request.setAttribute("message", "密码参数至少6位");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}
			ub.setAdminpassword(ToolBox.getMd5(pwd));
		}
		
		ub.setAdminaddress(ToolBox.filter(request.getParameter("address")));
		ub.setAdminmobilephone(ToolBox.filter(request.getParameter("mobiletel")));
		ub.setAdmintelephone(ToolBox.filter(request.getParameter("firmtel")));
		ub.setAdminname(ToolBox.filter(request.getParameter("name")));
		//int id=ToolBox.filterInt(request.getParameter("id"));
		
		ub.setWx_name(ToolBox.filter(request.getParameter("wx_name")));
		ub.setWx_openid(ToolBox.filter(request.getParameter("wx_openid")));
		ub.setAl_name(ToolBox.filter(request.getParameter("al_name")));
		
		ub.setAdminsex(ToolBox.filter(request.getParameter("sextype")));
		
		if(ub.AccessAble(UserBean.FUNID_CAN_EDIT_RIGHT))
		{
			String[] stra=request.getParameterValues("right");
			String right="";
			int[] arr=new int[200];
			int limit_id=0;
			for(String str:stra)
			{
				try {
					limit_id=Integer.parseInt(str);
					if(ub.AccessAble(limit_id))
					{
						arr[limit_id]=1;
					}
					else
					{
						arr[limit_id]=0;
					}
				} catch (Exception e) 
				{
					
				}
			}
			
			for (int i : arr) 
			{
				right+=i;
			}
			ub.setAdminrights(right);
		}
		UserBean.updateUser(ub);
		response.sendRedirect("UserInfo.jsp");
	}

}
