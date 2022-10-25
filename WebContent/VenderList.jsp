<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.ListIterator"%>
<%@ page import="beans.PortBean"%>
<%@ page import="beans.VenderBean"%>
<%@ page import="beans.UserBean"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.ado.SqlADO"%>
<%@ page import="com.tools.ToolBox"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
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
    .btn {
    	margin-bottom: 5px;
    }
	</style>
<style type="text/css">
	.quick-look
	{
		-webkit-border-horizontal-spacing: 0px;
		-webkit-border-image: none;
		-webkit-border-vertical-spacing: 0px;
		border-bottom-color: white;
		border-bottom-left-radius: 3px;
		border-bottom-right-radius: 3px;
		border-bottom-style: none;
		border-width: 0px;
		border-collapse: separate;
		border-left-color: white;
		border-left-style: none;
		border-right-color: white;
		border-right-style: none;
		border-top-color: white;
		border-top-left-radius: 3px;
		border-top-right-radius: 3px;
		border-top-style: none;
		font-family: Ubuntu, Helvetica, Arial, sans-serif;
		line-height: 14px;
		max-width: none;
		text-align: left;
		vertical-align: baseline;
		white-space: nowrap;
		padding:5px;
		margin:3px;
		display:block;
		float:left;
		width:170px;
		font-size:13px;
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
  	int pagecount=100;

 	int Page=ToolBox.filterInt(request.getAttribute("pageindex").toString());

	int count_per_page = ToolBox.filterInt(request.getAttribute("count_per_page").toString());

	ArrayList<VenderBean> lst = (ArrayList<VenderBean>)request.getAttribute("lst");
	RsCount=lst.size();

	String SellerId = ToolBox.filter(request.getParameter("sellerid"));
	String terminalName = ToolBox.filter(request.getParameter("terminalName"));
	int isOnline = ToolBox.filterInt(request.getParameter("isOnline"));
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
				  			<input type="search" id="sellerid" name="sellerid" value="<%=SellerId %>" class="form-control input-sm" placeholder="" aria-controls="dataTables-example">
				  		</div>
				<!--
				  		<div class="form-group col-md-3 col-sm-6 col-xs-12">
							<label class="control-label">Machine ID</label>
				  			<input type="search" id="terminalName" name="terminalName" value="<%=terminalName %>" class="form-control input-sm" placeholder="" aria-controls="dataTables-example">
				  		</div>
						<div class="form-group col-md-3 col-sm-6 col-xs-12">
							<label class="control-label">Is Online?</label>
							<select class="select form-control" name="isOnline" id="isOnline">
								<option value="">All</option>
								<option value="false">Offline</option>
								<option value="true">Online</option>
							</select>
			  			</div>
						<div class="form-group col-md-3 col-sm-6 col-xs-12">
							<label class="control-label">Temp</label>
							<select class="select form-control" name="tempCat" id="tempCat">
								<option value="">All</option>
								<option value="1">Red (>-12)</option>
								<option value="2">Blue (-12 to -18)</option>
								<option value="3">Blue (&gt-18)</option>
							</select>
			  			</div>
			  			-->
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
							int td_count=14;
						%>
						<form action="./VenderList" method="post" name="form1" id="form1">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" style="overflow-y:auto; width:100%;height:100px;border-spacing: 0px;">
								<thead>
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
									<tr role="row" style="background-color: #f5f5f5;">
										<th class="col-md-1"  style="width: 30px">#</th>
										<th class="col-md-1">
											<input type="checkbox" id="checkAll">
										</th>
										<th class="col-md-1" style="width: 80px">Temp</th>
										<th class="col-md-1" >Name</th>
										<th class="col-md-4">Sales, Balance/ Capacity</th>
										<th class="col-md-1" style="width: 50px">Error</th>
										<th class="col-md-1">Balance Stock</th>
										<th class="col-md-1">Out of Stock SKU</th>
										<th class="col-md-1" style="width: 80px">Channel</th>
										<th class="col-md-4">Status</th>
										<th class="col-md-1">Temp Time</th>
										<th class="col-md-1" style="width: 40px">Firmware</th>
										<th class="col-md-1">Address</th>
										<%if(ub.AccessAble(UserBean.FUNID_DISABLE_FRANCHISEE))
										{%>
										<th class="col-md-2">Setting</th>
										<%
										}%>
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
											ArrayList<PortBean> pbli=null;
											boolean quehuo=false;
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

												pbli=SqlADO.getPortBeanList(obj.getId(), false);
												quehuo=false;
												for(PortBean pb:pbli)
												{
													if(pb.getCapacity()>pb.getAmount())
													{
														quehuo=true;
														break;
													}
												}

												if(!quehuo)
												{
													continue;
												}

												Map<Integer, Integer> channelErrorPair = new HashMap<Integer, Integer>();
												String firstDigit = "";

									  %>

									<tr class="even" id="BMS<%=venderid%>">
										<td class=" sorting_1 col-md-1">
											<strong>
												<%=venderid%>
											</strong>
										</td>
										<td class="center col-md-1">
										    <input type="checkbox" name="vendid" value="<%=venderid%>">
										  </td>
										<td class="center col-md-1">
											<%
												if(0!=(Function_flg&VenderBean.FUNC_IS_TERMPER_VALID))
												{
													hasState=true;
													if(obj.getTemperature()!=32767)
													{
														if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_TEMP_GRAPH)) {
															if(obj.getTemperature()>-120)
															{
																out.print(String.format("<button onclick='ShowTemCurve(%d);' type='button' class='btn btn-danger btn-sm' style='margin-right:3px;'>Temp:%1.1f℃</button>",obj.getId(),obj.getTemperature()/10.0));
															}
															else if(obj.getTemperature() >-180 && obj.getTemperature() <= -120) {
																out.print(String.format("<button onclick='ShowTemCurve(%d);' type='button' class='btn btn-info btn-sm' style='margin-right:3px;'>Temp:%1.1f℃</button>",obj.getId(),obj.getTemperature()/10.0));
															}else
															{
																out.print(String.format("<button onclick='ShowTemCurve(%d);'  type='button' class='btn btn-success btn-sm' style='margin-right:3px;'>Temp:%1.1f℃</button>",obj.getId(),obj.getTemperature()/10.0));
															}

														}else {
															if(obj.getTemperature()>-120)
															{
																out.print(String.format("<button type='button' class='btn btn-danger btn-sm' style='margin-right:3px;'>Temp:%1.1f℃</button>",obj.getTemperature()/10.0));
															}
															else if(obj.getTemperature() >-180 && obj.getTemperature() <= -120) {
																out.print(String.format("<button type='button' class='btn btn-info btn-sm' style='margin-right:3px;'>Temp:%1.1f℃</button>",obj.getId(),obj.getTemperature()/10.0));

															}else
															{
																out.print(String.format("<button type='button' class='btn btn-success btn-sm' style='margin-right:3px;'>Temp:%1.1f℃</button>",obj.getTemperature()/10.0));
															}
														}

													}
													else
													{
														out.print("<button type='button' onclick='ShowTemCurve("+ obj.getId() +");' class='btn btn-danger btn-sm' style='margin-right:3px;'>Abnormal</button>");
													}
												}
											%>
										</td>
										<td class="center col-md-1">
											<%=obj.getTerminalName() %>
										</td>
										<td class="center col-md-4">
												<%-- <ul class="list-inline" style="font-size: 13px;"> --%>
												<ul style="font-size: 13px;">
													<%
													 	int totalVolume = 0;
														int totalSold = 0;
														int totalChannel = 0;
														int totalChannelError = 0;
														int	runOutChannel = 0;
														int runOutSku = 0;
														int actualSold = 0;
														double balancePercent = 0;
														double outSkuPercent = 0;
														for(PortBean pb:pbli)
														{
															// if(
															//		((Integer.parseInt(pb.getInneridname()) >= 40 && Integer.parseInt(pb.getInneridname()) <= 47 ) ||
															//		(Integer.parseInt(pb.getInneridname()) >= 10 && Integer.parseInt(pb.getInneridname()) <= 29) ||
															//		(Integer.parseInt(pb.getInneridname()) >= 30 && Integer.parseInt(pb.getInneridname()) <= 38) ||
															//		(Integer.parseInt(pb.getInneridname()) >= 51 && Integer.parseInt(pb.getInneridname()) <= 54) ||
															//		(Integer.parseInt(pb.getInneridname()) >= 61 && Integer.parseInt(pb.getInneridname()) <= 66)
															//		) && pb.getCapacity() != 0 ) {

																if(pb.getError_id() > 0) {
																	totalChannelError += 1;
																	channelErrorPair.put(pb.getInnerid() , pb.getError_id());
																}
																totalChannel += 1;
																totalVolume += pb.getCapacity();
																totalSold += pb.getAmount();
																%>
																<%-- <li style="padding: 5px 10px; margin: 3px 5px;"> --%>
																<li class="quick-look">
																	<span >
																		#:<%=pb.getInneridname()%> -
																	</span>
																	<span style="color: blue;">
																		<%=String.format("% 2d",pb.getCapacity()-pb.getAmount()) %>,
																	</span>
																	<%
																		if(pb.getAmount() <= 2) {
																	%>
																			<span style="color:red;">
																				<%=String.format("% 2d \t/ % 2d",pb.getAmount(), pb.getCapacity()) %>
																			</span>
																	<%
																		}else {
																	%>
																			<span  style="color:green;">
																				<%=String.format("% 2d \t/ % 2d",pb.getAmount(), pb.getCapacity()) %>
																			</span>
																	<%
																		}

																		if(pb.getAmount() == 0) {
																			runOutChannel += 1;
																		}
																	%>
																</li>
																<%
															//}
														}

														actualSold = totalVolume - totalSold;
														balancePercent = ((double)totalSold / (double)totalVolume) * 100;
														runOutSku = runOutChannel + totalChannelError;
														outSkuPercent = ((double)runOutSku/ (double)totalChannel) * 100;
													%>
												</ul>
										</td>
										<td class="center">
											<%
												for(Map.Entry<Integer, Integer> entry : channelErrorPair.entrySet()) {
													Integer key = entry.getKey();
													Integer value = entry.getValue();
											%>
											<li class="quick-look" style="width:70px;">
													<span style="color: red;">
														<strong>
															# <%= key %>: <%= value %>
														</strong>
													</span>
											</li>
											<%
												}
											%>
										</td>
										<td>
											<strong>
											<%
											if(balancePercent <= 30) {
											%>
													<span style="color: red;">
											<%
												}else if(balancePercent > 30 && balancePercent <= 60) {
											%>
													<span style="color: blue;">
											<%
												}else if(balancePercent > 60) {
											%>
													<span style="color: black;">
											<%
												}
											%>
														<%=String.format("% 3d/ % 3d", totalSold, totalVolume) %>
														(<%= String.format("%.0f", balancePercent) %>%)
														<br><br>
													</span>
											</strong>
										</td>
										<td>
											<strong>
											<%
											if(outSkuPercent > 40) {
											%>
													<span style="color: red;">
											<%
												}else {
											%>
													<span style="color: black;">
											<%
												}
											%>
														<%=String.format("% 3d/ % 3d", runOutSku, totalChannel) %>
														(<%= String.format("%.0f", outSkuPercent) %>%)
													</span>
											</strong>
										</td>
										<td class="center col-md-1">
											<%if(ub.AccessAble(UserBean.FUNID_DISABLE_FRANCHISEE))
											{
											%>
												<a class="btn btn-danger" href="PortList.jsp?mid=<%=venderid%>">
													<i class="glyphicon glyphicon-zoom-in icon-white"></i>
													Channel
												</a>
											<%
											}
											%>
										</td>
										<td class="center col-md-4" >
											<%=obj.isIsOnline()?"<button type='button' class='btn btn-success btn-xs' style='font-weight: 700;'>On</button>":"<button type='button' class='btn btn-success btn-xs' style='background-color:#777;border-color:#fff;font-weight: 700;'>Off</button>"%>
											<%
											  	if(0!=(Function_flg&VenderBean.FUNC_IS_MDB_COIN_VALID))
											  	{
											  		hasState=true;
											  		if(obj.getCoinAttube() > 1600) {
											  			out.print(String.format("<button type='button' class='btn btn-success btn-xs' style='margin-right:3px;'>Coins:%1.2f</button>",obj.getCoinAttube()/100.0));
											  		} else{
											  			out.print(String.format("<button type='button' class='btn btn-warning btn-xs' style='margin-right:3px;'>Coins:%1.2f</button>",obj.getCoinAttube()/100.0));
											  		}
											  			//out.print(String.format("<li class='normal-label mechine-state'>Coin Box:%1.2f</li>",obj.getCoinAtbox()/100.0));
											  		out.print(((MdbDeviceStatus&VenderBean.MDB_COMMUNICATION_COIN)==0)?"<button type='button' class='btn btn-warning btn-xs' style='margin-right:3px;'>Coin accepter Prob</button>":"<button type='button' class='btn btn-success btn-xs' style='margin-right:3px;'>Coin OK</button>");
											  	}

											  	if(0!=(Function_flg&VenderBean.FUNC_IS_MDB_BILL_VALID))
											  	{
											  		hasState=true;
											  		out.print(((MdbDeviceStatus&VenderBean.MDB_COMMUNICATION_BILL)==0)?"<button type='button' class='btn btn-warning btn-xs' style='margin-right:3px;'>Bill acceptor Prob</button>":"<button type='button' class='btn btn-success btn-xs' style='margin-right:3px;'>Bill OK</button>");
											  		out.print(String.format("<button type='button' class='btn btn-success btn-xs' style='margin-right:3px;'>Bill:%03d</button>",obj.getBills()/100));

											  	}
											  	if(0!=(Function_flg&VenderBean.FUNC_IS_MDB_CASHLESS_VALID))
											  	{
											  		hasState=true;
											  		out.print(((MdbDeviceStatus&VenderBean.MDB_COMMUNICATION_CASHLESS)==0)?"<button type='button' class='btn btn-warning btn-xs' style='margin-right:3px;'>Non cash Prob</button>":"<button type='button' class='btn btn-success btn-xs' style='margin-right:3px;'>Non Cash OK</button>");
											  	}
											  	if(0!=(Function_flg&VenderBean.FUNC_IS_SENSOR_VALID))
											  	{
											  		hasState=true;
											  		int flags1=obj.getFlags1();
											  		out.print("<button type='button' class='btn "+ (((flags1&1)==0)?"btn-danger":"btn-success") +" btn-xs' style='margin-right:3px;'>Sensor Status:"+ (((flags1&1)==0)?"0":"1") +"</button>");
											  		out.print("<button type='button' class='btn "+ (((flags1&2)==0)?"btn-success":"btn-danger") +" btn-xs' style='margin-right:3px;'>Error Code 9:"+ (((flags1&2)==0)?"N":"Y") +"</button>");
											  		out.print("<button type='button' class='btn btn-success btn-xs' style='margin-right:3px;'>Fan Status:"+ (((flags1&4)==0)?"0":"1") +"</button>");
											  	}
											  	if(0!=(Function_flg&VenderBean.FUNC_IS_ACB_VALID))
											  	{
											  		hasState=true;
											  		//int flags1=obj.getFlags1();
											  		//out.print("<button type='button' class='btn "+ (((flags1&1)==0)?"btn-danger":"btn-success") +" btn-sm' style='margin-right:3px;'>Sensor Status:"+ (((flags1&1)==0)?"0":"1") +"</button>");
											  		//out.print("<button type='button' class='btn "+ (((flags1&2)==0)?"btn-success":"btn-danger") +" btn-sm' style='margin-right:3px;'>Error Code 9:"+ (((flags1&2)==0)?"N":"Y") +"</button>");
											  		//out.print("<button type='button' class='btn btn-success btn-sm' style='margin-right:3px;'>Fan Status:"+ (((flags1&4)==0)?"0":"1") +"</button>");
											  		out.print("<button type='button' class='btn btn-success btn-xs' style='margin-right:3px;'>T2:"+ ((obj.getTem2()==VenderBean.INVALID_TEMPER)?"NA.":obj.getTem2()/10.0) +"℃</button>");
											  		out.print("<button type='button' class='btn btn-success btn-xs' style='margin-right:3px;'>T3:"+ ((obj.getTem3()==VenderBean.INVALID_TEMPER)?"NA.":obj.getTem2()/10.0) +"℃</button>");
											  		out.print("<button type='button' class='btn btn-success btn-xs' style='margin-right:3px;'>T4:"+ ((obj.getTem4()==VenderBean.INVALID_TEMPER)?"NA.":obj.getTem2()/10.0) +"℃</button>");
											  		out.print("<button type='button' class='btn btn-success btn-xs' style='margin-right:3px;'>FAN:"+ obj.getFan() +"</button>");
											  		out.print("<button type='button' class='btn btn-success btn-xs' style='margin-right:3px;'>DOOR:"+ obj.getDoorisopen()+"</button>");
											  	}
											  	if(!hasState)
											  	{
											  		out.print(String.format("No parameter has been detected!"));
											  	}
											  	%>

										</td>

										<td class="center col-md-1"><%=obj.getTemperUpdateTime() %></td>
										<td class="center col-md-1">
											<%= Integer.toHexString(obj.getCode_ver()) %>
										</td>
										<td class="center col-md-1" style="text-overflow:ellipsis;width:200px" title="<%=obj.getTerminalAddress()%>"><%=obj.getTerminalAddress()%></td>
										<%if(ub.AccessAble(UserBean.FUNID_DISABLE_FRANCHISEE))
										{%>
											<td class="center col-md-2">
												<a class="btn btn-success" href="map.jsp?id=<%=venderid%>">
													<i class="glyphicon glyphicon-map-marker icon-white"></i>
													Map
												</a>
												<a class="btn btn-info" href="VenderMod.jsp?mid=<%=venderid%>">
													<i class="glyphicon glyphicon-edit icon-white"></i>
													Detail
												</a>
											</td>
										<%
										}
										%>
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