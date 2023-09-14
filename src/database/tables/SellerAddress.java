package database.tables;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SellerAddress implements Serializable{
	private String email;
	private String houseNumber;
	private String areaStreet;
	private String landmark;
	private String pincode;
	private String city;
	private String State;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHouseNumber() {
		return houseNumber;
	}
	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}
	public String getAreaStreet() {
		return areaStreet;
	}
	public void setAreaStreet(String areaStreet) {
		this.areaStreet = areaStreet;
	}
	public String getLandmark() {
		return landmark;
	}
	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
}
