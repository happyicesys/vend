package beans;

public class Cls_AliTradeLog {
//	<?xml version="1.0" encoding="utf-8"?>
//
//	<notify>
//	  <gmt_payment>2015-02-16 10:17:10</gmt_payment>
//	  <partner>2088811497883683</partner>
//	  <buyer_email>friends2030@163.com</buyer_email>
//	  <trade_no>2015021600001000770044430856</trade_no>
//	  <total_fee>0.01</total_fee>
//	  <gmt_create>2015-02-16 10:16:42</gmt_create>
//	  <out_trade_no>150216101642000001229194</out_trade_no>
//	  <subject>扫码-怡宝-怡宝-数量1</subject>
//	  <trade_status>TRADE_SUCCESS</trade_status>
//	  <qrcode>gdqqhod1q7q7kx5377</qrcode>
//	</notify>

private int    id;
private String   gmt_payment;
private String	partner;
private String	buyer_email;
private String	trade_no;
private String	total_fee;
private String	gmt_create;
private String	out_trade_no;
private String	subject;
private String	trade_status;
private String	qrcode;
private String	userid;
private String	notifyXML;
private String	machineid;
private String	goodsid;
private int	transfor_status;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getGmt_payment() {
	return gmt_payment;
}
public void setGmt_payment(String gmt_payment) {
	this.gmt_payment = gmt_payment;
}
public String getPartner() {
	return partner;
}
public void setPartner(String partner) {
	this.partner = partner;
}
public String getBuyer_email() {
	return buyer_email;
}
public void setBuyer_email(String buyer_email) {
	this.buyer_email = buyer_email;
}
public String getTrade_no() {
	return trade_no;
}
public void setTrade_no(String trade_no) {
	this.trade_no = trade_no;
}
public String getTotal_fee() {
	return total_fee;
}
public void setTotal_fee(String total_fee) {
	this.total_fee = total_fee;
}
public String getGmt_create() {
	return gmt_create;
}
public void setGmt_create(String gmt_create) {
	this.gmt_create = gmt_create;
}
public String getOut_trade_no() {
	return out_trade_no;
}
public void setOut_trade_no(String out_trade_no) {
	this.out_trade_no = out_trade_no;
}
public String getSubject() {
	return subject;
}
public void setSubject(String subject) {
	this.subject = subject;
}
public String getTrade_status() {
	return trade_status;
}
public void setTrade_status(String trade_status) {
	this.trade_status = trade_status;
}
public String getQrcode() {
	return qrcode;
}
public void setQrcode(String qrcode) {
	this.qrcode = qrcode;
}
public String getUserid() {
	return userid;
}
public void setUserid(String userid) {
	this.userid = userid;
}
public String getNotifyXML() {
	return notifyXML;
}
public void setNotifyXML(String notifyXML) {
	this.notifyXML = notifyXML;
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
public int getTransfor_status() {
	return transfor_status;
}
public void setTransfor_status(int transfor_status) {
	this.transfor_status = transfor_status;
}



}
