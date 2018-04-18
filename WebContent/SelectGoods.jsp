<%@page import="com.ado.SqlADO"%>
<%@page import="beans.clsGoodsBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="beans.UserBean"%>
<%@page import="com.tools.ToolBox"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>

<%

    UserBean ub=(UserBean)session.getAttribute("usermessage");
	if(ub==null)
	{
		request.setAttribute("message", "You have no rights to access, please contact admin");
		request.setAttribute("LAST_URL", "index.jsp");
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
	}
	
	if(!ub.AccessAble(UserBean.FUNID_CAN_ACCESS_WEB))
	{
		request.setAttribute("message", "Unable to "+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ACCESS_WEB]);
		request.setAttribute("LAST_URL", "index.jsp");
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
	}
	
	
	if(!ub.AccessAble(UserBean.FUNID_CAN_VIEW_GOODS))
	{
		request.setAttribute("message", "Unable to "+UserBean.RIGHT_DES[UserBean.FUNID_CAN_VIEW_GOODS]);
		request.setAttribute("LAST_URL", "index.jsp");
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
	}
	%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
    <meta http-equiv="X-UA-Compatible" content="IE=9" />
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta name="description" content=""/>
    <meta name="author" content=""/>
    <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet"/>
    <link href="css/bootstrap/metisMenu.min.css" rel="stylesheet"/>
    <link href="css/bootstrap/timeline.css" rel="stylesheet"/>
    <link href="css/bootstrap/admin.css" rel="stylesheet"/>
    <link href="css/bootstrap/morris.css" rel="stylesheet"/>
    <link href="css/bootstrap/font-awesome.min.css" rel="stylesheet" type="text/css"/>
    <link href="./jquery_ui/css/cupertino/jquery-ui.min.css" rel="stylesheet" type="text/css" />
    <link href="./jquery_ui/css/showLoading.css" rel="stylesheet" type="text/css" />
     <!--[if lte IE 6]>
	<link rel="stylesheet" type="text/css" href="/css/bootstrap/bootstrap-ie6.css"/>
	<![endif]-->
	<!--[if lte IE 7]>
	<link rel="stylesheet" type="text/css" href="/css/bootstrap/ie.css"/>
	<![endif]-->
<title><%=ToolBox.WEB_NAME%></title>
</head>
<body>
	<%
	String goodsname=ToolBox.filter(request.getParameter("goodsname"));
	ArrayList<clsGoodsBean> gblst=null;
	if(goodsname.equals(""))
	{
		gblst=clsGoodsBean.getGoodsBeanLst(ub.getGroupid());
	}
	else
	{
		gblst=clsGoodsBean.getGoodsBeanLst(ub.getGroupid(),goodsname);
	}
	int colid=ToolBox.filterInt(request.getParameter("colid"));
%>
<div>  <form method="get" action="./SelectGoods.jsp">Search by product name
<label><input type="hidden" name="colid"  value="<%=colid%>"/> <input value="<%=goodsname %>" name="goodsname" id="goodsname" class="form-control input-sm" /></label><button type="submit"  id="sousuo">搜索</button>
</form></div>
	<table style="border-collapse: collapse;" width="99%"
		bordercolor="#CAD7F7" border="1" cellpadding="4" cellspacing="1">
		<tr class="hcol">
			<th id="no">Product Code</th>
			<th id="name">Name</th>
			<th id="name">Binding</th>
			<th id="name">Photo</th>
			<th id="no">Ref Price</th>
		</tr>
		<%
	if(gblst==null)
	{
		out.print("<tr><td colspan='8'>");
		out.print("<span class='waring-label'>No records available</span>");
    	out.print("</td></tr>");
    	return;
	}
	if(gblst.size()==0)
	{
		out.print("<tr><td colspan='8'>");
		out.print("<span class='waring-label'>No records available</span>");
    	out.print("</td></tr>");
    	return;
	}
	
	int i=0;
	
	for(i=0;i<gblst.size();i++)
	{clsGoodsBean gb=gblst.get(i);
		
	 %>
		<tr>
			<td><%=gb.getId()%></td>
			<td><%=gb.getGoodsname()%></td>

			<td><a href="./DoSelect?goodsid=<%=gb.getId()%>&colid=<%=colid %>" class="green_btn">Type</a></td>

			<td><img height="70" width="75"	src="images_little/<%=gb.getPicname()%>"></td>
			<td><%=String.format("%1.2f",gb.getPrice()/100.0)%></td>
		</tr>
		<%} %>
	</table>
</body>
</html>