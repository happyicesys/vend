<%@page import="com.clsConst"%>
<%@page import="beans.RefundBean"%>
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
		
		if(!ub.AccessAble(UserBean.FUNID_CAN_VIEW_VENDER))
		{
			request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_VIEW_VENDER]);
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}
		session.setAttribute("currentpage", request.getRequestURI()+"?"+request.getQueryString());
		
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
    //生成阿里二维码
    $("#but_al_qrcode").click(function(){  
       var obj;
    	if(!isSelectObj())
   	    {
    	    showAlert("请先选择需要生成二维码的终端！","生成阿里二维码");
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
        	 showAlert(obj,"生成阿里二维码");
             }),
         dataType: "text"
       });
       
       
    });
    //生成微信二维码
    $("#but_wx_qrcode").click(function(){  
    	//showAlert("微信功能暂未添加！");
        var obj;
        if(!isSelectObj())
        {
            showAlert("请先选择需要生成二维码的终端！","生成微信二维码");
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
             showAlert(obj,"生成微信二维码");
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
 	int Page=ToolBox.filterInt(request.getParameter("pageindex"));
 	if(Page==0)
 	{
 		Page=1;
 	}
	int count_per_page = ToolBox.filterInt(request.getParameter("count_per_page"));
	ArrayList<RefundBean> lst=SqlADO.getRefundBeanLst(ub.getVenderLimite(), ub.getGroupid());
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
								<a href="MainHome.jsp" target="main" style="padding-left:5px;margin-left:5px;">首页</a>
							</li>

							<li>
								<a href="#">交易管理</a>
							</li>
							<li class="active">退款日志</li>
						</ul>
					</div>
			<div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
			  <div class="row">
					<div class="col-xs-12">
						<div class="dataTables_length" id="dataTables-example_length">
							<form class="form-horizontal" role="form">
							<!-- <label>机器编号：</label>
							  <label><input type="search" name="id" class="form-control input-sm" placeholder="" aria-controls="dataTables-example"></label>
							<button type="submit" class="btn btn-default" style="background-color:#f4f4f4;">查询</button>
							 -->
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
							int td_count=13; 
						%>
						<form action="./VenderList" method="post" name="form1" id="form1">
						<div class="table-responsive">
							<table class="table table-bordered table-hover table-condensed" style="overflow-y:auto; width:100%;height:100px;border-spacing: 0px;">
								<thead>
									<tr role="row" style="background-color: #f5f5f5;">
										<th>编号</th>
										<th >交易订单号</th>
										<th >交易ID</th>
										<th >退款编号</th>
										<th >退款时间</th>
										<th >交易终端</th>
										<th >货道编号</th>
										<th>商品名称</th>
										<th>支付平台返回信息</th>
										<th >是否成功</th>
										<th >退款类型</th>
										<th >操作员</th>
										<th >退款金额</th>
									</tr>											
								</thead>

								<tbody role="alert" aria-live="polite" aria-relevant="all">
									<%
									 	int count=0;
									
										if(lst!=null)
										{
											RsCount=lst.size();
											System.out.println(RsCount);
											ListIterator<RefundBean> it=lst.listIterator();
											RefundBean obj;
											while(it.hasNext())
											{
												obj=it.next();
												count++;
												/*
												if(count<=(Page-1)*pagecount)
												{
													continue;
												}
												
												if(count>(Page)*pagecount)
												{
													break;
												}
												*/
												int id=obj.getId();
												boolean hasState=false;
									  %>
									<tr class="odd" >
										<td class=" sorting_1"><%=id%></td>
										<td class="center "><%=obj.getOrderid() %></td>
										<td class="center "><%=obj.getTrade_id() %></td>
										<td class="center "><%=obj.getRefundid() %></td>
										<td class="center "><%=ToolBox.getTimeLongString(obj.getRefundtime()) %></td>
										<td class="center "><%=obj.getTerminalid() %></td>
										<td class="center "><%=obj.getGoodsroadid() %></td>
										<td class="center "><%=obj.getGoodsname() %></td>
										
										<td class="center "><%=obj.getRet_msg() %></td>
										<td class="center "><%=obj.getIssuccess()==1?"成功":"失败" %></td>
										<td class="center "><%=clsConst.TRADE_TYPE_DES[obj.getTrade_type()] %></td>
										
										<td class="center "><%=obj.getOp_user() %></td>
										<td class="center "><%=String.format("%1.2f", obj.getRefund_amount()/100.0) %></td>
										
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
										    		out.println("<span class='waring-label'>您没有可以查看的数据！请确认您的用户账号是否正确！</span>");
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