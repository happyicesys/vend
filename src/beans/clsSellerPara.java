package beans;

public class clsSellerPara {

	private int _id;
	private int _ussdport=8001;
	private int _sellerport=8113;
	private int _outgoodsovertime=60;
	private int _twiceintervaltime=6;
	private boolean _allstop= false;
	private int _maxtimes=3;
	private int _actioninterval=30;
	private int _smsthreadcount=5;
	private int _copetradethreadcount=5;
	private int _maxseller=200;
	private String _pwd;
	private int _umpaythreadcount=5;
	private int _endtradethreadcount=5;
	private int _payovertime=90000;
	private int _feebackovertime=120000;
	
	private static clsSellerPara instance=null;
	
	
	
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public int get_ussdport() {
		return _ussdport;
	}
	public void set_ussdport(int _ussdport) {
		this._ussdport = _ussdport;
	}
	public int get_sellerport() {
		return _sellerport;
	}
	public void set_sellerport(int _sellerport) {
		this._sellerport = _sellerport;
	}
	public int get_outgoodsovertime() {
		return _outgoodsovertime;
	}
	public void set_outgoodsovertime(int _outgoodsovertime) {
		this._outgoodsovertime = _outgoodsovertime;
	}
	public int get_twiceintervaltime() {
		return _twiceintervaltime;
	}
	public void set_twiceintervaltime(int _twiceintervaltime) {
		this._twiceintervaltime = _twiceintervaltime;
	}
	public boolean is_allstop() {
		return _allstop;
	}
	public void set_allstop(boolean _allstop) {
		this._allstop = _allstop;
	}
	public int get_maxtimes() {
		return _maxtimes;
	}
	public void set_maxtimes(int _maxtimes) {
		this._maxtimes = _maxtimes;
	}
	public int get_actioninterval() {
		return _actioninterval;
	}
	public void set_actioninterval(int _actioninterval) {
		this._actioninterval = _actioninterval;
	}
	public int get_smsthreadcount() {
		return _smsthreadcount;
	}
	public void set_smsthreadcount(int _smsthreadcount) {
		this._smsthreadcount = _smsthreadcount;
	}
	public int get_copetradethreadcount() {
		return _copetradethreadcount;
	}
	public void set_copetradethreadcount(int _copetradethreadcount) {
		this._copetradethreadcount = _copetradethreadcount;
	}
	public int get_maxseller() {
		return _maxseller;
	}
	public void set_maxseller(int _maxseller) {
		this._maxseller = _maxseller;
	}
	public String is_pwd() {
		return _pwd;
	}
	public void set_pwd(String _pwd) {
		this._pwd = _pwd;
	}
	public int get_umpaythreadcount() {
		return _umpaythreadcount;
	}
	public void set_umpaythreadcount(int _umpaythreadcount) {
		this._umpaythreadcount = _umpaythreadcount;
	}
	public int get_endtradethreadcount() {
		return _endtradethreadcount;
	}
	public void set_endtradethreadcount(int _endtradethreadcount) {
		this._endtradethreadcount = _endtradethreadcount;
	}
	public int get_payovertime() {
		return _payovertime;
	}
	public void set_payovertime(int _payovertime) {
		this._payovertime = _payovertime;
	}
	public int get_feebackovertime() {
		return _feebackovertime;
	}
	public void set_feebackovertime(int _feebackovertime) {
		this._feebackovertime = _feebackovertime;
	}
	public static clsSellerPara getInstance() {
		return instance;
	}
	public static void setInstance(clsSellerPara instance) {
		clsSellerPara.instance = instance;
	}
	
	
}
