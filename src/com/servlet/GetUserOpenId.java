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

import beans.UserBean;
import beans.clsGroupBean;
import weixin.popular.api.SnsAPI;
import weixin.popular.bean.sns.SnsToken;

/**
 * Servlet implementation class GetUserOpenId
 */
@WebServlet("/GetUserOpenId")
public class GetUserOpenId extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetUserOpenId() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");		
		PrintWriter pw=response.getWriter();
		int uid=ToolBox.filterInt(request.getParameter("id"));
	    String nonce=ToolBox.filter(request.getParameter("nonce"));
	    
	    UserBean ub=UserBean.getUserBeanById(uid);

	    if(ub!=null)
	    {
	    	String key=ToolBox.filter(request.getParameter("state"));
	    	String state=ToolBox.getMd5(ub.getAdminpassword()+ub.getId()+nonce);
	    	if(key.equals(state))
	    	{
			    clsGroupBean groupBean=clsGroupBean.getGroup(ub.getGroupid());
			    
		    	String AppSecret=groupBean.getAppsecret();
		    	String appid=groupBean.getWx_appid();
		    	String code=request.getParameter("code");
		    	SnsToken token =SnsAPI.oauth2AccessToken(appid, AppSecret, code);
		    	ub.setWx_openid(token.getOpenid());
		    	UserBean.updateUser(ub);
		    	pw.println("<html> <body style=\"font-size:2em\"> 微信OPENID获取成功，请刷新用户信息页面！ </body> </html>");
	    	}
	    	else
	    	{
	    		pw.println("<html> <body style=\"font-size:2em\"> 用户无效！ </body> </html>");
	    	}
	    }
	    else
	    {
	    	pw.println("<html> <body style=\"font-size:2em\"> 参数错误！ </body> </html>");
	    }
    	
    	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
