<%@page import="beans.clsGroupBean"%>
<%@page import="java.util.ListIterator"%>
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
	
	
	if(!ub.AccessAble(UserBean.FUNID_CAN_VIEW_GROUP_ID))
	{
		request.setAttribute("message", "Unable to "+UserBean.RIGHT_DES[UserBean.FUNID_CAN_VIEW_GROUP_ID]);
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
	}
   	ArrayList<clsGroupBean> grouplst=clsGroupBean.getGroupLst();
  	int RsCount=0;
  	int pagecount=ub.getPagecount();
 	int Page=ToolBox.filterInt(request.getParameter("page"));
 	if(Page==0) 
 	{
 		Page=1;
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
    <script type="text/javascript" src="./jquery_ui/js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="./jquery_ui/js/jquery.showLoading.min.js"></script>
    <script type="text/javascript" src="./jquery_ui/js/jquery.ui.datepicker-zh-TW.js"></script>
    
    <script type="text/javascript">
    var Delete=function(groupid)
    {
    	/*提交Binding数据到后台*/
    	var delobj=new Object();
    	delobj.groupid=groupid;
    	$("body").showLoading(); 
        $.ajax({
            type: "POST",
            url: "./DelGroup",
            data:delobj,
            success: (function(obj){
                $("body").hideLoading();  
                if(obj.msg==1)
                {
                	alert(obj.detail);
                }
                else
                {
                	var tr = document.getElementById("tr"+groupid);//获取当前选择的行 
                	tr.parentNode.removeChild(tr);			   //引用该行的父元素．然后删除该行．
                }
                
                }),
            dataType: "json",
            error:(function(obj,txtmes,txtmes2){
                $("body").hideLoading();  
                alert("Fail to delete"+obj.status+" Error");
                })
        		
          });
    };	
    
    </script>
    
</head>
<body style="background-color: #fff;">
	<div class="breadcrumbs" id="breadcrumbs" style="margin-top:5px;">
		<ul class="breadcrumb">
			<li>
				<span class="glyphicon glyphicon-home"></span>
				<a href="MainHome.jsp" target="main" style="padding-left:5px;margin-left:5px;">Home</a>
			</li>

			<li>
				<a href="#">Admin Management</a>
			</li>
			<li class="active">Profile Management</li>
		</ul>
	</div>
	<div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
			  <div class="row">
					<div class="col-xs-12">
						<div class="dataTables_length" id="dataTables-example_length">
							<form class="form-horizontal" role="form">
									<%if(ub.AccessAble(UserBean.FUNID_CAN_CREATE_GROUP_ID)) 
									{%>
								<button type="button" class="btn btn-default" style="background-color:#f4f4f4;float:right;" onclick="javascript:location.href='AddGroup.jsp';" >Add Profile</button>
								<%} %>
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
						<form action="SellerUpdate" method="post">
							<table class="table table-bordered table-hover" style="overflow-y:auto; width:100%;height:100px;border-spacing: 0px;">
								<thead>
									<tr role="row" style="background-color: #f5f5f5;">
										<th style="width: 100px;">ID</th>
										<th style="width: 100px;">Profile Name</th>
										<th style="width: 100px;">Profile Desc</th>
										<th style="width: 100px;">Created At</th>
										<th style="width: 100px;">Updated At</th>
										<th style="width: 100px;">Wechat Merchant ID</th>
										<th style="width: 100px;">Alipay AppID</th>
										
										<th style="width:180px;">Operation</th>
									</tr>											
								</thead>
								<tbody role="alert" aria-live="polite" aria-relevant="all">
									<% 
										int count=0;
										if(grouplst!=null)
										{
											RsCount=grouplst.size();
											ListIterator<clsGroupBean> it=grouplst.listIterator();
											
											clsGroupBean gup;
								
											while(it.hasNext())
											{
												gup=it.next();
												count++;
												if(count<=(Page-1)*pagecount)
												{
													continue;
												}
												
												if(count>(Page)*pagecount)
												{
													break;
												}
								
										%>
											<tr class="odd" id="tr<%=gup.getId() %>">
												<td class="center"><%=gup.getId() %></td>
												<td class="center"><%=gup.getGroupname() %></td>
												<td class="center"><%=gup.getGroupdes() %></td>
												<td class="center"><%=ToolBox.getYMDHMS(gup.getCreattime())%></td>
												<td class="center"><%=ToolBox.getYMDHMS(gup.getUpdatetime())%></td>
												<td class="center"><%=gup.getWx_mch_id()%></td>
												<td class="center"><%=gup.getAl_APP_ID()%></td>
												<td class="center ">

												<%if(ub.AccessAble(UserBean.FUNID_CAN_CHANGE_GROUP_ID))
												{%> 
												<a class="btn btn-success" href="GroupManager.jsp?groupid=<%=gup.getId()%>">
													<i class="glyphicon glyphicon-zoom-in icon-white"></i>
													Edit
												</a>
												<%} %>
												
												<%if(ub.AccessAble(UserBean.FUNID_CAN_DEL_GROUP_ID))
												{%> 
												<a class="btn btn-info" onclick="Delete(<%=gup.getId() %>);" href="javascript:void(0);">
													<i class="glyphicon glyphicon-edit icon-white"></i>
													Delete
												</a>
												<%} %>
												</td>
											</tr>
										<%}
										}%>
										<tr class="odd">
											<td align="center" colspan="8">
											<%
										    	if(RsCount>0) 
										    	{
										    		out.println(ToolBox.getpages(null, "#999", Page, pagecount, RsCount));
												 }
										    	else 
										    	{
										    		out.println("<span class='waring-label'>Profile List Not Available!</span>");
										    	}
										    			
										  	%>
											</td>
										</tr>
								</tbody>
							</table>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>