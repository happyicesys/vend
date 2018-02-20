var SC = {
	couplet : function(){
		if(arguments.length>=1)	this.objID = document.getElementById(arguments[0]);
		if(arguments.length>=2)	this.divTop = arguments[1];
		if(arguments.length>=3) this.divPlane = arguments[2];
		if(arguments.length>=4)	this.scrollDelay = arguments[4];
		if(arguments.length>=5) this.waitTime = arguments[5];
		if(!this.objID){
			alert("对象名【"+ arguments[0] +"】无效，对联无法初始化，请检查对象名称是否正确！");
			this.objID = null; return;
		}else{
			this.objID.style.position = "absolute";
			this.objID.style.display = "block";
			this.objID.style.zIndex = 9999;
		}
		if("" == this.objID.style.top){
			if(isNaN(this.divTop)){
				alert("对象垂直位置(top)参数必须为数字。"); return;
			}else{
				this.objID.style.top = this.divTop+"px";
			}
		}
		if("" == this.objID.style.left && "" == this.objID.style.right){
			if(isNaN(this.divPlane)){
				alert("对象水平位置(left||right)参数必须为数字。"); return;
			}
			if(this.divPlane>0) this.objID.style.left = this.divPlane+"px";
			if(this.divPlane<0) this.objID.style.right = Math.abs(this.divPlane)+"px";
		}
		if(this.scrollDelay<15 || isNaN(this.scrollDelay)) this.scrollDelay = 10; 
		if(this.waitTime<500 || isNaN(this.waitTime)) this.waitTime = 500; 
		if(arguments.length>=1) this.start();
	},
	start : function(){
		if(null == this.objID) return;
		var objCouplet = this;
		timer = this.scrollDelay;
		objCouplet.lastScrollY = 0;
		objCouplet.timerID = null;
		objCouplet.startID = function(){
			if("block" == objCouplet.objID.style.display){
				objCouplet.run();
			}else{
				clearInterval(objCouplet.timerID);
			}
		}
		objCouplet.Begin = function(){
			objCouplet.timerID = setInterval(objCouplet.startID,timer);
		}
		setTimeout(objCouplet.Begin,this.waitTime);
	},
	run : function(){
		if(document.documentElement && document.documentElement.scrollTop){
			uu_scrY = parseFloat(document.documentElement.scrollTop);
		}else if(document.body){
			uu_scrY = parseFloat(document.body.scrollTop);
		}
		uu_divX = parseFloat(this.objID.style.top.replace("px",""));
		uu_curTop = .1 * (uu_scrY - this.lastScrollY);
		uu_curTop = uu_curTop>0?Math.ceil(uu_curTop):Math.floor(uu_curTop);
		this.objID.style.top = parseFloat(uu_divX + uu_curTop) + "px";
		this.lastScrollY += uu_curTop; 
	},
	float : function(){
		$.getScript("./config/communication.php?"+Math.random(),function(){
			var xTop = Communication['Top']; 
			var xAlign = Communication['Align']; 
			var xLevel = Communication['Level']; 
			var xBg = ($.browser.msie && $.browser.version < 7) ? 'images/qq_bg.gif' : 'images/qq_bg.png';
			if(Communication['isOpen'] == 0) return false; 
			var topimg = 'images/qq_top_' + Lang + '.png';
			var bttimg = 'images/qq_small_' + Lang + '.png';
			var boardDiv = '<div id="xMyQQ"><table id="Uphold" border="0" cellpadding="0" cellspacing="0"><tr><td><img width="150" height="37" src="' + topimg + '" style="behavior:url(tools/iepngfix/iepngfix.htc);" /></td></tr>';
			boardDiv += '<tr><td style="background:url(' + xBg + '); overflow:hidden; padding:10px 0; text-align:center;">';
			boardDiv += Communication['Code'];
			boardDiv += '</td></tr>';
			boardDiv += '<tr><td><img width="150" height="8" src="images/qq_bottom.png" style="behavior:url(tools/iepngfix/iepngfix.htc);" /></td></tr></table>';
			boardDiv += '<table id="Shrink" border="0" cellpadding="0" cellspacing="0">';
			boardDiv += '<tr><td><img src="' + bttimg + '" /></td></tr></table></div>'; 
			$(document.body).append(boardDiv); 
			$("#xMyQQ").css({
				"top" : xTop,
				"position" : "absolute",
				"z-index" : "9999999"
			});
			if(xAlign == 0){
				$("#xMyQQ").css("left",xLevel);
			} else {
				$("#xMyQQ").css("right",xLevel);
			};
			if(xAlign == 1) xLevel = "-" + xLevel;
			SC.couplet("xMyQQ",xTop,xLevel); 
			if(Communication['isOpen'] == 1){ 
				var xSpeed = $.browser.msie ? 0 : 200; 
				$("#Uphold").hide(); 
				$("#Shrink").mouseover(function(){ 
					$("#Shrink").hide(0,function(){ 
						$("#Uphold").show(xSpeed); 
					});
				});
				$("body").click(function(){ 
					$("#Uphold").hide(0,function(){ 
						$("#Shrink").show(xSpeed); 
					});
				});
			} else { 
				$("#Shrink").hide(); 
			} 			
			return true;
		});		
	},
	navigation : function(CONFIG_NAVIGATION){
		CONFIG_NAVIGATION = CONFIG_NAVIGATION.split(","); 
		if(CONFIG_NAVIGATION[0] != "1"){ 
			$(".xSC_MENU").hide();
			return false;
		}
		$(".xSC_MENU").css({
			"display" : "none", 
			"position" : "absolute", 
			"z-index" : "99999",
			"overflow" : "hidden"	
		});	
		var Enabled = CONFIG_NAVIGATION[3].split("|"); 
		if($.inArray("0",Enabled) != -1) SC.events('SC_MENU_HOME',CONFIG_NAVIGATION[1],CONFIG_NAVIGATION[2]);
		if($.inArray("1",Enabled) != -1) SC.events('SC_MENU_PRODUCT',CONFIG_NAVIGATION[1],CONFIG_NAVIGATION[2]);
		if($.inArray("2",Enabled) != -1) SC.events('SC_MENU_NEWS',CONFIG_NAVIGATION[1],CONFIG_NAVIGATION[2]);
		if($.inArray("3",Enabled) != -1) SC.events('SC_MENU_FEEDBACK',CONFIG_NAVIGATION[1],CONFIG_NAVIGATION[2]);
		if($.inArray("4",Enabled) != -1) SC.events('SC_MENU_GUESTBOOK',CONFIG_NAVIGATION[1],CONFIG_NAVIGATION[2]);
		if($.inArray("6",Enabled) != -1) SC.events('SC_MENU_DOWNLOAD',CONFIG_NAVIGATION[1],CONFIG_NAVIGATION[2]);
		var Page = CONFIG_NAVIGATION[4].split("|");
		for(i in Page){
			SC.events('SC_MENU_PAGE_' + Page[i],CONFIG_NAVIGATION[1],CONFIG_NAVIGATION[2]);
		}
	},
	events : function(TopClassID,Top,Left){
		var oTime;
		Top = parseInt(Top) + 38;
		Left = parseInt(Left);
		try{
			Top += $("#" + TopClassID).offset().top;
			Left += $("#" + TopClassID).offset().left;
			$("#x" + TopClassID).css({"top" : + Top + "px","left" : + Left + "px"}); 
		}catch(err){
		}
		$("#" + TopClassID).mousemove(function(){ 
			window.clearTimeout(oTime); 
			$(".xSC_MENU").hide(0,function(){ 
				$("#x" + TopClassID).show(); 
			});
		});
		$("#x" + TopClassID).hover(
			function(){window.clearTimeout(oTime);}, 
			function(){$("#x" + TopClassID).hide();}
		);
		$("#" + TopClassID).mouseout(function(){
			oTime = window.setTimeout(function(){ 
				$("#x" + TopClassID).hide();
			},800);
		});			
	},
	searchsubmit : function(form_id,wd_id,file,file_select_id){
		if(file_select_id != 0) file = $('#' + file_select_id).val();
		var nURL = 	'./?m1/f' + file + '/l' + Lang + '/w' + encodeURI($('#' + wd_id).val());
		document.getElementById(form_id).action = nURL;	
		return true;
	},
	trim : function(str){
		return str.replace(/(^[\s\u3000]*)|([\s\u3000]*$)/g,"");
	}	
}
SC.agree = function(isagree,module,id){
	$.ajax({
		type : 'POST',
		url: "inc/ajax.php?" + Math.random(),
		data : 'a=agree&Lang=' + Lang + '&isagree=' + isagree + '&module=' + module + '&id=' + id,
		dataType : 'script',
		success : function(){
			if(typeof(result) == 'undefined'){
				alert('Unable to complete request.');
			} else if (result == '1'){
				var AgreeTotal,agreeHTML,disagreeHTML,AgreeWidth,DisagreeWidth;
				AgreeTotal = agree + disagree; 
				agreeHTML = agree + ' (' + (agree/AgreeTotal*100).toFixed(1) + '%)'
				disagreeHTML = disagree + ' (' + (disagree/AgreeTotal*100).toFixed(1) + '%)'
				$("#agree").html(agreeHTML);
				$("#disagree").html(disagreeHTML);
				AgreeWidth = (agree / AgreeTotal * 142).toFixed(0);
				DisagreeWidth = (disagree / AgreeTotal * 142).toFixed(0);
				$("#bar_agree").animate({width: AgreeWidth + 'px'},1000);
				$("#bar_disagree").animate({width: DisagreeWidth + 'px'},1000); 
			} else {
				alert(result);	
			}
		}
	});
}
SC.UserLogin = function(param,uid,pid){
	$.ajax({
		type : 'POST',
		url: "inc/ajax.php?" + Math.random(),
		data : 'a=login&param=' + param + '&username=' + $("#"+uid).val() + '&password=' + $("#"+pid).val() + '&Lang=' + Lang,
		dataType : 'script',
		success : function(){
			if(typeof(result) == 'undefined'){
				alert('Unable to complete request.');
			} else if (result == '1'){
				if(param == 1){ 
					$("#ajax_welcome").html(welcome);
				}
			} else {
				alert(result);	
			}
		}
	});
}
if (typeof($) == 'undefined'){
	window.onerror = function(){return true;}
	alert('global.js 必须 jquery 支持。\r\r请在 global.js 文件前插入 js/jquery.js 脚本文件。');
}
var Lang = document.getElementsByTagName("script")[(document.getElementsByTagName("script").length)-1].src.match(/[^\?]*$/);
if(isNaN(Lang))	 alert('global.js 脚本文件调用时必须加上语言参数：global.js?{sc:$Sys.Lang#}');
$.getScript('config/config.js',function(){ 
	$(document).ready(function(){
		eval('var CONFIG_NAVIGATION = CONFIG_NAVIGATION' + Lang); 
		SC.navigation(CONFIG_NAVIGATION); 
		SC.float(); 
		if(CONFIG_TRADITIONAL == "1") $.getScript('js/gbtobig.js'); 
	});	
});