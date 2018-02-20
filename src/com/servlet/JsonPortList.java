package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import beans.PortBean;
import beans.UserBean;
import beans.VenderBean;

import com.ClsTime;
import com.ado.SqlADO;
import com.tools.ToolBox;

/**
 * Servlet implementation class JsonPortList
 */
@WebServlet("/JsonPortList")
public class JsonPortList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JsonPortList() {
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
		int mid=ToolBox.filterInt(request.getParameter("mid"));
		ArrayList<PortBean> pList=SqlADO.getPortBeanList(mid);
		JSONObject jsonObject=new JSONObject();
		JSONObject json=null;
		JSONArray json2=new JSONArray();
		for (PortBean portBean : pList) 
		{
			json=new JSONObject();
			json.put("VenderID",  portBean.getId());
			json.put("PortID", portBean.getInnerid());
			json.put("GoodsName", portBean.getGoodroadname());
			json.put("Stock", portBean.getAmount());
			json.put("Capacity", portBean.getCapacity());
			json.put("Price", portBean.getPrice()/100.0);
			json.put("Discount", portBean.getDiscount());
			json.put("UpdateTime",ToolBox.getYMDHMS(portBean.getUpdatetime()) );
			json.put("FaultID", portBean.getError_id());
			json.put("FaultDes", portBean.getErrorinfo());
			json.put("FaultTime", ToolBox.getYMDHMS(portBean.getLastErrorTime()));
			json2.add(json);
		}
		jsonObject.put("Rows", json2);
		jsonObject.put("Total",pList.size());
		
		System.out.println(jsonObject.toString());
		out.print(jsonObject.toString());
	}
}


