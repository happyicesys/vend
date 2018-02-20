package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.UserBean;

import com.clsEvent;
import com.ado.SqlADO;
import com.tools.ToolBox;

/**
 * Servlet implementation class WriteEvent
 */
public class WriteEvent extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WriteEvent() {
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
			pw.print("您没有登录或无权访问！请联系管理员！");
			return;
		}
		
		if(!ub.AccessAble(UserBean.FUNID_CAN_ACCESS_WEB))
		{
			pw.print("不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ACCESS_WEB]);
			return;
		}

		if(!ub.AccessAble(UserBean.FUNID_CAN_UPDATE_VENDER))
		{
			pw.print("不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_UPDATE_VENDER]);
			return;
		}

		String strString=request.getParameter("action");
		if(strString==null)
		{
			pw.print("参数错误！");
			return;
		}
		int action=ToolBox.filterInt(strString);
		
		strString=request.getParameter("obj");
		if(strString==null)
		{
			pw.print("参数错误！");
			return;
		}
		int obj=ToolBox.filterInt(strString);
		
		strString=request.getParameter("mid");
		if(strString==null)
		{
			pw.print("参数错误！");
			return;
		}
		int mid=ToolBox.filterInt(strString);
		

		switch(obj)
		{
//		case clsEvent.GT_EVENT:
//			if(!ub.AccessAble(UserBean.FUNID_CAN_UPDATA_GT_FIRM))
//			{
//				pw.print("不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_UPDATA_GT_FIRM]);
//				return;
//			}
//			clsEvent.WriteGTFlg(mid, action);
//			
//			pw.print("GT更新固件事件标示已经写入！");
//			break;			
//		case clsEvent.VMC_EVENT:
//			if(!ub.AccessAble(UserBean.FUNID_CAN_UPDATA_VMC_FIRM))
//			{
//				pw.print("不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_UPDATA_VMC_FIRM]);
//				return;
//			}
//			clsEvent.WriteMCFlg(mid, action);
//			
//			pw.print("VMC更新固件事件标示已经写入！");
//			break;
		}
		
		return;
	}
}
