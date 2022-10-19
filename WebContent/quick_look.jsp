<%@page import="beans.PortBean"%>
<%@ page import="beans.VenderBean"%>
<%@ page import="beans.UserBean"%>
<%@ page import="com.ado.SqlADO"%>
<%@ page import="com.tools.ToolBox"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
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


	if(!ub.AccessAble(UserBean.FUNID_CAN_VIEW_PORT))
	{
		request.setAttribute("message", "Unable to "+UserBean.RIGHT_DES[UserBean.FUNID_CAN_VIEW_PORT]);
		request.setAttribute("LAST_URL", "index.jsp");
		request.getRequestDispatcher("message.jsp").forward(request, response);
		return;
	}


    ArrayList<VenderBean> livb=SqlADO.getVenderListByIdLimint(ub.getCanAccessSellerid());

%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
    <meta http-equiv="X-UA-Compatible" content="IE=9" />
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
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
    <script type="text/javascript" src="./jquery_ui/js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="./jquery_ui/js/jquery.showLoading.min.js"></script>
    <script type="text/javascript" src="./jquery_ui/js/jquery.ui.datepicker-zh-TW.js"></script>
<style type="text/css">
.quick-look
{
	-webkit-border-horizontal-spacing: 0px;
	-webkit-border-image: none;
	-webkit-border-vertical-spacing: 0px;
	border-bottom-color: white;
	border-bottom-left-radius: 3px;
	border-bottom-right-radius: 3px;
	border-bottom-style: none;
	border-width: 0px;
	border-collapse: separate;
	border-left-color: white;
	border-left-style: none;
	border-right-color: white;
	border-right-style: none;
	border-top-color: white;
	border-top-left-radius: 3px;
	border-top-right-radius: 3px;
	border-top-style: none;
	font-family: Ubuntu, Helvetica, Arial, sans-serif;
	line-height: 14px;
	max-width: none;
	text-align: left;
	vertical-align: baseline;
	white-space: nowrap;
	padding:5px;
	margin:3px;
	display:block;
	float:left;
	width:170px;
	font-size:13px;
}
</style>
<title>Vending Out of Stock Quick Check</title>
</head>
<body style="background-color: #fff;">
	<div class="breadcrumbs" id="breadcrumbs" style="margin-top:5px;">
						<ul class="breadcrumb">
							<li>
								<span class="glyphicon glyphicon-home"></span>
								<a href="MainHome.jsp" target="main" style="padding-left:5px;margin-left:5px;">Home</a>
							</li>

							<li>
								<a href="#">Setting Management</a>
							</li>
							<li class="active">Vending Out of Stock Quick Check</li>
						</ul><!-- .breadcrumb -->

						<!-- #nav-search -->
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
										<th style="width: 30px;">Terminal ID</th>
										<th style="width: 100px;">Name</th>
										<th style="width: 50px;">Postcode</th>
										<th>Sales, Remaining/Volume Count</th>
										<th style="width: 120px;">Total</th>
										<th>Conn</th>
										<th>Temp</th>
									</tr>
								</thead>
								<tbody role="alert" aria-live="polite" aria-relevant="all">
									<%
									if(livb==null)
									{%>
										  <tr class="odd">
										  	<td class="center" colspan="3" align="center">
										    	Vending Out of Stock
											</td>
										  </tr>
									<%}
									else
									{
										ArrayList<PortBean> pbli=null;
										boolean quehuo=false;
										for(VenderBean vb:livb)
										{
											pbli=SqlADO.getPortBeanList(vb.getId());
											quehuo=false;
											for(PortBean pb:pbli)
											{
												if(pb.getCapacity()>pb.getAmount())
												//if(pb.getAmount()>pb.getCapacity())
												{
													quehuo=true;
													break;
												}
											}

											if(!quehuo)
											{
												continue;
											}

											//Map<Integer, String> channelErrorPair = new HashMap<Integer, String>();
										%>
										<tr class="even">
											<td class="center "><%=vb.getId() %></td>
											<td class="center "><a href="VenderMod.jsp?mid=<%=vb.getId() %>"><%=vb.getTerminalName() %></a></td>
											<td class="center "><%= vb.getTerminalAddress() %>
											<td class="center">
												<ul style="font-size: 13px;">
													<%
													 	int totalVolume = 0;
														int totalSold = 0;
														int totalChannel = 0;
														int totalChannelError = 0;
														int	runOutChannel = 0;
														int runOutSku = 0;
														int actualSold = 0;
														double balancePercent = 0;
														double outSkuPercent = 0;
														for(PortBean pb:pbli)
														{
															if(
																	((Integer.parseInt(pb.getInneridname()) >= 40 && Integer.parseInt(pb.getInneridname()) <= 47 ) ||
																	(Integer.parseInt(pb.getInneridname()) >= 10 && Integer.parseInt(pb.getInneridname()) <= 29) ||
																	(Integer.parseInt(pb.getInneridname()) >= 30 && Integer.parseInt(pb.getInneridname()) <= 38) ||
																	(Integer.parseInt(pb.getInneridname()) >= 51 && Integer.parseInt(pb.getInneridname()) <= 54) ||
																	(Integer.parseInt(pb.getInneridname()) >= 61 && Integer.parseInt(pb.getInneridname()) <= 66)
																	) && pb.getCapacity() != 0 ) {

																if(pb.getError_id() > 0) {
																	totalChannelError += 1;
																	//channelErrorPair.put(pb.getInnerid() , pb.getErrorinfo());
																}
																totalChannel += 1;
																totalVolume += pb.getCapacity();
																totalSold += pb.getAmount();
																%>
																<li class="quick-look">
																	<span >
																		#:<%=pb.getInneridname()%> -
																	</span>
																	<span style="color: blue;">
																		<%=String.format("% 2d",pb.getCapacity()-pb.getAmount()) %>,
																	</span>
																	<%
																		if(pb.getAmount() <= 2) {
																	%>
																			<span style="color:red;">
																				<%=String.format("% 2d \t/ % 2d",pb.getAmount(), pb.getCapacity()) %>
																				<%
																					if(pb.getError_id() > 0) {
																				%>
																					<span style="color:red;">
																						(E: <%= pb.getError_id() %>)
																					</span>
																				<%
																					}
																				%>
																			</span>
																	<%
																		}else {
																	%>
																			<span  style="color:green;">
																				<%=String.format("% 2d \t/ % 2d",pb.getAmount(), pb.getCapacity()) %>
																				<%
																					if(pb.getError_id() > 0) {
																				%>
																					<span style="color:red;">
																						(E: <%= pb.getError_id() %>)
																					</span>
																				<%
																					}
																				%>
																			</span>
																	<%
																		}

																		if(pb.getAmount() == 0) {
																			runOutChannel += 1;
																		}
																	%>


																	<!--
																	<span>
																		Name:<%=pb.getGoodroadname() %>
																	</span>
																	-->
																</li>
														<%
															}
														}

														actualSold = totalVolume - totalSold;
														balancePercent = ((double)totalSold / (double)totalVolume) * 100;
														runOutSku = runOutChannel + totalChannelError;
														outSkuPercent = ((double)runOutSku/ (double)totalChannel) * 100;
													%>

<%--
														<%
															for(Map.Entry<Integer, String> entry : channelErrorPair.entrySet()) {
																Integer key = entry.getKey();
																String value = entry.getValue();
														%>
														<li class="quick-look row">
																<span style="color: red;">
																	<strong>
																		# <%= key %>
																		Error: <%= value %>
																	</strong>
																</span>
														</li>
														<%
															}
														%> --%>


												</ul>
											</td>
											<td>
											<%
												if((((double)totalSold/ (double)totalVolume)*100) <= 32.00 || runOutChannel >= 4) {
													//if(balancePercent <= 32 || runOutChannel >= 4) {
												%>
														<span style="color: red;">
															<strong>
																Balance:<%=String.format("% 3d/ % 3d", totalSold, totalVolume) %>
																<br>
																(<%= String.format("%.0f", balancePercent) %>%)
																<br><br>
																Sold:<%=String.format("% 3d", totalVolume - totalSold) %>
																<br><br>
																Out of Stock SKU: <br><%=String.format("% 3d/ % 3d", runOutSku, totalChannel) %>
																(<%= String.format("%.0f", outSkuPercent) %>%)
															</strong>
														</span>
												<%
													}else if(((((double)totalSold/ (double)totalVolume)*100) > 32.00 && (((double)totalSold/ (double)totalVolume)*100) <= 55.00)  || (runOutChannel >= 3 && runOutChannel < 4)) {
												%>
														<span style="color: blue;">
															<strong>
																Balance:<%=String.format("% 3d/ % 3d", totalSold, totalVolume) %>
																<br>
																(<%= String.format("%.0f", balancePercent) %>%)
																<br><br>
																Sold:<%=String.format("% 3d", totalVolume - totalSold) %>
																<br><br>
																Out of Stock SKU: <br><%=String.format("% 3d/ % 3d", runOutSku, totalChannel) %>
																(<%= String.format("%.0f", outSkuPercent) %>%)
															</strong>
														</span>
												<%
													}else {
												%>
														<span>
															<strong>
																Balance:<%=String.format("% 3d/ % 3d", totalSold, totalVolume) %>
																<br>
																(<%= String.format("%.0f", balancePercent) %>%)
																<br><br>
																Sold:<%=String.format("% 3d", totalVolume - totalSold) %>
																<br><br>
																Out of Stock SKU: <br><%=String.format("% 3d/ % 3d", runOutSku, totalChannel) %>
																(<%= String.format("%.0f", outSkuPercent) %>%)
															</strong>
														</span>
												<%
													}
												%>
											</td>
											<td class="center">
												<%=vb.isIsOnline()?"<button type='button' class='btn btn-success btn-sm' style='font-weight: 700;'>On</button>":"<button type='button' class='btn btn-success btn-sm' style='background-color:#777;border-color:#fff;font-weight: 700;'>Off</button>"%>
											</td>
											<td>
												<%

											  		if(vb.getTemperature()!=32767)
											  		{
											  			if(ub.AccessAble(UserBean.FUNID_CAN_VIEW_TEMP_GRAPH)) {
													  		if(vb.getTemperature()>-120)
													  		{
													  			out.print(String.format("<button onclick='ShowTemCurve(%d);' type='button' class='btn btn-danger btn-sm' style='margin-right:3px;'>Temp:%1.1f℃</button>",vb.getId(),vb.getTemperature()/10.0));
													  		}
													  		else
													  		{
													  			out.print(String.format("<button onclick='ShowTemCurve(%d);'  type='button' class='btn btn-success btn-sm' style='margin-right:3px;'>Temp:%1.1f℃</button>",vb.getId(),vb.getTemperature()/10.0));
													  		}
											  			}else {
													  		if(vb.getTemperature()>-120)
													  		{
													  			out.print(String.format("<button type='button' class='btn btn-danger btn-sm' style='margin-right:3px;'>Temp:%1.1f℃</button>",vb.getTemperature()/10.0));
													  		}
													  		else
													  		{
													  			out.print(String.format("<button type='button' class='btn btn-success btn-sm' style='margin-right:3px;'>Temp:%1.1f℃</button>",vb.getTemperature()/10.0));
													  		}
													  	}

											  		}
											  		else
											  		{
											  			out.print("<button type='button' onclick='ShowTemCurve("+ vb.getId() +");' class='btn btn-danger btn-sm' style='margin-right:3px;'>Temperature Abnormal</button>");
											  		}

												%>
											</td>
										</tr>
										<%
										}

										%>
													<tr class="odd">
													 <td colspan="12" align="center" >Finish loading</td>
													</tr>
										<%
									}
									%>
								</tbody>
							</table>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>

<script>
	function ShowTemCurve(id)
	{
		location.href="./TempCurve.jsp?vid="+id;
	}
</script>

</body>
</html>
