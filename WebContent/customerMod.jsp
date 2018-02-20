<%@page import="beans.CustomerBean"%>
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
    
    
    if(!ub.AccessAble(UserBean.FUNID_CAN_CHANGE_CUSTOMER))
	{
		request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_CHANGE_CUSTOMER]);
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
	}
    
    int id=ToolBox.filterInt(request.getParameter("id"));
    if(id==0)
    {
		request.setAttribute("message", "参数错误");
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
    }
    
    CustomerBean bean=CustomerBean.getCustomerBeanById(id);
    if(bean==null)
    {
		request.setAttribute("message", "参数错误");
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

	if(form1.username.value=="")
	{
		objDiv=document.getElementById("tips_username");
		objDiv.innerText="用户名不能为空！";
		return false;
	}

	form1.submit();
	return true;
}



$(document).ready(function(){
	  if($('#is_times_limit').is(':checked')) {
		  $("#custerm_times_div").show();
		  $("#xiaofei_rest_times").show();
	  }
	  else
	  {
		  $("#custerm_times_div").hide();
		  $("#xiaofei_rest_times").hide();
	  }

	  $("#chkcardinfo").click(function(){
		  htmlobj=$.ajax({url:"./ChkVenderRepeat?action=3&cardinfo="+ $("#cardinfo").val() +"&"+Math.random(),async:false});
		  $("#tips_cradinfo").html(htmlobj.responseText);
		  });
	  
	  $("#is_times_limit").click(function(){
		  if($('#is_times_limit').is(':checked')) {
			  $("#custerm_times_div").show();
			  $("#xiaofei_rest_times").show();
		  }
		  else
		  {
			  $("#custerm_times_div").hide();
			  $("#xiaofei_rest_times").hide();
		  }
	   });
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
							<li class="active">更新会员</li>
						</ul><!-- .breadcrumb -->

						<!-- #nav-search -->
					</div>
			  	<form class="form-horizontal" role="form" action="UpdateCustomer" method="post" name="form1" >
			  	<div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">用户名</label>
				    <div class="col-sm-3">
				    <input type="hidden" name="id" value="<%=bean.getId() %>">
				      <input type="text" name="username" readonly="readonly" id="username" class="form-control input-sm input-sm" value="<%=bean.get_user_name()%>">
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">密码</label>
				    <div class="col-sm-3">
				      <input name="pwd" type="text" class="form-control input-sm input-sm"  value="" placeholder="密码为空表示不修改">
				      <span id="tips" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">卡号</label>
				    <div class="col-sm-3">
				      <input name="cardinfo" id="cardinfo" type="text" class="form-control input-sm input-sm"  value="<%=bean.get_user_id_card()%>"  placeholder="卡号(必填)">
				      </div>
				    <div class="col-sm-5">
				      <input class="btn btn-success" type="button" value="检测卡号是否被使用" name="chkcardinfo" id="chkcardinfo" />
				      <span id="tips_cradinfo" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">单笔最大交易额(元)</label>
				    <div class="col-sm-3">
				      <input id="_user_max_credit_limit" name="_user_max_credit_limit" type="text" class="form-control input-sm input-sm" value="<%=String.format("%1.2f", bean.get_user_max_credit_limit()/100.0)%>" placeholder="单笔最大交易额(必填)">
				      <span id="tips_user_max_credit_limit" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">是否禁用会员</label>
				    <div class="col-sm-3">
				      <input type="checkbox" name="_user_disable" id="_user_disable" value="1" <%=(bean.get_user_disable()==1)?"checked='checked'":"" %> />
				      <span id="tips_disable" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">启用消费次数限制</label>
				    <div class="col-sm-3">
				      <input type="checkbox" name="is_times_limit" id="is_times_limit" value="1" <%=(bean.get_user_xiaofei_times_en()==1)?"checked='checked'":"" %>/>
				      <span id="tips_is_times_limit" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group" id="custerm_times_div">
				    <label class="col-sm-4 control-label">每日消费次数</label>
				    <div class="col-sm-3">
				      <input id="custerm_times" name="custerm_times" type="text" class="form-control input-sm input-sm" value="<%=bean.get_user_xiaofei_times()%>" placeholder="每日消费次数(必填)">
				      <span id="tips_custerm_times" style="color:red;"></span>
				    </div>
				  </div>

				  <div class="form-group">
				    <label class="col-sm-4 control-label">移动电话</label>
				    <div class="col-sm-3">
				      <input name="mobiletel"  type="text" class="form-control input-sm" value="<%=bean.get_user_tel()%>">
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">出生年月</label>
				    <div class="col-sm-3">
				      <input onFocus="WdatePicker();" value="<%=ToolBox.getTimeString(bean.get_user_birthday())%>" name="birthday" type="text" class="form-control input-sm">
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">性别</label>
				    <div class="col-sm-3">
				    <label>
				      <input name="sextype" type="radio" value="男" <%=(bean.getSex_type().equals("男"))?"checked='checked'":"" %> />男
					 <input name="sextype" type="radio" value="女" <%=(bean.getSex_type().equals("女"))?"checked='checked'":"" %>/>女
						</label>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">住址</label>
				    <div class="col-sm-3">
				      <input name="address" type="text" class="form-control input-sm" value="<%=bean.get_user_address() %>" placeholder="住址">
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">创建时间</label>
				    <div class="col-sm-3">
				      <%=ToolBox.getYMDHMS(bean.get_user_builder_date())%>
				    </div>
				  </div>
				  
				  <div class="form-group" id="xiaofei_rest_times">
				    <label class="col-sm-4 control-label">今日剩余次数</label>
				    <div class="col-sm-3">
				      <%=bean.get_user_xiaofei_rest_times()%>次
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">当前卡内金额</label>
				    <div class="col-sm-3">
				      <%=String.format("%1.2f", bean.get_user_amount()/100.0)%>元
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">最近充值金额</label>
				    <div class="col-sm-3">
				      <%=String.format("%1.2f", bean.getLast_charge_amount()/100.0)%>元
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">最近充值时间</label>
				    <div class="col-sm-3">
				      <%=ToolBox.getTimeLongString(bean.getLast_charge_time())%>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">最近刷卡金额</label>
				    <div class="col-sm-3">
				      <%=String.format("%1.2f", bean.getLast_jiaoyi_amount()/100.0)%>元
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">最近刷卡时间</label>
				    <div class="col-sm-3">
				      <%=ToolBox.getTimeLongString(bean.getLast_jiaoyi_time())%>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">创建时间</label>
				    <div class="col-sm-3">
				      <%=ToolBox.getTimeLongString(bean.get_user_builder_date())%>
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="col-sm-offset-4 col-sm-3">
				      <input type="button" onclick="chk();" class="btn btn-primary" value="更新" />
				      <input type="reset" class="btn btn-primary" value="取消" />
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

