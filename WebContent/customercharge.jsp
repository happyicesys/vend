<%@page import="beans.clsGroupBean"%>
<%@page import="java.util.ArrayList"%>
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
    
    
    if(!ub.AccessAble(UserBean.FUNID_CAN_ADD_USER))
	{
		request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ADD_USER]);
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
    <script type="text/javascript" src="js/bootstrap/datePicker/WdatePicker.js"></script>
	<!-- <style type="text/css">
		.list-inline li {
		    float: left;
		    width: 170px;
		}
	</style> -->

<script type="text/javascript">


var chk=function()
{

	var objDiv=document.getElementById("tips");


	if(form1.cardinfo.value=="")
	{
		$("#tips_cradinfo").text("卡号不能为空");
		return false;
	}

	if(form1.amount.value=="")
	{
		$("#tips_jine").text("金额不能为空");
		return false;
	}

	if(isNaN(form1.amount.value))
	{
		$("#tips_jine").text("金额必须是数字");
		return false;
	}
	if(form1.amount.value<=0)
	{
		$("#tips_jine").text("金额必须大于0元");
		return false;
	}
	if(form1.amount.value>1000)
	{
		$("#tips_jine").text("金额不能大于1000元");
		return false;
	}
	form1.submit();
	return true;
}

var getCardAmount=function()
{
	  htmlobj=$.ajax({url:"./ChkVenderRepeat?action=4&cardinfo="+ $("#cardinfo").val() +"&"+Math.random(),async:false});
	  $("#tips_cradinfo").html(htmlobj.responseText);
};
$(document).ready(function(){

	  $("#chkcardinfo").click(function(){
		  htmlobj=$.ajax({url:"./ChkVenderRepeat?action=3&cardinfo="+ $("#cardinfo").val() +"&"+Math.random(),async:false});
		  $("#tips_cradinfo").html(htmlobj.responseText);
		  });


	});



 getCardAmount

</script>

<title>添加会员</title>
</head>
<body style="background-color: #fff;">
	 <div class="breadcrumbs" id="breadcrumbs" style="margin-top:5px;">
						<ul class="breadcrumb">
							<li>
								<span class="glyphicon glyphicon-home"></span>
								<a href="MainHome.jsp" target="main" style="padding-left:5px;margin-left:5px;">首页</a>
							</li>

							<li>
								<a href="#">会员管理</a>
							</li>
							<li class="active">会员充值</li>
						</ul><!-- .breadcrumb -->

						<!-- #nav-search -->
					</div>
			  	<form class="form-horizontal" role="form" action="./CustomerCharge" method="post" name="form1" id="form1">
			  	<div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">充值卡号</label>
				    <div class="col-sm-3">
				      <input name="cardinfo" id="cardinfo" type="text" class="form-control input-sm input-sm" onkeydown="if(event.keyCode==13){getCardAmount();};" placeholder="卡号(必填)">
				      </div>
				    <div class="col-sm-5">
				      <input class="btn btn-success" type="button" value="检测卡号是否存在" name="chkcardinfo" id="chkcardinfo" />
				      <span id="tips_cradinfo" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">充值金额</label>
				    <div class="col-sm-3">
				      <input name="amount" id="amount" type="text" class="form-control input-sm input-sm"  placeholder="充值金额(必填)">
				      <span id="tips_jine" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">充值时间</label>
				    <div class="col-sm-3">
				      <%=ToolBox.getYMDHMS(new Timestamp(ClsTime.SystemTime()))%>
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="col-sm-offset-4 col-sm-3">
				      <input type="button" onclick="chk();" class="btn btn-primary" value="充值"></input>
				      <button type="reset" class="btn btn-primary" value="取消">取消</button>
				      <button type="button" onclick="location.href='./chargedatalst.jsp';" class="btn btn-primary" value="查看充值记录">查看充值记录</button>
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

