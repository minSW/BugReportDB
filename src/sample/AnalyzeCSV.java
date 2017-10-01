package sample;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class AnalyzeCSV {

	AnalyzeCSV() throws Exception
	{
	// domain-project hashmap �ۼ�
	}
	
	public static void PrintToExcel() throws Exception {
		// TODO Auto-generated method stub
		
		HSSFWorkbook workbook=new HSSFWorkbook();
		new AnalyzeCSV();
		
		Iterator<String> it = BugDBConnection.DomainList.keySet().iterator();
		try{
			while(it.hasNext()){
				String domain = it.next();
				HSSFSheet sheet=workbook.createSheet(domain);
		
				//������ �� 
				HSSFRow row=null;
				//������ �� 
				HSSFCell cell=null;
				//������ DB������ ��ȸ 
				
				BufferedReader br = new BufferedReader(new FileReader("./data/"+BugDBConnection.filename+".csv"));
				int i=0;
				String str=br.readLine(); //skip the first line
				String[] line =str.split(",");	
		        row=sheet.createRow((short)i);
		        i++;
		        if(line !=null &&line.length >0){
		            for(int j=0;j<line.length;j++){
		                //������ row�� �÷��� �����Ѵ� 
		                cell=row.createCell(j);
		                //map�� ��� �����͸� ������ cell�� add�Ѵ� 
		                cell.setCellValue(line[j]);
		            }
		        }
		        
				while((str=br.readLine())!= null){
					line =str.split(",");	
					if(domain.equals(line[0])){ 
					        row=sheet.createRow((short)i);
					        i++;
					        if(line !=null &&line.length >0){
					            for(int j=0;j<line.length;j++){
					                //������ row�� �÷��� �����Ѵ� 
					                cell=row.createCell(j);
					                //map�� ��� �����͸� ������ cell�� add�Ѵ� 
					                cell.setCellValue(line[j]);
					            }
					        }
					    }
					}
				}	
		}
		finally{};

		FileOutputStream fileoutputstream=new FileOutputStream("./data/Analyze_"+BugDBConnection.filename+".xls");
		workbook.write(fileoutputstream);
		fileoutputstream.close();
		
		System.out.println("create");

	}

}