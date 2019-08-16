package com.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
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
import weixin.popular.bean.paymch.MchPayNotify;
import weixin.popular.support.ExpireKey;
import weixin.popular.support.expirekey.DefaultExpireKey;
import weixin.popular.util.SignatureUtil;
import weixin.popular.util.StreamUtils;
import weixin.popular.util.XMLConverUtil;
import wx.pay.util.PayUtil;


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

	private static ExpireKey expireKey = new DefaultExpireKey();
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");		
		
		PrintWriter pw=response.getWriter();

		//获取请求数据
		String xmlstr = StreamUtils.copyToString(request.getInputStream(), Charset.forName("utf-8"));
		//System.out.println(xmlstr);
		
		
		//将XML转为MAP,确保所有字段都参与签名验证
		Map<String,String> mapData = XMLConverUtil.convertToMap(xmlstr);
		//转换数据对象
		MchPayNotify infoResult = XMLConverUtil.convertToObject(MchPayNotify.class,xmlstr);
		
		if(infoResult==null)
		{
			pw.write(PayUtil.generateMchOrderReplyXML(clsConst.WX_FAIL_CODE,"无效对象"));
			return;
		}
		
		//已处理 去重
		if(expireKey.exists(infoResult.getTransaction_id()))
		{
			pw.write(PayUtil.generateMchOrderReplyXML(clsConst.WX_SUCCESS_CODE,"ok"));
			return;
		}



		
		int venderid=ToolBox.String2Integer(infoResult.getAttach());
		VenderBean vb=SqlADO.getVenderBeanByid(venderid);
		if(vb==null)
		{
			expireKey.add(infoResult.getTransaction_id());
			pw.write(PayUtil.generateMchOrderReplyXML(clsConst.WX_SUCCESS_CODE,"ok"));
			return;
		}
		clsGroupBean groupBean=clsGroupBean.getGroup(vb.getGroupid());
		if(groupBean==null)
		{
			expireKey.add(infoResult.getTransaction_id());
			pw.write(PayUtil.generateMchOrderReplyXML(clsConst.WX_SUCCESS_CODE,"ok"));
			return;
		}
		
		//签名验证
		if(!SignatureUtil.validateSign(mapData,groupBean.getWx_key())){
			System.out.println("签名失败");
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
