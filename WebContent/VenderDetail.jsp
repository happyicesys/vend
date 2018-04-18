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
 	
 	int Page=ToolBox.filterInt(request.getAttribute("pageindex").toString());
 	
	int count_per_page = ToolBox.filterInt(request.getAttribute("count_per_page").toString());
	
	ArrayList<VenderBean> lst = (ArrayList<VenderBean>)request.getAttribute("lst");
	
	String id = request.getAttribute("id").toString();
	
	int td_count=10; 
%>
<body>

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
										<td class=" sorting_1"><%=venderid%></td>
										<td class="center ">
										    <input type="checkbox" name="vendid" value="<%=venderid%>">
										    
										  </td>
										<td class="center "><button type="button" class="btn btn-danger btn-sm"><%=obj.getTerminalName() %></button></td>
										<td class="center "><%=obj.getTerminalAddress() %></td>
										<td class="center ">
											<%
											  	if(0!=(Function_flg&VenderBean.FUNC_IS_TERMPER_VALID))
											  	{
											  		hasState=true;
											  		out.print(String.format("<button type='button' class='btn btn-success btn-sm' style='margin-right:3px;'>Temperature:%1.1f℃</button>",obj.getTemperature()/10.0));
											  	}
											  	
											  	if(0!=(Function_flg&VenderBean.FUNC_IS_MDB_COIN_VALID))
											  	{
											  		hasState=true;
											  		out.print(String.format("<button type='button' class='btn btn-success btn-sm' style='margin-right:3px;'>Coins:%1.2f</button>",obj.getCoinAttube()/100.0));
											  		//out.print(String.format("<li class='normal-label mechine-state'>Coin Box:%1.2f</li>",obj.getCoinAtbox()/100.0));
											  		out.print(((MdbDeviceStatus&VenderBean.MDB_COMMUNICATION_COIN)==0)?"<button type='button' class='btn btn-warning btn-sm' style='margin-right:3px;'>Coin accepter Prob</button>":"<button type='button' class='btn btn-success btn-sm' style='margin-right:3px;'>硬币器正常</button>");
											  	}
											  	
											  	if(0!=(Function_flg&VenderBean.FUNC_IS_MDB_BILL_VALID))
											  	{
											  		hasState=true;
											  		out.print(((MdbDeviceStatus&VenderBean.MDB_COMMUNICATION_BILL)==0)?"<button type='button' class='btn btn-warning btn-sm' style='margin-right:3px;'>Bill acceptor Prob</button>":"<button type='button' class='btn btn-success btn-sm' style='margin-right:3px;'>纸币器正常</button>");
											  	}
											  	if(0!=(Function_flg&VenderBean.FUNC_IS_MDB_CASHLESS_VALID))
											  	{
											  		hasState=true;
											  		out.print(((MdbDeviceStatus&VenderBean.MDB_COMMUNICATION_CASHLESS)==0)?"<button type='button' class='btn btn-warning btn-sm' style='margin-right:3px;'>Non cash Prob</button>":"<button type='button' class='btn btn-success btn-sm' style='margin-right:3px;'>非现金设备正常</button>");
											  	}
											  	if(!hasState)
											  	{
											  		out.print(String.format("该机型无Real time Status参数！"));
											  	}
											  	%>
										</td>
										<td class="center ">
											<%=obj.isIsOnline()?"<button type='button' class='btn btn-success btn-sm' style='font-weight: 700;'>Online</button>":"<button type='button' class='btn btn-success btn-sm' style='background-color:#777;border-color:#fff;font-weight: 700;'>Disconnected</button>"%>
										</td>
										<td class="center "><%=obj.getIRErrCnt() %></td>
										<td class="center "><%=(obj.getLstSltE()==0)?"No Malfunction":String.format("%X号货道%d号故障", obj.getLstSltE()/1000,obj.getLstSltE()%1000) %></td>
										<td class="center ">
											<%=obj.getGprs_Sign()*100/31 %>%
										</td>
										<td class="center ">
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

</body>
</html>