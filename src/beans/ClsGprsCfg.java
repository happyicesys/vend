package beans;

import java.io.UnsupportedEncodingException;
import java.sql.Date;

import com.clsConst;
import com.ado.SqlADO;
import com.tools.ToolBox;

public class ClsGprsCfg {

//	SELECT




//	dbo.GprsCfg.firmcompiletime
//FROM
//	dbo.GprsCfg
	
	
	private int id;
	private int activeInterval;
	
	private int objTtl;
	
	private String serverUrl;
	private int valid;
	
	public String getVerCode() {
		return VerCode;
	}
	public void setVerCode(String verCode) {
		VerCode = verCode;
	}
	public void setFirmFile(String firmFile) {
		FirmFile = firmFile;
	}
	public String getFirmFile() {
		if(FirmFile==null)
		{
			return "";
		}
		else {
			return FirmFile;
		}
	}

	private String FirmFile;
	private String VerCode;
	
	public Date getFirmcompiletime() {
		return firmcompiletime;
	}
	public void setFirmcompiletime(Date firmcompiletime) {
		this.firmcompiletime = firmcompiletime;
	}

	private Date firmcompiletime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getActiveInterval() {
		return activeInterval;
	}
	public void setActiveInterval(int activeInterval) {
		this.activeInterval = activeInterval;
	}
	public String getServerUrl() {
		if(serverUrl==null)
		{
			return "";
		}
		else {
			return serverUrl;
		}
	}
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	public int getValid() {
		return valid;
	}
	public void setValid(int valid) {
		this.valid = valid;
	}
	
	public int getObjTtl() {
		return objTtl;
	}
	public void setObjTtl(int objTtl) {
		this.objTtl = objTtl;
	}
	
	public static final int OBJ_SIZE=2+1+2+512;
	private static final int DEFAULT_CFG_OBJ_ID = 0;
	public byte[] ToBytes() {
		int len=0;
		byte[] url=null;
		if(this.serverUrl!=null)
		{
			try {
				url=this.serverUrl.getBytes(clsConst.DEFAULT_CHAR_CODE);
				len=url.length;
			} catch (UnsupportedEncodingException e) 
			{
				e.printStackTrace();
			}
		}
		byte[] b=new byte[OBJ_SIZE-512+len];
		int index=0;
		index=ToolBox.short2bytes_little(b, index, (short)this.activeInterval);
		b[index++]=(byte) this.objTtl;
		index=ToolBox.short2bytes_little(b, index, (short)len);
		if(len>0)
		{
			System.arraycopy(url, 0, b, index, len);
		}
		return b;
		
	}
	public static ClsGprsCfg getInstance(int mid) {
		ClsGprsCfg gprsCfg= SqlADO.getGprsConfig(mid);
		if(gprsCfg==null)
		{
			gprsCfg=SqlADO.getGprsConfig(DEFAULT_CFG_OBJ_ID);
		}
		return gprsCfg;
	}
	
	
	

}
