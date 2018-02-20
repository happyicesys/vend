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
import beans.PortBean;
import beans.UserBean;

import com.ado.SqlADO;
import com.tools.ToolBox;

/**
 * Servlet implementation class jsonUserList
 */
@WebServlet("/jsonUserList")
public class jsonUserList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public jsonUserList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
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
			request.setAttribute("message", "您没有登录或无权访问！请联系管理员！");
			request.setAttribute("LAST_URL", "index.jsp");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}

		ArrayList<UserBean> uList=UserBean.getUserBeanListByright();
		JSONObject jsonObject=new JSONObject();
		JSONObject json=null;
		JSONArray json2=new JSONArray();
		for (UserBean uBean : uList) 
		{
			if((uBean!=null)&&(ub.getId()!=uBean.getId()))
			{
				json=new JSONObject();
				json.put("id", uBean.getId());
				json.put("adminname", uBean.getAdminname());
				json.put("adminusername", uBean.getAdminusername());
				json.put("adminaddress", uBean.getAdminaddress());
				json.put("adminmobilephone", uBean.getAdminmobilephone());
				json.put("lastloginip", uBean.getLastloginip());
				json.put("lastLoginTime", ToolBox.getYMDHM(uBean.getLastLoginTime()));
				json2.add(json);
			}
		}
		jsonObject.put("Rows", json2);
		jsonObject.put("Total",uList.size());
		
		System.out.println(jsonObject.toString());
		out.print(jsonObject.toString());
	}

}
