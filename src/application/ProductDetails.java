package application;

public class ProductDetails {
	private String id;
	private String productHeading;
	private String imageSrc;
	private String productBrand;
	private String description;
	private String category;
	private double newPrice;
	private double oldPrice;
	private double rating;
	private double deliveryCharges;
	private String quantity;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public double getDeliveryCharges() {
		return deliveryCharges;
	}

	public void setDeliveryCharges(double deliveryCharges) {
		this.deliveryCharges = deliveryCharges;
	}

	public String getProductHeading() {
		return productHeading;
	}
	
	public String getProductBrand() {
		return productBrand;
	}

	public void setProductBrand(String productBrand) {
		this.productBrand = productBrand;
	}

	public void setProductHeading(String productHeading) {
		this.productHeading = productHeading;
	}
	
	public double getNewPrice() {
		return newPrice;
	}
	
	public void setNewPrice(double newPrice) {
		this.newPrice = newPrice;
	}
	
	public double getOldPrice() {
		return oldPrice;
	}
	
	public void setOldPrice(double oldPrice) {
		this.oldPrice = oldPrice;
	}
	
	public String getImageSrc() {
		return imageSrc;
	}
	
	public void setImageSrc(String imageSrc) {
		this.imageSrc = imageSrc;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

}
