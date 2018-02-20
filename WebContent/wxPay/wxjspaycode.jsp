<%@page import="weixin.popular.bean.sns.SnsToken"%>
<%@page import="weixin.popular.api.SnsAPI"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <% 
    	String AppSecret="61d9b96d57de4eb54ac0fae4a7e2fa89";
    	String appid="wx2f96dace5e3723ac";
    	String code=request.getParameter("code");
    	String state=request.getParameter("state");
    	String idinfo=request.getParameter("idinfo");
    	SnsToken token =SnsAPI.oauth2AccessToken(appid, AppSecret, code);
    	
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript">
$('#pay-button').on('click',function(){
    $.ajax({
        url:'wechat/mall/pay/'+$('#prepay_id').html(),
        type : "get",
        data : {
            "timestamp" : new Date().getTime()
        },
        success : function(response) {
            var data = eval('(' + response + ')'); 
            var obj = data.msg;
            WeixinJSBridge.invoke('getBrandWCPayRequest',{  
                "appId" : obj.appId,                  //公众号名称，由商户传入  
                "timeStamp":obj.timestamp,          //时间戳，自 1970 年以来的秒数  
                "nonceStr" : obj.nonce,         //随机串  
                "package" : obj.packageName,      //商品包信息</span>  
                "signType" : obj.signType,        //微信签名方式
                "paySign" : obj.signature           //微信签名  
                },function(res){      

                if(res.err_msg == "get_brand_wcpay_request:ok" ) {  
                    alert('支付成功');  
                }
            });  
        }     
    });         
});
</script>
<title>科威尼迪科技</title>
</head>
<body>
	<%=token.getOpenid() %>

</body>
</html>