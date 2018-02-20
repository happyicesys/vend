package com.servlet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ClsTime;
import com.clsConst;
import com.ado.SqlADO;
import com.tools.ToolBox;

import net.sf.json.JSONObject;

import beans.UserBean;

/**
 * Servlet implementation class jsonAddUser
 */
@WebServlet("/jsonAddUser")
public class jsonAddUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public jsonAddUser() {
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
		JSONObject json_retJsonObject=new JSONObject();
		if(ub==null)
		{
			json_retJsonObject.put("errlevel", clsConst.ERR_LEVEL_CANT_ACCESS);
			json_retJsonObject.put("errdes", "您没有登录或无权访问！请联系管理员！");
			out.print(json_retJsonObject.toString());
			return;
		}
		
		if(!ub.AccessAble(UserBean.FUNID_CAN_ACCESS_WEB))
		{
			json_retJsonObject.put("errlevel", clsConst.ERR_LEVEL_CANT_ACCESS);
			json_retJsonObject.put("errdes", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ACCESS_WEB]);
			out.print(json_retJsonObject.toString());
			return;
		}
	    
	    String info=request.getParameter("userinfo");
	    JSONObject jsonObject=JSONObject.fromObject(info);
		try {
		    int uid=jsonObject.getInt("uid");
		    String adminusername=jsonObject.getString("adminusername");
		    String adminpassword=jsonObject.getString("adminpassword");
		    String repassword=jsonObject.getString("repassword");
			System.out.println(jsonObject.toString());
			UserBean userBean=null;
			
			if(uid==0)
			{
				/*添加新用户信息*/
			    if(!ub.AccessAble(UserBean.FUNID_CAN_ADD_USER))
				{
					json_retJsonObject.put("errlevel", clsConst.ERR_LEVEL_NO_OPERATION_RIGHT);
					json_retJsonObject.put("errdes", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_ADD_USER]);
					out.print(json_retJsonObject.toString());
					return;
				}
			    /*查询是该用户名是否已经被使用*/
			    userBean =UserBean.getUserBean(adminusername);
			    if(userBean!=null)
			    {
			    	/*用户名已经存在，不允许使用*/
					json_retJsonObject.put("errlevel",  clsConst.ERR_LEVEL_OPERATION_ERR);
					json_retJsonObject.put("errdes", "亲，该用户名已经被使用，请使用其他用户名！");
					out.print(json_retJsonObject.toString());
					return;
			    }
			    if(adminpassword.length()<6)
			    {
					json_retJsonObject.put("errlevel", clsConst.ERR_LEVEL_OPERATION_ERR);
					json_retJsonObject.put("errdes", "亲，密码长度要多于6位 哦！");
					out.print(json_retJsonObject.toString());
					return;
			    }
			    
			    if(!adminpassword.equals(repassword))
			    {
			    	/*两次密码输入不一致*/
					json_retJsonObject.put("errlevel", clsConst.ERR_LEVEL_OPERATION_ERR);
					json_retJsonObject.put("errdes", "亲，两次密码输入不一致，请重新输入！");
					out.print(json_retJsonObject.toString());
					return;
			    }
			    userBean=new UserBean();
			    userBean.setAdminusername(adminusername);
			    userBean.setAdminaddress(jsonObject.getString("adminaddress"));
			    userBean.setAdminmobilephone(jsonObject.getString("adminmobilephone"));
			    userBean.setAdminname(jsonObject.getString("adminname"));
			    userBean.setAdminpassword(adminpassword);

			    String adminrights=jsonObject.getString("adminrights");
			    String newrightString="";
			    for(int i=0;i<UserBean.RIGHT_DES.length;i++ )
			    {
			    	if(ub.AccessAble(i))
			    	{
			    		if(i<adminrights.length())
			    		{
					    	if(adminrights.charAt(i)=='1')
					    	{
					    		newrightString+="1";
					    		continue;
					    	}
			    		}
			    	}
			    	newrightString+="0";
			    }
			    userBean.setAdminrights(newrightString);
				    
			    
			    
			    String venderIdString=jsonObject.getString("VenderId");
			    String[] stra=venderIdString.split(",",0);
			    String canAccessVender="";
			    int i;
				if(stra!=null)
				{
				    for (i=0;i<stra.length-1;i++) {
				    	
				    	if(ToolBox.isInt(stra[i]))
				    	{
				    		canAccessVender+=stra[i]+",";
				    	}
				    	
					}
			    	if(ToolBox.isInt(stra[i]))
			    	{
			    		canAccessVender+=stra[i];
			    	}
				}
				userBean.setCanAccessSellerid(canAccessVender);
			    
			    
			    userBean.setAdminsex(jsonObject.getString("adminsex"));
			    userBean.setAdmintelephone(jsonObject.getString("admintelephone"));
			    if(ub.AccessAble(UserBean.FUNID_CAN_CHANGE_GROUP_ID))
			    {
			    	userBean.setGroupid(jsonObject.getInt("groupid"));
			    }else
			    {
			    	/*如果没有编辑集团编号的权限，就只能创建本集团的用户*/
			    	userBean.setGroupid(ub.getGroupid());
				}
			    userBean.setCreatetime(new Timestamp(ClsTime.SystemTime()));
			    UserBean.addUser(userBean);
				json_retJsonObject.put("errlevel", clsConst.ERR_LEVEL_NO_ERR);
				json_retJsonObject.put("errdes", "亲，用户信息已经成功添加！");
				
				out.print(json_retJsonObject.toString());
				return;
			}
			else 
			{
				/*更新用户信息*/
			    if(!ub.AccessAble(UserBean.FUNID_CAN_UPDATE_USER))
				{
					json_retJsonObject.put("errlevel", clsConst.ERR_LEVEL_NO_OPERATION_RIGHT);
					json_retJsonObject.put("errdes", "不被"+UserBean.RIGHT_DES[UserBean.FUNID_CAN_UPDATE_USER]);
					out.print(json_retJsonObject.toString());
					return;
				}
			    
			    if(!adminpassword.equals(repassword))
			    {
			    	/*两次密码输入不一致*/
					json_retJsonObject.put("errlevel", clsConst.ERR_LEVEL_OPERATION_ERR);
					json_retJsonObject.put("errdes", "亲，两次密码输入不一致，请重新输入！");
					out.print(json_retJsonObject.toString());
					return;
			    }
			    

			    int groupid=jsonObject.getInt("groupid");
			    if(!ub.AccessAble(UserBean.FUNID_CAN_CHANGE_GROUP_ID))
			    {
			    	if(ub.getGroupid()!=groupid)
			    	{
				    	/*在没有改变集团号的权利下，改变了管理员的集团编号*/
						json_retJsonObject.put("errlevel", clsConst.ERR_LEVEL_NO_OPERATION_RIGHT);
						json_retJsonObject.put("errdes", "你无权执行此项操作！");
						out.print(json_retJsonObject.toString());
						return;
			    	}
			    }
			    userBean=UserBean.getUserBeanById(uid);
			    userBean.setAdminaddress(jsonObject.getString("adminaddress"));
			    userBean.setAdminmobilephone(jsonObject.getString("adminmobilephone"));
			    userBean.setAdminname(jsonObject.getString("adminname"));
			    if(adminpassword!=null)
			    {
			    	adminpassword=adminpassword.trim();
			    	if(adminpassword.equals(""))
			    	{
			    		userBean.setAdminpassword(ToolBox.getMd5(adminpassword));
			    	}
			    }

			    String adminrights=jsonObject.getString("adminrights");
			    String newrightString="";
			    for(int i=0;i<UserBean.RIGHT_DES.length;i++ )
			    {
			    	if(ub.AccessAble(i))
			    	{
				    	if(adminrights.charAt(i)=='1')
				    	{
				    		newrightString+="1";
				    		continue;
				    	}
			    	}
			    	newrightString+="0";
			    }
			    userBean.setAdminrights(newrightString);
				    
			    
			    if(ub.AccessAble(UserBean.FUNID_CAN_ASIGN_VENDER))
			    {
				    String venderIdString=jsonObject.getString("VenderId");
				    String[] stra=venderIdString.split(",",0);
				    String canAccessVender="";
				    int i;
					if(stra!=null)
					{
					    for (i=0;i<stra.length-1;i++) {
					    	
					    	if(ToolBox.isInt(stra[i]))
					    	{
					    		canAccessVender+=stra[i]+",";
					    	}
					    	
						}
				    	if(ToolBox.isInt(stra[i]))
				    	{
				    		canAccessVender+=stra[i];
				    	}
					}
					userBean.setCanAccessSellerid(canAccessVender);
			    }
			    
			    
			    userBean.setAdminsex(jsonObject.getString("adminsex"));
			    userBean.setAdmintelephone(jsonObject.getString("admintelephone"));
			    if(ub.AccessAble(UserBean.FUNID_CAN_CHANGE_GROUP_ID))
			    {
			    	userBean.setGroupid(groupid);
			    }else
			    {
			    	/*如果没有编辑集团编号的权限，就只能创建本集团的用户*/
			    	userBean.setGroupid(ub.getGroupid());
				}
			    UserBean.updateUser(userBean);
				json_retJsonObject.put("errlevel", clsConst.ERR_LEVEL_NO_ERR);
				json_retJsonObject.put("errdes", "亲，用户信息已经修改成功！");
				out.print(json_retJsonObject.toString());
				return;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			json_retJsonObject.put("errlevel", clsConst.ERR_LEVEL_OPERATION_ERR);
			json_retJsonObject.put("errdes", "参数有误，请确认您的操作是否正确?");
			out.print(json_retJsonObject.toString());
			return;
		}

	}

}
