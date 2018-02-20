package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ado.SqlADO;
import com.tools.ToolBox;

import beans.JsonMsgBean;
import beans.UserBean;
import beans.clsGroupBean;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class DelGroup
 */
@WebServlet("/DelGroup")
public class DelGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DelGroup() {
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
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter pw=response.getWriter();
		UserBean ub=(UserBean)request.getSession().getAttribute("usermessage");	
		
		JsonMsgBean jsmb=new JsonMsgBean();
		if(ub==null)
		{
			jsmb.setMsg(1);
			jsmb.setDetail("您没有登录或无权访问！请联系管理员！");
			pw.print(JSONObject.fromObject(jsmb).toString());
			return;
		}
		
		if(!ub.AccessAble(UserBean.FUNID_CAN_ACCESS_WEB))
		{
			jsmb.setMsg(1);
			jsmb.setDetail("不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ACCESS_WEB]+"，您没有权限");
			pw.print(JSONObject.fromObject(jsmb).toString());
			return;
		}
	    
	    
	    if(!ub.AccessAble(UserBean.FUNID_CAN_DEL_GROUP_ID))
		{
			jsmb.setMsg(1);
			jsmb.setDetail("不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_DEL_GROUP_ID]+"，您没有权限");
			pw.print(JSONObject.fromObject(jsmb).toString());
			return;
		}

	    int groupid =ToolBox.filterInt(request.getParameter("groupid"));
	    clsGroupBean.DeleteclsGroupBean(groupid);/*删除所有的售货机*/
		jsmb.setMsg(0);
		jsmb.setDetail("OK");
		pw.print(JSONObject.fromObject(jsmb).toString());
		return;

	}

}
