package com.servlet;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ado.SqlADO;
import com.tools.ToolBox;

import beans.UserBean;
import beans.VenderBean;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class BindUser
 */
@WebServlet("/BindUser")
public class BindUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BindUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
			pw.print("You don't have permission to access this page, please try again");
			return;
		}
		
		if(!ub.AccessAble(UserBean.FUNID_CAN_ACCESS_WEB))
		{
			pw.print("不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ACCESS_WEB]+", You don't have permission to access");
			return;
		}
	    
	    
	    if(!ub.AccessAble(UserBean.FUNID_CAN_ADD_BIND_AL_WX_USER))
		{
			pw.print("不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ADD_BIND_AL_WX_USER]+", You don't have permission to access");
			return;
		}

	    int vid =ToolBox.filterInt(request.getParameter("vid"));
	    int uid =ToolBox.filterInt(request.getParameter("uid"));
	    
	    if(ub.CanAccessSeller(vid))
	    {	
	    	VenderBean vb=SqlADO.getVenderBeanByid(vid);

	    	
	    	vb.setAdminId(uid);
	    	SqlADO.updateSeller(vb);
			pw.print("Update complete");
			return;
	    }
	    else
	    {
			pw.print("不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_VIEW_VENDER]+", You don't have permission to access");
			return;
	    }

	}
}
