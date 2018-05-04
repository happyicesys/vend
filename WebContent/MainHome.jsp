<%@page import="com.clsConst"%>
<%@page import="java.util.Calendar"%>
<%@page import="beans.ClsSaleStatisticData"%>
<%@page import="java.sql.Date"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.ListIterator"%>
<%@ page import="beans.VenderBean"%>
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
		
		if(!ub.AccessAble(UserBean.FUNID_CAN_VIEW_VENDER))
		{
			request.setAttribute("message", "Unable to "+UserBean.RIGHT_DES[UserBean.FUNID_CAN_VIEW_VENDER]);
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}
		
	 	java.sql.Date beginDate=ToolBox.filteDate(request.getParameter("sdate"));
	    
	 	if(beginDate==null)
	 	{
	 		beginDate=new Date(System.currentTimeMillis());
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
    <script language="javascript" type="text/javascript" src="./jquery_ui/js/jquery-ui.min.js"></script>
    <script language="javascript" type="text/javascript" src="./jquery_ui/js/jquery.showLoading.min.js"></script>
    <script type="text/javascript" src="js/bootstrap/datePicker/WdatePicker.js"></script>

   	<style>
    #myModal
    {
        top:200px;
    }
</style>
    <script type="text/javascript">
    (function ($) {
		  $(document).ready(function() {
		    if ($.isFunction($.bootstrapIE6)) $.bootstrapIE6($(document));
		  });
		})(jQuery);
    
$(function () {
    $("#select_all").click(function(){
    	var idLists= $("input[name='vendid']");
        for(var i=0;i<idLists.length;i++){
            idLists[i].checked=true;
        }
    	//alert("select_all");
    });
    
    $("#fanxuan").click(function(){
        var idLists= $("input[name='vendid']");
        for(var i=0;i<idLists.length;i++){
            idLists[i].checked=!idLists[i].checked;
        }
    });
    
  //取消选择
    $("#deselect_all").click(function(){  
        $("input[name='vendid']").removeAttr("checked"); 
    });
    
  /*提示窗口*/
    $("#alertdlg").dialog({autoOpen: false,width:300,modal: true,
        buttons: {
            "确定": function() {
                $( this ).dialog( "close" );
              }
        }
    });
});

var showAlert=function(title,content)
{
    $( "#alertcontent" ).html(title);
    $( "#myModalLabel" ).html(content);
    $('#myModal').modal('show');
}

var _submit=function(date)
{
	/*提交Binding数据到后台*/
	var bindobj=new Object();
	bindobj.sdate=date;
	$("body").showLoading(); 
    $.ajax({
        type: "POST",
        url: "./HomePageSaleStatistic",
        data:bindobj,
        success: (function(obj){
            $("body").hideLoading();  
            if(obj.msg==1)
            {
            	alert(obj.detail);
            }
            else
            {
            	$("#_title").text("["+date+"] Sales Staticstics");
            	$("#all_count").text(obj.all_count);
            	$("#all_credit").text(obj.all_credit);
            	$("#cash_count").text(obj.cash_count);
            	$("#cash_credit").text(obj.cash_credit);
            	$("#wx_count").text(obj.wx_count);
            	$("#wx_credit").text(obj.wx_credit);
            	$('#freevend_count').text(obj.freevend_count);
            	$('#freevend_credit').text(obj.freevend_credit);
            
            	$("#al_count").text(obj.al_count);
            	$("#al_credit").text(obj.al_credit);
            	$("#card_count").text(obj.card_count);
            	$("#card_credit").text(obj.card_credit);
            	$("#bank_count").text(obj.bank_count);
            	$("#bank_credit").text(obj.bank_credit);
            }
            
            }),
        dataType: "json",
        error:(function(obj,txtmes,txtmes2){
            $("body").hideLoading();  
            alert(obj.status+" Error");
            })
    		
      });
}
</script>


</head>
<%
	session.setAttribute("currentpage", request.getRequestURI()+"?"+request.getQueryString());
	int RsCount=0;
	int pagecount=ub.getPagecount();
	int Page=ToolBox.filterInt(request.getParameter("page"));
	int jiesuan=ToolBox.filterInt(request.getParameter("jiesuan"));
	if(Page==0) 
	{
		Page=1;
	}
	String id = ToolBox.filter(request.getParameter("id"));
	

	
%>
<body style="background-color: #fff;">
	<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">×
            </button>
            <h4 class="modal-title" id="myModalLabel">
            
            </h4>
         </div>
         <div class="modal-body" id="alertcontent">
            
         </div>
         <div class="modal-footer">
            <button type="button" class="btn btn-default" 
               data-dismiss="modal">Confirm
            </button>
         </div>
      </div><!-- /.modal-content -->
   </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
          		<div class="breadcrumbs" id="breadcrumbs" style="margin-top:5px;">
						<ul class="breadcrumb">
							<li>
								<span class="glyphicon glyphicon-home"></span>
								<a href="#" target="main" style="padding-left:5px;margin-left:5px;">Home</a>
							</li>
						</ul>
					</div>
			<div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
			  <div class="row">
					<div class="col-xs-12">
						<div class="dataTables_length" id="dataTables-example_length">
							<form class="form-horizontal" role="form">
							<label>Choose Date</label>
							<%
							int i=0;
							Calendar c= Calendar.getInstance();
							for(i=0;i<10;i++)
							{
								
							%>

							&nbsp;<label><input type="button" onclick="_submit(this.value)" class='btn btn-success btn-sm' value="<%=ToolBox.getShortStdDateString(c.getTime())%>"/></label>
							<%
							c.add(Calendar.DAY_OF_MONTH, -1);
							} %>
							

							
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
						<div class="table-responsive">
						<br/><br/><br/>
							<table  class="table table-bordered table-hover table-condensed" style="overflow-y:auto; width:100%;height:100px;border-spacing: 0px;">
								<thead>
									<tr>
										<th id="_title" colspan="12" style="text-align: center;font-size: 2em">[<%=beginDate.toString() %>]销售统计</th>
									</tr>
								</thead>
								<thead>
									<tr  role="row" style="background-color: #f5f5f5; height:2em;">
										<th class="col-md-1 text-center">Sales Count</th>
										<th class="col-md-1 text-center">Total</th>
										<th class="col-md-1 text-center">Cash Sales</th>
										<th class="col-md-1 text-center">Cash Amount</th>
										<th class="col-md-1 text-center">Freevend #</th>
										<th class="col-md-1 text-center">Freevend Amt</th>										
										<!--  
										<th>Wechat Sales</th>
										<th>Wechat Pay</th>
										<th>Alipay Sales</th>
										<th>Alipay</th>
										<th>Card Sales</th>
										<th>Card</th>
										<th>China Union Pay Sales</th>
										<th>China Union Pay</th>
										-->
									</tr>											
								</thead>

								<tbody role="alert" aria-live="polite" aria-relevant="all">
									<%
									 	int count=0;
									
											Date edata=null;

											//beginDate =new Date(c.getTimeInMillis());
											c.setTimeInMillis(beginDate.getTime());
											c.add(Calendar.DAY_OF_MONTH, 1);
											edata=new Date(c.getTimeInMillis());
											ClsSaleStatisticData salestatistic_all= SqlADO.getSalesStatisticDataFromDb(
													beginDate,edata,ub.getVenderLimite(),clsConst.TRADE_TYPE_NO_LIMIT,jiesuan);
											ClsSaleStatisticData salestatistic_al= SqlADO.getSalesStatisticDataFromDb(
													beginDate,edata,ub.getVenderLimite(),clsConst.TRADE_TYPE_AL_QR,jiesuan);
											ClsSaleStatisticData salestatistic_wx= SqlADO.getSalesStatisticDataFromDb(
													beginDate,edata,ub.getVenderLimite(),clsConst.TRADE_TYPE_WX_QR,jiesuan);
											ClsSaleStatisticData salestatistic_card= SqlADO.getSalesStatisticDataFromDb(
													beginDate,edata,ub.getVenderLimite(),clsConst.TRADE_TYPE_CARD,jiesuan);
											ClsSaleStatisticData salestatistic_cash= SqlADO.getSalesStatisticDataFromDb(
													beginDate,edata,ub.getVenderLimite(),clsConst.TRADE_TYPE_CASH,jiesuan);
											ClsSaleStatisticData salestatistic_freevend= SqlADO.getSalesStatisticDataFromDb(
													beginDate,edata,ub.getVenderLimite(),clsConst.TRADE_TYPE_COCO,jiesuan);											
											ClsSaleStatisticData salestatistic_bank= SqlADO.getSalesStatisticDataFromDb(
													beginDate,edata,ub.getVenderLimite(),clsConst.TRADE_TYPE_BANK,jiesuan);
									  %>
									<tr class="odd" style="height:2em;">
										<td class="col-md-1 text-right" id="all_count"><%=salestatistic_all.getM_count() %></td>
										<td class="col-md-1 text-right" id="all_credit"><%=String.format("%1.2f", salestatistic_all.getM_credit()/100.0) %></td>
										
										<td class="col-md-1 text-right" id="cash_count"><%=salestatistic_cash.getM_count() %></td>
										<td class="col-md-1 text-right" id="cash_credit"><%=String.format("%1.2f",salestatistic_cash.getM_credit()/100.0) %></td>

										<td class="col-md-1 text-right" id="freevend_count"><%=salestatistic_freevend.getM_count() %></td>
										<td class="col-md-1 text-right" id="freevend_credit"><%=String.format("%1.2f",salestatistic_freevend.getM_credit()/100.0) %></td>										
										<!--  
										<td id="wx_count"><%=salestatistic_wx.getM_count() %></td>
										<td id="wx_credit"><%=String.format("%1.2f",salestatistic_wx.getM_credit()/100.0) %></td>
										
										<td id="al_count"><%=salestatistic_al.getM_count() %></td>
										<td id="al_credit"><%=String.format("%1.2f",salestatistic_al.getM_credit()/100.0) %></td>
										
										<td id="card_count"><%=salestatistic_card.getM_count() %></td>
										<td id="card_credit"><%=String.format("%1.2f",salestatistic_card.getM_credit()/100.0) %></td>
										
										<td id="bank_count"><%=salestatistic_bank.getM_count() %></td>
										<td id="bank_credit"><%=String.format("%1.2f",salestatistic_bank.getM_credit()/100.0) %></td>
										-->
									</tr>
								</tbody>
							</table>
							<br/><br/>

							</div>			
					</div>
                </div><!-- /.box-body -->
				
              </div><!-- /.box -->
			  
            </div><!-- /.col -->
			
          <!-- /.row -->
          
		  </div>
        
                <!-- /.col-lg-6 -->
                <!-- jQuery -->
   <!-- jQuery -->
    <!-- <script src="js/bootstrap/jQuery-2.1.4.min.js"></script> -->

    <!-- Bootstrap Core JavaScript -->
    <script src="js/bootstrap/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="js/bootstrap/metisMenu.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="js/bootstrap/admin.js"></script>

    <!-- Page-Level Demo Scripts - Tables - Use for reference -->
    <script>
    $(document).ready(function() {
    	$('#myModal').modal('hide');
    	$('#myModal').on('hide.bs.modal', function (e) {
    		
    		});
    });
    </script>
</body>
</html>