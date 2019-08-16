package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.clsConst;
import com.ado.SqlADO;
import com.alipay.AlipayRefund;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.tools.ToolBox;

import beans.RefundBean;
import beans.TradeBean;
import beans.UserBean;
import beans.VenderBean;
import beans.clsGroupBean;

import weixin.popular.bean.paymch.SecapiPayRefundResult;
import wx.pay.util.WxRefund;


/**
 * Servlet implementation class PayBack
 */
@WebServlet("/PayBack")
public class PayBack extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PayBack() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter pw=response.getWriter();
		UserBean ub=(UserBean)request.getSession().getAttribute("usermessage");		
		if(ub==null)
		{
			pw.print("您没有登录或无权访问！请联系管理员！");
			return;
		}
		
		if(!ub.AccessAble(UserBean.FUNID_CAN_ACCESS_WEB))
		{
			pw.print("不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ACCESS_WEB]+"，您没有权限");
			return;
		}
	    
	    
	    if(!ub.AccessAble(UserBean.FUNID_CAN_REFUND))
		{
			pw.print("不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_REFUND]+"，您没有权限");
			return;
		}

	    String out_trade_id =ToolBox.filter(request.getParameter("out_trade_id"));
	    String pwd=ToolBox.filter(request.getParameter("pwd"));
	    
	    if(!ub.getAdminpassword().equals(ToolBox.getMd5(pwd)))
	    {
			pw.print("密码错误，请重新输入密码");
			return;
	    }
	    
	    TradeBean tb= SqlADO.getTradeBean(out_trade_id);
	    if(tb==null)
	    {
			pw.print("无效的交易编号");
			return;
	    }
	    if(tb.getCardinfo()==null)
	    {
			pw.print("没有支付的交易订单");
			return;
	    }
	    if(tb.getCardinfo().equals(""))
	    {
			pw.print("没有支付的交易订单");
			return;
	    }
	    if(tb.getHas_jiesuan()==1)
	    {
			pw.print("订单已经结算，不能再退款");
			return;
	    }
	    if((tb.getReceivetime().before(new Timestamp(System.currentTimeMillis()-24*3600*1000)))&&(!ub.AccessAble(UserBean.FUNID_CAN_PAY_BACK_NO_LIMIT_TIME)))
	    {
			pw.print("订单已过退款期");
			return;
	    }
	    
	    
	    int vid =tb.getGoodmachineid();
	    if(ub.CanAccessSeller(vid))
	    {	
	    	VenderBean vb=SqlADO.getVenderBeanByid(vid);
	    	if(tb.getTradetype()==clsConst.TRADE_TYPE_AL_QR)
	    	{
//	    		AlipayRefund arefund=new AlipayRefund();
//	    		arefund.setOperator_id(ub.getId()+"-"+ub.getAdminusername());
//	    		arefund.setOut_request_no(ToolBox.getTimeString()+String.format("%06d", ToolBox.getRandomNumber()));
//	    		arefund.setOut_trade_no(tb.getOrderid());
//	    		arefund.setRefund_amount(tb.getPrice()/100.0);
//	    		arefund.setRefund_reason("人工退款");
//	    		arefund.setStore_id(String.format("%s", tb.getGoodmachineid()));
//	    		arefund.setTerminal_id(String.format("%s", tb.getGoodmachineid()));
//	    		arefund.setTrade_no("");
//	    		arefund.setIssuccess(0);
//	    		AlipayTradeRefundResponse ret=arefund.Refund(clsGroupBean.getGroup(vb.getGroupid()));
//
//	    		if(ret!=null)
//	    		{
//	    			if(ret.isSuccess())
//	    			{
//		    			arefund.setMsg("退款成功");
//		    			arefund.setIssuccess(1);
//		    			pw.print(arefund.getMsg());
//		    			/*更新交易，设置为结算和设置为已经退款*/
//		    			tb.setHas_feeback(1);
//		    			tb.setHas_jiesuan(1);
//		    			SqlADO.updateTradeBean(tb);
//		    			
//	    			}
//	    			else
//	    			{
//		    			arefund.setMsg("退款失败，"+ret.getMsg());
//		    			pw.print(arefund.getMsg());
//	    			}
//	    		}
//	    		else
//	    		{
//	    			arefund.setMsg("支付宝服务器返回失败");
//	    			pw.print(arefund.getMsg());
//	    			
//	    		}
//	    		refundBean=new RefundBean();
//	    		refundBean.setGoodsname(tb.getGoodsName());
//	    		refundBean.setGoodsroadid(tb.getGoodroadid());
//	    		refundBean.setIssuccess(arefund.getIssuccess());
//	    		refundBean.setOp_user(arefund.getOperator_id());
//	    		refundBean.setOrderid(arefund.getOut_trade_no());
//	    		refundBean.setRefund_amount(tb.getPrice());
//	    		refundBean.setRefundid(arefund.getOut_request_no());
//	    		refundBean.setRefundtime(new Date());
//	    		refundBean.setRet_msg(arefund.getMsg());
//	    		refundBean.setTerminalid(tb.getGoodmachineid());
//	    		refundBean.setTrade_id(tb.getId());
//	    		refundBean.setTrade_type(tb.getTradetype());
//	    		refundBean.setGroupid(vb.getGroupid());
//	    		SqlADO.AddRefundRecordToDb(refundBean);
	    		
	    		AlipayRefund arefund = AlipayRefund.PayBack(tb,ub,vb.getGroupid(),"人工退款");
	    		SqlADO.updateTradeBean(tb);
	    		 pw.print(arefund.getMsg());
	    		return;
	    	}
	    	else if(tb.getTradetype()==clsConst.TRADE_TYPE_WX_QR)
	    	{
	    		
//	    		WxRefund wxrefound=new WxRefund();
//	    		wxrefound.setDevice_info(String.format("%s", tb.getGoodmachineid()));
//	    		wxrefound.setNonce_str(ToolBox.getMd5(ToolBox.MakeTradeID(0)));
//	    		wxrefound.setOp_user_id(ub.getId()+"-"+ub.getAdminusername());
//	    		wxrefound.setOut_refund_no(ToolBox.getTimeString()+String.format("%06d", ToolBox.getRandomNumber()));
//	    		wxrefound.setOut_trade_no(tb.getOrderid());
//	    		wxrefound.setRefund_fee(tb.getPrice());
//	    		wxrefound.setRefund_fee_type("CNY");
//	    		wxrefound.setTotal_fee(tb.getPrice());
//	    		//wxrefound.setTransaction_id(n);
//	    		SecapiPayRefundResult ret= wxrefound.Refund(clsGroupBean.getGroup(vb.getGroupid()));
//	    		if(ret!=null)
//	    		{
//	    			//System.out.println(ret.getReturn_msg());
//	    			if(ret.getResult_code().equals("SUCCESS"))
//	    			{
//	    				wxrefound.setMsg("退款成功");
//	    				wxrefound.setIssuccess(1);
//		    			pw.print(wxrefound.getMsg());
//		    			
//		    			/*更新交易，设置为结算和设置为已经退款*/
//		    			tb.setHas_feeback(1);
//		    			tb.setHas_jiesuan(1);
//		    			SqlADO.updateTradeBean(tb);
//	    			}
//	    			else
//	    			{
//	    				wxrefound.setMsg("退款失败，"+ret.getErr_code_des());
//		    			pw.print(wxrefound.getMsg());
//	    			}
//	    		}
//	    		else
//	    		{
//	    			wxrefound.setMsg("微信服务器返回失败");
//	    			pw.print(wxrefound.getMsg());
//	    		}
//	    		
//	    		refundBean=new RefundBean();
//	    		refundBean.setGoodsname(tb.getGoodsName());
//	    		refundBean.setGoodsroadid(tb.getGoodroadid());
//	    		refundBean.setIssuccess(wxrefound.getIssuccess());
//	    		refundBean.setOp_user(wxrefound.getOp_user_id());
//	    		refundBean.setOrderid(wxrefound.getOut_trade_no());
//	    		refundBean.setRefund_amount(tb.getPrice());
//	    		refundBean.setRefundid(wxrefound.getOut_refund_no());
//	    		refundBean.setRefundtime(new Date());
//	    		refundBean.setRet_msg(wxrefound.getMsg());
//	    		refundBean.setTerminalid(tb.getGoodmachineid());
//	    		refundBean.setTrade_id(tb.getId());
//	    		refundBean.setTrade_type(tb.getTradetype());
//	    		refundBean.setGroupid(vb.getGroupid());
//	    		SqlADO.AddRefundRecordToDb(refundBean);
	    		
	    		WxRefund wxrefound = WxRefund.PayBack(tb,ub,vb.getGroupid(),"人工退款");
	    		SqlADO.updateTradeBean(tb);
				pw.print(wxrefound.getMsg());

	    		
	    		return;
	    	}
			return;
	    }
	    else
	    {
			pw.print("不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_VIEW_VENDER]+"，您没有权限");
			return;
	    }
	}

}
