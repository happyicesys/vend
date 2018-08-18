package beans;

import java.sql.Timestamp;
import java.util.Vector;

public class VenderBean {
	private int id;
	private int AdminId;
	private Timestamp BTime;
	private String TerminalName;
	private String TerminalAddress;
	private Timestamp UpdateTime;
	private boolean IsOnline;
	private int HuodongId;
	private String SellerTyp;
	private int GoodsPortCount;
	private String TipMesOnLcd;
	private boolean CanUse;
	private int queueMaxLength;
	private int MdbDeviceStatus;
	
	private int gprs_Sign;
	
	private boolean isActive;
	
	private int groupid;
	
	private int temperature;
	
	private byte[] priavte_key;
	
	private byte[] public_key;
	
	private int flags1;
	
	private int flags2;
	
	private int function_flg;
	
	private short gprs_event_flg;
	
	private String vmc_firmfile;
	private int code_ver;
	
	private int m_last_fid;
	private int m_cmdType;
	private String m_last_send_string;
	
	private int auto_refund=0;
	
	private int manual_refund=0;
	
	private int temp_alert=0;
	
	private int m_AllowUpdateGoodsByPc=0;/*是否允许由客户端更新产品编号信息*/
	
	private String TemperUpdateTime;
	
	private int prev_temp=0;
	private int temp_alert_loop=0;
	private int is_alert_sent;
	private String TemperLoopStartTime;
	private int is_coin_alert_sent;
	private int is_offline_alert_sent;
	private int offline_alert;
	private int offlinetimes;
	
	public String getTemperUpdateTime() {
		return TemperUpdateTime;
	}

	public void setTemperUpdateTime(String temperUpdateTime) {
		TemperUpdateTime = temperUpdateTime;
	}
	private static Vector<VenderBean> lst=new Vector<VenderBean>();
	
	private int autoTransfer;
	private double autoTransferRation;
	
	public double getAutoTransferRation() {
		return autoTransferRation;
	}

	public void setAutoTransferRation(double autoTransferRation) {
		this.autoTransferRation = autoTransferRation;
	}

	public int getAutoTransfer() {
		return autoTransfer;
	}

	public void setAutoTransfer(int autoTransfer) {
		this.autoTransfer = autoTransfer;
	}

	public String ChkFrameRepeat(int fid,int type) 
	{
		if((fid==m_last_fid)&&(m_cmdType==type))
		{
			return m_last_send_string;
		}
		else
		{
			return null;
		}
	}
	
	/*
	 * 如果测试到这次的数据帧没有发送过，就保存最新的数据帧
	 * */
	public void SaveLsteFrameData(int frameid,int frametype,String frame_ret_string) 
	{
		m_last_send_string=frame_ret_string;
		m_cmdType=frametype;
		m_last_fid=frameid;
	}
	
	public static int AddVender(VenderBean vender) 
	{
		lst.addElement(vender);
		return lst.size();
	}
	
	/**
	 * 检测列表中是否存在指定的机器
	 * @param vender 机器对象
	 * @return 返回队列中的机器对象，如果不存在就返回null
	 */
	public static VenderBean ChkVender(VenderBean vender)
	{
		for(VenderBean v:lst)
		{
			if(v.id==vender.id)
			{
				return v;
			}
		}
		return null;
	}
	
	/**
	 * 检测列表中是否存在指定编号的机器
	 * @param machineid 机器编号
	 * @return 返回机器对象，如果不存在，就返回null
	 */
	public static VenderBean ChkVender(int machineid)
	{
		for(VenderBean v:lst)
		{
			if(v.id==machineid)
			{
				return v;
			}
		}
		return null;
	}
	
	/**
	 * 将确定的对象从列表中移除
	 */
	public void RemoveVender() 
	{
		lst.remove(this);
	}
	
	/**
	 * 清空售货机列表中，所有的售货机对象
	 */
	public static void freshVenderPara() 
	{
		lst.clear();
		
	}

	/**
	 * 移除指定的机器，从而达到刷新参数的目的
	 * @param vender
	 */
	public static void freshVenderPara(VenderBean vender) 
	{
		VenderBean vb=ChkVender(vender);
		lst.remove(vb);
	}
	
	/**
	 * 移除指定的编号的机器，从而达到刷新参数的目的
	 * @param id
	 */
	public static void freshVenderPara(int id) 
	{
		VenderBean vb=ChkVender(id);
		lst.remove(vb);
	}
	
	public int getCode_ver() {
		if(code_ver==0)
		{
			code_ver=0x174;
		}
		return code_ver;
	}
	public void setCode_ver(int code_ver) {
		this.code_ver = code_ver;
	}
	public static final int FUNC_IS_TERMPER_VALID=(1<<0);
	public static final int FUNC_IS_MDB_BILL_VALID=(1<<1);
	public static final int FUNC_IS_MDB_COIN_VALID=(1<<2);
	public static final int FUNC_IS_MDB_CASHLESS_VALID=(1<<3);
	
	public static final int MDB_COMMUNICATION_COIN=(1<<0);
	public static final int MDB_COMMUNICATION_BILL=(1<<1);
	public static final int MDB_COMMUNICATION_CASHLESS=(1<<2);
	
	//temperature degree Celcius x 10
	public static final int TEMP_ALERT_LIMIT = -120;
	//time looping to send alert email
	public static final int TEMP_ALERT_LOOP = 12;
	//in the unit of minutes
	public static final int TEMP_LOOP_TIMING = 10;


	
	public String getPos_TERM_NO() {
		return pos_TERM_NO;
	}
	public void setPos_TERM_NO(String pos_TERM_NO) {
		this.pos_TERM_NO = pos_TERM_NO;
	}
	public String getPos_INST_NO() {
		return pos_INST_NO;
	}
	public void setPos_INST_NO(String pos_INST_NO) {
		this.pos_INST_NO = pos_INST_NO;
	}
	public String getPos_MERCH_NO() {
		return pos_MERCH_NO;
	}
	public void setPos_MERCH_NO(String pos_MERCH_NO) {
		this.pos_MERCH_NO = pos_MERCH_NO;
	}
	public String getPos_CLIENTAUTHCOD() {
		return pos_CLIENTAUTHCOD;
	}
	public void setPos_CLIENTAUTHCOD(String pos_CLIENTAUTHCOD) {
		this.pos_CLIENTAUTHCOD = pos_CLIENTAUTHCOD;
	}
	public String getPos_USERNAME() {
		return pos_USERNAME;
	}
	public void setPos_USERNAME(String pos_USERNAME) {
		this.pos_USERNAME = pos_USERNAME;
	}
	public String getPos_PWD() {
		return pos_PWD;
	}
	public void setPos_PWD(String pos_PWD) {
		this.pos_PWD = pos_PWD;
	}
	public String getTelNum() {
		if(TelNum==null)
		{
			return "";
		}
		else
		{
			return TelNum;
		}
	}
	public void setTelNum(String telNum) {
		TelNum = telNum;
	}
	private String pos_TERM_NO;
	private String pos_INST_NO;
	private String pos_MERCH_NO;
	
	private String pos_CLIENTAUTHCOD;
	private String pos_USERNAME;
	private String pos_PWD;
	private String TelNum;

	
	public int getBills() {
		return bills;
	}
	public void setBills(int bills) {
		this.bills = bills;
	}
	public int getCoinAtbox() {
		return coinAtbox;
	}
	public void setCoinAtbox(int coinAtbox) {
		this.coinAtbox = coinAtbox;
	}
	public int getCoinAttube() {
		return coinAttube;
	}
	public void setCoinAttube(int coinAttube) {
		this.coinAttube = coinAttube;
	}
	private int bills;
	private int coinAtbox;
	private int coinAttube;
	
	private int IRErrCnt;
	
	private int LstSltE;
	
	
	
	public double getJindu() {
		return jindu;
	}
	public double getWeidu() {
		return weidu;
	}
	public void setJindu(double jindu) {
		this.jindu = jindu;
	}
	public void setWeidu(double weidu) {
		this.weidu = weidu;
	}
	private double jindu,weidu;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAdminId() {
		return AdminId;
	}
	public Timestamp getBTime() {
		return BTime;
	}
	public String getTerminalName() {
		return TerminalName;
	}
	public String getTerminalAddress() {
		return TerminalAddress;
	}
	public Timestamp getUpdateTime() {
		return UpdateTime;
	}
	public boolean isIsOnline() {
		return IsOnline;
	}
	public int getHuodongId() {
		return HuodongId;
	}
	public String getSellerTyp() {
		return SellerTyp;
	}
	public int getGoodsPortCount() {
		return GoodsPortCount;
	}
	public String getTipMesOnLcd() {
		return TipMesOnLcd;
	}
	public boolean isCanUse() {
		return CanUse;
	}
	public int getQueueMaxLength() {
		return queueMaxLength;
	}
	public void setAdminId(int adminId) {
		AdminId = adminId;
	}
	public void setBTime(Timestamp bTime) {
		BTime = bTime;
	}
	public void setTerminalName(String terminalName) {
		if(terminalName==null)
			TerminalName="";
		else
			TerminalName = terminalName;
	}
	public void setTerminalAddress(String terminalAddress) {
		if(terminalAddress==null)
			TerminalAddress="";
		else
			TerminalAddress = terminalAddress;
	}
	public void setUpdateTime(Timestamp updateTime) {
		UpdateTime = updateTime;
	}
	public void setIsOnline(boolean isOnline) {
		IsOnline = isOnline;
	}
	public void setHuodongId(int huodongId) {
		HuodongId = huodongId;
	}
	public void setSellerTyp(String sellerTyp) {
		if(sellerTyp==null)
			SellerTyp="TCN-D900";
		else
			SellerTyp = sellerTyp;
	}
	public void setGoodsPortCount(int goodsPortCount) {
		GoodsPortCount = goodsPortCount;
	}
	public void setTipMesOnLcd(String tipMesOnLcd) {
		if(tipMesOnLcd==null)
			TipMesOnLcd ="";
		else
			TipMesOnLcd = tipMesOnLcd;
	}
	public void setCanUse(boolean canUse) {
		CanUse = canUse;
	}
	public void setQueueMaxLength(int queueMaxLength) {
		this.queueMaxLength = queueMaxLength;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public int getGroupid() {
		return groupid;
	}
	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}
	public int getMdbDeviceStatus() {
		return MdbDeviceStatus;
	}
	public void setMdbDeviceStatus(int mdbDeviceStatus) {
		MdbDeviceStatus = mdbDeviceStatus;
	}
	public int getGprs_Sign() {
		return gprs_Sign;
	}
	public void setGprs_Sign(int gprs_Sign) {
		this.gprs_Sign = gprs_Sign;
	}
	public int getTemperature() {
		return temperature;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	public int getFunction_flg() {
		return function_flg;
	}
	public void setFunction_flg(int function_flg) {
		this.function_flg = function_flg;
	}
	public byte[] getPriavte_key() {
		return priavte_key;
	}
	public void setPriavte_key(byte[] priavte_key) {
		this.priavte_key = priavte_key;
	}
	public byte[] getPublic_key() {
		return public_key;
	}
	public void setPublic_key(byte[] public_key) {
		this.public_key = public_key;
	}
	public int getFlags1() {
		return flags1;
	}
	public void setFlags1(int flags1) {
		this.flags1 = flags1;
	}
	public int getFlags2() {
		return flags2;
	}
	public void setFlags2(int flags2) {
		this.flags2 = flags2;
	}
	public short getGprs_event_flg() {
		return gprs_event_flg;
	}
	public void setGprs_event_flg(short gprs_event_flg) {
		this.gprs_event_flg = gprs_event_flg;
	}
	public String getVmc_firmfile() {
		return vmc_firmfile;
	}
	public void setVmc_firmfile(String vmc_firmfile) {
		this.vmc_firmfile = vmc_firmfile;
	}
	public int getLstSltE() {
		return LstSltE;
	}
	public void setLstSltE(int lstSltE) {
		LstSltE = lstSltE;
	}
	public int getIRErrCnt() {
		return IRErrCnt;
	}
	public void setIRErrCnt(int iRErrCnt) {
		IRErrCnt = iRErrCnt;
	}
	public int getLast_fid() {
		return m_last_fid;
	}
	public void setLast_fid(int m_last_fid) {
		this.m_last_fid = m_last_fid;
	}
	public String getLast_send_string() {
		return m_last_send_string;
	}
	public void setLast_send_string(String m_last_send_string) {
		this.m_last_send_string = m_last_send_string;
	}


	public int getM_cmdType() {
		return m_cmdType;
	}


	public void setM_cmdType(int m_cmdType) {
		this.m_cmdType = m_cmdType;
	}
	public String getId_Format() {
		return id_Format;
	}

	public void setId_Format(String id_Format) {
		this.id_Format = id_Format;
	}

	public int getAuto_refund() {
		return auto_refund;
	}

	public void setAuto_refund(int auto_refund) {
		this.auto_refund = auto_refund;
	}
	public int getManual_refund() {
		return manual_refund;
	}

	public void setManual_refund(int manual_refund) {
		this.manual_refund = manual_refund;
	}
	
	public int getTemp_alert() {
		return temp_alert;
	}

	public void setTemp_alert(int temp_alert) {
		this.temp_alert = temp_alert;
	}	
	
	public int getM_AllowUpdateGoodsByPc() {
		return m_AllowUpdateGoodsByPc;
	}

	public void setM_AllowUpdateGoodsByPc(int m_AllowUpdateGoodsByPc) {
		this.m_AllowUpdateGoodsByPc = m_AllowUpdateGoodsByPc;
	}
	private String id_Format;
	
	public void setPrev_temp(int prev_temp) {
		this.prev_temp = prev_temp;
	}
	
	public int getPrev_temp() {
		return prev_temp;
	}
	
	public void setTemp_alert_loop(int temp_alert_loop) {
		this.temp_alert_loop = temp_alert_loop;
	}
	
	public int getTemp_alert_loop() {
		return temp_alert_loop;
	}
	
	public void setIs_alert_sent(int is_alert_sent) {
		this.is_alert_sent = is_alert_sent;
	}
	
	public int getIs_alert_sent() {
		return is_alert_sent;
	}	
	
	public String getTemperLoopStartTime() {
		return TemperLoopStartTime;
	}

	public void setTemperLoopStartTime(String temperLoopStartTime) {
		this.TemperLoopStartTime = temperLoopStartTime;
	}	
	
	public void setIs_coin_alert_sent(int is_coin_alert_sent) {
		this.is_coin_alert_sent = is_coin_alert_sent;
	}
	
	public int getIs_coin_alert_sent() {
		return is_coin_alert_sent;
	}
	
	public void setIs_offline_alert_sent(int is_offline_alert_sent) {
		this.is_offline_alert_sent = is_offline_alert_sent;
	}
	
	public int getIs_offline_alert_sent() {
		return is_offline_alert_sent;
	}
	
	public void setOffline_alert(int offline_alert) {
		this.offline_alert = offline_alert;
	}
	
	public int getOffline_alert() {
		return offline_alert;
	}
	
	public void setOfflinetimes(int offlinetimes) {
		this.offlinetimes = offlinetimes;
	}
	
	public int getOfflinetimes() {
		return offlinetimes;
	}	
	
}
