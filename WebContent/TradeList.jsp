
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
	
	
	if(!ub.AccessAble(UserBean.FUNID_CAN_VIEW_TRADE_RECORD))
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
    <link href="css/select2.css" rel="stylesheet" />
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
<script type="text/javascript" src="js/moment.js"></script>
<script type="text/javascript" src="js/select2.js"></script>
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
	                title: 'ID'
	            }, {
	                field: 'liushuiid',
	                title: 'Transaction ID'
	            }, {
	                field: 'orderid',
	                title: 'Order ID'
	            }, {
	                field: 'receivetime',
	                title: 'Transaction time'
	            }, {
	                field: 'price',
	                title: 'Amount'
	            }, /*{
	                field: 'coin_credit',
	                title: 'Insert Coin'
	            },  {
	                field: 'bill_credit',
	                title: 'Insert Bill'
	            },  {
	                field: 'changes',
	                title: '找零'
	            },*/ {
	                field: 'tradetype',
	                title: 'Transaction Type'
	            },{
	                field: 'goodmachineid',
	                title: 'Vending ID'
	            },{
	                field: 'inneridname',
	                title: 'Product ID'
	            },{
	                field: 'goodsName',
	                title: 'Product Name'
	            }, {
	                field: 'changestatus',
	                title: 'Payment'
	            },{
	                field: 'sendstatus',
	                title: 'Dispense'
	            }
	            /*
	            {
	                field: 'opbut',
	                title: 'Operation'
	            }*/
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
	            tradetype:$('#tradetype option:selected').val(),
	            success:$('#success option:selected').val(),
	            jiesuan:$('#jiesuan option:selected').val()
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
    		html="IC卡号:"+row.cardinfo;
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
			  
			<form role="form" action="TradeList.jsp" name="form1" method="post">
				<div class="row">
					<div class="form-group col-md-4 col-sm-4 col-xs-12">
						<label class="control-label">Date From</label>
						<input name="sdate" id="stratTime" size="10" type="text" class="form-control input-sm" value="<%=StartDate%> " onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'endTime\',{M:3});}'})" />
					</div>
					<div class="form-group col-md-4 col-sm-4 col-xs-12">
						<label class="control-label">Date To</label>
						<input  name="edate" id="endTime" size="10" class="form-control input-sm"  type="text" value="<%=EndDate%>"  onFocus="WdatePicker({minDate:'#F{$dp.$D(\'stratTime\',{d:0});}'})" />
					</div>	
                    <div class="form-group col-md-4 col-sm-4 col-xs-12">
                        <div class="row col-md-12 col-sm-12 col-xs-12">
                            <label class="control-label">Date Shortcut</label>
                        </div>
                        <div class="btn-group">
                            <a href="" id="prevDateBtn" onclick="event.preventDefault();" class="btn btn-default"><i class="fa fa-backward"></i></a>
                            <a href="" id="todayDateBtn" onclick="event.preventDefault();" class="btn btn-default"><i class="fa fa-circle"></i></a>
                            <a href="" id="nextDateBtn" onclick="event.preventDefault();" class="btn btn-default"><i class="fa fa-forward"></i></a>
                        </div>
                    </div>
                </div>	
			  	<div class="row">	
					<div class="form-group col-md-3 col-sm-6 col-xs-12">
						<label class="control-label">Transaction Method</label>
						<select class="select form-control" name="tradetype" id="tradetype">
							<option <%=((tradetype==clsConst.TRADE_TYPE_NO_LIMIT)?"selected=\"selected\"":"")%> value="<%=clsConst.TRADE_TYPE_NO_LIMIT%>">All</option>
							<option <%=((tradetype==clsConst.TRADE_TYPE_CASH)?"selected=\"selected\"":"")%> value="<%=clsConst.TRADE_TYPE_CASH%>">Cash</option>
							<option <%=((tradetype==clsConst.TRADE_TYPE_CARD)?"selected=\"selected\"":"")%> value="<%=clsConst.TRADE_TYPE_CARD%>">Card</option>
							<option <%=((tradetype==clsConst.TRADE_TYPE_WX_QR)?"selected=\"selected\"":"")%> value="<%=clsConst.TRADE_TYPE_WX_QR%>">Wechat</option>
							<option <%=((tradetype==clsConst.TRADE_TYPE_AL_QR)?"selected=\"selected\"":"")%> value="<%=clsConst.TRADE_TYPE_AL_QR%>">Alipay</option>
							<option <%=((tradetype==clsConst.TRADE_TYPE_BANK)?"selected=\"selected\"":"")%> value="<%=clsConst.TRADE_TYPE_BANK%>">Bank Card</option>
							<option <%=((tradetype==clsConst.TRADE_TYPE_COCO)?"selected=\"selected\"":"")%> value="<%=clsConst.TRADE_TYPE_COCO%>">Free Vend</option>
						</select>
		  			</div> 		
					<div class="form-group col-md-3 col-sm-6 col-xs-12">
						<label class="control-label">Payment Status</label>
						<select class="select form-control" name="success" id="success">
							<option <%=((Success==0)?"selected=\"selected\"":"") %> value="0">All</option>
							<option <%=((Success==1)?"selected=\"selected\"":"") %> value="1">Succeed</option>
							<option <%=((Success==2)?"selected=\"selected\"":"") %> value="2">Failure</option>
						</select>
		  			</div> 	
					<div class="form-group col-md-3 col-sm-6 col-xs-12">
						<label class="control-label">Is Settled (结算)</label>
						<select class="select form-control" name="jiesuan" id="jiesuan">
							<option <%=((jiesuan==0)?"selected=\"selected\"":"") %> value="0">All</option>
							<option <%=((jiesuan==1)?"selected=\"selected\"":"") %> value="1">Already</option>
							<option <%=((jiesuan==2)?"selected=\"selected\"":"") %> value="2">Havent yet</option>
						</select>
		  			</div>		  					  					  			  		
				</div>                
                <div class="row">
					<div class="form-group col-md-3 col-sm-6 col-xs-12">
						<label class="control-label">Transaction ID</label>
			  			<input type="text" id="orderid" name="orderid" value="<%=orderId %>" class="form-control input-sm" placeholder="" aria-controls="dataTables-example">
			  		</div>                    
					<div class="form-group col-md-3 col-sm-6 col-xs-12">
						<label class="control-label">Card No/ Wechat UID/ Payment Acc</label>
			  			<input type="text" id="CardNumber" name="CardNumber" value="<%=CardNumber %>" class="form-control input-sm" placeholder="" aria-controls="dataTables-example">
			  		</div> 
					<div class="form-group col-md-3 col-sm-6 col-xs-12">
						<label class="control-label">Vending Machine ID</label>
			  			<input type="text" id="sellerid" name="sellerid" value="<%=SellerId %>" class="form-control input-sm" placeholder="" aria-controls="dataTables-example">
			  			<small>*Add "," in between to search multiple machines</small>
			  		</div> 
					<div class="form-group col-md-3 col-sm-6 col-xs-12">
						<label class="control-label">Product ID</label>
			  			<input type="text" id="portid" name="portid" value="<%=PortId %>" class="form-control input-sm" placeholder="" aria-controls="dataTables-example">
			  		</div> 	
			  	</div>

				<div class="row">
					<div class="button-group col-md-12 col-sm-12 col-xs-12" style="padding-bottom: 15px;">
						<button class="btn btn-default" style="background-color:#f4f4f4;" onclick="getpost();">Search</button>
						<button class="btn btn-default" style="background-color:#f4f4f4;" onclick="clr();">Clear</button>
						<button class="btn btn-default" style="background-color:#f4f4f4;" onclick="downExcel();">Export Excel</button>
					</div>																				
				</div>	
				<!--  			
					<div class="col-xs-12"> 
						<div class="dataTables_length" id="dataTables-example_length">
							<label>Transaction Method:</label>
							<label class="radio-inline" style="padding-top:0px;">
  								<input value="<%=clsConst.TRADE_TYPE_NO_LIMIT%>" <%=((tradetype==clsConst.TRADE_TYPE_NO_LIMIT)?"checked=\"checked\"":"")%> type="radio" name="tradetype"> 不限
							</label>
							<label class="radio-inline"  style="padding-top:0px;">
							  <input value="<%=clsConst.TRADE_TYPE_CASH%>" <%=((tradetype==clsConst.TRADE_TYPE_CASH)?"checked=\"checked\"":"")%> type="radio" name="tradetype"> Cash
							</label>
							<label class="radio-inline"  style="padding-top:0px;">
							  <input value="<%=clsConst.TRADE_TYPE_CARD%>" <%=((tradetype==clsConst.TRADE_TYPE_CARD)?"checked=\"checked\"":"")%> type="radio" name="tradetype"> Card
							</label>
							<label class="radio-inline"  style="padding-top:0px;">
							  <input value="<%=clsConst.TRADE_TYPE_WX_QR%>" <%=((tradetype==clsConst.TRADE_TYPE_WX_QR)?"checked=\"checked\"":"")%> type="radio" name="tradetype"> 微信
							</label>
							<label class="radio-inline"  style="padding-top:0px;">
							  <input value="<%=clsConst.TRADE_TYPE_AL_QR%>" <%=((tradetype==clsConst.TRADE_TYPE_AL_QR)?"checked=\"checked\"":"")%> type="radio" name="tradetype"> Alipay
							</label>
							<label class="radio-inline"  style="padding-top:0px;">
							  <input value="<%=clsConst.TRADE_TYPE_BANK%>" <%=((tradetype==clsConst.TRADE_TYPE_BANK)?"checked=\"checked\"":"")%> type="radio" name="tradetype">银行卡
							</label>
							<label class="radio-inline"  style="padding-top:0px;">
							  <input value="<%=clsConst.TRADE_TYPE_COCO%>" <%=((tradetype==clsConst.TRADE_TYPE_COCO)?"checked=\"checked\"":"")%> type="radio" name="tradetype">FreeVend
							</label>
							<label>&nbsp;Is Successful:</label>
							<label class="radio-inline" style="padding-top:0px;">
  								<input value="0" <%=((Success==0)?"checked=\"checked\"":"") %> type="radio" name="success"> 不限
							</label>
							<label class="radio-inline" style="padding-top:0px;">
							  <input value="1" <%=((Success==1)?"checked=\"checked\"":"") %> type="radio" name="success"> 成功
							</label>
							<label class="radio-inline" style="padding-top:0px;">
							  <input value="2" <%=((Success==2)?"checked=\"checked\"":"") %> type="radio" name="success"> 失败
							</label>
							
							<label>&nbsp;Is calculated:</label>
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
				-->
			</form>				
				

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
    <!-- Metis Menu Plugin JavaScript -->
    <script src="js/bootstrap/metisMenu.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="js/bootstrap/admin.js"></script>
    <script type="text/javascript">
		$('#prevDateBtn').click(function(event) {
			event.preventDefault();
			var oriStartDate = moment($('#stratTime').val(), "YYYY-MM-DD");
			var newStartDate = oriStartDate.subtract(1, "days").format("YYYY-MM-DD");
			var oriEndDate = moment($('#endTime').val(), "YYYY-MM-DD");
			var newEndDate = oriEndDate.subtract(1, "days").format("YYYY-MM-DD");		
			$('#stratTime').val(newStartDate);
			$('#endTime').val(newEndDate);
		});
		
		$('#todayDateBtn').click(function(event) {
			event.preventDefault();
			var newStartDate = moment().format("YYYY-MM-DD");
			var newEndDate = moment().format("YYYY-MM-DD");
			$('#stratTime').val(newStartDate);	
			$('#endTime').val(newEndDate);	
		});	
		
		$('#nextDateBtn').click(function(event) {
			event.preventDefault();
			var oriStartDate = moment($('#stratTime').val(), "YYYY-MM-DD");
			var newStartDate = oriStartDate.add(1, "days").format("YYYY-MM-DD");
			var oriEndDate = moment($('#endTime').val(), "YYYY-MM-DD");
			var newEndDate = oriEndDate.add(1, "days").format("YYYY-MM-DD");		
			$('#stratTime').val(newStartDate);
			$('#endTime').val(newEndDate);
		});	   
		$('.select').select2({
			placeholder: 'Select..'
		});
    </script>
</body>
</html>