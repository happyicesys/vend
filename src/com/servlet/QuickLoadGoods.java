package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ClsTime;
import com.clsConst;
import com.clsEvent;
import com.ado.SqlADO;
import com.tools.ToolBox;

import beans.PortBean;
import beans.UserBean;
import beans.clsGoodsBean;

/**
 * Servlet implementation class QuickLoadGoods
 */
public class QuickLoadGoods extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuickLoadGoods() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		}else if(!ub.AccessAble(UserBean.FUNID_CAN_UPDATE_PORT))
		{
			request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_UPDATE_PORT]);
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}
		
		int SellerId=ToolBox.filterInt(request.getParameter("vid"));
		/*检测是否能够管理这台机器*/
		if(!ub.CanManageThisVender(SellerId))
		{
			request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_UPDATE_PORT]);
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;				
		}
		
		ArrayList<PortBean> lstArrayList=SqlADO.getPortBeanList(SellerId);
		boolean need_load=false;
		for (PortBean portBean : lstArrayList) {
			if(portBean!=null)
			{
				if(portBean.getAmount()!=portBean.getCapacity())
				{
					need_load=true;
					break;
				}
			}
		}
		
		
		if(need_load)
		{
			SqlADO.SetPortGoodsCount(SellerId);
			//clsEvent.WriteMCFlg(SellerId, clsEvent.FLG_SET_COLLIST);
			SqlADO.AddTableCmd(SellerId, clsConst.TABLE_CMD_PUSH_SLOT, "", 
					2000, ClsTime.SystemTime(), "推送货道数据");
		}
		
		response.sendRedirect("PortList.jsp?mid="+SellerId);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
