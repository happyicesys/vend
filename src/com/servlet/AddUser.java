package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ado.SqlADO;
import com.tools.ToolBox;

import beans.UserBean;

/**
 * Servlet implementation class AddUser
 */
public class AddUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddUser() {
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
	    
	    
	    if(!ub.AccessAble(UserBean.FUNID_CAN_ADD_USER))
		{
			request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ADD_USER]);
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}
	    
	    
		UserBean tub=new UserBean();
		String pwd=request.getParameter("pwd");	
		if(pwd==null)
		{
			pwd="";
		}
		if(pwd.length()<6)
		{
			request.setAttribute("message", "Password is too short");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}
		if (!pwd.equals(""))
		{
			tub.setAdminpassword(ToolBox.getMd5(pwd));
		}

		String tem_username=request.getParameter("username");
		if(tem_username==null)
		{
			tem_username="";
		}
		if(tem_username.equals(""))
		{
			request.setAttribute("message", "Username cannot be empty");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}
		tem_username=tem_username.trim();
		UserBean tem_ub=UserBean.getUserBean(tem_username);
		if(tem_ub!=null)
		{
			request.setAttribute("message", "Username has been used, please try again");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}
		
		
		tub.setAdminusername(tem_username);
		

		tub.setAdminaddress(ToolBox.filter(request.getParameter("address")));
		tub.setAdminmobilephone(ToolBox.filter(request.getParameter("mobiletel")));
		tub.setAdmintelephone(ToolBox.filter(request.getParameter("firmtel")));
		tub.setAdminname(ToolBox.filter(request.getParameter("name")));
		tub.setAdminsex(ToolBox.filter(request.getParameter("sextype")));
		
		int groupid=0;
		if(ub.AccessAble(UserBean.FUNID_CAN_SET_USER_GROUP_ID_WHEN_ADD))
		{
			groupid=ToolBox.filterInt(request.getParameter("groupid"));
		}
		if(groupid==0)
		{
			groupid=ub.getGroupid();
		}

		tub.setGroupid(groupid);
		String[] stra=request.getParameterValues("right");
				
		String right="";
		int[] arr=new int[100];
		for(String str:stra)
		{
			
			try {
				arr[Integer.parseInt(str)]=1;
			} catch (Exception e) {
			}
		}
		
		for (int i : arr) 
		{
			right+=i;
		}
		tub.setAdminrights(right);
		
	    
	    String canAccessVender=request.getParameter("canAccessVender");
	    System.out.println(canAccessVender);
		tub.setCanAccessSellerid(canAccessVender);
		UserBean.addUser(tub);
		response.sendRedirect("UserList.jsp");
	    
	}

}
