package beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.connectionpool.DBConnectionManager;

public class CustomerBean {
	private int id;
	private String _user_id_string;
	/**
	 * 会员用户名
	 */
	private String _user_name;
	/**
	 * 会员密码
	 */
	private String _user_pwd;
	/**
	 * 会员电话号码
	 */
	private String _user_tel;
	/**
	 * 会员卡号
	 */
	private String _user_id_card;
	/**
	 * 会员生日
	 */
	private Timestamp _user_birthday;
	/**
	 * 当前会员金额,单位:分
	 */
	private int _user_amount;
	/**
	 * 会员创建日期
	 */
	private Timestamp _user_builder_date;//创建日期
//	/**
//	 * 会员充值日期
//	 */
//	private Timestamp _user_last_charge_date;//上次充值日期
//	/**
//	 * 会员最近一次充值金额
//	 */
//	private int _user_last_charge_amount;//最近一次充值金额
	private int groupid;
	private int _user_xiaofei_times;
	/**
	 * 是否启用消费次数限制
	 */
	private int _user_xiaofei_times_en;
	/**
	 *  性别
	 */
	private String sex_type;	
	/**
	 * 会员地址
	 */
	private String _user_address;
	
	/**
	 * 指示用户是否被禁用
	 */
	private int _user_disable;
	
	/**
	 * 消费次剩余次数
	 */
	private int _user_xiaofei_rest_times;
	
	private Timestamp last_jiaoyi_time;
	private Timestamp last_charge_time;
	private int last_charge_amount;
	private int last_jiaoyi_amount;
	
	private int _user_max_credit_limit;
	
	public Timestamp getLast_jiaoyi_time() {
		return last_jiaoyi_time;
	}
	public void setLast_jiaoyi_time(Timestamp last_jiaoyi_time) {
		this.last_jiaoyi_time = last_jiaoyi_time;
	}
	public Timestamp getLast_charge_time() {
		return last_charge_time;
	}
	public void setLast_charge_time(Timestamp last_charge_time) {
		this.last_charge_time = last_charge_time;
	}
	public int getLast_charge_amount() {
		return last_charge_amount;
	}
	public void setLast_charge_amount(int last_charge_amount) {
		this.last_charge_amount = last_charge_amount;
	}
	public int getLast_jiaoyi_amount() {
		return last_jiaoyi_amount;
	}
	public void setLast_jiaoyi_amount(int last_jiaoyi_amount) {
		this.last_jiaoyi_amount = last_jiaoyi_amount;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String get_user_id_string() {
		return _user_id_string;
	}
	public void set_user_id_string(String _user_id_string) {
		this._user_id_string = _user_id_string;
	}
	public String get_user_name() {
		return _user_name;
	}
	public void set_user_name(String _user_name) {
		this._user_name = _user_name;
	}
	public String get_user_pwd() {
		return _user_pwd;
	}
	public void set_user_pwd(String _user_pwd) {
		this._user_pwd = _user_pwd;
	}
	public String get_user_tel() {
		return _user_tel;
	}
	public void set_user_tel(String _user_tel) {
		this._user_tel = _user_tel;
	}
	public String get_user_id_card() {
		return _user_id_card;
	}
	public void set_user_id_card(String _user_id_card) {
		this._user_id_card = _user_id_card;
	}
	public Timestamp get_user_birthday() {
		return _user_birthday;
	}
	public void set_user_birthday(Timestamp _user_birthday) {
		this._user_birthday = _user_birthday;
	}
	public int get_user_amount() {
		return _user_amount;
	}
	public void set_user_amount(int _user_amount) {
		this._user_amount = _user_amount;
	}
	public Timestamp get_user_builder_date() {
		return _user_builder_date;
	}
	public void set_user_builder_date(Timestamp _user_builder_date) {
		this._user_builder_date = _user_builder_date;
	}

	public int getGroupid() {
		return groupid;
	}
	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public int get_user_xiaofei_times() {
		return _user_xiaofei_times;
	}
	public void set_user_xiaofei_times(int _user_xiaofei_times) {
		this._user_xiaofei_times = _user_xiaofei_times;
	}
	public int get_user_xiaofei_times_en() {
		return _user_xiaofei_times_en;
	}
	public void set_user_xiaofei_times_en(int _user_xiaofei_times_en) {
		this._user_xiaofei_times_en = _user_xiaofei_times_en;
	}
	
	public String getSex_type() {
		return sex_type;
	}
	public void setSex_type(String sex_type) {
		this.sex_type = sex_type;
	}
	public String get_user_address() {
		return _user_address;
	}
	public void set_user_address(String _user_address) {
		this._user_address = _user_address;
	}
	
	public int get_user_disable() {
		return _user_disable;
	}
	public void set_user_disable(int _user_disable) {
		this._user_disable = _user_disable;
	}
	
	public int get_user_xiaofei_rest_times() {
		return _user_xiaofei_rest_times;
	}
	public void set_user_xiaofei_rest_times(int _user_xiaofei_rest_times) {
		this._user_xiaofei_rest_times = _user_xiaofei_rest_times;
	}
	
	public int get_user_max_credit_limit() {
		return _user_max_credit_limit;
	}
	public void set_user_max_credit_limit(int _user_max_credit_limit) {
		this._user_max_credit_limit = _user_max_credit_limit;
	}

	
	private static DBConnectionManager ConnManager=DBConnectionManager.getInstance();
	private final static String CN="BlueShine";
	public final static String CMPPDB="CMPPDB";
	
		
	
	public static final int CUSTOMER_OK = 0;
	public static final int CUSTOMER_DISABLE = 1;	
	public static final int CUSTOMER_NOT_ENOUGH_BALANCE = 2;	
	public static final int CUSTOMER_NOT_ENOUGH_TIMES= 3;	
	public static final int CUSTOMER_INVALID_CARD= 4;
	public static final int CUSTOMER_CREDIT_VALUE_ERR= 5;
	public static final int CUSTOMER_CREDIT_OVER_LIMIT= 6;
	
	public static CustomerBean getCustomerBeanByCardID(String cardid) 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		CustomerBean beans=null;
	
		String sql ="select top(1) [id],[_user_id_string], [_user_name], [_user_pwd], [_user_tel], [_user_id_card],"
				+ " [_user_birthday], [_user_amount], [_user_builder_date], [groupid], [_user_address], [sex_type], [_user_xiaofei_times_en], "
				+ "[_user_xiaofei_times],[_user_disable],[_user_xiaofei_rest_times],[last_jiaoyi_amount],[last_jiaoyi_time],"
				+ "[last_charge_time],[last_charge_amount],[_user_max_credit_limit] from userdata where _user_id_card=?";
		
		Connection conn=ConnManager.getConnection(CN);
		try 
		{
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setString(i++,cardid);
			rs = ps.executeQuery();
			if(rs.next())
			{
				beans=new CustomerBean();
				beans._user_address=rs.getString("_user_address");
				beans._user_id_string=rs.getString("_user_id_string");
				beans._user_amount=rs.getInt("_user_amount");
				beans._user_birthday=rs.getTimestamp("_user_birthday");
				beans._user_builder_date=rs.getTimestamp("_user_builder_date");
				beans._user_id_card=rs.getString("_user_id_card");

				beans._user_name=rs.getString("_user_name");
				beans._user_pwd=rs.getString("_user_pwd");
				beans._user_tel=rs.getString("_user_tel");
				beans._user_xiaofei_times=rs.getInt("_user_xiaofei_times");
				beans._user_xiaofei_times_en=rs.getInt("_user_xiaofei_times_en");
				beans.groupid=rs.getInt("groupid");
				beans.id=rs.getInt("id");
				beans.sex_type=rs.getString("sex_type");
				beans._user_disable=rs.getInt("_user_disable");
				beans._user_xiaofei_rest_times=rs.getInt("_user_xiaofei_rest_times");
				beans.last_charge_amount=rs.getInt("last_charge_amount");
				beans.last_jiaoyi_amount=rs.getInt("last_jiaoyi_amount");
				beans.last_charge_time=rs.getTimestamp("last_charge_time");
				beans.last_jiaoyi_time=rs.getTimestamp("last_jiaoyi_time");
				beans._user_max_credit_limit=rs.getInt("_user_max_credit_limit");
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
		return beans;
	}
	
	public static ArrayList<CustomerBean> getCustomerBeanLst(int groupid) 
	{
		return getCustomerBeanLst(groupid,null);
	}

	public static ArrayList<CustomerBean> getCustomerBeanLst(int groupid,String con) 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		CustomerBean beans=null;
		ArrayList<CustomerBean> lst=new ArrayList<CustomerBean>();
		String sql ="select  [id],[_user_id_string], [_user_name], [_user_pwd], [_user_tel], [_user_id_card],"
				+ " [_user_birthday], [_user_amount], [_user_builder_date], [groupid], [_user_address], [sex_type], [_user_xiaofei_times_en], "
				+ "[_user_xiaofei_times],[_user_disable],[_user_xiaofei_rest_times],[last_jiaoyi_amount],[last_jiaoyi_time],"
				+ "[last_charge_time],[last_charge_amount],[_user_max_credit_limit] from userdata where groupid=? ";
		
		if(con!=null)
		{
			sql+= " "+ con;
		}
		sql+=" order by id asc";
		
		Connection conn=ConnManager.getConnection(CN);
		try 
		{
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++,groupid);
			rs = ps.executeQuery();
			while(rs.next())
			{
				beans=new CustomerBean();
				beans._user_id_string=rs.getString("_user_id_string");				
				beans._user_address=rs.getString("_user_address");
				beans._user_amount=rs.getInt("_user_amount");
				beans._user_birthday=rs.getTimestamp("_user_birthday");
				beans._user_builder_date=rs.getTimestamp("_user_builder_date");
				beans._user_id_card=rs.getString("_user_id_card");

				beans._user_name=rs.getString("_user_name");
				beans._user_pwd=rs.getString("_user_pwd");
				beans._user_tel=rs.getString("_user_tel");
				beans._user_xiaofei_times=rs.getInt("_user_xiaofei_times");
				beans._user_xiaofei_times_en=rs.getInt("_user_xiaofei_times_en");
				beans.groupid=rs.getInt("groupid");
				beans.id=rs.getInt("id");
				beans.sex_type=rs.getString("sex_type");
				beans._user_disable=rs.getInt("_user_disable");
				beans._user_xiaofei_rest_times=rs.getInt("_user_xiaofei_rest_times");
				beans.last_charge_amount=rs.getInt("last_charge_amount");
				beans.last_jiaoyi_amount=rs.getInt("last_jiaoyi_amount");
				beans.last_charge_time=rs.getTimestamp("last_charge_time");
				beans.last_jiaoyi_time=rs.getTimestamp("last_jiaoyi_time");
				beans._user_max_credit_limit=rs.getInt("_user_max_credit_limit");
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
	public static CustomerBean getCustomerBeanByUserName(String tem_username) 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		CustomerBean beans=null;
	
		String sql ="select top(1) [id],[_user_id_string], [_user_name], [_user_pwd], [_user_tel], [_user_id_card],"
				+ " [_user_birthday], [_user_amount], [_user_builder_date], [groupid], [_user_address], [sex_type], [_user_xiaofei_times_en], "
				+ "[_user_xiaofei_times],[_user_disable],[_user_xiaofei_rest_times],[last_jiaoyi_amount],[last_jiaoyi_time],"
				+ "[last_charge_time],[last_charge_amount],[_user_max_credit_limit] from userdata where _user_name=?";
		
		Connection conn=ConnManager.getConnection(CN);
		try 
		{
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setString(i++,tem_username);
			rs = ps.executeQuery();
			if(rs.next())
			{
				beans=new CustomerBean();
				beans._user_address=rs.getString("_user_address");
				beans._user_id_string=rs.getString("_user_id_string");
				beans._user_amount=rs.getInt("_user_amount");
				beans._user_birthday=rs.getTimestamp("_user_birthday");
				beans._user_builder_date=rs.getTimestamp("_user_builder_date");
				beans._user_id_card=rs.getString("_user_id_card");

				beans._user_name=rs.getString("_user_name");
				beans._user_pwd=rs.getString("_user_pwd");
				beans._user_tel=rs.getString("_user_tel");
				beans._user_xiaofei_times=rs.getInt("_user_xiaofei_times");
				beans._user_xiaofei_times_en=rs.getInt("_user_xiaofei_times_en");
				beans.groupid=rs.getInt("groupid");
				beans.id=rs.getInt("id");
				beans.sex_type=rs.getString("sex_type");
				beans._user_disable=rs.getInt("_user_disable");
				beans._user_xiaofei_rest_times=rs.getInt("_user_xiaofei_rest_times");
				beans.last_charge_amount=rs.getInt("last_charge_amount");
				beans.last_jiaoyi_amount=rs.getInt("last_jiaoyi_amount");
				beans.last_charge_time=rs.getTimestamp("last_charge_time");
				beans.last_jiaoyi_time=rs.getTimestamp("last_jiaoyi_time");
				beans._user_max_credit_limit=rs.getInt("_user_max_credit_limit");
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
		return beans;
	}
	public static CustomerBean getCustomerBeanById(int id) 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		CustomerBean beans=null;
	
		String sql ="select top(1) [id],[_user_id_string], [_user_name], [_user_pwd], [_user_tel], [_user_id_card],"
				+ " [_user_birthday], [_user_amount], [_user_builder_date], [groupid], [_user_address], [sex_type], [_user_xiaofei_times_en], "
				+ "[_user_xiaofei_times],[_user_disable],[_user_xiaofei_rest_times],[last_jiaoyi_amount],[last_jiaoyi_time],"
				+ "[last_charge_time],[last_charge_amount],[_user_max_credit_limit] from userdata where id=?";
		
		Connection conn=ConnManager.getConnection(CN);
		try 
		{
			ps=conn.prepareStatement(sql);
			int i=1;
			ps.setInt(i++,id);
			rs = ps.executeQuery();
			if(rs.next())
			{
				beans=new CustomerBean();
				beans._user_address=rs.getString("_user_address");
				beans._user_id_string=rs.getString("_user_id_string");
				beans._user_amount=rs.getInt("_user_amount");
				beans._user_birthday=rs.getTimestamp("_user_birthday");
				beans._user_builder_date=rs.getTimestamp("_user_builder_date");
				beans._user_id_card=rs.getString("_user_id_card");

				beans._user_name=rs.getString("_user_name");
				beans._user_pwd=rs.getString("_user_pwd");
				beans._user_tel=rs.getString("_user_tel");
				beans._user_xiaofei_times=rs.getInt("_user_xiaofei_times");
				beans._user_xiaofei_times_en=rs.getInt("_user_xiaofei_times_en");
				beans.groupid=rs.getInt("groupid");
				beans.id=rs.getInt("id");
				beans.sex_type=rs.getString("sex_type");
				beans._user_disable=rs.getInt("_user_disable");
				beans._user_xiaofei_rest_times=rs.getInt("_user_xiaofei_rest_times");
				beans.last_charge_amount=rs.getInt("last_charge_amount");
				beans.last_jiaoyi_amount=rs.getInt("last_jiaoyi_amount");
				beans.last_charge_time=rs.getTimestamp("last_charge_time");
				beans.last_jiaoyi_time=rs.getTimestamp("last_jiaoyi_time");
				beans._user_max_credit_limit=rs.getInt("_user_max_credit_limit");
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
		return beans;
	}
	public void Add() {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="INSERT INTO [userdata] (_user_id_string, [_user_name], [_user_pwd], "
				+ "[_user_tel], [_user_id_card], [_user_birthday], [_user_amount], "
				+ "[_user_builder_date], [groupid], [_user_address], [sex_type], [_user_xiaofei_times_en], "
				+ "[_user_xiaofei_times],[_user_disable],[last_jiaoyi_amount],[last_jiaoyi_time],"
				+ "[last_charge_time],[last_charge_amount],[_user_max_credit_limit]) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?,?,?)";
		
		Connection conn=ConnManager.getConnection(CN);
		
		try {
			ps=conn.prepareStatement(sql);
				int i=1;
				ps.setString(i++,this.get_user_id_string());
				ps.setString(i++,this.get_user_name());
				ps.setString(i++,this.get_user_pwd());
				ps.setString(i++,this.get_user_tel());
				ps.setString(i++,this.get_user_id_card());
				ps.setTimestamp(i++,this.get_user_birthday());
				ps.setInt(i++,this.get_user_amount());
				ps.setTimestamp(i++,this.get_user_builder_date());

				ps.setInt(i++,this.getGroupid());
				ps.setString(i++,this.get_user_address());
				ps.setString(i++,this.getSex_type());
				ps.setInt(i++,this.get_user_xiaofei_times_en());
				ps.setInt(i++,this.get_user_xiaofei_times());
				ps.setInt(i++,this.get_user_disable());
				ps.setInt(i++, this.getLast_jiaoyi_amount());
				ps.setTimestamp(i++, this.getLast_jiaoyi_time());
				ps.setTimestamp(i++, this.getLast_charge_time());
				ps.setInt(i++, this.getLast_charge_amount());
				ps.setInt(i++, this.get_user_max_credit_limit());
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
	
	public void Update() {
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="update [userdata] set [_user_id_string] = ?,[_user_name] = ?,[_user_pwd] = ?,[_user_tel] = ?,"
				+ "[_user_id_card] = ?,[_user_birthday] = ?,[_user_amount] = ?,[_user_builder_date] = ?,[groupid] = ?,"
				+ "[_user_address] =?,[sex_type] = ?,[_user_xiaofei_times_en] = ?,[_user_xiaofei_times] =?,[_user_disable] =?,"
				+ "[_user_xiaofei_rest_times] = ?,[last_jiaoyi_amount]=?,[last_jiaoyi_time]=?,[last_charge_time]=?,[last_charge_amount]=?,"
				+ "[_user_max_credit_limit]=? where id=?";
		
		Connection conn=ConnManager.getConnection(CN);
		
		try {
			ps=conn.prepareStatement(sql);
				int i=1;
				ps.setString(i++,this.get_user_id_string());
				ps.setString(i++,this.get_user_name());
				ps.setString(i++,this.get_user_pwd());
				ps.setString(i++,this.get_user_tel());
				ps.setString(i++,this.get_user_id_card());
				ps.setTimestamp(i++,this.get_user_birthday());
				ps.setInt(i++,this.get_user_amount());
				ps.setTimestamp(i++,this.get_user_builder_date());

				ps.setInt(i++,this.getGroupid());
				ps.setString(i++,this.get_user_address());
				ps.setString(i++,this.getSex_type());
				ps.setInt(i++,this.get_user_xiaofei_times_en());
				ps.setInt(i++,this.get_user_xiaofei_times());
				ps.setInt(i++,this.get_user_disable());
				ps.setInt(i++, this.get_user_xiaofei_rest_times());
				ps.setInt(i++, this.getLast_jiaoyi_amount());
				ps.setTimestamp(i++, this.getLast_jiaoyi_time());
				ps.setTimestamp(i++, this.getLast_charge_time());
				ps.setInt(i++, this.getLast_charge_amount());
				ps.setInt(i++, this.get_user_max_credit_limit());
				
				ps.setInt(i++, this.getId());
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
	
	public static void SetRestTimesToMaxLimitTimes() 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="update [userdata] set _user_xiaofei_rest_times=_user_xiaofei_times";
		Connection conn=ConnManager.getConnection(CN);
		
		try {
			ps=conn.prepareStatement(sql);
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
	public int Cusume(int credit) 
	{
		if(_user_disable==1)
		{
			//如果被禁用，
			return CUSTOMER_DISABLE;
		}

		if(_user_xiaofei_times_en==1)
		{
			_user_xiaofei_rest_times--;
			if(_user_xiaofei_rest_times<=0)
			{
				return CUSTOMER_NOT_ENOUGH_TIMES;
			}
		}

		if(credit>_user_amount)
		{
			return CUSTOMER_NOT_ENOUGH_BALANCE;
		}
		if(credit<0)
		{
			return CUSTOMER_CREDIT_VALUE_ERR;
		}
		if(credit>_user_max_credit_limit)
		{
			return CUSTOMER_CREDIT_OVER_LIMIT;
		}
		
		_user_amount-=credit;
		
		last_jiaoyi_amount=credit;
		last_jiaoyi_time=new Timestamp(System.currentTimeMillis());
		this.Update();
		return CUSTOMER_OK;
	}


	public int Cusume2(int credit) 
	{
		if(_user_disable==1)
		{
			//如果被禁用，
			return CUSTOMER_DISABLE;
		}


		if(credit>_user_amount)
		{
			return CUSTOMER_NOT_ENOUGH_BALANCE;
		}
		if(credit<0)
		{
			return CUSTOMER_CREDIT_VALUE_ERR;
		}
		if(credit>_user_max_credit_limit)
		{
			return CUSTOMER_CREDIT_OVER_LIMIT;
		}
		
		_user_amount-=credit;
		
		last_jiaoyi_amount=credit;
		last_jiaoyi_time=new Timestamp(System.currentTimeMillis());
		this.Update();
		return CUSTOMER_OK;
	}
}
