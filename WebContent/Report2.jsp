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
		
		if(!ub.AccessAble(UserBean.FUNID_CAN_VIEW_STASTIC))
		{
			request.setAttribute("message", "Unable to "+UserBean.RIGHT_DES[UserBean.FUNID_CAN_VIEW_STASTIC]);
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
   	<!-- <style type="text/css">
   		table {
    border-spacing: 0px;
    border-collapse: collapse;
}
   		th {
    font-weight: normal;
    text-align: left;
    white-space: nowrap;
    height: 24px;
    line-height: 24px;
    color: #5B5B5B;
    border-top: 1px solid #C6C9CA;
    border-right: 1px solid #C6C9CA;
    border-left: 1px solid #C6C9CA;
    background: #FFF url("images/admin_title.gif") repeat-x scroll left top;
    }
    td {
    font-weight: normal;
    text-align: left;
    white-space: nowrap;
    height: 24px;
    line-height: 24px;
    color: #5B5B5B;
    border-top: 1px solid #C6C9CA;
    border-right: 1px solid #C6C9CA;
    border-bottom: 1px solid #C6C9CA;
    border-left: 1px solid #C6C9CA;
    }
   	</style> -->
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
    
    

    $("#jiesuan").click(function(){
    	/*提交结算日期到后台*/
    	var stratTime= $("#stratTime").val();
    	var postobj=new Object();
    	postobj.stratTime=stratTime;
    	postobj.activeid=<%=clsConst.ACTION_JIESUAN%>;
    	$("body").showLoading(); 
        $.ajax({
            type: "POST",
            url: "./AjaxRequest",
            data:postobj,
            success: (function(obj){
                $("body").hideLoading();  
                if(obj.msg==1)
                {
                	alert(obj.detail);
                }
                else
                {

                }
                
                }),
            dataType: "text",
            error:(function(obj,txtmes,txtmes2){
                $("body").hideLoading();  
                alert("Calculation error "+obj.status+" Error");
                })
        		
          });
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
  
    var isSelectObj=function(){
        var idLists= $("input[name='vendid']");
        for(var i=0;i<idLists.length;i++)
        {
            if(idLists[i].checked)
           	{
           	   return true;
           	}
        }
        return false;
    };
    
    var showAlert=function(title,content)
    {
        
        $( "#alertcontent" ).html(title);
        $( "#myModalLabel" ).html(content);
        $('#myModal').modal('show');
    }
    
    var getListVal=function()
    {
    	var obj=new Object();;
        var idLists= $("input[name='vendid']");
        var i=0,j=0;
        obj.vendid=new Array();
        for( i=0;i<idLists.length;i++)
        {
            if(idLists[i].checked)
            {
            	obj.vendid[j++]=idLists[i].value;
            }
        }
        return obj;
    }

});

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
	<!-- <div id="alertdlg" title="返回" style="display: none;">
	    <br/>
	    <div id="alertcontent"></div>
	</div> -->
          		<div class="breadcrumbs" id="breadcrumbs" style="margin-top:5px;">
						<ul class="breadcrumb">
							<li>
								<span class="glyphicon glyphicon-home"></span>
								<a href="MainHome.jsp" target="main" style="padding-left:5px;margin-left:5px;">Home</a>
							</li>

							<li>
								<a href="#">Transaction Management</a>
							</li>
							<li class="active">Vending Machine List</li>
						</ul>
					</div>
			<div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
			  <div class="row">
					<div class="col-xs-12">
						<div class="dataTables_length" id="dataTables-example_length">
							<form class="form-horizontal" role="form">
							<label>Machine ID:</label>
							  <label><input type="search" name="id" value="<%=id %>" class="form-control input-sm" placeholder="" aria-controls="dataTables-example"></label>
							
							&nbsp;<label>Select Month(Monthly)：</label>
							
							<label><input  name="sdate" id="stratTime" size="10" type="text" class="form-control input-sm" value="<%=ToolBox.getYMD(beginDate)%> "  readonly="readonly" onFocus="WdatePicker({readOnly:true})" /></label>
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
							<button type="submit" class="btn btn-default" style="background-color:#f4f4f4;">Search</button>
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
						<%
							int td_count=15; 
						%>
						<form action="./VenderList" method="post" name="form1" id="form1">
						<div class="table-responsive">
							<table id="yourTableID" class="table table-bordered table-hover table-condensed" style="overflow-y:auto; width:100%;height:100px;border-spacing: 0px;">
								<thead>
									<tr role="row" style="background-color: #f5f5f5;">
										<th class="col-md-1 text-center">#</th>
										<th class="col-md-1 text-center">Name</th>
										<th class="col-md-1 text-center">Address</th>
										<th class="col-md-1 text-center">Sales Count</th>
										<th class="col-md-1 text-center">Total</th>
										<th class="col-md-1 text-center">Cash Sales</th>
										<th class="col-md-1 text-center">Cash</th>
										<th class="col-md-1 text-center">Freevend#</th>
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
									
									   
									   ArrayList<VenderBean> lst=SqlADO.getVenderListByIdLimint(id.equals("")?ub.getVenderLimite():id); 
									  	
										if(lst!=null)
										{
											RsCount=lst.size();
											ListIterator<VenderBean> it=lst.listIterator();
											VenderBean obj;

											int all_count=0;
											int all_credit=0;
											

											int cash_count=0;
											int cash_credit=0;
											
											int freevend_count=0;
											int freevend_credit=0;
											
											int wx_count=0;
											int wx_credit=0;
											
											int al_count=0;
											int al_credit=0;
											
											int card_count=0;
											int cardal_credit=0;
											
											int bank_count=0;
											int bank_credit=0;
											
											Date edata=null;

											Calendar c= Calendar.getInstance();
											c.setTimeInMillis(beginDate.getTime());
											//c.set(Calendar.DAY_OF_MONTH, 1);
											c.set(Calendar.HOUR_OF_DAY, 0);
											c.set(Calendar.MINUTE, 0);
											c.set(Calendar.SECOND, 0);
										
											
											beginDate =new Date(c.getTimeInMillis());
											c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH)+1);
											edata=new Date(c.getTimeInMillis());

											while(it.hasNext())
											{
												obj=it.next();
												/*计算相关金额*/
												ClsSaleStatisticData salestatistic_all= SqlADO.getSalesStatisticDataFromDb(
														beginDate,edata,String.format("%d", obj.getId()),clsConst.TRADE_TYPE_NO_LIMIT,jiesuan);
												ClsSaleStatisticData salestatistic_al= SqlADO.getSalesStatisticDataFromDb(
														beginDate,edata,String.format("%d", obj.getId()),clsConst.TRADE_TYPE_AL_QR,jiesuan);
												ClsSaleStatisticData salestatistic_wx= SqlADO.getSalesStatisticDataFromDb(
														beginDate,edata,String.format("%d", obj.getId()),clsConst.TRADE_TYPE_WX_QR,jiesuan);
												ClsSaleStatisticData salestatistic_card= SqlADO.getSalesStatisticDataFromDb(
														beginDate,edata,String.format("%d", obj.getId()),clsConst.TRADE_TYPE_CARD,jiesuan);
												ClsSaleStatisticData salestatistic_cash= SqlADO.getSalesStatisticDataFromDb(
														beginDate,edata,String.format("%d", obj.getId()),clsConst.TRADE_TYPE_CASH,jiesuan);
												ClsSaleStatisticData salestatistic_bank= SqlADO.getSalesStatisticDataFromDb(
														beginDate,edata,String.format("%d", obj.getId()),clsConst.TRADE_TYPE_BANK,jiesuan);
												ClsSaleStatisticData salestatistic_freevend= SqlADO.getSalesStatisticDataFromDb(
														beginDate,edata,String.format("%d", obj.getId()),clsConst.TRADE_TYPE_COCO,jiesuan);
												count++;
												if(count<=(Page-1)*pagecount)
												{
													continue;
												}
												
												if(count>(Page)*pagecount)
												{
													break;
												}
												
												int venderid=obj.getId();
												int MdbDeviceStatus=obj.getMdbDeviceStatus();
												int Function_flg=obj.getFunction_flg();
												//Function_flg|=VenderBean.FUNC_IS_MDB_BILL_VALID|VenderBean.FUNC_IS_MDB_COIN_VALID|VenderBean.FUNC_IS_TERMPER_VALID;
												boolean hasState=false;
												//PrintWriter pw=response.getWriter();
												
												all_count+=salestatistic_all.getM_count();
												all_credit+=salestatistic_all.getM_credit();
												
												cash_count+=salestatistic_cash.getM_count();
												cash_credit+=salestatistic_cash.getM_credit();
												
												freevend_count+=salestatistic_freevend.getM_count();
												freevend_credit+=salestatistic_freevend.getM_credit();
												
												wx_count+=salestatistic_wx.getM_count();
												wx_credit+=salestatistic_wx.getM_credit();
												
												al_count+=salestatistic_al.getM_count();
												al_credit+=salestatistic_al.getM_credit();
												
												card_count+=salestatistic_card.getM_count();
												cardal_credit+=salestatistic_card.getM_credit();
												
												bank_count+=salestatistic_bank.getM_count();
												bank_credit+=salestatistic_bank.getM_credit();
												
									  %>
									<tr class="odd">
										<td class="text-center col-md-1 sorting_1"><%=venderid%></td>
										<td class="text-left col-md-1"><button type="button" class="btn btn-danger btn-sm"><%=obj.getTerminalName() %></button></td>
										<td class="text-left col-md-1"><%=obj.getTerminalAddress() %></td>
										<td class="col-md-1 text-right" <%=salestatistic_all.getM_count()>0?"style='color:red;'":"" %>><%=salestatistic_all.getM_count() %></td>
										<td class="col-md-1 text-right" <%=salestatistic_all.getM_credit()>0?"style='color:red;'":"" %> ><%=String.format("%1.2f", salestatistic_all.getM_credit()/100.0) %></td>
										
										<td class="col-md-1 text-right" <%=salestatistic_cash.getM_count()>0?"style='color:red;'":"" %>><%=salestatistic_cash.getM_count() %></td>
										<td class="col-md-1 text-right" <%=salestatistic_cash.getM_credit()>0?"style='color:red;'":"" %>><%=String.format("%1.2f",salestatistic_cash.getM_credit()/100.0) %></td>
										<td class="col-md-1 text-right" <%=salestatistic_freevend.getM_count()>0?"style='color:red;'":"" %>><%=salestatistic_freevend.getM_count() %></td>
										<td class="col-md-1 text-right" <%=salestatistic_freevend.getM_credit()>0?"style='color:red;'":"" %>><%=String.format("%1.2f",salestatistic_freevend.getM_credit()/100.0) %></td>										
										<!--  
										<td <%=salestatistic_wx.getM_count()>0?"style='color:red;'":"" %>><%=salestatistic_wx.getM_count() %></td>
										<td <%=salestatistic_wx.getM_credit()>0?"style='color:red;'":"" %>><%=String.format("%1.2f",salestatistic_wx.getM_credit()/100.0) %></td>
										
										<td <%=salestatistic_al.getM_count()>0?"style='color:red;'":"" %>><%=salestatistic_al.getM_count() %></td>
										<td <%=salestatistic_al.getM_credit()>0?"style='color:red;'":"" %>><%=String.format("%1.2f",salestatistic_al.getM_credit()/100.0) %></td>
										
										<td <%=salestatistic_card.getM_count()>0?"style='color:red;'":"" %>><%=salestatistic_card.getM_count() %></td>
										<td <%=salestatistic_card.getM_credit()>0?"style='color:red;'":"" %> ><%=String.format("%1.2f",salestatistic_card.getM_credit()/100.0) %></td>
										
										<td <%=salestatistic_bank.getM_count()>0?"style='color:red;'":"" %>><%=salestatistic_bank.getM_count() %></td>
										<td <%=salestatistic_bank.getM_credit()>0?"style='color:red;'":"" %>><%=String.format("%1.2f",salestatistic_bank.getM_credit()/100.0) %></td>
										-->
										
									</tr>
									<% 		
										}
									
									%>
									
									<tr class="odd">
										<td class="text-center sorting_1" colspan="3">Current Month Total</td>
										
										<td class="col-md-1 text-right" <%=all_count>0?"style='color:red;'":"" %>><%=all_count %></td>
										<td class="col-md-1 text-right" <%=all_credit>0?"style='color:red;'":"" %> ><%=String.format("%1.2f", all_credit/100.0) %></td>
										<td class="col-md-1 text-right" <%=cash_count>0?"style='color:red;'":"" %>><%=cash_count %></td>
										<td class="col-md-1 text-right" <%=cash_credit>0?"style='color:red;'":"" %>><%=String.format("%1.2f",cash_credit/100.0) %></td>
										<td class="col-md-1 text-right" <%=cash_count>0?"style='color:red;'":"" %>><%=cash_count %></td>
										<td class="col-md-1 text-right" <%=cash_credit>0?"style='color:red;'":"" %>><%=String.format("%1.2f",cash_credit/100.0) %></td>										
										<td class="col-md-1 text-right" <%=freevend_count>0?"style='color:red;'":"" %>><%=freevend_count %></td>
										<td class="col-md-1 text-right" <%=freevend_credit>0?"style='color:red;'":"" %>><%=String.format("%1.2f",freevend_credit/100.0) %></td>	

										<!--  
										<td <%=wx_count>0?"style='color:red;'":"" %>><%=wx_count %></td>
										<td <%=wx_credit>0?"style='color:red;'":"" %>><%=String.format("%1.2f",wx_credit/100.0) %></td>
										<td <%=al_count>0?"style='color:red;'":"" %>><%=al_count %></td>
										<td <%=al_credit>0?"style='color:red;'":"" %>><%=String.format("%1.2f",al_credit/100.0) %></td>
										<td <%=card_count>0?"style='color:red;'":"" %>><%=card_count %></td>
										<td <%=cardal_credit>0?"style='color:red;'":"" %> ><%=String.format("%1.2f",cardal_credit/100.0) %></td>
										<td <%=bank_count>0?"style='color:red;'":"" %>><%=bank_count %></td>
										<td <%=bank_credit>0?"style='color:red;'":"" %>><%=String.format("%1.2f",bank_credit/100.0) %></td>
										-->
									</tr>
									<% 		
										}
									
									%>
									
									<tr class="odd">
										<td class="center" colspan="<%=td_count %>">
											<%
										    	if(RsCount>0) 
										    	{
										    		out.println(ToolBox.getpages(null, "#999", Page, pagecount, RsCount));
												 }
										    	else 
										    	{
										    		out.println("<span class='waring-label'>No records found, please contact admin</span>");
										    	}
										    	%>
										 </td>
									</tr>
								
								</tbody>
							</table>
							</div>
							</form>				
						
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