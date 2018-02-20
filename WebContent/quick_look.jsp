<%@page import="beans.PortBean"%>
<%@ page import="beans.VenderBean"%>
<%@ page import="beans.UserBean"%>
<%@ page import="com.ado.SqlADO"%>
<%@ page import="com.tools.ToolBox"%>
<%@ page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
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
	
	
	if(!ub.AccessAble(UserBean.FUNID_CAN_VIEW_PORT))
	{
		request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_VIEW_PORT]);
		request.setAttribute("LAST_URL", "index.jsp");
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
	}
    
    
    ArrayList<VenderBean> livb=SqlADO.getVenderListByIdLimint(ub.getCanAccessSellerid());
    
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
    <meta http-equiv="X-UA-Compatible" content="IE=9" />
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta name="description" content=""/>
    <meta name="author" content=""/>
    <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet"/>
    <link href="css/bootstrap/metisMenu.min.css" rel="stylesheet"/>
    <link href="css/bootstrap/timeline.css" rel="stylesheet"/>
    <link href="css/bootstrap/admin.css" rel="stylesheet"/>
    <link href="css/bootstrap/morris.css" rel="stylesheet"/>
    <link href="css/bootstrap/font-awesome.min.css" rel="stylesheet" type="text/css"/>
    <link href="./jquery_ui/css/cupertino/jquery-ui.min.css" rel="stylesheet" type="text/css" />
    <link href="./jquery_ui/css/showLoading.css" rel="stylesheet" type="text/css" />
     <!--[if lte IE 6]>
	<link rel="stylesheet" type="text/css" href="/css/bootstrap/bootstrap-ie6.css"/>
	<![endif]-->
	<!--[if lte IE 7]>
	<link rel="stylesheet" type="text/css" href="/css/bootstrap/ie.css"/>
	<![endif]-->
    <script src="js/bootstrap/jquery-1.12.0.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="./jquery_ui/js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="./jquery_ui/js/jquery.showLoading.min.js"></script>
    <script type="text/javascript" src="./jquery_ui/js/jquery.ui.datepicker-zh-TW.js"></script>
<style type="text/css">
.quick-look
{
	-webkit-border-horizontal-spacing: 0px;
	-webkit-border-image: none;
	-webkit-border-vertical-spacing: 0px;
	border-bottom-color: white;
	border-bottom-left-radius: 3px;
	border-bottom-right-radius: 3px;
	border-bottom-style: none;
	border-width: 0px;
	border-collapse: separate;
	border-left-color: white;
	border-left-style: none;
	border-right-color: white;
	border-right-style: none;
	border-top-color: white;
	border-top-left-radius: 3px;
	border-top-right-radius: 3px;
	border-top-style: none;
	font-family: Ubuntu, Helvetica, Arial, sans-serif;
	line-height: 14px;
	max-width: none;
	text-align: left;
	vertical-align: baseline;
	white-space: nowrap;
	padding:5px;
	margin:3px;
	display:block;
	float:left;
	width:300px;
	font-size:13px;
}
</style>
<title>售货机缺货状况快速查看</title>
</head>
<body style="background-color: #fff;">
	<div class="breadcrumbs" id="breadcrumbs" style="margin-top:5px;">
						<ul class="breadcrumb">
							<li>
								<span class="glyphicon glyphicon-home"></span>
								<a href="MainHome.jsp" target="main" style="padding-left:5px;margin-left:5px;">首页</a>
							</li>

							<li>
								<a href="#">设备管理</a>
							</li>
							<li class="active">售货机缺货状况快速查看</li>
						</ul><!-- .breadcrumb -->

						<!-- #nav-search -->
					</div>
	<div class="row" style="overflow-y:auto;">
    	<div class="col-xs-12">
        	<div class="box">
                <div class="box-body">
					<div id="DataTables_Table_0_wrapper" class="dataTables_wrapper" role="grid">
						<form action="SellerUpdate" method="post">
							<table class="table table-bordered table-hover" style="overflow-y:auto; width:100%;height:100px;border-spacing: 0px;">
								<thead>
									<tr role="row" style="background-color: #f5f5f5;">
										<th style="width: 100px;">终端编号</th>
										<th style="width: 100px;">查看详情</th>
										<th >货道缺货状况</th>
									</tr>											
								</thead>
								<tbody role="alert" aria-live="polite" aria-relevant="all">
									<%
									if(livb==null)
									{%>
										  <tr class="odd">
										  	<td class="center" colspan="3" align="center">
										    	售货机缺货状况快速查看
											</td>
										  </tr>
									<%}
									else
									{
										ArrayList<PortBean> pbli=null;
										boolean quehuo=false;
										for(VenderBean vb:livb)
										{
											pbli=SqlADO.getPortBeanList(vb.getId());
											quehuo=false;	
											for(PortBean pb:pbli)
											{
												if(pb.getCapacity()>pb.getAmount())
												{
													quehuo=true;
													break;
												}
											}
											
											if(!quehuo)
											{
												continue;
											}
										%>
										<tr class="odd">
											<td class="center "><%=vb.getId() %></td>
											<td class="center "><a href="PortList.jsp?mid=<%=vb.getId() %>">查看</a></td>
											<td> 
												<ul>
													<%
														for(PortBean pb:pbli)
														{
															if(pb.getCapacity()>pb.getAmount())
															{
														%>
														<li class="quick-look"><span>编号:<%=pb.getInneridname()%></span> <span>缺货:<%=String.format("%3d",pb.getCapacity()-pb.getAmount()) %></span> <span>名称:<%=pb.getGoodroadname() %></span></li>
														<%
															}
														}
													%>
												</ul>
											</td>
										</tr>
										<%
										}
										
										%>
													<tr class="odd">
													 <td colspan="3" align="center" >所有数据显示完毕</td>
													</tr>
										<%
									}
									%>
								</tbody>
							</table>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>
