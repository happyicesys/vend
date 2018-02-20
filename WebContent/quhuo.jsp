<%@page import="java.util.ArrayList"%>
<%@page import="beans.UserBean"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"%>
    
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
	
	if(!ub.AccessAble(UserBean.FUNID_CAN_SWIPE_QUHUO))
	{
		request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_SWIPE_QUHUO]);
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
	}
	
	String ret=request.getParameter("ret");
	String price=request.getParameter("price");
	if(price==null)
	{
		price="2.5";
	}
	
	%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
    <meta http-equiv="X-UA-Compatible" content="IE=9" />
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
<style type="text/css">
.but {
	font-size: 1.5em;
	width: 3em;
}
.txt {
	font-size: 2em;
	width: 100%;
	
}
.lastRecord
{
	font-size: 0.8em;
	width: 100%;
	text-align: left;
}
</style>
<script type="text/javascript">

function load()
{
	var cardinfo = document.getElementById("cardinfo");
	var tips = document.getElementById("tips");

	cardinfo.value="";
	cardinfo.focus();
	<%if(ret!=null)
	{%>
	tips.innerHTML=("<%=ret%>");
	<%}%>
}
function tijiao(obj) 
{
	//var cardinfo = document.getElementById("cardinfo");
	var tips = document.getElementById("tips");

	if(form1.cardinfo.value=="")
	{
		tips.innerHTML=("卡号不能为空！");
		return;
	}
	form1.count.value=obj;
	form1.submit();
}

</script>
<title></title>
</head>
<body style="font-size:1.5em;" onload="load();">
<form action="QuHuo" name="form1" method="post">
	卡号:<input class="txt" value="" name="cardinfo"  id="cardinfo" style="" onkeydown='if(event.keyCode==13)(tijiao(1));'/>
	<div id="tips" style="width: 100%;"></div>
	价格:<input class="txt" name="price" value="<%=price %>" id="price"/>
	<input type="hidden" name=count value="1" id="count"/>
	</form>
	<%
	ArrayList<String> lastRecord=(ArrayList<String>)session.getAttribute("lastRecord");
	if(lastRecord!=null)
	{
		for(int i=lastRecord.size()-1;i>=0;i--)
		{
			out.print("<div class='lastRecord'>");
			out.print(lastRecord.get(i));
			out.print("</div>");
		}
	}
	%>
</body>
</html>