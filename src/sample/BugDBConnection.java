package sample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.h2.jdbc.JdbcSQLException;

public class BugDBConnection {
	static public HashMap<String, Connection> connMap = new HashMap<String, Connection>();
	static public HashMap<String, ArrayList<String>> DomainList = new HashMap<>();
	static public ArrayList<String> ProjectList = new ArrayList<>();
	
	//static public HashMap<String,FieldSet> Domain = new HashMap<String, FieldSet>();
	//static public ArrayList<String> errorList = new ArrayList<String>();
	
	BugDBConnection() throws Exception
	{
		Class.forName("org.h2.Driver");
		String str;
		/*
		BufferedReader br0 = new BufferedReader(new FileReader("./data/domainDB.csv")); // whole data
		
		while((str=br0.readLine())!= null){
			String[] line = str.split(",");
			String domain = line[0];

			Connection conn = DriverManager.getConnection("jdbc:h2:./domainDB/"+domain,"sa","");
			System.out.println("-------- CONNECT WITH domain '"+domain+"' DB ----------");

			connMap.put(domain.toLowerCase(), conn); 
		}
		*/

		BufferedReader br = new BufferedReader(new FileReader("./data/domain.csv")); // whole data
		
		while((str=br.readLine())!= null){
			String[] line = str.split(",");
			String domain = line[0].toLowerCase();
			String project = line[1].toLowerCase().replace("?", "");

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

/*
		BufferedReader br = new BufferedReader(new FileReader("./data/datamap.csv")); // Screened data
		while((str=br.readLine())!= null){
			String[] line = str.split(",");
			String domain = line[0];
			String project = line[1];
//
			Connection conn = DriverManager.getConnection("jdbc:h2:./DB/"+domain+"/"+project,"sa","");
			System.out.println("-------- CONNECT WITH "+domain+" "+project+" DB ----------");

			connMap.put(domain.toLowerCase()+"-"+project.toLowerCase(), conn); 
		
			ProjectList.add(project);
			ProjectList.add(domain);
			
			if (!DomainList.containsKey(domain)){
				ArrayList<String> Projects=new ArrayList<>();
				Projects.add(project);
				DomainList.put(domain,Projects);
			}else{
				DomainList.get(domain).add(project);
			} // list domain and projects by using hashmap
		}
		*/

	}
	static public String filename="project_cnt_rate";
	
	public static void main(String[] args) throws Exception {

		new BugDBConnection();
	
		BufferedWriter bw = new BufferedWriter(new FileWriter("./data/"+filename+".csv"));
		bw.write("domain,project,prev,cnt,bool,number\n");
/*
		Connection conn = DriverManager.getConnection("jdbc:h2:./DB/assignee","sa","");
		System.out.println("-------- CONNECT WITH ASS DB ----------");
		
		Iterator<String> iter = DomainList.keySet().iterator();
*/
		Iterator<String> iter = connMap.keySet().iterator();

		try{
			while(iter.hasNext()){
				String key = iter.next();
				String domain = key.split("-")[0];
				String project = key.split("-")[1];
				Connection conn = connMap.get(key);
				
				System.out.println(key);

				Statement q = conn.createStatement();
				try{
					String prev="";
					int cnt=0;
					int num=0;
					int bool=0;
					
					ResultSet rs0= q.executeQuery("SELECT count(bug_id) as num FROM META_FIELD");
					rs0.next();
					num=rs0.getInt("num");
					
					ResultSet rs1= q.executeQuery("SELECT prev, count(prev) as cnt FROM history as h1 where field='product' and (date <= all (select date from history as h2 where h1.bug_id=h2.bug_id) ) group by prev order by cnt desc");
					while (rs1.next()){
						prev=rs1.getString("prev");
						cnt=rs1.getInt("cnt");
						if(prev.contains("mdt.")) prev.replace("mdt.", "");
						if(prev.contains(project)) prev=project;
		
						for(int i=0; i<DomainList.get(domain).size(); i++){
							String tmp=DomainList.get(domain).get(i);
							if (prev.equals(tmp)) {
								bool=1;
								break;
							}
						}
						bw.write(domain+","+project+","+prev+","+cnt+","+bool+","+num+"\n");
					}
					System.out.println("-------Complete");
				}catch(JdbcSQLException e){
					e.printStackTrace();
					System.out.println("---Exception");
				}
				conn.close();
			}
			bw.close();
			//AnalyzeCSV.PrintToExcel();
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
