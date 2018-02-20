package beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import com.connectionpool.DBConnectionManager;

public class TempBean {
	private int id;
	private int temp;
	private Timestamp ttime;
	private int vid;
	private int groupid;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTemp() {
		return temp;
	}
	public void setTemp(int temp) {
		this.temp = temp;
	}
	public Timestamp getTtime() {
		return ttime;
	}
	public void setTtime(Timestamp ttime) {
		this.ttime = ttime;
	}
	public int getVid() {
		return vid;
	}
	public void setVid(int vid) {
		this.vid = vid;
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
	public static void InsertclsGroupBean(TempBean temp) 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="INSERT INTO [tempTab] ([groupid], [temp], [ttime], [vid]) VALUES (?,?,?,?)";
		Connection conn=ConnManager.getConnection(CN);
		try {
			int i=1;
			ps=conn.prepareStatement(sql);
			ps.setInt(i++, temp.getGroupid());
			ps.setInt(i++, temp.getTemp());
			ps.setTimestamp(i++, temp.getTtime());
			ps.setInt(i++, temp.getVid());
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
	}
	
	//SELECT TOP 1000  * FROM [dbo].[tempTab]
	

	public static ArrayList<TempBean> getLst(int vid ,Timestamp start,Timestamp end) 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		ArrayList<TempBean> glst=new ArrayList<TempBean>();
		
		String sqlString;

		sqlString="SELECT id,vid,groupid,temp,ttime FROM [dbo].[tempTab] where vid=? and ttime>? and ttime<? order by ttime asc";
		Connection conn=ConnManager.getConnection(CN);
		
		TempBean tBean=null;
		try {
			ps=conn.prepareStatement(sqlString);
			int i=1;
			ps.setInt(i++, vid);
			ps.setTimestamp(i++, start);
			ps.setTimestamp(i++, end);
			rs=ps.executeQuery();
			
			while (rs.next()) 
			{
				tBean=new TempBean();
				tBean.setId(rs.getInt("id"));
				tBean.setGroupid(rs.getInt("groupid"));	
				tBean.setTemp(rs.getInt("temp"));				
				tBean.setVid(rs.getInt("vid"));
				tBean.setTtime(rs.getTimestamp("ttime"));
				glst.add(tBean);
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
	
	
}
