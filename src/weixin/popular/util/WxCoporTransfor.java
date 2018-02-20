package weixin.popular.util;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.apache.taglibs.standard.lang.jstl.BooleanLiteral;

import com.clsConst;
import com.ado.SqlADO;
import com.tools.ToolBox;

import beans.ClsSaleStatisticData;
import beans.PortBean;
import beans.TradeBean;
import beans.UserBean;
import beans.VenderBean;
import beans.clsGoodsBean;
import beans.clsGroupBean;
import weixin.popular.api.PayMchAPI;

import weixin.popular.bean.paymch.Transfers;
import weixin.popular.bean.paymch.TransfersResult;
import weixin.popular.client.LocalHttpClient;

public class WxCoporTransfor
{

	private String payment_no;

	private String payment_time;
	private String device_info;

	private String nonce_str;

	private String partner_trade_no;

	private String openid;
	private int adminid;
	private String check_name;
	
	public String getPayment_no() {
		return payment_no;
	}

	public void setPayment_no(String payment_no) {
		this.payment_no = payment_no;
	}

	public String getPayment_time() {
		return payment_time;
	}

	public void setPayment_time(String payment_time) {
		this.payment_time = payment_time;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getPartner_trade_no() {
		return partner_trade_no;
	}

	public void setPartner_trade_no(String partner_trade_no) {
		this.partner_trade_no = partner_trade_no;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getCheck_name() {
		return check_name;
	}

	public void setCheck_name(String check_name) {
		this.check_name = check_name;
	}

	public String getRe_user_name() {
		return re_user_name;
	}

	public void setRe_user_name(String re_user_name) {
		this.re_user_name = re_user_name;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}


	private String re_user_name;

	private String amount;

	private String desc;
	
	//private int groupid;
	clsGroupBean groupBean;


	protected String err_code;
	
	private String rescode;

	protected String err_code_des;
	public String getErr_code() {
		return err_code;
	}

	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}

	public String getErr_code_des() {
		return err_code_des;
	}

	public void setErr_code_des(String err_code_des) {
		this.err_code_des = err_code_des;
	}
	
	public clsGroupBean getGroupBean() {
		return groupBean;
	}

	public void setGroupBean(clsGroupBean groupBean) {
		this.groupBean = groupBean;
	}
	
	public String getRescode() {
		return rescode;
	}

	public void setRescode(String rescode) {
		this.rescode = rescode;
	}
	
	public int getAdminid() {
		return adminid;
	}

	public void setAdminid(int adminid) {
		this.adminid = adminid;
	}
	
	public static void main(String[] a) 
	{
		WxCoporTransfor t=new WxCoporTransfor();
		
		t.amount="100";
		t.check_name="FORCE_CHECK";
		t.desc="佣金";
		t.device_info="10";
		t.groupBean=clsGroupBean.getGroup(3);
		t.nonce_str=ToolBox.getMd5(ToolBox.MakeTradeID(0));
		t.openid="o_2NJsz95zYLTg-84UtSPZ8rIfH8";
		//t.openid="oBOZ1uB2qp-eqjlK-gHhs6Cl1d8k";
		t.partner_trade_no=ToolBox.getTimeString()+String.format("%06d", ToolBox.getRandomNumber());
		t.re_user_name="周文静";
		TransfersResult ret=t.transfor();
		
		System.out.println( ret.getReturn_msg());
		System.out.println( ret.getErr_code());
		System.out.println( ret.getErr_code_des());
	}

	public static boolean CreateTransfor(TradeBean tradeBean,clsGroupBean groupBean,UserBean ub,double rate)
	{
		
			if(ub==null)
			{
				return false;
			}
			String m_wxopenid=ub.getWx_openid();
			if(m_wxopenid==null)
			{
				return false;
			}
			if(m_wxopenid.trim().equals(""))
			{
				return false;
			}
			String m_adminname=ub.getAdminname();
			if(m_adminname==null)
			{
				return false;
			}
			if(m_adminname.trim().equals(""))
			{
				return false;
			}
		
		WxCoporTransfor t=new WxCoporTransfor();
		
		t.amount=String.valueOf((int)Math.rint(tradeBean.getPrice()*rate));
		t.check_name="FORCE_CHECK";
		t.desc=String.format("%d号机%s号货道销售收入",tradeBean.getGoodmachineid() ,tradeBean.getInneridname());
		t.device_info=String.format("%d号机%s号",tradeBean.getGoodmachineid() ,tradeBean.getInneridname());;
		t.groupBean=groupBean;
		t.nonce_str=ToolBox.getMd5(ToolBox.MakeTradeID(0));
		t.openid=ub.getWx_openid();
		t.partner_trade_no=ToolBox.getTimeString()+String.format("%06d", ToolBox.getRandomNumber());
		t.re_user_name=ub.getAdminname();
		TransfersResult ret=t.transfor();
		//System.out.println( ret.getReturn_msg());
		//System.out.println( ret.getErr_code());
		//System.out.println( ret.getErr_code_des());
		t.payment_no=ret.getPayment_no();
		t.payment_time=ret.getPayment_time();
		t.err_code=ret.getErr_code();
		t.err_code_des=ret.getErr_code_des();
		t.rescode=ret.getResult_code();
		t.AddTransforLog();
		if(ret.getResult_code()==null)
		{
			return false;
		}
		return (ret.getResult_code().equals("SUCCESS"));
		
	}
	

	public static boolean CreateTransfor(VenderBean venderBean,clsGroupBean groupBean,UserBean ub,int amount,double rate)
	{
			if(ub==null)
			{
				return false;
			}
			String m_wxopenid=ub.getWx_openid();
			if(m_wxopenid==null)
			{
				return false;
			}
			if(m_wxopenid.trim().equals(""))
			{
				return false;
			}
			String m_adminname=ub.getAdminname();
			if(m_adminname==null)
			{
				return false;
			}
			if(m_adminname.trim().equals(""))
			{
				return false;
			}
		
		WxCoporTransfor t=new WxCoporTransfor();
		
		int vamount=(int)Math.rint(amount*rate);
		if(vamount<100)
		{
			return false;
		}
		t.amount=String.valueOf(vamount);
		
		t.check_name="FORCE_CHECK";
		t.desc=String.format("%d号机销售收入",venderBean.getId() );
		t.device_info=String.format("%d号机",venderBean.getId() );;
		t.groupBean=groupBean;
		t.nonce_str=ToolBox.getMd5(ToolBox.MakeTradeID(0));
		t.openid=ub.getWx_openid();
		t.partner_trade_no=ToolBox.getTimeString()+String.format("%06d", ToolBox.getRandomNumber());
		t.re_user_name=ub.getAdminname();
		TransfersResult ret=t.transfor();
		t.payment_no=ret.getPayment_no();
		t.payment_time=ret.getPayment_time();
		t.err_code=ret.getErr_code();
		t.err_code_des=ret.getErr_code_des();
		t.rescode=ret.getResult_code();
		t.AddTransforLog();
		if(ret.getResult_code()==null)
		{
			return false;
		}
		return (ret.getResult_code().equals("SUCCESS"));
		
	}
	
	

	public static String CreateTransfor(clsGroupBean groupBean,UserBean ub,int vamount,String des,String deviceinfo)
	{
			if(ub==null)
			{
				return "无效用户";
			}
			String m_wxopenid=ub.getWx_openid();
			if(m_wxopenid==null)
			{
				return "OPENID无效";
			}
			if(m_wxopenid.trim().equals(""))
			{
				return "OPENID为空";
			}
			String m_adminname=ub.getAdminname();
			if(m_adminname==null)
			{
				return "用户姓名无效";
			}
			if(m_adminname.trim().equals(""))
			{
				return "用户姓名为空";
			}
		
		WxCoporTransfor t=new WxCoporTransfor();
		
		
		if(vamount<100)
		{
			return "金额必须大于1元";
		}
		t.amount=String.valueOf(vamount);
		
		t.check_name="FORCE_CHECK";
		if(des==null)
		{
			return "描述无效";
		}
		if(des.equals(""))
		{
			return "描述为空";
		}
		t.desc=des;
		t.device_info=deviceinfo;
		t.groupBean=groupBean;
		t.nonce_str=ToolBox.getMd5(ToolBox.MakeTradeID(0));
		t.openid=ub.getWx_openid();
		t.partner_trade_no=ToolBox.getTimeString()+String.format("%06d", ToolBox.getRandomNumber());
		t.re_user_name=ub.getAdminname();
		TransfersResult ret=t.transfor();
		t.payment_no=ret.getPayment_no();
		t.payment_time=ret.getPayment_time();
		t.err_code=ret.getErr_code();
		t.err_code_des=ret.getErr_code_des();
		t.rescode=ret.getResult_code();
		t.AddTransforLog();
		if(ret.getResult_code()==null)
		{
			return ret.getErr_code_des();
		}
		return ret.getResult_code();
		
	}
	private void AddTransforLog() {
		// TODO Auto-generated method stub
		System.out.println(this.rescode);
		System.out.println(this.err_code_des);
		System.out.println(this.err_code);
		SqlADO.AddTransforLog(this);
	}

	public TransfersResult transfor() 
    {
		
    	String mch_id=groupBean.getWx_mch_id();
    	LocalHttpClient.initMchKeyStore(mch_id,"D:/key/"+mch_id+".p12");
    	Transfers trans=new Transfers();
    	trans.setCheck_name(this.getCheck_name());
    	trans.setAmount(this.getAmount());
    	trans.setDesc(this.getDesc());
    	trans.setDevice_info(this.getDevice_info());
    	
    	trans.setMch_appid(groupBean.getWx_appid());
    	trans.setMchid(mch_id);
    	
    	trans.setNonce_str(this.getNonce_str());
    	trans.setOpenid(this.getOpenid());
    	trans.setPartner_trade_no(this.getPartner_trade_no());
    	trans.setRe_user_name(this.getRe_user_name());
    	
    	trans.setSpbill_create_ip(groupBean.getServerIp());
    	
    	return PayMchAPI.mmpaymkttransfersPromotionTransfers(trans,groupBean.getWx_key());
    	
    }

	public static void AutoTransfer() {
		List<VenderBean> mLst=SqlADO.getVenderBeanList();
		List<clsGroupBean> mGroupLst=clsGroupBean.getGroupLst();
		clsGroupBean mgroupBean=null;
		java.sql.Date edata=new Date(System.currentTimeMillis());
		Calendar c= Calendar.getInstance();
		//beginDate =new Date(c.getTimeInMillis());
		c.setTimeInMillis(edata.getTime());
		c.add(Calendar.DAY_OF_MONTH, -1);
		java.sql.Date beginDate=new Date(c.getTimeInMillis());
		for (VenderBean venderBean : mLst) {
			if(venderBean==null)
			{
				continue;
			}
			if(venderBean.getAutoTransfer()==0)
			{
				continue;
			}
			int groupid=venderBean.getGroupid();
			
			mgroupBean=null;
			for (clsGroupBean groupBean : mGroupLst) {
				if(groupBean==null)
				{
					continue;
				}
				if(groupid==groupBean.getId())
				{
					mgroupBean=groupBean;
					break;
				}
			}
			if(mgroupBean==null)
			{
				continue;
			}
			System.out.println(4);
			if(mgroupBean.getAutoTransfer()==0)
			{
				continue;
			}
			System.out.println(venderBean.getAdminId());
			if(venderBean.getAdminId()==0)
			{
				continue;
			}
			UserBean ub=UserBean.getUserBeanById(venderBean.getAdminId());
			//计算总金额
			ClsSaleStatisticData salestatistic_al= SqlADO.getSalesStatisticDataFromDb(
					beginDate,edata,String.valueOf(venderBean.getId()),clsConst.TRADE_TYPE_AL_QR,2);
			ClsSaleStatisticData salestatistic_wx= SqlADO.getSalesStatisticDataFromDb(
					beginDate,edata,String.valueOf(venderBean.getId()),clsConst.TRADE_TYPE_WX_QR,2);

			//if(salestatistic_al.getM_credit()+salestatistic_wx.getM_credit())
			
			boolean ret= CreateTransfor(venderBean, 
					mgroupBean, 
					ub,
					salestatistic_al.getM_credit()+salestatistic_wx.getM_credit(),
					venderBean.getAutoTransferRation()
					);
			if(!ret)
			{
				System.out.println(String.format("%d号售货机转款失败", venderBean.getId()));
			}
			else
			{
				//将交易设置为已结算
				//SqlADO.getTradeList(str, page, max);
				//SqlADO.updateTradeBeanHasJieSuan(1);
				SqlADO.SetTradeJiesuan(beginDate,edata,String.valueOf(venderBean.getId()));
				System.out.println(String.format("%d号售货机转款完成", venderBean.getId()));
				
			}
		}
	}
}
