package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ado.SqlADO;
import com.tools.ToolBox;

import beans.CustomerBean;
import beans.UserBean;
import beans.clsGoodsBean;

/**
 * Servlet implementation class ChkVenderRepeat
 */
public class ChkVenderRepeat extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChkVenderRepeat() {
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
		HttpSession session=request.getSession();
		int action=ToolBox.filterInt(request.getParameter("action"));
		UserBean ub=(UserBean)session.getAttribute("usermessage");
		
		String cardinfo;
		switch(action)
		{
		
		case 0:/*检测机器编号是否可以使用*/
			int vid=ToolBox.filterInt(request.getParameter("vid"));
			
			if(vid<=0)
			{
				pw.print("编号必须大于零");
				return;
			}
			if(SqlADO.ChkVenderRepeat(vid))
			{
				pw.print("该编号机器已经存在！");
			}
			else
			{
				pw.print("该编号可以使用！");
			}
			break;
			
		case 1:/*检测用户名是否可以使用*/
			String username=ToolBox.filter(request.getParameter("username"));
			if(username==null)
			{
				pw.print("用户名错误");
				return;
			}
			if(username.trim().equals(""))
			{
				pw.print("用户名不能为空");
				return;
			}
			if(UserBean.ChkUserRepeat(username))
			{
				pw.print("该用户名已经存在！");
			}
			else
			{
				pw.print("该用户名可以使用！");
			}
			break;
			
		case 2:
			String goodsname=ToolBox.filter(request.getParameter("goodsname"));
			if(goodsname==null)
			{
				pw.print("产品名称错误");
				return;
			}
			if(goodsname.trim().equals(""))
			{
				pw.print("产品名称不能为空");
				return;
			}
			if(clsGoodsBean.getGoodsBean(goodsname,ub.getGroupid())!=null)
			{
				pw.print("该产品已经存在！");
			}
			else
			{
				pw.print("该产品不存在，可以添加！");
			}
			break;
			
		case 3:
			cardinfo=ToolBox.filter(request.getParameter("cardinfo"));
			if(cardinfo==null)
			{
				pw.print("卡号错误");
				return;
			}
			if(cardinfo.trim().equals(""))
			{
				pw.print("卡号不能为空");
				return;
			}
			if(CustomerBean.getCustomerBeanByCardID(cardinfo)!=null)
			{
				pw.print("该卡号已经被使用！");
				return;
			}
			else
			{
				pw.print("该卡号可以使用！");
				return;
			}
		case 4:
			cardinfo=ToolBox.filter(request.getParameter("cardinfo"));
			if(cardinfo==null)
			{
				pw.print("卡号错误");
				return;
			}
			if(cardinfo.trim().equals(""))
			{
				pw.print("卡号不能为空");
				return;
			}
			CustomerBean c=CustomerBean.getCustomerBeanByCardID(cardinfo);
			if(c!=null)
			{
				pw.print(String.format("卡上余额：%1.2f元", c.get_user_amount()/100.0));
				return;
			}
			else
			{
				pw.print("该卡号没有绑定会员！");
				return;
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
