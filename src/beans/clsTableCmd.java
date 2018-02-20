package beans;

public class clsTableCmd {

	private int _id;
	private int _machineid;
	private int _cmd;
	private String _hexstring;
	private int  _status;
	private int _alivetime;
	
	private long _createtime;
	private String _extfield1;
	private String _extfield2;
	private String _extfield3;
	
	private String rem;
	
	
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public int get_machineid() {
		return _machineid;
	}
	public void set_machineid(int _machineid) {
		this._machineid = _machineid;
	}
	public int get_cmd() {
		return _cmd;
	}
	public void set_cmd(int _cmd) {
		this._cmd = _cmd;
	}
	public String get_hexstring() {
		return _hexstring;
	}
	public void set_hexstring(String _hexstring) {
		this._hexstring = _hexstring;
	}
	public int get_status() {
		return _status;
	}
	public void set_status(int _status) {
		this._status = _status;
	}
	public int get_alivetime() {
		return _alivetime;
	}
	public void set_alivetime(int _alivetime) {
		this._alivetime = _alivetime;
	}
	public long get_createtime() {
		return _createtime;
	}
	public void set_createtime(long _createtime) {
		this._createtime = _createtime;
	}
	public String get_extfield1() {
		return _extfield1;
	}
	public void set_extfield1(String _extfield1) {
		this._extfield1 = _extfield1;
	}
	public String get_extfield2() {
		return _extfield2;
	}
	public void set_extfield2(String _extfield2) {
		this._extfield2 = _extfield2;
	}
	public String get_extfield3() {
		return _extfield3;
	}
	public void set_extfield3(String _extfield3) {
		this._extfield3 = _extfield3;
	}
	public String getRem() {
		return rem;
	}
	public void setRem(String rem) {
		this.rem = rem;
	}

}
