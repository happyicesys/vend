<%@page import="weixin.popular.api.SnsAPI"%>
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
	if(!ub.AccessAble(UserBean.FUNID_CAN_SET_MYSELF_INFO))
	{
		request.setAttribute("message", "Unable to "+UserBean.RIGHT_DES[UserBean.FUNID_CAN_SET_MYSELF_INFO]);
		request.setAttribute("LAST_URL", "index.jsp");
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
	}
	clsGroupBean groupBean =clsGroupBean.getGroup(ub.getGroupid());
	int nonce=ToolBox.getRandomNumber();
	String state=ToolBox.getMd5(ub.getAdminpassword()+ub.getId()+nonce);
	String page_getopenid="GetUserOpenId.jsp";
	String redirect_uri ="http://"+
			request.getHeader("host")+"/"+page_getopenid+"?id="+ub.getId()+"&nonce="+nonce;
	
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
<script type="text/javascript" src="js/jquery.qrcode.min.js"></script> 
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

$(document).ready(function(){
		$("#SetAccessVender").click(function(){
			$('#myModal').modal('show');
		});
		
		$("#code").qrcode({ 
		    render: "canvas", //table方式 
		    width: 300, //宽度 
		    height:300, //高度 
		    text: "<%=SnsAPI.connectOauth2Authorize(groupBean.getWx_appid(),redirect_uri , false, state, null)%>" //任意内容 
		}); 
	  
	});

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
								<a href="#">User Management</a>
							</li>
							<li class="active">Personal Message</li>
						</ul><!-- .breadcrumb -->

						<!-- #nav-search -->
					</div>
			  	<form class="form-horizontal" role="form" action="UpdateSelf" method="post" name="form1" onsubmit="return(chk());">
			  	<div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Username</label>
				    <div class="col-sm-3">
				      ${sessionScope.usermessage.adminusername}
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Password</label>
				    <div class="col-sm-3">
				      <input type="password" name="pwd" class="form-control input-sm input-sm"  placeholder="">
				      
				    </div>
				    <div class="col-sm-5">
				      <label>Leave blank to remain the same</label>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Password Confirmation</label>
				    <div class="col-sm-3">
				      <input type="password" name="repwd" class="form-control input-sm input-sm">
				    </div>
				    <div class="col-sm-5">
				      <span id="tips" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group" style="border: medium  rgb(250,0,255)">
				    <label class="col-sm-4 control-label">Office Number</label>
				    <div class="col-sm-3">
				      <input name="firmtel" type="text" class="form-control input-sm" value="${sessionScope.usermessage.admintelephone}">
				    </div>
				    
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Mobile Number</label>
				    <div class="col-sm-3">
				      <input name="mobiletel"  type="text" class="form-control input-sm" value="${sessionScope.usermessage.adminmobilephone}">
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Manager Name</label>
				    <div class="col-sm-3">
				      <input name="name" type="text" class="form-control input-sm" value="${sessionScope.usermessage.adminname}" >
				    </div>
				    <div class="col-sm-5">
				      <label>Please provide correct information for auto transfer</label>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Gender</label>
				    <div class="col-sm-3">
				    <label>
				      <input <%=ub.getAdminsex().equals("男")?"checked='checked'":"" %> name="sextype" type="radio" value="男" />Male
						<input <%=ub.getAdminsex().equals("女")?"checked='checked'":"" %> name="sextype" type="radio" value="女" />Female
						</label>
				    </div>
				  </div>
				  
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Wechat Open ID</label>
				    <div class="col-sm-3">
				      <input  id="wx_openid"  name="wx_openid"  class="form-control input-sm"  type="text"  value="${sessionScope.usermessage.wx_openid}" />
				    </div>
				  </div>
				    <div class="form-group">
				    <label class="col-sm-4 control-label">Scan to get Open ID</label>
				      <div id="code"></div>
				    </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Address</label>
				    <div class="col-sm-3">
				      <input name="address" type="text" class="form-control input-sm" value="${sessionScope.usermessage.adminaddress}">
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Access Level</label>
				    	<div class="col-sm-8">
						    <ul class="list-inline">
								<%=ub.getRightLstString(ub.AccessAble(UserBean.FUNID_CAN_EDIT_RIGHT))%>
							</ul>
						</div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Access Vending</label>
				    <div class="col-sm-8">
				    <%if(ub.AccessAble(UserBean.FUNID_ACCESS_ALL_VENDER)) 
					{
						out.println("<span class='normal-label'>You already can access the view, this parameter need not setup</span>");
					}
					else
					{
					%>
				      <ul class="list-inline">
						<%
						int i=0;
						int[] arr=ToolBox.StringToIntArray(ub.getCanAccessSellerid());
						if(arr!=null)
						{
							
							for(i=0;i<arr.length;i++)
							{
								if(arr[i]>0)
								{
									out.println("<li>机器"+arr[i]+" <a href='SetAccessDisable?uid="+ ub.getId() +"&vid="+arr[i]+"'>Delete</a></li>");
								}
							} 
						}
						if(ub.AccessAble(UserBean.FUNID_CAN_ASIGN_VENDER))
						{
						%>
						<li><a href="javascript:void(0);" id="SetAccessVender">Add Vending Access</a></li>
						<%
						} %>
					</ul>
					<!-- 模态框（Modal） -->
				<div class="modal fade" id="myModal" tabindex="-1" role="dialog" 
				   aria-labelledby="myModalLabel" aria-hidden="true">
				   <div class="modal-dialog">
				      <div class="modal-content">
				         <div class="modal-header">
				            <button type="button" class="close" data-dismiss="modal" 
				               aria-hidden="true">×
				            </button>
				            <h4 class="modal-title" id="myModalLabel">
				            	Access Vending
				            </h4>
				         </div>
				         <div class="modal-body" id="alertcontent">
				         	<ul class="list-inline">
								<%
								if(arr!=null)
								{
									for(i=0;i<arr.length;i++)
									{
										if(arr[i]>0)
										{
											if(ub.CanAccessSeller(arr[i]))
											{
												out.print("<li style='width:150px;'><label><input type='checkbox' checked='checked' name='canAccessVender' value="+ arr[i] +" />机器"+arr[i]+"</label></li>");
											}
											else
											{
												out.print("<li><label><input type='checkbox' name='canAccessVender' value="+ arr[i] +" />机器"+arr[i]+"</label></li>");
											}
										}
									}
								}
								%>
							</ul>
				         </div>
				         <div class="modal-footer">
				            <button type="button" class="btn btn-default" 
				               data-dismiss="modal">Confirm
				            </button>
				         </div>
				      </div><!-- /.modal-content -->
				   </div><!-- /.modal-dialog -->
				</div><!-- /.modal -->  
					
					<%} 
					%>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Created At</label>
				    <div class="col-sm-3">
				      ${sessionScope.usermessage.createtime}
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Last Login Time</label>
				    <div class="col-sm-3">
				      ${sessionScope.usermessage.lastLoginTime}
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Profile ID</label>
				    	<div class="col-sm-3">
						    <%=String.format("[%d]-%s",groupBean.getId(), groupBean.getGroupname()) %>
						</div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Last Login IP</label>
				    <div class="col-sm-3">
				      ${sessionScope.usermessage.lastloginip}
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="col-sm-offset-4 col-sm-3">
				      <input type="submit" class="btn btn-primary" value="提&nbsp;&nbsp;&nbsp;&nbsp;交"></input>
				      <button type="submit" class="btn btn-primary" value="取消">取&nbsp;&nbsp;&nbsp;&nbsp;消</button>
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

