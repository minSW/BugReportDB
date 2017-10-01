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
	// domain-project hashmap 작성
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
		
				//엑셀의 행 
				HSSFRow row=null;
				//엑셀의 셀 
				HSSFCell cell=null;
				//임의의 DB데이터 조회 
				
				BufferedReader br = new BufferedReader(new FileReader("./data/"+BugDBConnection.filename+".csv"));
				int i=0;
				String str=br.readLine(); //skip the first line
				String[] line =str.split(",");	
		        row=sheet.createRow((short)i);
		        i++;
		        if(line !=null &&line.length >0){
		            for(int j=0;j<line.length;j++){
		                //생성된 row에 컬럼을 생성한다 
		                cell=row.createCell(j);
		                //map에 담긴 데이터를 가져와 cell에 add한다 
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
					                //생성된 row에 컬럼을 생성한다 
					                cell=row.createCell(j);
					                //map에 담긴 데이터를 가져와 cell에 add한다 
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