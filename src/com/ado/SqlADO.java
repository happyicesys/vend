package com.ado;

//SELECT     TOP (500) id, liushuiid, cardinfo, price, mobilephone, receivetime, tradetype, goodmachineid, goodroadid, goodsname, sendstatus, changestatus, 
//orderid
//FROM         traderecordinfo
//WHERE     (id NOT IN
//    (SELECT     TOP (0) id
//      FROM          traderecordinfo AS traderecordinfo_1
//      WHERE      (sendstatus = 1) AND (CAST(receivetime AS datetime) BETWEEN '2012-04-15' AND '2013-04-16') AND (price > 1)
//      ORDER BY id)) AND (sendstatus = 1) AND (CAST(receivetime AS datetime) BETWEEN '2012-04-15' AND '2013-04-16') AND (price > 1)
//ORDER BY id


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;


import weixin.popular.bean.WxTradeLog;
import weixin.popular.util.WxCoporTransfor;
import beans.ClsGprsCfg;
import beans.ClsLiuyan;
import beans.ClsSaleStatisticData;
import beans.PortBean;
import beans.RefundBean;

import beans.VenderBean;
import beans.TradeBean;
import beans.clsGetGoodsCode;
import beans.clsGroupBean;
import beans.clsSellerPara;
import beans.clsTableCmd;

import com.ClsTime;

import beans.Cls_AliQrCodeLog;
import beans.Cls_AliTradeLog;
import com.connectionpool.DBConnectionManager;
import com.tools.ToolBox;

public class SqlADO {
	
	public static final int EVENT_UPDATA_SLOT=0;
	public static final int EVENT_UPDATA_VNEDER=1;
	
	
	private static DBConnectionManager ConnManager=DBConnectionManager.getInstance();
	private final static String CN="BlueShine";
	public final static String CMPPDB="CMPPDB";	
	
	
	//private static c3p0Connect ConnManager=c3p0Connect.getInstance();
//	private final static String CN="BlueShine";
//	public final static String CMPPDB="CMPPDB";	

	


	public static ArrayList<VenderBean> getVenderListByIdLimint(String limiteid) {
		ResultSet rs=null;
		PreparedStatement ps=null;
		ArrayList<VenderBean> li=new ArrayList<VenderBean>(5);
		Connection conn=ConnManager.getConnection(CN);
		String sql="select t.id,t.BTime,t.TerminalName,t.TerminalAddress,t.UpdateTime,"
				+ "t.IsOnline,t.HuodongId,t.SellerTyp,t.GoodsPortCount,"
				+ "t.CanUse,t.AdminId,t.jindu,t.weidu,t.groupid,t.coinAttube,t.bills,t.MdbDeviceStatus,t.gprs_Sign,"
				+ "t.temperature,t.flags1,t.flags2,t.function_flg,t.coinAtbox,t.gprs_event_flg,t.IRErrCnt,t.LstSltE,t.auto_refund,"
				+ "t.manual_refund,t.AllowUpdateGoodsByPc,t.id_format,t.autoTransfer,t.autoTransferRation,t.TemperUpdateTime,v.name from TerminalInfo t "
				+ "left join vendcategories v on v.id = t.vendcategory_id " 
				+ "where t.id in("+limiteid +") order by IsOnline desc,id asc";
		try {
			
			ps= conn.prepareStatement(sql);
			rs= ps.executeQuery();
			while(rs.next())
			{
				VenderBean temv=new VenderBean();
				temv.setAdminId(rs.getInt("AdminId"));
				temv.setBTime(rs.getTimestamp("BTime"));
				temv.setCanUse(rs.getBoolean("CanUse"));
				temv.setGoodsPortCount(rs.getInt("goodsPortCount"));
				temv.setHuodongId(rs.getInt("HuodongId"));
				temv.setId(rs.getInt("id"));
				temv.setIsOnline(rs.getBoolean("isOnline"));
				temv.setSellerTyp(rs.getString("sellerTyp"));
				temv.setTerminalAddress(rs.getString("terminalAddress"));
				temv.setTerminalName(rs.getString("terminalName"));
				temv.setUpdateTime(rs.getTimestamp("updateTime"));
				
				temv.setJindu(rs.getDouble("jindu"));
				temv.setWeidu(rs.getDouble("weidu"));
				temv.setGroupid(rs.getInt("groupid"));
				
				temv.setCoinAttube(rs.getInt("coinAttube"));
				temv.setBills(rs.getInt("bills"));				
				temv.setMdbDeviceStatus(rs.getInt("MdbDeviceStatus"));
				temv.setGprs_Sign(rs.getInt("gprs_Sign"));
				
				temv.setTemperature(rs.getInt("temperature"));
				temv.setFlags1(rs.getInt("flags1"));
				temv.setFlags2(rs.getInt("flags2"));
				temv.setFunction_flg(rs.getInt("function_flg"));
				temv.setCoinAtbox(rs.getInt("coinAtbox"));
				temv.setGprs_event_flg(rs.getShort("gprs_event_flg"));
				
				temv.setIRErrCnt(rs.getInt("IRErrCnt"));
				temv.setLstSltE(rs.getInt("LstSltE"));
				temv.setAuto_refund(rs.getInt("auto_refund"));				
				temv.setManual_refund(rs.getInt("manual_refund"));	
				temv.setM_AllowUpdateGoodsByPc(rs.getInt("AllowUpdateGoodsByPc"));	
				temv.setId_Format(rs.getString("id_format"));	
				temv.setAutoTransfer(rs.getInt("autoTransfer"));
				
				temv.setAutoTransferRation(rs.getDouble("autoTransferRation"));
				temv.setTemperUpdateTime(rs.getString("TemperUpdateTime"));
				temv.setVendcategoryName(rs.getString("name"));				
				li.add(temv);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		if(li.size()>0)
		{
			return li;
		}
		else
		{
			return null;
		}
	}	
	
	public static ArrayList<VenderBean> getVenderListByIdLimint(String limiteid,String id) {
		ResultSet rs=null;
		PreparedStatement ps=null;
		ArrayList<VenderBean> li=new ArrayList<VenderBean>(5);
		Connection conn=ConnManager.getConnection(CN);
		String sql="select t.id,t.BTime,t.TerminalName,t.TerminalAddress,t.UpdateTime," +
				"t.IsOnline,t.HuodongId,t.SellerTyp,t.GoodsPortCount," +
				"t.CanUse,t.AdminId,t.jindu,t.weidu,t.groupid,t.coinAttube,t.bills,t.MdbDeviceStatus,t.gprs_Sign," +
				"t.temperature,t.flags1,t.flags2,t.function_flg,t.coinAtbox,t.gprs_event_flg,t.IRErrCnt,t.LstSltE,t.auto_refund," + 
				"t.manual_refund,t.AllowUpdateGoodsByPc,t.id_format,t.autoTransfer,t.autoTransferRation,t.TemperUpdateTime,vc.name from TerminalInfo t" +
				"left join vendcategories vc on vc.id = t.vendcategory_id " +
				"where t.id=? and t.id in("+limiteid +") order by t.IsOnline desc,t.id asc";
		try {
			
			ps= conn.prepareStatement(sql);
			ps.setString(1, id);
			rs= ps.executeQuery();
			while(rs.next())
			{
				VenderBean temv=new VenderBean();
				temv.setAdminId(rs.getInt("AdminId"));
				temv.setBTime(rs.getTimestamp("BTime"));
				temv.setCanUse(rs.getBoolean("CanUse"));
				temv.setGoodsPortCount(rs.getInt("goodsPortCount"));
				temv.setHuodongId(rs.getInt("HuodongId"));
				temv.setId(rs.getInt("id"));
				temv.setIsOnline(rs.getBoolean("isOnline"));
				temv.setSellerTyp(rs.getString("sellerTyp"));
				temv.setTerminalAddress(rs.getString("terminalAddress"));
				temv.setTerminalName(rs.getString("terminalName"));
				temv.setUpdateTime(rs.getTimestamp("updateTime"));
				
				temv.setJindu(rs.getDouble("jindu"));
				temv.setWeidu(rs.getDouble("weidu"));
				temv.setGroupid(rs.getInt("groupid"));
				
				temv.setCoinAttube(rs.getInt("coinAttube"));
				temv.setBills(rs.getInt("bills"));				
				temv.setMdbDeviceStatus(rs.getInt("MdbDeviceStatus"));
				temv.setGprs_Sign(rs.getInt("gprs_Sign"));
				
				temv.setTemperature(rs.getInt("temperature"));
				temv.setFlags1(rs.getInt("flags1"));
				temv.setFlags2(rs.getInt("flags2"));
				temv.setFunction_flg(rs.getInt("function_flg"));
				temv.setCoinAtbox(rs.getInt("coinAtbox"));
				temv.setGprs_event_flg(rs.getShort("gprs_event_flg"));
				
				temv.setIRErrCnt(rs.getInt("IRErrCnt"));
				temv.setLstSltE(rs.getInt("LstSltE"));
				temv.setAuto_refund(rs.getInt("auto_refund"));	
				temv.setManual_refund(rs.getInt("manual_refund"));
				temv.setM_AllowUpdateGoodsByPc(rs.getInt("AllowUpdateGoodsByPc"));
				temv.setId_Format(rs.getString("id_format"));
				temv.setAutoTransfer(rs.getInt("autoTransfer"));
				
				temv.setAutoTransferRation(rs.getDouble("autoTransferRation"));
				temv.setTemperUpdateTime(rs.getString("TemperUpdateTime"));
				temv.setVendcategoryName(rs.getString("name"));
				li.add(temv);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		if(li.size()>0)
		{
			return li;
		}
		else
		{
			return null;
		}
	}	
	
	public static ArrayList<VenderBean> getVenderBeanList() {
		return getVenderBeanList(0);
	}
	
	public static ArrayList<VenderBean> getVenderBeanList(int AdminId) {
		ResultSet rs=null;
		PreparedStatement ps=null;
		ArrayList<VenderBean> li=new ArrayList<VenderBean>(5);
		Connection conn=ConnManager.getConnection(CN);
		String sql="select t.id,t.AdminId,t.BTime,t.TerminalName,t.TerminalAddress,t.UpdateTime," +
				"t.IsOnline,t.HuodongId,t.SellerTyp,t.GoodsPortCount,t.CanUse,t.auto_refund,t.manual_refund,"
				+ "t.AllowUpdateGoodsByPc,t.groupid,t.autoTransfer,t.autoTransferRation,t.is_offline_alert_sent,t.offline_alert,t.offlinetimes,vc.name "
				+ "from TerminalInfo t"
				+ "left join vendcategories vc on vc.id = t.vendcategory_id ";
		if(AdminId!=0)
		{
			sql+=" where AdminId="+AdminId;
		}
		sql+=" order by id asc";
		try {
			ps=conn.prepareStatement(sql);
			rs=ps.executeQuery();
			while(rs.next())
			{
				VenderBean temv=new VenderBean();
				temv.setAdminId(rs.getInt("AdminId"));
				temv.setBTime(rs.getTimestamp("BTime"));
				temv.setCanUse(rs.getBoolean("CanUse"));
				temv.setGoodsPortCount(rs.getInt("goodsPortCount"));
				temv.setHuodongId(rs.getInt("HuodongId"));
				temv.setId(rs.getInt("id"));
				temv.setIsOnline(rs.getBoolean("isOnline"));
				temv.setSellerTyp(rs.getString("sellerTyp"));
				temv.setTerminalAddress(rs.getString("terminalAddress"));
				temv.setTerminalName(rs.getString("terminalName"));
				temv.setUpdateTime(rs.getTimestamp("updateTime"));
				
				temv.setAuto_refund(rs.getInt("auto_refund"));	
				temv.setManual_refund(rs.getInt("manual_refund"));
				temv.setM_AllowUpdateGoodsByPc(rs.getInt("AllowUpdateGoodsByPc"));
				temv.setGroupid(rs.getInt("groupid"));
				temv.setAutoTransfer(rs.getInt("autoTransfer"));
				temv.setAutoTransferRation(rs.getDouble("autoTransferRation"));
				temv.setIs_offline_alert_sent(rs.getInt("is_offline_alert_sent"));
				temv.setOffline_alert(rs.getInt("offline_alert"));
				temv.setOfflinetimes(rs.getInt("offlinetimes"));
				temv.setVendcategoryName(rs.getString("name"));
				li.add(temv);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return li;
	}
	public static VenderBean getVenderBeanByid(int id) {
		ResultSet rs=null;
		PreparedStatement ps=null;
		VenderBean temv=null;
		
		int i=1;
		Connection conn=ConnManager.getConnection(CN);
		String sql="select top 1 t.AdminId, t.BTime, t.TerminalName, t.TerminalAddress, t.UpdateTime," +
				" t.IsOnline, t.HuodongId, t.SellerTyp, t.TelNum, t.GoodsPortCount, t.TipMesOnLcd," +
				" t.CanUse, t.queueMaxLength, t.jindu, t.weidu, t.groupid, t.coinAttube, t.MdbDeviceStatus, t.gprs_Sign," +
				" t.temperature, t.flags1, t.flags2, t.function_flg, t.coinAtbox, t.gprs_event_flg, t.vmc_firmfile, t.IRErrCnt, t.LstSltE, t.pos_PWD,"+ 
				" t.code_ver, t.id_format, t.auto_refund, t.manual_refund, t.AllowUpdateGoodsByPc, t.autoTransfer, t.autoTransferRation, t.TemperUpdateTime, t.temp_alert, t.offline_alert, vc.name, t.long_temp_loop, t.long_temp_alert_sent, t.long_temp_loop_starttime, "+ 
				" t.refill_temp_loop, t.refill_temp_alert_sent, t.refill_temp_loop_starttime " +
				" from TerminalInfo t " +
				" left join vendcategories vc on vc.id=t.vendcategory_id " +
				" where t.id=?";
		
		try {
			ps=conn.prepareStatement(sql);
			ps.setInt(i++, id);
			rs=ps.executeQuery();
			if(rs.next())
			{
				temv=new VenderBean();
				temv.setAdminId(rs.getInt("AdminId"));
				temv.setBTime(rs.getTimestamp("BTime"));
				temv.setCanUse(rs.getBoolean("CanUse"));
				temv.setGoodsPortCount(rs.getInt("goodsPortCount"));
				temv.setHuodongId(rs.getInt("HuodongId"));
				temv.setId(id);
				temv.setIsOnline(rs.getBoolean("isOnline"));
				temv.setQueueMaxLength(rs.getInt("queueMaxLength"));
				temv.setSellerTyp(rs.getString("sellerTyp"));
				temv.setTelNum(rs.getString("TelNum"));
				temv.setTerminalAddress(rs.getString("terminalAddress"));
				temv.setTerminalName(rs.getString("terminalName"));
				temv.setTipMesOnLcd(rs.getString("tipMesOnLcd"));
				temv.setUpdateTime(rs.getTimestamp("updateTime"));
				temv.setJindu(rs.getDouble("jindu"));
				temv.setWeidu(rs.getDouble("weidu"));
				temv.setGroupid(rs.getInt("groupid"));

				temv.setCoinAttube(rs.getInt("coinAttube"));
				temv.setMdbDeviceStatus(rs.getInt("MdbDeviceStatus"));
				temv.setGprs_Sign(rs.getInt("gprs_Sign"));	
				temv.setTemperature(rs.getInt("temperature"));
				temv.setFlags1(rs.getInt("flags1"));
				temv.setFlags2(rs.getInt("flags2"));
				temv.setFunction_flg(rs.getInt("function_flg"));
				temv.setCoinAtbox(rs.getInt("coinAtbox"));
				temv.setGprs_event_flg(rs.getShort("gprs_event_flg"));
				
				temv.setVmc_firmfile(rs.getString("vmc_firmfile"));
				
				temv.setIRErrCnt(rs.getInt("IRErrCnt"));
				temv.setLstSltE(rs.getInt("LstSltE"));
				
				temv.setPos_PWD(rs.getString("pos_PWD"));
				
				temv.setId_Format(rs.getString("id_format"));
				
				temv.setCode_ver(rs.getInt("code_ver"));
				temv.setAuto_refund(rs.getInt("auto_refund"));
				temv.setManual_refund(rs.getInt("manual_refund"));	
				temv.setM_AllowUpdateGoodsByPc(rs.getInt("AllowUpdateGoodsByPc"));
				temv.setAutoTransfer(rs.getInt("autoTransfer"));
				
				temv.setAutoTransferRation(rs.getDouble("autoTransferRation"));
				temv.setTemperUpdateTime(rs.getString("TemperUpdateTime"));
				temv.setTemp_alert(rs.getInt("temp_alert"));
				temv.setOffline_alert(rs.getInt("offline_alert"));
				temv.setVendcategoryName(rs.getString("name"));
				temv.setLongTempAlertLoop(rs.getInt("long_temp_loop"));
				temv.setLongTempAlertSent(rs.getInt("long_temp_alert_sent"));
				temv.setLongTempLoopStarttime(rs.getString("long_temp_loop_starttime"));
				temv.setRefillTempAlertLoop(rs.getInt("refill_temp_loop"));
				temv.setRefillTempAlertSent(rs.getInt("refill_temp_alert_sent"));
				temv.setRefillTempLoopStarttime(rs.getString("refill_temp_loop_starttime"));				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return temv;
	}
	public static boolean exec(String sql) {
		ResultSet rs=null;
		Statement s=null;
		Connection conn=ConnManager.getConnection(CN);
		try {
			s=conn.createStatement();
			s.execute(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,s);
		}
	}	
	public static void updateSeller(VenderBean vb) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="update [TerminalInfo] set sellertyp=?,goodsPortCount=?,HuodongId=?," +
				"terminalAddress=?,terminalName=?,tipMesOnLcd=?,updateTime=getdate(),CanUse=?," +
				"jindu=?,weidu=?,[TelNum]=?,[groupid]=?,vmc_firmfile=?,[AdminId]=?,[auto_refund]=?,"
				+ "[manual_refund]=?,[AllowUpdateGoodsByPc]=?,[autoTransfer]=?,autoTransferRation=?,[temp_alert]=? where id=?";
		
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setString(i++, vb.getSellerTyp());//
			ps.setInt(i++, vb.getGoodsPortCount());//
			ps.setInt(i++, vb.getHuodongId());//
			ps.setString(i++, vb.getTerminalAddress());//
			ps.setString(i++, vb.getTerminalName());//
			ps.setString(i++, vb.getTipMesOnLcd());//
			ps.setBoolean(i++, vb.isCanUse());//
			ps.setDouble(i++, vb.getJindu());//
			ps.setDouble(i++, vb.getWeidu());//
			
			ps.setString(i++, vb.getTelNum());//
			ps.setInt(i++, vb.getGroupid());//
			ps.setString(i++, vb.getVmc_firmfile());//	
			ps.setInt(i++, vb.getAdminId());//
			ps.setInt(i++, vb.getAuto_refund());//
			ps.setInt(i++, vb.getManual_refund());//
			ps.setInt(i++, vb.getM_AllowUpdateGoodsByPc());
			ps.setInt(i++, vb.getAutoTransfer());
			ps.setDouble(i++, vb.getAutoTransferRation());//
			ps.setInt(i++, vb.getTemp_alert());
			ps.setInt(i++, vb.getId());
			
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}

	}
	
	//INSERT [TerminalInfo] ([Id],[AdminId],[BTime],[TerminalName],[TerminalAddress],[UpdateTime],[IsOnline],[HuodongId],[SellerTyp],[GoodsPortCount],[TipMesOnLcd],[CanUse],[queueMaxLength],[isActive],[jindu],[weidu],[pos_TERM_NO],[pos_INST_NO],[pos_MERCH_NO],[pos_CLIENTAUTHCOD],[pos_USERNAME],[pos_PWD],[TelNum],[groupid]) VALUES ( 1,0,N'2012-9-27 15:24:30',N'智能微超-1',N'贵州省贵阳市云岩区中华北路99号美佳大厦南楼',N'2013-5-8 9:49:51',1,0,N'WX_WINE_8',8,N'/#/欢迎使用手机支付自动售货机。/#/',1,1,1,106.71785,26.593861,N'60688014',N'00000000',N'011100592100015',N'3131313131313131',N'lqbj',N'1234',N'13910349593',0)

	public static boolean AddSeller(VenderBean vb) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql= "INSERT [TerminalInfo] ([Id],[AdminId],[BTime],[TerminalName]," +
				"[TerminalAddress],[UpdateTime],[IsOnline],[HuodongId],[SellerTyp]," +
				"[GoodsPortCount],[TipMesOnLcd],[CanUse],[queueMaxLength],[isActive]," +
				"[jindu],[weidu],[pos_TERM_NO],[pos_INST_NO],[pos_MERCH_NO]," +
				"[pos_CLIENTAUTHCOD],[pos_USERNAME],[pos_PWD],[TelNum],[groupid],[auto_refund],"
				+ "[manual_refund],[id_format],[AllowUpdateGoodsByPc],[autoTransfer],[autoTransferRation]) " +
				"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++, vb.getId());
			ps.setInt(i++, vb.getAdminId());
			ps.setDate(i++,new Date(ClsTime.SystemTime()));
			ps.setString(i++, vb.getTerminalName());
			ps.setString(i++, vb.getTerminalAddress());
			ps.setDate(i++,new Date(ClsTime.SystemTime()));
			ps.setBoolean(i++, vb.isIsOnline());
			ps.setInt(i++, vb.getHuodongId());
			ps.setString(i++, vb.getSellerTyp());
			ps.setInt(i++, vb.getGoodsPortCount());
			ps.setString(i++, vb.getTipMesOnLcd());
			ps.setBoolean(i++, vb.isCanUse());
			ps.setInt(i++, vb.getQueueMaxLength());
			ps.setBoolean(i++, vb.isActive());
			
			ps.setDouble(i++, vb.getJindu());
			ps.setDouble(i++, vb.getWeidu());
			
			ps.setString(i++, vb.getPos_TERM_NO());
			ps.setString(i++, vb.getPos_INST_NO());
			ps.setString(i++, vb.getPos_MERCH_NO());
			ps.setString(i++, vb.getPos_CLIENTAUTHCOD());
			ps.setString(i++, vb.getPos_USERNAME());
			ps.setString(i++, vb.getPos_PWD());
			
			ps.setString(i++, vb.getTelNum());
			ps.setInt(i++, vb.getGroupid());
			ps.setInt(i++, vb.getAuto_refund());
			ps.setInt(i++, vb.getManual_refund());
			ps.setString(i++, vb.getId_Format());
			ps.setInt(i++, vb.getM_AllowUpdateGoodsByPc());
			ps.setInt(i++, vb.getAutoTransfer());
			ps.setDouble(i++, vb.getAutoTransferRation());
			ps.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}
	

	public static int getAdminIdofSeller(int sellerid) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		int tem=-1;
		Connection conn=ConnManager.getConnection(CN);
		String sql="select top 1 AdminId from terminalinfo where id="+sellerid;
		try {
			ps=conn.prepareStatement(sql);rs=ps.executeQuery();
			if(rs.next())
			{
				tem=rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return tem;
	}
	
	public static PortBean getPortBean(int mid,int sid)
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		PortBean temp=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql="SELECT top 1 machineid,innerid,Inneridname,id,errorinfo,iserror," +
					" updatetime, (select goodsname from goodsinfo where goodsinfo.id=goodroadinfo.goodsid) as goodroadname, price, amount, lastErrorTime, " +
					"huodongid,capacity,goodsid,deviceid,discount,PauseFlg,qrcode,qrcode2,wx_qrcode,wx_qrcode2," +
					"al_tradeid,al_tradeid2,wx_tradeid,wx_tradeid2,updateqrtime,updateqrtime2" +
					" FROM goodroadinfo ";

		sql+="WHERE machineid ="+mid +" and innerid="+sid;	

		sql+=" order by machineid,innerid asc";
		try {
			ps=conn.prepareStatement(sql);
			rs=ps.executeQuery();
			if(rs.next())
			{
				temp=new PortBean();
				temp.setMachineid(rs.getInt("machineid"));
				temp.setInnerid(rs.getInt("innerid"));
				temp.setInneridname(rs.getString("Inneridname"));
				temp.setId(rs.getInt("id"));
				temp.setErrorinfo(rs.getString("errorinfo"));
				temp.setError_id(rs.getInt("iserror"));
				temp.setUpdatetime(rs.getTimestamp("updatetime"));
				temp.setGoodroadname(rs.getString("goodroadname"));
				temp.setPrice(rs.getInt("price"));
				temp.setAmount(rs.getInt("amount"));
				temp.setLastErrorTime(rs.getTimestamp("lastErrorTime"));
				temp.setHuodongid(rs.getInt("huodongid"));
				temp.setCapacity(rs.getInt("capacity"));
				temp.setGoodsid(rs.getInt("goodsid"));
				temp.setDeviceid(rs.getInt("deviceid"));
				temp.setDiscount(rs.getInt("discount"));
				temp.setPauseFlg(rs.getInt("PauseFlg"));
				
				temp.setQrcode(rs.getString("qrcode"));
				temp.setQrcode2(rs.getString("qrcode2"));
				temp.setAl_trade(rs.getString("al_tradeid"));
				temp.setAl_trade2(rs.getString("al_tradeid2"));
				
				temp.setWx_qrcode(rs.getString("wx_qrcode"));
				temp.setWx_qrcode2(rs.getString("wx_qrcode2"));
				temp.setWx_trade(rs.getString("wx_tradeid"));
				temp.setWx_trade2(rs.getString("wx_tradeid2"));
				temp.setUpdateqrtime(rs.getTimestamp("updateqrtime"));
				temp.setUpdateqrtime2(rs.getTimestamp("updateqrtime2"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return temp;
	}
	
	public static PortBean getPortBean(int id)
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		PortBean temp=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql="SELECT top 1 machineid,innerid,Inneridname,id,errorinfo,iserror," +
					" updatetime, goodroadname, price, amount, lastErrorTime, huodongid,capacity,goodsid,deviceid,PauseFlg" +
					" FROM goodroadinfo WHERE id=? " +
					"order by machineid,innerid asc";
		try {
			
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++, id);
			rs=ps.executeQuery();
			if(rs.next())
			{
				temp=new PortBean();
				temp.setMachineid(rs.getInt("machineid"));
				temp.setInnerid(rs.getInt("innerid"));
				temp.setInneridname(rs.getString("Inneridname"));
				temp.setId(rs.getInt("id"));
				temp.setErrorinfo(rs.getString("errorinfo"));
				temp.setError_id(rs.getInt("iserror"));
				temp.setUpdatetime(rs.getTimestamp("updatetime"));
				temp.setGoodroadname(rs.getString("goodroadname"));
				temp.setPrice(rs.getInt("price"));
				temp.setAmount(rs.getInt("amount"));
				temp.setLastErrorTime(rs.getTimestamp("lastErrorTime"));
				temp.setHuodongid(rs.getInt("huodongid"));
				temp.setCapacity(rs.getInt("capacity"));
				temp.setGoodsid(rs.getInt("goodsid"));
				temp.setDeviceid(rs.getInt("deviceid"));
				temp.setPauseFlg(rs.getInt("PauseFlg"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return temp;
	}

	public static ArrayList<PortBean> getPortBeanList(int SellerId) {
		return getPortBeanList(SellerId,true);
	}

	public static ArrayList<PortBean> getPortBeanList(int SellerId,boolean showempty) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		ArrayList<PortBean> li=new ArrayList<PortBean>(5);
		Connection conn=ConnManager.getConnection(CN);
		String sql="";
		if(showempty)
		{
			sql="SELECT machineid,innerid,Inneridname,id,errorinfo,iserror," +
					" updatetime, (select goodsname from goodsinfo where goodsinfo.id=goodroadinfo.goodsid) as goodroadname, " +
					"price, amount, lastErrorTime, huodongid,capacity,goodsid,discount,PauseFlg,qrcode,qrcode2,wx_qrcode,wx_qrcode2," +
					"al_tradeid,al_tradeid2,wx_tradeid,wx_tradeid2  FROM goodroadinfo WHERE machineid =? order by machineid,innerid asc";
		}
		else
		{
			sql="SELECT machineid,innerid,Inneridname,id,errorinfo,iserror," +
					" updatetime, (select goodsname from goodsinfo where goodsinfo.id=goodroadinfo.goodsid) as goodroadname, " +
					"price, amount, lastErrorTime, huodongid,capacity,goodsid,discount,PauseFlg,qrcode,qrcode2,wx_qrcode,wx_qrcode2," +
					"al_tradeid,al_tradeid2,wx_tradeid,wx_tradeid2  FROM goodroadinfo WHERE machineid =? and capacity>0 order by machineid,innerid asc";

		}
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++, SellerId);
			rs=ps.executeQuery();
			while(rs.next())
			{
				PortBean temp=new PortBean();
				temp.setMachineid(rs.getInt("machineid"));
				temp.setInnerid(rs.getInt("innerid"));
				temp.setInneridname(rs.getString("Inneridname"));
				temp.setId(rs.getInt("id"));
				temp.setErrorinfo(rs.getString("errorinfo"));
				temp.setError_id(rs.getInt("iserror"));
				temp.setUpdatetime(rs.getTimestamp("updatetime"));
				temp.setGoodroadname(rs.getString("goodroadname"));
				temp.setPrice(rs.getInt("price"));
				temp.setAmount(rs.getInt("amount"));
				temp.setLastErrorTime(rs.getTimestamp("lastErrorTime"));
				temp.setHuodongid(rs.getInt("huodongid"));
				temp.setCapacity(rs.getInt("capacity"));
				temp.setGoodsid(rs.getInt("goodsid"));
				temp.setDiscount(rs.getInt("discount"));
				temp.setPauseFlg(rs.getInt("PauseFlg"));
				temp.setQrcode(rs.getString("qrcode"));
				temp.setQrcode2(rs.getString("qrcode2"));
				temp.setAl_trade(rs.getString("al_tradeid"));
				temp.setAl_trade2(rs.getString("al_tradeid2"));
				
				temp.setWx_qrcode(rs.getString("wx_qrcode"));
				temp.setWx_qrcode2(rs.getString("wx_qrcode2"));
				temp.setWx_trade(rs.getString("wx_tradeid"));
				temp.setWx_trade2(rs.getString("wx_tradeid2"));
				li.add(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return li;
	}
	
	public static int getPortBeanCount(int SellerId,int deviceid)
	{
		int count=0;
		PreparedStatement ps=null;
		ResultSet rs=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql="SELECT count(id) as cnt  FROM goodroadinfo where machineid ="+SellerId;

		if(deviceid>0)
		{
			sql+=" and deviceid ="+deviceid;
		}

		try {
			ps=conn.prepareStatement(sql);
			rs=ps.executeQuery();
			if(rs.next())
			{
				count=rs.getInt("cnt");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		
		return count;
	}
	
	
	public static ArrayList<PortBean> getPortBeanList(int SellerId,int pageindex,int countperpage,int deviceid) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		ArrayList<PortBean> li=new ArrayList<PortBean>(5);
		Connection conn=ConnManager.getConnection(CN);
		String sql="SELECT top("+ countperpage +") machineid,innerid,Inneridname,id,errorinfo,iserror," +
					" updatetime, (select goodsname from goodsinfo where goodsinfo.id=goodroadinfo.goodsid) as goodroadname, " +
					"price, amount, lastErrorTime, huodongid,capacity,goodsid,deviceid,PauseFlg," +
					"(select picname from goodsinfo where goodsinfo.id=goodroadinfo.goodsid) as product_img" +
					" FROM goodroadinfo ";
		if(SellerId==0)
		{
			return li;
		}
		else
		{
			sql+="WHERE id not in(select top("+((pageindex-1)*countperpage)+") id from goodroadinfo where deviceid="+deviceid+" and machineid="+SellerId+" order by innerid asc) and machineid ="+SellerId;
		}
		
		if(deviceid>0)
		{
			sql+=" and deviceid ="+deviceid;
		}

		sql+=" order by innerid asc";
		
		//System.out.println(sql);
		try {
			ps=conn.prepareStatement(sql);
			rs=ps.executeQuery();
			while(rs.next())
			{
				PortBean temp=new PortBean();
				temp.setMachineid(rs.getInt("machineid"));
				temp.setInnerid(rs.getInt("innerid"));
				temp.setInneridname(rs.getString("Inneridname"));
				temp.setId(rs.getInt("id"));
				temp.setErrorinfo(rs.getString("errorinfo"));
				temp.setError_id(rs.getInt("iserror"));
				temp.setUpdatetime(rs.getTimestamp("updatetime"));
				temp.setGoodroadname(rs.getString("goodroadname"));
				temp.setPrice(rs.getInt("price"));
				temp.setAmount(rs.getInt("amount"));
				temp.setLastErrorTime(rs.getTimestamp("lastErrorTime"));
				temp.setHuodongid(rs.getInt("huodongid"));
				temp.setCapacity(rs.getInt("capacity"));
				temp.setDeviceid(rs.getInt("deviceid"));
				temp.setGoodsid(rs.getInt("goodsid"));
				temp.setProduct_img(rs.getString("product_img"));
				temp.setPauseFlg(rs.getInt("PauseFlg"));
				li.add(temp);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return li;
	}
	
	
	public static int getTradeRowsCount(String str)
	{
		ResultSet rs=null;
		Statement st=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql=null;
		int c=0;
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT count(traderecordinfo.id) as c FROM traderecordinfo where ");
		stringBuilder.append(str);
		sql=stringBuilder.toString();

		
		try {
			st=conn.createStatement();
			rs=st.executeQuery(sql);
			if(rs.next())
			{
				c=rs.getInt("c");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,st);
		}
		return c;
	}
	
	public static ArrayList<TradeBean> getTradeList(String str,int page,int max)
	{
		ArrayList<TradeBean> li=new ArrayList<TradeBean>(max);
		ResultSet rs=null;
		Statement st=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql=null;
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT top ");
		stringBuilder.append(max);
		stringBuilder.append(" traderecordinfo.id,liushuiid,Cardinfo,");
		stringBuilder.append("price,mobilephone, receivetime,");
		stringBuilder.append("tradetype,goodmachineid,goodroadid,goodsname,");
		stringBuilder.append("sendstatus,Changestatus,orderid,changes,coin_credit,bill_credit,");
		stringBuilder.append("inneridname,xmlstr,tradeid,has_jiesuan,has_feeback FROM traderecordinfo where ");
		stringBuilder.append("traderecordinfo.id not in(SELECT top ");
		stringBuilder.append((max*(page-1)));
		stringBuilder.append(" traderecordinfo.id FROM ");
		stringBuilder.append("traderecordinfo where ");
		stringBuilder.append(str);
		stringBuilder.append(" order by traderecordinfo.id asc) and ");
		stringBuilder.append(str);
		stringBuilder.append(" order by traderecordinfo.id asc");
		sql=stringBuilder.toString();
		System.out.println(sql);
		try {
			st=conn.createStatement();
			rs=st.executeQuery(sql);
			while(rs.next())
			{
				TradeBean tb=new TradeBean();
				tb.setId(rs.getInt(1));
				
				tb.setLiushuiid(rs.getString(2));
				tb.setCardinfo(rs.getString(3));
				tb.setPrice(rs.getInt(4));
				tb.setMobilephone(rs.getString(5));
				tb.setReceivetime(rs.getTimestamp(6));
				tb.setTradetype(rs.getInt(7));
				tb.setGoodmachineid(rs.getInt(8));
				tb.setGoodroadid(rs.getInt(9));
				tb.setGoodsName(rs.getString(10));				
				tb.setSendstatus(rs.getInt(11));
				tb.setChangestatus(rs.getInt(12));
				tb.setOrderid(rs.getString(13));
				tb.setChanges(rs.getInt(14));
				tb.setCoin_credit(rs.getInt(15));				
				tb.setBill_credit(rs.getInt(16));
				tb.setInneridname(rs.getString("inneridname"));
				tb.setXmlstr(rs.getString("xmlstr"));
				tb.setTradeid(rs.getString("tradeid"));
				tb.setHas_jiesuan(rs.getInt("has_jiesuan"));
				tb.setHas_feeback(rs.getInt("has_feeback"));
				li.add(tb);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,st);
		}
		return li;
	}
	
	public static ArrayList<TradeBean> getTradeListFromTemp(String where)
	{
		ArrayList<TradeBean> li=new ArrayList<TradeBean>();
		ResultSet rs=null;
		Statement st=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql="SELECT id,liushuiid,cardinfo,orderid,price,mobilephone,tradetype,goodmachineid,"
				+ "goodroadid,changestatus,sendstatus,status,receivetime,xmlstr,goodsname,"
				+ "changes,coin_credit,bill_credit,inneridname,tradeid,has_jiesuan,has_feeback FROM traderecordinfo_tem "
				+ where;
		try {
			st=conn.createStatement();
			rs=st.executeQuery(sql);
			while(rs.next())
			{
				TradeBean tb=new TradeBean();
				tb.setId(rs.getInt("id"));
				tb.setLiushuiid(rs.getString("liushuiid"));
				tb.setCardinfo(rs.getString("cardinfo"));
				tb.setOrderid(rs.getString("orderid"));
				tb.setPrice(rs.getInt("price"));
				tb.setMobilephone(rs.getString("mobilephone"));
				tb.setTradetype(rs.getInt("tradetype"));
				tb.setGoodmachineid(rs.getInt("goodmachineid"));
				tb.setGoodroadid(rs.getInt("goodroadid"));
				tb.setChangestatus(rs.getInt("changestatus"));
				tb.setSendstatus(rs.getInt("sendstatus"));
				tb.setStatus(rs.getInt("status"));
				tb.setReceivetime(rs.getTimestamp("receivetime"));
				tb.setXmlstr(rs.getString("xmlstr"));
				tb.setGoodsName(rs.getString("goodsname"));
				tb.setChanges(rs.getInt("changes"));
				tb.setCoin_credit(rs.getInt("coin_credit"));
				tb.setBill_credit(rs.getInt("bill_credit"));
				tb.setInneridname(rs.getString("inneridname"));
				tb.setTradeid(rs.getString("tradeid"));
				tb.setHas_feeback(rs.getInt("has_feeback"));
				tb.setHas_jiesuan(rs.getInt("has_jiesuan"));
				li.add(tb);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,st);
		}
		return li;
	}
	
	
	
	
	
	
	
	
	public static boolean updatePort(int id, String fieldname, String value) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="update [goodroadinfo] set updatetime=getdate(),"+fieldname+"=? where id=?";

		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setString(i++,value);
			ps.setInt(i++, id);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return true;
	}
	
	public static boolean updatePort(int id, String fieldname, int value) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="update [goodroadinfo] set updatetime=getdate(),"+fieldname+"=? where id=?";

		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++,value);
			ps.setInt(i++, id);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return true;
	}

	
	public static String VenderBeanID() {
		PreparedStatement ps=null;
		ResultSet rs=null;
		ArrayList<Integer> li=new ArrayList<Integer>(5);
		StringBuilder st=new StringBuilder();
		Connection conn=ConnManager.getConnection(CN);
		String sql="select id from [TerminalInfo] order by id asc";
		try {
			ps=conn.prepareStatement(sql);rs=ps.executeQuery();
			while(rs.next())
			{
				st.append(rs.getString("id"));
				st.append(",");
			}
			st.append("-1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return st.toString();
	}
	
	
//	public static void updateSelf(UserBean ub) {
//		StringBuilder stringBuilder = new StringBuilder();
//		stringBuilder.append("update admininfo set ");
//		if(!ub.getAdminpassword().equals(""))
//		{
//			stringBuilder.append("adminpassword='");
//			stringBuilder.append(ub.getAdminpassword());
//			stringBuilder.append("',");
//		}
//		stringBuilder.append("admintelephone='");
//		stringBuilder.append(ub.getAdmintelephone());
//		stringBuilder.append("',");
//		
//		stringBuilder.append("Adminrights='");
//		stringBuilder.append(ub.getAdminrights());
//		stringBuilder.append("',");
//		
//		stringBuilder.append("adminmobilephone='");
//		stringBuilder.append(ub.getAdminmobilephone());
//		stringBuilder.append("',");
//		stringBuilder.append("adminname='");
//		stringBuilder.append(ub.getAdminname());
//		stringBuilder.append("',");
//		stringBuilder.append("adminaddress='");
//		stringBuilder.append(ub.getAdminaddress());
//		stringBuilder.append("' where id=");
//		stringBuilder.append(ub.getId());
//		exec(stringBuilder.toString());
//	}
	public static void updatePort(int sellerId, int s, int e, String fieldname,String value) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="update [goodroadinfo] set updatetime=getdate(),"+fieldname+"=? where machineid=? and (innerid between ? and ?)";

		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setString(i++,value);
			ps.setInt(i++, sellerId);
			ps.setInt(i++, s);
			ps.setInt(i++, e);
			ps.executeUpdate();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}
	
	public static void updatePort(int sellerId, int s, int e, String fieldname,int value) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="update [goodroadinfo] set updatetime=getdate(),"+fieldname+"=? where machineid=? and (innerid between ? and ?)";

		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++,value);
			ps.setInt(i++, sellerId);
			ps.setInt(i++, s);
			ps.setInt(i++, e);
			ps.executeUpdate();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}
	
	
	public static String getSmsContent(int id) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String str=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql="select top 1 SmsContent from [traderecordinfo] where id="+id;
		try {
			ps=conn.prepareStatement(sql);rs=ps.executeQuery();
			if(rs.next())
				str=rs.getString(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return str;
	}
	public static TradeBean getTradeBean(int id) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		TradeBean tb=null;
		
		Connection conn=ConnManager.getConnection(CN);
		String sql="SELECT top 1 id,liushuiid,cardinfo,orderid,price,mobilephone,tradetype,goodmachineid,"
				+ "goodroadid,changestatus,sendstatus,status,receivetime,xmlstr,goodsname,"
				+ "changes,coin_credit,bill_credit,inneridname,has_jiesuan,has_feeback FROM traderecordinfo "
				+ "where id=?";
		int i=1;
		try {
			ps=conn.prepareStatement(sql);
			ps.setInt(i++,id);
			rs=ps.executeQuery();
			if(rs.next())
			{
				tb=new TradeBean();
				tb.setId(rs.getInt("id"));
				tb.setLiushuiid(rs.getString("liushuiid"));
				
				tb.setCardinfo(rs.getString("cardinfo"));
				
				tb.setOrderid(rs.getString("orderid"));
				tb.setPrice(rs.getInt("price"));
				tb.setMobilephone(rs.getString("mobilephone"));
				tb.setTradetype(rs.getInt("tradetype"));
				
				tb.setGoodmachineid(rs.getInt("goodmachineid"));
				tb.setGoodroadid(rs.getInt("goodroadid"));
				tb.setChangestatus(rs.getInt("changestatus"));
				tb.setSendstatus(rs.getInt("sendstatus"));
				tb.setStatus(rs.getInt("status"));
				tb.setReceivetime(rs.getTimestamp("receivetime"));
				tb.setXmlstr(rs.getString("xmlstr"));
				tb.setGoodsName(rs.getString("goodsname"));
				tb.setChanges(rs.getInt("changes"));
				
				tb.setCoin_credit(rs.getInt("coin_credit"));
				
				tb.setBill_credit(rs.getInt("bill_credit"));
				tb.setInneridname(rs.getString("inneridname"));
				tb.setHas_feeback(rs.getInt("has_feeback"));
				tb.setHas_jiesuan(rs.getInt("has_jiesuan"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return tb;
	}
	public static TradeBean getTradeBean(String orderid)
	{
		return getTradeBean(orderid,"traderecordinfo");
	}
	public static TradeBean getTradeBeanFromTemp(String orderid)
	{
		return getTradeBean(orderid,"traderecordinfo_tem");
	}
	
	public static TradeBean getTradeBean(String orderid,String tablename) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		TradeBean tb=null;
		
		Connection conn=ConnManager.getConnection(CN);
		String sql="SELECT top 1 id,liushuiid,cardinfo,orderid,price,mobilephone,tradetype,goodmachineid,"
				+ "goodroadid,changestatus,sendstatus,status,receivetime,xmlstr,goodsname,"
				+ "changes,coin_credit,bill_credit,inneridname,tradeid,has_jiesuan,has_feeback FROM "+ tablename+" "
				+ "where orderid=?";
		int i=1;
		try {
			ps=conn.prepareStatement(sql);
			ps.setString(i++,orderid);
			rs=ps.executeQuery();
			if(rs.next())
			{
				tb=new TradeBean();
				tb.setId(rs.getInt("id"));
				tb.setLiushuiid(rs.getString("liushuiid"));
				
				tb.setCardinfo(rs.getString("cardinfo"));
				
				tb.setOrderid(rs.getString("orderid"));
				tb.setPrice(rs.getInt("price"));
				tb.setMobilephone(rs.getString("mobilephone"));
				tb.setTradetype(rs.getInt("tradetype"));
				
				tb.setGoodmachineid(rs.getInt("goodmachineid"));
				tb.setGoodroadid(rs.getInt("goodroadid"));
				tb.setChangestatus(rs.getInt("changestatus"));
				tb.setSendstatus(rs.getInt("sendstatus"));
				tb.setStatus(rs.getInt("status"));
				tb.setReceivetime(rs.getTimestamp("receivetime"));
				tb.setXmlstr(rs.getString("xmlstr"));
				tb.setGoodsName(rs.getString("goodsname"));
				tb.setChanges(rs.getInt("changes"));
				tb.setCoin_credit(rs.getInt("coin_credit"));
				tb.setBill_credit(rs.getInt("bill_credit"));
				tb.setInneridname(rs.getString("inneridname"));
				tb.setTradeid(rs.getString("tradeid"));
				tb.setHas_feeback(rs.getInt("has_feeback"));
				tb.setHas_jiesuan(rs.getInt("has_jiesuan"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return tb;
	}




	

	
	
	

	public static boolean SetPortGoods(int colid, int goodsid,int price) 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="update goodroadinfo set goodsid=?,price=?,updatetime=getdate() where id=?";
		
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++,goodsid);
			ps.setInt(i++,price);
			ps.setInt(i++,colid);
			return (ps.executeUpdate()==colid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return false;
	}
	
	public static boolean SetPortGoods(int colid, int goodsid) 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="update goodroadinfo set goodsid=?,price=(select price from goodsinfo where id=?), updatetime=getdate() where id=?";
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++,goodsid);
			ps.setInt(i++,goodsid);
			ps.setInt(i++,colid);
			return (ps.executeUpdate()==colid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return false;
	}
	
	public static boolean deleteGoods(int goodsid) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="delete from goodsinfo where id=?";
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++,goodsid);
			return (ps.executeUpdate()==goodsid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return false;
	}
	
	public static boolean UpdateGoods(int goodsid,int price,String goodsname, String des1, String des2,String des3, String pic1) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="update goodsinfo set goodsname=?, price=?,picname=?,des1=?,des2=?,des3=? where id=?";
		
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setString(i++,goodsname);
			ps.setInt(i++,price);
			ps.setString(i++,pic1);
			ps.setString(i++,des1);
			ps.setString(i++,des2);
			ps.setString(i++,des3);
			ps.setInt(i++,goodsid);
			return (ps.executeUpdate()==goodsid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return false;
	}
	public static boolean IsGoodsBind(int goodsid) {
		ResultSet rs=null;
		Statement st=null;
		Connection conn=ConnManager.getConnection(CN);
		int c=0;
		String sql=null;

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("SELECT count(*) as c from goodroadinfo where goodsid="+goodsid);
			sql=stringBuilder.toString();
		try {
			st=conn.createStatement();
			rs=st.executeQuery(sql);
			if(rs.next())
			{
				c=rs.getInt("c");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,st);
		}
		return (c>0);
	}
	public static boolean updatePort(int sellerId, int i, int amount,int capacity, int price,int discount) {
		String sql="update [goodroadinfo] set updatetime=getdate(),price="
				+price+ ",amount="+amount+ 
				",capacity="+capacity+",discount="+ discount +" where machineid="+sellerId +" and innerid="+i;
		return exec(sql);
		
	}
	public static boolean updatePort(int sellerId, int i, int amount,int capacity, int price, int discount, int goodsid) {
		String sql="update [goodroadinfo] set updatetime=getdate(),price="
				+price+ ",amount="+amount+ 
				",capacity="+capacity+",discount="+ discount +",goodsid="+ goodsid +" where machineid="+sellerId +" and innerid="+i;
		return exec(sql);
	}
	
	public static boolean updatePortFault(int sellerId, String inneridname, int i) {
		String sql="update [goodroadinfo] set lastErrorTime=getdate(),iserror="+i+ 
				",errorinfo=(select top(1) des from [slot_errcode_tab] where errcode="+ i+") where machineid="+sellerId +" and inneridname='"+inneridname+"'";
		return exec(sql);
	}
	public static boolean updatePortFault(int sellerId, int innerid, int i) {
		String sql="update [goodroadinfo] set lastErrorTime=getdate(),iserror="+i+ 
				" ,errorinfo=(select top(1) CONCAT(des,' - Error: ',errcode) from [slot_errcode_tab] where errcode="+ i+") where machineid="+sellerId +" and innerid="+innerid;
		return exec(sql);
	}
	public static boolean ChkVenderRepeat(int vid) {
		VenderBean vb= getVenderBeanByid(vid);
		return (vb!=null);
	}


	public static void addGoodsPort(int sid, int id,String inneridname) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="INSERT INTO [goodroadinfo] ([machineid],[innerid],[amount],[price],[capacity]" +
				",[goodroadname],[updatetime],[iserror],[errorinfo],[lastErrorTime],[goodsid]" +
				",[DeviceId],[inneridname]) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++,id);
			ps.setInt(i++,sid);
			ps.setInt(i++, 0);
			ps.setInt(i++, 100);
			ps.setInt(i++,5);
			ps.setString(i++, "Channel "+sid);
			ps.setTimestamp(i++, new Timestamp(ClsTime.SystemTime()));
			ps.setInt(i++, 0);
			ps.setString(i++,"");
			ps.setTimestamp(i++, new Timestamp(ClsTime.SystemTime()));
			ps.setInt(i++, 0);
			ps.setInt(i++, 1);
			ps.setString(i++, inneridname);//String.format(clsConst.SLOT_FORMAT,sid)
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}

	
	public static int getAllSalesByDate(java.util.Date temdate,String limit)
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		int amount=0;
		Connection conn=ConnManager.getConnection(CN);

		if(limit.equals(""))
		{
			limit="-1";
		}
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT ");
		String sql="select sum(price) as amount from [traderecordinfo] where (receivetime between ? and ?)" +
				" and (sendstatus=?) and (Changestatus=?) and goodmachineid in("+limit+")";
		try {
			ps=conn.prepareStatement(sql);
			ps.setDate(1,new Date(temdate.getTime()));
			ps.setDate(2,new Date(temdate.getTime()+24*60*60*1000));
			
			ps.setBoolean(3,true);
			ps.setBoolean(4,true);
			rs=ps.executeQuery();
			if(rs.next())
			{
				amount=rs.getInt("amount");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return amount;
	}
	

	
	
	public static boolean SetPortGoodsCount(int vid) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="update goodroadinfo set amount=capacity,updatetime=GETDATE() where machineid=?";
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++,vid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return false;
	}
	

	public static void UpdateMechine_CoinAmont(int vid, int amount) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="update terminalinfo set coinAttube=?,updatetime=GETDATE() where id=?";
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++,amount);
			ps.setInt(i++,vid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}



	public static void UpdatePortAndGoodsid(ArrayList<PortBean> plst) 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="update goodroadinfo set amount=?,capacity=?,price=?,goodsid=?,iserror=?,"
				+ "errorinfo=(select top(1) des from [slot_errcode_tab] where errcode=?),updatetime=GETDATE(),PauseFlg=?  where machineid=? and innerid=?";
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			for (PortBean portBean : plst) 
			{
				int i=1;
				ps.setInt(i++,portBean.getAmount());
				ps.setInt(i++,portBean.getCapacity());
				ps.setInt(i++,portBean.getPrice());
				ps.setInt(i++,portBean.getGoodsid());
				ps.setInt(i++,portBean.getError_id());
				ps.setInt(i++,portBean.getError_id());
				
				ps.setInt(i++,portBean.getPauseFlg());
				
				ps.setInt(i++,portBean.getMachineid());
				ps.setInt(i++,portBean.getInnerid());
				ps.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}
	
	public static void UpdatePort(ArrayList<PortBean> plst) 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="update goodroadinfo set amount=?,capacity=?,price=?,iserror=?,"
				+ "errorinfo=(select top(1) des from [slot_errcode_tab] where errcode=?),updatetime=GETDATE(),PauseFlg=?  where machineid=? and innerid=?";
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			for (PortBean portBean : plst) 
			{
				int i=1;
				ps.setInt(i++,portBean.getAmount());
				ps.setInt(i++,portBean.getCapacity());
				ps.setInt(i++,portBean.getPrice());
				ps.setInt(i++,portBean.getError_id());
				ps.setInt(i++,portBean.getError_id());
				
				ps.setInt(i++,portBean.getPauseFlg());
				
				ps.setInt(i++,portBean.getMachineid());
				ps.setInt(i++,portBean.getInnerid());
				ps.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}
	
	
	public static void UpdatePortByInnername(ArrayList<PortBean> plst) 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="update goodroadinfo set amount=?,goodsid=?,capacity=?,price=?,iserror=?,errorinfo=" +
				"(select top(1) des from [slot_errcode_tab] where errcode=?),updatetime=GETDATE(),PauseFlg=?  "
				+ "where machineid=? and Inneridname=?";
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			for (PortBean portBean : plst) 
			{
				int i=1;
				ps.setInt(i++,portBean.getAmount());
				ps.setInt(i++,portBean.getGoodsid());
				ps.setInt(i++,portBean.getCapacity());
				ps.setInt(i++,portBean.getPrice());
				ps.setInt(i++,portBean.getError_id());
				ps.setInt(i++,portBean.getError_id());
				
				ps.setInt(i++,portBean.getPauseFlg());
				
				ps.setInt(i++,portBean.getMachineid());
				ps.setString(i++, portBean.getInneridname());
				ps.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}
	
	public static void insertTradeObj(TradeBean tbBean) {
		insertTradeObj(tbBean,"traderecordinfo");
	}
	
	public static void insertTradeObjToTemp(TradeBean tbBean) {
		insertTradeObj(tbBean,"traderecordinfo_tem");
	}
	
	public static void insertTradeObj(TradeBean tbBean,String tablename) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		if(tbBean==null)
		{
			return;
		}
		
		//(select goodsname from goodsinfo where goodsinfo.id=goodroadinfo.goodsid)
		
		//getg
		/*添加找零数量字段
		 * changes
		 * 2013年9月5日23:04:13
		 * */
		String sql="insert into ["+tablename+"] (liushuiid,Cardinfo,price," +
				"receivetime,tradetype,goodmachineid,goodroadid,goodsname,sendstatus," +
				"Changestatus,orderid,changes,coin_credit,bill_credit,xmlstr,inneridname,"
				+ "status,tradeid,has_jiesuan,has_feeback) values" +
				"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setString(i++,tbBean.getLiushuiid());
			ps.setString(i++,tbBean.getCardinfo());
			ps.setInt(i++, tbBean.getPrice());
			ps.setString(i++, ToolBox.getYMDHMS(new Timestamp(ClsTime.SystemTime())));
			ps.setInt(i++,tbBean.getTradetype());

			ps.setInt(i++, tbBean.getGoodmachineid());
			ps.setInt(i++, tbBean.getGoodroadid());
			
			ps.setString(i++, tbBean.getGoodsName());
			
			ps.setInt(i++,tbBean.getSendstatus());
			
			ps.setInt(i++,tbBean.getChangestatus());
			
			ps.setString(i++,tbBean.getOrderid());
			
			ps.setInt(i++,tbBean.getChanges());
			
			ps.setInt(i++,tbBean.getCoin_credit());
			ps.setInt(i++,tbBean.getBill_credit());
			
			ps.setString(i++,tbBean.getXmlstr());
			ps.setString(i++,tbBean.getInneridname());
			ps.setInt(i++,tbBean.getStatus());
			
			ps.setString(i++,tbBean.getTradeid());
			ps.setInt(i++,tbBean.getHas_jiesuan());
			ps.setInt(i++,tbBean.getHas_feeback());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}

	public static void AddOffLineTimes(int pollInterval) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="update [terminalinfo] set offlinetimes=offlinetimes+? where offlinetimes<(MaxOffinetimes+1000)";	
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			
			ps.setInt(i++,pollInterval);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}
	
	public static void ClrOffLineTimes(int vid) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="update [terminalinfo] set [offlinetimes]=0, IsOnline=1, is_offline_alert_sent=0 where id=?";	
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++,vid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}

	public static void SetTerminalOffLine() {
		String sql="update [terminalinfo] set IsOnline=1 where offlinetimes<MaxOffinetimes";
		
		exec(sql);
		
		sql="update [terminalinfo] set IsOnline=0,Gprs_Sign=0 where [offlinetimes]>=[MaxOffinetimes]";
		
		exec(sql);
		
	}
	
	public static void updateOfflineAlertSent(int vid) {
		PreparedStatement ps=null;
		ResultSet rs=null;		
		String sql = "update [terminalinfo] set is_offline_alert_sent=1 where id=?";
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++,vid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}		
	}

	public static void SubPortGoods(int vid, String inneridname, int c) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="update [goodroadinfo] set amount=amount-? where amount>0 and  machineid=? and inneridname=?";	
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++,c);
			ps.setInt(i++,vid);
			ps.setString(i++,inneridname);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}

	public static void UpdateMechine_MdbDeviceStatus(int mid, int para1) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="update terminalinfo set MdbDeviceStatus=?,updatetime=GETDATE() where id=?";
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++,para1);
			ps.setInt(i++,mid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}

	public static void UpdateMechine_GprsSign(int mid, int gprs_Sign) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="update terminalinfo set gprs_Sign=?,updatetime=GETDATE() where id=?";
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++,gprs_Sign);
			ps.setInt(i++,mid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}
	
	public static void UpdateMechine_Temperature(int mid, int temperature) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="update terminalinfo set temperature=?,updatetime=GETDATE() where id=?";
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++,temperature);
			ps.setInt(i++,mid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}

	public static String GetVendLinkKey(int mid) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String key=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql="select top 1 private_key from [TerminalInfo] where id="+mid;
		try {
			ps=conn.prepareStatement(sql);rs=ps.executeQuery();
			if(rs.next())
			{
				key=rs.getString("private_key");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return key;
	}
	
	public static void SetVendLinkKey(int mid,String key) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="update terminalinfo set private_key=? where id=?";
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setString(i++, key);
			ps.setInt(i++,mid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}

	public static String getPublicKey(int mid) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String key=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql="select top 1 public_key from [TerminalInfo] where id="+mid;
		try {
			ps=conn.prepareStatement(sql);rs=ps.executeQuery();
			if(rs.next())
			{
				key=rs.getString("public_key");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return key;
	}

	
	public static void SetVendPublicKey(int mid,String key) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql=null;
		sql="update terminalinfo set public_key=? where id=?";
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setString(i++, key);
			ps.setInt(i++,mid);
			ps.executeUpdate();
			//ps.ge
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}

	public static void UpdateMechinePara(VenderBean vb) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql=null;
		sql="update terminalinfo set coinAttube=?,MdbDeviceStatus=?,gprs_Sign=?," +
				"temperature=?,function_flg=?,coinAtbox=?,LstSltE=?,IRErrCnt=?,id_format=?,code_ver=?,bills=?,TemperUpdateTime=?," +
				"prev_temp=?,temp_alert_loop=?,is_alert_sent=?,TemperLoopStartTime=?,is_coin_alert_sent=?,long_temp_loop=?,long_temp_alert_sent=?,long_temp_loop_starttime=?," +
				"refill_temp_loop=?,refill_temp_alert_sent=?,refill_temp_loop_starttime=? "+
				" where id=?";
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			
			ps.setInt(i++, vb.getCoinAttube());
			ps.setInt(i++, vb.getMdbDeviceStatus());
			ps.setInt(i++, vb.getGprs_Sign());
			ps.setInt(i++, vb.getTemperature());
			
			ps.setInt(i++, vb.getFunction_flg());
			
			ps.setInt(i++, vb.getCoinAtbox());
			ps.setInt(i++, vb.getLstSltE());
			
			ps.setInt(i++, vb.getIRErrCnt());
			
			ps.setString(i++, vb.getId_Format());
			ps.setInt(i++, vb.getCode_ver());
			ps.setInt(i++, vb.getBills());
			ps.setString(i++, vb.getTemperUpdateTime());
			ps.setInt(i++, vb.getPrev_temp());
			ps.setInt(i++, vb.getTemp_alert_loop());
			ps.setInt(i++, vb.getIs_alert_sent());
			ps.setString(i++, vb.getTemperLoopStartTime());
			ps.setInt(i++, vb.getIs_coin_alert_sent());
			ps.setInt(i++, vb.getLongTempAlertLoop());
			ps.setInt(i++, vb.getLongTempAlertSent());
			ps.setString(i++, vb.getLongTempLoopStarttime());
			ps.setInt(i++, vb.getRefillTempAlertLoop());
			ps.setInt(i++, vb.getRefillTempAlertSent());
			ps.setString(i++, vb.getRefillTempLoopStarttime());			
			
			ps.setInt(i++,vb.getId());
			ps.executeUpdate();
			//ps.ge
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}

//	private int id;
//	private int activeInterval;
//	
//	private String serverUrl;
//	private int valid;
	public static ClsGprsCfg getGprsConfig(int id) {
		ClsGprsCfg cfg=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql="select top 1 id,activeInterval,serverUrl,objttl,valid,vercode,firmfile,firmcompiletime from [GprsCfg] where valid>0 and id="+id;
		try {
			ps=conn.prepareStatement(sql);rs=ps.executeQuery();
			if(rs.next())
			{
				cfg=new ClsGprsCfg();
				cfg.setId(rs.getInt("id"));
				cfg.setActiveInterval(rs.getInt("activeInterval"));
				cfg.setServerUrl(rs.getString("serverUrl"));
				cfg.setObjTtl(rs.getInt("objttl"));
				cfg.setValid(rs.getInt("valid"));
				cfg.setVerCode(rs.getString("vercode"));
				cfg.setFirmFile(rs.getString("firmfile"));
				cfg.setFirmcompiletime(rs.getDate("firmcompiletime"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return cfg;
	}

	public static void UpdateMechine_VmcFileName(int mid, String filename) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="update terminalinfo set vmc_firmfile=? where id=?";
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setString(i++, filename);
			ps.setInt(i++,mid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}
	
	public static int getClsGprsCfgCount() 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		int c=0;
		String sql="SELECT count(id) as count from dbo.GprsCfg";
		
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);rs=ps.executeQuery();
			if(rs.next())
			{
				c=rs.getInt("count");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return c;
	}
	public static ArrayList<ClsGprsCfg> getClsGprsCfgsLst(int page,int count_per_page)
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		ArrayList<ClsGprsCfg> lstArrayList=new ArrayList<ClsGprsCfg>();
		//String sql="SELECT id,activeInterval,serverUrl,valid,objttl,vercode,firmfile,firmcompiletime from dbo.GprsCfg";
		
		String sql=("SELECT top("+count_per_page+") id, activeInterval,serverUrl,valid,objttl,vercode,firmfile,firmcompiletime " +
				" FROM GprsCfg where id not in (SELECT top("+((page-1)*count_per_page)+") id FROM GprsCfg order by id asc) order by id asc");
		
		
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);rs=ps.executeQuery();
			ClsGprsCfg cfg=null;
			while(rs.next())
			{
				cfg=new ClsGprsCfg();
				cfg.setId(rs.getInt("id"));
				cfg.setActiveInterval(rs.getInt("activeInterval"));
				cfg.setServerUrl(rs.getString("serverUrl"));
				cfg.setObjTtl(rs.getInt("objttl"));
				cfg.setValid(rs.getInt("valid"));
				cfg.setVerCode(rs.getString("vercode"));
				cfg.setFirmFile(rs.getString("firmfile"));
				cfg.setFirmcompiletime(rs.getDate("firmcompiletime"));
				lstArrayList.add(cfg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return lstArrayList;
	}	
	public static ArrayList<ClsGprsCfg> getClsGprsCfgsLst()
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		ArrayList<ClsGprsCfg> lstArrayList=new ArrayList<ClsGprsCfg>();
		String sql="SELECT id,activeInterval,serverUrl,valid,objttl,vercode,firmfile,firmcompiletime from dbo.GprsCfg";
		
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);rs=ps.executeQuery();
			ClsGprsCfg cfg=null;
			while(rs.next())
			{
				cfg=new ClsGprsCfg();
				cfg.setId(rs.getInt("id"));
				cfg.setActiveInterval(rs.getInt("activeInterval"));
				cfg.setServerUrl(rs.getString("serverUrl"));
				cfg.setObjTtl(rs.getInt("objttl"));
				cfg.setValid(rs.getInt("valid"));
				cfg.setVerCode(rs.getString("vercode"));
				cfg.setFirmFile(rs.getString("firmfile"));
				cfg.setFirmcompiletime(rs.getDate("firmcompiletime"));
				lstArrayList.add(cfg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return lstArrayList;
	}

	public static void UpdateGTCfg(ClsGprsCfg objCfg) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="update GprsCfg set activeInterval=?,serverUrl=?,valid=?,objttl=?," +
				"vercode=?,firmfile=?,firmcompiletime=? where id=?";
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++, objCfg.getActiveInterval());
			ps.setString(i++, objCfg.getServerUrl());
			ps.setInt(i++, objCfg.getValid());
			ps.setInt(i++, objCfg.getObjTtl());
			ps.setString(i++, objCfg.getVerCode());
			ps.setString(i++, objCfg.getFirmFile());
			ps.setDate(i++, objCfg.getFirmcompiletime());
			ps.setInt(i++,objCfg.getId());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		
	}

	public static void UpdateMechine_CanUse(int mid, boolean canuse) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="update terminalinfo set CanUse=? where id=?";
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setBoolean(i++, canuse);
			ps.setInt(i++,mid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}




	public static PortBean getDistinctPortBean(int vendid,int goodsid) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		ArrayList<PortBean>  lst=new ArrayList<PortBean>();
		PortBean temp=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql="SELECT DISTINCT	goodroadinfo.machineid,goodroadinfo.goodsid,SUM (goodroadinfo.amount) AS inventory,goodroadinfo.price,goodsinfo.des1,goodsinfo.goodsname "
				+ "FROM goodroadinfo"
				+ "INNER JOIN goodsinfo ON (goodroadinfo.goodsid = goodsinfo.id)"
				+ "WHERE	goodroadinfo.goodsid = ? AND goodroadinfo.machineid = ?	 AND iserror =0 and pauseFlg=0" 
				+ "GROUP BY goodroadinfo.machineid,goodroadinfo.goodsid,goodroadinfo.price,goodsinfo.des1,goodsinfo.goodsname";
		try {
			
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++, goodsid);
			ps.setInt(i++, vendid);
			rs=ps.executeQuery();
			
			while(rs.next())
			{
				temp=new PortBean();
				temp.setAmount(rs.getInt("inventory"));
				temp.setGoodroadname(rs.getString("goodsname"));
				temp.setGoodsid(rs.getInt("goodsid"));
				temp.setMachineid(rs.getInt("machineid"));
				temp.setPrice(rs.getInt("price"));
				temp.setDesc(rs.getString("des1"));
				lst.add(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return temp;
	}
	
	public static ArrayList<PortBean> getDistinctPortBeanLst(String vendids) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		ArrayList<PortBean>  lst=new ArrayList<PortBean>();
		PortBean temp=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql="SELECT DISTINCT	goodroadinfo.machineid,goodroadinfo.goodsid,SUM (goodroadinfo.amount) AS inventory,goodroadinfo.price,goodsinfo.des1,goodsinfo.goodsname "
				+ "FROM goodroadinfo "
				+ "INNER JOIN goodsinfo ON (goodroadinfo.goodsid = goodsinfo.id) "
				+ "WHERE	goodroadinfo.pauseFlg = 0 and goodroadinfo.machineid  in ("+ vendids +") "
				+ "GROUP BY goodroadinfo.machineid,goodroadinfo.goodsid,goodroadinfo.price,goodsinfo.des1,goodsinfo.goodsname";
		try {
			
			ps=conn.prepareStatement(sql);
			rs=ps.executeQuery();
			
			while(rs.next())
			{
				temp=new PortBean();
				temp.setAmount(rs.getInt("inventory"));
				temp.setGoodroadname(rs.getString("goodsname"));
				temp.setGoodsid(rs.getInt("goodsid"));
				temp.setMachineid(rs.getInt("machineid"));
				temp.setPrice(rs.getInt("price"));
				temp.setDesc(rs.getString("des1"));
				lst.add(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return lst;
	}



	



	
	public static void InsertAliQrCodeLog(Cls_AliQrCodeLog alicodelog) 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
				String sql="INSERT INTO [qr_code_request_log] ( [querystring], [response], [machineid], [goodsid], [sendtime], [isok], [qrcode], [qrcode_img_url]) VALUES "
						+ "(?, ?,?,?,?,?,?,?)";
				Connection conn=ConnManager.getConnection(CN);
				try {
					int i=1;
					ps=conn.prepareStatement(sql);
					ps.setString(i++, alicodelog.getQuerystring());
					ps.setString(i++, alicodelog.getResponse());
					ps.setString(i++, alicodelog.getMachineid());
					ps.setString(i++, alicodelog.getGoodsid());
					ps.setString(i++, alicodelog.getSendtime());
					ps.setInt(i++, alicodelog.getIsok());
					ps.setString(i++, alicodelog.getQrcode());
					ps.setString(i++, alicodelog.getQrcode_img_url());
					
					ps.executeUpdate();

				} catch (Exception e) {
					e.printStackTrace();
				}
				finally
				{
					ConnManager.freeConnection(CN, conn,rs,ps);
				}
	}

	public static void UpdateGoodsPortQrCode(int machineid, int goodsid,String qrcode, String qrcode_img_url) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="UPDATE [goodroadinfo] set qrcode=? where goodsid=? and machineid=?";
		Connection conn=ConnManager.getConnection(CN);
		try {
			int i=1;
			ps=conn.prepareStatement(sql);
			ps.setString(i++, qrcode);
			ps.setInt(i++, goodsid);
			ps.setInt(i++, machineid);

			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}

	}
	
	public static void UpdateGoodsPortWxQrCode(int machineid, int goodsid,String wx_qrcode, String wx_qrcodeurl) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="UPDATE [goodroadinfo] set wx_qrcode=? where goodsid=? and machineid=?";
		Connection conn=ConnManager.getConnection(CN);
		try {
			int i=1;
			ps=conn.prepareStatement(sql);
			ps.setString(i++, wx_qrcode);
			ps.setInt(i++, goodsid);
			ps.setInt(i++, machineid);

			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}

	}
	//[gmt_payment]=?, [partner]=?, [buyer_email]=?, [trade_no]=?, [total_fee]=?, [gmt_create]=?, [trade_status]=?, [notifyXML]=?, [transfor_status]
	public static void 	InsertNewAliTrade(Cls_AliTradeLog trade)
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="INSERT INTO [AliTradeLog] ([out_trade_no], [subject], [qrcode], [userid], "
				+ "[machineid], [goodsid],[gmt_payment],[partner],[buyer_email],[trade_no],"
				+ "[total_fee],[gmt_create],[trade_status],[notifyXML],[transfor_status]) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection conn=ConnManager.getConnection(CN);
		try {
			int i=1;
			ps=conn.prepareStatement(sql);
			ps.setString(i++, trade.getOut_trade_no());
			ps.setString(i++, trade.getSubject());
			ps.setString(i++, trade.getQrcode());
			ps.setString(i++, trade.getUserid());
			ps.setString(i++, trade.getMachineid());
			ps.setString(i++, trade.getGoodsid());
			
			ps.setString(i++, trade.getGmt_payment());
			ps.setString(i++, trade.getPartner());
			ps.setString(i++, trade.getBuyer_email());
			ps.setString(i++, trade.getTrade_no());
			ps.setString(i++, trade.getTotal_fee());
			ps.setString(i++, trade.getGmt_create());
			ps.setString(i++, trade.getTrade_status());
			ps.setString(i++, trade.getNotifyXML());
			ps.setInt(i++, trade.getTransfor_status());
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}

	public static void 	UpdateAliTrade(Cls_AliTradeLog trade)
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="UPDATE TOP(1) [AliTradeLog] SET [gmt_payment]=?, [partner]=?, [buyer_email]=?, [trade_no]=?, [total_fee]=?, [gmt_create]=?, [trade_status]=?, [notifyXML]=?, [transfor_status]=? WHERE ([out_trade_no]=?)";
		Connection conn=ConnManager.getConnection(CN);
		try {
			int i=1;
			ps=conn.prepareStatement(sql);
			ps.setString(i++, trade.getGmt_payment());
			ps.setString(i++, trade.getPartner());
			ps.setString(i++, trade.getBuyer_email());
			ps.setString(i++, trade.getTrade_no());
			ps.setString(i++, trade.getTotal_fee());
			ps.setString(i++, trade.getGmt_create());
			ps.setString(i++, trade.getTrade_status());
			ps.setString(i++, trade.getNotifyXML());
			ps.setInt(i++, trade.getTransfor_status());
			ps.setString(i++, trade.getOut_trade_no());
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}

	public static void AddTableCmd(int machineid,int cmd, String hexstring,int alivetime,long createtime,String rem)
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
//INSERT INTO tablecmd (machineid, cmd, hexstring, status, alivetime, createtime, rem) VALUES (1, 6, 11111, 0, 12, 12, '支付宝扫码交易')
		String sql="INSERT INTO [tablecmd] ([machineid], [cmd], [hexstring], [status], [alivetime], [createtime], [extfield1], [extfield2], [extfield3], [rem]) "
				+ "VALUES (?, ?, ?, ?, ?, ?,?,?,?,?)";
		Connection conn=ConnManager.getConnection(CN);
		try {
			int i=1;
			ps=conn.prepareStatement(sql);
			ps.setInt(i++, machineid);
			ps.setInt(i++, cmd);
			ps.setString(i++, hexstring);
			ps.setInt(i++, 0);
			ps.setInt(i++, alivetime);
			ps.setLong(i++, createtime);
			ps.setString(i++,"");
			ps.setString(i++,"");
			ps.setString(i++,"");
			ps.setString(i++,rem);

			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}

	public static clsSellerPara getSellerPara(int id) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		StringBuilder strSql=new StringBuilder();
		strSql.append("select top(1) id,UssdPort,SellerPort,OutGoodsOverTime,TwiceIntervalTime,AllStop,MaxTimes,ActionInterval,SmsThreadCount,"
				+ "CopeTradeThreadCount,MaxSeller,Pwd,UmpayThreadCount,EndTradeThreadCount,payovertime,feebackovertime ");
		strSql.append(" FROM [SellerParam] ");
		strSql.append(" where id=? ");
		clsSellerPara para=null;
		Connection conn=ConnManager.getConnection(CN);
		try {
			int i=1;
			ps=conn.prepareStatement(strSql.toString());
			ps.setInt(i++, id);
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				para=new clsSellerPara();
				para.set_id(rs.getInt("id"));
//				para.set_actioninterval(rs.getInt(""));
//				para.set_allstop(rs.getBoolean(""));
//				para.set_copetradethreadcount(rs.getInt(""));
//				para.set_endtradethreadcount(rs.getInt(""));
//				para.set_feebackovertime(rs.getInt(""));
//
//				para.set_maxseller(rs.getInt(""));
//				para.set_maxtimes(rs.getInt(""));
//				para.set_outgoodsovertime(rs.getInt(""));
//				para.set_payovertime(rs.getInt(""));
//				para.set_pwd(rs.getString(""));
				
				para.set_sellerport(rs.getInt("SellerPort"));
				
				//para.set_smsthreadcount(_smsthreadcount);
				//para.set
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return para;
	}

	public static Cls_AliTradeLog getFirsttclsAliTradeLog(int transfor_status) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		StringBuilder strSql=new StringBuilder();

		strSql.append("select top(1) [id],[gmt_payment],[partner],[buyer_email],[trade_no],[total_fee],[gmt_create],[out_trade_no],[subject],[trade_status],");
		strSql.append(" [qrcode],[userid],[notifyXML],[machineid],[goodsid],[transfor_status] from [AliTradeLog] where [transfor_status]=?  order by id ");

		Cls_AliTradeLog para=null;
		Connection conn=ConnManager.getConnection(CN);
		try {
			int i=1;
			ps=conn.prepareStatement(strSql.toString());
			ps.setInt(i++, transfor_status);
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				para=new Cls_AliTradeLog();
				para.setId(rs.getInt("id"));
				para.setGmt_payment(rs.getString("gmt_payment"));
				para.setPartner(rs.getString("partner"));
				para.setBuyer_email(rs.getString("buyer_email"));
				para.setTrade_no(rs.getString("trade_no"));
				para.setTotal_fee(rs.getString("total_fee"));
				para.setGmt_create(rs.getString("gmt_create"));
				para.setOut_trade_no(rs.getString("out_trade_no"));
				
				para.setSubject(rs.getString("subject"));
				para.setTrade_status(rs.getString("trade_status"));
				para.setQrcode(rs.getString("qrcode"));
				para.setUserid(rs.getString("userid"));
				para.setNotifyXML(rs.getString("notifyXML"));
				para.setMachineid(rs.getString("machineid"));
				para.setGoodsid(rs.getString("goodsid"));
				para.setTransfor_status(rs.getInt("transfor_status"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		
		return para;
	}
	
	public static Cls_AliTradeLog getclsAliTradeLog(String out_trade_no) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		StringBuilder strSql=new StringBuilder();

		strSql.append("select top(1) [id],[gmt_payment],[partner],[buyer_email],[trade_no],[total_fee],[gmt_create],[out_trade_no],[subject],[trade_status],");
		strSql.append(" [qrcode],[userid],[notifyXML],[machineid],[goodsid],[transfor_status] from [AliTradeLog] where [out_trade_no]=?  order by id ");

		Cls_AliTradeLog para=null;
		Connection conn=ConnManager.getConnection(CN);
		try {
			int i=1;
			ps=conn.prepareStatement(strSql.toString());
			ps.setString(i++, out_trade_no);
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				para=new Cls_AliTradeLog();
				para.setId(rs.getInt("id"));
				para.setGmt_payment(rs.getString("gmt_payment"));
				para.setPartner(rs.getString("partner"));
				para.setBuyer_email(rs.getString("buyer_email"));
				para.setTrade_no(rs.getString("trade_no"));
				para.setTotal_fee(rs.getString("total_fee"));
				para.setGmt_create(rs.getString("gmt_create"));
				para.setOut_trade_no(rs.getString("out_trade_no"));
				
				para.setSubject(rs.getString("subject"));
				para.setTrade_status(rs.getString("trade_status"));
				para.setQrcode(rs.getString("qrcode"));
				para.setUserid(rs.getString("userid"));
				para.setNotifyXML(rs.getString("notifyXML"));
				para.setMachineid(rs.getString("machineid"));
				para.setGoodsid(rs.getString("goodsid"));
				para.setTransfor_status(rs.getInt("transfor_status"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		
		return para;
	}

	public static ArrayList<clsTableCmd> getclsTableCmdLst(int status,int mid) 
	{
		//
		PreparedStatement ps=null;
		ResultSet rs=null;
		StringBuilder strSql=new StringBuilder();

		strSql.append("select [id],[machineid],[cmd],[hexstring],[status],[alivetime],[createtime],[extfield1],[extfield2],[extfield3],[rem] from [tablecmd]");
		strSql.append(" where [status]=? and machineid=?   order by id ");

		ArrayList<clsTableCmd> tablecmdlst=new ArrayList<clsTableCmd>();
		Connection conn=ConnManager.getConnection(CN);
		try {
			int i=1;
			ps=conn.prepareStatement(strSql.toString());
			ps.setInt(i++, status);
			ps.setInt(i++, mid);	
			rs = ps.executeQuery();
			clsTableCmd tablecmd=null;
			while(rs.next())
			{
				tablecmd=new clsTableCmd();
				tablecmd.set_alivetime(rs.getInt("alivetime"));
				tablecmd.set_cmd(rs.getInt("cmd"));
				tablecmd.set_createtime(rs.getLong("createtime"));
				tablecmd.set_extfield1(rs.getString("extfield1"));
				tablecmd.set_extfield2(rs.getString("extfield2"));
				tablecmd.set_extfield3(rs.getString("extfield3"));
				tablecmd.set_hexstring(rs.getString("hexstring"));
				tablecmd.set_id(rs.getInt("id"));
				tablecmd.set_machineid(rs.getInt("machineid"));
				tablecmd.set_status(rs.getInt("status"));
				tablecmdlst.add(tablecmd);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		
		return tablecmdlst;
	}

	public static void update(clsTableCmd tcmd) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		//update [tablecmd] set [id] = ?,[machineid] = ?,[cmd] = ?,[hexstring] = ?,[status] = ?,[alivetime] = ?,[createtime] = ?,[extfield1] = ?,[extfield2] = ?,[extfield3] = ?,[rem] = ? where <自定义搜索条件>
		String sql="update [tablecmd] set [machineid] = ?,[cmd] = ?,[hexstring] = ?,[status] = ?,[alivetime] = ?,[createtime] = ?,[extfield1] = ?,[extfield2] = ?,[extfield3] = ?,[rem] = ? where id=?";
		Connection conn=ConnManager.getConnection(CN);
		try {
			int i=1;
			ps=conn.prepareStatement(sql);
			ps.setInt(i++, tcmd.get_machineid());
			ps.setInt(i++, tcmd.get_cmd());
			ps.setString(i++, tcmd.get_hexstring());
			ps.setInt(i++, tcmd.get_status());
			ps.setInt(i++, tcmd.get_alivetime());
			ps.setLong(i++, tcmd.get_createtime());
			ps.setString(i++, tcmd.get_extfield1());
			ps.setString(i++, tcmd.get_extfield2());
			ps.setString(i++, tcmd.get_extfield3());
			ps.setString(i++, tcmd.getRem());
			ps.setInt(i++, tcmd.get_id());
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}

	public static clsGetGoodsCode getGetGoodsCode(String goodscodeid) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		StringBuilder strSql=new StringBuilder();
		strSql.append("SELECT top(1) id,gmt_payment,partner,buyer_email,trade_no,total_fee,gmt_create,subject,trade_status,machineid,goodsid,transfor_status");
		strSql.append(" FROM GetGoodsCode where trade_no=? and trade_status=0");
		clsGetGoodsCode para=null;
		Connection conn=ConnManager.getConnection(CN);
		try {
			int i=1;
			ps=conn.prepareStatement(strSql.toString());
			ps.setString(i++, goodscodeid);
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				para=new clsGetGoodsCode();
				para.setId(rs.getInt("id"));
				para.setGmtpayment(rs.getString("gmt_payment"));
				para.setPartner(rs.getString("partner"));
				para.setBuyeremail(rs.getString("buyer_email"));
				para.setTradeno(rs.getString("trade_no"));
				para.setTotalfee(rs.getString("total_fee"));
				para.setGmtcreate(rs.getString("gmt_create"));
				
				para.setSubject(rs.getString("subject"));
				para.setTradestatus(rs.getString("trade_status"));
				para.setTransforstatus(rs.getInt("transfor_status"));

				para.setMachineid(rs.getString("machineid"));
				para.setGoodsid(rs.getString("goodsid"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		
		return para;
		
	}
	public static PortBean getValidPortBeanByGoods(int mid, int goodsid, int err) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		PortBean temp=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql="SELECT top 1 machineid,innerid,Inneridname,id,errorinfo,iserror," +
					" updatetime, goodroadname, price, amount, lastErrorTime, huodongid,capacity,goodsid,deviceid,PauseFlg" +
					" FROM goodroadinfo WHERE machineid=? and goodsid=? and iserror=? and amount>0" +
					"order by machineid,innerid asc";
		try {
			
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++, mid);
			ps.setInt(i++, goodsid);
			ps.setInt(i++, err);
			rs=ps.executeQuery();
			if(rs.next())
			{
				temp=new PortBean();
				temp.setMachineid(rs.getInt("machineid"));
				temp.setInnerid(rs.getInt("innerid"));
				temp.setInneridname(rs.getString("Inneridname"));
				temp.setId(rs.getInt("id"));
				temp.setErrorinfo(rs.getString("errorinfo"));
				temp.setError_id(rs.getInt("iserror"));
				temp.setUpdatetime(rs.getTimestamp("updatetime"));
				temp.setGoodroadname(rs.getString("goodroadname"));
				temp.setPrice(rs.getInt("price"));
				temp.setAmount(rs.getInt("amount"));
				temp.setLastErrorTime(rs.getTimestamp("lastErrorTime"));
				temp.setHuodongid(rs.getInt("huodongid"));
				temp.setCapacity(rs.getInt("capacity"));
				temp.setGoodsid(rs.getInt("goodsid"));
				temp.setDeviceid(rs.getInt("deviceid"));
				temp.setPauseFlg(rs.getInt("PauseFlg"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return temp;

	}

	public static PortBean getPortBeanByGoods(int mid, int goodsid, int err) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		PortBean temp=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql="SELECT top 1 machineid,innerid,Inneridname,id,errorinfo,iserror," +
					" updatetime, goodroadname, price, amount, lastErrorTime, huodongid,capacity,goodsid,deviceid,PauseFlg" +
					" FROM goodroadinfo WHERE machineid=? and goodsid=? and iserror=? " +
					"order by machineid,innerid asc";
		try {
			
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++, mid);
			ps.setInt(i++, goodsid);
			ps.setInt(i++, err);
			rs=ps.executeQuery();
			if(rs.next())
			{
				temp=new PortBean();
				temp.setMachineid(rs.getInt("machineid"));
				temp.setInnerid(rs.getInt("innerid"));
				temp.setInneridname(rs.getString("Inneridname"));
				temp.setId(rs.getInt("id"));
				temp.setErrorinfo(rs.getString("errorinfo"));
				temp.setError_id(rs.getInt("iserror"));
				temp.setUpdatetime(rs.getTimestamp("updatetime"));
				temp.setGoodroadname(rs.getString("goodroadname"));
				temp.setPrice(rs.getInt("price"));
				temp.setAmount(rs.getInt("amount"));
				temp.setLastErrorTime(rs.getTimestamp("lastErrorTime"));
				temp.setHuodongid(rs.getInt("huodongid"));
				temp.setCapacity(rs.getInt("capacity"));
				temp.setGoodsid(rs.getInt("goodsid"));
				temp.setDeviceid(rs.getInt("deviceid"));
				temp.setPauseFlg(rs.getInt("PauseFlg"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return temp;

	}

	public static void InsertNewWxTrade(WxTradeLog trade) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="INSERT INTO [WxTradeLog] ( [gmt_payment], [partner], [buyer_email], [trade_no], [total_fee], [gmt_create], [out_trade_no], [subject], [trade_status], [qrcode], [userid], [notifyXML], [machineid], [goodsid], [transfor_status])"
				+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		Connection conn=ConnManager.getConnection(CN);
		try {
			int i=1;
			ps=conn.prepareStatement(sql);
			ps.setString(i++, trade.getGmt_payment());
			ps.setString(i++, trade.getPartner());
			
			ps.setString(i++, trade.getBuyer_email());
			ps.setString(i++, trade.getTrade_no());
			
			ps.setString(i++, trade.getTotal_fee());
			ps.setString(i++, trade.getGmt_create());
			
			ps.setString(i++, trade.getOut_trade_no());
			ps.setString(i++, trade.getSubject());
			
			ps.setString(i++, trade.getTrade_status());
			ps.setString(i++, trade.getQrcode());
			
			ps.setString(i++, trade.getUserid());
			
			ps.setString(i++, trade.getNotifyXML());
			
			ps.setString(i++, trade.getMachineid());
			ps.setString(i++, trade.getGoodsid());
			ps.setInt(i++, trade.getTransfor_status());
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}

	public static WxTradeLog getWxTradeLog(String out_trade_no) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		StringBuilder strSql=new StringBuilder();

		strSql.append("select top(1) [id],[gmt_payment],[partner],[buyer_email],[trade_no],[total_fee],[gmt_create],[out_trade_no],[subject],[trade_status],");
		strSql.append(" [qrcode],[userid],[notifyXML],[machineid],[goodsid],[transfor_status] from [WxTradeLog] where [out_trade_no]=?  order by id ");

		WxTradeLog para=null;
		Connection conn=ConnManager.getConnection(CN);
		try {
			int i=1;
			ps=conn.prepareStatement(strSql.toString());
			ps.setString(i++, out_trade_no);
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				para=new WxTradeLog();
				para.setId(rs.getInt("id"));
				para.setGmt_payment(rs.getString("gmt_payment"));
				para.setPartner(rs.getString("partner"));
				para.setBuyer_email(rs.getString("buyer_email"));
				para.setTrade_no(rs.getString("trade_no"));
				para.setTotal_fee(rs.getString("total_fee"));
				para.setGmt_create(rs.getString("gmt_create"));
				para.setOut_trade_no(rs.getString("out_trade_no"));
				
				para.setSubject(rs.getString("subject"));
				para.setTrade_status(rs.getString("trade_status"));
				para.setQrcode(rs.getString("qrcode"));
				para.setUserid(rs.getString("userid"));
				para.setNotifyXML(rs.getString("notifyXML"));
				para.setMachineid(rs.getString("machineid"));
				para.setGoodsid(rs.getString("goodsid"));
				para.setTransfor_status(rs.getInt("transfor_status"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		
		return para;
	}
	
	
	public static void 	UpdateWxTradeLog(WxTradeLog trade)
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="UPDATE TOP(1) [WxTradeLog] SET [gmt_payment]=?, [partner]=?, [buyer_email]=?, [trade_no]=?, [total_fee]=?, [gmt_create]=?, [trade_status]=?, [notifyXML]=?, [transfor_status]=? WHERE ([out_trade_no]=?)";
		Connection conn=ConnManager.getConnection(CN);
		try {
			int i=1;
			ps=conn.prepareStatement(sql);
			ps.setString(i++, trade.getGmt_payment());
			ps.setString(i++, trade.getPartner());
			ps.setString(i++, trade.getBuyer_email());
			ps.setString(i++, trade.getTrade_no());
			ps.setString(i++, trade.getTotal_fee());
			ps.setString(i++, trade.getGmt_create());
			ps.setString(i++, trade.getTrade_status());
			ps.setString(i++, trade.getNotifyXML());
			ps.setInt(i++, trade.getTransfor_status());
			ps.setString(i++, trade.getOut_trade_no());
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}

	public static ArrayList<PortBean> getTheNewestPortBeanList(Timestamp date) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String goodsname="";
		String tem_str="";
		ArrayList<PortBean> li=new ArrayList<PortBean>(5);
		Connection conn=ConnManager.getConnection(CN);
		String sql="SELECT machineid,innerid,Inneridname,id,errorinfo,iserror," +
					" updatetime, (select goodsname from goodsinfo where goodsinfo.id=goodroadinfo.goodsid) as goodroadname, " +
					"price, amount, lastErrorTime, huodongid,capacity,goodsid,discount,PauseFlg,qrcode,qrcode2,wx_qrcode,wx_qrcode2," +
					"al_tradeid,al_tradeid2,wx_tradeid,wx_tradeid2,updateqrtime,updateqrtime2  "
					+ "FROM goodroadinfo WHERE (machineid<>2004) and ((updateqrtime=null) or(updateqrtime2=null) "
					+ "or (wx_qrcode2='') or (qrcode2='') or (wx_qrcode2=null) or (qrcode2=null) or (updateqrtime<?)"
					+ " or (updateqrtime2<?))";
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setTimestamp(i++, date);
			ps.setTimestamp(i++, date);
			rs=ps.executeQuery();
			while(rs.next())
			{
				PortBean temp=new PortBean();
				temp.setMachineid(rs.getInt("machineid"));
				temp.setInnerid(rs.getInt("innerid"));
				temp.setInneridname(rs.getString("Inneridname"));
				temp.setId(rs.getInt("id"));
				temp.setErrorinfo(rs.getString("errorinfo"));
				temp.setError_id(rs.getInt("iserror"));
				temp.setUpdatetime(rs.getTimestamp("updatetime"));
				goodsname=String.format("商品%s", temp.getInneridname());
				tem_str=rs.getString("goodroadname");
				if(tem_str!=null)
				{
					if(!tem_str.trim().equals(""))
					{
						goodsname=tem_str;
					}
				}
				temp.setGoodroadname(goodsname);
				temp.setPrice(rs.getInt("price"));
				temp.setAmount(rs.getInt("amount"));
				temp.setLastErrorTime(rs.getTimestamp("lastErrorTime"));
				temp.setHuodongid(rs.getInt("huodongid"));
				temp.setCapacity(rs.getInt("capacity"));
				temp.setGoodsid(rs.getInt("goodsid"));
				temp.setDiscount(rs.getInt("discount"));
				temp.setPauseFlg(rs.getInt("PauseFlg"));
				temp.setQrcode(rs.getString("qrcode"));
				temp.setQrcode2(rs.getString("qrcode2"));
				temp.setAl_trade(rs.getString("al_tradeid"));
				temp.setAl_trade2(rs.getString("al_tradeid2"));
				
				temp.setWx_qrcode(rs.getString("wx_qrcode"));
				temp.setWx_qrcode2(rs.getString("wx_qrcode2"));
				temp.setWx_trade(rs.getString("wx_tradeid"));
				temp.setWx_trade2(rs.getString("wx_tradeid2"));
				temp.setUpdateqrtime(rs.getTimestamp("updateqrtime"));
				temp.setUpdateqrtime2(rs.getTimestamp("updateqrtime2"));
				li.add(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return li;
	}

	public static void UpdateGoodsPortAllQrCode(PortBean pb) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="UPDATE [goodroadinfo] set amount=?,price=?, qrcode=?,qrcode2=?,wx_qrcode=?,wx_qrcode2=?,al_tradeid=?,al_tradeid2=?,"
				+ "wx_tradeid=?,wx_tradeid2=?,updateqrtime=?,updateqrtime2=? where innerid=? and machineid=?";
		Connection conn=ConnManager.getConnection(CN);
		try {
			int i=1;
			ps=conn.prepareStatement(sql);
			ps.setInt(i++, pb.getAmount());
			ps.setInt(i++, pb.getPrice());
			ps.setString(i++, pb.getQrcode());
			ps.setString(i++, pb.getQrcode2());
			
			ps.setString(i++, pb.getWx_qrcode());
			ps.setString(i++, pb.getWx_qrcode2());
			
			ps.setString(i++, pb.getAl_trade());
			ps.setString(i++, pb.getAl_trade2());
			
			ps.setString(i++, pb.getWx_trade());
			ps.setString(i++, pb.getWx_trade2());
			
			
			ps.setTimestamp(i++, pb.getUpdateqrtime());
			ps.setTimestamp(i++, pb.getUpdateqrtime2());
			
			ps.setInt(i++, pb.getInnerid());
			ps.setInt(i++, pb.getMachineid());


			
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}
	//UPDATE TOP(1) [zhouwenjing].[dbo].[traderecordinfo] SET [id]='23928', [liushuiid]='36', [cardinfo]='ohlcnuNqlM4o1CHlzgN4JvrkAhYI', [orderid]='20151209140330002004678667', [price]='200', [mobilephone]=NULL, [tradetype]='6', [goodmachineid]='2004', [goodroadid]='0', [changestatus]='1', [sendstatus]='1', [status]='0', [receivetime]='2015-12-09 14:03:51', [retmes]=NULL, [xmlstr]='{"buyer":"","goodsid":"0348","id":2026,"inneridname":"","machineid":"00002004","out_trade_no":"20151209140330002004678667","partner":"ohlcnuNqlM4o1CHlzgN4JvrkAhYI","subject":"百事可乐罐装"}', [goodsname]='百事可乐罐装', [groupid]='0', [changes]='0', [coin_credit]='0', [bill_credit]='0', [inneridname]='F1' WHERE ([id]='23928');
	public static void updateTradeBean(TradeBean tb)
	{
		updateTradeBean(tb,"traderecordinfo");
	}
	public static void updateTradeBeanTemp(TradeBean tb)
	{
		updateTradeBean(tb,"traderecordinfo_tem");
	}
	public static void updateTradeBean(TradeBean tb,String tablename) 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="UPDATE TOP(1) ["+tablename+"] SET [liushuiid]=?, [cardinfo]=?, "
				+ "[orderid]=?, [price]=?, [mobilephone]=?, [tradetype]=?, [goodmachineid]=?, [goodroadid]=?, "
				+ "[changestatus]=?, [sendstatus]=?, [status]=?, [receivetime]=?, [xmlstr]=?,"
				+ " [goodsname]=?,  [changes]=?, [coin_credit]=?, [bill_credit]=?, [inneridname]=?,tradeid=?,has_jiesuan=?,"
				+ " has_feeback=?,retmes=? "
				+ " WHERE ([id]=?)";
		Connection conn=ConnManager.getConnection(CN);
		
		try {
			int i=1;
			ps=conn.prepareStatement(sql);
			ps.setString(i++, tb.getLiushuiid());
			ps.setString(i++, tb.getCardinfo());
			
			ps.setString(i++, tb.getOrderid());
			ps.setInt(i++, tb.getPrice());
			
			ps.setString(i++, tb.getMobilephone());
			ps.setInt(i++, tb.getTradetype());
			
			ps.setInt(i++, tb.getGoodmachineid());
			ps.setInt(i++, tb.getGoodroadid());
			
			
			ps.setInt(i++, tb.getChangestatus());
			ps.setInt(i++, tb.getSendstatus());
			
			ps.setInt(i++, tb.getStatus());
			ps.setString(i++, ToolBox.getYMDHMS(tb.getReceivetime()));
			
			ps.setString(i++, tb.getXmlstr());
			ps.setString(i++, tb.getGoodsName());
			
			ps.setInt(i++, tb.getChanges());
			
			ps.setInt(i++, tb.getCoin_credit());
			ps.setInt(i++, tb.getBill_credit());
			
			ps.setString(i++, tb.getInneridname());
			ps.setString(i++, tb.getTradeid());
			
			ps.setInt(i++, tb.getHas_jiesuan());
			ps.setInt(i++, tb.getHas_feeback());
			ps.setString(i++, tb.getRetmes());
			
			ps.setInt(i++, tb.getId());

			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		
	}

		
	public static ArrayList<clsGetGoodsCode> getclsGetGoodsCode()
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		ArrayList<clsGetGoodsCode> ubli=new ArrayList<clsGetGoodsCode>(5);
		Connection conn=ConnManager.getConnection(CN);
		String sql="select * from GetGoodsCode";
			sql+=" order by gmt_create desc";
		try {
			ps=conn.prepareStatement(sql);rs=ps.executeQuery();
			while(rs.next())
			{
				clsGetGoodsCode ub=new clsGetGoodsCode();
				ub.setId(rs.getInt("id"));
				ub.setBuyeremail(rs.getString("buyeremail"));
				ub.setGmtcreate(rs.getString("gmtcreate"));
				ub.setGmtpayment(rs.getString("gmtpayment"));
				ub.setGoodsid(rs.getString("goodsid"));
				ub.setInneridname(rs.getString("inneridname"));
				ub.setMachineid(rs.getString("machineid"));
				ub.setPartner(rs.getString("partner"));
				ub.setSubject(rs.getString("subject"));
				ub.setTradeno(rs.getString("tradeno"));
				ub.setTradestatus(rs.getString("tradestatus"));
				ub.setTransforstatus(rs.getInt("transforstatus"));
				ubli.add(ub);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return ubli;
	}
	public static void InsertGetGoodsCode(clsGetGoodsCode ce) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="insert into getgoodscode (gmt_payment,partner,buyer_email,trade_no,total_fee,gmt_create,subject,"
				+ "trade_status,machineid,goodsid,transfor_status)"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		Connection conn=ConnManager.getConnection(CN);
		try {
			int i=1;
			ps=conn.prepareStatement(sql);
			ps.setString(i++, ce.getGmtpayment());
			ps.setString(i++, ce.getPartner());
			ps.setString(i++, ce.getBuyeremail());
			ps.setString(i++, ce.getTradeno());
			
			ps.setString(i++, ce.getTotalfee());
			ps.setString(i++, ToolBox.getYMDHMS(new Timestamp(ClsTime.SystemTime())));
			ps.setString(i++, ce.getSubject());
			ps.setString(i++, ce.getTradestatus());
			ps.setString(i++, ce.getMachineid());
			ps.setString(i++, ce.getGoodsid());
			
			ps.setInt(i++, ce.getTransforstatus());
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}
	public static void updateGetGoodsCode(int id,int goodsid,String total_fee) 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="UPDATE getgoodscode SET goodsid=?,total_fee=?"
				+ "WHERE id=?";
		Connection conn=ConnManager.getConnection(CN);
		
		try {
			int i=1;
			ps=conn.prepareStatement(sql);
			ps.setInt(i++, goodsid);
			ps.setString(i++, total_fee);
			ps.setInt(i++, id);

			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		
	}
	public static int getGetGoodsCodeCount() 
	{
		
		int count=0;
		ResultSet rs=null;
		Statement st=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql=null;

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("SELECT count(id) as c FROM getgoodscode ");
			sql=stringBuilder.toString();
		try {
			st=conn.createStatement();
			rs=st.executeQuery(sql);

			if(rs.next())
			{
				count=rs.getInt("c");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,st);
		}
		return count;
	}
	public static ArrayList<clsGetGoodsCode> getGetGoodsCodeLst(int page,int count_per_page) 
	{
		
		ArrayList<clsGetGoodsCode> li=new ArrayList<clsGetGoodsCode>();
		ResultSet rs=null;
		Statement st=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql=null;

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("SELECT top("+count_per_page+") id,gmt_payment,partner,buyer_email,inneridname,trade_no,total_fee,gmt_create,subject,"
				+ "trade_status,machineid,goodsid,transfor_status FROM GetGoodsCode where id not in (SELECT top("+((page-1)*count_per_page)+") id FROM GetGoodsCode order by id desc) order by id desc");
			sql=stringBuilder.toString();
			//System.out.println(sql);
			
		try {
			st=conn.createStatement();
			rs=st.executeQuery(sql);
			clsGetGoodsCode ub;
			while(rs.next())
			{
				ub=new clsGetGoodsCode();
				ub.setId(rs.getInt("id"));
				ub.setBuyeremail(rs.getString("buyer_email"));
				ub.setGmtcreate(rs.getString("gmt_create"));
				ub.setGmtpayment(rs.getString("gmt_payment"));
				ub.setGoodsid(rs.getString("goodsid"));
				ub.setInneridname(rs.getString("inneridname"));
				ub.setMachineid(rs.getString("machineid"));
				ub.setPartner(rs.getString("partner"));
				ub.setSubject(rs.getString("subject"));
				ub.setTradeno(rs.getString("trade_no"));
				ub.setTradestatus(rs.getString("trade_status"));
				ub.setTransforstatus(rs.getInt("transfor_status"));
				ub.setTotalfee(rs.getString("total_fee"));
				li.add(ub);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,st);
		}
		return li;
	}
	
	public static PortBean getPortBeanByGoods(int mid, int goodsid) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		PortBean temp=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql="SELECT top 1 machineid,innerid,Inneridname,id,errorinfo,iserror," +
					" updatetime, goodroadname, price, amount, lastErrorTime, huodongid,capacity,goodsid,deviceid,PauseFlg" +
					" FROM goodroadinfo WHERE machineid=? and goodsid=? and iserror=0 " +
					"order by machineid,innerid asc";
		try {
			
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++, mid);
			ps.setInt(i++, goodsid);
			//ps.setInt(i++, err);
			rs=ps.executeQuery();
			if(rs.next())
			{
				temp=new PortBean();
				temp.setMachineid(rs.getInt("machineid"));
				temp.setInnerid(rs.getInt("innerid"));
				temp.setInneridname(rs.getString("Inneridname"));
				temp.setId(rs.getInt("id"));
				temp.setErrorinfo(rs.getString("errorinfo"));
				temp.setError_id(rs.getInt("iserror"));
				temp.setUpdatetime(rs.getTimestamp("updatetime"));
				temp.setGoodroadname(rs.getString("goodroadname"));
				temp.setPrice(rs.getInt("price"));
				temp.setAmount(rs.getInt("amount"));
				temp.setLastErrorTime(rs.getTimestamp("lastErrorTime"));
				temp.setHuodongid(rs.getInt("huodongid"));
				temp.setCapacity(rs.getInt("capacity"));
				temp.setGoodsid(rs.getInt("goodsid"));
				temp.setDeviceid(rs.getInt("deviceid"));
				temp.setPauseFlg(rs.getInt("PauseFlg"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return temp;

	}

	public static clsGetGoodsCode getGoodsCodeById(int id) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		clsGetGoodsCode ub=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql="SELECT * FROM GetGoodsCode WHERE id=?";
		try {
			
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++, id);
			//ps.setInt(i++, err);
			rs=ps.executeQuery();
			if(rs.next())
			{
				ub=new clsGetGoodsCode();
				ub.setId(rs.getInt("id"));
				ub.setBuyeremail(rs.getString("buyer_email"));
				ub.setGmtcreate(rs.getString("gmt_create"));
				ub.setGmtpayment(rs.getString("gmt_payment"));
				ub.setGoodsid(rs.getString("goodsid"));
				ub.setInneridname(rs.getString("inneridname"));
				ub.setMachineid(rs.getString("machineid"));
				ub.setPartner(rs.getString("partner"));
				ub.setSubject(rs.getString("subject"));
				ub.setTradeno(rs.getString("trade_no"));
				ub.setTradestatus(rs.getString("trade_status"));
				ub.setTransforstatus(rs.getInt("transfor_status"));
				ub.setTotalfee(rs.getString("total_fee"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return ub;

	}
	public static void deleteGetGoodsCode(int id) {
		String sql="delete from GetGoodsCode where id="+id;
		exec(sql);
	}
	

	public static void addGoodsPortByLst(ArrayList<PortBean> lst) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="INSERT INTO [goodroadinfo] ([machineid],[innerid],[amount],[price],[capacity]" +
				",[goodroadname],[updatetime],[iserror],[errorinfo],[lastErrorTime],[goodsid]" +
				",[DeviceId],[inneridname]) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		if(lst==null)
		{
			return;
		}
		Connection conn=ConnManager.getConnection(CN);
		
		try {
			ps=conn.prepareStatement(sql);
			for(PortBean pb :lst)
			{
				
				int i=1;
				ps.setInt(i++,pb.getMachineid());
				ps.setInt(i++,pb.getInnerid());
				ps.setInt(i++, 0);
				ps.setInt(i++, 100);
				ps.setInt(i++,0);
				ps.setString(i++, "货道"+pb.getInnerid());
				ps.setDate(i++, new Date(ClsTime.SystemTime()));
				ps.setInt(i++, 0);
				ps.setString(i++,"");
				ps.setDate(i++, new Date(ClsTime.SystemTime()));
				ps.setInt(i++, 0);
				ps.setInt(i++, 1);
				ps.setString(i++, pb.getInneridname());//String.format(clsConst.SLOT_FORMAT,sid)
				ps.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}

	public static void deleteVender(int vid) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="delete from TerminalInfo where id=?";
		
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			ps.setInt(1, vid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}

	public static void deletePort(int vid) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="delete from goodroadinfo where machineid=?";
		
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			ps.setInt(1, vid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}	
	
	public static ClsSaleStatisticData getSalesStatisticDataFromDb(Date sdate,Date edate,String vid,int typeid,int jiesuan)
	{
		ClsSaleStatisticData obj=new ClsSaleStatisticData();
		PreparedStatement ps=null;
		ResultSet rs=null;
		int i=1;
		String sql=null;
		String str_jiesuan="";
		if(jiesuan==0)
		{
		}
		else if(jiesuan==1)
		{
			str_jiesuan=" and has_jiesuan=1";
		}
		else if(jiesuan==2)
		{
			str_jiesuan=" and has_jiesuan=0";
		}
		
		if(vid.equals(""))
		{
			vid="-1";
		}
		
		if(typeid==-1)
		{
			sql="SELECT sum(price) AS _credit ,count(*) as _count FROM traderecordinfo WHERE "
				+ "  CONVERT(date, receivetime) >= ? and CONVERT(date, receivetime) <= ? and changestatus=1 and goodmachineid in ("+vid+")"+str_jiesuan;
		}
		else
		{
			sql="SELECT sum(price) AS _credit ,count(*) as _count FROM traderecordinfo WHERE traderecordinfo.tradetype=?"
					+ " and CONVERT(date, receivetime) >= ? and CONVERT(date, receivetime) <= ? and changestatus=1 and goodmachineid in ("+vid+")"+str_jiesuan;
		}
		//System.out.println(sql);
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			if(typeid!=-1)
			{
				ps.setInt(i++, typeid);
			}
			ps.setDate(i++, sdate);
			ps.setDate(i++, edate);
			rs=ps.executeQuery();
			if(rs.next())
			{
				obj.setM_credit(rs.getInt("_credit"));
				obj.setM_count(rs.getInt("_count"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return obj;
	}

	public static void addLiuyan(ClsLiuyan cly) 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		if(cly==null)
		{
			return;
		}
		String sql="INSERT INTO [liuyan] ([admin_id],[op_datetime],[op_content],[mobiletel]" +
				",[username],[realname]) VALUES(?,?,?,?,?,?)";
		
		Connection conn=ConnManager.getConnection(CN);
		
		try {
			ps=conn.prepareStatement(sql);
				int i=1;
				ps.setInt(i++,0);
				ps.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
				ps.setString(i++, cly.getContent());
				ps.setString(i++, cly.getMobiletel());
				ps.setString(i++, cly.getUsername());
				ps.setString(i++, cly.getRealname());
				
				ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}



	public static void AddRefundRecordToDb(RefundBean refundBean) {
		//INSERT INTO [zhouwenjing].[dbo].[refundtab] ([id], [orderid], [trade_id], [refundtime], [terminalid], [goodsroadid], [goodsname], [ret_msg], [refundid], [issuccess], [trade_type], [op_user], [Refund_amount]) VALUES ('2', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

		if(refundBean==null)
		{
			return;
		}
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="INSERT INTO [refundtab] ([orderid], [trade_id], [refundtime], [terminalid], "
				+ "[goodsroadid], [goodsname], [ret_msg], [refundid], [issuccess], [trade_type], [op_user], [Refund_amount],groupid) "
				+ "VALUES  (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?);";
		
		Connection conn=ConnManager.getConnection(CN);
		
		try {
			ps=conn.prepareStatement(sql);
				int i=1;
				ps.setString(i++,refundBean.getOrderid());
				ps.setInt(i++,refundBean.getTrade_id());
				ps.setTimestamp(i++, new Timestamp(refundBean.getRefundtime().getTime()));
				ps.setInt(i++, refundBean.getTerminalid());
				ps.setInt(i++,refundBean.getGoodsroadid());
				ps.setString(i++,refundBean.getGoodsname());
				ps.setString(i++, refundBean.getRet_msg());
				
				ps.setString(i++, refundBean.getRefundid());
				ps.setInt(i++,refundBean.getIssuccess());
				
				ps.setInt(i++,refundBean.getTrade_type());
				
				ps.setString(i++, refundBean.getOp_user());
				ps.setInt(i++, refundBean.getRefund_amount());
				ps.setInt(i++, refundBean.getGroupid());
				ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}

	
	public static ArrayList<RefundBean> getRefundBeanLst(String venderLimite,int groupid) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		ArrayList<RefundBean> list=new ArrayList<RefundBean>();

		String strSql="SELECT id,orderid,trade_id,refundtime,terminalid,goodsroadid,goodsname,"
				+ "ret_msg,refundid,issuccess,trade_type,op_user,Refund_amount,groupid FROM	refundtab "
				+ " where terminalid in ("+venderLimite+") and groupid=?";
		System.out.println(strSql+groupid);
		Connection conn=ConnManager.getConnection(CN);
		RefundBean refundBean =null;
		try {
			int i=1;
			ps=conn.prepareStatement(strSql);
			ps.setInt(i++, groupid);
			rs = ps.executeQuery();
			while(rs.next())
			{
				refundBean=new RefundBean();
				refundBean.setGoodsname(rs.getString("goodsname"));
				refundBean.setGoodsroadid(rs.getInt("goodsroadid"));
				refundBean.setGroupid(rs.getInt("groupid"));
				refundBean.setId(rs.getInt("id"));
				refundBean.setIssuccess(rs.getInt("issuccess"));
				refundBean.setOp_user(rs.getString("op_user"));
				
				refundBean.setOrderid(rs.getString("orderid"));
				refundBean.setRefund_amount(rs.getInt("Refund_amount"));
				refundBean.setRefundid(rs.getString("refundid"));
				refundBean.setRefundtime(rs.getTimestamp("refundtime"));
				refundBean.setRet_msg(rs.getString("ret_msg"));
				refundBean.setTerminalid(rs.getInt("terminalid"));
				
				refundBean.setTrade_id(rs.getInt("trade_id"));
				refundBean.setTrade_type(rs.getInt("trade_type"));
				list.add(refundBean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		

		return list;
		
		
	}
	
	/**
	 * 获取需要出货的交易记录
	 * @param machineid 售货机编号
	 * @param sendstatus 是否已经出货,0表示没有出货，1表示已经出货
	 * @param changestatus 是否已经支付,0表示没有支付，1表示已经支付
	 * @return
	 */
	public static TradeBean getSingleTrade(int machineid,int sendstatus,int changestatus)
	{
		return getSingleTrade(machineid,sendstatus,changestatus,"traderecordinfo");
	}

	public static TradeBean getSingleTradeFromTemp(int machineid,int sendstatus,int changestatus)
	{
		return getSingleTrade(machineid,sendstatus,changestatus,"traderecordinfo_tem");
	}
	public static TradeBean getSingleTrade(int machineid,int sendstatus,int changestatus,String tablename) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		TradeBean tb=null;
		
		Connection conn=ConnManager.getConnection(CN);
		String sql="SELECT top 1 id,liushuiid,cardinfo,orderid,price,mobilephone,tradetype,goodmachineid,"
				+ "goodroadid,changestatus,sendstatus,status,receivetime,xmlstr,goodsname,"
				+ "changes,coin_credit,bill_credit,inneridname,tradeid,has_jiesuan,has_feeback FROM "+tablename+" "
				+ "where goodmachineid=? and sendstatus=? and changestatus=? and status=0 and receivetime>?";
		int i=1;
		try {
			ps=conn.prepareStatement(sql);
			ps.setInt(i++,machineid);
			ps.setInt(i++,sendstatus);
			ps.setInt(i++,changestatus);
			ps.setTimestamp(i++,new Timestamp(System.currentTimeMillis()-60*2*1000));
			rs=ps.executeQuery();
			if(rs.next())
			{
				tb=new TradeBean();
				tb.setId(rs.getInt("id"));
				tb.setLiushuiid(rs.getString("liushuiid"));
				
				tb.setCardinfo(rs.getString("cardinfo"));
				
				tb.setOrderid(rs.getString("orderid"));
				tb.setPrice(rs.getInt("price"));
				tb.setMobilephone(rs.getString("mobilephone"));
				tb.setTradetype(rs.getInt("tradetype"));
				
				tb.setGoodmachineid(rs.getInt("goodmachineid"));
				tb.setGoodroadid(rs.getInt("goodroadid"));
				tb.setChangestatus(rs.getInt("changestatus"));
				tb.setSendstatus(rs.getInt("sendstatus"));
				tb.setStatus(rs.getInt("status"));
				tb.setReceivetime(rs.getTimestamp("receivetime"));
				tb.setXmlstr(rs.getString("xmlstr"));
				tb.setGoodsName(rs.getString("goodsname"));
				tb.setChanges(rs.getInt("changes"));
				tb.setCoin_credit(rs.getInt("coin_credit"));
				tb.setBill_credit(rs.getInt("bill_credit"));
				tb.setInneridname(rs.getString("inneridname"));
				tb.setTradeid(rs.getString("tradeid"));
				tb.setHas_feeback(rs.getInt("has_feeback"));
				tb.setHas_jiesuan(rs.getInt("has_jiesuan"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return tb;
	}
	
	public static void insertWxLog(int group_id, String str, String xmlstr, String res) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="INSERT INTO [WxLog] ( [gmt_create], [querystring], [groupid], [notifyXML],[response]) "
				+ "VALUES ( ?, ?, ?, ?,?)";
		Connection conn=ConnManager.getConnection(CN);
		
		try {
			int i=1;
			ps=conn.prepareStatement(sql);
			ps.setString(i++, ToolBox.getDateTimeString());
			ps.setString(i++, str);
			
			ps.setInt(i++, group_id);

			ps.setString(i++,xmlstr);
			ps.setString(i++,res);
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}

	public static void SetTradeJiesuan(Date sdate, Date edate, String vid) {
		
		String sql="update traderecordinfo set has_jiesuan=1 WHERE  receivetime BETWEEN ? and ? and goodmachineid in ("+vid+")";
		PreparedStatement ps=null;
		ResultSet rs=null;
		Connection conn=ConnManager.getConnection(CN);
		
		try {
			int i=1;
			ps=conn.prepareStatement(sql);
			ps.setDate(i++, sdate);
			ps.setDate(i++, edate);

			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}

	public static void DeleteFromTemp(TradeBean tradeBean) {
		
		exec("delete from [traderecordinfo_tem] where orderid='"+ tradeBean.getOrderid() +"'");
	}



	public static void AddTransforLog(WxCoporTransfor wxCoporTransfor) 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql="INSERT INTO [WxTransfor] ([re_user_name], [amount], [desc],"
				+ " [groupid], [err_code], [rescode], [err_code_des], [payment_no], [payment_time], "
				+ "[device_info], [nonce_str], [partner_trade_no], [openid], [check_name],adminid) VALUES "
				+ "( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
		
		try {
			int i=1;
			
			ps=conn.prepareStatement(sql);
			ps.setString(i++,wxCoporTransfor.getRe_user_name());
			ps.setString(i++,wxCoporTransfor.getAmount());
			ps.setString(i++,wxCoporTransfor.getDesc());
			
			clsGroupBean g=wxCoporTransfor.getGroupBean();
			if(g!=null)
			{
				ps.setInt(i++,g.getId());
			}
			
			ps.setString(i++,wxCoporTransfor.getErr_code());
			ps.setString(i++,wxCoporTransfor.getRescode());
			ps.setString(i++,wxCoporTransfor.getErr_code_des());
			ps.setString(i++,wxCoporTransfor.getPayment_no());
			ps.setString(i++,wxCoporTransfor.getPayment_time());
			
			ps.setString(i++,wxCoporTransfor.getDevice_info());
			ps.setString(i++,wxCoporTransfor.getNonce_str());
			ps.setString(i++,wxCoporTransfor.getPartner_trade_no());
			ps.setString(i++,wxCoporTransfor.getOpenid());
			ps.setString(i++,wxCoporTransfor.getCheck_name());
			ps.setInt(i++,wxCoporTransfor.getAdminid());
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		
	}
	


}