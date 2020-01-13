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
				pw.print("Machine ID must be numeric and greater than 0");
				return;
			}
			if(SqlADO.ChkVenderRepeat(vid))
			{
				pw.print("Machine ID has been used, please try again");
			}
			else
			{
				pw.print("Machine ID Okay");
			}
			break;
			
		case 1:/*检测用户名是否可以使用*/
			String username=ToolBox.filter(request.getParameter("username"));
			if(username==null)
			{
				pw.print("Username is not correct");
				return;
			}
			if(username.trim().equals(""))
			{
				pw.print("Username cannot be blank");
				return;
			}
			if(UserBean.ChkUserRepeat(username))
			{
				pw.print("Username has been used, please try again");
			}
			else
			{
				pw.print("Username Okay");
			}
			break;
			
		case 2:
			String goodsname=ToolBox.filter(request.getParameter("goodsname"));
			if(goodsname==null)
			{
				pw.print("Invalid product name");
				return;
			}
			if(goodsname.trim().equals(""))
			{
				pw.print("Product name cannot be blank");
				return;
			}
			if(clsGoodsBean.getGoodsBean(goodsname,ub.getGroupid())!=null)
			{
				pw.print("Product name has been used, please try again");
			}
			else
			{
				pw.print("Product name okay");
			}
			break;
			
		case 3:
			cardinfo=ToolBox.filter(request.getParameter("cardinfo"));
			if(cardinfo==null)
			{
				pw.print("Invalid Card number");
				return;
			}
			if(cardinfo.trim().equals(""))
			{
				pw.print("Card number cannot be blank");
				return;
			}
			if(CustomerBean.getCustomerBeanByCardID(cardinfo)!=null)
			{
				pw.print("Card number has been used, please try again");
				return;
			}
			else
			{
				pw.print("Card number okay");
				return;
			}
		case 4:
			cardinfo=ToolBox.filter(request.getParameter("cardinfo"));
			if(cardinfo==null)
			{
				pw.print("Invalid card number");
				return;
			}
			if(cardinfo.trim().equals(""))
			{
				pw.print("Card number cannot be blank");
				return;
			}
			CustomerBean c=CustomerBean.getCustomerBeanByCardID(cardinfo);
			if(c!=null)
			{
				pw.print(String.format("Card Balance：%1.2f ", c.get_user_amount()/100.0));
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
