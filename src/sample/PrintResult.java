package sample;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class PrintResult {
	private static String component;
	
	public static void PrintQ1(BufferedWriter bw1,Field field) throws IOException{
		bw1.write(component+","+field.os+","+field.priority+","+field.hardware+","+field.version+","+field.component+","+field.severity+","+field.assignee+","+field.product+","+field.status+","+field.total+"\n");
	}
	
	public static void PrintQ2(BufferedWriter bw2, Field.Refield refield) throws IOException{
		bw2.write(component+","+refield.n0+","+refield.n1+","+refield.n2+","+refield.n3+","+refield.n4+","+refield.total+"\n");
	}
	public static void PrintQ3(BufferedWriter bw3, Field f1,Field f2,Field f3,Field f4) throws IOException{
		bw3.write(component+","+"os,"+f1.os+","+f2.os+","+f3.os+","+f4.os+","+f1.total+"\n");
		bw3.write(component+","+"priority,"+f1.priority+","+f2.priority+","+f3.priority+","+f4.priority+","+f1.total+"\n");
		bw3.write(component+","+"hardware,"+f1.hardware+","+f2.hardware+","+f3.hardware+","+f4.hardware+","+f1.total+"\n");
		bw3.write(component+","+"version,"+f1.version+","+f2.version+","+f3.version+","+f4.version+","+f1.total+"\n");
		bw3.write(component+","+"component,"+f1.component+","+f2.component+","+f3.component+","+f4.component+","+f1.total+"\n");
		bw3.write(component+","+"severity,"+f1.severity+","+f2.severity+","+f3.severity+","+f4.severity+","+f1.total+"\n");
		bw3.write(component+","+"assignee,"+f1.assignee+","+f2.assignee+","+f3.assignee+","+f4.assignee+","+f1.total+"\n");
		bw3.write(component+","+"product,"+f1.product+","+f2.product+","+f3.product+","+f4.product+","+f1.total+"\n");
		bw3.write(component+","+"status,"+f1.status+","+f2.status+","+f3.status+","+f4.status+","+f1.total+"\n");
	}
	public static void PrintQ4(BufferedWriter bw4,Field avg, Field min, Field max) throws IOException{
		bw4.write(component+","+"avg,"+avg.os+","+avg.priority+","+avg.hardware+","+avg.version+","+avg.component+","+avg.severity+","+avg.assignee+","+avg.product+","+avg.status+","+avg.total+"\n");
		bw4.write(component+","+"min,"+min.os+","+min.priority+","+min.hardware+","+min.version+","+min.component+","+min.severity+","+min.assignee+","+min.product+","+min.status+","+min.total+"\n");
		bw4.write(component+","+"max,"+max.os+","+max.priority+","+max.hardware+","+max.version+","+max.component+","+max.severity+","+max.assignee+","+max.product+","+max.status+","+max.total+"\n");
	}
	
	public static void Print_attribute(BufferedWriter bw1, BufferedWriter bw2,BufferedWriter bw3,BufferedWriter bw4) throws Exception{
		bw1.write("component,os,priority,hardware,version,component,severity,assignee,product,status,total\n");
		bw2.write("component,0,1,2,3,4,total\n");
		bw3.write("component,field,1,2,3,4,total\n");
		bw4.write("component,value,os,priority,hardware,version,component,severity,assignee,product,status,total\n");		
	}
	
	public static void PrintToCsv() throws Exception {
		// TODO Auto-generated method stub
		BufferedWriter bw1 = new BufferedWriter(new FileWriter("./data/output1.csv"));
		BufferedWriter bw2 = new BufferedWriter(new FileWriter("./data/output2.csv"));
		BufferedWriter bw3 = new BufferedWriter(new FileWriter("./data/output3.csv"));
		BufferedWriter bw4 = new BufferedWriter(new FileWriter("./data/output4.csv"));
		
		//print the name of attributes in Table
		Print_attribute(bw1,bw2,bw3,bw4);
		Iterator<String> iter = BugDBConnection.Domain.keySet().iterator();
		try{
			while(iter.hasNext()){
				String key = iter.next();
				
				if(key.contains(".")){
					String[] temp = key.split("\\.");
					component=temp[temp.length-1];
				}else if(key.contains("-")){
					String[] temp = key.split("-");
					component=temp[temp.length-1];
				}
				else {
					component=key;
				}
				
				FieldSet fieldset = BugDBConnection.Domain.get(key);
				PrintQ1(bw1,fieldset.field);
				PrintQ2(bw2, fieldset.refield);
				PrintQ3(bw3, fieldset.f1, fieldset.f2, fieldset.f3, fieldset.f4);
				PrintQ4(bw4, fieldset.avg, fieldset.min, fieldset.max);
				
			}
			bw1.close();
			bw2.close();
			bw3.close();
			bw4.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
