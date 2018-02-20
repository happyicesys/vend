package beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.connectionpool.DBConnectionManager;

public class clsGoodsBean {
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGoodsname() {
		return goodsname;
	}
	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getPicname() {
		return picname;
	}
	public void setPicname(String picname) {
		this.picname = picname;
	}
	public String getDes1() {
		return des1;
	}
	public void setDes1(String des1) {
		this.des1 = des1;
	}
	public String getDes2() {
		return des2;
	}
	public void setDes2(String des2) {
		this.des2 = des2;
	}
	public String getDes3() {
		return des3;
	}
	public void setDes3(String des3) {
		this.des3 = des3;
	}
	
	public int getInventory() {
		return inventory;
	}
	public void setInventory(int inventory) {
		this.inventory = inventory;
	}

	private int inventory;
	
	private int id;
	private String goodsname;
	private int price;
	private String picname;
	private String des1;
	private String des2;
	private String des3;

	private static DBConnectionManager ConnManager=DBConnectionManager.getInstance();
	private final static String CN="BlueShine";
	public final static String CMPPDB="CMPPDB";	
	
	public static ArrayList<clsGoodsBean> getGoodsBeanLst(int page,int count_per_page, int groupid) 
	{
		ArrayList<clsGoodsBean> li=new ArrayList<clsGoodsBean>();
		ResultSet rs=null;
		PreparedStatement ps=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql=null;

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("SELECT top("+count_per_page+") id, goodsname, price, picname, " +
					"des1 FROM goodsinfo where (groupid=? or groupid=0) and id not in (SELECT top("+((page-1)*count_per_page)+") id"
							+ " FROM goodsinfo where (groupid=? or groupid=0) order by id desc) order by id desc");
			sql=stringBuilder.toString();
			
		try {
			ps=conn.prepareStatement(sql);
			ps.setInt(1, groupid);
			ps.setInt(2, groupid);
			rs=ps.executeQuery();
			clsGoodsBean tb;
			while(rs.next())
			{
				tb=new clsGoodsBean();
				tb.setId(rs.getInt("id"));
				tb.setGoodsname(rs.getString("goodsname"));
				tb.setPrice(rs.getInt("price"));
				tb.setPicname(rs.getString("picname"));
				tb.setDes1(rs.getString("des1"));
				li.add(tb);
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
	//新添加
	public static ArrayList<clsGoodsBean> getGoodsBeanLst(int page,int count_per_page,String goodsname, int groupid) 
	{
		ArrayList<clsGoodsBean> li=new ArrayList<clsGoodsBean>();
		ResultSet rs=null;
		Statement st=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql=null;

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("SELECT top("+count_per_page+") id, goodsname, price, picname, " +
					"des1 FROM goodsinfo where goodsname like '%"+ goodsname +"%' and (groupid=? or groupid=0) and id not in (SELECT top("+((page-1)*count_per_page)+") "
							+ "id FROM goodsinfo where (groupid=? or groupid=0)  order by id desc) order by id desc");
			sql=stringBuilder.toString();
			System.out.println(sql);
			
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			int i=1;
			//ps.setString(i++,goodsname);
			ps.setInt(i++,groupid);
			ps.setInt(i++,groupid);
			rs=ps.executeQuery();
			clsGoodsBean tb;
			while(rs.next())
			{
				tb=new clsGoodsBean();
				tb.setId(rs.getInt("id"));
				tb.setGoodsname(rs.getString("goodsname"));
				tb.setPrice(rs.getInt("price"));
				tb.setPicname(rs.getString("picname"));
				tb.setDes1(rs.getString("des1"));
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
	
	public static clsGoodsBean getGoodsBean(String goodsname, int groupid) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		clsGoodsBean tb=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql="SELECT id, goodsname, price, picname, des1, des2, des3 FROM goodsinfo where  (groupid=? or groupid=0) and goodsname=?";
		try {
			ps=conn.prepareStatement(sql);
			ps.setInt(1, groupid);
			ps.setString(2, goodsname);
			rs=ps.executeQuery();
			if(rs.next())
			{
				tb=new clsGoodsBean();
				tb.setId(rs.getInt("id"));
				tb.setGoodsname(rs.getString("goodsname"));
				tb.setPrice(rs.getInt("price"));
				tb.setPicname(rs.getString("picname"));
				tb.setDes1(rs.getString("des1"));
				tb.setDes2(rs.getString("des2"));
				tb.setDes3(rs.getString("des3"));
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
	

	public static clsGoodsBean getGoodPriceAndInventory(int vendid,int goodsid)
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		clsGoodsBean temp=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql="SELECT DISTINCT " +
				" price,SUM (amount) AS [inventory] " +
				" FROM goodroadinfo" +
				" WHERE goodsid = ? AND machineid = ? AND iserror =0 and pauseFlg=0" +
				" GROUP BY price order by price desc";
		try {
			
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++, goodsid);
			ps.setInt(i++, vendid);
			rs=ps.executeQuery();
			if(rs.next())
			{
				temp=new clsGoodsBean();
				temp.setPrice(rs.getInt("price"));
				temp.setInventory(rs.getInt("inventory"));
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
	public static ArrayList<clsGoodsBean> getGoodsBeanList() 
	{
		ArrayList<clsGoodsBean> li=new ArrayList<clsGoodsBean>();
		ResultSet rs=null;
		Statement st=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql=null;

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("SELECT * FROM goodsinfo order by id desc");
			sql=stringBuilder.toString();
			//System.out.println(sql);
			
		try {
			st=conn.createStatement();
			rs=st.executeQuery(sql);
			clsGoodsBean tb;
			while(rs.next())
			{
				tb=new clsGoodsBean();
				tb.setId(rs.getInt("id"));
				tb.setGoodsname(rs.getString("goodsname"));
				tb.setPrice(rs.getInt("price"));
				tb.setPicname(rs.getString("picname"));
				tb.setDes1(rs.getString("des1"));
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
	
	public static clsGoodsBean getGoodsBean(int id)
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		clsGoodsBean tb=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql="SELECT id, goodsname, price, picname, des1, des2, des3 FROM goodsinfo where id=?";
		try {
			ps=conn.prepareStatement(sql);
			
			ps.setInt(1, id);
			rs=ps.executeQuery();
			if(rs.next())
			{
				tb=new clsGoodsBean();
				tb.setId(rs.getInt("id"));
				tb.setGoodsname(rs.getString("goodsname"));
				tb.setPrice(rs.getInt("price"));
				tb.setPicname(rs.getString("picname"));

				tb.setDes1(rs.getString("des1"));
				tb.setDes2(rs.getString("des2"));
				tb.setDes3(rs.getString("des3"));
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
	public static Boolean AddGoodsInfo(String goodsname,int price, String des1, String des2,String des3, String pic1, int groupid) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="insert into goodsinfo ( goodsname, price, picname, des1, des2, des3,groupid) values(?,?,?,?,?,?,?)";
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
			ps.setInt(i++,groupid);
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
	public static ArrayList<clsGoodsBean> getGoodsBeanLst(int groupid,String goodsname) 
	{
		ArrayList<clsGoodsBean> li=new ArrayList<clsGoodsBean>();
		ResultSet rs=null;
		PreparedStatement ps=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql=null;

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("SELECT id, goodsname, price, picname, des1, des2, des3 FROM goodsinfo where groupid=? and goodsname like '%"+ goodsname +"%'");
			sql=stringBuilder.toString();
			
		try {
			ps=conn.prepareStatement(sql);
			ps.setInt(1, groupid);
			rs=ps.executeQuery();
			clsGoodsBean tb;
			while(rs.next())
			{
				tb=new clsGoodsBean();
				tb.setId(rs.getInt("id"));
				tb.setGoodsname(rs.getString("goodsname"));
				tb.setPrice(rs.getInt("price"));
				tb.setPicname(rs.getString("picname"));
				tb.setDes1(rs.getString("des1"));
				tb.setDes2(rs.getString("des2"));
				tb.setDes3(rs.getString("des3"));
				li.add(tb);
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
	
	public static ArrayList<clsGoodsBean> getGoodsBeanLst(int groupid) 
	{
		ArrayList<clsGoodsBean> li=new ArrayList<clsGoodsBean>();
		ResultSet rs=null;
		PreparedStatement ps=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql=null;

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("SELECT id, goodsname, price, picname, des1, des2, des3 FROM goodsinfo where groupid=?");
			sql=stringBuilder.toString();
		try {
			ps=conn.prepareStatement(sql);
			ps.setInt(1, groupid);
			rs=ps.executeQuery();
			clsGoodsBean tb;
			while(rs.next())
			{
				tb=new clsGoodsBean();
				tb.setId(rs.getInt("id"));
				tb.setGoodsname(rs.getString("goodsname"));
				tb.setPrice(rs.getInt("price"));
				tb.setPicname(rs.getString("picname"));
				tb.setDes1(rs.getString("des1"));
				tb.setDes2(rs.getString("des2"));
				tb.setDes3(rs.getString("des3"));
				li.add(tb);
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
	
//
//	public static ArrayList<clsGoodsBean> getGoodsBeanLst(int groupid,int page) 
//	{
//		int count=50;
//		ArrayList<clsGoodsBean> li=new ArrayList<clsGoodsBean>();
//		ResultSet rs=null;
//		PreparedStatement ps=null;
//		Connection conn=ConnManager.getConnection(CN);
//		String sql=null;
//
//			StringBuilder stringBuilder = new StringBuilder();
//			stringBuilder.append("SELECT top("+count+") id, goodsname, price, picname, des1, des2, des3 FROM goodsinfo ");
//			stringBuilder.append( "where groupid=? and id not in (select top("+ (page-1) * count +") id from goodsinfo ");
//			stringBuilder.append( "where groupid=? order by id asc) order by id asc");
//			sql=stringBuilder.toString();
//		try {
//			ps=conn.prepareStatement(sql);
//			ps.setInt(1, groupid);
//			ps.setInt(2, groupid);
//			rs=ps.executeQuery();
//			clsGoodsBean tb;
//			while(rs.next())
//			{
//				tb=new clsGoodsBean();
//				tb.setId(rs.getInt("id"));
//				tb.setGoodsname(rs.getString("goodsname"));
//				tb.setPrice(rs.getInt("price"));
//				tb.setPicname(rs.getString("picname"));
//				tb.setDes1(rs.getString("des1"));
//				tb.setDes2(rs.getString("des2"));
//				tb.setDes3(rs.getString("des3"));
//				li.add(tb);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		finally
//		{
//			ConnManager.freeConnection(CN, conn,rs,ps);
//		}
//		return li;
//	}
	
	public static int getGoodsBeanCount(int groupid) 
	{
		int count=0;
		ResultSet rs=null;
		PreparedStatement ps=null;
		Connection conn=ConnManager.getConnection(CN);
		String sql=null;
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("SELECT count(id) as c FROM goodsinfo where groupid=? or groupid=0");
			sql=stringBuilder.toString();
		try {
			ps=conn.prepareStatement(sql);
			ps.setInt(1, groupid);
			rs=ps.executeQuery();

			if(rs.next())
			{
				count=rs.getInt("c");
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
	
	/*
	 * create by zhy 2016-1-26
	 */
	//货物查询
		public static int getGoodsBeanCount(String goodsname, int groupid) 
		{
			int count=0;
			PreparedStatement ps=null;
			ResultSet rs=null;
			Connection conn=ConnManager.getConnection(CN);
			String sql=null;

				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("SELECT count(id) as c FROM goodsinfo where  goodsname like '%"+ goodsname +"%' and groupid=? ");
				sql=stringBuilder.toString();
			try {
				ps=conn.prepareStatement(sql);
				int i=1;
				//ps.setString(i++,goodsname);
				ps.setInt(i++,groupid);
				rs=ps.executeQuery();

				if(rs.next())
				{
					count=rs.getInt("c");
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
}
