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
	static public HashMap<String, Connection> connMap = new HashMap<String, Connection>();;
	static public ArrayList<String> errorList = new ArrayList<String>();
	
	BugDBConnection() throws Exception
	{
		Class.forName("org.h2.Driver");
		BufferedReader br = new BufferedReader(new FileReader("./data/domain.csv"));
		String str;

		while((str=br.readLine())!= null){
			String[] line = str.split(",");
			String domain = line[0];
			String project = line[1].replace("?", "");

			Connection conn = DriverManager.getConnection("jdbc:h2:./DB/"+domain+"/"+project,"sa","");
			System.out.println("-------- CONNECT WITH "+domain+" "+project+" DB ----------");

			connMap.put(domain.toLowerCase()+"-"+project.toLowerCase(), conn); 
		
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
	
	public static void main(String[] args) throws Exception {
		//define BufferedWriter for Q1~Q4
		new BugDBConnection();
		
		BufferedWriter bw = new BufferedWriter(new FileWriter("./data/ass.csv"));
		bw.write("domain,project,type,1,2,3,4,sum,total\n");
		
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
						int n1=0,n2=0,n3=0,n4=0,n_sum=0;
						ResultSet rs1= q.executeQuery("select reasscount, count(bug_id) from (select bug_id, case when count<4 then count else '4' end as reasscount from (SELECT bug_id, count(bug_id) as count FROM HISTORY where field='assignee' group by bug_id) ) group by reasscount ");
						while (rs1.next()){
							String reass=rs1.getString("reasscount");
							int count=rs1.getInt("count(bug_id)");
							switch(reass){
								case "1": n1=count; break;
								case "2": n2=count; break;
								case "3": n3=count; break;
								case "4": n4=count; break;
							}
						}
						n_sum=n1+n2+n3+n4;
						bw.write(domain+","+project+","+"ALL"+","+n1+","+n2+","+n3+","+n4+","+n_sum+","+num+"\n");
						
						int m1=0,m2=0,m3=0,m4=0,m_sum=0;
						ResultSet rs2=q.executeQuery("select reasscount, count(B_ID) from (select b_id, case when count<4 then count else '4' end as reasscount from (select b_id, count(B_id) as count from (select bug_id as B_ID from beass2 group by bug_id), history as h where B_ID=h.bug_id and h.field='assignee' group by B_ID) ) group by reasscount");
						while (rs2.next()){
							String reass=rs2.getString("reasscount");
							int count=rs2.getInt("count(B_ID)");
							switch(reass){
								case "1": m1=count; break;
								case "2": m2=count; break;
								case "3": m3=count; break;
								case "4": m4=count; break;
							}
						}
						m_sum=m1+m2+m3+m4;
						bw.write(domain+","+project+","+"Y"+","+m1+","+m2+","+m3+","+m4+","+m_sum+","+num+"\n");
						
						bw.write(domain+","+project+","+"N"+","+(n1-m1)+","+(n2-m2)+","+(n3-m3)+","+(n4-m4)+","+(n_sum-m_sum)+","+num+"\n");
						System.out.println("-------Complete");
					}catch(JdbcSQLException e){
						e.printStackTrace();
						System.out.println("---Exception");
					}
					/*
					new AboutQ1().print_res(q, key, fieldset.field);
					new AboutQ2().print_res(q, key, fieldset.refield);
					new AboutQ3().print_res(q, key, fieldset.f1,fieldset.f2,fieldset.f3,fieldset.f4);
					new AboutQ4().print_res(q, key, fieldset.avg,fieldset.min,fieldset.max);
					*/
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
