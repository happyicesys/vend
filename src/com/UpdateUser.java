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
public class UpdateUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		doPost(request, response);
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
			request.setAttribute("message", "用户名或密码错误！请联系管理员！");
			request.setAttribute("LAST_URL", "index.jsp");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}
		int id=ToolBox.filterInt(request.getParameter("uid"));
		UserBean tub=UserBean.getUserBeanById(id);

		if(tub==null)
		{
			request.setAttribute("message", "参数错误，没有找到相关的账号！");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}

		String pwd=request.getParameter("pwd");
		if(pwd==null)
		{
			pwd="";
		}

		if (!pwd.equals(""))
		{
			if(pwd.length()<6)
			{
				request.setAttribute("message", "密码参数至少6位");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}
			tub.setAdminpassword(ToolBox.getMd5(pwd));
		}
		else
		{
			tub.setAdminpassword(null);
		}
		tub.setId(id);

		tub.setAdminaddress(ToolBox.filter(request.getParameter("address")));
		tub.setAdminmobilephone(ToolBox.filter(request.getParameter("mobiletel")));
		tub.setAdmintelephone(ToolBox.filter(request.getParameter("firmtel")));
		tub.setAdminname(ToolBox.filter(request.getParameter("name")));
		tub.setAdminsex(ToolBox.filter(request.getParameter("sextype")));
		tub.setAdminEmails(ToolBox.filter(request.getParameter("emails")));


		String[] stra=request.getParameterValues("right");
		String right="";
		int[] arr=new int[100];
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
		tub.setAdminrights(right);

	    String canAccessVender=request.getParameter("canAccessVender");

		tub.setCanAccessSellerid(canAccessVender);


		UserBean.updateUser(tub);

		response.sendRedirect("UserList.jsp");
	}
}
