package com.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.ObjectUtils;

import weixin.popular.util.StringUtils;
import beans.UserBean;
import beans.VenderBean;
import beans.clsGoodsBean;

import com.ado.SqlADO;
import com.tools.StringUtil;
import com.tools.ToolBox;

/**
 * Servlet implementation class EditGoods
 */
public class VenderList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VenderList() {
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
	    
	    
	    if(!ub.AccessAble(UserBean.FUNID_CAN_VIEW_VENDER))
	    {
	    	request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_UPDATE_GOODS]);
	    	request.getRequestDispatcher("message.jsp").forward(request, response);
	    	return;
	    }

		session.setAttribute("currentpage", request.getRequestURI());
		String SellerId= ToolBox.filter(request.getParameter("sellerid"));
		String terminalName = ToolBox.filter(request.getParameter("TerminalName"));
		Boolean isOnline = ToolBox.filterBol(request.getParameter("isOnline"));
		int tempCat = ToolBox.filterInt(request.getParameter("tempCat"));
		int pageindex=ToolBox.filterInt(request.getParameter("page"));

		if(pageindex==0)
		{
			pageindex=1;
		}
		int count_per_page =ub.getPagecount();
		ArrayList<VenderBean> lst = null;
		int RsCount=0;
		if(!StringUtil.isBlank(SellerId) || !StringUtil.isBlank(terminalName))
		{
			lst=SqlADO.getVenderListByParams(SellerId, terminalName, isOnline, tempCat);
		}else
		{
			//VenderBean vb=SqlADO.getVenderBeanByid(Integer.parseInt(SellerId));
			lst=SqlADO.getVenderListByIdLimint(ub.getVenderLimite());
		}
//		RsCount = lst.size();
//		request.setAttribute("RsCount", RsCount);
		request.setAttribute("lst", lst);
		request.setAttribute("count_per_page", count_per_page);
		request.setAttribute("pageindex", pageindex);
		request.setAttribute("id", SellerId);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("VenderList.jsp");    // 使用req对象获取RequestDispatcher对象
        dispatcher.forward(request, response);                                            // 使用RequestDispatcher对象在服务器端向目的路径跳转

//		response.sendRedirect("goodsList.jsp");
		
	}

}
