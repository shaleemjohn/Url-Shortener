package application;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.sun.prism.impl.Disposer.Record;

import database.DbConnect;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;


// for accessing data
public class Controller implements Initializable {
	
	@FXML
	private TextField txtShortURL;
	@FXML
	private TextField txturl;

	@FXML
	private TableView<LinkDetails> tableURL;
	@FXML
	private TableColumn<LinkDetails, String> columnID;
	@FXML
	private TableColumn<LinkDetails, String> columnLongURL;
	@FXML
	private TableColumn<LinkDetails, String> columnShortURL;
	@FXML
	private TableColumn<LinkDetails, String> columnValid;
	
	@FXML
	LineChart<String, Number> lineChart;
	@FXML
	BarChart<String, Number> barChart;
	@FXML
	private TextField txtCurrent;
	@FXML
	private Label lblClicks;
	@FXML
	private Label lblValidUpto;
	@FXML
	private Label lblError;
	@FXML
	private Label lblExpiredon;
	
	public static int currentId;
	public static int action = 0;
	ResultSet rs;
	Statement st;
	private ObservableList<LinkDetails> data;
	private DbConnect db;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		db = new DbConnect();
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes", "restriction" })
	@FXML
	public void getLinksFromDB() {
		try {
			Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/urlshortener", "root", "");
			
			data = FXCollections.observableArrayList();
			ResultSet rs = con.createStatement().executeQuery("SELECT * from url");
			
			while(rs.next()) {
				data.add(new LinkDetails(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));
				
			}
		} catch (SQLException ex) {
			
			System.err.println("Error: " + ex);
		}
		
		columnID.setCellValueFactory(new PropertyValueFactory<>("ID"));
		columnLongURL.setCellValueFactory(new PropertyValueFactory<>("LongURL"));
		columnShortURL.setCellValueFactory(new PropertyValueFactory<>("ShortURL"));
		columnValid.setCellValueFactory(new PropertyValueFactory<>("Elapsed"));
		
		tableURL.setItems(null);
		tableURL.setItems(data);
		
		if (action == 0) {
			TableColumn col_action = new TableColumn<>("Action");
			tableURL.getColumns().add(col_action);
			col_action.setCellValueFactory(
					new Callback<TableColumn.CellDataFeatures<Record,Boolean>,
					ObservableValue<Boolean>>() {
						
						@Override
						public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Record, Boolean> p) {
							return new SimpleBooleanProperty(p.getValue() != null);
						}
					});
			
			col_action.setCellFactory(
					new Callback<TableColumn<Record,Boolean>,TableCell<Record,Boolean>>() {
						
						@Override
						public TableCell<Record,Boolean> call(TableColumn<Record,Boolean> p) {
							return new ButtonCell();
						}
					}
					
				);
			
			action = 1;
		}
		
	}
	
	@SuppressWarnings("restriction")
	private class ButtonCell extends TableCell<Record, Boolean> {
		final Button cellButton = new Button("Dashboard");
		
		ButtonCell() {
			
			//Action when the button is pressed
			cellButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent t) {
					//get Selected Item
					LinkDetails current = (LinkDetails) ButtonCell.this.getTableView().getItems().get(ButtonCell.this.getIndex());
					// remove selected item from table list
				
					currentId = Integer.parseInt(current.getID());
					System.out.println(currentId);
					//data.remove(current);
					
					showDashboard();
					
				}
			});
		}
		
		//Display button if row is not empty
		@Override
		protected void updateItem(Boolean t, boolean empty) {
			super.updateItem(t, empty);
			if (!empty) {
				setGraphic(cellButton);
			}
			else {
				setGraphic(null);
			}
		}
	}
	
	public void showDashboard() {
		try {
			
			Parent root = FXMLLoader.load(getClass().getResource("Screen2.fxml"));
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			scene.getStylesheets().add(getClass().getResource("bootstrap.css").toExternalForm());
			
			stage.setScene(scene);
			stage.show();
			
		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
	}
	
	public void generateCharts() throws SQLException, ParseException {
		
		System.out.println(currentId);
		//String query = "SELECT date, COUNT(*) from linkaccessed group by date where date='"+currentId+"'";
		String query = "SELECT date, count(*) from linkaccessed where linkid='"+currentId+"' group by date";
		Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/urlshortener", "root", "");
		
		ResultSet rs = con.createStatement().executeQuery(query);
		
		//rs = st.executeQuery(query);
		
		System.out.println("Records form database");
		
		lineChart.getData().clear();
		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
		
		while(rs.next()) {
			String date = rs.getString(1);
			int count = rs.getInt(2);
			series.getData().add(new XYChart.Data<String, Number>(date,count));
					
		}
		
		series.setName("No. of Visits");
		lineChart.getData().add(series);
		
		for (final XYChart.Data<String, Number> data : series.getData()) {
			data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					Tooltip.install(data.getNode(), new Tooltip("Day: "+data.getXValue()+"\nVisits: "+String.valueOf(data.getYValue())));					
				}
				
			});
		}
		
		int clicks = 0;
		rs = con.createStatement().executeQuery("SELECT count(*) from linkaccessed where linkid='"+currentId+"'");
		while(rs.next()) {
			clicks = rs.getInt(1);
		}
		
		lblClicks.setText(Integer.toString(clicks));
		
		String date = "";
		rs = con.createStatement().executeQuery("SELECT time from url where id='"+currentId+"'");
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
		 
		System.out.println(future);
		System.out.println(present);
	
		printDifference(present, future);
		
	}
	
	public void printDifference(java.util.Date present, java.util.Date future) {
		
		long different = future.getTime() - present.getTime();
		//System.out.println(different);
		
		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;
		long daysInMilli = hoursInMilli * 24;
		
		long elapsedDays = different / daysInMilli;
		different = different % daysInMilli;
		
		long elapsedHours = different / hoursInMilli;
		different = different % hoursInMilli;
		
		long elapsedMinutes = different / minutesInMilli;
		different = different % minutesInMilli;
		
		long elapsedSeconds = different / secondsInMilli;
		
		System.out.printf(
			    "%d days, %d hours, %d minutes, %d seconds%n", 
			    elapsedDays,
			    elapsedHours, elapsedMinutes, elapsedSeconds);
		
		String remain = elapsedDays+" day(s) "+elapsedHours+" hour(s)\n"+elapsedMinutes+" minute(s) "+elapsedSeconds+" second(s) ";
		
		String dateTime = future.toString();
		if(elapsedSeconds < 0) {
			lblValidUpto.setText("Link Expired");
			lblExpiredon.setText("Expired On: "+ dateTime.substring(0, 19));
		} else {
			lblValidUpto.setText(remain);
		}
		
	}
	
	DbConnect DBobj = new DbConnect();
	Model convertUrlObj = new Model();
	
	public void showShortenURL(ActionEvent event) {
		//lblurl.setText(txturl.getText());
		
		String longURL = txturl.getText();
		try {
			new URL(longURL).openStream().close();
			lblError.setText("");
			int lastId = DBobj.addUrl(longURL);
			String shortURL = convertUrlObj.shortenURL(lastId);
			shortURL = "http://localhost:8080/UrlShortener/"+shortURL;
			DBobj.addRecord(lastId,longURL,shortURL);
			
			txtShortURL.setText(shortURL);
			
		} catch (MalformedURLException ex) {
			lblError.setText("URL is Invalid. Please enter a valid URL");
			txtShortURL.setText("");
		} catch (IOException ex) {
			lblError.setText("URL is Unavailable. Please enter a valid URL");
			txtShortURL.setText("");
		}
		
		
	}
	
	public void copyToClipboard() {
	    String ctc = txtShortURL.getText().toString();
	    StringSelection stringSelection = new StringSelection(ctc);
	    Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
	    clpbrd.setContents(stringSelection, null);
	       
	}
	
	public void deleteAllRecords() {
		DBobj.deleteAllData();
		tableURL.getItems().clear();
	}

}