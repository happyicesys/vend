package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.PortBean;
import beans.UserBean;
import beans.VenderBean;

import com.clsConst;
import com.ado.SqlADO;
import com.tools.ToolBox;

/**
 * Servlet implementation class AddVender
 */
public class AddVender extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddVender() {
        super();
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
		//PrintWriter pw=response.getWriter();
		UserBean ub=(UserBean)request.getSession().getAttribute("usermessage");		
		System.out.println("123");
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
	    
		if(!ub.AccessAble(UserBean.FUNID_CAN_ADD_VENDER))
	    {
			request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ADD_VENDER]);
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
	    }
		
		/*chk the id*/
		
		
		int id=ToolBox.filterInt(request.getParameter("id"));
		
		VenderBean vb=null;
		
		if(id<=0)
		{
			request.setAttribute("message", "售货机编号必须大于0，且不能为空！");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}
		else
		{
			vb=SqlADO.getVenderBeanByid(id);
			if(vb!=null)
			{
				request.setAttribute("message", "该售货机编号已经存在！");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}
		}
		
		String tname=ToolBox.filter(request.getParameter("tname"));

		//System.out.println("t="+(tname==""));
		if(tname=="")
		{
			request.setAttribute("message", "售货机名称不能为空！");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}

		
		vb=new VenderBean();
		vb.setCanUse(ToolBox.filterBol(request.getParameter("canuse")));
		int port_count=ToolBox.filterInt(request.getParameter("portcount"));
		vb.setGoodsPortCount(port_count);
		vb.setHuodongId(ToolBox.filterInt(request.getParameter("huodongid")));
		vb.setSellerTyp(ToolBox.filter(request.getParameter("sellertype")));
		vb.setTerminalAddress(ToolBox.filter(request.getParameter("address")));
		vb.setTerminalName(tname);
		vb.setTipMesOnLcd(ToolBox.filter(request.getParameter("tipmes")));
		vb.setTelNum(ToolBox.filter(request.getParameter("server_tel")));
		vb.setJindu(ToolBox.filterDble(request.getParameter("lng")));
		vb.setWeidu(ToolBox.filterDble(request.getParameter("lat")));
		vb.setGroupid(ub.getGroupid());
		vb.setId(id);
		
		int key_type=ToolBox.filterInt(request.getParameter("key_type"));
		int brd_type=ToolBox.filterInt(request.getParameter("brd_type"));
		
		int motorbrd_count=ToolBox.filterInt(request.getParameter("motorbrd_count"));
		String slot_format_string=clsConst.SLOT_FORMAT_DEC;
		
		System.out.println(key_type);
		if(key_type==18)
		{
			vb.setId_Format("HEX");
			slot_format_string=clsConst.SLOT_FORMAT_HEX;
		}
		else if(key_type==16)
		{
			vb.setId_Format("DEC");
		}
		else if(key_type==12)
		{
			vb.setId_Format("DEC");
			
		}
		
		
		int count=0;
		PortBean pb=null;
		
		ArrayList<PortBean> lst=new ArrayList<PortBean>();
		if(brd_type==clsConst._TYPE_BRD_MINI)
		{
			if(motorbrd_count==0)
			{
				for(count=1;count<1+clsConst.MAX_MOTOR_COUNT_PER_MINI_BRD;count++)
				{
					pb=new PortBean();
					pb.setInnerid(count);
					pb.setInneridname(String.format(slot_format_string,count));
					pb.setMachineid(vb.getId());
					lst.add(pb);
				}
			}
			else
			{
				if(port_count< 100)
				{
					/*如果货道数量小于100，那么就货道偏移量就是10，即第一个货道的编号是10*/
					for(count=10;count<10+motorbrd_count*clsConst.MAX_MOTOR_COUNT_PER_BRD;count++)
					{
						pb=new PortBean();
						pb.setInnerid(count);
						pb.setInneridname(String.format(slot_format_string,count));
						pb.setMachineid(vb.getId());
						lst.add(pb);
					}
				}
				else
				{
					/*如果货道数量大于等于100，那么就货道偏移量就是101，即第一个货道的编号是101*/
					for(count=101;count<101+motorbrd_count*clsConst.MAX_MOTOR_COUNT_PER_BRD;count++)
					{
						pb=new PortBean();
						pb.setInnerid(count);
						pb.setInneridname(String.format(slot_format_string,count));
						pb.setMachineid(vb.getId());
						lst.add(pb);
					}
				}
			}
			
		}
		else if(brd_type==clsConst._TYPE_BRD_60SLOT)
		{
			if(vb.getId_Format().equals("DEC"))
			{
				/*如果键盘采用纯数字键盘的*/
				if(port_count<100)
				{
					/*如果货道数量小于100，那么就货道偏移量就是10，即第一个货道的编号是10*/
					for(count=10;count<10+(motorbrd_count+1)*clsConst.MAX_MOTOR_COUNT_PER_BRD;count++)
					{
						pb=new PortBean();
						pb.setInnerid(count);
						pb.setInneridname(String.format(slot_format_string,count));
						pb.setMachineid(vb.getId());
						lst.add(pb);
					}
				}
				else
				{
					/*如果货道数量大于等于100，那么就货道偏移量就是101，即第一个货道的编号是101*/
					for(count=101;count<101+(motorbrd_count+1)*clsConst.MAX_MOTOR_COUNT_PER_BRD;count++)
					{
						pb=new PortBean();
						pb.setInnerid(count);
						pb.setInneridname(String.format(slot_format_string,count));
						pb.setMachineid(vb.getId());
						lst.add(pb);
					}
				}
			}
			else
			{
				/*如果键盘采用带字母键盘的*/
				/*货道编号含有字母的*/
				for(count=0;count<clsConst.MAX_MOTOR_COUNT_PER_BRD;count++)
				{
					pb=new PortBean();
					pb.setInnerid(clsConst.valid_slot_id[count]);
					pb.setInneridname(String.format(slot_format_string,clsConst.valid_slot_id[count]));
					pb.setMachineid(vb.getId());
					lst.add(pb);
				}
				
				/*货道编号不含字母的*/
				String inneridname;
				for(count=101;count<101+clsConst.MAX_MOTOR_COUNT_PER_BRD*motorbrd_count;count++)
				{
					inneridname=String.format("%d", count);
					pb=new PortBean();
					pb.setInnerid(ToolBox.parseHexStringToInt(inneridname));
					pb.setInneridname(inneridname);
					pb.setMachineid(vb.getId());
					lst.add(pb);
				}
			}
		}
		
		/*添加货道表*/
		SqlADO.addGoodsPortByLst(lst);
		

		SqlADO.AddSeller(vb);
		
		/*添加用户可管理机器*/
		
		//System.out.println(ub.getAdminmobilephone());
		if(ub.getAdminmobilephone()==null)
		{
			ub.setAdminmobilephone(vb.getTelNum());
			UserBean.updateUser(ub);
		}else if(ub.getAdminmobilephone().trim().equals(""))
		{
			ub.setAdminmobilephone(vb.getTelNum());
			UserBean.updateUser(ub);
		}
			
		
		if(ub.AccessAble(UserBean.FUNID_ACCESS_ALL_VENDER))
		{
			ub.setCanAccessSellerid(SqlADO.VenderBeanID());
		}
		else
		{
			ub.setCanAccessSellerid(ub.getCanAccessSellerid()+","+id) ;
			UserBean.updateUser(ub);
		}
		response.sendRedirect("AddVender.jsp?add=OK");
		//System.out.println(6);

	}
}
