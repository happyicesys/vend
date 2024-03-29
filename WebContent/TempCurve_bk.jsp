<%@page import="java.sql.Timestamp"%>
<%@page import="beans.TempBean"%>
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
    <title>24 Hours temp Line Graph</title>
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
		<link href="css/highcharts.css" rel="stylesheet" type="text/css">

     <!--[if lte IE 6]>
	<link rel="stylesheet" type="text/css" href="/css/bootstrap/bootstrap-ie6.css">
	<![endif]-->
	<!--[if lte IE 7]>
	<link rel="stylesheet" type="text/css" href="/css/bootstrap/ie.css">
	<![endif]-->
    <!--[if lte IE 8]><script language="javascript" type="text/javascript" src="assets/js/excanvas.min.js"></script><![endif]-->
    <script src="js/bootstrap/jquery-1.12.0.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="./js/jquery.flot.min.js"></script>
    <script type="text/javascript" src="js/bootstrap/datePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="js/moment.js"></script>
		<script type="text/javascript" src="js/moment-timezone.js"></script>
		<script type="text/javascript" src="js/highcharts.js"></script>
		<script type="text/javascript" src="js/highcharts-debugger.js"></script>
 </head>
 <%
	//java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");

 	Timestamp endDate=ToolBox.filterTime(request.getParameter("edate"));
 	int vid=ToolBox.filterInt(request.getParameter("vid"));

	Timestamp beginDate;
 	if(endDate==null)
 	{
 		endDate= new Timestamp(ClsTime.SystemTime());
 	}

 	beginDate= new Timestamp(endDate.getTime()-(long)48*60*60*1000);

	long count=48;
	int day=(int)count;

	boolean tem=false;

	int i=0;
	int[] arr_count=new int[day+1];
	String[] arr_xaxis=new String[day+1];
	java.util.Date temdate=(java.util.Date)beginDate.clone();
	StringBuilder sb1=new StringBuilder();
	ArrayList<TempBean> lst=TempBean.getLst(vid, beginDate, endDate);
	Timestamp beginDate2= (Timestamp)beginDate.clone();
	beginDate2.setHours(beginDate2.getHours());
	beginDate2.setMinutes(0);
	beginDate2.setNanos(0);
	beginDate2.setSeconds(0);
	long time1;
	double temp1 = 0;


	for(i=0;i<lst.size();i++)
	{
		//arr_count[i]=SqlADO.getAllSalesByDate(temdate);
		if(i % 5 == 0) {
			//time1 = (lst.get(i).getTtime().getTime()-beginDate2.getTime())/7200000.0;
			time1 = lst.get(i).getTtime().getTime();
			temp1 = lst.get(i).getTemp()/10.0;
			sb1.append("["+time1+","+String.format("%1.1f",temp1)+"]");
			if(i!=day)
			{
				sb1.append(',');
			}
		}
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
			<a href="#">Setting Management</a>
		</li>
		<li class="active">Temp Line Graph</li>
	</ul>
</div>

	<a class="btn btn-danger" href="javascript:void(0);" onclick="window.history.back(-1);" >
		<i class="glyphicon glyphicon-menu-left icon-white"></i>
		Back to Vending List
	</a>

<div id="container"></div>

<script language="javascript" type="text/javascript" >
$(document).ready(function() {
	Highcharts.setOptions({
			time: {
					timezone: 'Asia/Singapore'
			}
	});

	Highcharts.chart('container', {

			title: {
					text: 'Last 24 Hours Temperature'
			},

			yAxis: {
					title: {
							text: 'Temp (°C)'
					}
			},
			xAxis: {
				allowDecimals: false,
				type: 'datetime'
			},
			series: [{
					name: 'Temp',
					data: [<%=sb1%>]
			}],
			plotOptions: {
					line: {
							dataLabels: {
									enabled: false
							},
							enableMouseTracking: true
					}
			},
			responsive: {
					rules: [{
							condition: {
									maxWidth: 500
							},
							chartOptions: {
									legend: {
											layout: 'horizontal',
											align: 'center',
											verticalAlign: 'bottom'
									}
							}
					}]
			}
	});
});
</script>

 </body>
</html>
