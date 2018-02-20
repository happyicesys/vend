package beans;

public class Cls_AliQrCodeLog 
{
	private int id;
	private String querystring;
	private String response;
	private String machineid;
	private String goodsid;
	private String sendtime;
	private int isok;

	private String 	qrcode;
	private String 	qrcode_img_url;

	
	public String getQrcode() {
		return qrcode;
	}
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
	public String getQrcode_img_url() {
		return qrcode_img_url;
	}
	public void setQrcode_img_url(String qrcode_img_url) {
		this.qrcode_img_url = qrcode_img_url;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getQuerystring() {
		return querystring;
	}
	public void setQuerystring(String querystring) {
		this.querystring = querystring;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getMachineid() {
		return machineid;
	}
	public void setMachineid(String machineid) {
		this.machineid = machineid;
	}
	public String getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}
	public String getSendtime() {
		return sendtime;
	}
	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}
	public int getIsok() {
		return isok;
	}
	public void setIsok(int isok) {
		this.isok = isok;
	}
	
}
