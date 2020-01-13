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

import weixin.popular.util.StringUtils;
import beans.UserBean;
import beans.clsGoodsBean;

import com.ado.SqlADO;
import com.tools.StringUtil;
import com.tools.ToolBox;

/**
 * Servlet implementation class EditGoods
 */
public class GoodsList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoodsList() {
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
	    
	    
	    if(!ub.AccessAble(UserBean.FUNID_CAN_VIEW_GOODS))
	    {
	    	request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_VIEW_GOODS]);
	    	request.getRequestDispatcher("message.jsp").forward(request, response);
	    	System.out.println("no right");
	    	return;
	    }

		String goodsname= ToolBox.filter(request.getParameter("goodsname"));
		int pageindex=ToolBox.filterInt(request.getParameter("page"));
    	System.out.println(goodsname);
		if(pageindex==0)
		{
			pageindex=1;
		}
		int count_per_page =ub.getPagecount();
		ArrayList<clsGoodsBean> gblst = null;
		int RsCount=0;
		int groupid=ub.getGroupid();
		if(StringUtil.isBlank(goodsname))
		{
			RsCount =clsGoodsBean.getGoodsBeanCount(groupid);
			
			gblst=clsGoodsBean.getGoodsBeanLst(pageindex,count_per_page,groupid);
			
		}else
		{

//			clsGoodsBean gb=SqlADO.getGoodsBean(goodsname,groupid);
//			if(gb!=null)
//			{
				
				RsCount =clsGoodsBean.getGoodsBeanCount(goodsname,groupid);
				gblst=clsGoodsBean.getGoodsBeanLst(pageindex,count_per_page,goodsname,groupid);
				
//			}
		}
		request.setAttribute("RsCount", RsCount);
		request.setAttribute("gblst", gblst);
		request.setAttribute("count_per_page", count_per_page);
		request.setAttribute("pageindex", pageindex);
		request.setAttribute("goodsname", goodsname);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("goodsList.jsp");    // 使用req对象获取RequestDispatcher对象
        dispatcher.forward(request, response);                                            // 使用RequestDispatcher对象在服务器端向目的路径跳转

        session.setAttribute("currentpage", request.getRequestURI()+"?"+request.getQueryString());
		
	}

}
