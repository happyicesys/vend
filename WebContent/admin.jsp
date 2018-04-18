<%@page import="com.clsConst"%>
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
	//System.out.println(ub.getAdminrights());
	if(!ub.AccessAble(UserBean.FUNID_CAN_ACCESS_WEB))
	{
		request.setAttribute("message", "Unable to "+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ACCESS_WEB]);
		request.setAttribute("LAST_URL", "index.jsp");
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
	    <title><%=ToolBox.WEB_NAME %></title>
	    <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet">
	    <!--[if lte IE 6]>
		<link rel="stylesheet" type="text/css" href="css/bootstrap/bootstrap-ie6.css">
		<link rel="stylesheet" type="text/css" href="css/bootstrap/ie.css">
		<![endif]-->
		
	    <link href="css/bootstrap/metisMenu.min.css" rel="stylesheet">
	    <link href="css/bootstrap/timeline.css" rel="stylesheet">
	    <link href="css/bootstrap/admin.css" rel="stylesheet">
	    <link href="css/bootstrap/morris.css" rel="stylesheet">
	    <link href="css/bootstrap/font-awesome.min.css" rel="stylesheet" type="text/css">
	    <!--[if IE]>
		<link rel="stylesheet" type="text/css" href="css/bootstrap/jr.css">
		<![endif]-->
		<!--[if lt IE 9]>
		  <script src="js/bootstrap/html5shiv.min.js"></script>
		  <script src="js/bootstrap/respond.min.js"></script>
		<![endif]-->
	</head>
	<body>
		<!-- 头部 -->
	    <div id="wrapper" class="jrnavbar">
	    	<nav class="navbar navbar-default navbar-static-top" role="navigation" id="jrnavbar" style="margin-bottom: 0;background: #438eb9;">
	            <div class="navbar-header">
	                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
	                    <span class="sr-only">Toggle navigation</span>
	                    <span class="icon-bar"></span>
	                    <span class="icon-bar"></span>
	                    <span class="icon-bar"></span>
	                </button>
	                <a class="navbar-brand" href="admin.jsp" style="color: #fff;font-size: 24px;"><small><%=ToolBox.WEB_NAME %></small></a>
	            </div>
	            <ul class="nav navbar-top-links navbar-right">
	                
	                <li class="dropdown">
	                    <i  style="color: #FFF;" class="fa fa-user fa-fw"></i> <span style="color: #FFF;"><%=ub.getAdminusername()+"欢迎您！  IP:" +request.getRemoteAddr() %></span>
	                </li>
	                
	                <li class="dropdown">
	                    <a href="AddLiuyan.jsp" target="main" style="color: #FFF;">
	                        <i class="fa fa-comments fa-fw"></i> <span>Feedback</span>
	                    </a>
	                    
	                </li>
					<%if(ub.AccessAble(UserBean.FUNID_CAN_SET_MYSELF_INFO))
					{%>
	                <li class="dropdown">
	                    <a href="UserInfo.jsp" target="main" style="color: #FFF;">
	                        <i class="fa fa-info fa-fw"></i> <span>Personal Message</span>
	                    </a>
	                </li>
	                <%} %>
	                <li class="dropdown">
	                    <a href="Exit.jsp" style="color: #FFF;">
	                        <i class="fa fa-sign-out fa-fw"></i> <span>Logout</span>
	                    </a>
	                    
	                </li>
	            </ul>

	            <div class="navbar-default sidebar" role="navigation">
	                <div class="sidebar-nav navbar-collapse">
	                    <ul class="nav" id="side-menu">
	                    	<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_VENDER)||
									ub.AccessAble(UserBean.FUNID_CAN_ADD_VENDER)||
									ub.AccessAble(UserBean.FUNID_CAN_VIEW_VENDER_MAP)||
									ub.AccessAble(UserBean.FUNID_CAN_VIEW_PORT)||
									ub.AccessAble(UserBean.FUNID_CAN_VIEW_GOODS)||
									ub.AccessAble(UserBean.FUNID_CAN_ADD_GOODS)
								) 
							{%>
	                        <li>
	                            <a href="#"><i class="fa fa-wrench fa-fw"></i> Setting Management<span class="fa arrow"></span></a>
	                            <ul class="nav nav-second-level">
									<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_VENDER))
									{%>
	                                <li><a href="./VenderList" target="main">Vending Machine List</a></li>
	                                <%} %>
	                                
									<%if(ub.AccessAble(UserBean.FUNID_CAN_ADD_VENDER))
									{%>
									<li><a href="AddVender.jsp" target="main">Add Vending</a></li>
									<%} %>
									
									<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_VENDER_MAP))
									{%>
									<li><a href="map.jsp" target="main">Map</a></li>
									<%} %>
									
									<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_PORT))
									{%>
									<li><a href="PortList.jsp" target="main">Channel List</a></li>
									<li><a href="quick_look.jsp" target="main">Out of Stock List</a></li>
									<li><a href="quick_err_look.jsp" target="main">Malfunction Quick View</a></li>
									<%} %>
									
									<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_GOODS)) 
									{%>
									<li><a href="./GoodsList" target="main">Product List</a></li>
									<%} %>
									
									<%if(ub.AccessAble(UserBean.FUNID_CAN_ADD_GOODS)) 
									{%>
									<li><a href="addGoodsInfo.jsp" target="main">Add Product</a></li>
									<%} %>
									
	                            </ul>
	                        </li>
	                        <%} %>
							<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_TRADE_RECORD)||
									ub.AccessAble(UserBean.FUNID_CAN_VIEW_STASTIC)||
									ub.AccessAble(UserBean.FUNID_CAN_REFUND)||
									ub.AccessAble(UserBean.FUNID_CAN_VIEW_REFUND_LOG)
								) 
							{%>
							<li>
	                            <a href="#"><i class="fa fa-bar-chart-o fa-fw"></i> Transaction Management<span class="fa arrow"></span></a>
	                            <ul class="nav nav-second-level">
									<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_TRADE_RECORD)) 
									{%>
	                                <li><a href="TradeList.jsp" target="main">Transaction Inquiry</a></li>
	                                <%} %>
	                                
									<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_STASTIC)) 
									{%>
	                                <li><a href="Report.jsp" target="main">Monthly Report</a></li>
	                                <li><a href="Report2.jsp" target="main">Daily Report</a></li>
									<li><a href="Audit.jsp" target="main">Transaction Staticstic</a></li>
									<%} %>
									
									<%if(ub.AccessAble(UserBean.FUNID_CAN_REFUND)) 
									{%>
									<li><a href="feeback.jsp" target="main">Manual Refund</a></li>
									<%} %>
									
									<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_REFUND_LOG)) 
									{%>
									<li><a href="viewfeebacklog.jsp" target="main">Check Refund Record</a></li>
									<%} %>
	                            </ul>
	                        </li>
	                        <%} %>
							<%if(ub.AccessAble(UserBean.FUNID_CAN_ADD_BIND_AL_WX_USER)||
									ub.AccessAble(UserBean.FUNID_CAN_DELTE_VENDER)||
									ub.AccessAble(UserBean.FUNID_CAN_VIEW_FETCH_GOODS_CODE)||
									ub.AccessAble(UserBean.FUNID_CAN_VIEW_GROUP_ID)||
									ub.AccessAble(UserBean.FUNID_CAN_CREATE_GROUP_ID)||
									ub.AccessAble(UserBean.FUNID_CAN_MOD_SELF_GROUP_ID)
								) 
							{%>
							<li>
	                            <a href="#"><i class="fa fa-sitemap fa-fw"></i> Admin Management<span class="fa arrow"></span></a>
	                            <ul class="nav nav-second-level">
									<%if(ub.AccessAble(UserBean.FUNID_CAN_ADD_BIND_AL_WX_USER)) 
									{%>
	                                <li><a href="BindVenderToUser.jsp" target="main">Account Binding</a></li>
	                                <%} %>
									<%if(ub.AccessAble(UserBean.FUNID_CAN_DELTE_VENDER)) 
									{%>
	                          	    <li><a href="DelVender.jsp" target="main">Remove Vending Machine</a></li>
	                          	    <%} %>
									<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_FETCH_GOODS_CODE)) 
									{%>
									<li><a href="FetchGoodsCodeList.jsp" target="main">Retrieve Product List</a></li>
									<%} %>
									<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_GROUP_ID)) 
									{%>
									<li><a href="GroupList.jsp" target="main">Profile List</a></li>
									<%} %>
									<%if(ub.AccessAble(UserBean.FUNID_CAN_CREATE_GROUP_ID)) 
									{%>
									<li><a href="AddGroup.jsp" target="main">Add Profile</a></li>
									<%} %>
									<%if(ub.AccessAble(UserBean.FUNID_CAN_MOD_SELF_GROUP_ID)) 
									{%>
									<li><a href="GroupManager.jsp" target="main">Profile Edit</a></li>
									<%} %>
									
									<%if(ub.getAdminusername().toLowerCase().equals(clsConst.POWER_USER_NAME)) //
									{%>
									<li><a href="DoRequestOperation.jsp" target="main">Special Operation</a></li>
									<%} %>
	                            </ul>
	                        </li>
	                        <%} %>

							<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_USER_LST)||
									ub.AccessAble(UserBean.FUNID_CAN_VIEW_USER_LST)||
									ub.AccessAble(UserBean.FUNID_CAN_SET_MYSELF_INFO)
								) 
							{%>
							<li>
	                            <a href="#"><i class="fa fa-users fa-fw"></i> User Management<span class="fa arrow"></span></a>
	                            <ul class="nav nav-second-level">
									<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_USER_LST)) 
									{%>
	                                <li><a href="UserList.jsp" target="main">Manager Management</a></li>
	                                <%} %>
									<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_USER_LST)) 
									{%>
									<li><a href="AddUser.jsp"  target="main">Add Manager</a></li>
									<%} %>
									<%if(ub.AccessAble(UserBean.FUNID_CAN_SET_MYSELF_INFO))
									{%>
									<li><a href="UserInfo.jsp" target="main">Personal Message Management</a></li>
									<%} %>
	                            </ul>
	                        </li>
	                         <%} %>
	                         
	                         <%if(ub.AccessAble(UserBean.FUNID_CAN_ADD_CUSTOMER)||
									ub.AccessAble(UserBean.FUNID_CAN_CHANGE_CUSTOMER)||
									ub.AccessAble(UserBean.FUNID_CAN_CHARGE_FOR_CUSTOMER)
								) 
							{%>
							<li>
	                            <a href="#"><i class="fa fa-credit-card fa-fw"></i> Customer Management<span class="fa arrow"></span></a>
	                            <ul class="nav nav-second-level">
									<%if(ub.AccessAble(UserBean.FUNID_CAN_ADD_CUSTOMER))
									{%>
									<li><a href="AddCustomer.jsp" target="main">Add Customer</a></li>
									<%} %>
									<%if(ub.AccessAble(UserBean.FUNID_CAN_CHANGE_CUSTOMER)) 
									{%>
	                                <li><a href="CustomerList.jsp" target="main">Customer Management</a></li>
	                                <%} %>
									<%if(ub.AccessAble(UserBean.FUNID_CAN_CHARGE_FOR_CUSTOMER)) 
									{%>
									<li><a href="customercharge.jsp"  target="main">Customer Topup</a></li>
									<li><a href="chargedatalst.jsp"  target="main">Topup Record</a></li>
									<%} %>
									
									<%if(ub.AccessAble(UserBean.FUNID_CAN_SWIPE_QUHUO)) 
									{%>
									<li><a href="quhuo.jsp"  target="blank">Batch Swipe Card</a></li>
									<%} %>
	                            </ul>
	                        </li>
	                         <%} %>
	                    </ul>
	                </div>
	            </div>
        	</nav>
        	<div id="page-wrapper">
        	<% 
        	Object sessionobj=session.getAttribute("currentpage");
        	String currentpage=null;
        	if(sessionobj==null)
        	{
        		currentpage="./MainHome.jsp";
        	}
        	else
        	{
        		currentpage=sessionobj.toString();
        	}
        	%>
        	
        		<iframe id="mainframe" style="margin-left:-5px;margin-right:5px;min-height:700px;" name="main" marginheight=0 src="<%=currentpage %>" frameborder="0"  width="100%" scrolling="no" height="700px;"></iframe>
        	</div>
	    </div>
	 <script src="js/bootstrap/jquery-1.12.0.min.js"></script>
    <script src="js/bootstrap/bootstrap.min.js"></script>
    <!--[if lte IE 6]>
  	<script type="text/javascript" src="js/bootstrap-ie.js"></script>
  	<![endif]-->
    <script src="js/bootstrap/metisMenu.min.js"></script>
    <script src="js/bootstrap/admin.js"></script>
    <script>
	  //注意：下面的代码是放在和iframe同一个页面调用,放在iframe下面
	    $("#mainframe").load(function () {
	    var mainheight = $(this).contents().find("body").height() + 200;
	     $(this).height(mainheight);
	    });
    </script>
	</body>
</html>