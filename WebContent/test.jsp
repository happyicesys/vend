<%@page import="java.util.TimeZone"%>
<%@page import="com.clsEvent"%>
<%@page import="com.tools.ClsPwd"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ado.SqlADO"%>
<%@page import="com.tools.ToolBox"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%=request.getSession().getServletContext().getRealPath("/") %><br/>
<%=TimeZone.getDefault().getDisplayName() %><br>
<%=TimeZone.getDefault() %><br>
<%=ToolBox.getDateTimeString() %><br>

	<%//=request.getParameter("id") %>
	<form action="SetPara" method="post">
	
	t:<input type="text" name="t"/>
	p:<input type="text" name="p"/>
	m:<input type="text" name="m"/>
	f:<input type="text" name="f"/>
		
	<button type="submit">提交</button>
	</form>
	<br/>
	<%
	response.setStatus(500);
	int act=ToolBox.filterInt(request.getParameter("act"));
	int mid=0;//ToolBox.filterInt(request.getParameter("mid"));
	PrintWriter pw=response.getWriter();
	if(act==1)
	{
		byte[] key=ClsPwd.MakeKey();
		ClsPwd.SetPublicKey(key,mid);
		
		ToolBox.printX(key, key.length);
		
		pw.write(ToolBox.getBASE64(key));
		
		
	}
	
	clsEvent e= clsEvent.FindClsEvent(12);
	
	pw.write("val=");
	pw.write(String.format("%X", mid));
	
	pw.write("\r\n");
	%>
	${pageContext.request.requestURL}
	${pageContext.request.queryString}
	${param.mid}
	<form action="./test.jsp" method="post" >
	<input type="hidden" name="act" value="1"/>
	<input type="text" name="mid" value="${mid}"><br/>
	
	<button type="submit">创建公钥</button>
	</form>
</body>
</html>