package com.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.UserBean;
import beans.clsGoodsBean;

import com.ado.SqlADO;
import com.tools.ToolBox;

/**
 * Servlet implementation class EditGoods
 */
public class EditGoods extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditGoods() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		HttpSession session=request.getSession();
	    request.setCharacterEncoding("UTF-8");
	    response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		UserBean ub=(UserBean)session.getAttribute("usermessage");
		PrintWriter out= response.getWriter();
		
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
	    
	    
	    if(!ub.AccessAble(UserBean.FUNID_CAN_UPDATE_GOODS))
	    {
	    	request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_UPDATE_GOODS]);
	    	request.getRequestDispatcher("message.jsp").forward(request, response);
	    	return;
	    }

		String goodsname= ToolBox.filter(request.getParameter("goodsname"));
		
		int goodsid=ToolBox.filterInt(request.getParameter("goodsid"));
		
		if(goodsid==0)
		{
	    	request.setAttribute("message", "Invalid product parameter");
	    	request.getRequestDispatcher("message.jsp").forward(request, response);
	    	return;
		}

		if(goodsname==null)
		{
	    	request.setAttribute("message", "Invalid product name");
	    	request.getRequestDispatcher("message.jsp").forward(request, response);
	    	return;
		}
		else if(goodsname.trim().equalsIgnoreCase(("")))
		{
	    	request.setAttribute("message", "Invalid product name");
	    	request.getRequestDispatcher("message.jsp").forward(request, response);
	    	return;
		}

		clsGoodsBean gb=clsGoodsBean.getGoodsBean(goodsname,ub.getGroupid());
		if(gb!=null)
		{
			if(gb.getId()!=goodsid)
			{
				request.setAttribute("message", "Product existed");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}
		}
		

		String des1=ToolBox.filter(request.getParameter("des1"));
		String des2=null;
				/*ToolBox.filter(request.getParameter("des2"));*/
		String des3=null;
				/*ToolBox.filter(request.getParameter("des3"));*/
		
		String pic1=ToolBox.filter(request.getParameter("pic1"));
		
		double price=ToolBox.filterDble(request.getParameter("price"));
		
		price*=100.0;
		if(pic1!=null)
		{

		}
		else
		{
			clsGoodsBean goodsBean=clsGoodsBean.getGoodsBean(goodsid);
			pic1=goodsBean.getPicname();
		}
		SqlADO.UpdateGoods(goodsid,(int)price, goodsname, des1, des2, des3, pic1);

		response.sendRedirect("GoodsList");
		
	}

}
