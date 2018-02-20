package beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.ClsTime;
import com.connectionpool.DBConnectionManager;

public class TextMsgBean {
//	[id] int NOT NULL IDENTITY(1,1) ,
//	[gmt_create] datetime NULL ,
//	[content] varchar(1024) NULL ,
//	[touser] varchar(100) NULL ,
//	[status] int NULL ,
//	[retmsg] varchar(1024) NULL 
	
	private int id;
	private Timestamp gmt_create;
	private String content;
	private String touser;
	private int status;
	private String retmsg;
	
	private int groupid;

	public TextMsgBean() {

	}


	public TextMsgBean(String content, String touser,int groupid) {
		this.content = content;
		this.touser = touser;
		this.groupid=groupid;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Timestamp getGmt_create() {
		return gmt_create;
	}


	public void setGmt_create(Timestamp gmt_create) {
		this.gmt_create = gmt_create;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getTouser() {
		return touser;
	}


	public void setTouser(String touser) {
		this.touser = touser;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	public String getRetmsg() {
		return retmsg;
	}


	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}
	
	

	public int getGroupid() {
		return groupid;
	}


	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}
	
	//INSERT INTO [vender].[dbo].[WxTextMsg] ([id], [gmt_create], [content], [touser], [status], [retmsg], [groupid]) VALUES (NULL, '2016-09-12 21:05:35', '111', '11', '1', '1', '1')
	private static DBConnectionManager ConnManager=DBConnectionManager.getInstance();
	private final static String CN="BlueShine";
	public final static String CMPPDB="CMPPDB";	
	
	public static boolean add(TextMsgBean obj)
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="INSERT INTO [WxTextMsg] ([gmt_create], [content], [touser], [status], [retmsg], [groupid]) VALUES"
				+ " (?, ?, ?, ?, ?, ?)";
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setTimestamp(i++,new Timestamp(ClsTime.SystemTime()));
			ps.setString(i++, obj.content);
			ps.setString(i++, obj.touser);
			ps.setInt(i++,obj.status);
			ps.setString(i++,obj.retmsg);
			ps.setInt(i++,obj.groupid);

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
	

	public static void updateTextMsgBeanrLst(List<TextMsgBean> lst) {
		String sql;
		PreparedStatement ps=null;
		ResultSet rs=null;
		sql="update WxTextMsg set [gmt_create]=?, [content]=?, [touser]=?, [status]=?, [retmsg]=?, [groupid]=? where id=?";
			
		Connection conn=ConnManager.getConnection(CN);
		try {
			ps=conn.prepareStatement(sql);
			for (TextMsgBean textMsgBean : lst) {
				int i=1;
				ps.setTimestamp(i++,textMsgBean.gmt_create);
				ps.setString(i++,textMsgBean.content);
				ps.setString(i++,textMsgBean.touser);
				ps.setInt(i++,textMsgBean.status);
				ps.setString(i++,textMsgBean.retmsg);
				ps.setInt(i++,textMsgBean.groupid);

				ps.setInt(i++, textMsgBean.getId());
				ps.executeUpdate();
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		finally
		{
			ConnManager.freeConnection(CN, conn,rs,ps);
		}
		
	}
	
	public static List<TextMsgBean> getList(int status) 
	{
		List<TextMsgBean> lst=new ArrayList<TextMsgBean>();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		Connection conn=ConnManager.getConnection(CN);
		String sql="select [id], [gmt_create], [content], [touser], [status], [retmsg], [groupid] from WxTextMsg where status="+status;
		try {
			ps=conn.prepareStatement(sql);
			TextMsgBean tmb;
			rs=ps.executeQuery();
			while(rs.next())
			{
				tmb=new TextMsgBean();
				tmb.content=rs.getString("content");
				tmb.gmt_create=rs.getTimestamp("gmt_create");
				tmb.groupid=rs.getInt("groupid");
				tmb.id=rs.getInt("id");
				tmb.touser=rs.getString("touser");
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


	
	

}
