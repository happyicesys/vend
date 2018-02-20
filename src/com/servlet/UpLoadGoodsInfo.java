package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import beans.UserBean;
import beans.clsGoodsBean;

import com.ado.SqlADO;
import com.jspsmart.upload.File;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.tools.ToolBox;

/**
 * Servlet implementation class UpLoadGoodsInfo
 */
public class UpLoadGoodsInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpLoadGoodsInfo() {
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
		
		if(!ub.AccessAble(UserBean.FUNID_CAN_ACCESS_WEB))
		{
			request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ACCESS_WEB]);
			request.setAttribute("LAST_URL", "index.jsp");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}
		
		if(!ub.AccessAble(UserBean.FUNID_CAN_UPDATE_GOODS))
		{
			request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_UPDATE_GOODS]);
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}


		String goodsname= ToolBox.filter(request.getParameter("goodsname"));
		
		
		
		if(goodsname.trim().equalsIgnoreCase(("")))
		{
			request.setAttribute("message", "产品名称无效！");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}
		
		clsGoodsBean gb=clsGoodsBean.getGoodsBean(goodsname,ub.getGroupid());
		if(gb!=null)
		{
			request.setAttribute("message", "产品已经存在！");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}


		String des1=ToolBox.filter(request.getParameter("des1"));
		String des2=null;
				/*ToolBox.filter(request.getParameter("des2"));*/
		String des3=null;
				/*ToolBox.filter(request.getParameter("des3"));*/
		String pic1=ToolBox.filter(request.getParameter("pic1"));
		double price =ToolBox.filterDble(request.getParameter("price"));
		price*=100.0;
		clsGoodsBean.AddGoodsInfo(goodsname,(int)price, des1, des2, des3, pic1,ub.getGroupid());
		response.sendRedirect("GoodsList");
	}

}
