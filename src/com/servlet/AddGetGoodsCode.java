package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ado.SqlADO;
import com.tools.ToolBox;

import beans.UserBean;
import beans.clsGetGoodsCode;
import beans.clsGoodsBean;

/**
 * Servlet implementation class AddUser
 */
public class AddGetGoodsCode extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddGetGoodsCode() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
	    
	    
		if(!ub.AccessAble(UserBean.FUNID_CAN_ADD_FETCH_GOODS_CODE))
		{
			request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ADD_FETCH_GOODS_CODE]);
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}
	    
	    clsGetGoodsCode tub=new clsGetGoodsCode();

		tub.setTradeno(ToolBox.filter(request.getParameter("tradeno")));
		tub.setGoodsid(ToolBox.filter(request.getParameter("goodsid")));
		tub.setTotalfee(ToolBox.filter(request.getParameter("totalfee")));
		tub.setTransforstatus(0);
		
		SqlADO.InsertGetGoodsCode(tub);
		response.sendRedirect("FetchGoodsCodeList.jsp");
	    
	}

}
