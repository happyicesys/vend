<%@page import="beans.clsGoodsBean"%>
<%@page import="beans.PortBean"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.ado.SqlADO"%>
<%@ page import="com.tools.ToolBox"%>


<%
int SellerId=0;
SellerId=ToolBox.filterInt(request.getParameter("mid"));
int slotid=ToolBox.filterInt(request.getParameter("sid"));

PortBean pb= SqlADO.getPortBean(SellerId,slotid);


clsGoodsBean gb=clsGoodsBean.getGoodsBean(pb.getGoodsid());

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- saved from url=(0014)about:internet -->
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=ToolBox.WEB_NAME%>--帮助</title>



<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style type="text/css">
td img {
	display: block;
}
</style>

<link rel="stylesheet" type="text/css" href="css/main.css">
</head>
<body>FAQ： 1、接线 2、
</body>
</html>
