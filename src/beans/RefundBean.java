package beans;

import java.util.Date;

public class RefundBean 
{
	private int id;
	private String orderid;
	private int trade_id;
	private Date refundtime;
	private int terminalid;
	private int goodsroadid;
	private String goodsname;
	private String ret_msg;
	private String refundid;
	private int issuccess;
	private int trade_type;
	
	private int groupid;
	
	/**
	 * 操作人员
	 */
	private String op_user; 
	
	/**
	 * 退币金额，单位分
	 */
	private int Refund_amount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public int getTrade_id() {
		return trade_id;
	}

	public void setTrade_id(int trade_id) {
		this.trade_id = trade_id;
	}

	public Date getRefundtime() {
		return refundtime;
	}

	public void setRefundtime(Date refundtime) {
		this.refundtime = refundtime;
	}

	public int getTerminalid() {
		return terminalid;
	}

	public void setTerminalid(int terminalid) {
		this.terminalid = terminalid;
	}

	public int getGoodsroadid() {
		return goodsroadid;
	}

	public void setGoodsroadid(int goodsroadid) {
		this.goodsroadid = goodsroadid;
	}

	public String getGoodsname() {
		return goodsname;
	}

	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}

	public String getRet_msg() {
		return ret_msg;
	}

	public void setRet_msg(String ret_msg) {
		this.ret_msg = ret_msg;
	}

	public String getRefundid() {
		return refundid;
	}

	public void setRefundid(String refundid) {
		this.refundid = refundid;
	}

	public int getIssuccess() {
		return issuccess;
	}

	public void setIssuccess(int issuccess) {
		this.issuccess = issuccess;
	}

	public int getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(int trade_type) {
		this.trade_type = trade_type;
	}

	public String getOp_user() {
		return op_user;
	}

	public void setOp_user(String op_user) {
		this.op_user = op_user;
	}

	public int getRefund_amount() {
		return Refund_amount;
	}

	public void setRefund_amount(int refund_amount) {
		Refund_amount = refund_amount;
	}

	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}
	
	
	
	
}
