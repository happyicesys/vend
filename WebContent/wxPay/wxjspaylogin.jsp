<%@page import="weixin.popular.api.SnsAPI"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URL"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
    <% 
    String openid=(String)request.getSession().getAttribute("openid");
    if(openid==null)
    {
    	String redirurl="http://ww.vendertec.com/wxPay/wxjspaycode.jsp?id=123";
	    String url=SnsAPI.connectOauth2Authorize("wx2f96dace5e3723ac",redirurl, false, "1239128386782341765", null);
	    		
	   //"https://open.weixin.qq.com/connect/oauth2/authorize?"+
	   //"appid="+ "wx2f96dace5e3723ac" +"&redirect_uri="+ redirurl +"&response_type=code&scope=snsapi_base&state=1239128386782341765#wechat_redirect";
	    //url.re
	    //url.replace("APPID", "wxb94791a66ea6db9a");
	    //url.replace("REDIRECT_URI", redirurl);
	    //url.replace("SCOPE", "snsapi_base");
	    //url.replace("STATE", "1239128386782341765");
	    
	    System.out.println(url);
	    response.sendRedirect(url);
	    
	    //http://ww.vendertec.com:8080/VenderManagersj/wxPay/wxjspaylogin.jsp
    }
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript">


</script>
<title>科威尼迪科技</title>
</head>
<body>


</body>
</html>