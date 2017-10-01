package sample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.h2.jdbc.JdbcSQLException;

public class BugDBConnection {
	static public HashMap<String, Connection> connMap = new HashMap<String, Connection>();
	static public HashMap<String, ArrayList<String>> DomainList = new HashMap<>();
	static public String filename="assignee_project";
	
	//static public HashMap<String,FieldSet> Domain = new HashMap<String, FieldSet>();
	//static public ArrayList<String> errorList = new ArrayList<String>();
	
	BugDBConnection() throws Exception
	{
		Class.forName("org.h2.Driver");
		String str;
		/*
		BufferedReader br = new BufferedReader(new FileReader("./data/domain.csv")); // whole data
		
		while((str=br.readLine())!= null){
			String[] line = str.split(",");
			String domain = line[0];
			String project = line[1].replace("?", "");

			Connection conn = DriverManager.getConnection("jdbc:h2:./DB/"+domain+"/"+project,"sa","");
			System.out.println("-------- CONNECT WITH "+domain+" "+project+" DB ----------");

			connMap.put(domain.toLowerCase()+"-"+project.toLowerCase(), conn); 
		}
		*/

		BufferedReader br = new BufferedReader(new FileReader("./data/datamap.csv")); // Screened data
		while((str=br.readLine())!= null){
			String[] line = str.split(",");
			String domain = line[0];
			String project = line[1];
		
			Connection conn = DriverManager.getConnection("jdbc:h2:./DB/"+domain+"/"+project,"sa","");
			System.out.println("-------- CONNECT WITH "+domain+" "+project+" DB ----------");

			connMap.put(domain.toLowerCase()+"-"+project.toLowerCase(), conn); 
			
			if (!DomainList.containsKey(domain)){
				ArrayList<String> Projects=new ArrayList<>();
				Projects.add(project);
				DomainList.put(domain,Projects);
			}else{
				DomainList.get(domain).add(project);
			} // list domain and projects by using hashmap
		}
		
	}

	public static void main(String[] args) throws Exception {

		new BugDBConnection();
	
		BufferedWriter bw = new BufferedWriter(new FileWriter("./data/"+filename+".csv"));
		bw.write("domain,project,assignee\n");
		
		Iterator<String> iter = connMap.keySet().iterator();
		try{
			while(iter.hasNext()){
				String key = iter.next();
				String domain = key.split("-")[0];
				String project = key.split("-")[1];
				
				Connection conn = connMap.get(key);
				Statement q = conn.createStatement();
				//num=the number of bug reports in META_FIELD Table
				System.out.println(key);
				int num=0;
				ResultSet rs = q.executeQuery("SELECT count(*) as count FROM META_FIELD");		
				while(rs.next()){
					num=rs.getInt("count");
				}
				
				int hnum=0;
				ResultSet rs0=q.executeQuery("SELECT count(*) as count FROM HISTORY");
				while (rs0.next()){
					hnum=rs0.getInt("count");
				}
				
				if (hnum!=0){ // if it's not empty project
					try{
						String ass;
	
						ResultSet rs1= q.executeQuery("SELECT distinct assignee FROM META_FIELD order by assignee asc");
						while (rs1.next()){
							ass=rs1.getString("assignee");
							bw.write(domain+","+project+","+ass+"\n");
						}
						System.out.println("-------Complete");
					}catch(JdbcSQLException e){
						e.printStackTrace();
						System.out.println("---Exception");
					}
				}
				conn.close();	
			}
			bw.close();
			AnalyzeCSV.PrintToExcel();
		}
		catch(Exception e1)
		{	
			//System.out.println("Error!");
			e1.printStackTrace();
		}
	}
}

	/*
	static public void find_field(String fieldname, Field field,int count){
		switch(fieldname){
		case "os":
			field.os=count; break;
		case "priority":
			field.priority=count; break;
		case "hardware":
			field.hardware=count; break;
		case "version":
			field.version=count; break;
		case "component":
			field.component=count; break;
		case "severity":
			field.severity=count; break;
		case "assignee":
			field.assignee=count; break;
		case "product":
			field.product=count; break;
		}
	}
	public static void main(String[] args) throws Exception {
		//define BufferedWriter for Q1~Q4
		new BugDBConnection();
		
		BufferedWriter bw = new BufferedWriter(new FileWriter("./data/counting.csv"));
		bw.write("domain,project,num,total\n");
		
		Iterator<String> iter = connMap.keySet().iterator();
		try{
			while(iter.hasNext()){
				String key = iter.next();
				String domain = key.split("-")[0];
				String project = key.split("-")[1];
				
				Connection conn = connMap.get(key);
				Statement q = conn.createStatement();
				//num=the number of bug reports in META_FIELD Table
				System.out.println(key);
				int num=0;
				ResultSet rs = q.executeQuery("SELECT count(*) as count FROM META_FIELD");		
				while(rs.next()){
					num=rs.getInt("count");
				}
				//FieldSet fieldset=new FieldSet();
				//fieldset.set_Fieldset(key, num);
				//hnum=the number of history logs in HISTORY Table (if hnum=0 then pass)
				int hnum=0;
				ResultSet rs0=q.executeQuery("SELECT count(*) as count FROM HISTORY");
				while (rs0.next()){
					hnum=rs0.getInt("count");
				}
				
				if (hnum!=0){
					try{
						int cou=0;
						
						ResultSet rs1= q.executeQuery("SELECT count(distinct bug_id) as count FROM BEASS2 ");
						while (rs1.next()){
							cou=rs1.getInt("count");
							bw.write(domain+","+project+","+cou+","+num+"\n");
						}
						System.out.println("-------Complete");
					}catch(JdbcSQLException e){
						e.printStackTrace();
						System.out.println("---Exception");
					}
	
					new AboutQ1().print_res(q, key, fieldset.field);
					new AboutQ2().print_res(q, key, fieldset.refield);
					new AboutQ3().print_res(q, key, fieldset.f1,fieldset.f2,fieldset.f3,fieldset.f4);
					new AboutQ4().print_res(q, key, fieldset.avg,fieldset.min,fieldset.max);
				}
				//Domain.put(key, fieldset); //close the connection
				conn.close();	
			}
			//PrintResult.PrintToCsv();
			//AnalyzeQ3.PrintToExcel();
			bw.close();
		}
		catch(Exception e1)
		{	
			//System.out.println("Error!");
			e1.printStackTrace();
		}
	}
}
*/
