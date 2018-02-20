        (function($){
        	$.fn.serializeJson=function(){
	        	var serializeObj={};
	        	var array=this.serializeArray();
	        	var str=this.serialize();
	        	$(array).each(function()
	        	{
		        	if(serializeObj[this.name])
		        	{
			        	if($.isArray(serializeObj[this.name]))
			        	{
			        		serializeObj[this.name].push(this.value);
			        	}
			        	else
			        	{
			        		serializeObj[this.name]=[serializeObj[this.name],this.value];
			        	}
		        	}
		        	else
		        	{
		        		serializeObj[this.name]=this.value;
		        	}
	        	});
	        	return serializeObj;
        	};
        })(jQuery);

function getAjaxReturn(url,data) 
{ 
    var retstr=null; 
    $.ajax({ 
        type:"POST", 
        async:false, 
        url:url, 
        data:data, 
        success:function(msg)
	        { 
        		retstr=msg;
	        }
    }); 
    return retstr; 
} 


