package beans;

import java.sql.Timestamp;

public class PortBean {
	public static final int SLOT_OBJ_SIZE = 11;
	public static final int SLOTNAME_OBJ_SIZE = 16;
	private int id;
	private int machineid;
	private int innerid;
	private int amount;
	private int price;
	private int discount;
	private String goodroadname;
	private Timestamp updatetime;
	private int error_id;
	
	private String desc;
	private int PauseFlg;
	private String errorinfo;
	private Timestamp lastErrorTime;
	private int huodongid;
	
	private String product_img;
	
	private Timestamp updateqrtime;
	public Timestamp getUpdateqrtime() {
		return updateqrtime;
	}
	public void setUpdateqrtime(Timestamp updateqrtime) {
		this.updateqrtime = updateqrtime;
	}
	public Timestamp getUpdateqrtime2() {
		return updateqrtime2;
	}
	public void setUpdateqrtime2(Timestamp updateqrtime2) {
		this.updateqrtime2 = updateqrtime2;
	}
	private Timestamp updateqrtime2;

	private String Inneridname;

	public String getInneridname() {
		return Inneridname;
	}
	public void setInneridname(String inneridname) 
	{
		Inneridname = inneridname;
	}
	private String 	qrcode;
	private String 	qrcode2;
	
	public String getQrcode2() {
		return qrcode2;
	}
	public void setQrcode2(String qrcode2) {
		this.qrcode2 = qrcode2;
	}
	public String getAl_trade() {
		return al_trade;
	}
	public void setAl_trade(String al_trade) {
		this.al_trade = al_trade;
	}
	public String getAl_trade2() {
		return al_trade2;
	}
	public void setAl_trade2(String al_trade2) {
		this.al_trade2 = al_trade2;
	}
	public String getWx_qrcode2() {
		return wx_qrcode2;
	}
	public void setWx_qrcode2(String wx_qrcode2) {
		this.wx_qrcode2 = wx_qrcode2;
	}
	public String getWx_trade() {
		return wx_trade;
	}
	public void setWx_trade(String wx_trade) {
		this.wx_trade = wx_trade;
	}
	public String getWx_trade2() {
		return wx_trade2;
	}
	public void setWx_trade2(String wx_trade2) {
		this.wx_trade2 = wx_trade2;
	}
	private String 	al_trade;
	private String 	al_trade2;
	
	private String 	wx_qrcode;
	private String  wx_qrcode2;
	
	private String 	wx_trade;
	private String  wx_trade2;

	
	
	public String getQrcode() {
		return qrcode;
	}
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public String getWx_qrcode() {
		return wx_qrcode;
	}
	public void setWx_qrcode(String wx_qrcode) {
		this.wx_qrcode = wx_qrcode;
	}

	public int getError_id() {
		return error_id;
	}
	public void setError_id(int error_id) {
		this.error_id = error_id;
	}

	public int getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}
	public int getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(int deviceid) {
		this.deviceid = deviceid;
	}
	private int goodsid;
	
	private int deviceid;
	
	private int capacity;
	public int getId() {
		return id;
	}
	public int getMachineid() {
		return machineid;
	}
	public int getInnerid() {
		return innerid;
	}
	public int getAmount() {
		return amount;
	}

	public String getGoodroadname() {
		return goodroadname;
	}
	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public String getErrorinfo() {
		return errorinfo;
	}
	public Timestamp getLastErrorTime() {
		return lastErrorTime;
	}
	public int getHuodongid() {
		return huodongid;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setMachineid(int machineid) {
		this.machineid = machineid;
	}
	public void setInnerid(int innerid) {
		this.innerid = innerid;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setGoodroadname(String goodroadname) {
		if(goodroadname==null)
			this.goodroadname = "";
		else
			this.goodroadname = goodroadname;
	}
	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	public void setErrorinfo(String errorinfo) {
		if(errorinfo==null)
			this.errorinfo="";
		else
			this.errorinfo = errorinfo;
	}
	public void setLastErrorTime(Timestamp lastErrorTime) {
		this.lastErrorTime = lastErrorTime;
	}
	public void setHuodongid(int huodongid) {
		this.huodongid = huodongid;
	}
	/**
	 * @return the capacity
	 */
	public int getCapacity() {
		return capacity;
	}
	/**
	 * @param capacity the capacity to set
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public String getProduct_img() {
		return product_img;
	}
	public void setProduct_img(String product_img) {
		this.product_img = product_img;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	public int getPauseFlg() {
		return PauseFlg;
	}
	public void setPauseFlg(int pauseFlg) {
		PauseFlg = pauseFlg;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
