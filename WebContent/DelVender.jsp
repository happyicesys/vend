<%@page import="com.tools.StringUtil"%>
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
		session.setAttribute("currentpage", request.getRequestURI());
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
		
		
		if(!ub.AccessAble(UserBean.FUNID_CAN_DELTE_VENDER))
		{
			request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_DELTE_VENDER]);
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
});

var Delete=function(vid)
{
	/*提交绑定数据到后台*/
	var uid= $("#userlist"+vid).val();
	var bindobj=new Object();
	bindobj.vid=vid;
	$("body").showLoading(); 
    $.ajax({
        type: "POST",
        url: "./DelVender",
        data:bindobj,
        success: (function(obj){
            $("body").hideLoading();  
            if(obj.msg==1)
            {
            	$("#retmes"+vid).text(obj.detail);
            }
            else
            {
            	var tr = document.getElementById("tr"+vid);//获取当前选择的行 
            	tr.parentNode.removeChild(tr);			   //引用该行的父元素．然后删除该行．
            }
            
            }),
        dataType: "json",
        error:(function(obj,txtmes,txtmes2){
            $("body").hideLoading();  
            $("#retmes"+vid).text("删除失败！"+obj.status+"错误");
            })
    		
      });
};

</script>
</head>
<%
	int RsCount=0;
  	int pagecount=ub.getPagecount();
 	
 	int Page=ToolBox.filterInt(request.getParameter("pageindex"));

	int count_per_page = ToolBox.filterInt(request.getParameter("count_per_page"));
	
	//ArrayList<VenderBean> lst = (ArrayList<VenderBean>)request.getAttribute("lst");
	
	String id = request.getParameter("id");
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
								<a href="#">设备管理</a>
							</li>
							<li class="active">售货机列表</li>
						</ul>
					</div>
			<div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
			  <div class="row">
					<div class="col-xs-12">
						<div class="dataTables_length" id="dataTables-example_length">
							<form class="form-horizontal" role="form">

							<label>机器编号：</label>
							  <label><input type="search" name="id" value="" class="form-control input-sm" placeholder="" aria-controls="dataTables-example"></label>
							
							<button type="submit" class="btn btn-default" style="background-color:#f4f4f4;">查询</button>
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
							int td_count=10; 
						%>
						<form action="./VenderList" method="post" name="form1" id="form1">
						<div class="table-responsive">
							<table class="table table-bordered table-hover table-condensed" style="overflow-y:auto; width:100%;height:100px;border-spacing: 0px;">
								<thead>
									<tr role="row" style="background-color: #f5f5f5;">
										<th>编号</th>
										<th >选择</th>
										<th >名称</th>
										<th >状态</th>
										<th >转账用户名</th>
										
									</tr>											
								</thead>

								<tbody role="alert" aria-live="polite" aria-relevant="all">
									<%
									
									 	int count=0;
										ArrayList<VenderBean> lst;
										ArrayList<UserBean> user_lst;
										
										
										if(StringUtil.isBlank(id))
										{
									    	lst=SqlADO.getVenderListByIdLimint(ub.getVenderLimite());
										}
										else
										{
											lst=SqlADO.getVenderListByIdLimint(ub.getVenderLimite(),id);
											
										}
										if(lst!=null)
										{
											user_lst=UserBean.getUserBeanListByright();
											RsCount=lst.size();
											ListIterator<VenderBean> it=lst.listIterator();
											//VenderBean obj;
											//int index=0;
											for(VenderBean obj:lst)
											{
												int venderid=obj.getId();
												int MdbDeviceStatus=obj.getMdbDeviceStatus();
												int Function_flg=obj.getFunction_flg();
												//Function_flg|=VenderBean.FUNC_IS_MDB_BILL_VALID|VenderBean.FUNC_IS_MDB_COIN_VALID|VenderBean.FUNC_IS_TERMPER_VALID;
												boolean hasState=false;
												//PrintWriter pw=response.getWriter();
												//System.out.print(obj.getAdminId());
									  %>
									<tr class="odd" id="tr<%=venderid%>">
										<td class=" sorting_1"><%=venderid%></td>
										<td class="center ">
										    <input type="checkbox" name="vendid" value="<%=venderid%>">									  </td>
										<td class="center "><button type="button" class="btn btn-danger btn-sm"><%=obj.getTerminalName() %></button></td>
										<td class="center ">
											<%=obj.isIsOnline()?"在线":"离线" %>
										</td>
										<td class="center ">
										<input  class="btn btn-info" value="删除机器" type="button" onclick="Delete(<%=venderid %>);" />
										</td>
									</tr>
									<% 		
										}
									}
									%>
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