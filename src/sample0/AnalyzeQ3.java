package sample;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class AnalyzeQ3 {
	//static public ArrayList<String> DomainList = new ArrayList<>();
	static public HashMap<String, ArrayList<String>> DomainList = new HashMap<>();
	
	AnalyzeQ3() throws Exception
	{
		Iterator<String> iter = BugDBConnection.Domain.keySet().iterator();
		try{
			while(iter.hasNext()){
				String key = iter.next();
				if (!DomainList.containsKey(key)){
					ArrayList<String> Projects=new ArrayList<>();
					Projects.add(project);
					DomainList.put(domain,Projects);
				}else{
					DomainList.get(domain).add(project);
				}
			}
		}
		finally{};
	// domain-project hashmap �ۼ�
	}
	public static void PrintToExcel() throws Exception {
		// TODO Auto-generated method stub
		
		HSSFWorkbook workbook=new HSSFWorkbook();
		new AnalyzeQ3();
		
		Iterator<String> it = DomainList.keySet().iterator();
		try{
			while(it.hasNext()){
				String domain = it.next();
				HSSFSheet sheet=workbook.createSheet(domain);
		
				//������ �� 
				HSSFRow row=null;
				//������ �� 
				HSSFCell cell=null;
				//������ DB������ ��ȸ 
				
				BufferedReader br = new BufferedReader(new FileReader("./data/output3.csv"));
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

		FileOutputStream fileoutputstream=new FileOutputStream("./data/analyzeQ3.xls");
		workbook.write(fileoutputstream);
		fileoutputstream.close();
		
		System.out.println("create");

	}

}