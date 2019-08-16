package com.servlet;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.ClsSaleStatisticData;
import beans.PortBean;
import beans.TradeBean;
import beans.UserBean;
import beans.clsGroupBean;

import com.clsConst;
import com.ado.SqlADO;

import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import com.sun.org.apache.xerces.internal.impl.dtd.models.CMLeaf;
import com.tools.ToolBox;

import weixin.popular.bean.message.EventMessage;
import weixin.popular.bean.message.message.Message;
import weixin.popular.bean.xmlmessage.XMLMessage;
import weixin.popular.bean.xmlmessage.XMLNewsMessage;
import weixin.popular.bean.xmlmessage.XMLTextMessage;
import weixin.popular.bean.xmlmessage.XMLNewsMessage.Article;
import weixin.popular.util.StreamUtils;
import weixin.popular.util.XMLConverUtil;

/**
 * Servlet implementation class WCallBack
 */
@WebServlet("/WCallBack")
public class WCallBack extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WCallBack() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		/**/
		String str=request.getQueryString();
		//signature=a516fb933d444cd9f11c6d91d9a37bc94bf286ee&echostr=4529769951878404861&timestamp=1453529845&nonce=42645818
		System.out.println(str);
		int wxid=ToolBox.String2Integer(request.getParameter("id"));
		String signature=request.getParameter("signature");
		String echostr=request.getParameter("echostr");
		int timestamp=ToolBox.String2Integer(request.getParameter("timestamp"));
		int nonce=ToolBox.String2Integer(request.getParameter("nonce"));
		
		
		
		System.out.println(wxid);
		System.out.println(signature);
		System.out.println(echostr);
		System.out.println(timestamp);
		System.out.println(nonce);
		
		
		if(echostr!=null)
		{
			response.getWriter().print(echostr);
		}
//		clsGroupBean wxpara= SqlADO.getGroup(wxid);
//		
//		try {
//			WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(wxpara.getsToken(), 
//					wxpara.getsEncodingAESKey(), 
//					wxpara.getsCorpID()
//					);
//			
//			
//			// 解析出url上的参数值如下：
//			// String sVerifyMsgSig = HttpUtils.ParseUrl("msg_signature");
//			String sVerifyMsgSig = "5c45ff5e21c57e6ad56bac8758b79b1d9ac89fd3";
//			// String sVerifyTimeStamp = HttpUtils.ParseUrl("timestamp");
//			String sVerifyTimeStamp = "1409659589";
//			// String sVerifyNonce = HttpUtils.ParseUrl("nonce");
//			String sVerifyNonce = "263014780";
//			// String sVerifyEchoStr = HttpUtils.ParseUrl("echostr");
//			String sVerifyEchoStr = "P9nAzCzyDtyTWESHep1vC5X9xho/qYX3Zpb4yKa9SKld1DsH3Iyt3tP3zNdtp+4RPcs8TgAE7OaBO+FZXvnaqQ==";
//			String sEchoStr; //需要返回的明文
//			try {
//				sEchoStr = wxcpt.verifyUrl(sVerifyMsgSig, sVerifyTimeStamp,
//						sVerifyNonce, sVerifyEchoStr);
//				System.out.println("verifyurl echostr: " + sEchoStr);
//				// 验证URL成功，将sEchoStr返回
//				// HttpUtils.SetResponse(sEchoStr);
//			} catch (Exception e) {
//				//验证URL失败，错误原因请查看异常
//				e.printStackTrace();
//			}			
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		




		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub id=1&signature=05b327c7708bb8367b7eed33823c7c9b9b35af51&timestamp=1453535030&nonce=1919129937
		//doGet(request, response);
		response.setCharacterEncoding("UTF-8");
		
		String str=request.getQueryString();
		
		int group_id=ToolBox.String2Integer(request.getParameter("id"));
		String signature=request.getParameter("signature");
		String echostr=request.getParameter("echostr");
		int timestamp=ToolBox.String2Integer(request.getParameter("timestamp"));
		int nonce=ToolBox.String2Integer(request.getParameter("nonce"));
		
		ServletInputStream is= request.getInputStream();
		
		String xmlstr=StreamUtils.copyToString(is, Charset.forName("utf-8"));
		
		EventMessage xml=XMLConverUtil.convertToObject(EventMessage.class, xmlstr);
		
		if(group_id==0)
		{
			SqlADO.insertWxLog(group_id,str,xmlstr,null);
			return;
		}
		
		if(xml==null)
		{
			SqlADO.insertWxLog(group_id,str,xmlstr,null);
			return;
		}
		
		clsGroupBean groupBean=clsGroupBean.getGroup(group_id);

		if(xml.getMsgType().equals("text"))
		{
			String content_str= xml.getContent();
			if(content_str!=null)
			{
				/*查询该关注者是不是合法的管理员*/
				String user_openid=xml.getFromUserName();
				UserBean ub=UserBean.getUserByWxOpenId(user_openid);
				StringBuilder retstr=null;
				if(ub!=null)
				{
					String[] arr=content_str.split("@");
					String venderlimit;
					retstr=new StringBuilder();
					int cmdid= ToolBox.filterInt(arr[0]);
					int attachid=0;
					System.out.println(content_str);
					switch (cmdid) 
					{
					case clsConst.WX_CMD_ID_LOOK_TRADE_OF_TODAY:
						/*今日交易*/
						
						if(arr.length<2)
						{
							venderlimit=ub.getVenderLimite();
						}
						else
						{
							venderlimit=ToolBox.filter(arr[1]);
							
						}
					 	Calendar c= Calendar.getInstance();
					 	c.set(Calendar.HOUR_OF_DAY,0);
					 	c.set(Calendar.MINUTE,0);
					 	c.set(Calendar.SECOND,0);
						Date edate=null;
						Date beginDate =new Date(c.getTimeInMillis());

						c.add(Calendar.DAY_OF_MONTH, 1);
						edate=new Date(c.getTimeInMillis());
						int jiesuan=0;
						ClsSaleStatisticData salestatistic_all= SqlADO.getSalesStatisticDataFromDb(
								beginDate,edate,venderlimit,clsConst.TRADE_TYPE_NO_LIMIT,jiesuan);
						ClsSaleStatisticData salestatistic_al= SqlADO.getSalesStatisticDataFromDb(
								beginDate,edate,venderlimit,clsConst.TRADE_TYPE_AL_QR,jiesuan);
						ClsSaleStatisticData salestatistic_wx= SqlADO.getSalesStatisticDataFromDb(
								beginDate,edate,venderlimit,clsConst.TRADE_TYPE_WX_QR,jiesuan);
						ClsSaleStatisticData salestatistic_card= SqlADO.getSalesStatisticDataFromDb(
								beginDate,edate,venderlimit,clsConst.TRADE_TYPE_CARD,jiesuan);
						ClsSaleStatisticData salestatistic_cash= SqlADO.getSalesStatisticDataFromDb(
								beginDate,edate,venderlimit,clsConst.TRADE_TYPE_CASH,jiesuan);
						ClsSaleStatisticData salestatistic_bank= SqlADO.getSalesStatisticDataFromDb(
								beginDate,edate,venderlimit,clsConst.TRADE_TYPE_BANK,jiesuan);
						retstr.append("交易日期：");
						retstr.append(ToolBox.getDateString());
						retstr.append("\n");
						
						retstr.append("总笔数:");
						retstr.append(salestatistic_all.getM_count());
						retstr.append("\n");
						retstr.append("总交易额(元):");
						retstr.append(salestatistic_all.getM_credit()/100.0);
						retstr.append("\n");
						
						retstr.append("现金笔数:");
						retstr.append(salestatistic_cash.getM_count());
						retstr.append("\n");
						retstr.append("现金交易额(元):");
						retstr.append(salestatistic_cash.getM_credit()/100.0);
						retstr.append("\n");
						
						retstr.append("支付宝笔数:");
						retstr.append(salestatistic_al.getM_count());
						retstr.append("\n");
						retstr.append("支付宝交易额(元):");
						retstr.append(salestatistic_al.getM_credit()/100.0);
						retstr.append("\n");
						
						retstr.append("微信笔数:");
						retstr.append(salestatistic_wx.getM_count());
						retstr.append("\n");
						retstr.append("微信交易额(元):");
						retstr.append(salestatistic_wx.getM_credit()/100.0);
						retstr.append("\n");
						
						retstr.append("IC卡笔数:");
						retstr.append(salestatistic_card.getM_count());
						retstr.append("\n");
						retstr.append("IC卡交易额(元):");
						retstr.append(salestatistic_card.getM_credit()/100.0);
						retstr.append("\n");
						
						retstr.append("银行卡笔数:");
						retstr.append(salestatistic_bank.getM_count());
						retstr.append("\n");
						retstr.append("银行卡交易额(元):");
						retstr.append(salestatistic_bank.getM_credit()/100.0);
						retstr.append("\n");
						break;
	
					case clsConst.WX_CMD_ID_LOOK_TRADE_OF_THIS_MONTH:
						/*查询本月交易额*/
						if(arr.length<2)
						{
							venderlimit=ub.getVenderLimite();
						}
						else
						{
							venderlimit=ToolBox.filter(arr[1]);
							
						}
						break;
						
					case clsConst.WX_CMD_ID_LOOK_VENDER_STATE:
						/*查询机器状态*/
						if(arr.length<2)
						{
							retstr.append(String.format("没有填写机器编号，请使用:%d 机器编号,这样的格式，如要查询1002号机器的，就发送：%d 1002", 
									clsConst.WX_CMD_ID_LOOK_VENDER_STATE,clsConst.WX_CMD_ID_LOOK_VENDER_STATE));
						}
						else
						{
							/*回复机器状态*/
							attachid=ToolBox.filterInt(arr[1]);
							/*显示机器状态信息*/
							retstr.append("显示机器状态信息");
							
						}
						break;
					case clsConst.WX_CMD_ID_LOOK_SLOT_OUT_STATE:
						/*查询货道缺货信息*/
						if(arr.length<2)
						{
							retstr.append(String.format("没有填写机器编号，请使用:%d 机器编号,这样的格式，如要查询1002号机器的，就发送：%d 1002", 
									clsConst.WX_CMD_ID_LOOK_SLOT_OUT_STATE,clsConst.WX_CMD_ID_LOOK_SLOT_OUT_STATE));
						}
						else
						{
							/*回复机器状态*/
							attachid=ToolBox.filterInt(arr[1]);
							retstr.append("查询货道缺货信息");
							
						}
						break;
					case clsConst.WX_CMD_ID_LOOK_SLOT_ERR_STATE:
						/*查询货道 故障信息*/
						if(arr.length<2)
						{
							retstr.append(String.format("没有填写机器编号，请使用:%d 机器编号,这样的格式，如要查询1002号机器的，就发送：%d 1002", 
									clsConst.WX_CMD_ID_LOOK_SLOT_ERR_STATE,clsConst.WX_CMD_ID_LOOK_SLOT_ERR_STATE));
						}
						else
						{
							/*回复状态*/
							attachid=ToolBox.filterInt(arr[1]);
							retstr.append("查询货道故障信息");
						}
						break;
						
					default:
						retstr.append("--菜单列表--\n");
						retstr.append("请回复编号\n");
						retstr.append("1、查询今日交易额\n");
						//retstr.append("2、查询本月交易额\n");
						//retstr.append("3、查询机器状态\n");
						//retstr.append("4、查询货道缺货信息\n");
						//retstr.append("5、查询货道故障信息");
						
						if(content_str.toLowerCase().trim().equals("openid"))
						{
							retstr=new StringBuilder();
							retstr.append(xml.getFromUserName());
						}
						break;
					}
				}
				else
				{
					if(content_str.toLowerCase().trim().equals("openid"))
					{
						retstr=new StringBuilder();
						retstr.append(xml.getFromUserName());
					}
					
					else if(content_str.toLowerCase().startsWith("bind"))
					{
						retstr=new StringBuilder();
						String[] arr=content_str.split(" ");
						if(arr.length>=3)
						{
							String username=arr[1];
							String oneceid=arr[2];
							UserBean temub= UserBean.getUserBean(username);
							
							if(temub!=null)
							{
								if(temub.getOneceIdValidTime()!=null)
								{
									if(System.currentTimeMillis()< temub.getOneceIdValidTime().getTime())
									{
										if(temub.getOneceId()!=null)
										{
											if((oneceid.equals(temub.getOneceId()))&&(!temub.getOneceId().trim().equals("")))
											{
												temub.setWx_openid(xml.getFromUserName());
												temub.setOneceId("");
												UserBean.updateUser(temub);
												retstr.append("微信公众号绑定成功");
												
											}
											else
											{
												retstr.append("随机码错误");
											}
										}
										else
										{
											retstr.append("随机码错误");
										}
									}
									else
									{
										retstr.append("随机码超时");
									}
								}
								else
								{
									retstr.append("随机码超时");
								}
							}
							else
							{
								retstr.append("用户名不存在");
							}
						}
						else
						{
							retstr.append("参数错误");
						}
					}
				}
				
				if(retstr!=null)
				{
					SendTextMsg(xml, retstr.toString(),group_id,str,xmlstr,response);
				}
				
			}
		}
		if(xml.getMsgType().equals("event"))
		{
			if(xml.getEvent().equals("subscribe"))
			{
				/*添加关注*/
				SendTextMsg(xml, groupBean.getWelcomeMessage(),group_id,str,xmlstr,response);
			}
		}
		else 
		{
			//isgetgoods=true;
		}
		
		//SendTextMsg(xml, "您已经领取过商品，感谢您的再次光临！",group_id,str,xmlstr,response);

		return;

	}
	
	private void SendTextMsg(EventMessage xml,String text,int group_id,String querystr,String xmlstr,HttpServletResponse response)
	{
		try {
			XMLTextMessage textMessage=new XMLTextMessage(xml.getFromUserName(),xml.getToUserName(),text);
			String res=textMessage.toXML();		
			response.getWriter().println(res);
			SqlADO.insertWxLog(group_id,querystr,xmlstr,res);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}

}
