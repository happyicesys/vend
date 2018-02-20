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

import com.ado.SqlADO;
import com.tools.ToolBox;

import beans.UserBean;
import beans.clsGroupBean;

/**
 * Servlet implementation class AddGroup
 */
@WebServlet("/AddGroup")
public class AddGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddGroup() {
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
		
		if(!ub.AccessAble(UserBean.FUNID_CAN_ACCESS_WEB))
		{
			request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ACCESS_WEB]);
			request.setAttribute("LAST_URL", "index.jsp");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}

		if(!ub.AccessAble(UserBean.FUNID_CAN_CREATE_GROUP_ID))
		{
			request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_CREATE_GROUP_ID]);
			request.setAttribute("LAST_URL", "index.jsp");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}

		
		clsGroupBean bean=new clsGroupBean();
		
		String groupname=ToolBox.filter(request.getParameter("groupname"));
		String groupdes=ToolBox.filter(request.getParameter("groupdes"));

		String notify_url=ToolBox.filter(request.getParameter("notify_url"));
		
		String wx_notify_url=ToolBox.filter(request.getParameter("wx_notify_url"));
		String wx_appid=ToolBox.filter(request.getParameter("wx_appid"));
		String wx_mch_id=ToolBox.filter(request.getParameter("wx_mch_id"));
		String wx_key=ToolBox.filter(request.getParameter("wx_key"));

		String al_APP_ID=ToolBox.filter(request.getParameter("al_APP_ID"));
		String al_PRIVATE_KEY=ToolBox.filter(request.getParameter("al_PRIVATE_KEY"));
		String al_PUBLIC_KEY=ToolBox.filter(request.getParameter("al_PUBLIC_KEY"));
		String ServerIp=ToolBox.filter(request.getParameter("ServerIp"));
		String al_SIGN_TYPE=ToolBox.filter(request.getParameter("al_SIGN_TYPE"));
		String al_PARTNER_ID=ToolBox.filter(request.getParameter("al_PARTNER_ID"));
		
		String WelcomeMessage=ToolBox.filter(request.getParameter("WelcomeMessage"));
		String AppSecret=ToolBox.filter(request.getParameter("AppSecret"));
		
		bean.setGroupname(groupname);
		bean.setCreattime(new Timestamp(System.currentTimeMillis()));
		bean.setAl_SIGN_TYPE(al_SIGN_TYPE);
		bean.setAdminusername(ub.getAdminname());
		bean.setAl_APP_ID(al_APP_ID);
		bean.setAl_PRIVATE_KEY(al_PRIVATE_KEY);
		bean.setAl_PUBLIC_KEY(al_PUBLIC_KEY);
		bean.setALIPAY_PUBLIC_KEY(al_PUBLIC_KEY);
		bean.setGroupdes(groupdes);
		bean.setNotify_url(notify_url);
		bean.setServerIp(ServerIp);
		bean.setUpdatetime(new Timestamp(System.currentTimeMillis()));
		bean.setWx_appid(wx_appid);
		bean.setWx_key(wx_key);
		bean.setWx_mch_id(wx_mch_id);
		bean.setWx_notify_url(wx_notify_url);
		bean.setAl_PARTNER(al_PARTNER_ID);		
		bean.setWelcomeMessage(WelcomeMessage);		
		bean.setAppsecret(AppSecret);
		clsGroupBean.InsertclsGroupBean(bean);
		response.sendRedirect("GroupList.jsp");
	}
	
	

}
