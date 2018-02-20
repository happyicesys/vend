<%@page import="java.util.ListIterator"%>
<%@ page import="beans.UserBean"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.ado.SqlADO"%>
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
	
	
	if(!ub.AccessAble(UserBean.FUNID_CAN_VIEW_USER_LST))
	{
		request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_VIEW_USER_LST]);
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
	}
	ArrayList<UserBean> uli=null;
    if(ub.getAdminusername().toLowerCase().equals("andy_he"))
    {
    	uli=UserBean.getUserBeanListByright();
    }
    else
    {
   		uli=UserBean.getUserBeanListByright(ub.getGroupid());
    }

   	
  	int RsCount=0;
  	int pagecount=ub.getPagecount();
 	int Page=ToolBox.filterInt(request.getParameter("page"));
 	if(Page==0) 
 	{
 		Page=1;
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
    <script type="text/javascript" src="./jquery_ui/js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="./jquery_ui/js/jquery.showLoading.min.js"></script>
    <script type="text/javascript" src="./jquery_ui/js/jquery.ui.datepicker-zh-TW.js"></script>
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
							<li class="active">管理员管理</li>
						</ul>
	</div>
	<div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
			  <div class="row">
					<div class="col-xs-12">
						<div class="dataTables_length" id="dataTables-example_length">
							<form class="form-horizontal" role="form">
									<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_USER_LST)) 
									{%>
								<button type="button" class="btn btn-default" style="background-color:#f4f4f4;float:right;" onclick="javascript:location.href='AddUser.jsp';" >添加账号</button>
									<%} %>
							</form>
						</div>
					</div>
								
			  </div>
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
										<th style="width: 100px;">ID</th>
										<th style="width: 100px;">用户名</th>
										<th style="width: 100px;">姓名</th>
										<th style="width: 100px;">手机</th>
										<th style="width:600px;">登录IP</th>
										<th style="width:180px;">登录时间</th>
										<th style="width:180px;">操作</th>
									</tr>											
								</thead>
								<tbody role="alert" aria-live="polite" aria-relevant="all">
									<% 
		
										int count=0;
										if(uli!=null)
										{
											RsCount=uli.size();
											ListIterator<UserBean> it=uli.listIterator();
											
											UserBean tub;
								
											while(it.hasNext())
											{
												tub=it.next();
												count++;
												if(count<=(Page-1)*pagecount)
												{
													continue;
												}
												
												if(count>(Page)*pagecount)
												{
													break;
												}
								
										%>
													<tr class="odd">
														<td class="center"><%=tub.getId() %></td>
														<td class="center"><%=tub.getAdminusername() %></td>
														<td class="center">
															<%=tub.getAdminname() %>
														</td>
														<td class="center"><%=tub.getAdminmobilephone() %></td>
														<td class="center">[<%=tub.getLastloginip() %>]</td>
														<td class="center"><%=ToolBox.getYMDHMS(tub.getLastLoginTime() )%></td>
														<td class="center ">
														<%if((ub.getId()==tub.getId())||(tub.getAdminusername().toLowerCase().equals("andy_he")))
														{%> 
															<%if(ub.AccessAble(UserBean.FUNID_CAN_UPDATE_USER))
															{%> 
															<a class="btn btn-success" disabled="disabled">
																<i class="glyphicon glyphicon-zoom-in icon-white"></i>
																修改
															</a>
															<%} %>
															<%if(ub.AccessAble(UserBean.FUNID_CAN_DEL_USER))
															{%> 
															<a class="btn btn-info" disabled="disabled">
																<i class="glyphicon glyphicon-edit icon-white"></i>
																删除
															</a>
															<%} %>
														<%}else 
														{%> 
															<%if(ub.AccessAble(UserBean.FUNID_CAN_UPDATE_USER))
															{%> 
															<a class="btn btn-success" href="UserManager.jsp?id=<%=tub.getId()%>">
																<i class="glyphicon glyphicon-zoom-in icon-white"></i>
																修改
															</a>
															<%} %>
															
															<%if(ub.AccessAble(UserBean.FUNID_CAN_DEL_USER))
															{%> 
															<a class="btn btn-info" href="UserDelete?id=<%=tub.getId()%>">
																<i class="glyphicon glyphicon-edit icon-white"></i>
																删除
															</a>
															<%} %>
														<%}%>
														</td>
													</tr>
										<%}
										}%>
										<tr class="odd">
											<td align="center" colspan="7">
											<%
										    	if(RsCount>0) 
										    	{
										    		out.println(ToolBox.getpages(null, "#999", Page, pagecount, RsCount));
												 }
										    	else 
										    	{
										    		out.println("<span class='waring-label'>无用户列表数据！</span>");
										    	}
										    			
										  	%>
											</td>
										</tr>
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