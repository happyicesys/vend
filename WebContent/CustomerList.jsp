<%@page import="com.clsConst"%>
<%@page import="beans.CustomerBean"%>
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
    <script language="javascript" type="text/javascript" src="./jquery_ui/js/jquery.ui.datepicker-zh-TW.js"></script>
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
    //Generating Alipay 2D barcode
    $("#but_al_qrcode").click(function(){  
       var obj;
    	if(!isSelectObj())
   	    {
    	    showAlert("Please select 2D Barcode generator","Generating Alipay 2D barcode");
    	    return;
   	    }
       obj=getListVal();
       obj.qrcode_type="al";
       $("body").showLoading();  
       /*发送ajax*/
       $.ajax({
         type: "POST",
         url: "./AjaxQrCodeFactory",
         data:{data:JSON.stringify(obj)} ,
         success: (function(obj){
             $("body").hideLoading();  
        	 showAlert(obj,"Generating Alipay 2D barcode");
             }),
         dataType: "text"
       });
       
       
    });
    //Generating Wechat 2D barcode
    $("#but_wx_qrcode").click(function(){  
    	//showAlert("微信功能暂未添加！");
        var obj;
        if(!isSelectObj())
        {
            showAlert("Please select 2D Barcode generator","Generating Wechat 2D barcode");
            return;
        }
       obj=getListVal();
       obj.qrcode_type="wx";
       $("body").showLoading();  
       /*发送ajax*/
       $.ajax({
         type: "POST",
         url: "./AjaxQrCodeFactory",
         data:{data:JSON.stringify(obj)} ,
         success: (function(obj){
             $("body").hideLoading();  
             showAlert(obj,"Generating Wechat 2D barcode");
             }),
         dataType: "text"
       });
    });
});



</script>
</head>
<%
	int RsCount=0;
  	int pagecount=ub.getPagecount();
 	
 	int Page=ToolBox.filterInt(request.getParameter("page"));
	if(Page==0)
	{
		Page=1;
	}
	//int count_per_page = pagecount;//;ToolBox.filterInt(request.getParameter("count_per_page"));
	String customername=ToolBox.filter(request.getParameter("customername"));
	String cardinfo=ToolBox.filter(request.getParameter("cardinfo"));
	String id_string=ToolBox.filter(request.getParameter("id_string"));
	ArrayList<CustomerBean> lst=null;
	
	String con="";
	if(!cardinfo.equals(""))
	{
		con+= " and  _user_id_card like '%"+ cardinfo +"%'";
	}
	
	if(!customername.equals(""))
	{
		con+= " and  _user_name like '%"+ customername +"%' ";
	}
	
	if(!id_string.equals(""))
	{
		con+= " and  _user_id_string like '%"+ id_string +"%' ";
	}
	lst = CustomerBean.getCustomerBeanLst(ub.getGroupid(),con);

	


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
               data-dismiss="modal">确定
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
								<a href="#">Customer Management</a>
							</li>
							<li class="active">Customer List</li>
						</ul>
					</div>
			<div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
			  <div class="row">
					<div class="col-xs-12">
						<div class="dataTables_length" id="dataTables-example_length">
							<form class="form-horizontal" role="form">

										<%if(ub.AccessAble(UserBean.FUNID_CAN_ADD_CUSTOMER))
										{%>
										<button type="button" class="btn btn-default" style="background-color:#f4f4f4;" onclick="javascript:location.href='AddCustomer.jsp';" >Add Customer</button>
										<%} %>
							<label>Card ID</label>
							  <label><input type="search" name="cardinfo" value="<%=cardinfo %>" class="form-control input-sm" placeholder="" aria-controls="dataTables-example"></label>
							<label>会员名：</label>
							  <label><input type="search" name="customername" value="<%=customername %>" class="form-control input-sm" placeholder="" aria-controls="dataTables-example"></label>
							<label>Unique ID:</label>
							  <label><input type="search" name="id_string" value="<%=id_string %>" class="form-control input-sm" placeholder="" aria-controls="dataTables-example"></label>
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
							int td_count=12; 
						%>

						<div class="table-responsive">
							<table class="table table-bordered table-hover table-condensed" style="overflow-y:auto; width:100%;height:100px;border-spacing: 0px;">
								<thead>
									<tr role="row" style="background-color: #f5f5f5;">
										<th>#</th>
										<th >Customer Name</th>
										<th >Unique ID</th>
										<th >Credit Card No</th>
										<th >Card Credit Inside</th>
										<th >Address</th>
										<th >DOB</th>
										<th >Lastest Transaction Amount</th>
										<th >Lastest Transaction Date</th>
										<th >Lastest Topup Amount</th>
										<th >Lastest Topup Date</th>
										<th style="width: 250px;">Setting</th>
									</tr>											
								</thead>

								<tbody role="alert" aria-live="polite" aria-relevant="all">
									<%
									 	int count=0;
										if(lst!=null)
										{
											RsCount=lst.size();
											ListIterator<CustomerBean> it=lst.listIterator();
											CustomerBean obj;
											//int index=0;
											while(it.hasNext())
											{
												obj=it.next();
												count++;
												if(count<=(Page-1)*pagecount)
												{
													continue;
												}
												
												if(count>(Page)*pagecount)
												{
													break;
												}
												
												int id=obj.getId();

												//Function_flg|=VenderBean.FUNC_IS_MDB_BILL_VALID|VenderBean.FUNC_IS_MDB_COIN_VALID|VenderBean.FUNC_IS_TERMPER_VALID;
												boolean hasState=false;
									  %>
									<tr class="odd" id="BMS<%=id%>">
										<td class=" sorting_1"><%=id%></td>
										<td class="center "><%=obj.get_user_name() %></td>
										<td class="center "><%=obj.get_user_id_string()%></td>
										<td class="center "><%=obj.get_user_id_card()%></td>
										<td class="center "><%=String.format("%1.2f", obj.get_user_amount()/100.0) %></td>
										<td class="center "><%=obj.get_user_address() %></td>
										<td class="center "><%=ToolBox.getTimeString(obj.get_user_birthday()) %></td>
										<td class="center "><%=String.format("%1.2f", obj.getLast_jiaoyi_amount()/100.0) %></td>
										<td class="center "><%=ToolBox.getTimeLongString(obj.getLast_jiaoyi_time()) %></td>
										<td class="center "><%=String.format("%1.2f", obj.getLast_charge_amount()/100.0) %></td>
										<td class="center "><%=ToolBox.getTimeLongString(obj.getLast_charge_time()) %></td>
										<td class="center ">
											 <a class="btn btn-success" href="customerMod.jsp?id=<%=id%>">
												<i class="glyphicon glyphicon-map-marker icon-white"></i>
												Details/ Amendment
											</a>
											 
											<a class="btn btn-info" href="TradeList.jsp?tradetype=<%=clsConst.TRADE_TYPE_CARD %>&CardNumber=<%=obj.get_user_id_card()%>">
												<i class="glyphicon glyphicon-edit icon-white"></i>
												Records
											</a>
										</td>
									</tr>
									<% 		
										}
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
										    		out.println("<span class='waring-label'>You dont have member to maintain</span>");
										    	}
										    			
										    	%>
										 </td>
									</tr>
								
								</tbody>
							</table>
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