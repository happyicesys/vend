

<%@page import="beans.UserBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.tools.ToolBox"%>

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
    
    if(!ub.AccessAble(UserBean.FUNID_CAN_ADD_GOODS))
    {
    	request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ADD_GOODS]);
    	request.getRequestDispatcher("message.jsp").forward(request, response);
    	return;
    }
    
    
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="./images/adminstyle.css" rel="stylesheet" type="text/css" />
<link href="./css/styles.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-1.7.js"></script>
<script type="text/javascript" src="js/jquery.blockUI.js"></script>

<script type="text/javascript">
function chkForm()
{
	if(smartForm.goodsname.value=="")
	{
		alert("产品名称不能为空");
		return false;
	}
	if(smartForm.pic1.value=="")
	{
		alert("产品图片不能为空");
		return false;
	}
	
	if(smartForm.des1.value.length>200)
	{
		alert("描述内容不能超过200个字符");
		return false;
	}
	
	if(smartForm.des2.value.length>200)
	{
		alert("描述内容不能超过200个字符");
		return false;
	}
	if(smartForm.des3.value.length>200)
	{
		alert("描述内容不能超过200个字符");
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
<body>

<br/>
	<form action="./UpLoadGoodsInfo" name="smartForm" method="post"	onsubmit="return chkForm();">
<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr class="Notodd"><td><table border="0" cellspacing="0" cellpadding="0"><tr class="Notodd">

</tr></table>
</td>
	<td>

		<table border="0" align="right" cellpadding="0" cellspacing="0">

			<tr>
			<td>
				&thinsp;
			</td>
			</tr>

		</table>	
			
	</td>
	</tr>
</table>



		<table width="80%" border="0" align="center" cellpadding="4" cellspacing="1" bgcolor="#CCCCCC">
		  <tr class="odd">
		    <td colspan="2" align="center" class="Main_Title">添加产品信息</td>
		  </tr>
		  
			<tr class="odd">
				<td width="30%" style="text-align:right;">产品名称</td>
				<td class="left"><input class="white-text" name="goodsname" id="goodsname" type="text" value="" size="50" />
				建议不超过8个汉字
				<input class="green_btn" type="button" value="检测产品是否重复" id="chk_repeat" name="chk_repeat" />
				<span id="tips_goodsname" style="color: red;"></span>
				</td>
			</tr>
			<tr class="odd">
				<td style="text-align:right;">产品图片</td>
				<td class="left" style="vertical-align: middle;"><iframe src="uploadfile.jsp" frameborder="0" height="30px" width="500px"></iframe>
					<input name="pic1" type="hidden" value="" id="pic1" /></td>
			</tr>
			<tr class="odd">
				<td style="text-align:right;">参考价格</td>
				<td class="left"><input class="white-text" name="price" type="text" /></td>
			</tr>
			<tr class="odd">
				<td style="text-align:right;">产品描述</td>
				<td class="left"><textarea  class="white-text" style="height: 100px;" name="des1" cols="50" ></textarea>描述内容不能超过200个字符</td>
			</tr>
			<tr class="odd">
				<td colspan="2" align="center">
					<input class="blue_btn" type="submit" value="提交" />
					<input class="blue_btn" type="reset" value="取消" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>