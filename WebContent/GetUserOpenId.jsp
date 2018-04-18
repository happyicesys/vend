<%@page import="weixin.popular.api.SnsAPI"%>
<%@page import="weixin.popular.bean.sns.SnsToken"%>
<%@page import="beans.clsGroupBean"%>
<%@page import="beans.UserBean"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.tools.ToolBox"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Get Open ID</title>
</head>
<body style="font-size:2em">
<%
request.setCharacterEncoding("utf-8");
response.setCharacterEncoding("utf-8");		

PrintWriter pw=response.getWriter();
int uid=ToolBox.filterInt(request.getParameter("id"));
String nonce=ToolBox.filter(request.getParameter("nonce"));

UserBean ub=UserBean.getUserBeanById(uid);

if(ub!=null)
{
	String key=ToolBox.filter(request.getParameter("state"));
	String state=ToolBox.getMd5(ub.getAdminpassword()+ub.getId()+nonce);
	if(key.equals(state))
	{
	    clsGroupBean groupBean=clsGroupBean.getGroup(ub.getGroupid());
	    
    	String AppSecret=groupBean.getAppsecret();
    	String appid=groupBean.getWx_appid();
    	String code=request.getParameter("code");
    	SnsToken token =SnsAPI.oauth2AccessToken(appid, AppSecret, code);
    	ub.setWx_openid(token.getOpenid());
    	UserBean.updateUser(ub);
    	pw.println("Successfully get Wechat Open ID, please refresh!");
	}
	else
	{
		pw.println(" Cannot access, please contact admin");
	}
}
else
{
	pw.println(" Parameters Error！ ");
}
%>
</body>
</html>