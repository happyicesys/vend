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

function ShowTemCurve(id)
{
	location.href="./TempCurve.jsp?vid="+id;
}

</script>
</head>
<%
	int RsCount=0;
  	int pagecount=ub.getPagecount();
 	
 	int Page=ToolBox.filterInt(request.getAttribute("pageindex").toString());

	int count_per_page = ToolBox.filterInt(request.getAttribute("count_per_page").toString());
	
	ArrayList<VenderBean> lst = (ArrayList<VenderBean>)request.getAttribute("lst");
	
	String id = request.getAttribute("id").toString();
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
								<a href="#">Setting Management</a>
							</li>
							<li class="active">Vending Machine List</li>
						</ul>
					</div>
				<form class="form">
					<div class="row">			
						<div class="form-group col-md-3 col-sm-6 col-xs-12">
							<label class="control-label">Machine ID</label>
				  			<input type="search" name="id" value="<%=id %>" class="form-control input-sm" placeholder="" aria-controls="dataTables-example">
				  		</div>
			  		</div>
			  		
					<div class="row">
						<div class="button-group col-md-12 col-sm-12 col-xs-12" style="padding-bottom: 15px;">
							<button type="submit" class="btn btn-default" style="background-color:#f4f4f4;">Search</button>
							<!-- 
							<button type="button" class="btn btn-default" style="background-color:#f4f4f4;" id="select_all">Select All</button>
							<button type="button" class="btn btn-default" style="background-color:#f4f4f4;" id="deselect_all">Clear Selection</button>
							<button type="button" class="btn btn-default" style="background-color:#f4f4f4;" id="fanxuan">Deselect</button>
							 -->
							<%if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_VENDER))
							{%>
							<button type="button" class="btn btn-default" style="background-color:#f4f4f4;" onclick="javascript:location.href='AddVender.jsp';" >Add Machine</button>
							<%} %>
						</div>																				
					</div>
											  		
				</form>
			
			<div class="row" style="overflow-y:auto;">
            <div class="col-xs-12">

              <div class="box">
                <div class="box-body">
					<div id="DataTables_Table_0_wrapper" class="dataTables_wrapper" role="grid">
						<%
							int td_count=11; 
						%>
						<form action="./VenderList" method="post" name="form1" id="form1">
						<div class="table-responsive">
							<table class="table table-bordered table-hover table-condensed" style="overflow-y:auto; width:100%;height:100px;border-spacing: 0px;">
								<thead>
									<tr role="row" style="background-color: #f5f5f5;">
										<th class="col-md-1">#</th>
										<th class="col-md-1">
											<input type="checkbox" id="checkAll">
										</th>
										<th class="col-md-1">Conn</th>
										<th class="col-md-1">Category</th>
										<th class="col-md-2" >Name</th>
										<th class="col-md-1">Address</th>
										<th class="col-md-4">Status</th>
										<!-- 
											<th style="width:400px;">Real time Status</th>
											<th style="width: 250px;">Setting</th>
										 -->
										<th class="col-md-1">Temp Time</th>
										<th class="col-md-1">Error</th>
										<th class="col-md-2">Setting</th>
									</tr>											
								</thead>

								<tbody role="alert" aria-live="polite" aria-relevant="all">
									<%
									 	int count=0;
									
									  /* ArrayList<VenderBean> lst=SqlADO.getVenderListByIdLimint(ub.getVenderLimite()); */
									  	
										if(lst!=null)
										{
											RsCount=lst.size();
											ListIterator<VenderBean> it=lst.listIterator();
											VenderBean obj;
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
												
												int venderid=obj.getId();
												int MdbDeviceStatus=obj.getMdbDeviceStatus();
												int Function_flg=obj.getFunction_flg();
												//Function_flg|=VenderBean.FUNC_IS_MDB_BILL_VALID|VenderBean.FUNC_IS_MDB_COIN_VALID|VenderBean.FUNC_IS_TERMPER_VALID;
												boolean hasState=false;
												//PrintWriter pw=response.getWriter();
													
									  %>
									<tr class="odd" id="BMS<%=venderid%>">
										<td class=" sorting_1 col-md-1"><%=venderid%></td>
										<td class="center col-md-1">
										    <input type="checkbox" name="vendid" value="<%=venderid%>">
										  </td>
										<td class="center col-md-1">
											<%=obj.isIsOnline()?"<button type='button' class='btn btn-success btn-sm' style='font-weight: 700;'>On</button>":"<button type='button' class='btn btn-success btn-sm' style='background-color:#777;border-color:#fff;font-weight: 700;'>Off</button>"%>
										</td>
										<td class="center col-md-1"></td>
										<td class="center col-md-2"><%=obj.getTerminalName() %></td>
										<td class="center col-md-1" style="text-overflow:ellipsis;width:200px" title="<%=obj.getTerminalAddress()%>"><%=obj.getTerminalAddress()%></td>
										<td class="center col-md-4" >
											<%
											  	if(0!=(Function_flg&VenderBean.FUNC_IS_TERMPER_VALID))
											  	{
											  		hasState=true;
											  		if(obj.getTemperature()!=32767)
											  		{
												  		if(obj.getTemperature()>-120)
												  		{
												  			out.print(String.format("<button onclick='ShowTemCurve(%d);' type='button' class='btn btn-danger btn-sm' style='margin-right:3px;'>Temp:%1.1f℃</button>",obj.getId(),obj.getTemperature()/10.0));
												  		}
												  		else
												  		{
												  			out.print(String.format("<button onclick='ShowTemCurve(%d);'  type='button' class='btn btn-success btn-sm' style='margin-right:3px;'>Temp:%1.1f℃</button>",obj.getId(),obj.getTemperature()/10.0));
												  		}
											  		}
											  		else
											  		{
											  			out.print("<button type='button' onclick='ShowTemCurve("+ obj.getId() +");' class='btn btn-danger btn-sm' style='margin-right:3px;'>Temperature Abnormal</button>");
											  		}
											  	}
											  	
											  	if(0!=(Function_flg&VenderBean.FUNC_IS_MDB_COIN_VALID))
											  	{
											  		hasState=true;
											  		if(obj.getCoinAttube() > 1600) {
											  			out.print(String.format("<button type='button' class='btn btn-success btn-sm' style='margin-right:3px;'>Coins:%1.2f</button>",obj.getCoinAttube()/100.0));
											  		} else{
											  			out.print(String.format("<button type='button' class='btn btn-warning btn-sm' style='margin-right:3px;'>Coins:%1.2f</button>",obj.getCoinAttube()/100.0));
											  		}
											  			//out.print(String.format("<li class='normal-label mechine-state'>Coin Box:%1.2f</li>",obj.getCoinAtbox()/100.0));
											  		out.print(((MdbDeviceStatus&VenderBean.MDB_COMMUNICATION_COIN)==0)?"<button type='button' class='btn btn-warning btn-sm' style='margin-right:3px;'>Coin accepter Prob</button>":"<button type='button' class='btn btn-success btn-sm' style='margin-right:3px;'>Coin OK</button>");
											  	}
											  	
											  	if(0!=(Function_flg&VenderBean.FUNC_IS_MDB_BILL_VALID))
											  	{
											  		hasState=true;
											  		out.print(((MdbDeviceStatus&VenderBean.MDB_COMMUNICATION_BILL)==0)?"<button type='button' class='btn btn-warning btn-sm' style='margin-right:3px;'>Bill acceptor Prob</button>":"<button type='button' class='btn btn-success btn-sm' style='margin-right:3px;'>Bill OK</button>");
											  		out.print(String.format("<button type='button' class='btn btn-success btn-sm' style='margin-right:3px;'>Bill:%03d</button>",obj.getBills()/100));
											  		
											  	}
											  	if(0!=(Function_flg&VenderBean.FUNC_IS_MDB_CASHLESS_VALID))
											  	{
											  		hasState=true;
											  		out.print(((MdbDeviceStatus&VenderBean.MDB_COMMUNICATION_CASHLESS)==0)?"<button type='button' class='btn btn-warning btn-sm' style='margin-right:3px;'>Non cash Prob</button>":"<button type='button' class='btn btn-success btn-sm' style='margin-right:3px;'>Non Cash OK</button>");
											  	}
											  	if(!hasState)
											  	{
											  		out.print(String.format("No parameter has been detected!"));
											  	}
											  	%>

										</td>

										<td class="center col-md-1"><%=obj.getTemperUpdateTime() %></td>
										<%
											String slot_format="";
											if(obj.getId_Format().equals("HEX"))
											{
												slot_format="%X Channel %d Error";
											}
											else
											{
												slot_format="%d Channel %d Error";
											}
										%>
										<td class="center col-md-1"><%=(obj.getLstSltE()==0)?"No Malfunction":String.format(slot_format, obj.getLstSltE()/1000,obj.getLstSltE()%1000) %></td>
										<td class="center col-md-2">
											<a class="btn btn-success" href="map.jsp?id=<%=venderid%>">
												<i class="glyphicon glyphicon-map-marker icon-white"></i>
												Map
											</a>
											<a class="btn btn-info" href="VenderMod.jsp?mid=<%=venderid%>">
												<i class="glyphicon glyphicon-edit icon-white"></i>
												Detail
											</a>
											<a class="btn btn-danger" href="PortList.jsp?mid=<%=venderid%>">
												<i class="glyphicon glyphicon-zoom-in icon-white"></i>
												Channel
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
    
	    $('#checkAll').change(function(){
	        var all = this;
	        $(this).closest('table').find('input[type="checkbox"]').prop('checked', all.checked);
	    });
    </script>
</body>
</html>