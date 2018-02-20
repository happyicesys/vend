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
 * Servlet implementation class DoDeletcGoods
 */
public class DoDeletcGoods extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DoDeletcGoods() {
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
	    
		int goodsid= ToolBox.filterInt(request.getParameter("goodsid"));
		
		if(goodsid==0)
		{
	    	request.setAttribute("message", "产品参数无效！");
	    	request.getRequestDispatcher("message.jsp").forward(request, response);
	    	return;
		}
		
		
		
		if(SqlADO.IsGoodsBind(goodsid))
		{
	    	request.setAttribute("message", "产品与货道有绑定关系,请先取消产品与货道的绑定关系！");
	    	request.setAttribute("LAST_URL", "GoodsList");	
	    	request.getRequestDispatcher("message.jsp").forward(request, response);
	    	return;
		}
		else
		{
			/*删除图片*/
			clsGoodsBean goodsBean=clsGoodsBean.getGoodsBean(goodsid);
			String p=request.getRealPath("images_little")+"\\"+goodsBean.getPicname();
			File file=new File(p);
			file.delete();
			SqlADO.deleteGoods(goodsid);
	    	request.setAttribute("message", "产品删除成功");
	    	request.setAttribute("LAST_URL", "GoodsList");	    	
	    	request.getRequestDispatcher("message.jsp").forward(request, response);
	    	return;
		}
	}

}
