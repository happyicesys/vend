package wx.pay.util;

import java.util.Date;

import com.tools.ToolBox;

import beans.PortBean;
import beans.clsGroupBean;
import weixin.popular.api.PayMchAPI;
import weixin.popular.bean.paymch.MchBaseResult;
import weixin.popular.bean.paymch.Unifiedorder;
import weixin.popular.bean.paymch.UnifiedorderResult;
import weixin.popular.util.XMLConverUtil;



public class PayUtil {


	public static String MakeWxQrcode(PortBean pbt,String out_trade_no,clsGroupBean groupBean)
	{
		String total_amount=String.format("%d",pbt.getPrice());
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
		
		return wxPay(out_trade_no,
					total_amount,
					subject,
					String.format("%05d-%04d",pbt.getMachineid(),pbt.getGoodsid()),
					pbt.getMachineid(),
					groupBean);
	}
	
	
	private static String wxPay(String out_trade_no, String total_amount,
			String subject, String venderid_goodsid,int venderid, clsGroupBean groupBean) {
		

		/*发起统一预支付*/
		Unifiedorder unifiedorder=new Unifiedorder();
		
		unifiedorder.setAppid(groupBean.getWx_appid()); //微信分配的公众账号ID
		unifiedorder.setMch_id(groupBean.getWx_mch_id());//微信支付分配的商户号
		
		
		unifiedorder.setDevice_info(venderid_goodsid);//终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
		
		unifiedorder.setNonce_str(ToolBox.getMd5(ToolBox.MakeTradeID(0)));//随机字符串，不长于32位。推荐随机数生成算法
		
		unifiedorder.setBody(subject);//商品或支付单简要描述
		unifiedorder.setAttach(String.format("%d", venderid));//附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
        //String  out_trade_no=productid;//ToolBox.MakeTradeID(mid_int);
		unifiedorder.setOut_trade_no(out_trade_no);//商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
		
		unifiedorder.setTotal_fee(total_amount);//订单总金额，只能为整数，详见支付金额
		
		unifiedorder.setSpbill_create_ip(groupBean.getServerIp());//APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
		
		unifiedorder.setTime_start(ToolBox.getTimeString());//订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
		
		unifiedorder.setTime_expire(ToolBox.getTimeString(new Date(70000+System.currentTimeMillis())));//订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。其他详见时间规则
		
		unifiedorder.setNotify_url(groupBean.getWx_notify_url());//接收微信支付异步通知回调地址
		
		unifiedorder.setTrade_type(groupBean.getWx_pay_type());//取值如下：JSAPI，NATIVE，APP，详细说明见参数规定
		
		unifiedorder.setProduct_id(venderid_goodsid);//trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
		
		//unifiedorder.setOpenid(openid);//trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。下单前需要调用【网页授权获取用户信息】接口获取到用户的Openid。
		
		//Map<String, String> map=  MapUtil.objectToMap(unifiedorder);
		//String sign = SignatureUtil.generateSign(map,groupBean.getWx_key());
		
		//unifiedorder.setSign(sign);//签名，详见签名生成算法
		System.out.println("s1"+System.currentTimeMillis());
		UnifiedorderResult resobj= PayMchAPI.payUnifiedorder(unifiedorder,groupBean.getWx_key());
		System.out.println("e1"+System.currentTimeMillis());
		System.out.println("getCode_url"+resobj.getCode_url());
		System.out.println("getErr_code"+resobj.getErr_code());

		System.out.println("getErr_code_des"+resobj.getErr_code_des());
		
		
		System.out.println("getReturn_code"+resobj.getReturn_code());
		System.out.println("getReturn_msg"+resobj.getReturn_msg());
		
		System.out.println("getResult_code"+resobj.getResult_code());

		return resobj.getCode_url();
	}

	public static String wxPayForJsPay(String out_trade_no, String total_amount,
			String subject, String venderid_goodsid,int venderid,String OpenId, clsGroupBean groupBean) {
		

		/*发起统一预支付*/
		Unifiedorder unifiedorder=new Unifiedorder();
		
		unifiedorder.setAppid(groupBean.getWx_appid()); //微信分配的公众账号ID
		unifiedorder.setMch_id(groupBean.getWx_mch_id());//微信支付分配的商户号
		
		
		unifiedorder.setDevice_info(venderid_goodsid);//终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
		
		unifiedorder.setNonce_str(ToolBox.getMd5(ToolBox.MakeTradeID(0)));//随机字符串，不长于32位。推荐随机数生成算法
		
		unifiedorder.setBody(subject);//商品或支付单简要描述
		unifiedorder.setAttach(String.format("%d", venderid));//附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
        //String  out_trade_no=productid;//ToolBox.MakeTradeID(mid_int);
		unifiedorder.setOut_trade_no(out_trade_no);//商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
		
		unifiedorder.setTotal_fee(total_amount);//订单总金额，只能为整数，详见支付金额
		
		unifiedorder.setSpbill_create_ip(groupBean.getServerIp());//APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
		
		unifiedorder.setTime_start(ToolBox.getTimeString());//订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
		
		unifiedorder.setTime_expire(ToolBox.getTimeString(new Date(70000+System.currentTimeMillis())));//订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。其他详见时间规则
		
		unifiedorder.setNotify_url(groupBean.getWx_notify_url());//接收微信支付异步通知回调地址
		
		unifiedorder.setTrade_type("JSAPI");//取值如下：JSAPI，NATIVE，APP，详细说明见参数规定
		
		unifiedorder.setProduct_id(venderid_goodsid);//trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
		
		unifiedorder.setOpenid(OpenId);//trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。下单前需要调用【网页授权获取用户信息】接口获取到用户的Openid。
		
		//Map<String, String> map=  MapUtil.objectToMap(unifiedorder);
		//String sign = SignatureUtil.generateSign(map,groupBean.getWx_key());
		
		//unifiedorder.setSign(sign);//签名，详见签名生成算法
		System.out.println("s1"+System.currentTimeMillis());
		UnifiedorderResult resobj= PayMchAPI.payUnifiedorder(unifiedorder,groupBean.getWx_key());
		System.out.println("e1"+System.currentTimeMillis());
		System.out.println("getCode_url"+resobj.getCode_url());
		System.out.println("getErr_code"+resobj.getErr_code());

		System.out.println("getErr_code_des"+resobj.getErr_code_des());
		
		
		System.out.println("getReturn_code"+resobj.getReturn_code());
		System.out.println("getReturn_msg"+resobj.getReturn_msg());
		
		System.out.println("getResult_code"+resobj.getResult_code());

		return resobj.getPrepay_id();
	}

	public static void main(String[] args) 
	{
//		clsWxPara para=clsWxPara.getInstance();
//		System.out.println( generateTradeNotifyReplyXML(
//				"SUCCESS",
//				"好的",
//				"",
//				"SUCCESS",
//				"好的"
//				));
//		
//		String code= generateMchPayNativeRequestURL(para.getAppid(),para.getMch_id(),"53_1",para.getKey()); 
//		System.out.println(code);
//		System.out.println(getShortWxCodeString(code));
		clsGroupBean groupBean=clsGroupBean.getGroup(3);
		System.out.println(groupBean.getWx_key());
		System.out.println(wxPay("122212325512","1","苹果","123414",3,groupBean));

	}

	/**
	 * 生成回调回复
	 * @param wxSuccessCode，SUCCESS 表示成功，FAIL 表示失败
	 * @param string 回复错误描述信息
	 * @return 返回XML字符串
	 */
	public static String generateMchOrderReplyXML(String wxSuccessCode,String string) {
		MchBaseResult baseResult = new MchBaseResult();
		baseResult.setReturn_code(wxSuccessCode);
		baseResult.setReturn_msg(string);
		return XMLConverUtil.convertToXML(baseResult);
	}

}