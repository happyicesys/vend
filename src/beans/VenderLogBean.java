package beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.ClsTime;
import com.connectionpool.DBConnectionManager;

public class VenderLogBean 
{
//	[id] int NOT NULL IDENTITY(1,1) ,
//	[request] varchar(MAX) NULL ,
//	[response] varchar(MAX) NULL ,
//	[mid] int NULL ,
//	[gmt] datetime NULL ,

	private int id;
	private String request;
	private String response;
	private int mid;
	private Timestamp gmt;
	private String request_ori;
	private int fid;
	private int tid;
	private boolean abandon;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	public Timestamp getGmt() {
		return gmt;
	}
	public void setGmt(Timestamp gmt) {
		this.gmt = gmt;
	}
	public String getRequest_ori() {
		return request_ori;
	}
	public void setRequest_ori(String request_ori) {
		this.request_ori = request_ori;
	}
	public int getFid() {
		return fid;
	}
	public void setFid(int fid) 
	{
		this.fid = fid;
	}
	
	public int getTid() 
	{
		return tid;
	}
	public void setTid(int tid) 
	{
		this.tid = tid;
	}
	


	public VenderLogBean(String request, String response, int mid,
			String request_ori, int fid, int tid) {
		super();
		this.request = request;
		this.response = response;
		this.mid = mid;
		this.request_ori = request_ori;
		this.fid = fid;
		this.tid = tid;
		this.abandon=false;
	}
	public VenderLogBean() {
		// TODO Auto-generated constructor stub
	}

	private static DBConnectionManager ConnManager=DBConnectionManager.getInstance();
	private final static String CN="BlueShine";
	public final static String CMPPDB="CMPPDB";	
	
	private static Object staticobj=new Object();
    private static ArrayList<VenderLogBean> lst=new ArrayList<VenderLogBean>();
    //private static boolean ForceSave=false;
	
	public static ArrayList<VenderLogBean> getLst() {
		return lst;
	}
	public boolean add()
	{
		if(!abandon)
		{
			this.gmt=new Timestamp(System.currentTimeMillis());
			synchronized (staticobj)
			{
				
				lst.add(this);
				
				if(lst.size()>6000)
				{
					//writeToSql();
					lst.clear();
				}
			}
		}
		return true;
	}
	
	public static int ForceSaveLog()
	{
		int count=lst.size();
		synchronized (staticobj)
		{
			writeToSql();
			lst.clear();
		}
		return count;
	}
	
	private static void writeToSql()
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="set nocount on INSERT INTO [VenderLog] ([gmt],[request], [response], [mid],[request_ori],fid,tid) VALUES"
				+ " (?, ?, ?, ?, ?, ?, ?)";
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			
			for (VenderLogBean obj : lst) {
				int i=1;
				ps.setTimestamp(i++,obj.gmt);
				ps.setString(i++, obj.request);
				ps.setString(i++, obj.response);
				ps.setInt(i++,obj.mid);
				ps.setString(i++,obj.request_ori);
				ps.setInt(i++,obj.fid);
				ps.setInt(i++,obj.tid);
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
	 
	

//	public static void updateTextMsgBeanrLst(List<VenderLogBean> lst) {
//		String sql;
//
//		sql="update WxTextMsg set [gmt_create]=?, [content]=?, [touser]=?, [status]=?, [retmsg]=?, [groupid]=? where id=?";
//			
//		Connection conn=ConnManager.getConnection(CN);
//		try {
//			PreparedStatement ps=conn.prepareStatement(sql);
//			for (TextMsgBean textMsgBean : lst) {
//				int i=1;
//				ps.setTimestamp(i++,textMsgBean.gmt_create);
//				ps.setString(i++,textMsgBean.content);
//				ps.setString(i++,textMsgBean.touser);
//				ps.setInt(i++,textMsgBean.status);
//				ps.setString(i++,textMsgBean.retmsg);
//				ps.setInt(i++,textMsgBean.groupid);
//
//				ps.setInt(i++, textMsgBean.getId());
//				ps.executeUpdate();
//			}
//			
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		finally
//		{
//			ConnManager.freeConnection(CN, conn,rs,ps);
//		}
//		
//	}
	
	public static List<VenderLogBean> getList(int status) 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		List<VenderLogBean> lst=new ArrayList<VenderLogBean>();
		
		rs=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql="select [id], [gmt],[request], [response], [mid],[request_ori],fid,tid from VenderLog";
		try {
			ps=conn.prepareStatement(sql);
			VenderLogBean tmb;
			rs=ps.executeQuery();
			while(rs.next())
			{
				tmb=new VenderLogBean();
				tmb.id=rs.getInt("id");
				tmb.gmt=rs.getTimestamp("gmt");
				tmb.request=rs.getString("request");
				tmb.response=rs.getString("response");
				tmb.mid=rs.getInt("mid");
				tmb.request_ori=rs.getString("request_ori");
				tmb.fid=rs.getInt("fid");
				tmb.tid=rs.getInt("tid");
				lst.add(tmb);
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
	public void updateResponse() {

//		String sql;
//
//		sql="update VenderLog set response=? where id=?";
//			
//		Connection conn=ConnManager.getConnection(CN);
//		try {
//			PreparedStatement ps=conn.prepareStatement(sql);
//			int i=1;
//			ps.setString(i++,this.response);
//			ps.setInt(i++,this.id);
//			ps.executeUpdate();
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		finally
//		{
//			ConnManager.freeConnection(CN, conn,rs,ps);
//		}
	}
	public boolean isAbandon() {
		return abandon;
	}
	public void setAbandon(boolean abandon) {
		this.abandon = abandon;
	}
}
