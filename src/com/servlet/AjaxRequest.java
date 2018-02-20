package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Out;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import weixin.popular.api.MessageAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.message.message.Message;
import weixin.popular.bean.message.message.TextMessage;
import weixin.popular.support.TokenManager;
import weixin.popular.util.PayUtil;
import weixin.popular.util.WxCoporTransfor;

import com.clsConst;
import com.clsEvent;
import com.ado.SqlADO;
import com.alipay.AlipayQrcode;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.tools.ToolBox;

import beans.PortBean;
import beans.TextMsgBean;
import beans.UserBean;
import beans.VenderBean;
import beans.VenderLogBean;
import beans.clsGroupBean;

/**
 * Servlet implementation class AjaxRequest
 */
@WebServlet("/AjaxRequest")
public class AjaxRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AjaxRequest() {
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
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter pw=response.getWriter();
		UserBean ub=(UserBean)request.getSession().getAttribute("usermessage");	
		
		if(ub==null)
		{
			request.setAttribute("message", "您没有登录或无权访问！请联系管理员！");
			request.setAttribute("LAST_URL", "index.jsp");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}
		
		if(!ub.AccessAble(UserBean.FUNID_CAN_ACCESS_WEB))
		{
			request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ACCESS_WEB]);
			request.setAttribute("LAST_URL", "index.jsp");
			request.getRequestDispatcher("message.jsp").forward(request, response);
			return;
		}
		
		int actid=ToolBox.filterInt(request.getParameter("activeid"));
		int mid=ToolBox.filterInt(request.getParameter("id"));
		switch (actid) {
		case clsConst.ACTION_FRESH_VENDER_PARA:
			if(ub.getAdminusername().toLowerCase().equals(clsConst.POWER_USER_NAME))
			{
				VenderBean.freshVenderPara();
			}
		break;
		case clsConst.ACTION_TEST_WX_SEND_MSG:
			//发送公众号微信消息
			
			if(ub.getAdminusername().toLowerCase().equals(clsConst.POWER_USER_NAME))
			{
				//VenderBean.freshVenderPara();
				int groupBeanid=ToolBox.filterInt(request.getParameter("groupid"));
//				
//				clsGroupBean groupBean=clsGroupBean.getGroup(groupBeanid);
				String touser=request.getParameter("touser");
				String content=request.getParameter("content");
//				Message message=new TextMessage(touser, content);
//
//				String token=TokenManager.FreshToken(groupBean);
//				
//
//				BaseResult ret = MessageAPI.messageCustomSend(token, message);
//				//System.out.println(ret.getErrmsg());
//				pw.write(ret.getErrmsg());
				
				TextMsgBean msgBean=new TextMsgBean(content, touser, groupBeanid);
						TextMsgBean.add(msgBean);
			}
		break;		
		case clsConst.ACTION_TEST_AL_QRCODE_GET:
			try
			{
				int groupBeanid=ToolBox.filterInt(request.getParameter("groupid"));
				PortBean pb=new PortBean();
				pb.setPrice(1);
				pb.setInnerid(1);
				pb.setMachineid(1);
				pb.setGoodsid(1);
				pb.setGoodroadname("测试");
				clsGroupBean groupBean=clsGroupBean.getGroup(groupBeanid);
				AlipayTradePrecreateResponse res=AlipayQrcode.MakeQrcode(pb,ToolBox.getTimeString(),groupBean);
				if(res!=null)
				{
					if(res.isSuccess())
					{
						pw.write(res.getQrCode());
					}
					else
					{
						pw.write("二维码获取失败，返回结果为失败");
					}
					
				}
				else
				{
					pw.write("二维码获取失败，返回结果为空");
				}
			}
			catch (Exception e) {
				pw.write(e.getMessage());
			}
			return;
		case clsConst.ACTION_TEST_WX_QRCODE_GET:
			try
			{
				int groupBeanid=ToolBox.filterInt(request.getParameter("groupid"));
				PortBean pb=new PortBean();
				pb.setPrice(1);
				pb.setInnerid(1);
				pb.setMachineid(1);
				pb.setGoodsid(1);
				pb.setGoodroadname("测试");
				clsGroupBean groupBean=clsGroupBean.getGroup(groupBeanid);
				String qrcode=PayUtil.MakeWxQrcode(pb,
						ToolBox.getTimeString(),
						groupBean);
				if(qrcode!=null)
				{
					pw.write(qrcode);
				}
				else
				{
					pw.write("二维码获取失败，返回结果为空");
				}
			}
			catch (Exception e) {
				pw.write(e.getMessage());
			}
			return;
		case clsConst.ACTION_SAVE_LOG:	
			if(ub.getAdminusername().toLowerCase().equals(clsConst.POWER_USER_NAME))
			{
				int count=VenderLogBean.ForceSaveLog();
				pw.write(String.format("共保存%d条数据，ok", count));
			}
			return;
		case clsConst.ACTION_MANUAL_TRANSFER:	
			if(ub.getAdminusername().toLowerCase().equals(clsConst.POWER_USER_NAME))
			{
				WxCoporTransfor.AutoTransfer();
				pw.write("指令执行完成");
			}
			return;			
		case clsConst.ACTION_SET_VENDER_CAN_USE:
			if(!ub.AccessAble(UserBean.FUNID_CAN_UPDATE_VENDER))
			{
				request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_UPDATE_VENDER]);
				request.setAttribute("LAST_URL", "index.jsp");
				request.getRequestDispatcher("message.jsp").forward(request, response);
				return;
			}
			String active= ToolBox.filter(request.getParameter("active"));
			//boolean canuse=true;
			if(active.equals("OFF"))
			{
				SqlADO.UpdateMechine_CanUse(mid, false);
			}
			else if (active.equals("ON")) {
				SqlADO.UpdateMechine_CanUse(mid, true);
			}
			break;
			
			case clsConst.ACTION_FILL_STOCK:
				if(!ub.AccessAble(UserBean.FUNID_CAN_UPDATE_PORT))
				{
					request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_UPDATE_PORT]);
					request.setAttribute("LAST_URL", "index.jsp");
					request.getRequestDispatcher("message.jsp").forward(request, response);
					return;
				}
				if(!ub.CanManageThisVender(mid))
				{
					request.setAttribute("message", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_UPDATE_PORT]);
					request.getRequestDispatcher("message.jsp").forward(request, response);
					return;				
				}
				
				ArrayList<PortBean> lstArrayList=SqlADO.getPortBeanList(mid);
				boolean need_load=false;
				for (PortBean portBean : lstArrayList) {
					if(portBean!=null)
					{
						if(portBean.getAmount()!=portBean.getCapacity())
						{
							need_load=true;
							break;
						}
					}
				}
				if(need_load)
				{
					SqlADO.SetPortGoodsCount(mid);
					clsEvent.WriteMCFlg(mid, clsEvent.FLG_SET_COLLIST);
				}
			break;
			
			case clsConst.ACTION_JSON_GET_GROUPLST:
				ArrayList<clsGroupBean> gList=clsGroupBean.getGroupLst();
				JSONArray jsonArray_group=new JSONArray();
				JSONObject jsonObject_group=null;
				for (clsGroupBean gbBean:gList) {
					jsonObject_group=new JSONObject();
					jsonObject_group.put("id",gbBean.getId());
					jsonObject_group.put("groupname",gbBean.getGroupname());
					jsonObject_group.put("groupdes",gbBean.getGroupdes());
					jsonObject_group.put("creattime",ToolBox.getYMDHMS(gbBean.getCreattime()));
					jsonArray_group.add(jsonObject_group);
				}
				System.out.println(jsonArray_group.toString());
				pw.print(jsonArray_group.toString());
				return;
				

			case clsConst.ACTION_JSON_UPDATE_PORT:
				System.out.println(mid);
				String dataString=request.getParameter("data");
				System.out.println(dataString);
				JSONArray jsonArray=JSONArray.fromObject(dataString);
				int portid=0;
				int amount;
				int capacity;
				int price;
				int discount;
				int goodsid;
				JSONObject jsonport;
				double value;
				boolean port_changed=false;
				boolean port_goodsname_changed=false;
				for (Object object : jsonArray) 
				{
					jsonport=(JSONObject)object;
					//mid=jsonport.getInt("VenderID");
					portid=jsonport.getInt("PortID");
					amount=jsonport.getInt("Stock");
					capacity=jsonport.getInt("Capacity");
					value=jsonport.getDouble("Price");
					price=(int)(value*100);	
					discount=jsonport.getInt("Discount");
					PortBean p=SqlADO.getPortBean(mid, portid);
					if(jsonport.containsKey("goodsname"))
					{
						goodsid=jsonport.getInt("GoodsName");/*实际返回的是商品编号*/
					}else 
					{
						goodsid=p.getGoodsid();
					}
					
					if(p!=null)
					{
						if(p.getGoodsid()!=goodsid)
						{
							port_goodsname_changed=true;
							clsEvent.WriteMCFlg(mid,clsEvent.FLG_SET_COL_NAME);
						}
						if((p.getAmount()!=amount)||
							(p.getCapacity()!=capacity)||
							(p.getPrice()!=price)||
							(p.getDiscount()!=discount)
							)
						{
							port_changed=true;
							clsEvent.WriteMCFlg(mid,clsEvent.FLG_SET_COLLIST);
						}
						
						if(port_changed||port_goodsname_changed)
						{
							SqlADO.updatePort(mid,portid,amount,capacity,price,discount,goodsid);
						}
					}
				}
				break;

		default:
			break;
		}
		pw.write("ok");
		return;
	}

}
