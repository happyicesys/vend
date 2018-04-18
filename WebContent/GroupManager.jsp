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
	
	int groupid=ToolBox.filterInt(request.getParameter("groupid"));
	

	if(groupid!=0)
	{
		if(groupid==ub.getGroupid())
		{
			
		}
		else
		{
			if(!ub.AccessAble(UserBean.FUNID_CAN_CHANGE_GROUP_ID))
			{
				request.setAttribute("message", "Unable to "+UserBean.RIGHT_DES[UserBean.FUNID_CAN_CHANGE_GROUP_ID]);
				request.setAttribute("LAST_URL", "index.jsp");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}
		}
	}
	else
	{
		if(!ub.AccessAble(UserBean.FUNID_CAN_MOD_SELF_GROUP_ID))
		{
			request.setAttribute("message", "Unable to "+UserBean.RIGHT_DES[UserBean.FUNID_CAN_MOD_SELF_GROUP_ID]);
			request.setAttribute("LAST_URL", "index.jsp");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}
		groupid=ub.getGroupid();
	}
	
	clsGroupBean groupBean =clsGroupBean.getGroup(groupid);
	
	if(groupBean==null)
	{
		request.setAttribute("message", "集团参数错误，请联系管理员！");
		request.setAttribute("LAST_URL", "index.jsp");
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
	}

	InetAddress addr = InetAddress.getLocalHost();
	String ip=addr.getHostAddress().toString();//获得本机IP

	pageContext.setAttribute("ip", ip);
	
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
			objDiv.innerText="Password must more than 6 characters";
			return false;
		}
		
		if((form1.pwd.value)!=(form1.repwd.value))
		{

			objDiv.innerText="Password confirmation does not match";
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

<title>Personal Message</title>
</head>
<body style="background-color: #fff;">
	 <div class="breadcrumbs" id="breadcrumbs" style="margin-top:5px;">
						<ul class="breadcrumb">
							<li>
								<span class="glyphicon glyphicon-home"></span>
								<a href="MainHome.jsp" target="main" style="padding-left:5px;margin-left:5px;">Home</a>
							</li>

							<li>
								<a href="#">Admin Management</a>
							</li>
							<li class="active">Profile Message</li>
						</ul><!-- .breadcrumb -->

						<!-- #nav-search -->
					</div>
			  	<form class="form-horizontal" role="form" action="UpdateGroup" method="post" name="form1" onsubmit="return(chk());">
			  	<div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Profile Name</label>
				    <div class="col-sm-3">
				      <input type="text" name="groupdes" readonly="readonly" class="form-control input-sm input-sm"  value="<%=groupBean.getGroupname() %>" placeholder="Profile Desc">
				      <input type="hidden" name="groupid" value="<%=groupBean.getId() %>" />
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Profile Desc</label>
				    <div class="col-sm-3">
				      <input type="text" name="groupdes" class="form-control input-sm input-sm"  value="<%=groupBean.getGroupdes() %>" placeholder="Profile Desc">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Welcome Message</label>
				    <div class="col-sm-3">
				      <input type="text" name="WelcomeMessage" id="WelcomeMessage" class="form-control input-sm input-sm"  value="<%=groupBean.getWelcomeMessage() %>" placeholder="Welcome Message">
				    </div>
				  </div>
				  <div class="form-group" style="border: medium  rgb(250,0,255)">
				    <label class="col-sm-4 control-label">Created At</label>
				    <div class="col-sm-3">
						<input name="Creattime" readonly="readonly" type="text" class="form-control input-sm" value="<%=ToolBox.getYMDHMS(groupBean.getCreattime()) %>">				      
				    </div>
				    
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Latest Update Time</label>
				    <div class="col-sm-3">
				      <input name="updatetime" readonly="readonly" type="text" class="form-control input-sm" value="<%=ToolBox.getYMDHMS(groupBean.getUpdatetime()) %>">
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Latest Update By</label>
				    <div class="col-sm-3">
				      <input name="adminusername" readonly="readonly" type="text" class="form-control input-sm" value="<%=groupBean.getAdminusername() %>" >
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Wechat AppID</label>
				    <div class="col-sm-3">
				      <input id="wx_appid" name="wx_appid"  class="form-control input-sm"  type="text"  value="<%=groupBean.getWx_appid() %>" />
				    </div>
				    <div class="col-sm-5">
				      <label>Required</label>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Wechat key</label>
				    <div class="col-sm-3">
				      <input id="wx_key" name="wx_key"  class="form-control input-sm"  type="text"  value="<%=groupBean.getWx_key() %>" />
				    </div>
				    <div class="col-sm-5">
				      <label>Required</label>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Wechat Merchant ID</label>
				    <div class="col-sm-3">
				      <input  id="wx_mch_id"  name="wx_mch_id" class="form-control input-sm"  type="text"  value="<%=groupBean.getWx_mch_id()%>" />
				    </div>
				    <div class="col-sm-5">
				      <!--<label><a href="wxhelp.html">Wechat Help</a></label>  -->
				      <label>Required</label>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Wechat App Secret</label>
				    <div class="col-sm-3">
				      <input  id="AppSecret"  name="AppSecret" class="form-control input-sm"  type="text"  value="<%=groupBean.getAppsecret()%>" />
				    </div>
				    <div class="col-sm-5">
				      <!--<label><a href="wxhelp.html">Wechat Help</a></label>  -->
				      <label>Optional</label>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Wechat Notify URL</label>
				    <div class="col-sm-3">
				      <input  id="wx_notify_url" name="wx_notify_url" readonly="readonly" class="form-control input-sm" type="text"  value="<%=groupBean.getWx_notify_url()%>" />
				    </div>
				  </div>
				  
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Server IP</label>
				    <div class="col-sm-3">
				      <input  id="ServerIp"  name="ServerIp" readonly="readonly" class="form-control input-sm"  type="text"  value="<%=groupBean.getServerIp()%>" />
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Alipay Partner ID</label>
				    <div class="col-sm-3">
				     <input id="al_PARTNER_ID" name="al_PARTNER_ID"  class="form-control input-sm"  type="text"  value="<%=groupBean.getAl_PARTNER()%>" />
				    </div>
				    <div class="col-sm-5">
				      <!-- <label><a href="wxhelp.html">Alipay Help</a></label> -->
				      <label>Required</label>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Alipay face pay App ID</label>
				    <div class="col-sm-3">
				     <input id="al_APP_ID" name="al_APP_ID"  class="form-control input-sm"  type="text"  value="<%=groupBean.getAl_APP_ID()%>" />
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Alipay private key</label>
				    <div class="col-sm-3">
				      <textarea name="al_PRIVATE_KEY" id="al_PRIVATE_KEY" rows="10"  class="form-control input-sm" ><%=groupBean.getAl_PRIVATE_KEY()%></textarea>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Alipay public key</label>
				    <div class="col-sm-3">
				      <textarea name="al_PUBLIC_KEY" id="al_PUBLIC_KEY"  rows="10"  class="form-control input-sm" ><%=groupBean.getAl_PUBLIC_KEY()%></textarea>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Signature Method</label>
				    <div class="col-sm-3">
				      <input name="al_SIGN_TYPE" id="al_SIGN_TYPE" readonly="readonly" type="text" class="form-control input-sm" value="<%=groupBean.getAl_SIGN_TYPE()%>">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Alipay Notify URL</label>
				    <div class="col-sm-3">
				      <input name="notify_url" id="notify_url" readonly="readonly" type="text" class="form-control input-sm" value="<%=groupBean.getNotify_url()%>">
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

