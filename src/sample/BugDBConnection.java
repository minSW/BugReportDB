package sample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class BugDBConnection {
	static public HashMap<String, Connection> connMap = new HashMap<String, Connection>();;
	static public ArrayList<String> CompoList = new ArrayList<String>();
	static public Connection conn;
	
	BugDBConnection() throws Exception
	{
		Class.forName("org.h2.Driver");
		
		conn = DriverManager.getConnection("jdbc:h2:./DB/compo","sa","");
		System.out.println("CONNECT");
		/*
		BufferedReader br = new BufferedReader(new FileReader("./data/domain.csv"));
		String str;

		while((str=br.readLine())!= null){
			String[] line = str.split(",");
			String domain = line[0];
			String project = line[1].replace("?", "");

			Connection conn = DriverManager.getConnection("jdbc:h2:./DB/"+domain+"/"+project,"sa","");
			//System.out.println("-------- CONNECT WITH "+domain+" "+project+" DB ----------");

			connMap.put(domain.toLowerCase()+"-"+project.toLowerCase(), conn); 
		
		}
		*/
	}

	static void CreateComponentSet() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader("./data/component.csv"));
		String str;
		while((str=br.readLine())!= null){
			String[] line = str.split(",");
			String component = line[0];
			CompoList.add(component);
		}
	}
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

	static public HashMap<String,FieldSet> Domain = new HashMap<String, FieldSet>();
	static public BufferedWriter bw;
	
	public static void main(String[] args) throws Exception {
		//define BufferedWriter for Q1~Q4
		new BugDBConnection();
		CreateComponentSet();
		
		//bw = new BufferedWriter(new FileWriter("./data/componentReport.csv"));	
		//Iterator<String> iter = connMap.keySet().iterator();
		Iterator<String> iter = CompoList.iterator();
		try{
			while(iter.hasNext()){
				String key = iter.next();
				Statement q = conn.createStatement();
				//num=the number of bug reports in META_FIELD Table
				System.out.println(key);
				int num=0;
				ResultSet rs = q.executeQuery("SELECT count(*) as count FROM history where component='"+key+"'");		
				while(rs.next()){
					num=rs.getInt("count");
				}
				FieldSet fieldset=new FieldSet();
				fieldset.set_Fieldset(key, num);
				//hnum=the number of history logs in HISTORY Table (if hnum=0 then pass)
				if (num!=0){
					
					/*
					try{
					//q.execute("CREATE TABLE BEASS2 (BUG_ID INT, DATE VARCHAR(128), FIELD VARCHAR(128), PREV VARCHAR(128), POST VARCHAR(128) )");
					//q.execute("INSERT INTO BEASS2(BUG_ID,DATE,FIELD,PREV,POST) SELECT BUG_ID,DATE,FIELD,PREV,POST FROM BEASS as b1 where PARSEDATETIME(date,'yyyy-MM-dd hh:mm:ss')  <= all (select PARSEDATETIME(b2.date,'yyyy-MM-dd hh:mm:ss') from beass as b2 where b2.post='assigned' and b1.bug_id=b2.bug_id) order by date asc");
					q.execute("delete FROM BEASS2 where prev='new' and post='assigned'");
					}catch(JdbcSQLException e){
						System.out.println("---Exist");
					}
					*/
					new AboutQ1().print_res(q, key, fieldset.field);
					new AboutQ2().print_res(q, key, fieldset.refield);
					new AboutQ3().print_res(q, key, fieldset.f1,fieldset.f2,fieldset.f3,fieldset.f4);
					//new AboutQ4().print_res(q, key, fieldset.avg,fieldset.min,fieldset.max);

					//new ComponentCSV().toCSV(q, key);
				}
				Domain.put(key, fieldset); //close the connection
			}
			
			PrintResult.PrintToCsv();
			//AnalyzeQ3.PrintToExcel();
			//bw.close();
			conn.close();	
		}
		catch(Exception e1)
		{	
			//System.out.println("Error!");
			e1.printStackTrace();
		}
	}

}
