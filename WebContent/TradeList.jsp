﻿
<%@page import="com.ClsTime"%>
<%@page import="com.clsConst"%>
<%@ page import="beans.TradeBean"%>
<%@ page import="beans.VenderBean"%>
<%@ page import="beans.UserBean"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.ado.SqlADO"%>
<%@ page import="com.tools.ToolBox"%>
<%@ page import="java.sql.Timestamp"%>
<%@ page import="java.sql.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
    request.setCharacterEncoding("UTF-8");
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
	
	
	if(!ub.AccessAble(UserBean.FUNID_CAN_VIEW_TRADE_RECORD))
	{
		request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_VIEW_TRADE_RECORD]);
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
	}
	
	session.setAttribute("currentpage", request.getRequestURI()+"?"+request.getQueryString());
	
    Date StartDate=ToolBox.filteDate(request.getParameter("sdate"));
    Date EndDate=ToolBox.filteDate(request.getParameter("edate"));
	if((StartDate==null) && (EndDate==null))
	{
		StartDate=new Date(ClsTime.SystemTime());
		EndDate=new Date(ClsTime.SystemTime());
	}else if((StartDate==null) && (EndDate!=null))
	{
		//StartDate=ToolBox.addDate(EndDate,0);
	}else if((StartDate!=null) && (EndDate==null))
	{
		//EndDate=ToolBox.addDate(StartDate,1);
	}else
	{
		if (EndDate.before(StartDate))
		{
			Date t=EndDate;
			EndDate=StartDate;
			StartDate=t;
		}
	}
    String orderId=ToolBox.filter(request.getParameter("orderid"));
	String  SellerId =ToolBox.filter(request.getParameter("sellerid"));
	String PortId= ToolBox.filter(request.getParameter("portid"));
	String CardNumber=ToolBox.filter(request.getParameter("CardNumber"));
	int Success=ToolBox.filterInt(request.getParameter("success"));
	int jiesuan=ToolBox.filterInt(request.getParameter("jiesuan"));
	//int PaySuccess=ToolBox.filterInt(request.getParameter("paysuccess"));
	int maxrows=ToolBox.filterInt(request.getParameter("maxrows"));
	
	String str_tradetype =request.getParameter("tradetype");

	int tradetype=clsConst.TRADE_TYPE_NO_LIMIT; 
	if(str_tradetype!=null)
	{
		tradetype=ToolBox.filterInt(str_tradetype);
	}
	int pageindex=ToolBox.filterInt(request.getParameter("pageindex"));
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
    <link rel="stylesheet" href="css/bootstrap/bootstrap-table.css">
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
<!-- <script type="text/javascript" src="./js/jquery.date_input.js"></script> -->
<script type="text/javascript" src="js/bootstrap/datePicker/WdatePicker.js"></script>
<!-- <script type="text/javascript">$($.date_input.initialize);</script> -->
<!--[if  IE ]>
	<style type="text/css">
#portid{
width:216px;
}
</style>
	<![endif]-->
<style type="text/css">
.bootstrap-table .table > thead > tr > th{
background-color: #F5F5F5;
}
</style>
<script type="text/javascript">
	var IntervalId;
	var nowh=10;
	var Max=265;
	var min=20;
	var step=8;
	var fold=true;
	var ConditionBox=function(b)
	{
		if(b)
		{
			if(nowh<=min)
			{
				clearInterval(IntervalId);
				return;
			}
			nowh-=step;
		}else
		{
			if(nowh>=Max)
			{
				clearInterval(IntervalId);
				return;
			}
			nowh+=step;
		}
		var objdiv=document.getElementById("condition");
		objdiv.style.height=nowh+"px";
		//objdiv.style.height="10px";
	}
	var startc=function()
	{
		fold=!fold;
		clearInterval(IntervalId);
		IntervalId=setInterval("ConditionBox("+fold+")",4);
	}
	var clr=function()
	{
		form1.orderid.value="";
		form1.sellerid.value="";
		form1.portid.value="";
		form1.CardNumber.value="";
		form1.success[0].checked=true;
		form1.tradetype[0].checked=true;
	}
	
	var downExcel=function()
	{
		form1.method="get";
		form1.action="./GetExcel";
		form1.submit();
	}
	
	var getpost=function()
	{
		form1.method="post";
		form1.action="TradeList.jsp";
		form1.submit();
	}
	$(function () {

	    //1.初始化Table
	    var oTable = new TableInit();
	    oTable.Init();

	    //2.初始化Button的点击事件
	    /* var oButtonInit = new ButtonInit();
	    oButtonInit.Init(); */

	});


	var TableInit = function () {
	    var oTableInit = new Object();
	    //初始化Table
	    oTableInit.Init = function () {
	        $('#tradeList').bootstrapTable({
	            url: 'TradeList',         //请求后台的URL（*）
	            method: 'post',                      //请求方式（*）
	            toolbar: '#toolbar',                //工具按钮用哪个容器
	            striped: true,                      //是否显示行间隔色
	            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
	            pagination: true,                   //是否显示分页（*）
	            sortable: false,                     //是否启用排序
	            sortOrder: "asc",                   //排序方式
	            queryParams: oTableInit.queryParams,//传递参数（*）
	            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
	            pageNumber:1,                       //初始化加载第一页，默认第一页
	            pageSize: 50,                       //每页的记录行数（*）
	            pageList: [50,100,500],        //可供选择的每页的行数（*）
	            strictSearch: true,
	            clickToSelect: true,                //是否启用点击选中行
	            height: 700,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
	            uniqueId: "id",                     //每一行的唯一标识，一般为主键列
	            cardView: false,                    //是否显示详细视图
	            detailView: true,                   //是否显示父子表
	            detailFormatter:detailFormatter,
	            columns: [{
	                field: 'id',
	                title: '序号'
	            }, {
	                field: 'liushuiid',
	                title: '交易编号'
	            }, {
	                field: 'orderid',
	                title: '订单号'
	            }, {
	                field: 'receivetime',
	                title: '交易时间'
	            }, {
	                field: 'price',
	                title: '金额'
	            }, {
	                field: 'coin_credit',
	                title: '投入硬币'
	            },  {
	                field: 'bill_credit',
	                title: '投入纸币'
	            },  /*{
	                field: 'changes',
	                title: '找零'
	            },*/ {
	                field: 'tradetype',
	                title: '交易类型'
	            },{
	                field: 'goodmachineid',
	                title: '货机号'
	            },{
	                field: 'inneridname',
	                title: '货道号'
	            },{
	                field: 'goodsName',
	                title: '商品名称'
	            }, {
	                field: 'changestatus',
	                title: '支付'
	            },{
	                field: 'sendstatus',
	                title: '出货'
	            },
	            {
	                field: 'opbut',
	                title: '操作'
	            }
	            ]
	        });
	    };

	    //得到查询的参数
	  oTableInit.queryParams = function (params) {
	        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
	            limit: params.limit,   //页面大小
	            offset: params.offset,  //页码
	            sdate: $("#stratTime").val(),
	            edate: $("#endTime").val(),
	            sellerid: $("#sellerid").val(),
	            orderid: $("#orderid").val(),
	            CardNumber: $("#CardNumber").val(),
	            maxrows: params.limit,
	            pageindex:params.pageNumber,
	            portid: $("#portid").val(),
	            CardNumber: $("#CardNumber").val(),
	            tradetype:$('input:radio[name="tradetype"]:checked').val(),
	            success:$('input:radio[name="success"]:checked').val(),
	            jiesuan:$('input:radio[name="jiesuan"]:checked').val()
	        };
	        return temp;
	    };
	    return oTableInit;
	};
		
    function detailFormatter(index, row) {
    	var html;
    	if(row.tradetypeid==<%=clsConst.TRADE_TYPE_AL_QR %>)
    	{
    		html="支付宝用户名:"+row.cardinfo +"<br/>支付宝订单号:"+row.tradeid;
    	}
    	else if(row.tradetypeid==<%=clsConst.TRADE_TYPE_WX_QR %>)
    	{
    		html="微信识别号:"+row.cardinfo +"<br/>微信订单号:"+row.tradeid;
    	}
    	else if(row.tradetypeid==<%=clsConst.TRADE_TYPE_CARD%>)
    	{
    		html="IC卡号:"+row.cardinfo;
    	}
    	else if(row.tradetypeid==<%=clsConst.TRADE_TYPE_BANK%>)
    	{
    		html="银行卡号:"+row.cardinfo ;
    	}
    	else
    	{
    		html="无其他附加信息！";
    	}
        return html;
    }
	
</script>
<title><%=ToolBox.WEB_NAME%></title>
</head>
<body style="background-color: #fff;">
	<div class="breadcrumbs" id="breadcrumbs" style="margin-top:5px;">
		<ul class="breadcrumb">
			<li>
				<span class="glyphicon glyphicon-home"></span>
					<a href="MainHome.jsp" target="main" style="padding-left:5px;margin-left:5px;">首页</a>
			</li>
			<li>
				<a href="#">交易管理</a>
			</li>
			<li class="active">交易查询</li>
		</ul>
	</div>
	<div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
			  <div class="row">
			  	<form class="form-horizontal" role="form" action="TradeList.jsp" name="form1" method="post">
					<div class="col-xs-12">
						<div class="dataTables_length" id="dataTables-example_length">
							<label>时　　间：</label>
							<label><input  name="sdate" id="stratTime" size="9" class="form-control input-sm" type="text" value="<%=StartDate%>"  onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'endTime\',{M:3});}'})" /></label>&nbsp;-&nbsp;<label><input  name="edate" id="endTime" size="10" class="form-control input-sm"  type="text" value="<%=EndDate%>"  onFocus="WdatePicker({minDate:'#F{$dp.$D(\'stratTime\',{d:0});}'})" />
							</label>
							<label>订单编号：</label>
							  <label><input type="text" size="18" class="form-control input-sm" name="orderid" id="orderid" value="<%=orderId%>" placeholder="" aria-controls="dataTables-example"></label>
							<label>&nbsp;卡号/微信识别号/支付账号：</label>
							  <label><input type="text" size="18" class="form-control input-sm" name="CardNumber" id="CardNumber" value="<%=CardNumber%>" placeholder="" aria-controls="dataTables-example"></label>
							<label>&nbsp;&nbsp;售货机号：</label>
							  <label><input size="18" type="text" class="form-control input-sm" name="sellerid" id="sellerid" value="<%=SellerId%>" placeholder="" aria-controls="dataTables-example">可以用英文“,”隔开多个机器编号，以便同时查询多台机器的交易</label>
						</div>
					</div>
					<div class="col-xs-12"> 
						<div class="dataTables_length" id="dataTables-example_length">
							<label>货道编号：</label>
							  <label><input type="text" size="26" class="form-control input-sm" name="portid" id="portid" value="<%=PortId%>" placeholder="" aria-controls="dataTables-example"></label>
							<label>交易方式：</label>
							<label class="radio-inline" style="padding-top:0px;">
  								<input value="<%=clsConst.TRADE_TYPE_NO_LIMIT%>" <%=((tradetype==clsConst.TRADE_TYPE_NO_LIMIT)?"checked=\"checked\"":"")%> type="radio" name="tradetype"> 不限
							</label>
							<label class="radio-inline"  style="padding-top:0px;">
							  <input value="<%=clsConst.TRADE_TYPE_CASH%>" <%=((tradetype==clsConst.TRADE_TYPE_CASH)?"checked=\"checked\"":"")%> type="radio" name="tradetype"> 现金
							</label>
							<label class="radio-inline"  style="padding-top:0px;">
							  <input value="<%=clsConst.TRADE_TYPE_CARD%>" <%=((tradetype==clsConst.TRADE_TYPE_CARD)?"checked=\"checked\"":"")%> type="radio" name="tradetype"> 刷卡
							</label>
							<label class="radio-inline"  style="padding-top:0px;">
							  <input value="<%=clsConst.TRADE_TYPE_WX_QR%>" <%=((tradetype==clsConst.TRADE_TYPE_WX_QR)?"checked=\"checked\"":"")%> type="radio" name="tradetype"> 微信
							</label>
							<label class="radio-inline"  style="padding-top:0px;">
							  <input value="<%=clsConst.TRADE_TYPE_AL_QR%>" <%=((tradetype==clsConst.TRADE_TYPE_AL_QR)?"checked=\"checked\"":"")%> type="radio" name="tradetype"> 支付宝
							</label>
							<label class="radio-inline"  style="padding-top:0px;">
							  <input value="<%=clsConst.TRADE_TYPE_BANK%>" <%=((tradetype==clsConst.TRADE_TYPE_BANK)?"checked=\"checked\"":"")%> type="radio" name="tradetype">银行卡
							</label>
							<label>&nbsp;是否成功：</label>
							<label class="radio-inline" style="padding-top:0px;">
  								<input value="0" <%=((Success==0)?"checked=\"checked\"":"") %> type="radio" name="success"> 不限
							</label>
							<label class="radio-inline" style="padding-top:0px;">
							  <input value="1" <%=((Success==1)?"checked=\"checked\"":"") %> type="radio" name="success"> 成功
							</label>
							<label class="radio-inline" style="padding-top:0px;">
							  <input value="2" <%=((Success==2)?"checked=\"checked\"":"") %> type="radio" name="success"> 失败
							</label>
							
							<label>&nbsp;是否结算：</label>
							<label class="radio-inline" style="padding-top:0px;">
  								<input value="0" <%=((jiesuan==0)?"checked=\"checked\"":"") %> type="radio" name="jiesuan"> 不限
							</label>
							<label class="radio-inline" style="padding-top:0px;">
							  <input value="1" <%=((jiesuan==1)?"checked=\"checked\"":"") %> type="radio" name="jiesuan"> 已结算
							</label>
							<label class="radio-inline" style="padding-top:0px;">
							  <input value="2" <%=((jiesuan==2)?"checked=\"checked\"":"") %> type="radio" name="jiesuan"> 未结算
							</label>
							&nbsp;
							<input type="button" class="btn btn-default" style="background-color:#f4f4f4;" onclick="getpost();" value="提交"></input>
							<input type="button" class="btn btn-default" style="background-color:#f4f4f4;" onclick="clr();" value="清除"></input>
							<input type="button" class="btn btn-default" style="background-color:#f4f4f4;" onclick="downExcel();" value="下载EXCEL"></input>
						</div>
					</div>
				</form>				
			  </div>
	</div>
	<div class="row" style="overflow-y:auto;">
            <div class="col-xs-12">

              <div class="box">
                <div class="box-body">
					<div id="DataTables_Table_0_wrapper" class="dataTables_wrapper" role="grid">
						<div class="table-responsive">
							<table id="tradeList">
							</table>
							</div>
					</div>
                </div>
              </div>
            </div>
		  </div>
	<script src="js/bootstrap/bootstrap.min.js"></script>
	<script src="js/bootstrap/bootstrap-table.js"></script>
	<script src="js/bootstrap/bootstrap-table-zh-CN.js"></script>
    <!-- Metis Menu Plugin JavaScript -->
    <script src="js/bootstrap/metisMenu.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="js/bootstrap/admin.js"></script>
</body>
</html>