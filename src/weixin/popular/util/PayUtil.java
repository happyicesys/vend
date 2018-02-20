package weixin.popular.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Date;

import com.ado.SqlADO;
import com.tools.ToolBox;

import beans.PortBean;
import beans.clsGroupBean;
import weixin.popular.api.PayMchAPI;
import weixin.popular.bean.pay.PayJsRequest;
import weixin.popular.bean.pay.PayNativeReply;
import weixin.popular.bean.pay.PayNativeRequest;
import weixin.popular.bean.pay.PayPackage;
import weixin.popular.bean.paymch.MchOrderInfoResultRelay;
import weixin.popular.bean.paymch.MchPayApp;
import weixin.popular.bean.paymch.MchPayNativeReply;
import weixin.popular.bean.paymch.PapayEntrustweb;
import weixin.popular.bean.paymch.Unifiedorder;
import weixin.popular.bean.paymch.UnifiedorderResult;

public class PayUtil {


	/**
	 * 生成支付JS请求JSON
	 * @param payPackage
	 * @param appId
	 * @param paternerKey
	 * @param paySignkey	appkey
	 * @return
	 */
	public static String generatePayJsRequestJson(PayPackage payPackage,
				String appId,
				String paternerKey,
				String paySignkey){
		Map<String, String> mapP = MapUtil.objectToMap(payPackage);
		String package_ = SignatureUtil.generatePackage(mapP, paternerKey);
		PayJsRequest payJsRequest = new PayJsRequest();
		payJsRequest.setAppId(appId);
		payJsRequest.setNonceStr(UUID.randomUUID().toString());
		payJsRequest.setPackage_(package_);
		payJsRequest.setSignType("sha1");
		payJsRequest.setTimeStamp(System.currentTimeMillis()/1000+"");
		Map<String, String> mapS = MapUtil.objectToMap(payJsRequest,"signType","paySign");
		String paySign = SignatureUtil.generatePaySign(mapS,paySignkey);
		payJsRequest.setPaySign(paySign);
		return JsonUtil.toJSONString(payJsRequest);
	}



	/**
	 * 生成Native支付JS请求URL
	 * @param appid
	 * @param productid
	 * @param paySignkey
	 * @return
	 */
	public static String generatePayNativeRequestURL(
			String appid,
			String productid,
			String paySignkey){

		PayNativeRequest payNativeRequest = new PayNativeRequest();
		payNativeRequest.setAppid(appid);
		payNativeRequest.setNoncestr(UUID.randomUUID().toString());
		payNativeRequest.setProductid(productid);
		payNativeRequest.setTimestamp(System.currentTimeMillis()/1000+"");
		Map<String, String> mapS = MapUtil.objectToMap(payNativeRequest,"sign");
		String sign = SignatureUtil.generatePaySign(mapS,paySignkey);
		payNativeRequest.setSign(sign);

		Map<String, String> map = MapUtil.objectToMap(payNativeRequest);
		return "weixin://wxpay/bizpayurl?" + MapUtil.mapJoin(map, false, false);
	}

	/**
	 * 生成 native 支付回复XML
	 * @param payPackage
	 * @param appId
	 * @param retCode 0 正确
	 * @param retErrMsg
	 * @param paternerKey
	 * @return
	 */
	public static String generatePayNativeReplyXML(PayPackage payPackage,
			String appId,
			String retCode,
			String retErrMsg,
			String paternerKey){

		PayNativeReply payNativeReply = new PayNativeReply();
		payNativeReply.setAppid(appId);
		payNativeReply.setNoncestr(UUID.randomUUID().toString());
		payNativeReply.setRetcode(retCode);
		payNativeReply.setReterrmsg(retErrMsg);
		payNativeReply.setTimestamp(System.currentTimeMillis()+"");
		String package_ = SignatureUtil.generatePackage(MapUtil.objectToMap(payPackage),paternerKey);
		payNativeReply.setPackage_(package_);
		payNativeReply.setSignmethod("sha1");
		String appSignature = SignatureUtil.generatePackage(
									MapUtil.objectToMap(payNativeReply,"appsignature","signmethod"),
									paternerKey);
		payNativeReply.setAppsignature(appSignature);

		return XMLConverUtil.convertToXML(payNativeReply);
	}



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


	public static String generateMchOrderReplyXML(
			String result_code,
			String return_msg
			)
	{
		MchOrderInfoResultRelay infoResultRelay=new MchOrderInfoResultRelay();
		infoResultRelay.setReturn_code(result_code);
		infoResultRelay.setReturn_msg(return_msg);
		String xmlstr= XMLConverUtil.convertToXML(infoResultRelay);
		return xmlstr;
	}

	//MCH -------------------------------------------------


	/**
	 * (MCH)生成支付JS请求对象
	 * @param prepay_id	预支付订单号
	 * @param appId
	 * @param key 商户支付密钥
	 * @return
	 */
	public static String generateMchPayJsRequestJson(String prepay_id,String appId,String key){
		String package_ = "prepay_id="+prepay_id;
		PayJsRequest payJsRequest = new PayJsRequest();
		payJsRequest.setAppId(appId);
		payJsRequest.setNonceStr(UUID.randomUUID().toString().replace("-", ""));
		payJsRequest.setPackage_(package_);
		payJsRequest.setSignType("MD5");
		payJsRequest.setTimeStamp(System.currentTimeMillis()/1000+"");
		//@fantycool 提交修正bug
		//Map<String, String> mapS = MapUtil.objectToMap(payJsRequest,"signType","paySign");
		Map<String, String> mapS = MapUtil.objectToMap(payJsRequest);
		String paySign = SignatureUtil.generateSign(mapS,key);
		payJsRequest.setPaySign(paySign);
		return JsonUtil.toJSONString(payJsRequest);
	}


	/**
	 * (MCH)生成Native支付请求URL
	 * @param appid
	 * @param mch_id
	 * @param productid
	 * @param key
	 * @return
	 */
	public static String generateMchPayNativeRequestURL(
			String appid,
			String mch_id,
			String productid,
			String key){

		PayNativeRequest payNativeRequest = new PayNativeRequest();
		payNativeRequest.setAppid(appid);
		payNativeRequest.setNoncestr(UUID.randomUUID().toString().replace("-", ""));
		payNativeRequest.setProductid(productid);
		payNativeRequest.setTimestamp(System.currentTimeMillis()/1000+"");
		Map<String, String> mapS = MapUtil.objectToMap(payNativeRequest,"sign");
		mapS.put("mch_id",mch_id);
		String sign = SignatureUtil.generatePaySign(mapS,key);
		payNativeRequest.setSign(sign);

		Map<String, String> map = MapUtil.objectToMap(payNativeRequest);
		return "weixin://wxpay/bizpayurl?" + MapUtil.mapJoin(map, false, false);
	}

	/**
	 * (MCH)生成 native 支付回复XML
	 * @param mchPayNativeReply
	 * @param key
	 * @return
	 */
	public static String generateMchPayNativeReplyXML(MchPayNativeReply mchPayNativeReply,String key){
		Map<String, String> map = MapUtil.objectToMap(mchPayNativeReply);
		String sign = SignatureUtil.generateSign(map,key);
		mchPayNativeReply.setSign(sign);
		return XMLConverUtil.convertToXML(mchPayNativeReply);
	}

	/**
	 * (MCH)生成支付APP请求数据
	 * @param prepay_id	预支付订单号
	 * @param appId
	 * @param partnerid 商户平台号
	 * @param key 商户支付密钥
	 * @return
	 */
	public static MchPayApp generateMchAppData(String prepay_id,String appId,String partnerid,String key){
		Map<String, String> wx_map = new LinkedHashMap<String, String>();
		wx_map.put("appid", appId);
		wx_map.put("partnerid", partnerid);
		wx_map.put("prepayid", prepay_id);
		wx_map.put("package", "Sign=WXPay");
		wx_map.put("noncestr", UUID.randomUUID().toString().replace("-", ""));
		wx_map.put("timestamp", System.currentTimeMillis()/1000+"");
		String sign = SignatureUtil.generateSign(wx_map,key);
		MchPayApp mchPayApp = new MchPayApp();
		mchPayApp.setAppid(appId);
		mchPayApp.setPartnerid(partnerid);
		mchPayApp.setPrepayid(prepay_id);
		mchPayApp.setPackage_(wx_map.get("package"));
		mchPayApp.setNoncestr(wx_map.get("noncestr"));
		mchPayApp.setTimestamp(wx_map.get("timestamp"));
		mchPayApp.setSign(sign);
		return mchPayApp;
	}

	/**
	 * 生成代扣签约URL
	 * @param papayEntrustweb
	 * @param key
	 * @return
	 */
	public static String generatePapayEntrustwebURL(PapayEntrustweb papayEntrustweb,String key){
		Map<String,String> map = MapUtil.objectToMap( papayEntrustweb);
		String sign = SignatureUtil.generateSign(map,key);
		map.put("sign", sign);
		String params = MapUtil.mapJoin(map, false, true);
		return "https://api.mch.weixin.qq.com/papay/entrustweb?"+params;
	}


}