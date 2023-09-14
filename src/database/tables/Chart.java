package database.tables;

import java.io.Serializable;
import java.sql.Date;


public class Chart implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date date;
	private double total;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	
}
