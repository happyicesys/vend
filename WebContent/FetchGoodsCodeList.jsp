<%@page import="java.text.Format"%>
<%@page import="com.ado.SqlADO"%>
<%@page import="beans.clsGetGoodsCode"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="beans.UserBean"%>
<%@page import="com.tools.ToolBox"%>
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
    
	if(!ub.AccessAble(UserBean.FUNID_CAN_VIEW_FETCH_GOODS_CODE))
	{
		request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_VIEW_FETCH_GOODS_CODE]);
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
	}
	
    
    %>
<%
	int pageindex=ToolBox.filterInt(request.getParameter("page"));

	int count_per_page =ub.getPagecount();
	
	if(pageindex==0)
	{
		pageindex=1;
	}
	
	int RsCount =SqlADO.getGetGoodsCodeCount();
	

	ArrayList<clsGetGoodsCode> gblst=SqlADO.getGetGoodsCodeLst(pageindex,count_per_page);


	//int colid=ToolBox.filterInt(request.getParameter("colid"));
%>
<!DOCTYPE html>
<html>
<head>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
			<li class="active">取货码列表</li>
		</ul>
	</div>
    <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
			  <div class="row">
					<div class="col-xs-12">
						<div class="dataTables_length" id="dataTables-example_length">
							<form class="form-horizontal" role="form" id="form2" name="form2" method="post" action="./GoodsList">
									<%if(ub.AccessAble(UserBean.FUNID_CAN_ADD_FETCH_GOODS_CODE)) 
									{%>
								<button type="button" class="btn btn-default" style="background-color:#f4f4f4;" type="button" value="添加取货码" onclick="javascript:location.href='AddFetchGoodsCode.jsp';">添加取货码</button>
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
										<th style="width: 100px;">取货时间</th>
										<th style="width: 100px;">售货机编号</th>
										<th style="width: 100px;">货道号</th>
										<th style="width: 100px;">取货码</th>
										<th style="width:100px;">金额</th>
										<th style="width: 100px;">创建时间</th>
										<th style="width: 100px;">商品编号</th>
										<th style="width: 100px;">取货状态</th>
										<th style="width:180px;">操作</th>
									</tr>											
								</thead>
								<tbody role="alert" aria-live="polite" aria-relevant="all">
									<%
										if(gblst==null)
										{
											out.print("<tr class='odd'><td colspan='9'>");
									    	out.print("<span class='waring-label'>没有查找到数据</span>");
									    	out.print("</td></tr>");
										}
										else
										{
											if(gblst.size()==0)
											{
												out.print("<tr class='odd'><td colspan='9'>");
												out.print("<span class='waring-label'>没有查找到数据</span>");
										    	out.print("</td></tr>");
											}
											else
											{
											
												int i=0;
												for(i=0;i<gblst.size();i++)
												{
													clsGetGoodsCode gb=gblst.get(i);
													
												 %>
													<tr class="odd">
														<td class="center"><%=gb.getGmtpayment() == null?"":gb.getGmtpayment()%></td>
														<td class="center"><%=gb.getMachineid()== null?"":gb.getMachineid()%></td>
														<td class="center"><%=gb.getInneridname()== null?"":gb.getInneridname()%></td>
														<td class="center"><%=gb.getTradeno() %></td>
														<td class="center"><%=gb.getTotalfee()%></td>
														<td class="center"><%=gb.getGmtcreate()%></td>
														<td class="center"><%=gb.getGoodsid()== null?"":gb.getGoodsid()%></td>
														<td class="center"><%=gb.getTransforstatus() == 0?"未取货":"已取货"%></td>
									
														<td class="center ">
															<a class="btn btn-success" href="./editFetchGoodsCode.jsp?id=<%=gb.getId()%>">
																<i class="glyphicon glyphicon-edit icon-white"></i>
																修改
															</a>
															<a class="btn btn-info" href="./DoDeletGetGoodsCode?id=<%=gb.getId()%>">
																<i class="glyphicon glyphicon-trash icon-white"></i>
																删除
															</a>
														</td>
													</tr>
										<%
										}
										%>
										<tr class="odd">
										<td align="center" colspan="10">
										<%
										
								    	if(RsCount>0) 
								    	{
								    		out.println(ToolBox.getpages(null, "#999", pageindex, count_per_page, RsCount));
										 }
								    	else 
								    	{
								    		out.println("您没有可以查看的数据！请确认您的用户账号是否正确！");
								    	}
										%>
										</td>
										</tr>
										<%
									}
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