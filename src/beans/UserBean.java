package beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.ClsTime;
import com.ado.SqlADO;
import com.connectionpool.DBConnectionManager;
import com.tools.ToolBox;
public class UserBean {

    public final static String[] RIGHT_DES={"允许访问系统","允许访问所有机器","允许查看机器参数","允许修改机器参数","允许添加机器",
    										"允许查看地图","允许修改货道参数","允许查看货道参数","允许查看交易记录","允许下载交易记录",
    										"允许查看交易统计","允许查看用户列表","允许修改用户数据","允许分配机器管理权","允许添加用户",
    										"允许添加商品","允许浏览商品","允许修改商品","允许上传文件","允许修改自身权限",
    										"允许退款","允许查看退款记录","允许删除用户","允许创建集团","允许修改取货码",
    										"允许添加取货码","允许绑定账号到机器","允许删除机器","允许修改集团信息","允许删除集团信息",
    										"允许查看集团信息","允许更新本集团信息","允许指定用户集团号","允许删除取货码","允许查看取货码",
    										"允许修改个人信息","允许无限期退款","允许使用结算功能","允许添加会员帐户","允许删除会员帐户",
    										"允许修改会员帐户","允许为会员帐户充值","允许批量取货","允许查看温度表","加盟商静访"};
    
	public final static int FUNID_CAN_ACCESS_WEB			=0;	
    public final static int FUNID_ACCESS_ALL_VENDER			=1;
	public final static int FUNID_CAN_VIEW_VENDER			=2;
	public final static int FUNID_CAN_UPDATE_VENDER			=3;
	public final static int FUNID_CAN_ADD_VENDER			=4;
	
	public final static int FUNID_CAN_VIEW_VENDER_MAP		=5;
	public final static int FUNID_CAN_UPDATE_PORT			=6;
	public final static int FUNID_CAN_VIEW_PORT				=7;
	public final static int FUNID_CAN_VIEW_TRADE_RECORD		=8;
	public final static int FUNID_CAN_DOWNLOAD_TRADE_RECORD	=9;
	
	public final static int FUNID_CAN_VIEW_STASTIC			=10;
	public final static int FUNID_CAN_VIEW_USER_LST			=11;
	public final static int FUNID_CAN_UPDATE_USER			=12;
	public final static int FUNID_CAN_ASIGN_VENDER			=13;
	public final static int FUNID_CAN_ADD_USER				=14;
	
	public final static int FUNID_CAN_ADD_GOODS				=15;
	public final static int FUNID_CAN_VIEW_GOODS			=16;
	public final static int FUNID_CAN_UPDATE_GOODS			=17;
	public final static int FUNID_CAN_UPLOAD_FILE			=18;
	public final static int FUNID_CAN_EDIT_RIGHT			=19;
	
	public final static int FUNID_CAN_REFUND        		=20;	
	public final static int FUNID_CAN_VIEW_REFUND_LOG		=21;	

	public final static int FUNID_CAN_DEL_USER				=22;/**/
	public final static int FUNID_CAN_CREATE_GROUP_ID		=23;
	
	public final static int FUNID_CAN_EDIT_FETCH_GOODS_CODE =24;
	public final static int FUNID_CAN_ADD_FETCH_GOODS_CODE	=25;/*允许添加取货码*/
	public final static int FUNID_CAN_ADD_BIND_AL_WX_USER	=26;/*允许绑定微信账号到机器*/
	public final static int FUNID_CAN_DELTE_VENDER			=27;/*允许绑定微信账号到机器*/
	
	public final static int FUNID_CAN_CHANGE_GROUP_ID		=28;
	public final static int FUNID_CAN_DEL_GROUP_ID			=29;
	public final static int FUNID_CAN_VIEW_GROUP_ID			=30;
	public final static int FUNID_CAN_MOD_SELF_GROUP_ID		=31;
	public final static int FUNID_CAN_SET_USER_GROUP_ID_WHEN_ADD=32;
	
	public final static int FUNID_CAN_DELETE_FETCH_GOODS_CODE=33;
	public final static int FUNID_CAN_VIEW_FETCH_GOODS_CODE=34;
	public final static int FUNID_CAN_SET_MYSELF_INFO=35;
	public final static int FUNID_CAN_PAY_BACK_NO_LIMIT_TIME=36;

	public final static int FUNID_CAN_JIESUAN=37;	
	
	public final static int FUNID_CAN_ADD_CUSTOMER=38; /*添加会员帐户*/
	public final static int FUNID_CAN_DEL_CUSTOMER=39; /*删除会员帐户*/
	public final static int FUNID_CAN_CHANGE_CUSTOMER=40; /*修改会员帐户*/
	public final static int FUNID_CAN_CHARGE_FOR_CUSTOMER=41; /*为会员帐户充值*/
	
	public final static int FUNID_CAN_SWIPE_QUHUO=42; /*为会员帐户充值*/
	public final static int FUNID_CAN_VIEW_TEMP_GRAPH=43;
	public final static int FUNID_DISABLE_FRANCHISEE=44;
	public String getRightLstString(boolean en) 
	{
		int i=0;
		StringBuilder sb=new StringBuilder();
		if(en)
		{
			for(i=0;i<RIGHT_DES.length;i++)
			{
				/*没有允许的权限不显示*/
				if(!this.AccessAble(i))
				{
					continue;
				}
				
				sb.append("<li style='float:left;width: 170px;'><label><input type=\"checkbox\" ");
				if(this.AccessAble(i))
				{
					sb.append("checked='checked'");
				}
				sb.append("value='");
				sb.append(i);
				sb.append("' name='right' />");
				sb.append(RIGHT_DES[i]);
				sb.append("</label> </li>");
			}
		}
		else
		{
			for(i=0;i<RIGHT_DES.length;i++)
			{
				/*没有允许的权限不显示*/
				if(!this.AccessAble(i))
				{
					continue;
				}
				
				sb.append("<li style='float: left;width: 170px;'><label><input type='checkbox' disabled='disabled'");

				if(this.AccessAble(i))
				{
					sb.append(" checked='checked'");
				}
				sb.append(" value='");
				sb.append(i);
				sb.append("' name='right' />");
				sb.append(RIGHT_DES[i]);
				sb.append("</label> </li>");
			}
		}
		return sb.toString();
	}

	
	
    private int id;
	private String adminusername;
	private String adminpassword;
	private String admintelephone;
	private String adminmobilephone;
	private String adminname;
	private String adminsex;
	private String adminaddress;
	
	private String adminrights;
	
	private String wx_name;
	private String al_name;
	private String wx_openid;
	
	private String OneceId;/*绑定微信公众号到用户的随机码*/
	
	private Timestamp OneceIdValidTime;/*绑定微信公众号到用户的随机码的有效时间*/
	
	
	private int VenderId[];
	public String getWx_name() {
		return wx_name;
	}
	public void setWx_name(String wx_name) {
		this.wx_name = wx_name;
	}
	public String getAl_name() {
		return al_name;
	}
	public void setAl_name(String al_name) {
		this.al_name = al_name;
	}
	public String getWx_openid() {
		return wx_openid;
	}
	public void setWx_openid(String wx_openid) {
		this.wx_openid = wx_openid;
	}


	

	private String CanAccessSellerid;
	
	private int groupid;
	
	private int type;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getAdminrights() {
		return adminrights;
	}
	public void setAdminrights(String adminrights) {
		this.adminrights = adminrights;
	}
	private String lastloginip;
	private Timestamp createtime;
	private Timestamp lastLoginTime;
	
	
	public Timestamp getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Timestamp lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAdminusername() {
		return adminusername;
	}
	public String getAdminpassword() {
		if(adminpassword==null)
		{
			return "";
		}
		return adminpassword;
	}
	public String getAdmintelephone() {
		return admintelephone;
	}
	public String getAdminmobilephone() {
		return adminmobilephone;
	}
	public String getAdminname() {
		return adminname;
	}
	public String getAdminsex() {
		return adminsex;
	}
	public String getAdminaddress() {
		return adminaddress;
	}

	public String getLastloginip() {
		return lastloginip;
	}
	public Timestamp getCreatetime() {
		return createtime;
	}
	public void setAdminusername(String adminusername) {
		if(adminusername==null)
			this.adminusername = "";
		else
			this.adminusername = adminusername;
	}
	public void setAdminpassword(String adminpassword) {
		if(null==adminpassword)
			this.adminpassword = "";
		else
			this.adminpassword = adminpassword;
	}
	public void setAdmintelephone(String admintelephone) {
		if(admintelephone==null)
			this.admintelephone = "";
		else
			this.admintelephone = admintelephone;
	}
	public void setAdminmobilephone(String adminmobilephone) {
		if(null==adminmobilephone)
			this.adminmobilephone="";
		else
			this.adminmobilephone = adminmobilephone;
	}
	public void setAdminname(String adminname) {
		if(null==adminname)
			this.adminname = "";
		else
			this.adminname = adminname;
	}
	public void setAdminsex(String adminsex) {
		if(null==adminsex)
			this.adminsex = "男";
		else
			this.adminsex = adminsex;
	}
	public void setAdminaddress(String adminaddress) {
		if(adminaddress==null)
		{
			this.adminaddress = "";
		}
		else
			this.adminaddress = adminaddress;
	}
	public void setLastloginip(String lastloginip) {
		if(lastloginip==null)
			this.lastloginip = "0.0.0.0";
		else
			this.lastloginip = lastloginip;
	}
	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}
	
	public boolean CanAccessSeller(int sellerid)
	{
		return CanManageThisVender(sellerid);
	}
	
	public String getVenderLimite()
	{
		
		if(CanAccessSellerid==null)
		{
			return "-1";
		}
		else
		{
			if(CanAccessSellerid.trim().equals(""))
			{
				return "-1";
			}
			return CanAccessSellerid;
		}
	}
	public int[] getVenderId() 
	{
		return VenderId;
	}
	public void setVenderId(int[] venderId) 
	{
		VenderId = venderId;
	}
	public void setCanAccessSellerid(String canAccessSellerid) 
	{
		if(canAccessSellerid==null)
		{
			CanAccessSellerid="";
		}
		else
		{
		    int i;
		    String canAccessVender="";
		    
		    String[] stra=canAccessSellerid.split(",",0);
		    if(stra!=null)
		    {
			    for (i=0;i<stra.length-1;i++) {
			    	
			    	if(ToolBox.isInt(stra[i]))
			    	{
			    		canAccessVender+=stra[i]+",";
			    	}
			    	
				}
		    	if(ToolBox.isInt(stra[i]))
		    	{
		    		canAccessVender+=stra[i];
		    	}
		    	  	
		    }
		    CanAccessSellerid=canAccessVender;
		}
	}
	public String getCanAccessSellerid() 
	{
		return CanAccessSellerid;
	}
	
	
	public int getPagecount() {
		return 300;
	}
	
	
	 public boolean AccessAble(int funid)
	 {
		 String sign=adminrights.substring(funid, funid+1);
		 return (sign.equals("1"));
	 }
	public boolean CanManageThisVender(int sellerId) 
	{
		if(AccessAble(FUNID_ACCESS_ALL_VENDER))
		{
			return true;
		}
		else
		{
			int[] int_arr=ToolBox.StringToIntArray(CanAccessSellerid);
			if(int_arr!=null)
			{
				for (int i : int_arr) 
				{
					if(sellerId==i)
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	public int getGroupid() {
		return groupid;
	}
	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}
	private static DBConnectionManager ConnManager=DBConnectionManager.getInstance();
	private final static String CN="BlueShine";
	public final static String CMPPDB="CMPPDB";	
	public static UserBean getUserByWxOpenId(String user_openid) {
		UserBean ub=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		//Statement stmt = null;
		Connection conn=ConnManager.getConnection(CN);
		String sql="select top 1 id,adminusername,admintelephone,adminmobilephone,adminname," +
				"adminsex,adminaddress,adminrights,lastloginip,createtime," +
				"lastLoginTime,type,CanAccessSeller,groupid,adminpassword,[wx_name],[al_name],[wx_openid],[OneceId],"
				+ " OneceIdValidTime from admininfo " +
				"where wx_openid=?";
		try {
			ps=conn.prepareStatement(sql);
			ps.setString(1,user_openid);
			rs=ps.executeQuery();
			if(rs.next())
			{
				ub=new UserBean();
				ub.setAdminusername(rs.getString("adminusername"));
				ub.setId(rs.getInt("id"));
				ub.setAdmintelephone(rs.getString("admintelephone"));
				ub.setAdminmobilephone(rs.getString("adminmobilephone"));
				ub.setAdminname(rs.getString("adminname"));
				ub.setAdminsex(rs.getString("adminsex"));
				ub.setAdminaddress(rs.getString("adminaddress"));
				ub.setAdminrights(rs.getString("adminrights"));
				ub.setLastloginip(rs.getString("lastloginip"));
				ub.setCreatetime(rs.getTimestamp("createtime"));
				ub.setLastLoginTime(rs.getTimestamp("lastLoginTime"));
				ub.setType(rs.getInt("type"));
				ub.setCanAccessSellerid(rs.getString("CanAccessSeller"));
				ub.setAdminpassword(rs.getString("adminpassword"));
				ub.setGroupid(rs.getInt("groupid"));
				ub.setWx_name(rs.getString("wx_name"));
				ub.setWx_openid(rs.getString("wx_openid"));
				ub.setAl_name(rs.getString("al_name"));
				ub.setOneceId(rs.getString("OneceId"));
				ub.setOneceIdValidTime(rs.getTimestamp("OneceIdValidTime"));
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
	
	
	
	public static UserBean getUserBean(String username, String pwd) {
		UserBean ub=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		//Statement stmt = null;
		Connection conn=ConnManager.getConnection(CN);
		String sql="select top 1 id,adminusername,admintelephone,adminmobilephone,adminname," +
				"adminsex,adminaddress,adminrights,lastloginip,createtime," +
				"lastLoginTime,type,CanAccessSeller,groupid,adminpassword,[wx_name],[al_name],[wx_openid] from admininfo  " +
				"where adminusername=? and adminpassword=?";
		try {
		
			ps=conn.prepareStatement(sql);
			ps.setString(1,username);
			ps.setString(2,pwd);
			rs=ps.executeQuery();
		if(rs.next())
		{
			ub=new UserBean();
			ub.setAdminusername(rs.getString("adminusername"));
			ub.setId(rs.getInt("id"));
			ub.setAdmintelephone(rs.getString("admintelephone"));
			ub.setAdminmobilephone(rs.getString("adminmobilephone"));
			ub.setAdminname(rs.getString("adminname"));
			ub.setAdminsex(rs.getString("adminsex"));
			ub.setAdminaddress(rs.getString("adminaddress"));
			ub.setAdminrights(rs.getString("adminrights"));
			ub.setLastloginip(rs.getString("lastloginip"));
			ub.setCreatetime(rs.getTimestamp("createtime"));
			ub.setLastLoginTime(rs.getTimestamp("lastLoginTime"));
			ub.setType(rs.getInt("type"));
			ub.setCanAccessSellerid(rs.getString("CanAccessSeller"));
			ub.setAdminpassword(rs.getString("adminpassword"));
			ub.setGroupid(rs.getInt("groupid"));
			ub.setWx_name(rs.getString("wx_name"));
			ub.setWx_openid(rs.getString("wx_openid"));
			ub.setAl_name(rs.getString("al_name"));
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
	

	public static UserBean getUserBean(String username) {
		UserBean ub=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		//Statement stmt = null;
		Connection conn=ConnManager.getConnection(CN);
		String sql="select top 1 id,adminusername,admintelephone,adminmobilephone,adminname," +
				"adminsex,adminaddress,adminrights,lastloginip,createtime," +
				"lastLoginTime,type,CanAccessSeller,groupid,adminpassword,[wx_name],[al_name],[wx_openid],[OneceId],"
				+ " OneceIdValidTime from admininfo " +
				"where adminusername=?";
		try {
		
			ps=conn.prepareStatement(sql);
			ps.setString(1,username);
			rs=ps.executeQuery();
			if(rs.next())
			{
				ub=new UserBean();
				ub.setAdminusername(rs.getString("adminusername"));
				ub.setId(rs.getInt("id"));
				ub.setAdmintelephone(rs.getString("admintelephone"));
				ub.setAdminmobilephone(rs.getString("adminmobilephone"));
				ub.setAdminname(rs.getString("adminname"));
				ub.setAdminsex(rs.getString("adminsex"));
				ub.setAdminaddress(rs.getString("adminaddress"));
				ub.setAdminrights(rs.getString("adminrights"));
				ub.setLastloginip(rs.getString("lastloginip"));
				ub.setCreatetime(rs.getTimestamp("createtime"));
				ub.setLastLoginTime(rs.getTimestamp("lastLoginTime"));
				ub.setType(rs.getInt("type"));
				ub.setCanAccessSellerid(rs.getString("CanAccessSeller"));
				ub.setAdminpassword(rs.getString("adminpassword"));
				ub.setGroupid(rs.getInt("groupid"));
				ub.setWx_name(rs.getString("wx_name"));
				ub.setWx_openid(rs.getString("wx_openid"));
				ub.setAl_name(rs.getString("al_name"));
				ub.setOneceId(rs.getString("OneceId"));
				ub.setOneceIdValidTime(rs.getTimestamp("OneceIdValidTime"));
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
	
	
	public static UserBean getUserBeanById(int id) {
		UserBean ub=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql="select top 1 adminusername,id,admintelephone,adminmobilephone,adminname," +
				"adminsex,adminaddress,adminrights,lastloginip,createtime,lastLoginTime," +
				"type,CanAccessSeller,adminpassword,groupid,adminpassword,[wx_name],[al_name],[wx_openid] from admininfo where id="+id;
		try {
			ps=conn.prepareStatement(sql);rs=ps.executeQuery();
			if(rs.next())
			{
				ub=new UserBean();
				ub.setAdminusername(rs.getString("adminusername"));
				ub.setId(rs.getInt("id"));
				ub.setAdmintelephone(rs.getString("admintelephone"));
				ub.setAdminmobilephone(rs.getString("adminmobilephone"));
				ub.setAdminname(rs.getString("adminname"));
				ub.setAdminsex(rs.getString("adminsex"));
				ub.setAdminaddress(rs.getString("adminaddress"));
				ub.setAdminrights(rs.getString("adminrights"));
				ub.setLastloginip(rs.getString("lastloginip"));
				ub.setCreatetime(rs.getTimestamp("createtime"));
				ub.setLastLoginTime(rs.getTimestamp("lastLoginTime"));
				ub.setType(rs.getInt("type"));
				ub.setCanAccessSellerid(rs.getString("CanAccessSeller"));
				ub.setAdminpassword(rs.getString("adminpassword"));
				ub.setGroupid(rs.getInt("groupid"));
				ub.setWx_name(rs.getString("wx_name"));
				ub.setWx_openid(rs.getString("wx_openid"));
				ub.setAl_name(rs.getString("al_name"));
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
	
	public static ArrayList<UserBean> getUserBeanListByright(int groupid)
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		int i=1;
		ArrayList<UserBean> ubli=new ArrayList<UserBean>(5);
		Connection conn=ConnManager.getConnection(CN);
		String sql="select adminusername,id,admintelephone,adminmobilephone,adminname," +
				"adminsex,adminaddress,adminrights,wx_name,al_name,wx_openid," +
				"lastloginip,createtime,lastLoginTime,type,groupid,[wx_name],[al_name],[wx_openid] from admininfo ";
			sql+="  where groupid=? order by id";
		try {
			ps=conn.prepareStatement(sql);
			ps.setInt(i++, groupid);
			rs=ps.executeQuery();
			while(rs.next())
			{
				UserBean ub=new UserBean();
				ub.setAdminusername(rs.getString("adminusername"));
				ub.setId(rs.getInt("id"));
				ub.setAdmintelephone(rs.getString("admintelephone"));
				ub.setAdminmobilephone(rs.getString("adminmobilephone"));
				ub.setAdminname(rs.getString("adminname"));
				ub.setAdminsex(rs.getString("adminsex"));
				ub.setAdminaddress(rs.getString("adminaddress"));
				ub.setAdminrights(rs.getString("adminrights"));
				ub.setWx_name(rs.getString("wx_name"));				
				ub.setAl_name(rs.getString("al_name"));				
				ub.setWx_openid(rs.getString("wx_openid"));				
				ub.setLastloginip(rs.getString("lastloginip"));
				ub.setCreatetime(rs.getTimestamp("createtime"));
				ub.setLastLoginTime(rs.getTimestamp("lastLoginTime"));
				ub.setType(rs.getInt("type"));
				ub.setGroupid(rs.getInt("groupid"));
				ub.setWx_name(rs.getString("wx_name"));
				ub.setWx_openid(rs.getString("wx_openid"));
				ub.setAl_name(rs.getString("al_name"));
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
	public static ArrayList<UserBean> getUserBeanListByright()
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		ArrayList<UserBean> ubli=new ArrayList<UserBean>(5);
		Connection conn=ConnManager.getConnection(CN);
		String sql="select adminusername,id,admintelephone,adminmobilephone,adminname," +
				"adminsex,adminaddress,adminrights,wx_name,al_name,wx_openid," +
				"lastloginip,createtime,lastLoginTime,type,groupid,[wx_name],[al_name],[wx_openid] from admininfo ";
			sql+=" order by id";
		try {
			ps=conn.prepareStatement(sql);rs=ps.executeQuery();
			while(rs.next())
			{
				UserBean ub=new UserBean();
				ub.setAdminusername(rs.getString("adminusername"));
				ub.setId(rs.getInt("id"));
				ub.setAdmintelephone(rs.getString("admintelephone"));
				ub.setAdminmobilephone(rs.getString("adminmobilephone"));
				ub.setAdminname(rs.getString("adminname"));
				ub.setAdminsex(rs.getString("adminsex"));
				ub.setAdminaddress(rs.getString("adminaddress"));
				ub.setAdminrights(rs.getString("adminrights"));
				ub.setWx_name(rs.getString("wx_name"));				
				ub.setAl_name(rs.getString("al_name"));				
				ub.setWx_openid(rs.getString("wx_openid"));				
				ub.setLastloginip(rs.getString("lastloginip"));
				ub.setCreatetime(rs.getTimestamp("createtime"));
				ub.setLastLoginTime(rs.getTimestamp("lastLoginTime"));
				ub.setType(rs.getInt("type"));
				ub.setGroupid(rs.getInt("groupid"));
				ub.setWx_name(rs.getString("wx_name"));
				ub.setWx_openid(rs.getString("wx_openid"));
				ub.setAl_name(rs.getString("al_name"));
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
	

	public static boolean addUser(UserBean obj)
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="insert into [admininfo](adminmobilephone, adminname,adminaddress,CanAccessSeller, " +
				"adminusername, adminpassword, adminrights,createtime,adminsex,groupid) " +
				"values(?,?,?,?,?,?,?,?,?,?)";
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setString(i++, obj.getAdminmobilephone());
			ps.setString(i++, obj.getAdminname());
			ps.setString(i++,obj.getAdminaddress());
			ps.setString(i++,obj.getCanAccessSellerid());
			ps.setString(i++,obj.getAdminusername());
			ps.setString(i++, obj.getAdminpassword());
			ps.setString(i++, obj.getAdminrights());
			ps.setTimestamp(i++,new Timestamp(ClsTime.SystemTime()));
			ps.setString(i++, obj.getAdminsex());
			ps.setInt(i++, obj.getGroupid());
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
	public static void updateUser(UserBean ub) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql;
		if(ub.getAdminpassword().equals(""))
		{
			sql="update admininfo set " +
					"CanAccessSeller=?,adminmobilephone=?,adminname=?," +
					"adminaddress=?,adminrights=?,type=?,lastLoginTime=?,"
					+ "lastloginip=?,[wx_name]=?,[al_name]=?,[wx_openid]=?,admintelephone=?,oneceId=? where id=?";
		}
		else
		{
			sql="update admininfo set adminpassword=?," +
					"CanAccessSeller=?,adminmobilephone=?,adminname=?," +
					"adminaddress=?,adminrights=?,type=?,lastLoginTime=?,"
					+ "lastloginip=?,[wx_name]=?,[al_name]=?,[wx_openid]=?,admintelephone=?,oneceId=? where id=?";
		}
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			if(!ub.getAdminpassword().equals(""))
			{
				ps.setString(i++,ub.getAdminpassword());
			}
			ps.setString(i++,ub.getCanAccessSellerid());
			ps.setString(i++,ub.getAdminmobilephone());
			ps.setString(i++,ub.getAdminname());
			ps.setString(i++,ub.getAdminaddress());
			ps.setString(i++,ub.getAdminrights());
			ps.setInt(i++,ub.getType());
			ps.setTimestamp(i++,ub.getLastLoginTime());
			ps.setString(i++,ub.getLastloginip());
			ps.setString(i++,ub.getWx_name());
			ps.setString(i++,ub.getAl_name());
			ps.setString(i++,ub.getWx_openid());
			ps.setString(i++,ub.getAdmintelephone());
			ps.setString(i++,ub.getOneceId());
			ps.setInt(i++, ub.getId());
			ps.executeUpdate();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		
	}
	
	public static void deleteUser(int id) {
		String sql="delete from [admininfo] where id="+id;
		SqlADO.exec(sql);
	}
	
	public static boolean ChkUserRepeat(String username) {
		UserBean ub= getUserBean(username);
		return (ub!=null);
	}

	public String getOneceId() {
		return OneceId;
	}
	public void setOneceId(String oneceId) {
		OneceId = oneceId;
	}
	public Timestamp getOneceIdValidTime() {
		return OneceIdValidTime;
	}
	public void setOneceIdValidTime(Timestamp oneceIdValidTime) {
		OneceIdValidTime = oneceIdValidTime;
	}
}
