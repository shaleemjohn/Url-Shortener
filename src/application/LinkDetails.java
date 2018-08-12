package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LinkDetails {
	private final StringProperty ID;
	private final StringProperty LongURL;
	private final StringProperty ShortURL;
	private final StringProperty Elapsed;
	
	public LinkDetails(String ID, String LongURL, String ShortURL, String Elapsed) {
		this.ID = new SimpleStringProperty(ID);
		this.LongURL = new SimpleStringProperty(LongURL);
		this.ShortURL = new SimpleStringProperty(ShortURL);
		this.Elapsed = new SimpleStringProperty(Elapsed);
		
	}

	// Getters
	public String getID() {
		return ID.get();
	}
	
	
	public String getLongURL() {
		return LongURL.get();
	}

	public String getShortURL() {
		return ShortURL.get();
	}

	
	public String getElapsed() {
		return Elapsed.get();
	}
	
	
	// Setters
	public void setID(String value) {
		ID.set(value);
	}
	
	
	public void setLongURL(String value) {
		LongURL.set(value);
	}
	
	public void setShortURL(String value) {
		ShortURL.set(value);
	}
	
	
	public void setElapsed(String value) {
		Elapsed.set(value);
	}
	
	
	// Properties
	
	public StringProperty IDProperty() {
		return ID;
	}
	
	
	public StringProperty LongURLProperty() {
		return LongURL;
	}
	
	public StringProperty ShortURLProperty() {
		return ShortURL;
	}
	
	
	public StringProperty ElapsedProperty() {
		return Elapsed;
	}
	
	
}
