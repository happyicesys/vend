package com.servlet;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import beans.TradeBean;
import beans.clsFromGprs;

import com.tools.ToolBox;

/**
 * Servlet implementation class SetPara4
 */
@WebServlet("/SetPara4")
public class SetPara4 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetPara4() {
        super();
        // TODO Auto-generated constructor stub
    }

    static Vector<TradeBean> beans=new Vector<TradeBean>();
    
    static Vector<String> gprs_ret_send=new Vector<String>();
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setCharacterEncoding(CHAR_CODE);
		response.setCharacterEncoding(CHAR_CODE);
		response.setContentType("text/html; charset=GBK");
		PrintWriter pw=response.getWriter();
		
		String list=ToolBox.filter(request.getParameter("list"));
		if(!list.equals(""))
		{
			pw.write("【list=clr】清空列表<br/>");
			pw.write("【list=list】显示列表<br/>");
			pw.write("<br/>");
			pw.write("<br/>");
			if(list.trim().equals("list"))
			{
				for (String string : gprs_ret_send) 
				{
					pw.write(string);
					pw.write("<br/>");
				}
				
			}
			else if(list.trim().equals("clr"))
			{
				gprs_ret_send.clear();
			}
			pw.write("显示完成");
			return;
		}
		
		int count= ToolBox.filterInt(request.getParameter("count"));
		
		count/=5;
		count*=5;
		if(count<=0)
		{
			pw.write("数量必须大于等于5，且是5的倍数");
		}
		else
		{
			TradeBean tb=new TradeBean();
			tb.setOrderid(ToolBox.getTimeString());
			tb.setPrice(count);
			beans.addElement(tb);
			pw.write("恭喜，添加成功，出"+count+"个球");
		}
	}

	final String CHAR_CODE="GBK";
	final  String START_LETTER="{\"Type\":\"";
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding(CHAR_CODE);
		response.setCharacterEncoding(CHAR_CODE);
		response.setContentType("text/html; charset=GBK");
		PrintWriter pw=response.getWriter();
		
		InputStream is = request.getInputStream(); 
		DataInputStream input = new DataInputStream(is); 
		
		byte[] strb=new byte[20480];
		int poststrlen=0;
		int need_len=request.getContentLength();
		while (poststrlen<need_len) 
		{
			poststrlen+=input.read(strb,poststrlen,need_len-poststrlen); 
			//System.out.println(poststrlen);
		}
		String ret_str="1";

		String poststr=new String(strb,0,poststrlen,CHAR_CODE);
		System.out.println("接收到一个数据请求："+request.getRemoteAddr()+"@"+ ToolBox.getDateTimeString()+","+poststr);
		String[] arrstr=poststr.split("&",0);
		
		Hashtable<String, String> hash=new Hashtable<String, String>(2,(float)0.8);		
		for (String string : arrstr) {
			String[] subarrstr=string.split("=",2);
			if(subarrstr.length>=2)
			{
				hash.put(subarrstr[0], subarrstr[1]);
			}
			else if(subarrstr.length==1)
			{
				hash.put(subarrstr[0], null);
			}
		}
		
		int f=ToolBox.filterInt(hash.get("f"));/*帧编号，服务器上保存1条最近的编号信息，如果收到的是重复的信息，那么不处理数据，仅仅回传上次发送过的数据*/
		int t=ToolBox.filterInt(hash.get("t"));/*数据类型*/
		int machineid=ToolBox.filterInt(hash.get("m"));/*机器编号，必须填写*/
		int gprs=ToolBox.filterInt(hash.get("g"));/*GPRS信号*/
		String p=hash.get("p");
		
		p=p.replaceFirst("!", "=");

		clsFromGprs  gprsdata=new clsFromGprs(machineid, f, t, gprs, p);
		
		System.out.println(gprsdata.getStr_content());
		gprs_ret_send.add(ToolBox.getDateTimeString() +"  服务器接收:"+ gprsdata.getStr_content());
		
		if(gprsdata.getStr_content().startsWith("{"))
		{
			JSONObject obj=JSONObject.fromObject(gprsdata.getStr_content());
			if(obj!=null)
			{
				if(obj.getString("TYP").equals("1"))
				{
					//POLL
					if(beans.size()>0)
					{
						TradeBean tb=beans.get(0);
						if(tb!=null)
						{	
							ret_str=String.format("%s,%d", tb.getOrderid(),tb.getPrice());
						}
						beans.remove(0);
					}
					else
					{
						
					}
				}
				else if(obj.getString("TYP").equals("2"))
				{
					//上报交易
					

				}
				String base64_str=ToolBox.getBASE64(ret_str);
				base64_str=base64_str.replace("\r", "");
				base64_str=base64_str.replace("\n", "");
				gprs_ret_send.add(ToolBox.getDateTimeString() +"  服务器发送:"+ ret_str);
				System.out.println(ret_str);
				String str=String.format("%d,%d,%s",f,base64_str.length(), base64_str);
				System.out.println(str);
				pw.write(str);
			}
		}
	}

}
