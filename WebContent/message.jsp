<%@page import="com.tools.ToolBox"%>
<%@page import="beans.UserBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
	<link rel="stylesheet" type="text/css" href="css/bootstrap/bootstrap-ie6.css">
	<![endif]-->
	<!--[if lte IE 7]>
	<link rel="stylesheet" type="text/css" href="css/bootstrap/ie.css">
	<![endif]-->
	<!--[if IE]>
	<link rel="stylesheet" type="text/css" href="css/bootstrap/jr.css">
	<![endif]-->
    <script src="js/bootstrap/jquery-1.12.0.min.js" type="text/javascript"></script>
    <script language="javascript" type="text/javascript" src="./jquery_ui/js/jquery-ui.min.js"></script>
    <script language="javascript" type="text/javascript" src="./jquery_ui/js/jquery.showLoading.min.js"></script>
    <script language="javascript" type="text/javascript" src="./jquery_ui/js/jquery.ui.datepicker-zh-TW.js"></script>
    <style>
    #myModal
    {
        top:200px;
    }
</style>

<title>Notification</title>
</head>

<%
	Object obj=request.getAttribute("LAST_URL");
	String returl=null;
	if(obj!=null)
	{
		returl=obj.toString();
	}
	if(returl==null)
	{
		returl=request.getHeader("REFERER");

	}
	if(returl==null)
	{
		returl="index.jsp";
	}
	
	if(returl.endsWith("admin.jsp")||returl.endsWith("top.jsp")||returl.endsWith("menu.jsp"))
	{
		returl=ToolBox.HOMEPAGE;
	}
	System.out.print(returl);
%>

<body height="700px;">
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
           <!--  <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">×
            </button> -->
            <h4 class="modal-title" id="myModalLabel">
            	User Access Level
            </h4>
         </div>
         <div class="modal-body" id="alertcontent">
            <p>${requestScope.message}</p>
         </div>
         <div class="modal-footer">
            <button type="button" class="btn btn-default" 
               data-dismiss="modal" onclick="top.location.href='index.jsp';">Sign In
            </button>
            <%if(!(returl.equals("index.jsp")||(returl.endsWith("index.jsp"))))
				  {%>
			  <button type="button" class="btn btn-default" 
               data-dismiss="modal" onclick="location.href='<%=returl%>';">Back</button>
			<%} %>
         </div>
      </div><!-- /.modal-content -->
   </div><!-- /.modal-dialog -->
</div>
    <script src="js/bootstrap/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="js/bootstrap/metisMenu.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="js/bootstrap/admin.js"></script>

    <!-- Page-Level Demo Scripts - Tables - Use for reference -->
    <script>
    $(document).ready(function() {
    	$('#myModal').modal('show');
    	
    });
    </script>
    <script language="javascript" type="text/javascript">  
  
        function Button_onclick(concent) {
        	alert(concent);
        	 if(window.parent.length>0)
                 window.parent.location=concent;
  
        }  
  
    </script> 
</body>
</html>