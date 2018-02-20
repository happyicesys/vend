package beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.connectionpool.DBConnectionManager;

public class CustomerChargeLogBean {
	private int id;
	private String adminname;
	private String cardinfo;
	private String customername;
	private Timestamp gmt;
	private int amount;
	private int amount_after_charge;
	
	private String _user_id_string;
	
	private int groupid;
	private String tradeid;
	
	public int getGroupid() {
		return groupid;
	}
	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAdminname() {
		return adminname;
	}
	public void setAdminname(String adminname) {
		this.adminname = adminname;
	}
	public String getCardinfo() {
		return cardinfo;
	}
	public void setCardinfo(String cardinfo) {
		this.cardinfo = cardinfo;
	}
	public String getCustomername() {
		return customername;
	}
	public void setCustomername(String customername) {
		this.customername = customername;
	}
	public Timestamp getGmt() {
		return gmt;
	}
	public void setGmt(Timestamp gmt) {
		this.gmt = gmt;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getAmount_after_charge() {
		return amount_after_charge;
	}
	public void setAmount_after_charge(int amount_after_charge) {
		this.amount_after_charge = amount_after_charge;
	}
	public String getTradeid() {
		return tradeid;
	}
	public void setTradeid(String tradeid) {
		this.tradeid = tradeid;
	}
	
	public String get_user_id_string() {
		return _user_id_string;
	}
	public void set_user_id_string(String _user_id_string) {
		this._user_id_string = _user_id_string;
	}
	private static DBConnectionManager ConnManager=DBConnectionManager.getInstance();
	private final static String CN="BlueShine";
	public final static String CMPPDB="CMPPDB";
	

	public static ArrayList<CustomerChargeLogBean> getCustomerBeanLst(String str,int pageindex,int count_per_page) 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		CustomerChargeLogBean beans=null;
		ArrayList<CustomerChargeLogBean> lst=new ArrayList<CustomerChargeLogBean>();
		String sql ="select top("+ count_per_page +") [id],[adminname],[_user_id_string],[tradeid],[cardinfo],[customername],[gmt],[amount],[amount_after_charge],"
				+ "[groupid] from [CustomerChargeLog] where id not in(select top("+ count_per_page*(pageindex-1) +") id "
						+ "from [CustomerChargeLog] where "+ str +") and " + str +" order by gmt desc";
		
		Connection conn=ConnManager.getConnection(CN);
		try 
		{
			ps=conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next())
			{
				beans=new CustomerChargeLogBean();
				beans.setId(rs.getInt("id"));
				beans.setAdminname(rs.getString("adminname"));
				beans.setCardinfo(rs.getString("cardinfo"));
				beans.setCustomername(rs.getString("customername"));
				beans.setGmt(rs.getTimestamp("gmt"));
				beans.setAmount(rs.getInt("amount"));
				beans.setAmount_after_charge(rs.getInt("amount_after_charge"));
				beans.setGroupid(rs.getInt("groupid"));
				beans.setTradeid(rs.getString("tradeid"));
				beans.set_user_id_string(rs.getString("_user_id_string"));
				lst.add(beans);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		return lst;
	}
	
	public void Add() 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="INSERT INTO [CustomerChargeLog] ([adminname],[_user_id_string] ,[cardinfo] ,[customername] ,"
				+ "[gmt] ,[amount] ,[amount_after_charge] ,[groupid],[tradeid] ) VALUES "
				+ "(?,?,?,?,?,?,?,?,?)";
		
		Connection conn=ConnManager.getConnection(CN);
		
		try {
			ps=conn.prepareStatement(sql);
				int i=1;
				ps.setString(i++, this.adminname);
				ps.setString(i++, this._user_id_string);
				ps.setString(i++, this.cardinfo);
				ps.setString(i++, this.customername);
				ps.setTimestamp(i++, this.gmt);
				ps.setInt(i++, this.amount);
				ps.setInt(i++, this.amount_after_charge);
				ps.setInt(i++, this.groupid);
				ps.setString(i++, this.tradeid);
				ps.executeUpdate();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}
	public static int getLogRowsCount(String str) {
		ResultSet rs=null;
		Statement st=null;
		Connection conn=ConnManager.getConnection(CN);
		int c=0;
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT count(id) as c FROM CustomerChargeLog where ");
		stringBuilder.append(str);
		String sql=stringBuilder.toString();

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


}
