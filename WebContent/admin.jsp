<%@page import="com.clsConst"%>
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
	//System.out.println(ub.getAdminrights());
	if(!ub.AccessAble(UserBean.FUNID_CAN_ACCESS_WEB))
	{
		request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ACCESS_WEB]);
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
	                        <i class="fa fa-comments fa-fw"></i> <span>意见反馈</span>
	                    </a>
	                    
	                </li>
					<%if(ub.AccessAble(UserBean.FUNID_CAN_SET_MYSELF_INFO))
					{%>
	                <li class="dropdown">
	                    <a href="UserInfo.jsp" target="main" style="color: #FFF;">
	                        <i class="fa fa-info fa-fw"></i> <span>个人信息</span>
	                    </a>
	                </li>
	                <%} %>
	                <li class="dropdown">
	                    <a href="Exit.jsp" style="color: #FFF;">
	                        <i class="fa fa-sign-out fa-fw"></i> <span>退出系统</span>
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
	                            <a href="#"><i class="fa fa-wrench fa-fw"></i> 设备管理<span class="fa arrow"></span></a>
	                            <ul class="nav nav-second-level">
									<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_VENDER))
									{%>
	                                <li><a href="./VenderList" target="main">售货机列表</a></li>
	                                <%} %>
	                                
									<%if(ub.AccessAble(UserBean.FUNID_CAN_ADD_VENDER))
									{%>
									<li><a href="AddVender.jsp" target="main">添加售货机</a></li>
									<%} %>
									
									<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_VENDER_MAP))
									{%>
									<li><a href="map.jsp" target="main">地图信息</a></li>
									<%} %>
									
									<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_PORT))
									{%>
									<li><a href="PortList.jsp" target="main">货道列表</a></li>
									<li><a href="quick_look.jsp" target="main">缺货快速浏览</a></li>
									<li><a href="quick_err_look.jsp" target="main">故障快速浏览</a></li>
									<%} %>
									
									<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_GOODS)) 
									{%>
									<li><a href="./GoodsList" target="main">商品列表</a></li>
									<%} %>
									
									<%if(ub.AccessAble(UserBean.FUNID_CAN_ADD_GOODS)) 
									{%>
									<li><a href="addGoodsInfo.jsp" target="main">添加商品</a></li>
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
	                            <a href="#"><i class="fa fa-bar-chart-o fa-fw"></i> 交易管理<span class="fa arrow"></span></a>
	                            <ul class="nav nav-second-level">
									<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_TRADE_RECORD)) 
									{%>
	                                <li><a href="TradeList.jsp" target="main">交易查询</a></li>
	                                <%} %>
	                                
									<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_STASTIC)) 
									{%>
	                                <li><a href="Report.jsp" target="main">月报表</a></li>
	                                <li><a href="Report2.jsp" target="main">日报表</a></li>
									<li><a href="Audit.jsp" target="main">交易统计</a></li>
									<%} %>
									
									<%if(ub.AccessAble(UserBean.FUNID_CAN_REFUND)) 
									{%>
									<li><a href="feeback.jsp" target="main">交易人工退款</a></li>
									<%} %>
									
									<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_REFUND_LOG)) 
									{%>
									<li><a href="viewfeebacklog.jsp" target="main">查看退款记录</a></li>
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
	                            <a href="#"><i class="fa fa-sitemap fa-fw"></i> 高级管理<span class="fa arrow"></span></a>
	                            <ul class="nav nav-second-level">
									<%if(ub.AccessAble(UserBean.FUNID_CAN_ADD_BIND_AL_WX_USER)) 
									{%>
	                                <li><a href="BindVenderToUser.jsp" target="main">账号绑定</a></li>
	                                <%} %>
									<%if(ub.AccessAble(UserBean.FUNID_CAN_DELTE_VENDER)) 
									{%>
	                          	    <li><a href="DelVender.jsp" target="main">删除售货机</a></li>
	                          	    <%} %>
									<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_FETCH_GOODS_CODE)) 
									{%>
									<li><a href="FetchGoodsCodeList.jsp" target="main">取货码列表</a></li>
									<%} %>
									<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_GROUP_ID)) 
									{%>
									<li><a href="GroupList.jsp" target="main">集团列表</a></li>
									<%} %>
									<%if(ub.AccessAble(UserBean.FUNID_CAN_CREATE_GROUP_ID)) 
									{%>
									<li><a href="AddGroup.jsp" target="main">添加集团</a></li>
									<%} %>
									<%if(ub.AccessAble(UserBean.FUNID_CAN_MOD_SELF_GROUP_ID)) 
									{%>
									<li><a href="GroupManager.jsp" target="main">本集团修改</a></li>
									<%} %>
									
									<%if(ub.getAdminusername().toLowerCase().equals(clsConst.POWER_USER_NAME)) //
									{%>
									<li><a href="DoRequestOperation.jsp" target="main">特殊操作</a></li>
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
	                            <a href="#"><i class="fa fa-users fa-fw"></i> 用户管理<span class="fa arrow"></span></a>
	                            <ul class="nav nav-second-level">
									<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_USER_LST)) 
									{%>
	                                <li><a href="UserList.jsp" target="main">管理员管理</a></li>
	                                <%} %>
									<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_USER_LST)) 
									{%>
									<li><a href="AddUser.jsp"  target="main">添加管理员</a></li>
									<%} %>
									<%if(ub.AccessAble(UserBean.FUNID_CAN_SET_MYSELF_INFO))
									{%>
									<li><a href="UserInfo.jsp" target="main">个人信息管理</a></li>
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
	                            <a href="#"><i class="fa fa-credit-card fa-fw"></i> 会员管理<span class="fa arrow"></span></a>
	                            <ul class="nav nav-second-level">
									<%if(ub.AccessAble(UserBean.FUNID_CAN_ADD_CUSTOMER))
									{%>
									<li><a href="AddCustomer.jsp" target="main">添加会员</a></li>
									<%} %>
									<%if(ub.AccessAble(UserBean.FUNID_CAN_CHANGE_CUSTOMER)) 
									{%>
	                                <li><a href="CustomerList.jsp" target="main">会员管理</a></li>
	                                <%} %>
									<%if(ub.AccessAble(UserBean.FUNID_CAN_CHARGE_FOR_CUSTOMER)) 
									{%>
									<li><a href="customercharge.jsp"  target="main">会员充值</a></li>
									<li><a href="chargedatalst.jsp"  target="main">充值记录</a></li>
									<%} %>
									
									<%if(ub.AccessAble(UserBean.FUNID_CAN_SWIPE_QUHUO)) 
									{%>
									<li><a href="quhuo.jsp"  target="blank">批量提货刷卡</a></li>
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