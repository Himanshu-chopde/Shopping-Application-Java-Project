package database.tables;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CustomerWishlist implements Serializable{
	private String productId;
	private String customerEmail;
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getCustomerEmail() {
		return customerEmail;
	}
	
	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
	
}
