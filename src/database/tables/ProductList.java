package database.tables;

public class ProductList {
	private String productId;
	private String title;
	private String newPrice;
	private String oldPrice;
	private String deliveryCharge;
	private String brand;
	private String category;
	private String description;
	private String imageExtension;
	private int quantity;
	private double rating = 0;
	
	public String getProductId() {
		return productId;
	}
	
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getNewPrice() {
		return newPrice;
	}
	
	public void setNewPrice(String newPrice) {
		this.newPrice = newPrice;
	}
	
	public String getOldPrice() {
		return oldPrice;
	}
	
	public void setOldPrice(String oldPrice) {
		this.oldPrice = oldPrice;
	}
	
	public String getDeliveryCharge() {
		return deliveryCharge;
	}
	
	public void setDeliveryCharge(String deliveryCharge) {
		this.deliveryCharge = deliveryCharge;
	}
	
	public String getBrand() {
		return brand;
	}
	
	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getImageExtension() {
		return imageExtension;
	}
	
	public void setImageExtension(String imageExtension) {
		this.imageExtension = imageExtension;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}
	
}
