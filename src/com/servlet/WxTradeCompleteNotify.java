package com.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.clsConst;
import com.ado.SqlADO;
import com.tools.ToolBox;

import beans.TradeBean;
import beans.VenderBean;
import beans.clsGroupBean;
import weixin.popular.bean.paymch.MchOrderInfoResult;
import weixin.popular.util.PayUtil;
import weixin.popular.util.SignatureUtil;
import weixin.popular.util.XMLConverUtil;

/**
 * Servlet implementation class WxTradeCompleteNotify
 */
@WebServlet("/WxTradeCompleteNotify")
public class WxTradeCompleteNotify extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WxTradeCompleteNotify() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");		
		

		
		PrintWriter pw=response.getWriter();
		/*支付结果返回*/
		//clsWxPara para=clsWxPara.getInstance();
		
		InputStream is= request.getInputStream();
		byte[] b=new byte[10240];
		int len=is.read(b);
		String xmlstr=new String(b,0,len,"utf-8");
		Document doc;
		Map<String, String> map= new LinkedHashMap<String, String>();

		try {
			doc=DocumentHelper.parseText(xmlstr);
			Element root=doc.getRootElement();

	        for (Iterator<Element>  i = root.elementIterator(); i.hasNext();) {  
	            Element element = i.next();  
	            System.out.println(element.getName()+ ""+ element.getTextTrim());  
	            map.put(element.getName(), element.getTextTrim());
	            
	        }  
		} catch (DocumentException e) {
			e.printStackTrace();
		}


		
		MchOrderInfoResult infoResult = XMLConverUtil.convertToObject(MchOrderInfoResult.class, xmlstr);
		if(infoResult==null)
		{
			pw.write(PayUtil.generateMchOrderReplyXML(clsConst.WX_FAIL_CODE,"无效对象"));
			return;
		}

		String venderid=infoResult.getAttach();

		VenderBean vb=SqlADO.getVenderBeanByid(ToolBox.String2Integer(venderid));
		clsGroupBean groupBean=clsGroupBean.getGroup(vb.getGroupid());
		
		String sign=null;
		sign=SignatureUtil.generateSign(map, groupBean.getWx_key());
		if(!sign.equals(infoResult.getSign()))
		{
			System.out.println(sign);
			System.out.println(infoResult.getSign());
			pw.write(PayUtil.generateMchOrderReplyXML(clsConst.WX_FAIL_CODE,"签名失败"));
			return;
		}
		


			
        TradeBean tradeBean= SqlADO.getTradeBeanFromTemp(infoResult.getOut_trade_no());
        if(tradeBean!=null)
        {
            tradeBean.setLiushuiid("处理中");
			 tradeBean.setCardinfo(infoResult.getOpenid());
			 tradeBean.setMobilephone(infoResult.getOpenid());
			 tradeBean.setChangestatus(1);
			 tradeBean.setReceivetime(new Timestamp(System.currentTimeMillis()));
			 tradeBean.setXmlstr(xmlstr);
			 tradeBean.setTradetype(clsConst.TRADE_TYPE_WX_QR);
        	 tradeBean.setTradeid(infoResult.getTransaction_id());/*微信支付订单号*/
			 
			 SqlADO.updateTradeBeanTemp(tradeBean);
        }

		pw.write(PayUtil.generateMchOrderReplyXML(clsConst.WX_SUCCESS_CODE,"ok"));
		return;
	}

}
