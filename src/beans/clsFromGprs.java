package beans;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tools.ToolBox;

public class clsFromGprs {

	private static List<clsFromGprs> lst;
	
	private int deviceid;
	private int frameid;
	private int typeid;
	private byte[] content;
	private String str_content;
	private String hexstr_content;
	private String  base64_content;
	
	private int gprs;
	
	private String time;
	
	public String toString()
	{
			return base64_content;
	}

	public static List<clsFromGprs> getLst() {
		return lst;
	}
	
	public String getStr_content() {
		return str_content;
	}

	public void setStr_content(String str_content) {
		this.str_content = str_content;
	}

	public String getHexstr_content() {
		return hexstr_content;
	}

	public void setHexstr_content(String hexstr_content) {
		this.hexstr_content = hexstr_content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public static List<clsFromGprs> CopyLst ()
	{
		List<clsFromGprs> c_lst=new ArrayList<clsFromGprs>();
		if(lst==null)
		{
			lst=new ArrayList<clsFromGprs>();
		}
		synchronized (lst) {
			for (clsFromGprs clsFromGprs : lst) {
				c_lst.add(clsFromGprs);
			}
			lst.clear();
		}

		return c_lst;
	}
	
	
	public clsFromGprs(int deviceid, int frameid, int typeid, int gprs,	String base64_content) {
		super();
		this.deviceid = deviceid;
		this.frameid = frameid;
		this.typeid = typeid;
		this.content=ToolBox.getFromBASE64(base64_content);
		this.gprs=gprs;
		try {
			this.str_content=new String(this.content,"GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.hexstr_content=ToolBox.byte2hex(this.content);
		this.time=ToolBox.getDateTimeString();
		this.base64_content = base64_content;
	}

	public static void AddData(int deviceid, int frameid, int typeid, int gprs,	String base64_content) 
	{
		if(lst==null)
		{
			lst=new ArrayList<clsFromGprs>();
		}
		clsFromGprs data=new clsFromGprs(deviceid, frameid, typeid,  gprs,	base64_content);
		synchronized (lst) {
			lst.add(data);
		}
	}

	public static void setLst(List<clsFromGprs> lst) {
		clsFromGprs.lst = lst;
	}

	public int getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(int deviceid) {
		this.deviceid = deviceid;
	}

	public int getFrameid() {
		return frameid;
	}

	public void setFrameid(int frameid) {
		this.frameid = frameid;
	}

	public int getTypeid() {
		return typeid;
	}

	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getBase64_content() {
		return base64_content;
	}

	public void setBase64_content(String base64_content) {
		this.base64_content = base64_content;
	}

	public int getGprs() {
		return gprs;
	}

	public void setGprs(int gprs) {
		this.gprs = gprs;
	}
	
	
	
}
