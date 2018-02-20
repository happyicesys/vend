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
	if(!ub.AccessAble(UserBean.FUNID_CAN_SET_MYSELF_INFO))
	{
		request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_SET_MYSELF_INFO]);
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
								<a href="#">用户管理</a>
							</li>
							<li class="active">个人信息</li>
						</ul><!-- .breadcrumb -->

						<!-- #nav-search -->
					</div>
			  	<form class="form-horizontal" role="form" action="UpdateSelf" method="post" name="form1" onsubmit="return(chk());">
			  	<div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">用户名</label>
				    <div class="col-sm-3">
				      ${sessionScope.usermessage.adminusername}
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">密码</label>
				    <div class="col-sm-3">
				      <input type="password" name="pwd" class="form-control input-sm input-sm"  placeholder="">
				      
				    </div>
				    <div class="col-sm-5">
				      <label>为空表示不修改！</label>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">重复密码</label>
				    <div class="col-sm-3">
				      <input type="password" name="repwd" class="form-control input-sm input-sm">
				    </div>
				    <div class="col-sm-5">
				      <span id="tips" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group" style="border: medium  rgb(250,0,255)">
				    <label class="col-sm-4 control-label">固定电话</label>
				    <div class="col-sm-3">
				      <input name="firmtel" type="text" class="form-control input-sm" value="${sessionScope.usermessage.admintelephone}">
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
				      <input name="name" type="text" class="form-control input-sm" value="${sessionScope.usermessage.adminname}" >
				    </div>
				    <div class="col-sm-5">
				      <label>如果开通自动转款，请真实填写，否则转款会失败</label>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">性别</label>
				    <div class="col-sm-3">
				    <label>
				      <input <%=ub.getAdminsex().equals("男")?"checked='checked'":"" %> name="sextype" type="radio" value="男" />男
						<input <%=ub.getAdminsex().equals("女")?"checked='checked'":"" %> name="sextype" type="radio" value="女" />女
						</label>
				    </div>
				  </div>
				  
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">微信openID</label>
				    <div class="col-sm-3">
				      <input  id="wx_openid"  name="wx_openid"  class="form-control input-sm"  type="text"  value="${sessionScope.usermessage.wx_openid}" />
				    </div>
				  </div>
				    <div class="form-group">
				    <label class="col-sm-4 control-label">扫描获取OPENID</label>
				      <div id="code"></div>
				    </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">住址</label>
				    <div class="col-sm-3">
				      <input name="address" type="text" class="form-control input-sm" value="${sessionScope.usermessage.adminaddress}">
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">权限</label>
				    	<div class="col-sm-8">
						    <ul class="list-inline">
								<%=ub.getRightLstString(ub.AccessAble(UserBean.FUNID_CAN_EDIT_RIGHT))%>
							</ul>
						</div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">机器查看限制</label>
				    <div class="col-sm-8">
				    <%if(ub.AccessAble(UserBean.FUNID_ACCESS_ALL_VENDER)) 
					{
						out.println("<span class='normal-label'>您已具有查看所有机器的权限，该参数不需要设置！</span>");
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
									out.println("<li>机器"+arr[i]+" <a href='SetAccessDisable?uid="+ ub.getId() +"&vid="+arr[i]+"'>删除</a></li>");
								}
							} 
						}
						if(ub.AccessAble(UserBean.FUNID_CAN_ASIGN_VENDER))
						{
						%>
						<li><a href="javascript:void(0);" id="SetAccessVender">添加所属机器</a></li>
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
				            	机器查看限制
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
				               data-dismiss="modal">确定
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
				    <label class="col-sm-4 control-label">创建时间</label>
				    <div class="col-sm-3">
				      ${sessionScope.usermessage.createtime}
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">上次登录时间</label>
				    <div class="col-sm-3">
				      ${sessionScope.usermessage.lastLoginTime}
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">集团编号</label>
				    	<div class="col-sm-3">
						    <%=String.format("[%d]-%s",groupBean.getId(), groupBean.getGroupname()) %>
						</div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">上次登录IP</label>
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

