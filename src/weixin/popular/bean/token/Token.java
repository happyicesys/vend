package weixin.popular.bean.token;

import weixin.popular.bean.BaseResult;

public class Token extends BaseResult {

	private String access_token;
	private int expires_in;
	private int next_gmt_time;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String accessToken) {
		access_token = accessToken;
	}

	public int getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(int expiresIn) {
		expires_in = expiresIn;
	}

	public int getNext_gmt_time() {
		return next_gmt_time;
	}

	public void setNext_gmt_time(int next_gmt_time) {
		this.next_gmt_time = next_gmt_time;
	}


	

}
