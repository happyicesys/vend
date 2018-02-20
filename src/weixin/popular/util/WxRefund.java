package weixin.popular.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.Date;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.ado.SqlADO;
import com.tools.ToolBox;

import beans.RefundBean;
import beans.TradeBean;
import beans.UserBean;
import beans.clsGroupBean;
import weixin.popular.api.PayMchAPI;
import weixin.popular.bean.paymch.SecapiPayRefund;
import weixin.popular.bean.paymch.SecapiPayRefundResult;
import weixin.popular.client.LocalHttpClient;

public class WxRefund {

	public static void main(String[] a) 
	{
		WxRefund wxrefound=new WxRefund();
		wxrefound.setDevice_info("0001");
		wxrefound.setNonce_str(ToolBox.getMd5(ToolBox.MakeTradeID(0)));
		wxrefound.setOp_user_id("1");
		wxrefound.setOut_refund_no("1231293");
		wxrefound.setOut_trade_no("2016060109255406020130101");
		wxrefound.setRefund_fee(1);
		wxrefound.setRefund_fee_type("CNY");
		wxrefound.setTotal_fee(1);
		//wxrefound.setTransaction_id(n);
		SecapiPayRefundResult ret= wxrefound.Refund(clsGroupBean.getGroup(3));
		System.out.println(ret.getReturn_msg());
		System.out.println(XMLConverUtil.convertToXML( ret));
		
	}
	
    public final static void main1(String[] args) throws Exception {
    	String mch_id="1340987401";
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File("D:/key/1340987401.p12"));
        try {
            keyStore.load(instream, mch_id.toCharArray());
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom().useSSL()
                .loadKeyMaterial(keyStore, mch_id.toCharArray())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER
                );
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        try {
        	
        	HttpPost httppost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");
            System.out.println("executing request" + httppost.getRequestLine());

            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();

                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    System.out.println("Response content length: " + entity.getContentLength());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
                    String text;
                    while ((text = bufferedReader.readLine()) != null) {
                        System.out.println(text);
                    }
                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }
    
    public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getOut_refund_no() {
		return out_refund_no;
	}

	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}

	public int getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}

	public int getRefund_fee() {
		return refund_fee;
	}

	public void setRefund_fee(int refund_fee) {
		this.refund_fee = refund_fee;
	}

	public String getRefund_fee_type() {
		return refund_fee_type;
	}

	public void setRefund_fee_type(String refund_fee_type) {
		this.refund_fee_type = refund_fee_type;
	}

	public String getOp_user_id() {
		return op_user_id;
	}

	public void setOp_user_id(String op_user_id) {
		this.op_user_id = op_user_id;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	private String  device_info;
    private String  nonce_str;
    private String  out_trade_no;
    private String  transaction_id;
    private String  out_refund_no;
    private int total_fee;
    private int refund_fee;  
    private String refund_fee_type;   
    private String op_user_id;
    private String sign;
    private String msg;
    
    private int issuccess;
    public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getIssuccess() {
		return issuccess;
	}

	public void setIssuccess(int issuccess) {
		this.issuccess = issuccess;
	}

	public SecapiPayRefundResult Refund(clsGroupBean groupBean) 
    {
    	String mch_id=groupBean.getWx_mch_id();
    	LocalHttpClient.initMchKeyStore(mch_id,"D:/key/"+mch_id+".p12");
    	SecapiPayRefund secapiPayRefund = new SecapiPayRefund();
    	secapiPayRefund.setAppid(groupBean.getWx_appid());
    	secapiPayRefund.setMch_id(groupBean.getWx_mch_id());
    	secapiPayRefund.setDevice_info(this.device_info);
    	secapiPayRefund.setNonce_str(this.nonce_str);
    	secapiPayRefund.setOp_user_id(op_user_id);
    	secapiPayRefund.setOut_refund_no(out_refund_no);
    	secapiPayRefund.setOut_trade_no(out_trade_no);
    	secapiPayRefund.setRefund_fee(refund_fee);
    	secapiPayRefund.setTotal_fee(total_fee);
    	secapiPayRefund.setTransaction_id(null);
    	SecapiPayRefundResult ret= PayMchAPI.secapiPayRefund(secapiPayRefund, groupBean.getWx_key());
    	return ret;
    	
    }

	public static WxRefund PayBack(TradeBean tb, UserBean ub, int groupid, String string) {
		WxRefund wxrefound=new WxRefund();
		wxrefound.setDevice_info(String.format("%s", tb.getGoodmachineid()));
		wxrefound.setNonce_str(ToolBox.getMd5(ToolBox.MakeTradeID(0)));
		if(ub!=null)
		{
			wxrefound.setOp_user_id(ub.getId()+"-"+ub.getAdminusername());
		}
		else
		{
			wxrefound.setOp_user_id("平台自动退款");
		}
		wxrefound.setOut_refund_no(ToolBox.getTimeString()+String.format("%06d", ToolBox.getRandomNumber()));
		wxrefound.setOut_trade_no(tb.getOrderid());
		wxrefound.setRefund_fee(tb.getPrice());
		wxrefound.setRefund_fee_type("CNY");
		wxrefound.setTotal_fee(tb.getPrice());
		//wxrefound.setTransaction_id(n);
		SecapiPayRefundResult ret= wxrefound.Refund(clsGroupBean.getGroup(groupid));
		if(ret!=null)
		{
			//System.out.println(ret.getReturn_msg());
			if(ret.getResult_code().equals("SUCCESS"))
			{
				wxrefound.setMsg("退款成功");
				wxrefound.setIssuccess(1);
    			
    			/*更新交易，设置为结算和设置为已经退款*/
    			tb.setHas_feeback(1);
    			tb.setHas_jiesuan(1);
    			//SqlADO.updateTradeBean(tb);
			}
			else
			{
				wxrefound.setMsg("退款失败，"+ret.getErr_code_des());
			}
		}
		else
		{
			wxrefound.setMsg("微信服务器返回失败");
		}
		RefundBean refundBean = new RefundBean();
		refundBean.setGoodsname(tb.getGoodsName());
		refundBean.setGoodsroadid(tb.getGoodroadid());
		refundBean.setIssuccess(wxrefound.getIssuccess());
		refundBean.setOp_user(wxrefound.getOp_user_id());
		refundBean.setOrderid(wxrefound.getOut_trade_no());
		refundBean.setRefund_amount(tb.getPrice());
		refundBean.setRefundid(wxrefound.getOut_refund_no());
		refundBean.setRefundtime(new Date());
		refundBean.setRet_msg(wxrefound.getMsg());
		refundBean.setTerminalid(tb.getGoodmachineid());
		refundBean.setTrade_id(tb.getId());
		refundBean.setTrade_type(tb.getTradetype());
		refundBean.setGroupid(groupid);
		SqlADO.AddRefundRecordToDb(refundBean);
		return wxrefound;
	}
}
