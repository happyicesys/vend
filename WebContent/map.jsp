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
	
	if(!ub.AccessAble(UserBean.FUNID_CAN_VIEW_VENDER_MAP))
	{
		request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_VIEW_VENDER_MAP]);
		request.setAttribute("LAST_URL", "index.jsp");
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
	}
    
	int id=ToolBox.filterInt(request.getParameter("id"));
	session.setAttribute("currentpage", request.getRequestURI());

	
	int[] vid= ToolBox.StringToIntArray(ub.getCanAccessSellerid());
	
	if(vid==null)
	{
		request.setAttribute("message", ToolBox.CANNTACCESS);
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
	}
	
		ArrayList<VenderBean> lst=SqlADO.getVenderListByIdLimint(ub.getVenderLimite());
		VenderBean vb=null;
	    if(id<=0)
	    {
	    	id=vid[0];
	    }
	    
	    if(id<=0)
	    {
    		request.setAttribute("message", ToolBox.CANNTACCESS);
    		request.getRequestDispatcher("message.jsp").forward(request, response);
    		return;
	    }
	    
	    if(lst==null)
	    {
    		request.setAttribute("message", ToolBox.CANNTACCESS);
    		request.getRequestDispatcher("message.jsp").forward(request, response);
    		return;
	    }

    	if(id>0)
    	{
        	
    		vb=SqlADO.getVenderBeanByid(id);
    		
    		if(vb!=null)
    		{
        	%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
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
    <link href="./images/adminstyle.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="./css/styles.css" />
<script type="text/javascript" src="js/jquery-1.7.js"></script>
<script type="text/javascript" src="js/jquery.blockUI.js"></script>
<title><%=ToolBox.WEB_NAME%></title>
<style type="text/css">
.BMapLabel{
	max-width: 200px;
}
</style>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.3"></script>
<script type="text/javascript" src="http://dev.baidu.com/wiki/static/map/API/examples/script/convertor.js"></script>

<script type="text/javascript"> 
		var map;            // 创建Map实例
		var last_div="li<%=id%>";
		function initialize() 
		{
			var point;
			var marker;
			var label;
			map= new BMap.Map("container")
			
			<%for(VenderBean vobj: lst)
			{
				if(vobj.getId()!=id)
				{
					%>
					point = new BMap.Point(<%=vobj.getJindu()%>, <%=vobj.getWeidu()%>);  
					marker = new BMap.Marker(point);  // 创建标注
					map.addOverlay(marker);              // 将标注添加到地图中
					label= new BMap.Label("<%=vobj.getTerminalName()%>:<%=String.format("%03d号售货机",vobj.getId())%><BR/>地址:<%= vobj.getTerminalAddress()%>",{offset:new BMap.Size(20,-10)});
					marker.setLabel(label);
					map.addOverlay(marker);
					<%
				}
			}
			%>
			
			point = new BMap.Point(<%=vb.getJindu()%>, <%=vb.getWeidu()%>);    // 创建点坐标
			map.centerAndZoom(point,18);                     // 初始化地图,设置中心点坐标和地图级别。
			marker = new BMap.Marker(point);  // 创建标注
			map.addOverlay(marker);              // 将标注添加到地图中
		
			label = new BMap.Label("<%=vb.getTerminalName()%>:<%=String.format("%03d号售货机",id) %><BR/>地址:<%= vb.getTerminalAddress()%>",{offset:new BMap.Size(20,-10)});
			marker.setLabel(label);
			map.addOverlay(marker);
			map.addControl(new BMap.NavigationControl());  
			map.addControl(new BMap.ScaleControl());                    // 添加比例尺控件
			map.addControl(new BMap.OverviewMapControl());              //添加缩略地图控件
		}
		
		$(document).ready(function(){
			  $("#chk_repeat").click(function(){
			  htmlobj=$.ajax({url:"./ChkVenderRepeat?action=0&vid="+ $("#vid").val() +"&"+Math.random(),async:false});
			  $("#tips").html(htmlobj.responseText);
			  });
			});
		
		var ShowThisVendOnMap=function(vid,lng,lat)
		{
			var point=new BMap.Point(lng, lat);
			map.centerAndZoom(point,18);
			
			document.getElementById(last_div).style.background="#43A1DA";
			last_div="li"+vid;
			document.getElementById(last_div).style.background="#73A839";
		}
</script>
</head>
<body onload="initialize()" style="background-color: #fff;">
	<div class="breadcrumbs" id="breadcrumbs">
						<ul class="breadcrumb">
							<li>
								<span class="glyphicon glyphicon-home"></span>
								<a href="MainHome.jsp" target="main" style="padding-left:5px;margin-left:5px;">首页</a>
							</li>

							<li>
								<a href="#">设备管理</a>
							</li>
							<li class="active">地图信息</li>
						</ul><!-- .breadcrumb -->

						<!-- #nav-search -->
					</div>
	<div class="row">
		<div style="width:74%; height:800px; border: 2px solid #eee; float:right;margin:5px; "	 id="container">
		
		</div>
		<div style="width:260px; height:800px; float:left;overflow: auto; text-align: center;float:left;">
			<%
			for(VenderBean vobj:lst)
			{ 
				if(vobj==null)
				{
					continue;
				}
			%>
					<button id='li<%=vobj.getId() %>' style="width: 200px;margin: 2px;"  class="<%=((vobj.getId()==id)?"green_btn":"blue_btn")%>" onclick="ShowThisVendOnMap(<%=vobj.getId()+","+vobj.getJindu()+","+vobj.getWeidu()%>)"><%=vobj.getId()%>号售货机</button>
			<%}%>
		</div>
	</div>
</body>
</html>
<%
    		}else
    		{
        		request.setAttribute("message", "输入参数有误");
        		request.getRequestDispatcher("message.jsp").forward(request, response);
        		return;
    		}
    	}else
    	{
    		request.setAttribute("message","输入参数有误");
    		request.getRequestDispatcher("message.jsp").forward(request, response);
    		return;
    	}
%>