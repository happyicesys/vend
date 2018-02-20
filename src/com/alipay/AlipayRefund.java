/**
 * 
 */
package com.alipay;

import java.util.Date;

import com.ado.SqlADO;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.factory.AlipayAPIClientFactory;
import com.tools.ToolBox;

import beans.RefundBean;
import beans.TradeBean;
import beans.UserBean;
import beans.clsGroupBean;

/**
 * @author Administrator
 *
 */
public class AlipayRefund {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AlipayRefund arefund=new AlipayRefund();
		arefund.operator_id="周文静";
		arefund.out_request_no="123";
		arefund.out_trade_no="2016060210242854020130127";
		arefund.refund_amount=1.00;
		arefund.refund_reason="退款";
		arefund.store_id="0001";
		arefund.terminal_id="0001";
		arefund.trade_no="";
		AlipayTradeRefundResponse response=arefund.Refund(clsGroupBean.getGroup(3));

		if(response!=null)
		{
			System.out.println(response.getMsg());
		}
		else
		{
			
		}
	}
	
	
	
	private String out_trade_no;
	private String trade_no;
	private double refund_amount;
	private String refund_reason;
	private String out_request_no;
	private String operator_id;
	private String store_id;
    private String terminal_id;
    
    public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getIssuccess() {
		return issuccess;
	}

	public void setIssuccess(int issuccess) {
		this.issuccess = issuccess;
	}


	private String msg;
    private int issuccess;
	
	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public double getRefund_amount() {
		return refund_amount;
	}

	public void setRefund_amount(double refund_amount) {
		this.refund_amount = refund_amount;
	}

	public String getRefund_reason() {
		return refund_reason;
	}

	public void setRefund_reason(String refund_reason) {
		this.refund_reason = refund_reason;
	}

	public String getOut_request_no() {
		return out_request_no;
	}

	public void setOut_request_no(String out_request_no) {
		this.out_request_no = out_request_no;
	}

	public String getOperator_id() {
		return operator_id;
	}

	public void setOperator_id(String operator_id) {
		this.operator_id = operator_id;
	}

	public String getStore_id() {
		return store_id;
	}

	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

	public String getTerminal_id() {
		return terminal_id;
	}

	public void setTerminal_id(String terminal_id) {
		this.terminal_id = terminal_id;
	}

	public AlipayTradeRefundResponse Refund(clsGroupBean groupBean)
	{
		AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(groupBean);
		
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizContent(this.toJsonString());
		AlipayTradeRefundResponse response=null;
		try {
			response = alipayClient.execute(request);
		} catch (AlipayApiException e) {
			System.err.println("ErrCode:"+e.getErrCode());
			System.err.println("ErrMsg:"+e.getErrMsg());
			e.printStackTrace();
		}
		return response;
	}

	private String toJsonString() {
		return JSON.toJSONString(this);
	}

	public static AlipayRefund PayBack(TradeBean tb,UserBean ub, int groupid,String reason) {
		AlipayRefund arefund=new AlipayRefund();
		if(ub!=null)
		{
			arefund.setOperator_id(ub.getId()+"-"+ub.getAdminusername());
		}
		else
		{
			arefund.setOperator_id("平台自动退款");
		}
		arefund.setOut_request_no(ToolBox.getTimeString()+String.format("%06d", ToolBox.getRandomNumber()));
		arefund.setOut_trade_no(tb.getOrderid());
		arefund.setRefund_amount(tb.getPrice()/100.0);
		arefund.setRefund_reason(reason);
		arefund.setStore_id(String.format("%s", tb.getGoodmachineid()));
		arefund.setTerminal_id(String.format("%s", tb.getGoodmachineid()));
		arefund.setTrade_no("");
		arefund.setIssuccess(0);
		AlipayTradeRefundResponse ret=arefund.Refund(clsGroupBean.getGroup(groupid));

		if(ret!=null)
		{
			if(ret.isSuccess())
			{
    			arefund.setMsg("退款成功");
    			arefund.setIssuccess(1);
    			//pw.print(arefund.getMsg());
    			/*更新交易，设置为结算和设置为已经退款*/
    			tb.setHas_feeback(1);
    			tb.setHas_jiesuan(1);
    			//SqlADO.updateTradeBean(tb);
			}
			else
			{
    			arefund.setMsg("退款失败，"+ret.getMsg());
    			//pw.print(arefund.getMsg());
			}
		}
		else
		{
			arefund.setMsg("支付宝服务器返回失败");
			//pw.print(arefund.getMsg());
		}
		RefundBean refundBean = new RefundBean();
		refundBean.setGoodsname(tb.getGoodsName());
		refundBean.setGoodsroadid(tb.getGoodroadid());
		refundBean.setIssuccess(arefund.getIssuccess());
		refundBean.setOp_user(arefund.getOperator_id());
		refundBean.setOrderid(arefund.getOut_trade_no());
		refundBean.setRefund_amount(tb.getPrice());
		refundBean.setRefundid(arefund.getOut_request_no());
		refundBean.setRefundtime(new Date());
		refundBean.setRet_msg(arefund.getMsg());
		refundBean.setTerminalid(tb.getGoodmachineid());
		refundBean.setTrade_id(tb.getId());
		refundBean.setTrade_type(tb.getTradetype());
		refundBean.setGroupid(groupid);
		SqlADO.AddRefundRecordToDb(refundBean);
		return arefund;
	}

}
