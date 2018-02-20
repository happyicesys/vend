package weixin.popular.bean;

public class clsWxPara {
	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getTrade_type() {
		return trade_type;
	}
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}
	private String appid;
	private String mch_id;
	private String openid;
	private String trade_type;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public static clsWxPara getInstance() {
		if(instance==null)
		{
			ReadPara();
		}
		return instance;
	}
	private String key;
	
	private String secret;
	
	private static clsWxPara instance;
	
	public static void  ReadPara()
	{
		instance=new clsWxPara();
		instance.appid="wxb94791a66ea6db9a";
		instance.mch_id="1301930801";
		instance.openid="";
		instance.trade_type="NATIVE";
		instance.key="44115A8D5A4E8EFDF04F58FCFCF393B9";		/*这两个参数需要提供*/
		instance.secret="";/*这两个参数需要提供*/
	}

}
