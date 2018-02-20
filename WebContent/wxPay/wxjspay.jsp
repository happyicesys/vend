<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <% 
	

    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
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


</body>
</html>