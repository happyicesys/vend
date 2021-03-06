
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
	
	
	if(!ub.AccessAble(UserBean.FUNID_CAN_CHARGE_FOR_CUSTOMER))
	{
		request.setAttribute("message", "Unable to "+UserBean.RIGHT_DES[UserBean.FUNID_CAN_VIEW_TRADE_RECORD]);
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
    String tradeid=ToolBox.filter(request.getParameter("tradeid"));
	String cardinfo=ToolBox.filter(request.getParameter("cardinfo"));
	String id_string=ToolBox.filter(request.getParameter("id_string"));
	//int PaySuccess=ToolBox.filterInt(request.getParameter("paysuccess"));
	int maxrows=ToolBox.filterInt(request.getParameter("maxrows"));
	
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
		form1.tradeid.value="";
		form1.cardinfo.value="";
		form1.id_string.value="";
	}
	
	var downExcel=function()
	{
		form1.method="get";
		form1.action="./GetChargeListExcel";
		form1.submit();
	}
	
	var getpost=function()
	{
		form1.method="post";
		form1.action="chargedatalst.jsp";
		form1.submit();
	}
	$(function () {

	    //1.?????????Table
	    var oTable = new TableInit();
	    oTable.Init();

	    //2.?????????Button???????????????
	    /* var oButtonInit = new ButtonInit();
	    oButtonInit.Init(); */

	});


	var TableInit = function () {
	    var oTableInit = new Object();
	    //?????????Table
	    oTableInit.Init = function () {
	        $('#tradeList').bootstrapTable({
	            url: 'ChargeList',         //???????????????URL???*???
	            method: 'post',                      //???????????????*???
	            toolbar: '#toolbar',                //???????????????????????????
	            striped: true,                      //????????????????????????
	            cache: false,                       //??????????????????????????????true?????????????????????????????????????????????????????????*???
	            pagination: true,                   //?????????????????????*???
	            sortable: false,                     //??????????????????
	            sortOrder: "asc",                   //????????????
	            queryParams: oTableInit.queryParams,//??????Parameters???*???
	            sidePagination: "server",           //???????????????client??????????????????server??????????????????*???
	            pageNumber:1,                       //??????????????????????????????????????????
	            pageSize: 50,                       //????????????????????????*???
	            pageList: [50,100,500],        //?????????????????????????????????*???
	            strictSearch: true,
	            clickToSelect: true,                //???????????????????????????
	            height: 700,                        //???????????????????????????height?????????????????????????????????????????????????????????
	            uniqueId: "id",                     //?????????????????????????????????????????????
	            cardView: false,                    //????????????????????????
	            detailView: false,                   //?????????????????????
	            /*detailFormatter:detailFormatter,*/
	            columns: [{
	                field: 'id',
	                title: 'ID'
	            }, {
	                field: 'tradeid',
	                title: 'Transaction ID'
	            },{
	                field: 'adminname',
	                title: 'Admin Name'
	            }, {
	                field: 'customername',
	                title: 'Customer Name'
	            },{
	                field: 'id_string',
	                title: 'Unique ID'
	            },  {
	                field: 'cardinfo',
	                title: 'Credit Card No'
	            }, {
	                field: 'gmt',
	                title: 'Topup Time'
	            }, {
	                field: 'amount',
	                title: 'Topup Amount'
	            },  {
	                field: 'amount_after_charge',
	                title: 'Balance'
	            }
	            ]
	        });
	    };

	    //???????????????Parameters
	  oTableInit.queryParams = function (params) {
	        var temp = {   //????????????????????????????????????????????????????????????????????????????????????????????????????????????
	            limit: params.limit,   //????????????
	            offset: params.offset,  //??????
	            sdate: $("#stratTime").val(),
	            edate: $("#endTime").val(),
	            cardinfo: $("#cardinfo").val(),
	            tradeid: $("#tradeid").val(),
	            id_string:$("#id_string").val(),
	            maxrows: params.limit,
	            pageindex:params.pageNumber
	        };
	        return temp;
	    };
	    return oTableInit;
	};
		
    function detailFormatter(index, row) {
    	var html;
    	if(row.tradetypeid==<%=clsConst.TRADE_TYPE_AL_QR %>)
    	{
    		html="Alipay Username:"+row.cardinfo +"<br/>Alipay Transaction ID:"+row.tradeid;
    	}
    	else if(row.tradetypeid==<%=clsConst.TRADE_TYPE_WX_QR %>)
    	{
    		html="Wechat User ID:"+row.cardinfo +"<br/>Wechat Transaction ID:"+row.tradeid;
    	}
    	else if(row.tradetypeid==<%=clsConst.TRADE_TYPE_CARD%>)
    	{
    		html="IC??????:"+row.cardinfo;
    	}
    	else if(row.tradetypeid==<%=clsConst.TRADE_TYPE_BANK%>)
    	{
    		html="Bankcard Number:"+row.cardinfo ;
    	}
    	else
    	{
    		html="No other message";
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
					<a href="MainHome.jsp" target="main" style="padding-left:5px;margin-left:5px;">Home</a>
			</li>
			<li>
				<a href="#">Transaction Management</a>
			</li>
			<li class="active">Transaction Inquiry</li>
		</ul>
	</div>
	<div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
			  <div class="row">
			  	<form class="form-horizontal" role="form" action="TradeList.jsp" name="form1" method="post">
					<div class="col-xs-12">
						<div class="dataTables_length" id="dataTables-example_length">
							<label>Time</label>
							<label><input  name="sdate" id="stratTime" size="9" class="form-control input-sm" type="text" value="<%=StartDate%>"  onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'endTime\',{M:3});}'})" /></label>&nbsp;-&nbsp;<label><input  name="edate" id="endTime" size="10" class="form-control input-sm"  type="text" value="<%=EndDate%>"  onFocus="WdatePicker({minDate:'#F{$dp.$D(\'stratTime\',{d:0});}'})" />
							</label>
							<label>Transaction ID</label>
							  <label><input type="text" size="18" class="form-control input-sm" name="tradeid" id="tradeid" value="<%=tradeid%>"  aria-controls="dataTables-example"></label>
							<label>&nbsp;Card ID</label>
							  <label><input type="text" size="18" class="form-control input-sm" name="cardinfo" id="cardinfo" value="<%=cardinfo%>"  aria-controls="dataTables-example"></label>
							  <label>&nbsp;Unique ID:</label>
							  <label><input type="text" size="18" class="form-control input-sm" name="id_string" id="id_string" value="<%=id_string%>"  aria-controls="dataTables-example"></label>
							<input type="button" class="btn btn-default" style="background-color:#f4f4f4;" onclick="getpost();" value="??????"></input>
							<input type="button" class="btn btn-default" style="background-color:#f4f4f4;" onclick="clr();" value="??????"></input>
							<input type="button" class="btn btn-default" style="background-color:#f4f4f4;" onclick="downExcel();" value="??????EXCEL"></input>
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