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

import beans.ClsLiuyan;
import beans.UserBean;

/**
 * Servlet implementation class AddLiuyan
 */
@WebServlet("/AddLiuyan")
public class AddLiuyan extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddLiuyan() {
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
	    
	    
	    if(!ub.AccessAble(UserBean.FUNID_CAN_ADD_USER))
		{
			request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ADD_USER]);
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}
	    
	    String username=ToolBox.filter(request.getParameter("username"));
	    
	    if(!username.equals(ub.getAdminusername()))
	    {
			request.setAttribute("message", "非法用户名！");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
	    }
	    
	    if(username.equals(""))
	    {
			request.setAttribute("message", "用户名不能为空！");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
	    }
	    

	    
	    String mobiletel=ToolBox.filter(request.getParameter("mobiletel"));
		String realname=ToolBox.filter(request.getParameter("realname"));    
		
	    String time=ToolBox.filter(request.getParameter("time"));
		String content=ToolBox.filter(request.getParameter("content"));   
		
	    if(mobiletel.equals(""))
	    {
			request.setAttribute("message", "手机号码不能为空！");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
	    }
	    
	    if(content.equals(""))
	    {
			request.setAttribute("message", "内容不能为空！");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
	    }
		
	    if(content.length()>200)
	    {
			request.setAttribute("message", "内容不能大于200个字符");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
	    }

	    ClsLiuyan cly=new ClsLiuyan();
	    cly.setContent(content);
	    cly.setMobiletel(mobiletel);
	    cly.setRealname(realname);
	    cly.setTime(time);
	    cly.setUsername(username);
	    
	    SqlADO.addLiuyan(cly);
	    
		request.setAttribute("message", "留言发送成功，感谢您的建议！");
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;

	}

}
