package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.UserBean;

import com.clsConst;
import com.clsEvent;
import com.ado.SqlADO;
import com.jspsmart.upload.File;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.tools.ToolBox;

/**
 * Servlet implementation class UpLoadFirmFile
 */
public class UpLoadFirmFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpLoadFirmFile() {
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
		
		StringBuilder sb=new StringBuilder();
		//SqlADO.AddGoodsInfo(goodsname,des1,des2,des3,pic1);
		sb.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		sb.append("<HTML>");
		sb.append("  <HEAD>");
		sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
		sb.append("<TITLE>");
		sb.append(ToolBox.WEB_NAME);

		sb.append("</TITLE><link href=\"./css/styles.css\" rel=\"stylesheet\" type=\"text/css\" /></HEAD>");	
		
	    if(ub==null)
	    {
			sb.append("<BODY>");
			sb.append("您没有登录或无权访问！");
			sb.append("  </BODY>");
			sb.append("</HTML>");
			out.print(sb.toString());
	    	return;
	    } 
		
		SmartUpload smartUpload=new SmartUpload();
		//smartUpload.initialize(pageContext);
		
		smartUpload.initialize(this.getServletConfig(), request, response);
		
		
		try {
			smartUpload.upload();
		} catch (SmartUploadException e1) {
			sb.append("<BODY>");
			sb.append("文件组件加载失败！");
			sb.append("  </BODY>");
			sb.append("</HTML>");
			out.print(sb.toString());
			e1.printStackTrace();
			return;
		}

		


		if(smartUpload.getFiles()!=null)
		{
			if(smartUpload.getFiles().getCount()==1)
			{
				String ext;
				
				try {
					int mid= ToolBox.filterInt(smartUpload.getRequest().getParameter("mid"));
					int act=ToolBox.filterInt(smartUpload.getRequest().getParameter("act"));
					File f=smartUpload.getFiles().getFile(0);
					ext=f.getFileExt();
					if(!ext.toLowerCase().equals("bin"))
					{
						sb.append("<BODY>");
						sb.append("<span class='waring-label'>文件无效！</span> <button class='wight_btn' onclick='history.back();'>返回</button>");
						sb.append("  </BODY>");
						sb.append("</HTML>");
						out.print(sb.toString());
						return;
					}
					String pic1=f.getFileName();
					String p=null;
					if(act==clsEvent.VMC_EVENT)
					{
						p=request.getRealPath("flash_bin")+"\\"+pic1;
						f.saveAs(p);
					}
					else if(act==clsEvent.GT_EVENT)
					{
						p=request.getRealPath("gt_flash_bin")+"\\"+pic1;
						f.saveAs(p);
					}
					
					/*保存到数据库*/
					//SqlADO.UpdateMechine_VmcFileName(mid, pic1);
					/*写事件标志*/
					//clsEvent.WriteMCFlg(mid,clsEvent.FLG_UPDATE_FLASH);
					
					sb.append("  <BODY style='margin:0px;text-align:left;' onload=\"parent.win_callback('"+ pic1 +"');\">");
					sb.append(pic1);
					
					sb.append("  </BODY>");
					sb.append("</HTML>");
					out.print(sb.toString());

					
				} catch (SmartUploadException e) {
					sb.append("<BODY>文件上传失败！</BODY></HTML>");
					out.print(sb.toString());
					e.printStackTrace();
				}
				
				return;
					
			}
			else
			{
				sb.append("<BODY>文件上传失败！</BODY></HTML>");
				out.print(sb.toString());
				return;
			}
			
			
		}else
		{
			sb.append("<BODY>文件上传失败！</BODY></HTML>");
			out.print(sb.toString());
			return;
		}
	}

}
