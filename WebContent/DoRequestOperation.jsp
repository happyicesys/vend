

<%@page import="com.ado.SqlADO"%>
<%@page import="beans.clsGroupBean"%>
<%@page import="beans.clsGoodsBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.clsConst"%>
<%@page import="beans.UserBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.tools.ToolBox"%>

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

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta name="description" content=""/>
    <meta name="author" content=""/>
    <!-- Bootstrap Core CSS -->
    <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet"/>
    <!-- MetisMenu CSS -->
    <link href="css/bootstrap/metisMenu.min.css" rel="stylesheet"/>
    <!-- Timeline CSS -->
    <link href="css/bootstrap/timeline.css" rel="stylesheet"/>
    <!-- Custom CSS -->
    <link href="css/bootstrap/admin.css" rel="stylesheet"/>
    <!-- Morris Charts CSS -->
    <link href="css/bootstrap/morris.css" rel="stylesheet"/>
    <!-- Custom Fonts -->
    <link href="css/bootstrap/font-awesome.min.css" rel="stylesheet" type="text/css"/>
    <!-- <link href="./images/adminstyle.css" rel="stylesheet" type="text/css" /> -->
    <link href="./jquery_ui/css/cupertino/jquery-ui.min.css" rel="stylesheet" type="text/css" />
    <link href="./jquery_ui/css/showLoading.css" rel="stylesheet" type="text/css" />
    <script  language="javascript"  type="text/javascript" src="./jquery_ui/js/jquery-1.9.1.js"></script>
    <script language="javascript" type="text/javascript" src="./jquery_ui/js/jquery-ui.min.js"></script>
    <script language="javascript" type="text/javascript" src="./jquery_ui/js/jquery.showLoading.min.js"></script>
    <script language="javascript" type="text/javascript" src="./jquery_ui/js/jquery.ui.datepicker-zh-TW.js"></script>
    <script type="text/javascript" src="js/jquery.qrcode.min.js"></script> 

	<script type="text/javascript">
	$(document).ready(function(){
		  $("#clearpara").click(function(){
		  htmlobj=$.ajax({url:"./AjaxRequest",type:"POST",data:{activeid:<%=clsConst.ACTION_FRESH_VENDER_PARA%>},async:false});
		  //alert(htmlobj.responseText);
		  $("#operation_ret").val(htmlobj.responseText);
		  });
		  
		  //微信发送消息测试按钮
		  $("#wx_sendmsg").click(function(){
			  if($("#groupid").val()==0)
			  {
				  $("#operation_ret").val("Profile ID cannot be empty");
				  return;
			  }
			  htmlobj=$.ajax({
				  url:"./AjaxRequest",
				  type:"POST",
				  data:{activeid:<%=clsConst.ACTION_TEST_WX_SEND_MSG%>,touser:$("#touser").val(),content:$("#content").val(),groupid:$("#groupid").val()},
				  async:false
				  });
			  
			  $("#operation_ret").val(htmlobj.responseText);
			  });
		  
		  
		  $("#test_wx_qrcode_get").click(function(){
			  if($("#groupid").val()==0)
			  {
				  $("#operation_ret").val("Profile ID cannot be empty");
				  return;
			  }
			  htmlobj=$.ajax({
				  url:"./AjaxRequest",
				  type:"POST",
				  data:{activeid:<%=clsConst.ACTION_TEST_WX_QRCODE_GET%>,groupid:$("#groupid").val()},
				  async:true,
			      dataType: "text",
				  success:function(ret){
					  $("#operation_ret").val(ret);
					  $("#code").empty();
					  $("#code").qrcode({ 
						    render: "canvas", //table方式 
						    width: 200, //宽度 
						    height:200, //高度 
						    text: ret 
						});   
				  }
			  	});
			  });
		  
		  $("#ForceSaveLog").click(function(){
			  htmlobj=$.ajax({
				  url:"./AjaxRequest",
				  type:"POST",
				  data:{activeid:<%=clsConst.ACTION_SAVE_LOG%>},
				  async:true,
			      dataType: "text",
				  success:function(ret){$("#operation_ret").val(ret);}
			  	});
			  });
		  
		  
		  $("#manualTransfer").click(function(){
			  htmlobj=$.ajax({
				  url:"./AjaxRequest",
				  type:"POST",
				  data:{activeid:<%=clsConst.ACTION_MANUAL_TRANSFER%>},
				  async:true,
			      dataType: "text",
				  success:function(ret){$("#operation_ret").val(ret);}
			  	});
			  });
		  
		  $("#test_al_qrcode_get").click(function(){
			  if($("#groupid").val()==0)
			  {
				  $("#operation_ret").val("Profile ID cannot be empty");
				  return;
			  }
			  htmlobj=$.ajax({
				  url:"./AjaxRequest",
				  type:"POST",
				  data:{activeid:<%=clsConst.ACTION_TEST_AL_QRCODE_GET%>,groupid:$("#groupid").val()},
				  async:true,
			      dataType: "text",
				  success:function(ret){
					  $("#operation_ret").val(ret);
					  $("#code").empty();
						$("#code").qrcode({ 
						    render: "canvas", //table方式 
						    width: 200, //宽度 
						    height:200, //高度 
						    text: ret 
						}); 
					  }
				  });
			  });
		});
	</script>
</head>
<body>
	<button id="clearpara" class="btn btn-default form-control" style="margin:5px 0px;">Clear Vending List Cache</button>
	<button id="wx_sendmsg" class="btn btn-default form-control" style="margin:5px 0px;">Send News to public</button>
	<button id="test_wx_qrcode_get" class="btn btn-default form-control" style="margin:5px 0px;">Test Wechat 2D barcode receive</button>
	<button id="test_al_qrcode_get" class="btn btn-default form-control" style="margin:5px 0px;">Test Alipay 2D barcode receive</button>
	<button id="ForceSaveLog" class="btn btn-default form-control" style="margin:5px 0px;">Force to record the log</button>
	<button id="manualTransfer" class="btn btn-default form-control" style="margin:5px 0px;">Manual transfer</button>
	<!-- <button id="test_qrcode_pay" class="btn btn-default form-control" style="margin:5px 0px;">测试二维码Payment</button>
	 -->
	   	<label style="margin:5px 0px;text-align: left;" for="groupid">Profile ID</label>
     <select name="groupid" id="groupid" class="form-control input-sm">
     	<option value="0">Please select profile</option>
     	<%
     	int i=0;
     	ArrayList<clsGroupBean> groupBeans=clsGroupBean.getGroupLst();
     	for(clsGroupBean grBean:groupBeans)
     	{
     	%>
     	<option value="<%=grBean.getId()%>"><%=grBean.getId()+"-"+grBean.getGroupname() %></option>
     	<%
 		}
     	%>
     </select>
   	<label style="margin:5px 0px;text-align: left;" for="operation_ret">Results</label>
     <textarea class="btn btn-default form-control" style="margin:5px 0px;text-align: left;" rows="5" name="operation_ret" id="operation_ret"></textarea>
     <div id="code"></div>
     <label style="margin:5px 0px;text-align: left;" for="content">Message</label>
     <textarea class="btn btn-default form-control" style="margin:5px 0px;text-align: left;" rows="5" name="content" id="content"></textarea>
     <label style="margin:5px 0px;text-align: left;" for="content">Receiver OpenID</label>
     <input class="form-control" style="margin:5px 0px;text-align: left;" name="touser" id="touser" value="<%=ub.getWx_openid()%>"/>
     
</body>
</html>