package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.UserBean;
import beans.VenderBean;

import com.clsConst;
import com.ado.SqlADO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class VenderListJson
 */
@WebServlet("/VenderListJson")
public class VenderListJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VenderListJson() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
	    request.setCharacterEncoding("UTF-8");
	    response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		UserBean ub=(UserBean)session.getAttribute("usermessage");
		PrintWriter out= response.getWriter();
		if(ub==null)
		{
			request.setAttribute("message", "您没有登录或无权访问！请联系管理员！");
			request.setAttribute("LAST_URL", "index.jsp");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}
		ArrayList<VenderBean> vList= SqlADO.getVenderListByIdLimint(ub.getVenderLimite());
		
		JSONObject jsonObject=new JSONObject();
		JSONObject json=null;
		JSONArray json2=new JSONArray();
		for (VenderBean venderBean : vList) 
		{
			json=new JSONObject();
			json.put("VenderID",  venderBean.getId());
			json.put("VenderName", venderBean.getTerminalName());
			json.put("VenderAddr", venderBean.getTerminalAddress());
			json.put("CoinState", venderBean.getMdbDeviceStatus());
			json.put("CoinCount", venderBean.getCoinAtbox());
			json.put("BillState", venderBean.getMdbDeviceStatus());
			json.put("Temperature", (venderBean.getTemperature()/10.0)+"℃");
			json.put("VenderLink", venderBean.isIsOnline()?"在线":"离线");
			json.put("VenderCanUse", venderBean.isCanUse()?"是":"否");
			json.put("VenderGPRS", (venderBean.getGprs_Sign()*100/31)+"%");
			//json.put("ManagerBut", "<button type=\"button\" value=\"详情\"/>");
			json2.add(json);
		}
		jsonObject.put("Rows", json2);
		jsonObject.put("Total",vList.size());
		
		System.out.println(jsonObject.toString());
		out.print(jsonObject.toString());
	}

}
