package com.servlet;

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
 * Servlet implementation class SetAccessDisable
 */
public class SetAccessDisable extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetAccessDisable() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
	    
	    
	    if(!ub.AccessAble(UserBean.FUNID_CAN_UPDATE_USER))
		{
			request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_UPDATE_USER]);
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}
		
		int id=ToolBox.filterInt(request.getParameter("uid"));
		
		UserBean tub;
		if(ub.getId()==id)
		{
			tub=ub;
		}
		else
		{
			tub=UserBean.getUserBeanById(id);
		}
		
		
		if(tub==null)
		{
			request.setAttribute("message", "Invalid parameter, manager not found");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}

		int vid=ToolBox.filterInt(request.getParameter("vid"));
		
		int[] arr=ToolBox.StringToIntArray(tub.getCanAccessSellerid());
		
		String canAccessVender="";
		for(int v:arr)
		{
			if(v!=vid)
			{
				canAccessVender+=v+",";
			}
		}

		if(canAccessVender.endsWith(","))
		{
			canAccessVender=canAccessVender.substring(0,canAccessVender.length()-1);
		}
			
		tub.setCanAccessSellerid(canAccessVender);
		UserBean.updateUser(tub);
	    //需要改成跳转到上一页
		String url=request.getHeader("Referer");
		response.sendRedirect(url);
		//response.sendRedirect("UserInfo.jsp?uid="+id);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
