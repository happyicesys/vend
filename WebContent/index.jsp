<%@page import="com.tools.ToolBox"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.tools.ToolBox"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title><%=ToolBox.WEB_NAME%></title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
    <meta http-equiv="X-UA-Compatible" content="IE=9" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
    <meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template."/>
    <meta name="author" content="Muhammad Usman"/>

    <!-- The styles--> 
    <link id="bs-css" href="css/bootstrap/bootstrap-cerulean.min.css" rel="stylesheet"/>
    <link href="css/bootstrap/charisma-app.css" rel="stylesheet"/>
	<!--[if lte IE 6]>
	<link rel="stylesheet" type="text/css" href="/css/bootstrap/bootstrap-ie6.css">
	<![endif]-->
	<!--[if lte IE 7]>
	<link rel="stylesheet" type="text/css" href="/css/bootstrap/ie.css">
	<![endif]-->
    <!-- jQuery -->
    <script src="js/bootstrap/jquery-1.12.0.min.js" type="text/javascript"></script>
	<!--[if lte IE 6]>
	  <script type="text/javascript" src="js/bootstrap/bootstrap-ie.js"></script>
	  <![endif]-->
	<!--[if lt IE 9]>
	  <script src="js/bootstrap/html5shiv.min.js"></script>
	  <script src="js/bootstrap/respond.min.js"></script>
	<![endif]-->

	<script type="text/javascript">
		(function ($) {
		  $(document).ready(function() {
		    if ($.isFunction($.bootstrapIE6)) $.bootstrapIE6($(document));
		  });
		})(jQuery);
			function check()
			{
			    if (document.userform.username.value == "")
			    {
			    alert("请填写用户名！");
			    document.userform.username.focus();
			    return (false);
			    }
				if ((document.userform.username.value.length < 2 )||(document.userform.username.value.length >20 ))
			    {
			    alert("请正确填写用户名！");
			    document.userform.username.focus();
			    return (false);
			    }
			
			    if (document.userform.password.value == "")
			    {
			    alert("请填写密码！");
			    document.userform.password.focus();
			    return (false);
			    }
				
				if ((document.userform.password.value.length < 6 )||(document.userform.password.value.length >20 ))
			    {
			    alert("请正确填写密码！");
			    document.userform.password.focus();
			    return (false);
			    }
			}
			function refreshCode(){
	       		document.getElementById("code").src="RandomCode?qs="+Math.random();
	        }
		</script>
</head>

<body style="overflow:scroll;overflow-x:hidden">
<div class="ch-container">
    <div class="row">
        
    <div class="row">
        <div class="col-md-12 center login-header">
            <h2><%=ToolBox.VERSTRING %></h2>
        </div>
        <!--/span-->
    </div><!--/row-->

    <div class="row">
        <div class="well col-md-5 center login-box">
            <div class="alert alert-info">
                请输入用户名和密码！
            </div>
            <form class="form-horizontal" action="./CheckLogin" method="post" name="userform" onSubmit="return check()">
                <fieldset>
                    <div class="input-group input-group-lg">
                        <span class="input-group-addon"><i class="glyphicon glyphicon-user red"></i></span>
                        <input type="text" class="form-control" placeholder="用户名" name="username" id="username" value=""/>
                    </div>
                    <div class="clearfix"></div><br>

                    <div class="input-group input-group-lg">
                        <span class="input-group-addon"><i class="glyphicon glyphicon-lock red"></i></span>
                        <input type="password" class="form-control" placeholder="密码" name="password" id="password" type="password" value=""/>
                    </div>
                    <div class="clearfix"></div>
					
                    <div class="clearfix"></div>

                    <p class="center col-md-5">
                        <button type="submit" class="btn btn-primary">登陆</button>
                    </p>
                </fieldset>
            </form>
			
        </div>
        <!--/span-->
	<div style="text-align:center;font-size:24px">
	<p style="color:red;">部分管理员的微信支付宝信息还没有健全,请赶紧健全,否则上月转款无法触发,会累积到下一月</p>
	<p>由于微信和支付宝结算款全部进入我司公帐,并且金额较大,所以我司无法避税,现在统一调整扣除费率为3%(税点)+0.6%(微信支付宝手续费)</p>

	<p>自动退款功能已经开放，如有需要请在[售货机详情]里面开启自动退款功能，每台机器需要单独设置</p>
	<p>符合自动退款的条件是：1、扫码后4分钟内不出货；2、出货有故障</p>
	</div>	
    </div><!--/row-->
	
</div><!--/fluid-row-->

</div><!--/.fluid-container-->

<!-- external javascript -->
<p class="center" style="font-size: 13px;">
			<br>
			Copyright © 2011 WWW.mCUvEND.COM All Rights Reserved 版权所有·WWW.mCUvEND.COM
			<br>
			有些内容可能在IE浏览器上显示不全,推荐使用chrome或火狐等其他内核的浏览器,给您带来不便，敬请谅解！
		</p>


</body>
</html>