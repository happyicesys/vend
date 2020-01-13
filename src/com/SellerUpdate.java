package com;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import beans.VenderBean;
import beans.UserBean;
import com.tools.ToolBox;
import com.ado.SqlADO;
/**
 * Servlet implementation class SellerUpdate
 */
public class SellerUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SellerUpdate() {
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
		// TODO Auto-generated method stub
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
		
		if(!ub.AccessAble(UserBean.FUNID_CAN_UPDATE_VENDER))
		{
			request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_UPDATE_VENDER]);
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}
		
		int id=ToolBox.filterInt(request.getParameter("id"));
	    if(!ub.CanAccessSeller(id))
	    {
			request.setAttribute("message", ToolBox.CANNTACCESS);
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
	    }

	    	boolean vend_changed=false;
	    	
	    	
			VenderBean vb=SqlADO.getVenderBeanByid(id);
			
			vb.setCanUse(ToolBox.filterBol(request.getParameter("canuse")));
			//vb.setGoodsPortCount(ToolBox.filterInt(request.getParameter("portcount")));
			
			vb.setHuodongId(ToolBox.filterInt(request.getParameter("huodongid")));
			
			vb.setSellerTyp(ToolBox.filter(request.getParameter("sellertype")));
			vb.setTerminalAddress(ToolBox.filter(request.getParameter("address")));
			vb.setTerminalName(ToolBox.filter(request.getParameter("tname")));
			vb.setAuto_refund(ToolBox.filterInt(request.getParameter("auto_refund")));
			vb.setTemp_alert(ToolBox.filterInt(request.getParameter("temp_alert")));
			vb.setTempAlertExtraEmails(ToolBox.filter(request.getParameter("temp_alert_extra_emails")));
			vb.setM_AllowUpdateGoodsByPc(ToolBox.filterInt(request.getParameter("AllowUpdateGoodsByPc")));
			//System.out.println(vb.getAuto_refund());
			String tipmes= request.getParameter("tipmes");
			if(!vb.getTipMesOnLcd().equals(tipmes))
			{
				vend_changed=true;
				vb.setTipMesOnLcd(tipmes);
			}
			
			vb.setJindu(ToolBox.filterDble(request.getParameter("lng")));
			vb.setWeidu(ToolBox.filterDble(request.getParameter("lat")));
			
			String telString=request.getParameter("server_tel");
			if(!vb.getTelNum().equals(telString))
			{
				vend_changed=true;
				vb.setTelNum(telString);
			}
			
			String vmcfile=request.getParameter("firm_text");
			vb.setVmc_firmfile(vmcfile);
			if(vmcfile!=null)
			{
				if(!vmcfile.equals(""))
				{
					/*添加售货机固件更新事件*/
					clsEvent.WriteMCFlg(vb.getId(),clsEvent.FLG_UPDATE_FLASH);
				}
			}
			
			SqlADO.updateSeller(vb);
			
			if(vend_changed)
			{
				clsEvent.WriteMCFlg(id,clsEvent.FLG_SET_MECHINE_DATA);
			}
			
			response.sendRedirect("VenderMod.jsp?result=OK&mid="+vb.getId());

	}
}
