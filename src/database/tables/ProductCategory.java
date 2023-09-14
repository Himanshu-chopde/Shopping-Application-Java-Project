package database.tables;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ProductCategory implements Serializable {
	private String categoryName;
	
	public String getCategoryName() {
		return categoryName;
	}
	
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
}
