package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.clsConst;
import com.ado.SqlADO;
import com.tools.ToolBox;

import beans.TradeBean;
import beans.UserBean;
import beans.clsGroupBean;
import weixin.popular.util.WxCoporTransfor;

/**
 * Servlet implementation class DoTranfor
 */
@WebServlet("/DoTranfor")
public class DoTranfor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DoTranfor() {
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
		if(ub==null)
		{
			pw.print("您没有登录或无权访问！请联系管理员！");
			return;
		}
		
		if(!ub.AccessAble(UserBean.FUNID_CAN_ACCESS_WEB))
		{
			pw.print("不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ACCESS_WEB]+"，您没有权限");
			return;
		}
	    
	    
	    if(!ub.AccessAble(UserBean.FUNID_CAN_REFUND))
		{
			pw.print("不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_REFUND]+"，您没有权限");
			return;
		}
	    if(!ub.getAdminusername().toLowerCase().equals(clsConst.POWER_USER_NAME))
	    {
			pw.print("不被允许小额转帐，您没有权限");
			return;
	    }

	    String pwd =ToolBox.getMd5(request.getParameter("pwd"));
	    
	    
	    
	    if(!ub.getAdminpassword().equals(pwd))
	    {
			pw.print("密码错误，请重新输入密码");
			return;
	    }
	    double amount=ToolBox.filterDble(request.getParameter("amount"));
	    
	    if(amount>1000.0)
	    {
			pw.print("金额过大");
			return;
	    }
	    int uid=ToolBox.filterInt(request.getParameter("userlist"));
	    
	    String des=ToolBox.filter(request.getParameter("des"));
	    if(uid==0)
	    {
			pw.print("无效的编号");
			return;
	    }
	    
	    UserBean userBean=UserBean.getUserBeanById(uid);
	    clsGroupBean groupBean=clsGroupBean.getGroup(userBean.getGroupid());
	    pw.print(WxCoporTransfor.CreateTransfor(groupBean,userBean,(int)(amount*100),des,ub.getAdminusername()+"-"+ub.getAdminname()));
		
	}

}
