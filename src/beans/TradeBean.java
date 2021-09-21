package beans;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import com.ado.SqlADO;
import com.tools.ToolBox;

public class TradeBean {
	private int id;
	private String liushuiid;/*订单号*/
	private String cardinfo;/*卡片信息，刷卡交易时有效*/
	private int price;/*商品价格*/
	
	/*用户手机号码*/
	private String mobilephone; 
	
	private Timestamp receivetime; /**/
	private int tradetype;/*交易类型*/
    private int goodmachineid;/*机器编号*/
    private int goodroadid;/*货道编号*/
    private int changestatus;/*支付状态*/
    private int sendstatus;/*出货状态*/
    private String SmsContent;/*短信内容*/
    private int paystatus;
    private String orderid;/**/
    
    private int changes;/*找零金额*/
    
    
	private String goodsName;/*商品名称*/
	
	private String inneridname;
	
	private String xmlstr;
	
	private int status;
	
	private String tradeid;
	
	private int has_jiesuan;
	
	private int has_feeback;
	private String retmes;
	private int sErr;
	
	public int getHas_jiesuan() {
		return has_jiesuan;
	}
	public void setHas_jiesuan(int has_jiesuan) {
		this.has_jiesuan = has_jiesuan;
	}
	public int getHas_feeback() {
		return has_feeback;
	}
	public void setHas_feeback(int has_feeback) {
		this.has_feeback = has_feeback;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getXmlstr() {
		return xmlstr;
	}
	public void setXmlstr(String xmlstr) {
		this.xmlstr = xmlstr;
	}
	

	
	public String getInneridname() {
		return inneridname;
	}
	public void setInneridname(String inneridname) {
		this.inneridname = inneridname;
	}
	public int getBill_credit() {
		return bill_credit;
	}
	public void setBill_credit(int bill_credit) {
		this.bill_credit = bill_credit;
	}
	public int getCoin_credit() {
		return coin_credit;
	}
	public void setCoin_credit(int coin_credit) {
		this.coin_credit = coin_credit;
	}
	private int bill_credit;/*纸币实收金额*/
	
	private int coin_credit;/*硬币实收金额*/
	
	public int getId() {
		return id;
	}
	public String getLiushuiid() {
		return liushuiid;
	}
	public String getCardinfo() {
		return cardinfo;
	}
	public int getPrice() {
		return price;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public Timestamp getReceivetime() {
		return receivetime;
	}
	public int getTradetype() {
		return tradetype;
	}
	public int getGoodmachineid() {
		return goodmachineid;
	}
	public int getGoodroadid() {
		return goodroadid;
	}
	public int getChangestatus() {
		return changestatus;
	}
	public int getSendstatus() {
		return sendstatus;
	}
	public String getSmsContent() {
		return SmsContent;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setLiushuiid(String liushuiid) {
		if(liushuiid==null)
			this.liushuiid ="";
		else
			this.liushuiid = liushuiid;
	}
	public void setCardinfo(String ussdinfo) {
		if(ussdinfo==null)
			this.cardinfo ="";
		else
			this.cardinfo = ussdinfo;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public void setMobilephone(String mobilephone) {
		if(mobilephone==null)
			this.mobilephone="";
		else
			this.mobilephone = mobilephone;
	}
	public void setReceivetime(Timestamp receivetime) {
		this.receivetime = receivetime;
	}
	public void setTradetype(int tradetype) {
		this.tradetype = tradetype;
	}
	public void setGoodmachineid(int goodmachineid) {
		this.goodmachineid = goodmachineid;
	}
	public void setGoodroadid(int goodroadid) {
		this.goodroadid = goodroadid;
	}
	public void setChangestatus(int changestatus) {
		this.changestatus = changestatus;
	}
	public void setSendstatus(int sendstatus) {
		this.sendstatus = sendstatus;
	}
	public void setSmsContent(String smsContent) {
		if(smsContent==null)
			SmsContent ="";
		else
			SmsContent = smsContent;
	}
	public void setGoodsName(String string) {
		
		goodsName=string;
	}
	
	public String getGoodsName() {
		
		return goodsName;
	}
	/**
	 * @return the orderid
	 */
	public String getOrderid() {
		if(orderid==null)
		{
			return "";
		}else
		{
			return orderid;
		}
	}
	/**
	 * @param orderid the orderid to set
	 */
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public int getChanges() {
		return changes;
	}
	public void setChanges(int changes) {
		this.changes = changes;
	}
	public int getPaystatus() {
		return paystatus;
	}
	public void setPaystatus(int paystatus) {
		this.paystatus = paystatus;
	}

	public String getTradeid() {
		return tradeid;
	}
	public void setTradeid(String tradeid) {
		this.tradeid = tradeid;
	}
	public String getRetmes() {
		return retmes;
	}
	public void setRetmes(String retmes) {
		this.retmes = retmes;
	}
	
	public int getSErr() {
		return sErr;
	}
	public void setSErr(int sErr) {
		this.sErr = sErr;
	}

	public static ArrayList<TradeBean> getTradeByVenderLst(int lasttime, int status) {
		Date d=new Date(System.currentTimeMillis()-lasttime);
		String where="where changestatus=1 "
				+ "and sendstatus=0 and '"+ ToolBox.getTimeLongString(d) +"'>receivetime";
		System.out.println(where);
		return SqlADO.getTradeListFromTemp(where);
	}
}
