package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ado.SqlADO;
import com.tools.ToolBox;

import beans.PortBean;
import beans.UserBean;
import beans.clsGoodsBean;

/**
 * Servlet implementation class jsonGoodsList
 */
@WebServlet("/jsonGoodsList")
public class jsonGoodsList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public jsonGoodsList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
	    
		if(!ub.AccessAble(UserBean.FUNID_CAN_VIEW_GOODS))
		{
			request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_VIEW_GOODS]);
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}
		ArrayList<clsGoodsBean> gblst=clsGoodsBean.getGoodsBeanLst(ub.getGroupid());
		
		JSONArray jsonArray=new JSONArray();
		JSONObject json=null;
		for (clsGoodsBean goodsBean : gblst) 
		{
			if(goodsBean==null)
			{
				continue;
			}
			json=new JSONObject();
			json.put("id",  goodsBean.getId());
			json.put("goodsname", goodsBean.getGoodsname());
			json.put("price", goodsBean.getPrice()/100.0);
			json.put("des1", goodsBean.getDes1());
			json.put("des2", goodsBean.getDes2());
			json.put("des3", goodsBean.getDes3());
			json.put("picname", goodsBean.getPicname());
			jsonArray.add(json);
		}
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("Rows", jsonArray);
		jsonObject.put("Total",gblst.size());
		out.print(jsonObject.toString());
		
	}

}
