package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DbConnect {
	private Connection con;
	private Statement st;
	private ResultSet rs;
	
	public DbConnect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/urlshortener", "root", "");
			st = con.createStatement();
			
		} catch (Exception ex) {
			System.out.println("Erro: "+ex);
		}
  	}
	
	public int addUrl(String longurl) {
		
		int lastId = 0;
		try {
			
			String query = "INSERT INTO longurl (longurl) VALUES('"+longurl+"')";
			System.out.println("Inserted Longurl in database");
			
			// Getting last inserted ID
			st = con.createStatement();
			st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			
			rs = st.getGeneratedKeys();
			if(rs.next()) {
				lastId = rs.getInt(1);
			}
			
		} catch (Exception ex) {
			System.out.println("Erro: "+ex);
		}
		return lastId;
	}
	
	public void addRecord(int id, String longurl, String shorturl) {
		try {
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE,2);
			String elapseTime = dateFormat.format(c.getTime());
			
			String query = "INSERT INTO url (id,longurl,shorturl,time) VALUES('"+id+"','"+longurl+"','"+shorturl+"','"+elapseTime+"')";
			st.execute(query);
			System.out.println("Inserted Url Record in database");
			
		} catch (Exception ex) {
			System.out.println("Erro: "+ex);
		}
	}
	
	public void deleteAllData() {
		try {
			//String query = "UPDATE tasks SET description="+desc+", category="+category+" WHERE id=1";
			String query = "DELETE FROM `url` WHERE 1";
			st.execute(query);
			
			String query1 = "DELETE FROM `longurl` WHERE 1";
			st.execute(query1);
			
			//UPDATE `tasks` SET `description`= "hello task" ,`category`= "hello" WHERE id=1
			System.out.println("Deleted from database");
			
			
		} catch (Exception ex) {
			System.out.println("Erro: "+ex);
		}
	}
	
	public String getActualURL(String shortUrl) {
		
		String longUrl = "";
	
		try {
			
			String query = "SELECT id,longurl FROM url where BINARY shorturl='"+shortUrl+"'";
			rs = st.executeQuery(query);
		
			//rs = con.createStatement().executeQuery("SELECT id,longurl FROM url where shorturl='"+shortUrl+"'");
			System.out.println("2------------------rs: "+rs);
			int id = 0;
			while(rs.next()) {
				id = rs.getInt("id");
				longUrl = rs.getString("longurl");
			}
		
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			Calendar c = Calendar.getInstance();
			String date = dateFormat.format(c.getTime());
			
			String query1 = "INSERT INTO linkaccessed (linkid,date) VALUES('"+id+"','"+date+"')";
			st.execute(query1);
			System.out.println("Inserted Linkaccessed in database");
			
		} catch (Exception ex) {
			System.out.println("Erro: "+ex);
		}
		return longUrl;
	}
	
	public boolean linkExpired(String shortUrl) throws ParseException, SQLException {
		String date = "";
		rs = con.createStatement().executeQuery("SELECT time from url where shorturl='"+shortUrl+"'");
		while(rs.next()) {
			date = rs.getString(1);
		}
		
		//lblValidUpto.setText(date);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		String now = dateFormat.format(c.getTime());
		System.out.println(now);
		
		java.util.Date future = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
		java.util.Date present = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(now);
	
		long different = future.getTime() - present.getTime();
		if (different < 0) {
			return true;
		}
		else
			return false;
	}
	
}
