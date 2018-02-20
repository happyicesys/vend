<%@page import="java.util.ArrayList"%>
<%@page import="beans.TradeBean"%>
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
    
    
    if(!ub.AccessAble(UserBean.FUNID_CAN_REFUND))
	{
		request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_REFUND]);
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
	}
   
    session.setAttribute("currentpage", request.getRequestURI()+"?"+request.getQueryString());
    
    int id= ToolBox.filterInt(request.getParameter("id"));
    TradeBean tb=null;
    if(id!=0)
    {
    	tb=SqlADO.getTradeBean(id);
    }
    
    ArrayList<UserBean> user_lst;
    user_lst=UserBean.getUserBeanListByright();
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


$(document).ready(function(){

	$("#but_feeback").click(function(){
		var amount=$("#amount").val();
		var pwd=$("#pwd").val();
		var userlist=$("#userlist").val();
		var des=$("#des").val();
		
		var obj=new Object();
		obj.amount=amount;
		obj.userlist=userlist;
		obj.pwd=pwd;
		obj.des=des;
		
		
		$("body").showLoading(); 
	    $.ajax({
	        type: "POST",
	        url: "./DoTranfor",
	        data:obj,
	        success: (function(obj){
	            $("body").hideLoading();  
	            $("#tips").text(obj);
	            }),
	        dataType: "text",
	        error:(function(obj,txtmes,txtmes2){
	            $("body").hideLoading();  
	            $("#tips").text("退费失败！"+obj.status+"错误");
	            })
	    		
	      });	  
	});
});
</script>

<title>转帐</title>
</head>
<body style="background-color: #fff;">
	 <div class="breadcrumbs" id="breadcrumbs" style="margin-top:5px;">
						<ul class="breadcrumb">
							<li>
								<span class="glyphicon glyphicon-home"></span>
								<a href="MainHome.jsp" target="main" style="padding-left:5px;margin-left:5px;">首页</a>
							</li>

							<li>
								<a href="#">交易管理</a>
							</li>
							<li class="active">转帐</li>
						</ul><!-- .breadcrumb -->

						<!-- #nav-search -->
					</div>
			  	<form class="form-horizontal" role="form"   method="post" name="form1">
			  	<div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">用户密码</label>
				    <div class="col-sm-3">
				      <input id="pwd" name="pwd" type="password" class="form-control input-sm input-sm" value="" placeholder="密码(必填)">
				    </div>
				  </div>
				  <div class="form-group" style="border: medium  rgb(250,0,255)">
				    <label class="col-sm-4 control-label">收款人</label>
				    <div class="col-sm-3">
						<select name="receiver" id="userlist" class="form-control input-sm">
							<option  value="0">未选择</option>
							<% 
							for(UserBean ubobj:user_lst)
							{
								if(ubobj.getWx_openid()!=null)
								{
									out.print("<option  value=\""+ ubobj.getId() +"\">"+ ubobj.getAdminusername()+"-"+ubobj.getAdminname()+"</option>");
								}
							}
							%>
						</select>
				    </div>
				  </div>
				  
				  <div class="form-group" style="border: medium  rgb(250,0,255)">
				    <label class="col-sm-4 control-label">转帐金额</label>
				    <div class="col-sm-3">
				      <input name="amount"  id="amount" type="text" class="form-control input-sm">
				    </div>
				    <div class="col-sm-5">金额必须大于1元</div>
				  </div>
				  
				  <div class="form-group" style="border: medium  rgb(250,0,255)">
				    <label class="col-sm-4 control-label">转帐说明</label>
				    <div class="col-sm-3">
				      <input name="des"  id="des" type="text" class="form-control input-sm">
				    </div>

				  </div>
				  <div class="form-group">
				    <div class="col-sm-offset-4 col-sm-3">
				      <input type="button" class="btn btn-primary" value="提交" id="but_feeback"></input>
				      <span id="tips" style="color: red;"></span>
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

