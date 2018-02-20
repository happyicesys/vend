<%@page import="com.clsConst"%>
<%@page import="com.ClsTime"%>
<%@page import="java.util.ListIterator"%><%@ page import="beans.UserBean"%><%@ page import="java.util.ArrayList"%><%@ page import="com.ado.SqlADO"%><%@ page import="com.tools.ToolBox"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
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
	
	
	if(!ub.AccessAble(UserBean.FUNID_CAN_VIEW_USER_LST))
	{
		request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_VIEW_USER_LST]);
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
	}
    
    
   	ArrayList<UserBean> uli=UserBean.getUserBeanListByright();

   	
  	int RsCount=0;
  	int pagecount=ub.getPagecount();
 	int Page=ToolBox.filterInt(request.getParameter("page"));
 	if(Page==0) 
 	{
 		Page=1;
 	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="./lib/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
	<link href="./lib/ligerUI/skins/Gray/css/all.css" rel="stylesheet" type="text/css" />

	<link href="./lib/ligerUI/skins/ligerui-icons.css" rel="stylesheet" type="text/css" />
	<script src="./lib/jquery/jquery-1.3.2.min.js" type="text/javascript"></script>
	<script src="./lib/ligerUI/js/core/base.js" type="text/javascript"></script>
	<script src="./lib/ligerUI/js/plugins/ligerGrid.js" type="text/javascript"></script> 
	<script src="./lib/ligerUI/js/plugins/ligerResizable.js" type="text/javascript"></script>
	<script src="./lib/ligerUI/js/plugins/ligerDrag.js" type="text/javascript"></script> 
	<script src="./lib/ligerUI/js/plugins/ligerDialog.js" type="text/javascript"></script>
	<script src="./lib/ligerUI/js/plugins/ligerToolBar.js" type="text/javascript"></script>
    <script src="./lib/ligerUI/js/plugins/ligerForm.js" type="text/javascript"></script>
    <script src="./lib/ligerUI/js/plugins/ligerDateEditor.js" type="text/javascript"></script>
    <script src="./lib/ligerUI/js/plugins/ligerComboBox.js" type="text/javascript"></script>
    <script src="./lib/ligerUI/js/plugins/ligerCheckBox.js" type="text/javascript"></script>
    <script src="./lib/ligerUI/js/plugins/ligerButton.js" type="text/javascript"></script>
    <script src="./lib/ligerUI/js/plugins/ligerDialog.js" type="text/javascript"></script>
    <script src="./lib/ligerUI/js/plugins/ligerRadio.js" type="text/javascript"></script>
    <script src="./lib/ligerUI/js/plugins/ligerSpinner.js" type="text/javascript"></script>
    <script src="./lib/ligerUI/js/plugins/ligerTextBox.js" type="text/javascript"></script> 
    <script src="./lib/ligerUI/js/plugins/ligerTip.js" type="text/javascript"></script>
    <script src="./lib/jquery-validation/jquery.validate.min.js" type="text/javascript"></script> 
    <script src="./lib/jquery-validation/jquery.metadata.js" type="text/javascript"></script>
    <script src="./lib/jquery-validation/messages_cn.js" type="text/javascript"></script>
   <script src="./js/VenderJs.js" type="text/javascript"></script>
   <script src="./js/json2form.js" type="text/javascript"></script>
	<style>
<!--
	#userinfo{display: none;}
-->

.zhou-label
{
	width:90px;text-align:left;
}
.zhou-space
{
	width: 40px;
}


</style>

    <style type="text/css">
           body{ font-size:12px;}
        .l-table-edit {}
        .l-table-edit-td{ padding:4px;}
        .l-button-submit,.l-button-test{width:80px; float:left; margin-left:5px; margin-right:5px;padding-bottom:2px;}
        .l-verify-tip{ left:230px; top:120px;}
        #errorLabelContainer{ padding:10px; width:99%; border:1px solid #FF4466; display:none; background:#FFEEEE; color:Red;}
    </style>
	
    <script type="text/javascript">
    	var userlist;
    	var userinfowin=null;
    	var mainform;
        $(function()
        {
        	userlist=$("#maingrid4").ligerGrid({
            	title:"管理员信息",
                columns: [
				 {display: '编号',name:'id',width:80},
				 {display: '用户名',name:'adminusername',minWidth: 60,width:150,align:'left'},
				 {display: '姓名',name:'adminname',width:150,align:'left'},
				 {display: '地址',name:'adminaddress',width:200,align:'left'},
				 {display: '手机',name:'adminmobilephone',width:100,align:'left'},
				 {display: '登录IP',name:'lastloginip',width:100,align:'left'},
				 {display: '登录时间',name:'lastLoginTime',width:150}
                ], 
                pageSize: 20,
                rownumbers:true,
                method:'post',
                url: './jsonUserList',
                dataAction: 'local',//本地排序
                width: '100%', height: '98%',
                toolbar: { items: [
                                   { text: '添加用户', click: f_AddUser, icon: 'add' },
                                   { line: true },
                                   { text: '编辑用户', click: f_EditUser, icon: 'modify' },
                                   { line: true },
                                   { text: '删除用户', click: f_DeleteUser, icon: 'busy' }
                                   ]
                         },
                onDblClickRow : function (data, rowindex, rowobj)
                {
                	f_EditUser(data);
                }
            });
        	
        	$("#pageloading").hide();
        });
        
        $(function()
      		{
	             var dataGridColumns = [
	                 { header: '编号', name: 'id', width: 40 },
	                 { header: '名字', name: 'groupname' }
	             ];
	             var ret=getAjaxReturn("./AjaxRequest","activeid=<%=clsConst.ACTION_JSON_GET_GROUPLST%>");
	             
	             eval("var dataGrid="+ret);
	             
	             $("#groupid").ligerComboBox({ data: dataGrid,checkbox:false, textField: 'id', columns: dataGridColumns, selectBoxWidth: 200, isMultiSelect: false
	             });
      		});

        function clrFrom() 
        {
        	$("#uid").val("0");
        	$("#adminusername").val("");
        	$("#groupid").val("<%=ub.getGroupid()%>");
        	$("#adminpassword").val("");
        	$("#repassword").val("");
        	$("#adminname").val("");
        	$("#admintelephone").val("");
        	$("#adminmobilephone").val("");
        	$("#adminsex").val("男");
        	
        	$("#adminaddress").val("");
        	$("#VenderId").val("");
        	$("#adminrights").val("");
        }
        
        function f_AddUser()
        {
	   		 $("#adminpassword").attr("validate","{required:true,minlength:6}");
		     $("#repassword").attr("validate","{required:true,minlength:6}");
		     $("#adminusername").attr("readonly",false);
        	clrFrom();
        	if(!userinfowin)
       		{
        		userinfowin=$.ligerDialog.open({title:'添加管理员',width:650,target:$("#userinfo")});

       		}else
       		{
       			userinfowin.active();
       		}
		}
        function f_EditUser()
        {
        	var selected = userlist.getSelected();
            if (!selected) 
            { 
            	$.ligerDialog.warn("亲,请选择一行");
            	return; 
            }
        	var ret=getAjaxReturn("./jsonUser","activeid=<%= clsConst.ACTION_JSON_GET_USER%>&uid="+selected.id);
        	eval("var jsonRet="+ret);
        	switch(jsonRet.errlevel)
        	{
        	 case <%=clsConst.ERR_LEVEL_NO_ERR%>:
        		 $("#adminpassword").attr("validate","");
        	     $("#repassword").attr("validate","");
        	     $("#adminusername").attr("readonly",true);
        		 clrFrom();
        		 $("#userfrm").json2form({data:jsonRet.datasrc});
             	if(!userinfowin)
           		{
            		userinfowin=$.ligerDialog.open({title:'编辑管理员信息',width:650,target:$("#userinfo")});
           		}else
           		{
           			userinfowin.active();
           		}
        		 break;
        	 case <%=clsConst.ERR_LEVEL_CANT_ACCESS%>:
        		 $.ligerDialog.error(jsonRet.errdes,'禁止访问');
        		 break;
        	 case <%=clsConst.ERR_LEVEL_NO_OPERATION_RIGHT%>:
        		 $.ligerDialog.error(jsonRet.errdes,'禁止操作');
        		 break;
        	 case <%=clsConst.ERR_LEVEL_OPERATION_ERR%>:
        		 $.ligerDialog.error(jsonRet.errdes,'操作错误');
        		 break;
        	}
       	}
        function f_DeleteUser(data)
        {
        	var selected = userlist.getSelected();
            if (!selected) 
            { 
            	$.ligerDialog.warn(tips);
            	return; 
            }
        	$.ligerDialog.confirm('亲，您真的要删除该管理员吗?', 
           			function(yes) 
           			{
           				if(yes)
           				{
           					var ret=getAjaxReturn("./jsonUser","activeid=<%= clsConst.ACTION_JSON_DELETE_USER%>&uid="+selected.id);
           					eval("var jsonRet="+ret);
           		        	switch(jsonRet.errlevel)
           		        	{
           		        	 case <%=clsConst.ERR_LEVEL_NO_ERR%>:
           		        		    userlist.reload();
        			        		var ltips= $.ligerDialog.tip({title: '提示信息',content:'亲,恭喜您删除成功！'});
        		        	 		setTimeout(function(){ltips.close();}, 6000);
           		        		 break;
           		        	 case <%=clsConst.ERR_LEVEL_CANT_ACCESS%>:
           		        		 $.ligerDialog.error(jsonRet.errdes,'禁止访问');
           		        		 break;
           		        	 case <%=clsConst.ERR_LEVEL_NO_OPERATION_RIGHT%>:
           		        		 $.ligerDialog.error(jsonRet.errdes,'禁止操作');
           		        		 break;
           		        	 case <%=clsConst.ERR_LEVEL_OPERATION_ERR%>:
           		        		 $.ligerDialog.error(jsonRet.errdes,'操作错误');
           		        		 break;
           		        	}
           				}
           			});
        	
        }
        


        
        var groupicon = "./lib/ligerUI/skins/icons/communication.gif";
        $(function ()
                {
		        	$.metadata.setType("attr", "validate");
		            //创建表单结构
		            mainform = $("form[name='userfrm']");

                    var validator = $("form[name='userfrm']").validate({
                        errorPlacement: function (lable, element)
                        {

                            if (element.hasClass("l-textarea"))
                            {
                                element.addClass("l-textarea-invalid");
                            }
                            else if (element.hasClass("l-text-field"))
                            {
                                element.parent().addClass("l-text-invalid");
                            }
                            $(element).removeAttr("title").ligerHideTip();
                            $(element).attr("title", lable.html()).ligerTip();
                        },
                        success: function (lable)
                        {
                            var element = $("#" + lable.attr("for"));
                            if (element.hasClass("l-textarea"))
                            {
                                element.removeClass("l-textarea-invalid");
                            }
                            else if (element.hasClass("l-text-field"))
                            {
                                element.parent().removeClass("l-text-invalid");
                            }
                            $(element).removeAttr("title").ligerHideTip();
                        },
                        submitHandler: function ()
                        {
                        	 var data = $("#userfrm").serializeJson(); 
                        	 var ret=getAjaxReturn("./jsonAddUser","userinfo="+encodeURIComponent(liger.toJSON(data)));
                        	 eval("var retJsonValue = "+ret); 
                        	 switch(retJsonValue.errlevel)
                        	{
                        	 case <%=clsConst.ERR_LEVEL_NO_ERR%>:
                             	var ltips= $.ligerDialog.tip({title: '提示信息',content:retJsonValue.errdes});
                           	 	setTimeout(function(){ltips.close();}, 5000);
                           	 	if(userlist)
                           	 	{
                           	 		userlist.reload();
                           	 	}
                           	 	
                        		 break;
                        	 case <%=clsConst.ERR_LEVEL_CANT_ACCESS%>:
                        		 $.ligerDialog.error(retJsonValue.errdes,'禁止访问',function(){
                         	     	if(userinfowin)
                        	     	{
                         	     		userinfowin.hide();
                         	     	}
                        		 });
                        		 break;
                        	 case <%=clsConst.ERR_LEVEL_NO_OPERATION_RIGHT%>:
                        		 $.ligerDialog.error(retJsonValue.errdes,'禁止操作',function(){
                          	     	if(userinfowin)
                        	     	{
                         	     		userinfowin.hide();
                         	     	}
                        		 });
	                 	     	
                        		 break;
                        	 case <%=clsConst.ERR_LEVEL_OPERATION_ERR%>:
                        		 $.ligerDialog.error(retJsonValue.errdes,'操作错误');
                        		 break;
                        	}
                        	 //alert(ret);
                        }
                    }); 
                }
        );
	</script>
</head>
<body style="padding:6px; overflow:hidden;">
<div id="userinfo"  class="liger-form">
	<form id="userfrm" name="userfrm" class="liger-form" autocomplete="off">
		<table width="100%" border="0" class="l-table-edit">
			<tr>
				<input type="hidden" value="0" id="uid" name="uid" />
				<td align="left" class="l-table-edit-td" width="20%">用户名:</td>
				<td align="left" class="l-table-edit-td">
				<input  value="" name="adminusername" type="text" id="adminusername" class="l-text" validate="{required:true,minlength:6}" /> 
				</td><td width="400"><input class="l-button" type="button" style="width: 150px;" value="检查用户名是否可以使用" id="chkname" />		</td>
				
			</tr>
			
			<tr>
				<td align="left" class="l-table-edit-td">集团:</td>
				<td align="left" class="l-table-edit-td">
					<input readonly="readonly" name="groupid" type="text" id="groupid" class="l-text"  />
				</td>
				<td></td>
			</tr>

			<tr>
				<td align="left" class="l-table-edit-td">密码:</td>
				<td  align="left" class="l-table-edit-td" >
					<input value="" type="password" id="adminpassword" name="adminpassword" validate="{required:true,minlength:6}"  type="text" class="l-text"/></td>
				<td></td>
			</tr>

			<tr>
				<td align="left" class="l-table-edit-td">重复密码:</td>
				<td align="left" class="l-table-edit-td">
					<input  value="" type="password" id="repassword" name="repassword" validate="{required:true,minlength:6}"  type="text"  class="l-text"/></td>
				<td></td>
			</tr>
			
			<tr>
				<td align="left" class="l-table-edit-td">管理员姓名:</td>
				<td align="left" class="l-table-edit-td">
				<input id="adminname" name="adminname" type="text" class="l-text" />
				</td>
				<td></td>
			</tr>
			<tr class="odd">
				<td align="left" class="l-table-edit-td">固定电话:</td>
				<td align="left" class="l-table-edit-td">
					<input validate="{digits:true,minlength:8,maxlength:15}" id="admintelephone" name="admintelephone" type="text" class="l-text"/> 
				</td>
				<td></td>
			</tr>
			<tr>
				<td align="left" class="l-table-edit-td">移动电话:</td>
				<td align="left" class="l-table-edit-td">
				<input validate="{digits:true,minlength:8,maxlength:15}" id="adminmobilephone" name="adminmobilephone" type="text" class="l-text"/> 
				</td>
				<td></td>
			</tr>
			
			<tr>
				<td align="left" class="l-table-edit-td">性别:</td>
				<td align="left" class="l-table-edit-td">
				<select name="adminsex" id="adminsex">
					<option>男</option>
					<option>女</option>
				</select>
				</td>
				<td></td>
			</tr>

			<tr>
				<td align="left" class="l-table-edit-td">地址:</td>
				<td align="left" class="l-table-edit-td" colspan="2">
				<input id="adminaddress" name="adminaddress" type="text"  class="l-text" style="width: 400px;" />
				</td>
			</tr>
			
			<tr>
				<td align="left" class="l-table-edit-td">用户权限:</td>
				<td align="left" class="l-table-edit-td">
					<input type="hidden" value="" id="adminrights" name="adminrights" />
					<input class="l-button" type="button" value="设置权限" id="adddevice" />			
				</td>
				<td></td>
			</tr>
			
			<tr>
				<td align="left" class="l-table-edit-td">售货机分配:</td>
				<td align="left" class="l-table-edit-td">
					<input type="hidden" value="" id="VenderId" name="VenderId" />
					<input class="l-button" type="button" value="添加机器" id="adddevice" />
				</td>
				<td></td>
			</tr>
			
			<tr>
				<td align="left" class="l-table-edit-td">创建时间:</td>
				<td align="left" class="l-table-edit-td">
					<input id="createtime" name="createtime" readonly="readonly" value="<%=ToolBox.getDateTimeString() %>" type="text" class="l-text"/>
				</td>
				<td></td>
			</tr>
			
			<tr>
				<td align="left" class="l-table-edit-td">上次登录时间:</td>
				<td align="left" class="l-table-edit-td">
					<input id="lastlogintime" name="lastlogintime" readonly="readonly" value="<%=ToolBox.getDateTimeString() %>" type="text" class="l-text"/>
				</td>
				<td></td>
			</tr>
			<tr>
				<td align="left" class="l-table-edit-td">上次登录IP:</td>
				<td align="left" class="l-table-edit-td">
					<input ip="lastloginip" name="lastloginip" readonly="readonly" value="0.0.0.0" type="text" class="l-text"/>
				</td>
				<td></td>
			</tr>
			<tr>
				<td align="left" class="l-table-edit-td"></td>
				<td align="left" class="l-table-edit-td" colspan="2">
					<input value="提交" type="submit" class="l-button l-button-submit"/>
				</td>
			</tr>
		</table>
	</form>
</div>


    <div id="maingrid4" style="margin:0; padding:0"></div>
  <div style="display:none;">
  <!-- g data total ttt -->
</div>
</body>
</html>