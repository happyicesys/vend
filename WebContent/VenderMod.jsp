<%@page import="beans.clsGroupBean"%>
<%@page import="com.clsEvent"%>
<%@ page import="beans.VenderBean"%>
<%@ page import="beans.UserBean"%>
<%@ page import="com.ado.SqlADO"%>
<%@ page import="com.tools.ToolBox"%>
<%@ page import="java.util.ArrayList"%>
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

if(!ub.AccessAble(UserBean.FUNID_CAN_UPDATE_VENDER))
{
	request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_UPDATE_VENDER]);
	request.getRequestDispatcher("message.jsp").forward(request, response);
	return;
}

session.setAttribute("currentpage", request.getRequestURI()+"?"+request.getQueryString());



int id=ToolBox.filterInt(request.getParameter("mid"));


if(!ub.CanAccessSeller(id))
{
	request.setAttribute("message", ToolBox.CANNTACCESS);
	request.getRequestDispatcher("message.jsp").forward(request, response);
	return;
}


	if(id>0)
	{
    	VenderBean vb=null;
		vb=SqlADO.getVenderBeanByid(id);
		if(vb!=null)
		{
			clsGroupBean groupBean =clsGroupBean.getGroup(vb.getGroupid());
			
%>
<!DOCTYPE>
<html>
<head>
<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
    <meta http-equiv="X-UA-Compatible" content="IE=8" />
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <!-- Bootstrap Core CSS -->
    <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet">
    <!-- MetisMenu CSS -->
    <link href="css/bootstrap/metisMenu.min.css" rel="stylesheet">
    <!-- Timeline CSS -->
    <link href="css/bootstrap/timeline.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="css/bootstrap/admin.css" rel="stylesheet">
    <!-- Morris Charts CSS -->
    <link href="css/bootstrap/morris.css" rel="stylesheet">
    <!-- Custom Fonts -->
    <link href="css/bootstrap/font-awesome.min.css" rel="stylesheet" type="text/css">
    <!-- <link href="./images/adminstyle.css" rel="stylesheet" type="text/css" /> -->
    <link href="./jquery_ui/css/cupertino/jquery-ui.min.css" rel="stylesheet" type="text/css" />
    <link href="./jquery_ui/css/showLoading.css" rel="stylesheet" type="text/css" />
    <script  language="javascript"  type="text/javascript" src="./jquery_ui/js/jquery-1.9.1.js"></script>
    <script language="javascript" type="text/javascript" src="./jquery_ui/js/jquery-ui.min.js"></script>
    <script language="javascript" type="text/javascript" src="./jquery_ui/js/jquery.showLoading.min.js"></script>
    <script language="javascript" type="text/javascript" src="./jquery_ui/js/jquery.ui.datepicker-zh-TW.js"></script>
    <script type="text/javascript" src="js/jquery.blockUI.js"></script>
    <script type="text/javascript">
var ShowMapWin=function()
{
	window.open('selectpoint.html', 'SelectPoint', 'height=800, width=600, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=no,location=n o, status=no');
}



</script>
<title>查看/修改 售货机--<%=id%></title>
</head>
<body style="background-color: #fff;">
	 <div class="breadcrumbs" id="breadcrumbs" style="margin-top:5px;">
						<ul class="breadcrumb">
							<li>
								<span class="glyphicon glyphicon-home"></span>
								<a href="MainHome.jsp" target="main" style="padding-left:5px;margin-left:5px;">首页</a>
							</li>

							<li>
								<a href="#">设备管理</a>
							</li>
							<li class="active">查看/修改<%=String.format("%03d",id)%>售货机</li>
						</ul><!-- .breadcrumb -->

						<!-- #nav-search -->
					</div>
			  	<form class="form-horizontal" role="form" action="SellerUpdate" method="post">
				  <div class="form-group">
				    <label class="col-sm-4 control-label">终端名称</label>
				    <div class="col-sm-3">
				      <input name="tname" type="text" class="form-control input-sm input-sm"  placeholder="终端名称(必填)" value="<%=vb.getTerminalName()%>">
				      <span id="tips_t" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">上线时间</label>
				    <div class="col-sm-3">
				      <%=ToolBox.getYMDHMS(vb.getBTime())%>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">更新时间</label>
				    <div class="col-sm-3">
				      <%=ToolBox.getYMDHMS(vb.getUpdateTime())%>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">货道数量</label>
				    <div class="col-sm-3">
				      <input name="portcount" id="portcount" readonly="readonly" value="<%=vb.getGoodsPortCount() %>" type="text" class="form-control input-sm input-sm"  placeholder="货道数量(必填)">
					</div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">活动编号</label>
				    <div class="col-sm-3">
				      <input name="huodongid" readonly="readonly" value="<%=vb.getHuodongId() %>" type="text" class="form-control input-sm input-sm">
				    </div>
				  </div>
				  <div class="form-group" style="border: medium  rgb(250,0,255)">
				    <label class="col-sm-4 control-label">售货机型号</label>
				    <div class="col-sm-3">
				      <input name="sellertype" type="text" class="form-control input-sm"  placeholder="售货机型号" value="<%=vb.getSellerTyp() %>"/>
				    </div>
				    
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">服务电话</label>
				    <div class="col-sm-3">
				      <input name="server_tel"  type="text" class="form-control input-sm" value="<%=vb.getTelNum() %>" placeholder="终端名称">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">转账对应账号</label>
				    <div class="col-sm-3">
				      <%
				      UserBean ubobj= UserBean.getUserBeanById(vb.getAdminId());
				      if(ubobj!=null)
				      {
				    	  out.print(ubobj.getAdminusername() + "-"+ubobj.getAdminname());
				      }
				      else
				      {
				    	  out.print("没有绑定转账账号!");
				      }
				      %>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">售货机地址</label>
				    <div class="col-sm-3">
				      <input name="address" type="text" class="form-control input-sm"  placeholder="售货机地址" value="<%=vb.getTerminalAddress() %>"><!-- 添加地址信息，便于地图查找 -->
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">售货机经度</label>
				    <div class="col-sm-3">
				      <input id="lng" name="lng" type="text" class="form-control input-sm"  placeholder="售货机经度" readonly="readonly" value="<%=vb.getJindu() %>">
				    </div>
				    <div class="col-sm-5">
				      	<input class="btn btn-success" type="button" value="在地图上查找位置" onclick="ShowMapWin()"/>
				      </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">售货机纬度</label>
				    <div class="col-sm-3">
				      <input id="lat" name="lat" type="text" class="form-control input-sm" placeholder="售货机纬度" readonly="readonly" value="<%=vb.getWeidu() %>" >
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">温馨提示信息</label>
				    <div class="col-sm-4">
				      <textarea class="form-control" name="tipmes" cols="50" rows="4" ></textarea>
				    </div>
				    <div class="col-sm-3">
				      <span><%=vb.getTipMesOnLcd()%></span>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">是否在线</label>
				    	<div class="col-sm-3">
						    <%=(vb.isIsOnline()?"是":"否")%>
						</div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">允许自动退款</label>
				    	<div class="col-sm-3">
						    <input name="auto_refund" type="checkbox" value="1" <%=(vb.getAuto_refund()==1?"checked=\"checked\"":"")%> />
						</div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">允许人工退款</label>
				    	<div class="col-sm-3">
						    <input name="manual_refund" type="checkbox" value="1" <%=(vb.getManual_refund()==1?"checked=\"checked\"":"")%> />
						</div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">允许下位机更新货道商品</label>
				    	<div class="col-sm-3">
						    <input name="AllowUpdateGoodsByPc" type="checkbox" value="1" <%=(vb.getM_AllowUpdateGoodsByPc()==1?"checked=\"checked\"":"")%> />
						</div>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">集团编号</label>
				    	<div class="col-sm-3">

						    <%=(groupBean==null)?"集团没有归属":String.format("[%d]-%s",groupBean.getId(), groupBean.getGroupname()) %>
						</div>
				  </div>
				  
				  <div class="form-group">
				    <div class="col-sm-offset-4 col-sm-3">
				       <input name="id" type="hidden" value="<%=vb.getId()%>" /> 
				      <button type="submit" class="btn btn-primary" value="修改">修&nbsp;&nbsp;&nbsp;&nbsp;改</button>
				      <button type="reset" class="btn btn-primary" value="取消">取&nbsp;&nbsp;&nbsp;&nbsp;消</button>
				      <button  class="btn btn-primary"  value="返回列表" type="button" onclick="javascript:history.go(-1)" >返回列表</button>
				    </div>
				  </div>
				</form>
</body>
</html>
<%
    		}else
    		{
    			request.setAttribute("message", "参数错误，没有找到该机器！");
    			request.getRequestDispatcher("message.jsp").forward(request, response);
    			return;
    		}
    	}else
    	{
			request.setAttribute("message", "参数错误，没有找到该机器！");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
    	}
%>