

/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.alipay.constants;

/**
 * 支付宝服务窗环境常量（demo中常量只是参考，需要修改成自己的常量值）
 * 
 * @author taixu.zqq
 * @version $Id: AlipayServiceConstants.java, v 0.1 2014年7月24日 下午4:33:49 taixu.zqq Exp $
 */
public class AlipayServiceEnvConstants {

    /**支付宝公钥-从支付宝服务窗获取*/
    public static final String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
    /**签名编码-视支付宝服务窗要求*/
    public static final String SIGN_CHARSET      = "GBK";

    /**字符编码-传递给支付宝的数据编码*/
    public static final String CHARSET           = "GBK";

    /**签名类型-视支付宝服务窗要求*/
    public static final String SIGN_TYPE         = "RSA";
    
    
    public static final String PARTNER           = "2088021897087413";

    /** 服务窗appId  */
    //TODO !!!! 注：该appId必须设为开发者自己的服务窗id  这里只是个测试id
    public static final String APP_ID            = "2015120800934069";

    //开发者请使用openssl生成的密钥替换此处  请看文档：https://fuwu.alipay.com/platform/doc.htm#2-1接入指南
    //TODO !!!! 注：该私钥为测试账号私钥  开发者必须设置自己的私钥 , 否则会存在安全隐患 
    public static final String PRIVATE_KEY       = 
	"MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMiQYVa9slZ4Ex8e"+
	"W7nuF7Ouf5IreGixOvbygFLf4MFrEQ78KeqHMQ36sAFhRKiPgFL4Ss3rG5D0Ocn7"+
	"Zeb8+BygXK01q3/b/tWws72jw57Ph0CgKTeEEPV17ddlNQoJbT/RDMEQTS70zYy4"+
	"Qb7fZmmnX033V4KfGpPjfodDcgtvAgMBAAECgYEAmKxPbJg6iVgeNjIF4SM9J6pN"+
	"shclMYy4NgOja3LAxHucIKwTvFPtI5cRI/vHKAkUxhP73YR7F3zSAM8+YvduZgJw"+
	"ZG26YMsvp4dcVrbWwp+fRI+m78EKXjZtnHLcpNXwWq5eB4HH8K55UjfiNNrHxs6N"+
	"ltFayUy9OBVREZ5KsAECQQDyGUhrj50OFHtjAeEUGMtQfKN6W5JVFqFHf+gx3huK"+
	"45/zSiZGYFRySB/wv0bu+77R9dAQ/B7VUarZcWCuecovAkEA1BSPKi03q9cLXZpj"+
	"xSFA6HQDiWsfpECdFebb0lQEuy+NpU9h+U1QIcBfOduRaCQ2ydAYGK2vcwy0Ebjg"+
	"YufCwQJBAMSzxiOei/zkp1vKKxYpDwhWPH0t6bc4q9FsGzxpOfee+lax6GdF3Vb9"+
	"QvZYc0m5QVFWZ7dlIagwsp/5fpREYAECQQCmEStBxYKQZqWZuClj1XQ0EZQraNyw"+
	"C4B2A/4hpnTF9qidx5laU/XpV++5KHul5qWYRgB4Ypi+KDZgojnieMSBAkAsAFCk"+
	"mR+BF0HRg+Q59HolZIoduFAtm8svt2LoNXSQlyxwgilCo9vl2S/DVN3+4oItmM3B"+
	"UDcOwCrj4eHBSUy8";

    //TODO !!!! 注：该公钥为测试账号公钥  开发者必须设置自己的公钥 ,否则会存在安全隐患
    public static final String PUBLIC_KEY        ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
    /**支付宝网关*/
    public static final String ALIPAY_GATEWAY    = "https://openapi.alipay.com/gateway.do";

    /**授权访问令牌的授权类型*/
    public static final String GRANT_TYPE        = "authorization_code";
}