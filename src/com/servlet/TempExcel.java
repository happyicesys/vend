package com.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.tools.Tool;

import beans.TempBean;
import beans.UserBean;
import beans.VenderBean;

import com.ClsTime;
import com.clsConst;
import com.ado.SqlADO;
import com.tools.ToolBox;

import java.io.File;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;




/**
 * Servlet implementation class TempExcel
 */
public class TempExcel extends HttpServlet {
	private static final long serialVersionUID = 1L;

	String[] TitleString=new String[]{
			  "#",
			  "ID",
			  "Time",
			  "Temp",
			  };

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TempExcel() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session=request.getSession();
	    request.setCharacterEncoding("UTF-8");
		UserBean ub=(UserBean)session.getAttribute("usermessage");
		PrintWriter out= response.getWriter();

	    if(ub==null)
	    {
	    	out.print(ToolBox.NOLOGIN);
	    	return;
	    }

	    int vid=ToolBox.filterInt(request.getParameter("vid"));

		ArrayList<TempBean> tb=SqlADO.getTempListByVid(vid);


		String filepath=this.getServletContext().getRealPath("/");
		String filename="./"+Integer.toString(vid)+"_" + ToolBox.getTimeString();
		String zipfilename=filepath+filename+".zip";
		String xlsfilename=filepath+filename+".xls";
        try {
			// 打开文件
			WritableWorkbook book = Workbook.createWorkbook(new File(xlsfilename));
			// 生成名为“第一页”的工作表，参数0表示这是第一页
			WritableSheet sheet = book.createSheet("Page 1", 0);

			Label label;
			int i;
			for(i=0;i<TitleString.length;i++)
			{
				label = new Label(i, 0, TitleString[i]);
				sheet.addCell(label);
			}

			jxl.write.Number number;
			int j;
			i=0;
			for (TempBean tempBean : tb) {
				i++;
				j=0;
				number = new jxl.write.Number(j++,i, i);
				sheet.addCell(number);

				label = new Label(j++,i,  String.format("%d",tempBean.getVid()));
				sheet.addCell(label);

				label = new Label(j++,i,  ToolBox.getYMDHMS(tempBean.getTtime()));
				sheet.addCell(label);

				label = new Label(j++,i,  String.format("%d", tempBean.getTemp()));
				sheet.addCell(label);
			}



			book.write();
			book.close();


		       File f = new File(xlsfilename);
		       FileInputStream fis = new FileInputStream(f);
		       BufferedInputStream bis = new BufferedInputStream(fis);
		       byte[] buf = new byte[1024];
		       int len;
		       FileOutputStream fos = new FileOutputStream(zipfilename);
		       BufferedOutputStream bos = new BufferedOutputStream(fos);
		       ZipOutputStream zos = new ZipOutputStream(bos);//压缩包
		       ZipEntry ze = new ZipEntry(f.getName());//这是压缩包名里的文件名
		       zos.putNextEntry(ze);//写入新的 ZIP 文件条目并将流定位到条目数据的开始处

		       while((len=bis.read(buf))!=-1)
		       {
		          zos.write(buf,0,len);
		          zos.flush();
		       }

		       bis.close();
		       zos.close();

		       response.sendRedirect(filename+".zip");


		} catch (RowsExceededException e) {

			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
