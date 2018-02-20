package weixin.popular.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import weixin.popular.bean.paymch.MchBaseResult;

@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class QrUrlFactoryResult  extends MchBaseResult
{
	@XmlElement
	private String short_url;

	public String getShort_url() {
		return short_url;
	}

	public void setShort_url(String short_url) {
		this.short_url = short_url;
	}
	
	
}
