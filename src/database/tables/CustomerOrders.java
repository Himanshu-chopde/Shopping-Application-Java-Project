package database.tables;

import java.io.Serializable;
import java.sql.Date;

public class CustomerOrders implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int orderId;
	private String email;
	private String productId;
	private int productQuantity;
	private Date orderDate;
	private int isDelivered;
	private int rating;
	
	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public int getOrderId() {
		return orderId;
	}
	
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getProductId() {
		return productId;
	}
	
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public int getProductQuantity() {
		return productQuantity;
	}
	
	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}
	
	public int getIsDelivered() {
		return isDelivered;
	}
	
	public void setIsDelivered(int isDelivered) {
		this.isDelivered = isDelivered;
	}
	
}
