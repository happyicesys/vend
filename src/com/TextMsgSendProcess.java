package com;

import java.util.List;

import beans.TextMsgBean;
import beans.clsGroupBean;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.message.message.TextMessage;
import weixin.popular.support.TokenManager;

public class TextMsgSendProcess extends Thread 
{
	
	public void run() 
	{
		while(true)
		{
			try {
				sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//System.out.println("process running");
			try
			{
				List<TextMsgBean> msglst = TextMsgBean.getList(0);
				String access_token=null;
				TextMessage message=null;
				clsGroupBean groupBean=null;
				if(msglst.size()>0)
				{
					for (TextMsgBean textMsgBean : msglst)
					{
						if(textMsgBean!=null)
						{
							groupBean=clsGroupBean.getGroup(textMsgBean.getGroupid());
							access_token=TokenManager.FreshToken(groupBean);
							if(access_token!=null)
							{
								message=new TextMessage(textMsgBean.getTouser(), textMsgBean.getContent());
								BaseResult ret= weixin.popular.api.MessageAPI.messageCustomSend(access_token, message);
								textMsgBean.setStatus(2);//设置为已经发送
								textMsgBean.setRetmsg(String.format("%s-%s",ret.getErrcode(), ret.getErrmsg()));
							}
							else
							{
								System.out.println("access_token=null");
							}
						}
					}
					TextMsgBean.updateTextMsgBeanrLst(msglst);
				}
			}
			catch(Exception e1)
			{
				e1.printStackTrace();
			}
		}
	}
}
