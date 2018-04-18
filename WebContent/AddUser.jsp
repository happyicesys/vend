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

	var objDiv=document.getElementById("tips");
	
	if(form1.pwd.value!="")
	{
		if(form1.pwd.value.length<6)
		{
			objDiv.innerText="Password must more than 6 characters";
			return false;
		}
	}
	else
	{
		objDiv.innerText="Password must not be empty";
		return false;
	}
	

	if(form1.username.value=="")
	{
		objDiv=document.getElementById("tips_username");
		objDiv.innerText="Username must not be empty";
		return false;
	}
	var venderlst=$("input[name='canAccessVender1']");
	var s_vender="";
	for(var i=0;i<venderlst.length;i++)
		{
		  if(venderlst[i].checked)
			  {
			  s_vender+=venderlst[i].value+",";
			  }
		}

	$("#canAccessVender").val(s_vender);
	return true;
}



$(document).ready(function(){
	  $("#chk_repeat").click(function(){
	  htmlobj=$.ajax({url:"./ChkVenderRepeat?action=1&username="+ $("#username").val() +"&"+Math.random(),async:false});
	  $("#tips_username").html(htmlobj.responseText);
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

<title>Add Manager</title>
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
							<li class="active">Add Manager</li>
						</ul><!-- .breadcrumb -->

						<!-- #nav-search -->
					</div>
			  	<form class="form-horizontal" role="form" action="AddUser" method="post" name="form1" onsubmit="return(chk());">
			  	<div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Username</label>
				    <div class="col-sm-3">
				      <input type="text" name="username" id="username" class="form-control input-sm input-sm" placeholder="Username(Required)" id="chk_repeat">
				    </div>
				    <div class="col-sm-5">
				      <input class="btn btn-success" type="button" value="检测用户名是否存在" name="chkusername" id="chk_repeat" />
				      <span id="tips_username" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Password</label>
				    <div class="col-sm-3">
				      <input name="pwd" type="password" class="form-control input-sm input-sm"  placeholder="Password(Required)">
				      <span id="tips" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group" style="border: medium  rgb(250,0,255)">
				    <label class="col-sm-4 control-label">Office Number</label>
				    <div class="col-sm-3">
				      <input name="firmtel" type="text" class="form-control input-sm"  placeholder="">
				    </div>
				    
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Mobile Number</label>
				    <div class="col-sm-3">
				      <input name="mobiletel"  type="text" class="form-control input-sm"  placeholder="">
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Manager Name</label>
				    <div class="col-sm-3">
				      <input name="name" type="text" class="form-control input-sm">
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
				      <input name="address" type="text" class="form-control input-sm" placeholder="">
				    </div>
				  </div>
				  <%if(ub.AccessAble(UserBean.FUNID_CAN_SET_USER_GROUP_ID_WHEN_ADD)) 
				  {%>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Profile</label>
				    <div class="col-sm-3">
				      <select name="groupid" id="groupid" class="form-control input-sm">
				      	<option>Please select profile</option>
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
				    </div>
				  </div>
				  <%}%>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Access Level</label>
				    	<div class="col-sm-8">
						    <ul class="list-inline">
								<%
								UserBean tub=new UserBean();
								
								/*继承创建者的权限*/
								tub.setAdminrights(ub.getAdminrights());
								%>
									<%=tub.getRightLstString(true) %>
							</ul>
						</div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Access Vending</label>
				    <div class="col-sm-8">
				      <ul class="list-inline">
						<%
	
						if(ub.AccessAble(UserBean.FUNID_CAN_ASIGN_VENDER))
						{
						%>
							<li><input class="btn btn-success" type="button" value="Add Vending Access" id="SetAccessVender" /></li>
						<%
						}
						else
						{
							out.println("你不"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ASIGN_VENDER]);
						}
						%>
					</ul>
					<%
					if(ub.AccessAble(UserBean.FUNID_CAN_ASIGN_VENDER))
					{
						int[] arr=ToolBox.StringToIntArray(ub.getCanAccessSellerid());
						int i=0;
					%>
					
			        <input type="hidden" name="canAccessVender" id="canAccessVender" value="<%=tub.getCanAccessSellerid()%>" />
					<%-- <div id="dialog"  style="display:none">	
							<ul class="list-inline">
								<%
								for(i=0;i<arr.length;i++)
								{
									if(arr[i]>0)
									{
										out.print("<li style='width:150px;'><label><input type='checkbox' name='canAccessVender1' value="+ arr[i] +" />机器"+arr[i]+"</label></li>");
									}
								}
								%>
							</ul>
					</div> --%>
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
				            	Add Channel
				            </h4>
				         </div>
				         <div class="modal-body" id="alertcontent">
				         	<ul class="list-inline">
								<%
								for(i=0;i<arr.length;i++)
								{
									if(arr[i]>0)
									{
										out.print("<li style='width:150px;'><label><input type='checkbox' name='canAccessVender1' value="+ arr[i] +" />机器"+arr[i]+"</label></li>");
									}
								}
								%>
							</ul>
				         </div>
				         <div class="modal-footer">
				            <button type="submit" class="btn btn-default" 
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
				    <label class="col-sm-4 control-label">Created At</label>
				    <div class="col-sm-3">
				      <%=ToolBox.getYMDHM(new Timestamp(ClsTime.SystemTime()))%>
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="col-sm-offset-4 col-sm-3">
				      <input type="submit" class="btn btn-primary" value="添&nbsp;&nbsp;&nbsp;&nbsp;加"></input>
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

