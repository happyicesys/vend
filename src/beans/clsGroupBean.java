package beans;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.connectionpool.DBConnectionManager;

public class clsGroupBean {
	private int id;
	private String groupname;
	private String groupdes;
	private Timestamp creattime;


	private String WelcomeMessage;
	private String notify_url;

	private String wx_notify_url;
	private String wx_appid;
	private String wx_mch_id;
	private String wx_key;
	
	private String Appsecret;
	private int min_mid;
	private int max_mid;
	/**
	 * 下一次获取access_token的时间，单位是S
	 */
	private int Next_access_token_time;

	private String Access_token;
	
	private String wx_pay_type;
	private String ALIPAY_PUBLIC_KEY;
	private String al_SIGN_TYPE;
	private String al_PARTNER;
	private String al_APP_ID;
	private String al_PRIVATE_KEY;
	private String al_PUBLIC_KEY;
	private String ServerIp;	
	
	private Timestamp updatetime;
	private String adminusername;
	
	private int autoTransfer;
	
	
	public int getAutoTransfer() {
		return autoTransfer;
	}
	public void setAutoTransfer(int autoTransfer) {
		this.autoTransfer = autoTransfer;
	}
	public Timestamp getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}
	public String getAdminusername() {
		return adminusername;
	}
	public void setAdminusername(String adminusername) {
		this.adminusername = adminusername;
	}
	public String getWx_notify_url() {
		return wx_notify_url;
	}
	public void setWx_notify_url(String wx_notify_url) {
		this.wx_notify_url = wx_notify_url;
	}
	public String getWx_appid() {
		return wx_appid;
	}
	public void setWx_appid(String wx_appid) {
		this.wx_appid = wx_appid;
	}
	public String getWx_mch_id() {
		return wx_mch_id;
	}
	public void setWx_mch_id(String wx_mch_id) {
		this.wx_mch_id = wx_mch_id;
	}
	public String getWx_key() {
		return wx_key;
	}
	public void setWx_key(String wx_key) {
		this.wx_key = wx_key;
	}
	public String getWx_pay_type() {
		return wx_pay_type;
	}
	public void setWx_pay_type(String wx_pay_type) {
		this.wx_pay_type = wx_pay_type;
	}
	public String getALIPAY_PUBLIC_KEY() {
		return ALIPAY_PUBLIC_KEY;
	}
	public void setALIPAY_PUBLIC_KEY(String aLIPAY_PUBLIC_KEY) {
		ALIPAY_PUBLIC_KEY = aLIPAY_PUBLIC_KEY;
	}
	public String getAl_SIGN_TYPE() {
		return al_SIGN_TYPE;
	}
	public void setAl_SIGN_TYPE(String al_SIGN_TYPE) {
		this.al_SIGN_TYPE = al_SIGN_TYPE;
	}
	public String getAl_PARTNER() {
		return al_PARTNER;
	}
	public void setAl_PARTNER(String al_PARTNER) {
		this.al_PARTNER = al_PARTNER;
	}
	public String getAl_APP_ID() {
		return al_APP_ID;
	}
	public void setAl_APP_ID(String al_APP_ID) {
		this.al_APP_ID = al_APP_ID;
	}
	public String getAl_PRIVATE_KEY() {
		return al_PRIVATE_KEY;
	}
	public void setAl_PRIVATE_KEY(String al_PRIVATE_KEY) {
		this.al_PRIVATE_KEY = al_PRIVATE_KEY;
	}
	public String getAl_PUBLIC_KEY() {
		return al_PUBLIC_KEY;
	}
	public void setAl_PUBLIC_KEY(String al_PUBLIC_KEY) {
		this.al_PUBLIC_KEY = al_PUBLIC_KEY;
	}
	public String getServerIp() {
		return ServerIp;
	}
	public void setServerIp(String serverIp) {
		ServerIp = serverIp;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public String getGroupdes() {
		return groupdes;
	}
	public void setGroupdes(String groupdes) {
		this.groupdes = groupdes;
	}
	public Timestamp getCreattime() {
		return creattime;
	}
	public void setCreattime(Timestamp creattime) {
		this.creattime = creattime;
	}


	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getWelcomeMessage() {
		return WelcomeMessage;
	}
	public void setWelcomeMessage(String welcomeMessage) {
		WelcomeMessage = welcomeMessage;
	}
	public int getNext_access_token_time() {
		return Next_access_token_time;
	}
	public void setNext_access_token_time(int next_access_token_time) {
		Next_access_token_time = next_access_token_time;
	}
	public String getAccess_token() {
		return Access_token;
	}
	public void setAccess_token(String access_token) {
		Access_token = access_token;
	}
	public String getAppsecret() {
		return Appsecret;
	}
	public void setAppsecret(String appsecret) {
		Appsecret = appsecret;
	}

	
	private static DBConnectionManager ConnManager=DBConnectionManager.getInstance();
	private final static String CN="BlueShine";
	public final static String CMPPDB="CMPPDB";	
	public static void InsertclsGroupBean(clsGroupBean groupBean) 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
				String sql="INSERT INTO [grouptab] ([groupname], [groupdes], [createtime], "
						+ "[WelcomeMessage], [notify_url], [wx_notify_url], [wx_appid], "
						+ "[wx_mch_id], [wx_key], [wx_pay_type],[ALIPAY_PUBLIC_KEY], [al_SIGN_TYPE],"
						+ " [al_PARTNER], [al_APP_ID], [al_PRIVATE_KEY], [al_PUBLIC_KEY], [ServerIp],"
						+ " [updatetime], [adminusername],[Appsecret],[min_mid],[max_mid],[autoTransfer])"
						+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				Connection conn=ConnManager.getConnection(CN);
				try {
					int i=1;
					ps=conn.prepareStatement(sql);
					ps.setString(i++, groupBean.getGroupname());
					ps.setString(i++, groupBean.getGroupdes());
					ps.setTimestamp(i++, groupBean.getCreattime());
					ps.setString(i++, groupBean.getWelcomeMessage());
					ps.setString(i++, groupBean.getNotify_url());
					ps.setString(i++, groupBean.getWx_notify_url());
					ps.setString(i++, groupBean.getWx_appid());
					ps.setString(i++, groupBean.getWx_mch_id());
					ps.setString(i++, groupBean.getWx_key());
					ps.setString(i++, groupBean.getWx_pay_type());
					ps.setString(i++, groupBean.getALIPAY_PUBLIC_KEY());
					ps.setString(i++, groupBean.getAl_SIGN_TYPE());
					ps.setString(i++, groupBean.getAl_PARTNER());
					ps.setString(i++, groupBean.getAl_APP_ID());
					ps.setString(i++, groupBean.getAl_PRIVATE_KEY());
					ps.setString(i++, groupBean.getAl_PUBLIC_KEY());
					ps.setString(i++, groupBean.getServerIp());
					ps.setTimestamp(i++, groupBean.getUpdatetime());
					ps.setString(i++, groupBean.getAdminusername());
					ps.setString(i++, groupBean.getAppsecret());
					ps.setInt(i++, groupBean.getMin_mid());
					ps.setInt(i++, groupBean.getMax_mid());
					ps.setInt(i++, groupBean.getAutoTransfer());
					ps.executeUpdate();

				} catch (Exception e) {
					e.printStackTrace();
				}
				finally
				{
					ConnManager.freeConnection(CN, conn,rs,ps);
				}
	}
	

	public static ArrayList<clsGroupBean> getGroupLst() 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		ArrayList<clsGroupBean> glst=new ArrayList<clsGroupBean>();
		String sqlString="SELECT id,groupname,groupdes,createtime,WelcomeMessage,notify_url,"
				+ "wx_notify_url,wx_appid,wx_mch_id,wx_key,wx_pay_type,"
				+ "ALIPAY_PUBLIC_KEY,al_SIGN_TYPE,al_PARTNER,al_APP_ID,al_PRIVATE_KEY,al_PUBLIC_KEY,"
				+ "ServerIp,updatetime,adminusername,Appsecret,min_mid,max_mid,autoTransfer FROM grouptab order by id asc";
		Connection conn=ConnManager.getConnection(CN);
		
		clsGroupBean gbBean=null;
		try {
			ps=conn.prepareStatement(sqlString);rs=ps.executeQuery();
			while (rs.next()) 
			{
				gbBean=new clsGroupBean();
				gbBean.setId(rs.getInt("id"));
				gbBean.setGroupname(rs.getString("groupname"));	
				gbBean.setGroupdes(rs.getString("groupdes"));				
				gbBean.setCreattime(rs.getTimestamp("createtime"));

				gbBean.setWelcomeMessage(rs.getString("WelcomeMessage"));
				gbBean.setNotify_url(rs.getString("notify_url"));
				
				gbBean.setWx_notify_url(rs.getString("wx_notify_url"));
				gbBean.setWx_appid(rs.getString("wx_appid"));
				gbBean.setWx_mch_id(rs.getString("wx_mch_id"));
				gbBean.setWx_key(rs.getString("wx_key"));
				gbBean.setWx_pay_type(rs.getString("wx_pay_type"));
				
				gbBean.setALIPAY_PUBLIC_KEY(rs.getString("ALIPAY_PUBLIC_KEY"));
				gbBean.setAl_SIGN_TYPE(rs.getString("al_SIGN_TYPE"));
				gbBean.setAl_PARTNER(rs.getString("al_PARTNER"));
				gbBean.setAl_APP_ID(rs.getString("al_APP_ID"));
				
				gbBean.setAl_PRIVATE_KEY(rs.getString("al_PRIVATE_KEY"));
				gbBean.setAl_PUBLIC_KEY(rs.getString("al_PUBLIC_KEY"));		
				gbBean.setServerIp(rs.getString("ServerIp"));
				
				gbBean.setUpdatetime(rs.getTimestamp("updatetime"));				
				gbBean.setAdminusername(rs.getString("adminusername"));
				gbBean.setAppsecret(rs.getString("Appsecret"));
				gbBean.setMax_mid(rs.getInt("max_mid"));
				gbBean.setMin_mid(rs.getInt("min_mid"));
				gbBean.setAutoTransfer(rs.getInt("autoTransfer"));
				glst.add(gbBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}

		return glst;
	}
	
	
	public static void UpdateclsGroupBean(clsGroupBean groupBean) 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
				String sql="UPDATE TOP(1) [grouptab] SET [groupname]=?, [groupdes]=?, [createtime]=?,"
						+ " [WelcomeMessage]=?, [notify_url]=?, [wx_notify_url]=?, [wx_appid]=?, "
						+ "[wx_mch_id]=?, [wx_key]=?, [wx_pay_type]=?, "
						+ "[ALIPAY_PUBLIC_KEY]=?, [al_SIGN_TYPE]=?, [al_PARTNER]=?, [al_APP_ID]=?, "
						+ "[al_PRIVATE_KEY]=?, [al_PUBLIC_KEY]=?, [ServerIp]=?, [updatetime]=?, [adminusername]=?,"
						+ "[Appsecret]=?,[min_mid]=?,[max_mid]=?,[autoTransfer]=? WHERE ([id]=?)";
				Connection conn=ConnManager.getConnection(CN);
				try {
					int i=1;
					ps=conn.prepareStatement(sql);
					ps.setString(i++, groupBean.getGroupname());
					ps.setString(i++, groupBean.getGroupdes());
					ps.setTimestamp(i++, groupBean.getCreattime());
					ps.setString(i++, groupBean.getWelcomeMessage());
					ps.setString(i++, groupBean.getNotify_url());
					
					ps.setString(i++, groupBean.getWx_notify_url());
					ps.setString(i++, groupBean.getWx_appid());
					
					
					ps.setString(i++, groupBean.getWx_mch_id());
					ps.setString(i++, groupBean.getWx_key());
					
					ps.setString(i++, groupBean.getWx_pay_type());
					ps.setString(i++, groupBean.getALIPAY_PUBLIC_KEY());
					
					ps.setString(i++, groupBean.getAl_SIGN_TYPE());
					ps.setString(i++, groupBean.getAl_PARTNER());
					
					ps.setString(i++, groupBean.getAl_APP_ID());
					ps.setString(i++, groupBean.getAl_PRIVATE_KEY());
					
					ps.setString(i++, groupBean.getAl_PUBLIC_KEY());
					ps.setString(i++, groupBean.getServerIp());
					
					ps.setTimestamp(i++, groupBean.getUpdatetime());
					ps.setString(i++, groupBean.getAdminusername());
					ps.setString(i++, groupBean.getAppsecret());
					ps.setInt(i++, groupBean.getMin_mid());
					ps.setInt(i++, groupBean.getMax_mid());
					ps.setInt(i++, groupBean.getAutoTransfer());
					ps.setInt(i++, groupBean.getId());
					
					ps.executeUpdate();

				} catch (Exception e) {
					e.printStackTrace();
				}
				finally
				{
					ConnManager.freeConnection(CN, conn,rs,ps);
				}
	}
	
	public static void DeleteclsGroupBean(int id) 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
				String sql="DELETE FROM [grouptab] WHERE ([id]=?)";
				Connection conn=ConnManager.getConnection(CN);
				try {
					int i=1;
					ps=conn.prepareStatement(sql);
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
	

	

	
	public static clsGroupBean getGroup(int groupid) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		clsGroupBean group=null;
		String sqlString="SELECT id,groupname,groupdes,createtime,WelcomeMessage,notify_url,"
				+ "wx_notify_url,wx_appid,wx_mch_id,wx_key,wx_pay_type,"
				+ "ALIPAY_PUBLIC_KEY,al_SIGN_TYPE,al_PARTNER,al_APP_ID,al_PRIVATE_KEY,al_PUBLIC_KEY,ServerIp,"
				+ "updatetime,adminusername,Appsecret,min_mid,max_mid,autoTransfer FROM grouptab WHERE id = ?";
		Connection conn=ConnManager.getConnection(CN);
		
		try {
			int i=1;
			ps=conn.prepareStatement(sqlString);
			ps.setInt(i++, groupid);
			rs=ps.executeQuery();
			while (rs.next()) 
			{
				group=new clsGroupBean();
				group.setCreattime(rs.getTimestamp("createtime"));
				group.setGroupdes(rs.getString("groupdes"));
				group.setGroupname(rs.getString("groupname"));
				group.setId(rs.getInt("id"));
				group.setNotify_url(rs.getString("notify_url"));
				group.setWelcomeMessage(rs.getString("WelcomeMessage"));
				group.setWx_notify_url(rs.getString("wx_notify_url"));
				group.setWx_appid(rs.getString("wx_appid"));
				group.setWx_mch_id(rs.getString("wx_mch_id"));
				group.setWx_key(rs.getString("wx_key"));
				group.setWx_pay_type(rs.getString("wx_pay_type"));
				group.setALIPAY_PUBLIC_KEY(rs.getString("ALIPAY_PUBLIC_KEY"));
				
				group.setAl_SIGN_TYPE(rs.getString("al_SIGN_TYPE"));
				group.setAl_PARTNER(rs.getString("al_PARTNER"));
				group.setAl_APP_ID(rs.getString("al_APP_ID"));
				group.setAl_PRIVATE_KEY(rs.getString("al_PRIVATE_KEY"));
				group.setAl_PUBLIC_KEY(rs.getString("al_PUBLIC_KEY"));
				group.setServerIp(rs.getString("ServerIp"));
				
				group.setUpdatetime(rs.getTimestamp("updatetime"));				
				group.setAdminusername(rs.getString("adminusername"));
				group.setAppsecret(rs.getString("Appsecret"));
				group.setMax_mid(rs.getInt("max_mid"));
				group.setMin_mid(rs.getInt("min_mid"));
				group.setAutoTransfer(rs.getInt("autoTransfer"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}

		return group;
	}
	public int getMin_mid() {
		return min_mid;
	}
	public void setMin_mid(int min_mid) {
		this.min_mid = min_mid;
	}
	public int getMax_mid() {
		return max_mid;
	}
	public void setMax_mid(int max_mid) {
		this.max_mid = max_mid;
	}
}
