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
	if(form1.pwd.value!="")
	{
		if(form1.pwd.value.length<6)
		{
			$("#tips").text("密码不足6位");
			return false;
		}
	}
	else
	{
		$("#tips").text("密码不能为空");
		return false;
	}
	

	if(form1.username.value=="")
	{
		$("#tips_username").text("用户名不能为空！");
		return false;
	}

	if(isNaN($("#jine").val()))
	{
		$("#tips_jine").text("金额必须是数字");
		return false;
	}
	if($("#jine").val()<0)
	{
		$("#tips_jine").text("金额必须大于等于0元");
		return false;
	}
	if($("#jine").val()>1000)
	{
		$("#tips_jine").text("金额不能大于1000元");
		return false;
	}
	form1.submit();
	return true;
}



$(document).ready(function(){
	$("#custerm_times_div").hide();

	  
	  $("#chkcardinfo").click(function(){
		  htmlobj=$.ajax({url:"./ChkVenderRepeat?action=3&cardinfo="+ $("#cardinfo").val() +"&"+Math.random(),async:false});
		  $("#tips_cradinfo").html(htmlobj.responseText);
		  });
	  
	  $("#is_times_limit").click(function(){
		  if($('#is_times_limit').is(':checked')) {
			  $("#custerm_times_div").show();
		  }
		  else
		  {
			  $("#custerm_times_div").hide();
		  }
	   });
	  
	  /*添加货道窗口*/
	  $("#dialog").dialog({autoOpen: false,width:500,height:600,modal: false,
	      buttons: {
	          "确定": function() {
	              $( this ).dialog( "close" );
	            }
	      }
	  });
		  
		$("#SetAccessVender").click(function(){
			 /* $("#dialog").dialog("open"); */
			$('#myModal').modal('show');
		})
	  
	});

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
							<li class="active">添加会员</li>
						</ul><!-- .breadcrumb -->

						<!-- #nav-search -->
					</div>
			  	<form class="form-horizontal" role="form" action="AddCustomer" method="post" name="form1">
			  	<div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">用户名</label>
				    <div class="col-sm-3">
				      <input type="text" name="username" id="username" class="form-control input-sm input-sm" placeholder="用户名(必填)" id="chk_repeat">
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">密码</label>
				    <div class="col-sm-3">
				      <input name="pwd" type="text" class="form-control input-sm input-sm"  placeholder="密码(必填)">
				      <span id="tips" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">卡号</label>
				    <div class="col-sm-3">
				      <input name="cardinfo" id="cardinfo" type="text" class="form-control input-sm input-sm"  placeholder="卡号(必填)">
				      </div>
				    <div class="col-sm-5">
				      <input class="btn btn-success" type="button" value="检测卡号是否被使用" name="chkcardinfo" id="chkcardinfo" />
				      <span id="tips_cradinfo" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">金额(元)</label>
				    <div class="col-sm-3">
				      <input name="jine" id="jine" value="0" type="text" class="form-control input-sm input-sm"  placeholder="预充值金额(必填)">
				    </div>
				    <div class="col-sm-5">
				      <span id="tips_jine" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group" >
				    <label class="col-sm-4 control-label">单笔最大交易额(元)</label>
				    <div class="col-sm-3">
				      <input id="_user_max_credit_limit" name="_user_max_credit_limit" type="text" class="form-control input-sm input-sm" value="50.00" placeholder="单笔最大交易额(必填)">
				      <span id="tips_user_max_credit_limit" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">启用消费次数限制</label>
				    <div class="col-sm-3">
				      <input type="checkbox" name="is_times_limit" id="is_times_limit" value="1"/>
				      <span id="tips_is_times_limit" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group" id="custerm_times_div">
				    <label class="col-sm-4 control-label">每日消费次数</label>
				    <div class="col-sm-3">
				      <input id="custerm_times" name="custerm_times" type="text" class="form-control input-sm input-sm" value="" placeholder="每日消费次数(必填)">
				      <span id="tips_custerm_times" style="color:red;"></span>
				    </div>
				  </div>

				  <div class="form-group">
				    <label class="col-sm-4 control-label">移动电话</label>
				    <div class="col-sm-3">
				      <input name="mobiletel"  type="text" class="form-control input-sm"  placeholder="">
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">出生年月</label>
				    <div class="col-sm-3">
				      <input onFocus="WdatePicker();" name="birthday" type="text" class="form-control input-sm">
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">性别</label>
				    <div class="col-sm-3">
				    <label>
				      <input checked='checked' name="sextype" type="radio" value="男" />男
						<input name="sextype" type="radio" value="女" />女
						</label>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">住址</label>
				    <div class="col-sm-3">
				      <input name="address" type="text" class="form-control input-sm" placeholder="住址">
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">创建时间</label>
				    <div class="col-sm-3">
				      <%=ToolBox.getYMDHMS(new Timestamp(ClsTime.SystemTime()))%>
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="col-sm-offset-4 col-sm-3">
				      <input type="button" onclick="chk();" class="btn btn-primary" value="添&nbsp;&nbsp;&nbsp;&nbsp;加"></input>
				      <button type="reset" class="btn btn-primary" value="取消">取&nbsp;&nbsp;&nbsp;&nbsp;消</button>
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

