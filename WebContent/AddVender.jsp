<%@page import="com.clsConst"%>
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
	
	if(!ub.AccessAble(UserBean.FUNID_CAN_ADD_VENDER))
	{
		request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ADD_VENDER]);
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
	}
	session.setAttribute("currentpage", request.getRequestURI());
	int[] valid_slot_id=null;
	if(clsConst.SLOT_FORMAT_HEX.equals("%X"))
	{
		valid_slot_id=new int[]{  0xA0,0xA1,0xA2,0xA3,0xA4,0xA5,0xA6,0xA7,0xA8,0xA9,
										0xB0,0xB1,0xB2,0xB3,0xB4,0xB5,0xB6,0xB7,0xB8,0xB9,
										0xC0,0xC1,0xC2,0xC3,0xC4,0xC5,0xC6,0xC7,0xC8,0xC9,
										0xD0,0xD1,0xD2,0xD3,0xD4,0xD5,0xD6,0xD7,0xD8,0xD9,
										0xE0,0xE1,0xE2,0xE3,0xE4,0xE5,0xE6,0xE7,0xE8,0xE9,
										0xF0,0xF1,0xF2,0xF3,0xF4,0xF5,0xF6,0xF7,0xF8,0xF9,
										
										0x101,0x102,0x103,0x104,0x105,0x106,0x107,0x108,0x109,0x110,
										0x111,0x112,0x113,0x114,0x115,0x116,0x117,0x118,0x119,0x120,
										0x121,0x122,0x123,0x124,0x125,0x126,0x127,0x128,0x129,0x130,
										0x131,0x132,0x133,0x134,0x135,0x136,0x137,0x138,0x139,0x140,
										0x141,0x142,0x143,0x144,0x145,0x146,0x147,0x148,0x149,0x150,
										0x151,0x152,0x153,0x154,0x155,0x156,0x157,0x158,0x159,0x160,
										
										0x161,0x162,0x163,0x164,0x165,0x166,0x167,0x168,0x169,0x170,
										0x171,0x172,0x173,0x174,0x175,0x176,0x177,0x178,0x179,0x180,
										0x181,0x182,0x183,0x184,0x185,0x186,0x187,0x188,0x189,0x190,
										0x191,0x192,0x193,0x194,0x195,0x196,0x197,0x198,0x199,0x200,
										0x201,0x202,0x203,0x204,0x205,0x206,0x207,0x208,0x209,0x210,
										0x211,0x212,0x213,0x214,0x215,0x216,0x217,0x218,0x219,0x220,
										
										
		};
	}
	
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
    <script type="text/javascript">
$(document).ready(function(){
	
		  $("#chk_repeat").click(function(){
			  htmlobj=$.ajax({url:"./ChkVenderRepeat?action=0&vid="+ $("#vid").val() +"&"+Math.random(),async:false});
			  $("#tips").html(htmlobj.responseText);
		  });

		  
		  $("#motorbrd_count").change(function(){
			  //alert($("#motorbrd_count").val());
			  calPortCount();
		  });
		  $("#brd_type").change(function(){
			  //alert($("#motorbrd_count").val());
			  calPortCount();
		  });
	
	  $("#chk_repeat").click(function(){
		  htmlobj=$.ajax({url:"./ChkVenderRepeat?action=0&vid="+ $("#vid").val() +"&"+Math.random(),async:false});
		  $("#tips").html(htmlobj.responseText);
	  });
  
	$("#vid").change
	(
		function()
		{
			$("#tips").text("");
		}
	);
	
	  /*添加货道窗口*/
    $("#dialog").dialog({autoOpen: false,width:500,height:475,modal: false,
        buttons: {
            "确定": function() {
                $( this ).dialog( "close" );
              }
        }
    });
	  
	$("#addSlot").click(function(){
		/*  $("#dialog").dialog("open"); */
		 $('#myModal').modal('show');
	})
});



var ShowMapWin=function()
{
	window.open('selectpoint.html', 'SelectPoint', 'height=800, width=600, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=no,location=n o, status=no');
}

function isNumeric(strNumber) 
{
	var newPar=/^(-|\+)?\d+(\.\d+)?$/;
	return newPar.test(strNumber); 
}
function calPortCount()
{
	  var motorbrd_count=$("#motorbrd_count").val();
	  var brd_type=$("#brd_type").val();
	  var sum=0;
	  sum=motorbrd_count*60;
	  if(brd_type==1)
		{
		  if(sum==0)
			  {
			  	sum=12;
			  }
		}
	  else if(brd_type==2)
		{
		  sum+=60;
		}
	  $("#portcount").val(sum);	
};

var chk=function()
{
	if(!isNumeric(form.id.value))
	{
		$("#tips").text("售货机编号无效");
		return false;
	}
	
	if(form.tname.value=="")
	{
		$("#tips_t").text("售货机名称不能为空");
		return false;
	}
	
	if($("#key_type").val()==0)
	{
		$("#tips_key_type").text("键盘类型不能为空！");
		return false;
	}

	if($("#brd_type").val()==0)
	{
		$("#tips_brd_type").text("主板类型不能为空！");
		return false;
	}
	return true;
};

</script>
<title>添加 售货机</title>
</head>
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
				            	添加货道
				            </h4>
				         </div>
				         <div class="modal-body" id="alertcontent">
				            <ul class="list-inline">
							<%
							int i=0;
							if(valid_slot_id!=null)
							{
								for(int sid :valid_slot_id)
								{
									out.print("<li style='width:130px;'><label><input type=\"checkbox\" onclick='SlotChkBoxOnClick(this);' name=\"slottabbox\" value=\""+
									sid+"\" />货道"+String.format(clsConst.SLOT_FORMAT_HEX,sid)+"</label></li>");
								}
							}
							else
							{
								for( i=101;i<101+2*60;i++)
								{
									out.print("<li style='width:130px;'><label><input type=\"checkbox\" onclick='SlotChkBoxOnClick(this);' name=\"slottabbox\" value=\""+i+"\" />货道"+i+"</label></li>");
								}
								
							}
							%>
							</ul>
				         </div>
				         <div class="modal-footer">
				            <button type="button" class="btn btn-default" 
				               data-dismiss="modal">确定
				            </button>
				         </div>
				      </div><!-- /.modal-content -->
				   </div><!-- /.modal-dialog -->
				</div><!-- /.modal -->  
	 <div class="breadcrumbs" id="breadcrumbs" style="margin-top:5px;">
						<ul class="breadcrumb">
							<li>
								<span class="glyphicon glyphicon-home"></span>
								<a href="MainHome.jsp" target="main" style="padding-left:5px;margin-left:5px;">首页</a>
							</li>

							<li>
								<a href="#">设备管理</a>
							</li>
							<li class="active">添加售货机</li>
						</ul><!-- .breadcrumb -->

						<!-- #nav-search -->
					</div>
			  	<form class="form-horizontal" role="form" action="AddVender" name="form" method="post" onsubmit="return(chk());">
			  	<div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">终端编号</label>
				    <div class="col-sm-3">
				      <input type="text" name="id" id="vid" class="form-control input-sm input-sm" placeholder="终端编号(必填)" id="chk_repeat">
				    </div>
				    <div class="col-sm-5">
				      <input class="btn btn-success" type="button" value="检测编号是否重复" id="chk_repeat" name="chk_repeat" />
				      <span id="tips" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">终端名称</label>
				    <div class="col-sm-3">
				      <input name="tname" type="text" class="form-control input-sm input-sm"  placeholder="终端名称(必填)">
				      <span id="tips_t" style="color:red;"></span>
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">货道数量</label>
				    <div class="col-sm-3">
				    <input class="form-control input-sm input-sm" name="portcount"  value="12" id="portcount"  type="text" readonly="readonly"/>
					</div>
					<label class="col-sm-5"> 货道数量与主板类型和驱动板数量有关，无法直接修改，货道配置好之后，将无法再更改</label>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">键盘类型</label>
				    <div class="col-sm-3">
				    <select class="form-control input-sm input-sm" name="key_type" id="key_type">
						<option value='0' >请选择键盘类型</option>
						<option value='16' >16个按钮纯数字</option>
						<option value='18' >18个按钮带字母</option>
						<option value='12' >一对一选货按钮</option>
					</select>
					</div>
					<label class="col-sm-5"><span id="tips_key_type" style="color: red;"></span></label>
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">主板类型</label>
				    <div class="col-sm-3">
					<select  class="form-control input-sm input-sm"  name="brd_type" id="brd_type">
						<option value='0' >请选择主板类型</option>
						<option value='1' >12货道迷你主板</option>
						<option value='2' >60货道一体化主板</option>
					</select>
					</div>
					<label class="col-sm-5"><span id="tips_brd_type" style="color: red;"></span>
					<a href="" target="blank">查看12货道迷你主板图片</a>  <a href="" target="blank">查看60货道一体化主板图片</a>
					</label>
					
				  </div>
				  
				  <div class="form-group">
				    <label class="col-sm-4 control-label">驱动板数量</label>
				    <div class="col-sm-3">
					<select  class="form-control input-sm input-sm"  name="motorbrd_count" id="motorbrd_count">
					<%for(int i_tem=0;i_tem<=16;i_tem++) 
					{%>
						<option value='<%=i_tem %>' ><%=i_tem %>片</option>
					<%} %>	
					</select>
					</div>
				  </div>
				  
				  <div class="form-group" style="border: medium  rgb(250,0,255)">
				    <label class="col-sm-4 control-label">售货机型号</label>
				    <div class="col-sm-3">
				      <input name="sellertype" type="text" class="form-control input-sm"  placeholder="售货机型号">
				    </div>
				    
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">服务电话</label>
				    <div class="col-sm-3">
				      <input name="server_tel"  type="text" class="form-control input-sm" value="<%=ub.getAdminmobilephone() %>" placeholder="终端名称">
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">售货机地址</label>
				    <div class="col-sm-3">
				      <input name="address" type="text" class="form-control input-sm"  placeholder="售货机地址"><!-- 添加地址信息，便于地图查找 -->
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">售货机经度</label>
				    <div class="col-sm-3">
				      <input id="lng" name="lng" type="text" class="form-control input-sm"  placeholder="售货机经度">
				    </div>
				    <div class="col-sm-5">
				      	<input class="btn btn-success" type="button" value="在地图上查找位置" onclick="ShowMapWin()"/>
				      </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">售货机纬度</label>
				    <div class="col-sm-3">
				      <input id="lat" name="lat" type="text" class="form-control input-sm" placeholder="售货机纬度">
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-4 control-label">是否允许使用</label>
				    	<div class="col-sm-3">
						    <select class="form-control input-sm" name="canuse">
							  <option value="true">是</option>
							  <option value="false">否</option>
							</select>
						</div>
						<input  type="hidden"  name="slottab"  id="slottab" value=""/>
						<%-- <div id="dialog"  style="display:none">			   
							<ul class="list-inline">
							<%
							int i=0;
							if(valid_slot_id!=null)
							{
								for(int sid :valid_slot_id)
								{
									out.print("<li style='width:130px;'><label><input type=\"checkbox\" onclick='SlotChkBoxOnClick(this);' name=\"slottabbox\" value=\""+
									sid+"\" />货道"+String.format(clsConst.SLOT_FORMAT_HEX,sid)+"</label></li>");
								}
							}
							else
							{
								for( i=101;i<101+2*60;i++)
								{
									out.print("<li style='width:130px;'><label><input type=\"checkbox\" onclick='SlotChkBoxOnClick(this);' name=\"slottabbox\" value=\""+i+"\" />货道"+i+"</label></li>");
								}
								
							}
							%>
							</ul>
						</div> --%>
				  </div>
				  
				  <div class="form-group">
				    <div class="col-sm-offset-4 col-sm-3">
				      <input type="submit" class="btn btn-primary" value="添&nbsp;&nbsp;&nbsp;&nbsp;加"></input>
				      <button type="submit" class="btn btn-primary" value="取消">取&nbsp;&nbsp;&nbsp;&nbsp;消</button>
				      <button class="btn btn-primary"  value="返回列表" type="button" onclick="location.href='VenderList';" >返回列表</button>
				    </div>
				  </div>
				  </div>
				</form>
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