package com.brian.sendmail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.ado.SqlADO;

import beans.VenderBean;

import java.util.ArrayList;
import java.util.Properties;
public class SampleMail {
    private static final String ALIDM_SMTP_HOST = "smtp.webfaction.com";
    //private static final String ALIDM_SMTP_PORT = "25";//或"80"
    public static void Send(String subject, String content) {
        // 配置发送邮件的环境属性
        final Properties props = new Properties();
        // 表示SMTP发送邮件，需要进行身份验证
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", ALIDM_SMTP_HOST);
        //props.put("mail.smtp.port", ALIDM_SMTP_PORT);
        // 如果使用ssl，则去掉使用25端口的配置，进行如下配置,
         props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
         props.put("mail.smtp.socketFactory.port", "465");
         props.put("mail.smtp.port", "465");
        // 发件人的账号
        props.put("mail.user", "system_happyice");
        // 访问SMTP服务时需要提供的密码
        props.put("mail.password", "1234574");
        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
//        mailSession.setDebug(true);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        try {
        // 设置发件人
        InternetAddress from = new InternetAddress("system@happyice.com.sg");
        	
        Address[] to = new Address[1];
        //to[0] = new InternetAddress("daniel.ma@happyice.com.sg");
        //to[1] = new InternetAddress("kent@happyice.com.sg");
       // to[2] = new InternetAddress("jiahaur@happyice.com.sg");
        //to[3] = new InternetAddress("brianlee@happyice.com.my");
        to[0] = new InternetAddress("brianlee@happyice.com.my");
        //to[1] = new InternetAddress("leehongjie91@gmail.com");

        message.setFrom(from);
        Address[] a = new Address[1];
        a[0] = new InternetAddress("system@happyice.com.my");
        message.setReplyTo(a);
        // 设置收件人
        message.setRecipients(MimeMessage.RecipientType.TO, to);
        // 设置邮件标题
        message.setSubject(subject);
        // 设置邮件的内容体
        message.setText(content);
        // 发送邮件
        Transport.send(message);
        }
        catch (MessagingException e) {
            String err = e.getMessage();
            // 在这里处理message内容， 格式是固定的
            System.out.println(err);
        }
    }
    
    public static void main(String[] args) {
    		ArrayList<Integer> VenderIdLst=new ArrayList();

    		ArrayList<VenderBean> lst=SqlADO.getVenderBeanList();
    		for(VenderBean obj :lst)
    		{
    			if(obj.isIsOnline())
    			{
    				VenderIdLst.remove(new Integer(obj.getId()));
    				System.out.println(String.format("%d has remove!", obj.getId()));
    			}
    			else
    			{
    				for(int i=0; i < VenderIdLst.size(); i++) {
    					//System.out.println(VenderIdLst.get(i));
    					//System.out.println(obj.getId());
    					if(VenderIdLst.get(i) == obj.getId()) {
    						SampleMail.Send(String.format("Offline Notification for Vending: %d - %s", obj.getId(), obj.getTerminalName()), String.format("Last Active Time: %1$te %1$tm %1$tY", obj.getTemperUpdateTime()));
    					}
    					
    				}
    			}
    			
    		}
    		System.out.println(VenderIdLst);
    		
    
    }
}