/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.alipay.factory;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.constants.AlipayServiceEnvConstants;

import beans.clsGroupBean;
import sun.misc.Cleaner;


/**
 * API调用客户端工厂
 * 
 * @author taixu.zqq
 * @version $Id: AlipayAPIClientFactory.java, v 0.1 2014年7月23日 下午5:07:45 taixu.zqq Exp $
 */
public class AlipayAPIClientFactory {

    /** API调用客户端 */
    private static AlipayClient alipayClient;
    
//    /**
//     * 获得API调用客户端
//     * 
//     * @return
//     */
//    public static AlipayClient getAlipayClient(){
//        
//        if(null == alipayClient){
//            alipayClient = new DefaultAlipayClient(
//            		AlipayServiceEnvConstants.ALIPAY_GATEWAY,
//            		AlipayServiceEnvConstants.APP_ID, 
//            		AlipayServiceEnvConstants.PRIVATE_KEY, 
//            		"json", 
//            		AlipayServiceEnvConstants.CHARSET,
//            		AlipayServiceEnvConstants.ALIPAY_PUBLIC_KEY);
//        }
//        return alipayClient;
//    }

	public static AlipayClient getAlipayClient(clsGroupBean groupBean) 
	{
		AlipayClient Client;
		Client= new DefaultAlipayClient(
        		AlipayServiceEnvConstants.ALIPAY_GATEWAY,
        		groupBean.getAl_APP_ID(), 
        		groupBean.getAl_PRIVATE_KEY(), 
        		"json", 
        		AlipayServiceEnvConstants.CHARSET,
        		groupBean.getAl_PUBLIC_KEY());
		return Client;

	}
}
