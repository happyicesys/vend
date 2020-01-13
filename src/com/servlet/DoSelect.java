package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.PortBean;
import beans.UserBean;

import com.clsEvent;
import com.ado.SqlADO;
import com.tools.ToolBox;

/**
 * Servlet implementation class DoSelect
 */
public class DoSelect extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DoSelect() {
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
	    	out.print(ToolBox.ErrRequest("Please login or you don't have permission to access"));
	    	return;
	    } 


		String retstr;
		int colid= ToolBox.filterInt(request.getParameter("colid"));
		
		int goodsid=ToolBox.filterInt(request.getParameter("goodsid"));
		
		if((colid==0)&&(goodsid==0))
		{
	    	out.print(ToolBox.ErrRequest("Parameter error!"));
	    	return;
		}
		
		//System.out.println(colid);
		
		PortBean portBean=SqlADO.getPortBean(colid);
		if(portBean!=null)
		{
			if(portBean.getGoodsid()!=goodsid)
			{
				clsEvent.WriteMCFlg(portBean.getMachineid(),clsEvent.FLG_SET_COL_NAME);
				SqlADO.SetPortGoods(colid, goodsid);
			}
		}
		
		out.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.append("<HTML>");
		out.append("  <HEAD>");
		out.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
		out.append("<TITLE>");
		out.append(ToolBox.WEB_NAME);

		out.append("</TITLE><link href=\"./css/styles.css\" rel=\"stylesheet\" type=\"text/css\" /></HEAD>");
		out.append("  <BODY onload=\"window.opener.location.reload();window.close();\">");

		out.append("  </BODY>");
		out.append("</HTML>");
		
		
		out.print(ToolBox.TipRequest("Product binding successful"));
	}

}
