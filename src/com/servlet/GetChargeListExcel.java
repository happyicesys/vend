package com.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

import beans.CustomerBean;
import beans.CustomerChargeLogBean;
import beans.TradeBean;
import beans.UserBean;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Servlet implementation class GetChargeListExcel
 */
@WebServlet("/GetChargeListExcel")
public class GetChargeListExcel extends HttpServlet {
	private static final long serialVersionUID = 1L;
    //
	String[] TitleString=new String[]
			{
				"序号",
				"交易号",
				"充值管理员",
				"会员名",
				"卡号",
				"充值时间",
				"充值金额",
				"充值后余额"
			};
	
  /**
   * @see HttpServlet#HttpServlet()
   */
  public GetChargeListExcel() {
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
		
	    Date StartDate=ToolBox.filteDate(request.getParameter("sdate"));
	    Date EndDate=ToolBox.filteDate(request.getParameter("edate"));
		if((StartDate==null) && (EndDate==null))
		{
			StartDate=new Date(ClsTime.SystemTime());
			EndDate=new Date(ClsTime.SystemTime()+86400000);
		}else if((StartDate==null) && (EndDate!=null))
		{
			StartDate=ToolBox.addDate(EndDate,-1);
		}else if((StartDate!=null) && (EndDate==null))
		{
			EndDate=ToolBox.addDate(StartDate,1);
		}else
		{
			if (EndDate.before(StartDate))
			{
				Date t=EndDate;
				EndDate=StartDate;
				StartDate=t;
			}
		}

		String cardinfo=ToolBox.filter(request.getParameter("cardinfo"));
		String tradeid=ToolBox.filter(request.getParameter("tradeid"));
		int tradetype=ToolBox.filterInt(request.getParameter("tradetype"));


		String sql="groupid="+ub.getGroupid() +" and ";
		
		if(!tradeid.equals(""))
		{
			sql+=" tradeid like '%"+tradeid+"%' and";
		}
		
		if(!cardinfo.equals(""))
		{
			sql+=" cardinfo like '%"+cardinfo+"%' and";
		}
		sql+=" CAST(gmt AS datetime) between '"+StartDate+"' and '"+ToolBox.addDate(EndDate,1)+"'" ;
		
		int TotalCount=CustomerChargeLogBean.getLogRowsCount(sql);

		ArrayList<CustomerChargeLogBean> li=CustomerChargeLogBean.getCustomerBeanLst(sql,1,TotalCount);
		
		
		String filepath=this.getServletContext().getRealPath("/");
		String filename="./"+ub.getAdminusername()+"_"+ ToolBox.getDateString();
		String zipfilename=filepath+filename+".zip";
		String xlsfilename=filepath+filename+".xls";
      try {
			// 打开文件
			WritableWorkbook book = Workbook.createWorkbook(new File(xlsfilename));
			// 生成名为“第一页”的工作表，参数0表示这是第一页
			WritableSheet sheet = book.createSheet("第一页", 0);
			
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
			for (CustomerChargeLogBean log : li) {
				i++;
				j=0;
				number = new jxl.write.Number(j++,i, i);
				sheet.addCell(number);
				
				label = new Label(j++, i, log.getTradeid());
				sheet.addCell(label);
				
				label = new Label(j++,i,  log.getAdminname());
				sheet.addCell(label);
				
				label = new Label(j++,i,  log.getCustomername());
				sheet.addCell(label);
				
				label = new Label( j++,i, log.getCardinfo());
				sheet.addCell(label);
				
				label = new Label( j++,i, ToolBox.getTimeLongString(log.getGmt()));
				sheet.addCell(label);
				
				number = new jxl.write.Number(j++,i,   log.getAmount()/100.0);
				sheet.addCell(number);
				
				number = new jxl.write.Number(j++,i,log.getAmount_after_charge()/100.0);
				sheet.addCell(number);	
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
