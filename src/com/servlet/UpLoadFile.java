package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.UserBean;
import com.jspsmart.upload.File;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.tools.ToolBox;

/**
 * Servlet implementation class UpLoadFile
 */
public class UpLoadFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpLoadFile() {
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
			sb.append("图片组件加载失败！");
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
				
				if(!smartUpload.getFiles().getFile(0).getTypeMIME().toLowerCase().endsWith("image"))
				{
					sb.append("<BODY>");
					sb.append("<span class='waring-label'>产品图片无效！</span> <button class='wight_btn' onclick='history.back();'>返回</button>");
					sb.append("  </BODY>");
					sb.append("</HTML>");
					out.print(sb.toString());
					return;
				}

				try {
					
					
					File f=smartUpload.getFiles().getFile(0);
					ext=f.getFileExt();
					String pic1=ToolBox.MakeRPID()+ "."+ ext.toUpperCase();
					String p=request.getRealPath("images_little")+"\\"+pic1;

					//System.out.print(p);
					
					f.saveAs(p);
					
					/*ADD to DB*/
					

					sb.append("  <BODY style='margin:0px;text-align:left;' onload=\"parent.document.getElementById('pic1').value='"+ pic1 +"';\">");
					
					sb.append(pic1);
					
					sb.append("  </BODY>");
					sb.append("</HTML>");
					out.print(sb.toString());

					
				} catch (SmartUploadException e) {
					sb.append("<BODY>");
					sb.append("产品添加失败！");
					sb.append("  </BODY>");
					sb.append("</HTML>");
					out.print(sb.toString());
					e.printStackTrace();
				}
				
				return;
					
			}
			else
			{
				sb.append("<BODY>");
				sb.append("产品添加失败！");
				sb.append("  </BODY>");
				sb.append("</HTML>");
				out.print(sb.toString());
				return;
			}
			
			
		}else
		{
			sb.append("<BODY>");
			sb.append("产品添加失败！");
			sb.append("  </BODY>");
			sb.append("</HTML>");
			out.print(sb.toString());
			return;
		}

	}

}
