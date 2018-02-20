package com.alipay;

import java.text.SimpleDateFormat;

import org.apache.commons.lang.math.RandomUtils;

import com.ado.SqlADO;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.factory.AlipayAPIClientFactory;
import com.tools.ToolBox;

import beans.PortBean;
import beans.clsGroupBean;

public class AlipayQrcode {
	public static void main(String[] args) {
		clsGroupBean groupBean=clsGroupBean.getGroup(3);
		AlipayTradePrecreateResponse  ret=MakeQrcode("A1",1,12,1,"可口可乐",groupBean);
		
		System.out.println(ret.getQrCode());
		
	}
	
	public static AlipayTradePrecreateResponse MakeQrcode(PortBean pbt,String out_trade_no,clsGroupBean groupBean) 
	{
		
		String total_amount=String.format("%1.2f",pbt.getPrice()/100.0);
		String subject=pbt.getGoodroadname();
		if(subject==null)
		{
			subject=pbt.getInneridname();//String.format("", pbt.getInneridname())
		}
		if(subject==null)
		{
			subject="GOODS";
		}
		if(subject.equals(""))
		{
			subject=pbt.getInneridname();
		}
		
		return qrPay(out_trade_no,
					total_amount,
					subject,
					String.format("%05d-%04d",pbt.getMachineid(),pbt.getGoodsid()),
					groupBean);
	}
	public static AlipayTradePrecreateResponse MakeQrcode(PortBean pbt,String notifyurl) 
	{
		
		String out_trade_no=ToolBox.MakeOutTradeID(pbt.getMachineid());
		String total_amount=String.format("%1.2f",pbt.getPrice()/100.0);
		String subject=pbt.getGoodroadname();
		
		clsGroupBean groupBean=clsGroupBean.getGroup(0);
		
		return qrPay(out_trade_no,total_amount,subject,String.format("%05d-%04d",pbt.getMachineid(),pbt.getGoodsid()),groupBean);
	}
	public static AlipayTradePrecreateResponse MakeQrcode(String inneridname,
			int goodsid,int machineid,int price,String goodsnames,clsGroupBean groupBean) 
	{
		
		String out_trade_no=ToolBox.MakeOutTradeID(machineid);
		String total_amount=String.format("%1.2f",price/100.0);
		String subject=goodsnames;

		return qrPay(out_trade_no,total_amount,subject,String.format("%05d-%04d",machineid,goodsid),groupBean);
	}
//"http://121.40.250.28/VenderManager/ali/notify_url.jsp"
	public static AlipayTradePrecreateResponse qrPay(String out_trade_no,
													String total_amount,
													String subject,
													String body,
													clsGroupBean groupBean)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time_expire= sdf.format(System.currentTimeMillis()+24*60*60*1000);
		
		StringBuilder sb = new StringBuilder();
		sb.append("{\"out_trade_no\":\"" + out_trade_no + "\",");
		sb.append("\"total_amount\":\""+total_amount+"\",\"discountable_amount\":\"0.00\",");
		sb.append("\"subject\":\""+subject+"\",\"body\":\""+body+"\",");
		sb.append("\"timeout_express\": \"2m\"}");


		AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(groupBean);

		
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		request.setBizContent(sb.toString());
		request.setNotifyUrl(groupBean.getNotify_url());
		AlipayTradePrecreateResponse response = null;
		try {
			response = alipayClient.execute(request);

			//System.out.println(response.getBody());
			//System.out.println(response.isSuccess());
			//System.out.println(response.getMsg());
			// 这里只是简单的打印，请开发者根据实际情况自行进行处理
			if (null != response && response.isSuccess()) {
				if (response.getCode().equals("10000")) {
					//System.out.println("商户订单号："+response.getOutTradeNo());
					//System.out.println("二维码值："+response.getQrCode());//商户将此二维码值生成二维码，然后展示给用户，用户用支付宝手机钱包扫码完成支付
					//二维码的生成，网上有许多开源方法，可以参看：http://blog.csdn.net/feiyu84/article/details/9089497
					
				} else 
				{
					System.out.println("错误码："+response.getSubCode());
					System.out.println("错误描述："+response.getSubMsg());
				}
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	


}
