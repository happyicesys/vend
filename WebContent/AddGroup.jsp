<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.Date"%>
<%@page import="java.net.InetAddress"%>
<%@page import="com.ado.SqlADO"%>
<%@page import="beans.clsGroupBean"%>
<%@ page import="beans.UserBean"%>
<%@ page import="com.tools.ToolBox"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
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
	

	if(!ub.AccessAble(UserBean.FUNID_CAN_CREATE_GROUP_ID))
	{
		request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_CREATE_GROUP_ID]);
		request.setAttribute("LAST_URL", "index.jsp");
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
	}
	
	//InetAddress addr = InetAddress.getLocalHost();
	//request.getLocalAddr()
	String ip=request.getLocalAddr();//获得本机IP
	pageContext.setAttribute("ip", ip);
	
	int port= request.getServerPort();
	if(port!=80)
	{
		pageContext.setAttribute("serverurl", ip+":"+port);
	}
	else
	{
		pageContext.setAttribute("serverurl", ip);
	}
    %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
    <meta http-equiv="X-UA-Compatible" content="IE=9" />
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet">
    <link href="css/bootstrap/metisMenu.min.css" rel="stylesheet">
    <link href="css/bootstrap/timeline.css" rel="stylesheet">
    <link href="css/bootstrap/admin.css" rel="stylesheet">
    <link href="css/bootstrap/morris.css" rel="stylesheet">
    <link href="css/bootstrap/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link href="./jquery_ui/css/cupertino/jquery-ui.min.css" rel="stylesheet" type="text/css" />
    <link href="./jquery_ui/css/showLoading.css" rel="stylesheet" type="text/css" />
     <!--[if lte IE 6]>
	<link rel="stylesheet" type="text/css" href="/css/bootstrap/bootstrap-ie6.css">
	<![endif]-->
	<!--[if lte IE 7]>
	<link rel="stylesheet" type="text/css" href="/css/bootstrap/ie.css">
	<![endif]-->
    <script src="js/bootstrap/jquery-1.12.0.min.js" type="text/javascript"></script>
    <script language="javascript" type="text/javascript" src="./jquery_ui/js/jquery-ui.min.js"></script>
    <script language="javascript" type="text/javascript" src="./jquery_ui/js/jquery.showLoading.min.js"></script>
    <script language="javascript" type="text/javascript" src="./jquery_ui/js/jquery.ui.datepicker-zh-TW.js"></script>
	<script type="text/javascript" src="js/jquery.blockUI.js"></script>

<script type="text/javascript">
var chk=function()
{
	var objDiv=document.getElementById("tips");
	
	if(form1.pwd.value!="")
	{
		if(form1.pwd.value.length<6)
		{
			objDiv.innerText="密码不足6位！";
			return false;
		}
		
		if((form1.pwd.value)!=(form1.repwd.value))
		{

			objDiv.innerText="两次密码输入不一致！";
			return false;
		}
	}
	
	return true;
}

var ShowPanel=function () 
{
	jQuery.blockUI
    (
    	{
	    	message: $("#dialog"), 
	    	css: { 
	    		cursor:				'default'
	    		},
	    	overlayCSS:{
				backgroundColor:	'#000',
				opacity:			0.5,
				cursor:				'default'
	    	},
    	}
    );
}

var closePanel=function()
{
	jQuery.unblockUI();
}



</script>

<title>个人信息</title>
</head>
<body style="background-color: #fff;">
	 <div class="breadcrumbs" id="breadcrumbs" style="margin-top:5px;">
						<ul class="breadcrumb">
							<li>
								<span class="glyphicon glyphicon-home"></span>
								<a href="MainHome.jsp" target="main" style="padding-left:5px;margin-left:5px;">首页</a>
							</li>

							<li>
								<a href="#">高级管理</a>
							</li>
							<li class="active">添加集团信息</li>
						</ul><!-- .breadcrumb -->

						<!-- #nav-search -->
					</div>
			  	<form class="form-horizontal" role="form" action="AddGroup" method="post" name="form1" onsubmit="return(chk());">
			  	<div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">集团名称</label>
				    <div class="col-sm-3">
				      <input type="text" name="groupname" id="groupname" class="form-control input-sm input-sm"  placeholder="集团名称">
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">集团描述</label>
				    <div class="col-sm-3">
				      <input type="text" name="groupdes" class="form-control input-sm input-sm"  placeholder="集团描述">
				    </div>
				  </div>
				  <div class="form-group" style="border: medium  rgb(250,0,255)">
				    <label class="col-sm-4 control-label">添加关注时的欢迎信息</label>
				    <div class="col-sm-3">
						<input name="WelcomeMessage" id="WelcomeMessage" type="text" class="form-control input-sm">				      
				    </div>
				  </div>
				  
				  <div class="form-group" style="border: medium  rgb(250,0,255)">
				    <label class="col-sm-4 control-label">创建时间</label>
				    <div class="col-sm-3">
						<input name="Creattime" readonly="readonly" type="text" class="form-control input-sm" value="<%=ToolBox.getYMDHMS(new Timestamp(System.currentTimeMillis())) %>">				      
				    </div>
				  </div>

				  <div class="form-group">
				    <label class="col-sm-4 control-label">微信APPID</label>
				    <div class="col-sm-3">
				      <input id="wx_appid" name="wx_appid"  class="form-control input-sm"  type="text" />
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">微信密钥</label>
				    <div class="col-sm-3">
				      <input id="wx_key" name="wx_key"  class="form-control input-sm"  type="text"  />
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">微信商户号</label>
				    <div class="col-sm-3">
				      <input  id="wx_mch_id"  name="wx_mch_id" class="form-control input-sm"  type="text"  />
				    </div>
				    <div class="col-sm-5">
				      <label><a href="wxhelp.html">微信账号信息的获取方法</a></label>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">微信应用密钥</label>
				    <div class="col-sm-3">
				      <input  id="AppSecret"  name="AppSecret" class="form-control input-sm"  type="text"  />
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">微信回调地址</label>
				    <div class="col-sm-3">
				      <input  id="wx_notify_url" name="wx_notify_url" value="http://${serverurl}/WxTradeCompleteNotify" class="form-control input-sm" type="text"  />
				    </div>
				    <div class="col-sm-5">
				      <label>建议使用默认值</label>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">服务器IP地址</label>
				    <div class="col-sm-3">
				      <input  id="ServerIp"  name="ServerIp" readonly="readonly" class="form-control input-sm"  type="text"  value="${ip}" />
				    </div>
				  </div>

				  <div class="form-group">
				    <label class="col-sm-4 control-label">支付宝合作者ID</label>
				    <div class="col-sm-3">
				     <input id="al_PARTNER_ID" name="al_PARTNER_ID"  class="form-control input-sm"  type="text"  />
				    </div>
				    <div class="col-sm-5">
				      <label><a href="wxhelp.html">支付宝账号信息的获取方法</a></label>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">支付宝APPID</label>
				    <div class="col-sm-3">
				     <input id="al_APP_ID" name="al_APP_ID"  class="form-control input-sm"  type="text"  />
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">支付宝私钥</label>
				    <div class="col-sm-3">
				      <textarea name="al_PRIVATE_KEY" id="al_PRIVATE_KEY" rows="10"  class="form-control input-sm" ></textarea>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">支付宝公钥</label>
				    <div class="col-sm-3">
				      <textarea name="al_PUBLIC_KEY" id="al_PUBLIC_KEY"   rows="10"  class="form-control input-sm" ></textarea>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">签名方式</label>
				    <div class="col-sm-3">
				      <input name="al_SIGN_TYPE" id="al_SIGN_TYPE" readonly="readonly" type="text" class="form-control input-sm" value="RSA">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">支付宝回调地址</label>
				    <div class="col-sm-3">
				      <input name="notify_url" id="notify_url" type="text" class="form-control input-sm" value="http://${serverurl}/ali/notify_url.jsp" >
				    </div>
				    <div class="col-sm-5">
				      <label>建议使用默认值</label>
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="col-sm-offset-4 col-sm-3">
				      <input type="submit" class="btn btn-primary" value="提交"></input>
				      <button type="submit" class="btn btn-primary" value="取消">取消</button>
				    </div>
				  </div>
				  </div>
				</form>
				<!-- Bootstrap Core JavaScript -->
    <script src="js/bootstrap/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="js/bootstrap/metisMenu.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="js/bootstrap/admin.js"></script>

    <!-- Page-Level Demo Scripts - Tables - Use for reference -->
    <script>
    $(document).ready(function() {
    	$('#myModal').modal('hide');
    	$('#myModal').on('hide.bs.modal', function (e) {
    		
    		});
    });
    </script>
</body>
</html>

