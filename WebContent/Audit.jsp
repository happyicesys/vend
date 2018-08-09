<%@page import="com.ClsTime"%>
<%@page import="java.sql.Date"%>
<%@page import="com.sun.org.apache.bcel.internal.generic.DADD"%>
<%@page import="com.ado.SqlADO"%>
<%@page import="beans.clsGoodsBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="beans.UserBean"%>

<%@page import="com.tools.ToolBox"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
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

	if(!ub.AccessAble(UserBean.FUNID_CAN_VIEW_STASTIC))
	{
		request.setAttribute("message", "Unable to "+UserBean.RIGHT_DES[UserBean.FUNID_CAN_VIEW_STASTIC]);
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
	}
	
    %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Transaction Staticstic</title>
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
    <!--[if lte IE 8]><script language="javascript" type="text/javascript" src="assets/js/excanvas.min.js"></script><![endif]-->
    <script src="js/bootstrap/jquery-1.12.0.min.js" type="text/javascript"></script>
    <script language="javascript" type="text/javascript" src="./js/jquery.flot.min.js"></script>
    <script type="text/javascript" src="js/bootstrap/datePicker/WdatePicker.js"></script>

 </head>
 <%
	//java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");  
 
 	java.sql.Date d1=ToolBox.filteDate(request.getParameter("edate"));
 	java.sql.Date d2=ToolBox.filteDate(request.getParameter("sdate"));
    
	java.sql.Date endDate;
	java.sql.Date beginDate;
 	if(d1!=null)
 	{
 		endDate=d1;
 	}else
 	{
 		endDate= new java.sql.Date(ClsTime.SystemTime());
 	}
 	
 	if(d2!=null)
 	{
 		beginDate=d2;
 	}else
 	{
 		beginDate= new java.sql.Date(endDate.getTime()-(long)24*60*60*1000*30);
 	}

	long count=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
	int day=(int)count;
	
	boolean tem=false;
	if(day>30)
	{
		beginDate= new java.sql.Date(endDate.getTime()-(long)24*60*60*1000*30);
		tem=true;
	}
	
	day=30;
	if(day<0)
	{
		day=-day;
	}
	int i=0;
	int[] arr_count=new int[day+1];
	String[] arr_xaxis=new String[day+1];
	java.util.Date temdate=(java.util.Date)beginDate.clone();
	StringBuilder sb1=new StringBuilder();
	StringBuilder sb2=new StringBuilder();
	for(i=0;i<=day;i++)
	{
		//arr_count[i]=SqlADO.getAllSalesByDate(temdate);
		sb1.append("["+i+","+String.format("%1.2f",SqlADO.getAllSalesByDate(temdate,ub.getCanAccessSellerid())/100.0)+"]");
		if(i!=day)
		{
			sb1.append(',');
		}
		//arr_xaxis[i]=ToolBox.getDateString();
		sb2.append("["+i+",'"+ToolBox.getD(temdate)+"']");
		if(i!=day)
		{
			sb2.append(',');
		}
		temdate=new Date(temdate.getTime()+(24*60*60*1000));
	}
%>
 
    <body  >

<div class="breadcrumbs" id="breadcrumbs" style="margin-top:5px;">
	<ul class="breadcrumb">
		<li>
			<span class="glyphicon glyphicon-home"></span>
			<a href="MainHome.jsp" target="main" style="padding-left:5px;margin-left:5px;">Home</a>
		</li>

		<li>
			<a href="#">Transaction Management</a>
		</li>
		<li class="active">Transaction Statistic</li>
	</ul>
</div>
	<form role="form">
		<div class="row">
			<div class="form-group col-md-3 col-sm-6 col-xs-12">
				<label class="control-label">From</label>
	  			<input  name="sdate" id="stratTime" size="10" type="text" class="form-control input-sm" value="<%=ToolBox.getYMD(beginDate)%> "  readonly="readonly" onFocus="WdatePicker({readOnly:true})" />
	  		</div>			
			<div class="form-group col-md-3 col-sm-6 col-xs-12">
				<label class="control-label">To</label>
	  			<input  name="edate" id="endTime" size="10" type="text" class="form-control input-sm" value="<%=ToolBox.getYMD(endDate)%> "  readonly="readonly" onFocus="WdatePicker({readOnly:true})" />
	  		</div>

         </div>
		<div class="row">
			<div class="button-group col-md-12 col-sm-12 col-xs-12" style="padding-bottom: 15px;">
				<button type="submit" class="btn btn-default" style="background-color:#f4f4f4;">Search</button>
			</div>																				
		</div>         
	</form>

	
    <div id="placeholder" style="width:92%;height:700px;"></div>
	
	
	

<script language="javascript" type="text/javascript" >
$(function () {
    var d1 =[<%=sb1.toString()%>];
    function plotWithOptions() {
        $.plot($("#placeholder"), [{label:"<%=ToolBox.getYMD(beginDate)%> until <%=ToolBox.getYMD(endDate)%> Sales Graph",data:d1}], {
            series: {
                lines: { show: true,},
                bars: { show: true, barWidth: <%=10.0/day%>,align: "center"}
            },
       		 xaxis: {
            	ticks: [<%=sb2.toString()%>]
        	},
            
        });
    }
    plotWithOptions();
    
    
});
</script>

 </body>
</html>
