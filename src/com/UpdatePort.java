package com;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.PortBean;
import beans.UserBean;

import com.ado.SqlADO;
import com.tools.ToolBox;

/**
 * Servlet implementation class UpdatePort
 */
public class UpdatePort extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdatePort() {
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
			pw.println(0);
	    	return;		
		}else if(ub.getType()==0)//游客账号
		{
			pw.println(0);
	    	return;
		}
		else
		{
			int SellerId=ToolBox.filterInt(request.getParameter("id"));
			int innerid=ToolBox.filterInt(request.getParameter("innerid"));			
			if(ub.CanManageThisVender(SellerId))
			{
				SqlADO.updatePortFault(SellerId,innerid,clsConst.ERR_NO_ERR);
				clsEvent.WriteMCFlg(SellerId,clsEvent.FLG_SET_COLLIST);
				response.sendRedirect("PortList.jsp?id="+SellerId);
			}
			else
			{
				pw.println(ToolBox.CANNTACCESS);
			}
		}
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
		}else if(!ub.AccessAble(UserBean.FUNID_CAN_UPDATE_PORT))
		{
			request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_UPDATE_PORT]);
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}
		else
		{
			int SellerId=ToolBox.filterInt(request.getParameter("id"));
			/*检测是否能够管理这台机器*/
			if(!ub.CanManageThisVender(SellerId))
			{
				request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_UPDATE_PORT]);
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;				
			}
			else
			{
				int amount=0;
				int capacity=0;
				float value=(float) 0.00;
				int price=0;
				int discount=0;
				
				String str=null;
				boolean port_changed=false;
				for(int i=0;i<255;i++)
				{
					str=request.getParameter("amount"+i);
					if(str!=null)
					{

						
						PortBean p=SqlADO.getPortBean(SellerId, i);
						if(p!=null)
						{
							amount=ToolBox.filterInt(str);
							capacity=ToolBox.filterInt(request.getParameter("capacity"+i));
							
							price=(int)(value*100);	
							
							value=ToolBox.filterFlt(request.getParameter("discount"+i));
							discount=(int)(value*100);	
							
							if((p.getAmount()!=amount)||
								(p.getCapacity()!=capacity)||
								(p.getPrice()!=price)||
								(p.getDiscount()!=discount)
								)
							{
								SqlADO.updatePort(SellerId,i,amount,capacity,price,discount);
								port_changed=true;
							}
						}
					}
				}
				if(port_changed)
				{
					//clsEvent.WriteMCFlg(SellerId,clsEvent.FLG_SET_COLLIST);
					SqlADO.AddTableCmd(SellerId, clsConst.TABLE_CMD_PUSH_SLOT, "", 
							2000, ClsTime.SystemTime(), "推送货道数据");
				}
				response.sendRedirect("PortList.jsp?mid="+SellerId);
			}
		}
	}
}
