package restsocket;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import application.LinkDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@Path("/service")
@ApplicationPath("/rest")
public class RestService extends Application{

	private ObservableList<LinkDetails> data;
	
	@GET
	//@Path("/access")
	@Produces(MediaType.APPLICATION_JSON)
	public ObservableList<LinkDetails> getUrl() throws ClassNotFoundException {
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			
			Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/urlshortener", "root", "");
			
			data = FXCollections.observableArrayList();
			ResultSet rs = con.createStatement().executeQuery("SELECT * from url");
			
			while(rs.next()) {	
				data.add(new LinkDetails(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));
				
			}
		} catch (SQLException ex) {
			
			System.err.println("Error: " + ex);
		}
		
		return data;
	}
	
	@GET
	@Path("{linkId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ObservableList<LinkDetails> getUrlById(@PathParam("linkId") Integer linkId) throws ClassNotFoundException {
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			
			Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/urlshortener", "root", "");
			
			data = FXCollections.observableArrayList();
			ResultSet rs = con.createStatement().executeQuery("SELECT * from url where id='"+linkId+"'");
			
			while(rs.next()) {	
				data.add(new LinkDetails(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));
				
			}
		} catch (SQLException ex) {
			
			System.err.println("Error: " + ex);
		}
		
		return data;
	}
}
