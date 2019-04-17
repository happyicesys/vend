<%@page import="beans.clsGroupBean"%>
<%@page import="com.clsEvent"%>
<%@ page import="beans.VenderBean"%>
<%@ page import="beans.UserBean"%>
<%@ page import="com.ado.SqlADO"%>
<%@ page import="com.tools.ToolBox"%>
<%@ page import="java.util.ArrayList"%>
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

if(!ub.AccessAble(UserBean.FUNID_CAN_UPDATE_VENDER))
{
	request.setAttribute("message", "Unable to "+UserBean.RIGHT_DES[UserBean.FUNID_CAN_UPDATE_VENDER]);
	request.getRequestDispatcher("message.jsp").forward(request, response);
	return;
}

session.setAttribute("currentpage", request.getRequestURI()+"?"+request.getQueryString());



int id=ToolBox.filterInt(request.getParameter("mid"));


if(!ub.CanAccessSeller(id))
{
	request.setAttribute("message", ToolBox.CANNTACCESS);
	request.getRequestDispatcher("message.jsp").forward(request, response);
	return;
}


	if(id>0)
	{
    	VenderBean vb=null;
		vb=SqlADO.getVenderBeanByid(id);
		if(vb!=null)
		{
			clsGroupBean groupBean =clsGroupBean.getGroup(vb.getGroupid());
			
%>
<!DOCTYPE>
<html>
<head>
<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
    <meta http-equiv="X-UA-Compatible" content="IE=8" />
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <!-- Bootstrap Core CSS -->
    <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet">
    <!-- MetisMenu CSS -->
    <link href="css/bootstrap/metisMenu.min.css" rel="stylesheet">
    <!-- Timeline CSS -->
    <link href="css/bootstrap/timeline.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="css/bootstrap/admin.css" rel="stylesheet">
    <!-- Morris Charts CSS -->
    <link href="css/bootstrap/morris.css" rel="stylesheet">
    <!-- Custom Fonts -->
    <link href="css/bootstrap/font-awesome.min.css" rel="stylesheet" type="text/css">
    <!-- <link href="./images/adminstyle.css" rel="stylesheet" type="text/css" /> -->
    <link href="./jquery_ui/css/cupertino/jquery-ui.min.css" rel="stylesheet" type="text/css" />
    <link href="./jquery_ui/css/showLoading.css" rel="stylesheet" type="text/css" />
    <script  language="javascript"  type="text/javascript" src="./jquery_ui/js/jquery-1.9.1.js"></script>
    <script language="javascript" type="text/javascript" src="./jquery_ui/js/jquery-ui.min.js"></script>
    <script language="javascript" type="text/javascript" src="./jquery_ui/js/jquery.showLoading.min.js"></script>
    <script language="javascript" type="text/javascript" src="./jquery_ui/js/jquery.ui.datepicker-zh-TW.js"></script>
    <script type="text/javascript" src="js/jquery.blockUI.js"></script>
    <script type="text/javascript">
var ShowMapWin=function()
{
	window.open('selectpoint.html', 'SelectPoint', 'height=800, width=600, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=no,location=n o, status=no');
}



</script>
<title>Check/Edit Vending--<%=id%></title>
</head>
<body style="background-color: #fff;">
	 <div class="breadcrumbs" id="breadcrumbs" style="margin-top:5px;">
						<ul class="breadcrumb">
							<li>
								<span class="glyphicon glyphicon-home"></span>
								<a href="MainHome.jsp" target="main" style="padding-left:5px;margin-left:5px;">Home</a>
							</li>

							<li>
								<a href="#">Setting Management</a>
							</li>
							<li class="active">Check/Edit<%=String.format("%03d",id)%>GPRS Signal</li>
						</ul><!-- .breadcrumb -->

						<!-- #nav-search -->
					</div>
			  	<form class="form-horizontal" role="form" action="SellerUpdate" method="post">
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Terminal Name</label>
				    <div class="col-sm-3">
				      <input name="tname" type="text" class="form-control input-sm input-sm"  placeholder="Terminal Name(Required)" value="<%=vb.getTerminalName()%>">
				      <span id="tips_t" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Online Time</label>
				    <div class="col-sm-3">
				      <%=ToolBox.getYMDHMS(vb.getBTime())%>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Refresh Time</label>
				    <div class="col-sm-3">
				      <%=ToolBox.getYMDHMS(vb.getUpdateTime())%>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Number of Channel</label>
				    <div class="col-sm-3">
				      <input name="portcount" id="portcount" readonly="readonly" value="<%=vb.getGoodsPortCount() %>" type="text" class="form-control input-sm input-sm"  placeholder="Number of Channel(Required)">
					</div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Activity ID</label>
				    <div class="col-sm-3">
				      <input name="huodongid" readonly="readonly" value="<%=vb.getHuodongId() %>" type="text" class="form-control input-sm input-sm">
				    </div>
				  </div>
				  <div class="form-group" style="border: medium  rgb(250,0,255)">
				    <label class="col-sm-4 control-label">Vending Model</label>
				    <div class="col-sm-3">
				      <input name="sellertype" type="text" class="form-control input-sm"  placeholder="Vending Model" value="<%=vb.getSellerTyp() %>"/>
				    </div>
				    
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Customer Service Tel</label>
				    <div class="col-sm-3">
				      <input name="server_tel"  type="text" class="form-control input-sm" value="<%=vb.getTelNum() %>" placeholder="Terminal Name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Account for Transfer</label>
				    <div class="col-sm-3">
				      <%
				      UserBean ubobj= UserBean.getUserBeanById(vb.getAdminId());
				      if(ubobj!=null)
				      {
				    	  out.print(ubobj.getAdminusername() + "-"+ubobj.getAdminname());
				      }
				      else
				      {
				    	  out.print("No transfer account founded");
				      }
				      %>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Vending Address</label>
				    <div class="col-sm-3">
				      <input name="address" type="text" class="form-control input-sm"  placeholder="Vending Address" value="<%=vb.getTerminalAddress() %>"><!-- 添加地址信息，便于地图查找 -->
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Vending Logitude</label>
				    <div class="col-sm-3">
				      <input id="lng" name="lng" type="text" class="form-control input-sm"  placeholder="Vending Logitude" readonly="readonly" value="<%=vb.getJindu() %>">
				    </div>
				    <div class="col-sm-5">
				      	<input class="btn btn-success" type="button" value="Lat Lng On Map" onclick="ShowMapWin()"/>
				      </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Vending Latitude</label>
				    <div class="col-sm-3">
				      <input id="lat" name="lat" type="text" class="form-control input-sm" placeholder="Vending Latitude" readonly="readonly" value="<%=vb.getWeidu() %>" >
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Welcome Message</label>
				    <div class="col-sm-4">
				      <textarea class="form-control" name="tipmes" cols="50" rows="4" ></textarea>
				    </div>
				    <div class="col-sm-3">
				      <span><%=vb.getTipMesOnLcd()%></span>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Is Online</label>
				    	<div class="col-sm-3">
						    <%=(vb.isIsOnline()?"Yes":"No")%>
						</div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Enable Auto Refund</label>
				    	<div class="col-sm-3">
						    <input name="auto_refund" type="checkbox" value="1" <%=(vb.getAuto_refund()==1?"checked=\"checked\"":"")%> />
						</div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Enable Manual Refund</label>
				    	<div class="col-sm-3">
						    <input name="manual_refund" type="checkbox" value="1" <%=(vb.getManual_refund()==1?"checked=\"checked\"":"")%> />
						</div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Enable Temp Alert</label>
				    	<div class="col-sm-3">
						    <input name="temp_alert" type="checkbox" value="1" <%=(vb.getTemp_alert()==1?"checked=\"checked\"":"")%> />
						</div>
				  </div>		
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Enable Offline Alert</label>
				    	<div class="col-sm-3">
						    <input name="offline_alert" type="checkbox" value="1" <%=(vb.getOffline_alert()==1?"checked=\"checked\"":"")%> />
						</div>
				  </div>				  		  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Allow Update Goods by PC</label>
				    	<div class="col-sm-3">
						    <input name="AllowUpdateGoodsByPc" type="checkbox" value="1" <%=(vb.getM_AllowUpdateGoodsByPc()==1?"checked=\"checked\"":"")%> />
						</div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Category</label>
				    <div class="col-sm-3">
					    <select class="form-control input-sm input-sm" name="category" id="category">
							<option value='0' >Please choose the keyboard type</option>
							<option value='16' >16 buttons numeric</option>
							<option value='18' >18 buttons alphanumeric</option>
							<option value='12' >1 to 1 select button</option>
						</select>
					</div>
				  </div>				  
				  <!--  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Profile ID</label>
				    	<div class="col-sm-3">

						    <%=(groupBean==null)?"集团没有归属":String.format("[%d]-%s",groupBean.getId(), groupBean.getGroupname()) %>
						</div>
				  </div>
				  -->
				  <div class="form-group">
				    <div class="col-sm-offset-4 col-sm-3">
				       <input name="id" type="hidden" value="<%=vb.getId()%>" /> 
				      <button type="submit" class="btn btn-success" value="Edit">Edit</button>
				      <button type="reset" class="btn btn-danger" value="取消">Cancel</button>
				      <button  class="btn btn-primary"  value="Back" type="button" onclick="javascript:history.go(-1)" >Back</button>
				    </div>
				  </div>
				</form>
</body>
</html>
<%
    		}else
    		{
    			request.setAttribute("message", "Parameters Error，没有找到该机器！");
    			request.getRequestDispatcher("message.jsp").forward(request, response);
    			return;
    		}
    	}else
    	{
			request.setAttribute("message", "Parameters Error，没有找到该机器！");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
    	}
%>