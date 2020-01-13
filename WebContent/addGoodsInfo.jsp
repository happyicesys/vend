<%@page import="beans.UserBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.tools.ToolBox"%>

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
    
    if(!ub.AccessAble(UserBean.FUNID_CAN_ADD_GOODS))
    {
    	request.setAttribute("message", "Unable to "+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ADD_GOODS]);
    	request.getRequestDispatcher("message.jsp").forward(request, response);
    	return;
    }
    
	session.setAttribute("currentpage", request.getRequestURI());
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta name="description" content=""/>
    <meta name="author" content=""/>
    <!-- Bootstrap Core CSS -->
    <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet"/>
    <!-- MetisMenu CSS -->
    <link href="css/bootstrap/metisMenu.min.css" rel="stylesheet"/>
    <!-- Timeline CSS -->
    <link href="css/bootstrap/timeline.css" rel="stylesheet"/>
    <!-- Custom CSS -->
    <link href="css/bootstrap/admin.css" rel="stylesheet"/>
    <!-- Morris Charts CSS -->
    <link href="css/bootstrap/morris.css" rel="stylesheet"/>
    <!-- Custom Fonts -->
    <link href="css/bootstrap/font-awesome.min.css" rel="stylesheet" type="text/css"/>
    <!-- <link href="./images/adminstyle.css" rel="stylesheet" type="text/css" /> -->
    <link href="./jquery_ui/css/cupertino/jquery-ui.min.css" rel="stylesheet" type="text/css" />
    <link href="./jquery_ui/css/showLoading.css" rel="stylesheet" type="text/css" />
    <script  language="javascript"  type="text/javascript" src="./jquery_ui/js/jquery-1.9.1.js"></script>
    <script language="javascript" type="text/javascript" src="./jquery_ui/js/jquery-ui.min.js"></script>
    <script language="javascript" type="text/javascript" src="./jquery_ui/js/jquery.showLoading.min.js"></script>
    <script language="javascript" type="text/javascript" src="./jquery_ui/js/jquery.ui.datepicker-zh-TW.js"></script>

<script type="text/javascript">
function chkForm()
{
	if(smartForm.goodsname.value=="")
	{
		alert("Product name cannot be empty");
		return false;
	}

	
	if(smartForm.des1.value.length>200)
	{
		alert("Content cannot be more than 200 words");
		return false;
	}
	
	if(smartForm.des2.value.length>200)
	{
		alert("Content cannot be more than 200 words");
		return false;
	}
	if(smartForm.des3.value.length>200)
	{
		alert("Content cannot be more than 200 words");
		return false;
	}
	return true;
}
$(document).ready(function(){
	  $("#chk_repeat").click(function(){
	  htmlobj=$.ajax({url:"./ChkVenderRepeat?action=2&goodsname="+ $("#goodsname").val() +"&"+Math.random(),async:false});
	  $("#tips_goodsname").html(htmlobj.responseText);
	  });
	});

</script>
</head>
<body style="background-color: #fff;">
			<div class="breadcrumbs" id="breadcrumbs" style="margin-top:5px;">
						<ul class="breadcrumb">
							<li>
								<!-- <i class="icon-home home-icon"></i> -->
								<span class="glyphicon glyphicon-home"></span>
								<a href="MainHome.jsp" target="main" style="padding-left:5px;margin-left:5px;">Home</a>
							</li>

							<li>
								<a href="#">Setting Management</a>
							</li>
							<li class="active">Add Product Info</li>
						</ul><!-- .breadcrumb -->

						<!-- #nav-search -->
					</div>
			  <form class="form-horizontal" style="height:700px;" action="./UpLoadGoodsInfo" name="smartForm" method="post" onsubmit="return chkForm();" >
			  	<div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Product Name</label>
				    <div class="col-sm-3">
				      <input type="text" name="goodsname" id="goodsname" class="form-control input-sm" placeholder=""/>
				    </div>
				    <div class="col-sm-5">
				      <input class="btn btn-success" type="button" value="Check Product Duplication" id="chk_repeat" name="chk_repeat" />
				      <span id="tips" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Product Photo</label>
				    <div class="col-sm-3">
				    	<iframe src="uploadfile.jsp" frameborder="0" height="30px" width="500px"></iframe>
				      	<input name="pic1" type="hidden" value="" id="pic1" class="form-control input-sm input-sm"  placeholder=""/>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Ref Price</label>
				    <div class="col-sm-3">
				      <input name="price" type="text" class="form-control input-sm input-sm"  placeholder=""/>
					</div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">Product Desc</label>
				    <div class="col-sm-4">
				      <textarea class="form-control" name="des1" cols="50" rows="10"></textarea>
				    </div>
				    <div class="col-sm-3">
				      <span>Content cannot be more than 200 words</span>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="col-sm-offset-4 col-sm-3">
				      <button type="submit" class="btn btn-primary" value="Add">Add</button>
				      <button type="reset" class="btn btn-primary" value="Back" onclick="javascript:history.go(-1)">Back</button>
				    </div>
				  </div>
				  </div>
				</form>
</body>
</html>