<%@page import="beans.clsGetGoodsCode"%>
<%@page import="com.clsEvent"%>
<%@ page import="beans.VenderBean"%>
<%@ page import="beans.UserBean"%>
<%@page import="beans.clsGoodsBean"%>
<%@page import="com.tools.StringUtil"%>
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
		request.setAttribute("message", "Unable to "+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ADD_FETCH_GOODS_CODE]);
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
	}
	
    
    int id=ToolBox.filterInt(request.getParameter("id"));
    clsGetGoodsCode cg = SqlADO.getGoodsCodeById(id);
    if(id==0)
    {
		request.setAttribute("message", "Parameters Error, please input again");
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
    }
    
    			
        	%>
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
<!-- <script type="text/javascript">
$(document).ready(function(){ 
	$('#mySelect').change(function(){ 
		var p1=$(this).children('option:selected').val();//这就是selected的值 
	}) 
	}) 
</script> -->

<title>Editing Retrive Product ID</title>
</head>
<body>

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
			<li class="active">Editing Retrive Product ID</li>
		</ul>
	</div>
	<form class="form-horizontal" action="./AddGetGoodsCode" name="smartForm" method="post" onsubmit="return chkForm();" >
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Product</label>
				    <div class="col-sm-3">
				    	<select name="goodsid" id="mySelect">
						<%
							ArrayList<clsGoodsBean> gblst=clsGoodsBean.getGoodsBeanLst(ub.getGroupid());
							if(gblst!=null){
								for(clsGoodsBean cb:gblst){
									if(cb.getId()==ToolBox.filterInt(cg.getGoodsid())){
						%>
								<option value="<%=cb.getId() %>" selected="selected"><%=cb.getGoodsname() %></option>
						<%
							}else{
						%>
								<option value="<%=cb.getId() %>"><%=cb.getGoodsname() %></option>
						<%
							}
									}}
						%>
						</select>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Payment Amount</label>
				    <div class="col-sm-3">
				    <input type="hidden" name="uid" value="<%=cg.getId()%>">
				      <input name="totalfee" type="text" class="form-control input-sm input-sm" value="<%=cg.getTotalfee() %>" placeholder=""/>
					</div>
				  </div>
				  
				  <div class="form-group">
				    <div class="col-sm-offset-4 col-sm-3">
				      <button type="submit" class="btn btn-primary" value="添加">添&nbsp;&nbsp;&nbsp;&nbsp;加</button>
				      <button type="reset" class="btn btn-primary" value="取消" onclick="javascript:history.go(-1)">取&nbsp;&nbsp;&nbsp;&nbsp;消</button>
				    </div>
				  </div>
				</form>
	
</body>
</html>
