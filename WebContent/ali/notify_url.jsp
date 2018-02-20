<%@page import="beans.VenderBean"%>
<%@page import="beans.clsGroupBean"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="beans.TradeBean"%>
<%@page import="com.tools.ToolBox"%>
<%@page import="com.ClsTime"%>
<%@page import="com.clsConst"%>
<%@page import="beans.Cls_AliTradeLog"%>
<%@ page import="com.alipay.util.*"%>
<%@page import="com.ado.SqlADO"%>
<%
/* *
 功能：支付宝服务器异步通知页面
 版本：3.3
 日期：2012-08-17
 说明：
 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。

 //***********页面功能说明***********
 创建该页面文件时，请留心该页面文件中无任何HTML代码及空格。
 该页面不能在本机电脑测试，请到服务器上做测试。请确保外部可以访问该页面。
 该页面调试工具请使用写文本函数logResult，该函数在com.alipay.util文件夹的AlipayNotify.java类文件中
 如果没有收到该页面返回的 success 信息，支付宝会在24小时内按一定的时间策略重发通知
 //********************************
 * */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%
Map<String,String> params = new HashMap<String,String>();
Map requestParams = request.getParameterMap();
String parastring="";
for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
	String name = (String) iter.next();
	String[] values = (String[]) requestParams.get(name);
	String valueStr = "";
	for (int i = 0; i < values.length; i++) {
		valueStr = (i == values.length - 1) ? valueStr + values[i]
				: valueStr + values[i] + ",";
	}
	//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
	//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
	params.put(name, valueStr);
	parastring+=name+"="+valueStr+"\r\n";
	
}

System.out.println(parastring);
//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
//商户订单号
	String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

	//支付宝交易号

	String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

	//交易状态
	String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
	String body=new String(request.getParameter("body").getBytes("ISO-8859-1"),"UTF-8");
	//20160730-1604
	String[] arr=body.split("-",-1);
	
	int venderid=ToolBox.String2Integer(arr[0]);
	int Goodsid=ToolBox.String2Integer(arr[1]);

	VenderBean vb=SqlADO.getVenderBeanByid(venderid);
	clsGroupBean groupBean =clsGroupBean.getGroup(vb.getGroupid());
	System.out.println(venderid);
	System.out.println(vb.getGroupid());
	System.out.println(groupBean.getAl_PARTNER());
	System.out.println(groupBean.getAl_PUBLIC_KEY());
	if(AlipayNotify.verify(params,groupBean.getAl_PARTNER(),groupBean.getAl_PUBLIC_KEY())){//验证成功

        Cls_AliTradeLog trade=new Cls_AliTradeLog();
        String gmt_payment=new String(request.getParameter("gmt_payment").getBytes("ISO-8859-1"),"UTF-8");
        
        String buyer_email=new String(request.getParameter("buyer_logon_id").getBytes("ISO-8859-1"),"UTF-8");
        String partner=buyer_email;
        String total_fee=new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");
        //String gmt_create=ParseResponseXml.parseSqlXML(notify_data, "gmt_create");

        String subject=new String(request.getParameter("subject").getBytes("ISO-8859-1"),"UTF-8");
        
        //String qrcode=ParseResponseXml.parseSqlXML(notify_data, "qrcode");
        
        trade.setNotifyXML(null);
        trade.setBuyer_email(buyer_email);
        //trade.setGmt_create(gmt_create);
        trade.setGmt_payment(gmt_payment);
        trade.setOut_trade_no(out_trade_no);
        trade.setPartner(partner);
        trade.setTotal_fee(total_fee);
        trade.setTrade_no(trade_no);
        trade.setTrade_status(trade_status);
        trade.setMachineid(arr[0]);//00001-0003
        trade.setGoodsid(arr[1]);
        trade.setNotifyXML(body);
        trade.setTransfor_status(0);
        if(trade_status.equals("TRADE_SUCCESS"))
        {
            trade.setTransfor_status(clsConst.TRADE_NEED_TRANSFOR);
             /*创建订单命令到数据库*/
             //SqlADO.InsertNewAliTrade(trade);
             //trade=SqlADO.getclsAliTradeLog(trade.getOut_trade_no());
             /*添加tablecmd*/
             /* SqlADO.AddTableCmd(
            		  ToolBox.String2Integer(trade.getMachineid()),
                     clsConst.TABLE_CMD_TRADE_TYPE_AL_QR,
                     trade.getOut_trade_no(),
                     60*1000,
                     ClsTime.SystemTime(), 
                     "支付宝扫码交易，支付成功"+ToolBox.getDateTimeString());
             */
             TradeBean tradeBean= SqlADO.getTradeBeanFromTemp(out_trade_no);
             if(tradeBean!=null)
             {
            	 tradeBean.setLiushuiid("处理中");
            	 tradeBean.setCardinfo(buyer_email);
            	 tradeBean.setMobilephone(buyer_email);
            	 tradeBean.setChangestatus(1);
            	 tradeBean.setReceivetime(new Timestamp(System.currentTimeMillis()));
            	 tradeBean.setXmlstr(parastring);
            	 tradeBean.setTradetype(clsConst.TRADE_TYPE_AL_QR);
            	 tradeBean.setTradeid(trade_no);/*平台交易ID*/
            	 SqlADO.updateTradeBeanTemp(tradeBean);
             }
        }
        else if(trade_status.equals("TRADE_FINISHED"))
        {
        	
        }
        else
        {
            trade.setTransfor_status(clsConst.TRADE_FAIL_TO_PAY);
        }
        out.println("success"); //请不要修改或删除

    }else{//验证失败
        out.println("fail");
    }
%>
