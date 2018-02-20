<%@page import="com.clsConst"%>
<%@page import="beans.PortBean"%>
<%@ page import="beans.VenderBean"%>
<%@ page import="beans.UserBean"%>
<%@ page import="com.ado.SqlADO"%>
<%@ page import="com.tools.ToolBox"%>
<%@ page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%
    UserBean ub=(UserBean)session.getAttribute("usermessage");
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
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="./images/adminstyle.css" rel="stylesheet" type="text/css" />
<link href="./css/styles.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../js/csshovernotie6.js"></script>

<title>售货机参数快捷操作</title>
</head>
<body style="margin: 5px;">
<div style="text-align:center;margin:auto;">
	<div style="width: 270px;text-align: left;border:1px #ccc solid;margin:auto;padding: 10px;">
		<form action="" name="form1" style="margin:0px;padding: 0px;">
			<input type="hidden" value="<%=clsConst.ACTION_SET_PRICE %>">
			<ul>
				<li style="text-align: center;line-height:40px;">批量设置价格</li>
				<li style="line-height: 40px;">机器编号:　<input type="text" name="mid" value="" class="white-text"/></li>
				<li style="line-height: 40px;">货道编号:　<input type="text" name="sid" value="" class="white-text"/></li>
				<li style="line-height: 40px;">价　　格:　<input type="text" name="val" value="" class="white-text"/>元</li>
				<li style="line-height: 40px;text-align: center;">
					<input class="blue_btn" type="submit" value="确定">
					<input class="blue_btn" type="button" value="清除">
				</li>
			</ul>
		</form>
	</div>
	
	<div>
		<form action="">
			批量设置折扣
			<input type="hidden" value="<%=clsConst.ACTION_SET_DISCOUNT %>">
			<ul>
				<li style="line-height: 40px;">机器编号:　<input type="text" name="mid" value="" class="white-text"/></li>
				<li style="line-height: 40px;">货道编号:　<input type="text" name="sid" value="" class="white-text"/></li>
				<li style="line-height: 40px;">折　　扣:　<input type="text" name="val" value="" class="white-text"/>元</li>
			</ul>
			<input class="blue_btn" type="submit" value="确定">
			<input class="blue_btn" type="button" value="清除">
		</form>
	</div>
	
	<div>
		<form action="">
			批量设置容量
			<input type="hidden" value="<%=clsConst.ACTION_SET_CAPACITY %>">
			<ul>
				<li style="line-height: 40px;">机器编号:　<input type="text" name="mid" value="" class="white-text"/></li>
				<li style="line-height: 40px;">货道编号:　<input type="text" name="sid" value="" class="white-text"/></li>
				<li style="line-height: 40px;">容　　量:　<input type="text" name="val" value="" class="white-text"/>　元</li>
			</ul>
			<input class="blue_btn" type="submit" value="确定">
			<input class="blue_btn" type="button" value="清除">
		</form>
	</div>
	<div>
		<form action="">
			批量设置库存
			<input type="hidden" value="<%=clsConst.ACTION_SET_STOCK %>">
			<ul>
				<li style="line-height: 40px;">机器编号:　<input type="text" name="mid" value="" class="white-text"/></li>
				<li style="line-height: 40px;">货道编号:　<input type="text" name="sid" value="" class="white-text"/></li>
				<li style="line-height: 40px;">库　　存:　<input type="text" name="val" value="" class="white-text"/>元</li>
			</ul>
			<input class="blue_btn" type="submit" value="确定">
			<input class="blue_btn" type="button" value="清除">
		</form>
	</div>
	</div>
</body>
</html>
