<%@page import="com.tools.ToolBox"%>
<%@page import="beans.UserBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

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
	
	if(!ub.AccessAble(UserBean.FUNID_CAN_UPLOAD_FILE))
	{
		request.setAttribute("message", "Unable to "+UserBean.RIGHT_DES[UserBean.FUNID_CAN_UPLOAD_FILE]);
		request.setAttribute("LAST_URL", "index.jsp");
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
	}
	
	
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link REL="stylesheet" type="text/css" href="css/main.css">
<link REL="stylesheet" type="text/css" href="css/styles.css">
<title>Upload Attachment</title>
</head>
<body style="margin: 0px; text-align: left;">
	<form action="./UpLoadFirmFile"  name="smartForm" method="post" enctype="multipart/form-data">
		<input type="hidden" name="mid" value="${param.mid}"/>
		<input type="hidden" name="act" value="${param.act}"/>
		<input class="white-text" type="file" name="firm" /><input class="green_btn" type="submit" value="提交">
		<br/>File must be in bin extension, do not exceed 100K
	</form>
</body>
</html>