package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import beans.UserBean;

import com.clsConst;
import com.ado.SqlADO;
import com.tools.ToolBox;

/**
 * Servlet implementation class jsonUser
 */
@WebServlet("/jsonUser")
public class jsonUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public jsonUser() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		HttpSession session=request.getSession();
	    request.setCharacterEncoding("UTF-8");
	    response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		UserBean ub=(UserBean)session.getAttribute("usermessage");
		PrintWriter out= response.getWriter();
		JSONObject json_retJsonObject=new JSONObject();
		if(ub==null)
		{
			json_retJsonObject.put("errlevel", clsConst.ERR_LEVEL_CANT_ACCESS);
			json_retJsonObject.put("errdes", "您没有登录或无权访问！请联系管理员！");
			out.print(json_retJsonObject.toString());
			return;
		}
		
		if(!ub.AccessAble(UserBean.FUNID_CAN_VIEW_USER_LST))
		{
			json_retJsonObject.put("errlevel", clsConst.ERR_LEVEL_NO_OPERATION_RIGHT);
			json_retJsonObject.put("errdes", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_VIEW_USER_LST]);
			out.print(json_retJsonObject.toString());
			return;
		}
		
		if(!ub.AccessAble(UserBean.FUNID_CAN_UPDATE_USER))
		{
			json_retJsonObject.put("errlevel", clsConst.ERR_LEVEL_NO_OPERATION_RIGHT);
			json_retJsonObject.put("errdes", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_UPDATE_USER]);
			out.print(json_retJsonObject.toString());
			return;
		}
		int uid=ToolBox.filterInt(request.getParameter("uid"));
		int activeid=ToolBox.filterInt(request.getParameter("activeid"));
		
		if(uid==0)
		{
			json_retJsonObject.put("errlevel", clsConst.ERR_LEVEL_OPERATION_ERR);
			json_retJsonObject.put("errdes", "参数有误，请确认您的操作是否正确?");
			out.print(json_retJsonObject.toString());
			return;
		}
		
		if(activeid==clsConst.ACTION_JSON_GET_USER)
		{
			UserBean uBean=UserBean.getUserBeanById(uid);
			if((uBean==null)||(ub.getId()==uBean.getId()))
			{
				json_retJsonObject.put("errlevel", clsConst.ERR_LEVEL_OPERATION_ERR);
				json_retJsonObject.put("errdes", "参数有误，请确认您的操作是否正确?");
				out.print(json_retJsonObject.toString());
				return;
			}
			JSONObject json=new JSONObject();
			json.put("uid", uBean.getId());
			json.put("adminname", uBean.getAdminname());
			json.put("adminusername", uBean.getAdminusername());
			json.put("adminaddress", uBean.getAdminaddress());
			json.put("adminmobilephone", uBean.getAdminmobilephone());
			json.put("lastloginip", uBean.getLastloginip());
			json.put("lastLoginTime", ToolBox.getYMDHM(uBean.getLastLoginTime()));
			json.put("createtime", ToolBox.getYMDHM(uBean.getCreatetime()));
			json.put("adminrights",uBean.getAdminrights());
			json.put("VenderId", uBean.getVenderId());
			json.put("adminsex", uBean.getAdminsex());
			json.put("admintelephone", uBean.getAdmintelephone());
			json.put("groupid", uBean.getGroupid());
			
			json_retJsonObject.put("errlevel", clsConst.ERR_LEVEL_NO_ERR);
			json_retJsonObject.put("errdes", "ok");
			json_retJsonObject.put("datasrc", json);
			System.out.println(json_retJsonObject.toString());
			out.print(json_retJsonObject.toString());
		}
		else if(activeid==clsConst.ACTION_JSON_DELETE_USER)
		{
			UserBean.deleteUser(uid);
			json_retJsonObject.put("errlevel", clsConst.ERR_LEVEL_NO_ERR);
			json_retJsonObject.put("errdes", "亲，用户信息已经成功删除！");
			System.out.println(json_retJsonObject.toString());
			out.print(json_retJsonObject.toString());
		}
		else {
			json_retJsonObject.put("errlevel", clsConst.ERR_LEVEL_OPERATION_ERR);
			json_retJsonObject.put("errdes", "参数有误，请确认您的操作是否正确?");
			out.print(json_retJsonObject.toString());
			return;
		}
	}
}
