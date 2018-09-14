<%@page import="beans.clsGroupBean"%>
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
    
    
    if(!ub.AccessAble(UserBean.FUNID_CAN_UPDATE_USER))
	{
		request.setAttribute("message", "Unable to "+UserBean.RIGHT_DES[UserBean.FUNID_CAN_UPDATE_USER]);
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
	}
	else if(!ub.AccessAble(UserBean.FUNID_CAN_VIEW_USER_LST))
	{
		request.setAttribute("message", "Unable to "+UserBean.RIGHT_DES[UserBean.FUNID_CAN_VIEW_USER_LST]);
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
	}
		
		int id=ToolBox.filterInt(request.getParameter("id"));
		UserBean tub=UserBean.getUserBeanById(id);
		
		if(tub==null)
		{
			request.setAttribute("message", "Parameter error, no records found!");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}
		
		clsGroupBean groupBean =clsGroupBean.getGroup(tub.getGroupid());
		//tub.setCanAccessSellerid(SqlADO.VenderBeanID());
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

<script type="text/javascript">
var chk=function()
{
	try
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
	//alert(s_vender);
	return true;
	}
	catch(e)
	{
		return false;
	}
}

$(document).ready(function(){

	  
	  /*添加货道窗口*/
	  $("#dialog").dialog({autoOpen: false,width:500,height:600,modal: false,
	      buttons: {
	          "关闭": function() {
	              $( this ).dialog( "close" );
	            }
	      }
	  });
		  
		$("#SetAccessVender").click(function(){
			$('#myModal').modal('show');
		})
	  
	});
	


</script>

<title>Manager Message</title>
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
							<li class="active">User message amend</li>
						</ul><!-- .breadcrumb -->

						<!-- #nav-search -->
					</div>
			  	<form class="form-horizontal" role="form" action="UpdateUser" method="post" name="form1" onsubmit="return(chk());">
			  	<input type="hidden" name="uid" value="<%=id %>"  />
			  	<div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Username</label>
				    <div class="col-sm-3">
				      <%=tub.getAdminusername()%>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Password</label>
				    <div class="col-sm-3">
				      <input type="password" id="pwd" name="pwd" class="form-control input-sm input-sm" >
				      
				    </div>
				    <div class="col-sm-5">
				      <label>Leave blank to remain the same</label>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Password Confirmation</label>
				    <div class="col-sm-3">
				      <input type="password" id="repwd"  name="repwd" class="form-control input-sm input-sm">
				    </div>
				    <div class="col-sm-5">
				      <span id="tips" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group" style="border: medium  rgb(250,0,255)">
				    <label class="col-sm-4 control-label">Office Number</label>
				    <div class="col-sm-3">
				      <input name="firmtel" type="text" class="form-control input-sm" value="<%=tub.getAdmintelephone()%>" >
				    </div>
				    
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Mobile Number</label>
				    <div class="col-sm-3">
				      <input name="mobiletel"  type="text" class="form-control input-sm" value="<%=tub.getAdminmobilephone()%>">
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Manager Name</label>
				    <div class="col-sm-3">
				      <input name="name" type="text" class="form-control input-sm" value="<%=tub.getAdminname()%>" >
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Gender</label>
				    <div class="col-sm-3">
				    <label>
				      <input <%=tub.getAdminsex().equals("男")?"checked='checked'":"" %> name="sextype" type="radio" value="男" />Male
						<input <%=tub.getAdminsex().equals("女")?"checked='checked'":"" %> type="radio" value="女" />Female
						</label>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Wechat Open ID</label>
				    <div class="col-sm-3">
				      <input  id="wx_openid"  readonly="readonly" name="wx_openid"  class="form-control input-sm"  type="text"  value="<%=tub.getWx_openid() %>" />
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Address</label>
				    <div class="col-sm-3">
				      <input name="address" type="text" class="form-control input-sm" value="<%=tub.getAdminaddress()%>" />
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Access Level</label>
				    	<div class="col-sm-8">
						    <ul class="list-inline">
								<%
								//=tub.getRightLstString(ub.AccessAble(UserBean.FUNID_CAN_EDIT_RIGHT)) 
									StringBuilder sb=new StringBuilder();
									for(int i=0;i<UserBean.RIGHT_DES.length;i++)
									{
										/*没有允许的权限不显示*/
										//if(!ub.AccessAble(i))
										//{
										//	continue;
										//}
										
										sb.append("<li style='width:170px;'><label><input type=\"checkbox\" ");
										if(tub.AccessAble(i))
										{
											sb.append("checked='checked'");
										}
										sb.append("value='");
										sb.append(i);
										sb.append("' name='right' />");
										sb.append(UserBean.RIGHT_DES[i]);
										sb.append("</label> </li>");
									}
									out.print(sb.toString());
								%>
							</ul>
						</div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Access Vending</label>
				    <div class="col-sm-8">
				    <%if(tub.AccessAble(UserBean.FUNID_ACCESS_ALL_VENDER)) 
					{
						out.println("<span class='normal-label'>该用户已具有查看所有机器的权限，该参数不需要设置！</span>");
					}
					else
					{
					%>
				      <ul class="list-inline">
						<%
						int i=0;
						int[] arr=ToolBox.StringToIntArray(tub.getCanAccessSellerid());
						if(arr!=null)
						{
							if(ub.AccessAble(UserBean.FUNID_CAN_ASIGN_VENDER))
							{
								for(i=0;i<arr.length;i++)
								{
									if(arr[i]>0)
									{
										out.println("<li style='width:150px;border: 1px solid #855555;background-color: #E1EFFF;font-size: 15px;'>机器"+arr[i]+" <a href='SetAccessDisable?uid="+ tub.getId() +"&vid="+arr[i]+"'>Delete</a></li>");
									}
								}
							}else
							{
								for(i=0;i<arr.length;i++)
								{
									if(arr[i]>0)
									{
										out.println("<li style='width:150px;border: 1px solid #855555;background-color: #E1EFFF;font-size: 15px;'>机器"+arr[i]+"</li>");
									}
								}
							}
						}
						if(ub.AccessAble(UserBean.FUNID_CAN_ASIGN_VENDER))
						{
						%>
						<li style='width:150px;border: 1px solid #855555;background-color: #E1EFFF;font-size: 15px;'><a href="javascript:void(0);" id="SetAccessVender">Add Vending Access</a></li>
						<%
						} %>
					</ul>
					<%} 
					System.out.println(ub.getCanAccessSellerid());
					if(ub.AccessAble(UserBean.FUNID_CAN_ASIGN_VENDER))
					{
						int[] arr=ToolBox.StringToIntArray(ub.getCanAccessSellerid());
						int i=0;
					%>
				    <input type="hidden" name="canAccessVender" id="canAccessVender" value="<%=tub.getCanAccessSellerid()%>" />
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
								for(i=0;i<arr.length;i++)
								{
									if(arr[i]>0)
									{
										if(tub.CanAccessSeller(arr[i]))
										{
											out.print("<li style='width:150px;'><label><input type='checkbox' checked='checked' name='canAccessVender1' value="+ arr[i] +" />机器"+arr[i]+"</label></li>");
										}
										else
										{
											out.print("<li style='width:150px;'><label><input type='checkbox' name='canAccessVender1' value="+ arr[i] +" />机器"+arr[i]+"</label></li>");
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
				      <%=ToolBox.getYMDHM(tub.getCreatetime())%>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Last Login Time</label>
				    <div class="col-sm-3">
				      <%=ToolBox.getYMDHM(tub.getLastLoginTime())%>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Profile ID</label>
				    	<div class="col-sm-3">
				    	<%if(groupBean!=null)
				    		{%>
						    <%=String.format("[%d]-%s", groupBean.getId(),groupBean.getGroupname()) %>
							<%}else{
								%>
								Invalid Profile ID 
								<%
							} %>
						</div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Last Login IP</label>
				    <div class="col-sm-3">
				      <%=tub.getLastloginip()%>
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
				<p style="text-align: left;margin-left: 200px;margin-top: 20px;">
	注意：请谨慎修改权限，有被锁的风险。如有疑问请联系管理员！
	</p>
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

