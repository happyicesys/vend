<%@page import="com.ClsTime"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="com.ado.SqlADO"%>
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
	<!-- <style type="text/css">
		.list-inline li {
		    float: left;
		    width: 170px;
		}
	</style> -->

<script type="text/javascript">


var chk=function()
{
	if((form1.username.value)=="")
	{
		$("#err").text("用户名不能为空!");
		return false;
	}
	
	if((form1.mobiletel.value)=="")
	{
		$("#err").text("手机号码不能为空!");
		return false;
	}
	if((form1.content.value)=="")
	{
		$("#err").text("内容不能为空!");
		return false;
	}
	if((form1.content.value.length)>200)
	{
		$("#err").text("内容不能 超过200个字符!");
		return false;
	}
	return true;
}

</script>

<title>添加管理员</title>
</head>
<body style="background-color: #fff;">
	 <div class="breadcrumbs" id="breadcrumbs" style="margin-top:5px;">
						<ul class="breadcrumb">
							<li>
								<span class="glyphicon glyphicon-home"></span>
								<a href="MainHome.jsp" target="main" style="padding-left:5px;margin-left:5px;">首页</a>
							</li>

							<li>
								<a href="#">用户管理</a>
							</li>
							<li class="active">添加管理</li>
						</ul><!-- .breadcrumb -->

						<!-- #nav-search -->
					</div>
			  	<form class="form-horizontal" role="form" action="AddLiuyan" method="post" name="form1" onsubmit="return(chk());">
			  	<div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">用户名</label>
				    <div class="col-sm-3">
				      <input type="text" readonly="readonly" name="username" id="username" class="form-control input-sm input-sm"  value="${sessionScope.usermessage.adminusername}">
				    </div>
				  </div>

				  <div class="form-group">
				    <label class="col-sm-4 control-label">移动电话</label>
				    <div class="col-sm-3">
				      <input name="mobiletel"  type="text" class="form-control input-sm" value="${sessionScope.usermessage.adminmobilephone}">
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">管理员姓名</label>
				    <div class="col-sm-3">
				      <input name="realname" type="text" class="form-control input-sm" value="${sessionScope.usermessage.adminname}">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">留言内容</label>
				    <div class="col-sm-3">
				      <textarea name="content" class="form-control input-sm" rows="5" cols="50"></textarea>
				    </div>
				  </div>

				  <div class="form-group">
				    <label class="col-sm-4 control-label">创建时间</label>
				    <div class="col-sm-3">
				      <input name="time" readonly="readonly"  type="text" class="form-control input-sm" value="<%=ToolBox.getYMDHM(new Timestamp(ClsTime.SystemTime()))%>">
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="col-sm-offset-4 col-sm-3">
				      <input type="submit" class="btn btn-primary" value="添&nbsp;&nbsp;&nbsp;&nbsp;加"></input>
				      <button type="submit" class="btn btn-primary" value="取消">取&nbsp;&nbsp;&nbsp;&nbsp;消</button>
				      <span id="err"></span>
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

