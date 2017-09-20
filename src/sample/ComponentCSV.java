package sample;

import java.sql.ResultSet;
import java.sql.Statement;

public class ComponentCSV {

	void toCSV(Statement q, String key) throws Exception{

		String component;
		String bug_id, field, prev, post;
		String date;
		
		System.out.println("================="+key+" Success======================");

		try
		{
				ResultSet rs1 = q.executeQuery("select m.component, b.* from meta_field as m,beass2 as b where m.bug_id=b.bug_id");			
				
				while(rs1.next()){
					component=rs1.getString("component");
					bug_id=rs1.getString("bug_id");
					date=rs1.getString("date");
					field=rs1.getString("field");
					prev=rs1.getString("prev");
					post=rs1.getString("post");
					BugDBConnection.bw.write(component+","+bug_id+","+date+","+field+","+prev+","+post+"\n");
					
				}	
		}
		catch(Exception e1)
		{	
			//System.out.println("Error!");
			e1.printStackTrace();
		}
	}
	public static void main(String[] args) {

//select m.component, b.* from meta_field as m, ( select * FROM BEASS2 WHERE field !='status' and field !='resolution' ) as b where m.bug_id=b.bug_id
		System.out.println("success");
	}

}


