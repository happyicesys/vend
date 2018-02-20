package com.connectionpool;

	   
	import java.io.FileInputStream; 
import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; 
import java.sql.Statement;
import java.util.Properties; 
	   




	import javax.sql.DataSource; 
	   




import com.mchange.v2.c3p0.DataSources; 
	   
	/**
	 * c3p0连接池管理类
	 */ 
	public class c3p0Connect { 
	   
	    private static final String JDBC_DRIVER = "driverClass"; 
	    private static final String JDBC_URL = "jdbcUrl"; 
	   
	    private static DataSource ds; 
	    
	    private static c3p0Connect instance;
	    /**
	     * 初始化连接池代码块
	     */ 
	    static { 
	        initDBSource(); 
	    } 
	   
	    /**
	     * 初始化c3p0连接池
	     */ 
	    private static final void initDBSource() { 
	        Properties c3p0Pro = new Properties(); 
	        try { 
	            // 加载配置文件 
	            String path = c3p0Connect.class.getResource("/").getPath(); 
	            String websiteURL = (path.replace("/build/classes", "").replace("%20"," ").replace("classes/", "") + "c3p0.properties").replaceFirst("/", ""); 
	            FileInputStream in = new FileInputStream(websiteURL); 
	            c3p0Pro.load(in); 
	        } catch (Exception e) { 
	            e.printStackTrace(); 
	        } 
	   
	        String drverClass = c3p0Pro.getProperty(JDBC_DRIVER); 
	        if (drverClass != null) { 
	            try { 
	                // 加载驱动类 
	                Class.forName(drverClass); 
	            } catch (ClassNotFoundException e) { 
	                e.printStackTrace(); 
	            } 
	   
	        } 
	   
	        Properties jdbcpropes = new Properties(); 
	        Properties c3propes = new Properties(); 
	        for (Object key : c3p0Pro.keySet()) { 
	            String skey = (String) key; 
	            if (skey.startsWith("c3p0.")) { 
	                c3propes.put(skey, c3p0Pro.getProperty(skey)); 
	            } else { 
	                jdbcpropes.put(skey, c3p0Pro.getProperty(skey)); 
	            } 
	        } 
	   
	        try { 
	            // 建立连接池 
	            DataSource unPooled = DataSources.unpooledDataSource(c3p0Pro.getProperty(JDBC_URL), jdbcpropes); 
	            System.out.println(c3p0Pro.getProperty(JDBC_URL));
	            ds = DataSources.pooledDataSource(unPooled, c3propes); 
	   
	        } catch (SQLException e) { 
	            e.printStackTrace(); 
	        } 
	    } 
	   
	    /**
	     * 获取数据库连接对象
	     * 
	     * @return 数据连接对象
	     * @throws SQLException
	     */ 
//	    public static synchronized Connection getConnection() throws SQLException { 
//	        final Connection conn = ds.getConnection(); 
//	        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED); 
//	        return conn; 
//	    } 
	    
	    public synchronized Connection getConnection(String name) { 

	        Connection conn;
			try {
				conn = ds.getConnection();
		        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED); 
		        return conn; 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return null;
	    } 
	    public synchronized Connection getConnection(){ 
	        Connection conn=null;
			try {
				conn = ds.getConnection();
				conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED); 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        
	        return conn; 
	    } 
	    
	    public static void main (String args[]) 
	    {
	    	
	    	try {
	    		c3p0Connect ins=c3p0Connect.getInstance();
				Connection conn = ins.getConnection();
				StringBuilder strSql=new StringBuilder();

				strSql.append("select top(1) [id],[gmt_payment],[partner],[buyer_email],[trade_no],[total_fee],[gmt_create],[out_trade_no],[subject],[trade_status],");
				strSql.append(" [qrcode],[userid],[notifyXML],[machineid],[goodsid],[transfor_status] from [WxTradeLog] where [out_trade_no]=?  order by id ");

				PreparedStatement ps= conn.prepareStatement(strSql.toString());
				
				int i=1;
				ps.setString(i++, "20150408185102000001615100");			
				ResultSet rs= ps.executeQuery();
				if(rs.next())
				{
					System.out.println(rs.getInt("id"));
				}
				
				System.out.println(conn);
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public static c3p0Connect getInstance() {
			if(instance==null)
			{
				instance=new c3p0Connect();

			}
			return instance;
		}

		public  void freeConnection(String CN,Connection conn,ResultSet rs,Statement st)
		{
	        if(rs!=null){
	            try{
	                //关闭存储查询结果的ResultSet对象
	                rs.close();
	            }catch (Exception e) {
	                e.printStackTrace();
	            }
	            rs = null;
	        }
	        if(st!=null){
	            try{
	                //关闭负责执行SQL命令的Statement对象
	                st.close();
	            }catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	        
	        if(conn!=null){
	            try{
	                //将Connection连接对象还给数据库连接池
	                conn.close();
	            }catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
		}

	}