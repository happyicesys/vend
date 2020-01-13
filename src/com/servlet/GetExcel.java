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

import beans.TradeBean;
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
 * Servlet implementation class GetExcel
 */
public class GetExcel extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	String[] TitleString=new String[]{
			  "#",
			  "Transaction #",
			  "Order #",
			  "Date Time",
			  "Bank Card #",
			  "Amount",
			  "Change",
			  "Method",
			  "Machine #",
			  "Channel #",
			  "Product",
			  "Payment Status",
			  "Dispense Status"
			  };
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetExcel() {
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
	    String orderId=ToolBox.filter(request.getParameter("orderid"));
		String  SellerId =ToolBox.filter(request.getParameter("sellerid"));
		String PortId= ToolBox.filter(request.getParameter("portid"));
		String CardNumber=ToolBox.filter(request.getParameter("CardNumber"));
		int Success=ToolBox.filterInt(request.getParameter("success"));
		//int maxrows=ToolBox.filterInt(request.getParameter("maxrows"));
		int tradetype=ToolBox.filterInt(request.getParameter("tradetype"));
		int pageindex=ToolBox.filterInt(request.getParameter("pageindex"));
		//if(maxrows<=0)
		//{
		//	maxrows=500;
		//}
		if(pageindex<=0)
		{
			pageindex=1;
		}
		String sql="";
		if(!orderId.equals(""))
		{
			sql+=" traderecordinfo.liushuiid like '%"+orderId+"%' and";
		}
		if(!CardNumber.equals(""))
		{
			sql+=" traderecordinfo.mobilephone like '%"+CardNumber+"%' and";
		}
		if(!SellerId.equals(""))
		{
			SellerId=SellerId.replaceAll("，",",");
			String[] seller;
			seller=SellerId.split(",",0);
			SellerId="";
			for(int i=0;i<seller.length;i++)
			{
				int vid=ToolBox.filterInt(seller[i]);
				//检查所输入的是否全部位数字
				if(vid>0)
				{
					if(ub.CanAccessSeller(vid))
					{
						SellerId+=vid+",";
					}
				}
			}
			if(SellerId.endsWith(",")) 
			{
				SellerId=SellerId.substring(0,SellerId.length()-1);
			}
			sql+=" (traderecordinfo.goodmachineid in("+SellerId+")) and";
		}
		else
		{
			sql+=" (traderecordinfo.goodmachineid in("+ub.getCanAccessSellerid()+")) and";
		}
		if(!PortId.equals(""))
		{
			PortId=PortId.replaceAll("，",",");
			sql+=" (traderecordinfo.goodroadid in("+PortId+")) and";
		}
		if(Success==1)//成功的
		{
			sql+=" traderecordinfo.sendstatus=1 and";
		}else if(Success==2)//不成功的
		{
			sql+=" traderecordinfo.sendstatus=0 and";
		}
		
		if(tradetype==clsConst.TRADE_TYPE_NO_LIMIT)
		{
			//sql+=" traderecordinfo.tradetype=0 and";
		}
		else 
		{
			sql+=" traderecordinfo.tradetype="+ tradetype +" and";
		}
		
		sql+=" traderecordinfo.receivetime between '"+StartDate+"' and '"+ToolBox.addDate(EndDate,1)+"' and liushuiid<>''" ;
		
		int TotalCount=SqlADO.getTradeRowsCount(sql);

		System.out.println(sql);
		ArrayList<TradeBean> tbli=SqlADO.getTradeList(sql,1,TotalCount);
		
		
		String filepath=this.getServletContext().getRealPath("/");
		String filename="./"+ub.getAdminusername()+"_"+ ToolBox.getDateString();
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
			for (TradeBean tradeBean : tbli) {
				i++;
				j=0;
				number = new jxl.write.Number(j++,i, i);
				sheet.addCell(number);
				
				label = new Label(j++, i, tradeBean.getLiushuiid());
				sheet.addCell(label);
				
				label = new Label(j++,i,  tradeBean.getOrderid());
				sheet.addCell(label);
				
				label = new Label(j++,i,  ToolBox.getYMDHMS(tradeBean.getReceivetime()));
				sheet.addCell(label);
				
				label = new Label( j++,i, tradeBean.getCardinfo());
				sheet.addCell(label);
				
				number = new jxl.write.Number(j++,i,   tradeBean.getPrice()/100.0);
				sheet.addCell(number);
				
				number = new jxl.write.Number(j++,i,tradeBean.getChanges()/100.0);
				sheet.addCell(number);

				int tradetype_int=tradeBean.getTradetype();
				String tradetype_name=null;
				switch (tradetype_int) {
				case clsConst.TRADE_TYPE_CASH:
					tradetype_name="Cash";
					break;
				case clsConst.TRADE_TYPE_AL_QR:
					tradetype_name="Alipay";
					break;
				case clsConst.TRADE_TYPE_BANK:
					tradetype_name="Debit Card";
					break;
				case clsConst.TRADE_TYPE_CARD:
					tradetype_name="Credit Card";
					break;
				case clsConst.TRADE_TYPE_GOODS_CODE:
					tradetype_name="Passcode";
					break;
				case clsConst.TRADE_TYPE_AL_SOUND:
					tradetype_name="Sonic Pay";
					break;
					
				case clsConst.TRADE_TYPE_SANFU:
					tradetype_name="Union Pay";
					break;
					
				case clsConst.TRADE_TYPE_WX_QR:
					tradetype_name="Wechat";
					break;
				case clsConst.TRADE_TYPE_COCO:
					tradetype_name="FreeVend";
					break;
				default:
					break;
				}
				
				label = new Label(j++,i,  tradetype_name);
				sheet.addCell(label);	
				
				
				label = new Label(j++,i,  String.format("%03d",tradeBean.getGoodmachineid()));
				sheet.addCell(label);		
				
				label = new Label(j++,i,  String.format("%02d",tradeBean.getGoodroadid()));
				sheet.addCell(label);	
				
				label = new Label(j++,i,  tradeBean.getGoodsName());
				sheet.addCell(label);					
				
				label = new Label(j++,i,  ((tradeBean.getChangestatus()!=0)?"Success":"Failure"));
				sheet.addCell(label);		
				
				label = new Label(j++,i,  ((tradeBean.getSendstatus()!=0)?"Success":"Failure"));
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
