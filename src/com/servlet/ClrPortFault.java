package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.UserBean;

import com.clsConst;
import com.clsEvent;
import com.ado.SqlADO;
import com.tools.ToolBox;

/**
 * Servlet implementation class ClrPortFault
 */
@WebServlet("/ClrPortFault")
public class ClrPortFault extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClrPortFault() {
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
			pw.println("更新失败");
	    	return;		
		}else if(ub.getType()==0)//游客账号
		{
			pw.println("更新失败");
	    	return;
		}
		else
		{
			int SellerId=ToolBox.filterInt(request.getParameter("mid"));
			int innerid=ToolBox.filterInt(request.getParameter("portid"));			
			if(ub.CanManageThisVender(SellerId))
			{
				SqlADO.updatePortFault(SellerId,innerid,clsConst.ERR_NO_ERR);
				clsEvent.WriteMCFlg(SellerId,clsEvent.FLG_SET_COLLIST);
				//response.sendRedirect("PortList.jsp?id="+SellerId);
				pw.println("更新成功");
			}
			else
			{
				//pw.println(ToolBox.CANNTACCESS);
				pw.println("更新失败");
			}
		}
	}

}
