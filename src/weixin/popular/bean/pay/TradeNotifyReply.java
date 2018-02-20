package weixin.popular.bean.pay;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import weixin.popular.bean.AdaptorCDATA;


@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class TradeNotifyReply {
	
	@XmlElement(name="appid")
	private String appid;
	
	@XmlElement(name="nonce_str")
	private String nonce_str;

	

	@XmlElement(name="return_code")
	private String return_code;
	
	@XmlElement(name="return_msg")
	private String return_msg;
	
	@XmlElement(name="mch_id")
	private String mch_id;
	
	@XmlElement(name="prepay_id")
	private String prepay_id;
	
		@XmlElement(name="result_code")
	private String result_code;
	
	@XmlElement(name="err_code_des")
	private String err_code_des;
	
	
	@XmlElement(name="sign")
	private String sign;


	public String getAppid() {
		return appid;
	}


	public void setAppid(String appid) {
		this.appid = appid;
	}


	public String getNonce_str() {
		return nonce_str;
	}


	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}


	public String getReturn_code() {
		return return_code;
	}


	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}


	public String getReturn_msg() {
		return return_msg;
	}


	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}


	public String getMch_id() {
		return mch_id;
	}


	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}


	public String getPrepay_id() {
		return prepay_id;
	}


	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
	}


	public String getResult_code() {
		return result_code;
	}


	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}


	public String getErr_code_des() {
		return err_code_des;
	}


	public void setErr_code_des(String err_code_des) {
		this.err_code_des = err_code_des;
	}


	public String getSign() {
		return sign;
	}


	public void setSign(String sign) {
		this.sign = sign;
	}
		
	

}
