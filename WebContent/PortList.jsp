<%@page import="beans.clsGoodsBean"%>
<%@page import="beans.PortBean"%>
<%@page import="java.util.ListIterator"%>
<%@ page import="beans.VenderBean"%>
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
		
		if(!ub.AccessAble(UserBean.FUNID_CAN_VIEW_PORT))
		{
			request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_VIEW_PORT]);
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}
		int show_emptyslot=ToolBox.filterInt(request.getParameter("show_emptyslot"));
		session.setAttribute("currentpage", request.getRequestURI()+"?"+request.getQueryString());
		int sellerId=ToolBox.filterInt(request.getParameter("mid"));
		ArrayList<VenderBean> vblst=SqlADO.getVenderListByIdLimint(ub.getVenderLimite());
		if(sellerId==0)
		{
			if(vblst==null)
			{
				request.setAttribute("message", "没有可以管理的售货机！如有疑问，请联系管理员！");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}
			else
			{
				sellerId=vblst.get(0).getId();
			}
		}
		else
		{
			if(!ub.CanAccessSeller(sellerId))
			{
				request.setAttribute("message", ToolBox.CANNTACCESS);
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}
		}
		%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
    <meta http-equiv="X-UA-Compatible" content="IE=9" />
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
    <meta name="description" content=""/>
    <meta name="author" content=""/>
    <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet"/>
    <link href="css/bootstrap/metisMenu.min.css" rel="stylesheet"/>
    <link href="css/bootstrap/timeline.css" rel="stylesheet"/>
    <link href="css/bootstrap/admin.css" rel="stylesheet"/>
    <link href="css/bootstrap/morris.css" rel="stylesheet"/>
    <link href="css/bootstrap/font-awesome.min.css" rel="stylesheet" type="text/css"/>
    <link href="./jquery_ui/css/cupertino/jquery-ui.min.css" rel="stylesheet" type="text/css" />
    <link href="./jquery_ui/css/showLoading.css" rel="stylesheet" type="text/css" />
     <!--[if lte IE 6]>
	<link rel="stylesheet" type="text/css" href="/css/bootstrap/bootstrap-ie6.css"/>
	<![endif]-->
	<!--[if lte IE 7]>
	<link rel="stylesheet" type="text/css" href="/css/bootstrap/ie.css"/>
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
	<script type="text/javascript">

	$(document).ready(function(){
		$("#load").click(function(){
			window.location.href="./QuickLoadGoods?vid=<%=sellerId%>"+"&"+Math.random();
		});
		
		$( "#but_hide_emptyslot" ).button();
        $( "#but_show_emptyslot" ).button();
	
        $("#but_hide_emptyslot").click(function(){
        	
        	window.location.href="./PortList.jsp?mid=<%=sellerId%>"+"&show_emptyslot=0&"+Math.random();

        });
        
        $("#but_show_emptyslot").click(function(){
        	
        	window.location.href="./PortList.jsp?mid=<%=sellerId%>"+"&show_emptyslot=1&"+Math.random();

        });
		
		$( ".qrcode" ).button();
		$( "#re_wx_qrcode" ).button();
        $( "#re_al_qrcode" ).button();
	
        $("#re_al_qrcode").click(function(){
        	var obj=new Object();
        	obj.vendid=new Array();
            obj.vendid[0]=<%=sellerId%>;
            obj.qrcode_type="al";
            $("body").showLoading();  
            /*发送ajax*/
            $.ajax({
              type: "POST",
              url: "./AjaxQrCodeFactory",
              data:{data:JSON.stringify(obj)} ,
              success: (function(obj){
                  $("body").hideLoading();  
                  showAlert(obj,"更新阿里二维码");
                  }),
              dataType: "text"
            });
        });
        
        $("#re_wx_qrcode").click(function(){
        	
            var obj=new Object();
            obj.vendid=new Array();
            obj.vendid[0]=<%=sellerId%>;
            obj.qrcode_type="wx";
            $("body").showLoading();  
            /*发送ajax*/
            $.ajax({
              type: "POST",
              url: "./AjaxQrCodeFactory",
              data:{data:JSON.stringify(obj)} ,
              success: (function(obj){
                  $("body").hideLoading();  
                  showAlert(obj,"更新微信二维码");
                  }),
              dataType: "text"
            });
        });
		
        /*提示窗口*/
        $("#alertdlg").dialog({autoOpen: false,width:300,modal: true,
            buttons: {
                "确定": function() {
                    $( this ).dialog( "close" );
                  }
            }
        });
        
        $("#qrcode_win").dialog({autoOpen: false,width:300,modal: true,
            buttons: {
                "确定": function() {
                    $( this ).dialog( "close" );
                  }
            }
        });
		
	});
	
	
	
	function SelectGoods(colid) 
	{
		window.open ('SelectGoods.jsp?colid='+colid+'&'+Math.random(), 'SelectGoods', 'height=800, width=600, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=no,location=n o, status=no');
	}
	
	var showAlert=function(title,content)
    {
        
        $( "#alertcontent" ).html(title);
        $( "#myModalLabel" ).html(content);
        $('#myModal').modal('show');
    }
	function  showqrcode(url) 
	{
		//$("#qr_img").attr("src",url);
		$('#alertcontent').html("");
		$('#alertcontent').qrcode(
				{
					"ecLevel": 'H',
					"size": 258,
				    "color": "#3a3",
				    "text": url,
				    "mode":2,
				    "label":"东吉"
				}
				);
		$('#myModal').modal('show');
	}

	
</script>
</head>



<%

	int RsCount=0;
	int pagecount=ub.getPagecount();
	int Page=ToolBox.filterInt(request.getParameter("page"));
	if(Page==0) 
	{
		Page=1;
	}
    int colcount=10;
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
							<label>跳转到：</label>
							  <label>
							  	<select class="form-control input-sm" onchange="window.open('?mid='+this.options[this.selectedIndex].value,'_self')">
								<% 
									ListIterator<VenderBean> vbit= vblst.listIterator();
									VenderBean vbobj;
									int index=0;
									while(vbit.hasNext())
									{
										vbobj=vbit.next();
										index=vbobj.getId();
									%>
									<option value=<%=index%> <%=((sellerId==index)?"selected":"") %> ><%=index%></option>
									<%} %>
								</select>
							  </label>
								<%if(show_emptyslot==0)
								{ %>
								<input class="btn btn-default"  id="but_show_emptyslot"  type="button"   value="显示空货道" />
								<%}
								else
								{
									%>
									<input class="btn btn-default"  id="but_hide_emptyslot"  type="button"   value="不显示空货道" />
									<%
								}
								%>
							</form>
						</div>
					</div>
								
			  </div>
			</div>
			<div class="row" style="overflow-y:auto;">
            <div class="col-xs-12">
				<input type="hidden" value="<%=sellerId %>" name="id"/>
              <div class="box">
                <div class="box-body">
					<div id="DataTables_Table_0_wrapper" class="dataTables_wrapper" role="grid">
						<%
							int td_count=10; 
						%>
						<form action="UpdatePort" method="post" name="form2" id="form2">
						<div class="table-responsive">
							<table class="table table-bordered table-hover table-condensed" style="overflow-y:auto; width:100%;height:100px;border-spacing: 0px;">
								<thead>
									<tr role="row" style="background-color: #f5f5f5;">
										<th>货机号</th>
										<th>货道号</th>
										<th>货道名称</th>
										<th>货物数量</th>
										<th>货道容量</th>
										<th>价格</th>
										<th>更新时间</th>
										<th>故障</th>
										<th >故障信息</th>
										<th>故障时间</th>
									</tr>											
								</thead>

								<tbody role="alert" aria-live="polite" aria-relevant="all">
									<%
									 	int count=0;
									  	clsGoodsBean goods=null;
									    ArrayList<PortBean> lst=SqlADO.getPortBeanList(sellerId,show_emptyslot!=0);
									    RsCount=lst.size();
									    
										if(lst!=null)
										{
											ListIterator<PortBean> it=lst.listIterator();
											PortBean obj;
											//int index=0;
											while(it.hasNext())
											{
												obj=it.next();
												count++;
												if(count<=(Page-1)*pagecount)
												{
													continue;
												}
												
												if(count>(Page)*pagecount)
												{
													break;
												}
												
													
												boolean isIserror=obj.getError_id()>0;
												int innerid=obj.getInnerid();
												
									  %>
									<tr class="odd">
										<td class=" sorting_1"><%=sellerId%></td>
										<td class="center ">
										    <%=obj.getInneridname()%>
										    
										  </td>
										<td class="center "><input type="button" class="btn btn-danger btn-sm" value="<%="["+obj.getGoodsid()+"]"+obj.getGoodroadname()%>" onclick="SelectGoods(<%= obj.getId()%>)"></input></td>
										<td class="center "><input class="form-control input-sm" style="width: 8em;" value="<%=obj.getAmount()%>" name="amount<%=innerid%>" type="text"/></td>
										<td class="center "><input class="form-control input-sm" style="width: 8em;" value="<%=obj.getCapacity() %>" name="capacity<%=innerid%>" type="text"/></td>
										<td class="center "><input class="form-control input-sm" style="width: 8em;" value="<%=String.format("%1.2f",obj.getPrice()/100.0)%>" name="price<%=innerid%>" type="text"/></td>
										<td class="center "><%=ToolBox.getYMDHM(obj.getUpdatetime())%></td>
										<td class="center ">
											<%if(isIserror)
											{
													if(ub.AccessAble(UserBean.FUNID_CAN_UPDATE_PORT))
													{
														out.print("<span class='waring-label'><a href='UpdatePort?id="+ sellerId +"&innerid="+ innerid +"'>有</a></span>");
													}
													else
													{
														out.print("<span class='waring-label'>有</span>");
													}
											}
											else
											{
												out.print("<span class='normal-label'>无</span>");
											}
											%>
										</td>
										<td class="center ">
											<%=obj.getErrorinfo()%>
										</td>
										<td class="center "><%=ToolBox.getYMDHMS(obj.getLastErrorTime())%></td>
									</tr>  
									<% 		
										}
									}
									%>
									<tr class="odd">
								    <td colspan="<%=colcount %>" align="center">
								    <%
								    	if(RsCount>0) 
								    	{
								    		out.println(ToolBox.getpages("mid="+sellerId, "#999", Page, pagecount, RsCount));
										 }
								    	else 
								    	{
								    		out.println("<span class='waring-label'>没有符合条件的货道！</span>");
								    	}
								    			
								    	%></td>
								    </tr>
								
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


		