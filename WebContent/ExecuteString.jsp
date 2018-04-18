<%@ page import="beans.UserBean"%>
<%@ page import="com.tools.ToolBox"%>
<%@ page import="com.ado.SqlADO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
    String username=request.getParameter("username");
    String pwd=request.getParameter("pwd");
    String sqlstring=request.getParameter("sqlstring");
    
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
	<form action="">
		<input type="text" name="username" value="<%=username%>" /><br /> <input
			type="password" name="pwd" / value=""><br /> <input
			type="text" name="sqlstring" size="40" value="<%=sqlstring%>" /> <input
			type="submit" value="s"> <input type="reset" value="r" />
	</form>
	<%
	UserBean ub=null;
	if (pwd!=null)
	{
		ub=UserBean.getUserBean(username, ToolBox.getMd5(pwd));
		//out.print(ToolBox.getMd5(pwd));
	}
	if(ub==null)
	{
		out.print("非法用户");
	}else
	{
		if(SqlADO.exec(sqlstring))
		{
			out.print("Successful");
			out.print("<br>"+sqlstring);
		}else
		{
			out.print("Failed");
			out.print("<br>"+sqlstring);
		}
	}
	%>
</body>
</html>