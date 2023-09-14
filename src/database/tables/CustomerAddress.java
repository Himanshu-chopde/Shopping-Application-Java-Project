package database.tables;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CustomerAddress implements Serializable{
	private String email;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String houseNumber;
	private String areaStreet;
	private String landmark;
	private String pincode;
	private String city;
	private String state;
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
}
