var today=new Date();  //define a Date object to get current time
   var desTxtId;          //the very input type=text element to get date
   var cal;               //the calendar interface 
   var tbl;
   var tblchild;
   var obj;
   var browserName=navigator.appName;
   var isIE=browserName.indexOf("Microsoft")!=-1?true:false;
   
   function montharr(m0,m1,m2,m3,m4,m5,m6,m7,m8,m9,m10,m11)  //define an object includes month days 
   {
      this[0]=m0;this[1]=m1;this[2]=m2;this[3]=m3;this[4]=m4;this[5]=m5;this[6]=m6;
	  this[7]=m7;this[8]=m8;this[9]=m9;this[10]=m10;this[11]=m11; }
	  
   function fillcalendar()  // 'fill' data into the calendar
   {
	  var monthDays=new montharr(31,28,31,30,31,30,31,31,30,31,30,31);
	  var monthNames=new Array("1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月");
	  year=today.getFullYear();
	  if(((year%4==0)&&(year%100!=0))||(year%400==0))monthDays[1]=29;
	  nDays=monthDays[today.getMonth()];
	  firstDay=today;
	  firstDay.setDate(1);
	  startDay=firstDay.getDay();
	  column=0;
	  tblchild=tbl.firstChild;
	  tblchild.childNodes[1].childNodes[2].innerHTML=year+"年"+monthNames[today.getMonth()]
	  for(var i=0;i<startDay;i++){tblchild.childNodes[3].childNodes[i].innerHTML="&nbsp;";column++;}
	  j=3;
	  var txt="";
	  for(var i=1;i<=nDays;i++)
	  {
          tblchild.childNodes[j].childNodes[column].innerHTML=i;
		  column++;
		  if(column==7){j=j+1;column=0;}
	  }
	  if(column<7&&column!=0)
	     for(var i=column;i<7;i++)tblchild.childNodes[j].childNodes[i].innerHTML="&nbsp;";
   }
   
   function monthadd()    
   {
       clearCalendar();
	   today.setMonth((today.getMonth()+1)%12);
	   if(today.getMonth()==0)today.setFullYear(today.getFullYear()+1);
	   fillcalendar();
   }
   
   function monthsub()
   {
       clearCalendar();
	   today.setMonth((today.getMonth()+11)%12);
	   if(today.getMonth()==11)today.setFullYear(today.getFullYear()-1);
	   fillcalendar();
   }
   
   function changeyear(yearValue)
   {
		if(1900>yearValue||yearValue>2999){alert("所选年份超界!!");return;}
		today.setFullYear(yearValue);
		clearCalendar();
		fillcalendar();
   }
   
   function changemonth(monthValue)
   {
		today.setMonth(monthValue-1);
		clearCalendar();
		fillcalendar();
   }
   
   function returndate(vday)
   {
	   if(vday=="&nbsp;"){hidecal();return;}
	   if(vday!="")
	   { month=today.getMonth()+1;
	     year=today.getFullYear();
	     desTxtId.value=year+"-"+month+"-"+vday; }
	   else desTxtId.value=""
	   hidecal();
   }
   
   function clearCalendar()
   {
	  for(var i=3;i<tblchild.childNodes.length;i++)
	     for(var j=0;j<tblchild.childNodes[3].childNodes.length;j++)
		     tblchild.childNodes[i].childNodes[j].innerHTML="";
   }

  function buildtbl()
  {
	  var now = new Date();
      var year = now.getYear();
	  var month=now.getMonth()+1;
	  var day=now.getDay()
	  
      document.write("<div id='Calendar' style=\"display:none;position:absolute;width:220;background:#FFDBBA;z-index:100\">");
      document.write("<table id='calTbl' width='220' align='center' border='1' bordercolorlight='#000000' bordercolordark='#FFFFFF'>");
      document.write("<tr>");
	  document.write("<td Author='rodger' align='center' colspan='5' valign='middle'>");
	  document.write("<select Author='rodger' size='1' onchange='changeyear(this.value)'>");
	  for(var i=1900;i<2999;i++) {
	  if (i==year)  document.write("<option Author='rodger' value='"+i+"' selected>"+i+"</option>");
	  else  document.write("<option Author='rodger' value='"+i+"'>"+i+"</option>");
	  }
	  document.write("</select>年") 
      document.write("<select Author='rodger' size='1' onchange='changemonth(this.value)'>");
	  for(var i=1;i<=12;i++){
	  if (i==month)  document.write("<option Author='rodger' value='"+i+"' selected>"+i+"</option>");
	   else document.write("<option Author='rodger' value='"+i+"'>"+i+"</option>");
	  }
	  document.write("</select>月</td>") 
	  document.write("<td Author='rodger' align='center'><a href='javascript:void(null)' onclick='returndate(\"\")'><font face='Webdings' color='red'>=</font></a></td>"); 
	  document.write("<td Author='rodger' align='center'><a href='javascript:void(null)' onclick='hidecal()'><font face='Webdings' color='red'>r</font></a></td>");
	  document.write("</tr><tr>");
      document.write("<td Author='rodger' align='center'><a Author='rodger' href='javascript:void(null)' onclick='changeyear(today.getFullYear()-1)'><font Author='rodger' face='Webdings'>7</font></a></th>");
      document.write("<td Author='rodger' align='center'><a Author='rodger' href='javascript:void(null)'onclick='monthsub()'><font Author='rodger' face='Webdings'>3</font></a></th>");
	  document.write("<td Author='rodger' colspan='3' align='center' style='font-size:9pt;font-weight:bold;color:blue'>&nbsp;</th>");
      document.write("<td Author='rodger' align='center'><a href='javascript:void(null)' onclick='monthadd()'><font Author='rodger' face='Webdings'>4</font></a></th>");
	  document.write("<td Author='rodger' align='center'><a href='javascript:void(null)' onclick='changeyear(today.getFullYear()+1)'><font Author='rodger' face='Webdings'>8</font></a></th>");
	  document.write("</tr><tr>");
      document.write("<td Author='rodger' align='center' style='font-size:9pt;color:red;font-weight:bold' bgcolor='#66aa77'>日</th>");
      document.write("<td Author='rodger' align='center' style='font-size:9pt;color:white;font-weight:bold' bgcolor='#66aa77'>一</th>");
      document.write("<td Author='rodger' align='center' style='font-size:9pt;color:white;font-weight:bold' bgcolor='#66aa77'>二</th>");
      document.write("<td Author='rodger' align='center' style='font-size:9pt;color:white;font-weight:bold' bgcolor='#66aa77'>三</th>");
      document.write("<td Author='rodger' align='center' style='font-size:9pt;color:white;font-weight:bold' bgcolor='#66aa77'>四</th>");
	  document.write("<td Author='rodger' align='center' style='font-size:9pt;color:white;font-weight:bold' bgcolor='#66aa77'>五</th>");
	  document.write("<td Author='rodger' align='center' style='font-size:9pt;color:green;font-weight:bold' bgcolor='#66aa77'>六</th>");
	  document.write("</tr>");
	  for(var i=0;i<6;i++)
	  {
	      document.write("<tr>");
		  for(var j=0;j<7;j++)
		  {
              if(j==0)varStyle="font-weight:bold;color:red";
              else if(j==6)varStyle="font-weight:bold;color:#66aa66";
              else varStyle="font-weight:bold;color:blue;";
			  if(isIE)varCursor="hand";
			  else varCursor="default"
              document.write("<td Author='rodger' align='center' width='14%' style='"+varStyle+"' onmousedown=returndate(this.innerHTML);this.style.backgroundColor='' onmouseover=this.style.cursor='"+varCursor+"';this.style.backgroundColor='#FFFFFF' onmouseout=this.style.backgroundColor=''></td>"); 
          }
		  document.write("</tr>");
	  }
      document.write("</table>");
      document.write("</div> ");
	  cal=document.getElementById("Calendar");
	  tbl=document.getElementById("calTbl");
	  fillcalendar();
	  hidecal();
  }
 
  function hidecal(){ cal.style.display="none"; }
  
  function showcal(obj)
  {
      desTxtId=obj;
	  cal.style.display="";
	  vtop=offsetTop(obj);
	  vheight=obj.offsetHeight;
      vwidth=obj.offsetWidth;
	  vleft=offsetLeft(obj)+vwidth;
	  if(isIE)
	  {
	     if(vtop+cal.offsetHeight>document.body.scrollTop+document.body.clientHeight)
	        vtop=vtop+vheight-cal.offsetHeight;
	     if(vleft+cal.offsetWidth>document.body.scrollLeft+document.body.clientWidth)
	       vleft=offsetLeft(obj)-cal.offsetWidth;
	     cal.style.top=vtop;
	     cal.style.left=vleft;
	  }
	  else
	  {
	      if(vtop+cal.offsetHeight>window.innerHeight)
	          vtop=vtop+vheight-cal.offsetHeight;
	      if(vleft+cal.offsetWidth>window.innerWidth)
	          vleft=offsetLeft(obj)-cal.offsetWidth;
	      cal.style.top=vtop;
	      cal.style.left=vleft;
	  }
  }
  
  function offsetLeft(obj)
  {   
    x = obj.offsetLeft;
    for (objParent=obj.offsetParent;objParent;objParent=objParent.offsetParent)x+=objParent.offsetLeft;
    return x;
  }
  function offsetTop(obj)
  { 
     y = obj.offsetTop;
     for(objParent=obj.offsetParent;objParent;objParent=objParent.offsetParent)y+=objParent.offsetTop;
     return y;
  } 
  
  buildtbl();