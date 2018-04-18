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
    
    
    if(!ub.AccessAble(UserBean.FUNID_CAN_ADD_USER))
	{
		request.setAttribute("message", "Unable to "+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ADD_USER]);
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
			$("#tips").text("Password must longer than 6 characters");
			return false;
		}
	}
	else
	{
		$("#tips").text("Password must not be empty");
		return false;
	}
	

	if(form1.username.value=="")
	{
		$("#tips_username").text("Username must not be empty");
		return false;
	}

	if(isNaN($("#jine").val()))
	{
		$("#tips_jine").text("Amount must be in numbers");
		return false;
	}
	if($("#jine").val()<0)
	{
		$("#tips_jine").text("Amount must be greater than 0");
		return false;
	}
	if($("#jine").val()>1000)
	{
		$("#tips_jine").text("Amount must not be greater than 1000");
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
	  
	  /*Add Channel窗口*/
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

<title>Add Customer</title>
</head>
<body style="background-color: #fff;">
	 <div class="breadcrumbs" id="breadcrumbs" style="margin-top:5px;">
						<ul class="breadcrumb">
							<li>
								<span class="glyphicon glyphicon-home"></span>
								<a href="MainHome.jsp" target="main" style="padding-left:5px;margin-left:5px;">Home</a>
							</li>

							<li>
								<a href="#">Customer Management</a>
							</li>
							<li class="active">Add Customer</li>
						</ul><!-- .breadcrumb -->

						<!-- #nav-search -->
					</div>
			  	<form class="form-horizontal" role="form" action="AddCustomer" method="post" name="form1">
			  	<div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Username</label>
				    <div class="col-sm-3">
				      <input type="text" name="username" id="username" class="form-control input-sm input-sm" placeholder="Username(Required)" id="chk_repeat">
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Password</label>
				    <div class="col-sm-3">
				      <input name="pwd" type="text" class="form-control input-sm input-sm"  placeholder="Password(Required)">
				      <span id="tips" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Credit Card No</label>
				    <div class="col-sm-3">
				      <input name="cardinfo" id="cardinfo" type="text" class="form-control input-sm input-sm"  placeholder="Credit Card No(Required)">
				      </div>
				    <div class="col-sm-5">
				      <input class="btn btn-success" type="button" value="检测卡号是否被使用" name="chkcardinfo" id="chkcardinfo" />
				      <span id="tips_cradinfo" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Amount(Dollar)</label>
				    <div class="col-sm-3">
				      <input name="jine" id="jine" value="0" type="text" class="form-control input-sm input-sm"  placeholder="预充值金额(Required)">
				    </div>
				    <div class="col-sm-5">
				      <span id="tips_jine" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group" >
				    <label class="col-sm-4 control-label">Single Largest Transaction(Dollar)</label>
				    <div class="col-sm-3">
				      <input id="_user_max_credit_limit" name="_user_max_credit_limit" type="text" class="form-control input-sm input-sm" value="50.00" placeholder="Single Largest Transaction(Required)">
				      <span id="tips_user_max_credit_limit" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Purchase Limit</label>
				    <div class="col-sm-3">
				      <input type="checkbox" name="is_times_limit" id="is_times_limit" value="1"/>
				      <span id="tips_is_times_limit" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group" id="custerm_times_div">
				    <label class="col-sm-4 control-label">Daily Purchase Count</label>
				    <div class="col-sm-3">
				      <input id="custerm_times" name="custerm_times" type="text" class="form-control input-sm input-sm" value="" placeholder="Daily Purchase Count(Required)">
				      <span id="tips_custerm_times" style="color:red;"></span>
				    </div>
				  </div>

				  <div class="form-group">
				    <label class="col-sm-4 control-label">Mobile Number</label>
				    <div class="col-sm-3">
				      <input name="mobiletel"  type="text" class="form-control input-sm"  placeholder="">
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">DOB</label>
				    <div class="col-sm-3">
				      <input onFocus="WdatePicker();" name="birthday" type="text" class="form-control input-sm">
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Gender</label>
				    <div class="col-sm-3">
				    <label>
				      <input checked='checked' name="sextype" type="radio" value="男" />Male
						<input name="sextype" type="radio" value="女" />Female
						</label>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Address</label>
				    <div class="col-sm-3">
				      <input name="address" type="text" class="form-control input-sm" placeholder="Address">
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Created At</label>
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

