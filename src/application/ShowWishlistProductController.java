package application;

import org.hibernate.Session;
import org.hibernate.Transaction;

import database.configuration.ConnectDatabase;
import database.tables.CustomerCart;
import database.tables.CustomerWishlist;
import database.tables.ProductList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class ShowWishlistProductController {
	
	public String productId;
	private int availableQuantity;
	boolean refreshHomePage = false;

    @FXML
    private ImageView image;

    @FXML
    private Label lblBrand, lblCategory, lblDeliveryCharges, lblHeading, lblNewPrice, lblOutOfStock, lblRating;

    @FXML
    private Text lblOldPrice;
    
    @FXML
	private Label lbl1Star, lbl2Star, lbl3Star, lbl4Star, lbl5Star, lblFeedback;
	
	@FXML
	private Pane paneStar;

    @FXML
    void addToCart(ActionEvent event) {
    	Session session = ConnectDatabase.getSession();
    	
    	CustomerCart cart = new CustomerCart();
		cart.setProductId(productId);
		cart.setCustomerEmail(HomePageController.email);
		cart.setProductQuantity(1);

		CustomerCart available = session.get(CustomerCart.class, cart);
		if (available == null) {
			Transaction transaction = ConnectDatabase.getTransaction(session);
			session.save(cart);
			transaction.commit();
		}
		else {
			available.setProductQuantity(available.getProductQuantity() + 1);
			Transaction transaction = ConnectDatabase.getTransaction(session);
			session.update(available);
			transaction.commit();
		}
		
		updateQuantityInProductList();
		
		refreshHomePage = true;
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Add To Cart");
		alert.setHeaderText("Product is successfully added to your cart");
		alert.show();
    }
    
    private void updateQuantityInProductList() {
		Session session = ConnectDatabase.getSession();
		ProductList product = session.get(ProductList.class, productId);
		Transaction transaction = ConnectDatabase.getTransaction(session);
		product.setQuantity(product.getQuantity()-1);
		session.update(product);
		transaction.commit();
	}

    @FXML
    void removeProduct(ActionEvent event) {
    	CustomerWishlist customerWishlist = new CustomerWishlist();
    	customerWishlist.setCustomerEmail(HomePageController.email);
    	customerWishlist.setProductId(productId);
    	
    	Session session = ConnectDatabase.getSession();
    	Transaction transaction = ConnectDatabase.getTransaction(session);
    	session.delete(customerWishlist);
    	transaction.commit();
    	
    	WishlistController.wishlistController.productDetailsList = null;
    	WishlistController.wishlistController.tilePane.getChildren().clear();
    	WishlistController.wishlistController.displayProducts(WishlistController.wishlistController.getWishlistProductFromDatabase());
    	
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Removed From Wishlist");
    	alert.setHeaderText("Product is successfully removed from your wishlist.");
    	alert.show();
    }
    
    public void setProductData(ProductDetails productDetails) {
		Image productImage = new Image(getClass().getResourceAsStream(productDetails.getImageSrc()));
		String deliveryCharges;
		String rating;
		
		if(productDetails.getDeliveryCharges() == 0.0) {
			deliveryCharges = "Free Delivery";
		}
		else {
			deliveryCharges = "+₹"+productDetails.getDeliveryCharges();
		}
		
		if(productDetails.getRating() == 0.0) {
			rating = "No Rating";
			paneStar.setVisible(false);
		}
		else {
			rating = "Rating: "+productDetails.getRating();
		}
		
		image.setImage(productImage);

		productId = productDetails.getId();
		lblHeading.setText(productDetails.getProductHeading());
		lblNewPrice.setText(((Double) productDetails.getNewPrice()).toString());
		lblOldPrice.setText(((Double) productDetails.getOldPrice()).toString());
		lblBrand.setText(productDetails.getProductBrand());
		lblDeliveryCharges.setText(deliveryCharges);
		lblRating.setText(rating);
//		description = productDetails.getDescription();
		lblCategory.setText(productDetails.getCategory());
		
		Session session = ConnectDatabase.getSession();
		ProductList productList = session.get(ProductList.class, productId);
		availableQuantity = productList.getQuantity();
		
		setRatingStarColor(productDetails.getRating());
		
		if(availableQuantity <= 0) {
			lblOutOfStock.setVisible(true);
		}
	}
    
    private void setRatingStarColor(double rating) {
    	int rate = (int) rating;
		switch(rate) {
		case 1:
			lbl1Star.setText("★");
			break;
		case 2:
			lbl1Star.setText("★");
			lbl2Star.setText("★");
			break;
		case 3:
			lbl1Star.setText("★");
			lbl2Star.setText("★");
			lbl3Star.setText("★");
			break;
		case 4:
			lbl1Star.setText("★");
			lbl2Star.setText("★");
			lbl3Star.setText("★");
			lbl4Star.setText("★");
			break;
		case 5:
			lbl1Star.setText("★");
			lbl2Star.setText("★");
			lbl3Star.setText("★");
			lbl4Star.setText("★");
			lbl5Star.setText("★");
			break;
		}
	}

}
