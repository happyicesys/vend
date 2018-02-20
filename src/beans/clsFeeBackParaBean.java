package beans;

import java.util.Vector;

public class clsFeeBackParaBean {
	private static Vector<clsFeeBackParaBean> Lst= new Vector<clsFeeBackParaBean>(); 
	private VenderBean Vender;
	private TradeBean Trade;
	private String Reason;
	
	public clsFeeBackParaBean(TradeBean tradeBean, VenderBean vb, String string) {
		
		super();
		Trade=tradeBean;
		Vender=vb;
		setReason(string);
	}
	public static Vector<clsFeeBackParaBean> getLst() {
		return Lst;
	}
	public static void setLst(Vector<clsFeeBackParaBean> lst) {
		Lst = lst;
	}
	public VenderBean getVender() {
		return Vender;
	}
	public void setVender(VenderBean vender) {
		Vender = vender;
	}
	public TradeBean getTrade() {
		return Trade;
	}
	public void setTrade(TradeBean trade) {
		Trade = trade;
	}
	public String getReason() {
		return Reason;
	}
	public void setReason(String reason) {
		Reason = reason;
	}

	
}
