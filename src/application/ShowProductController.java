package application;

import java.net.URL;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.Transaction;

import database.configuration.ConnectDatabase;
import database.tables.CustomerCart;
import database.tables.CustomerWishlist;
import database.tables.ProductList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ShowProductController implements Initializable {
	public static String id, heading, Brand, newPrice, oldPrice, deliveryCharges, rating, description, quantity;
	public static Image image;
	public static double numRating;
	public boolean refreshHomePage = false;
	public int rate;
	
	@FXML
	private Button btnAddToCart, btnAddToWishlist, btnCancel;

	@FXML
	private Label lblBrand, lblDeliveryCharges, lblNewPrice, lblProductHeading, lblRating;

	@FXML
	private Text lblOldPrice;

	@FXML
	private ImageView productImage;

	@FXML
	private TextArea textDescription;
	
	@FXML
	private Label lbl1Star, lbl2Star, lbl3Star, lbl4Star, lbl5Star, lblFeedback;
	
	@FXML
	private Pane paneStar;

	@FXML
	void addToCartAction(ActionEvent event) {
		if (HomePageController.email.equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Add To Cart");
			alert.setHeaderText("Please login to add this product in cart.");
			alert.show();
			return;
		}
		
		Session session = ConnectDatabase.getSession();
		ProductList productList = session.get(ProductList.class, id);
		if(productList.getQuantity() < 1) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Add To Cart");
			alert.setHeaderText("Product is not available in stock");
			alert.setContentText("you can add this product to your wishlist");
			alert.show();
			return;
		}
		
		CustomerCart cart = new CustomerCart();
		cart.setProductId(id);
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

	@FXML
	void addToWishlistAction(ActionEvent event) {
		if (HomePageController.email.equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Add To Wishlist");
			alert.setHeaderText("Please login to add this product in wishlist.");
			alert.show();
			return;
		}
		
		Session session = ConnectDatabase.getSession();
		CustomerWishlist wishlist = new CustomerWishlist();
		wishlist.setProductId(id);
		wishlist.setCustomerEmail(HomePageController.email);
		CustomerWishlist getWishlist = session.get(CustomerWishlist.class, wishlist);
		
		if(getWishlist != null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Add To Wishlist");
			alert.setHeaderText("Product is already available in your wishlist");
			alert.show();
			return;
		}
		Transaction transaction = ConnectDatabase.getTransaction(session);
		
		session.save(wishlist);
		transaction.commit();
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Add To Wishlist");
		alert.setHeaderText("Product is successfully added to your wishlist");
		alert.show();
	}

	@FXML
	void cancelAction(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		lblProductHeading.setText(heading);
		lblBrand.setText(Brand);
		lblNewPrice.setText(newPrice);
		lblOldPrice.setText(oldPrice);
		lblDeliveryCharges.setText(deliveryCharges);
		lblRating.setText(rating);
		textDescription.setText(description);
		textDescription.setEditable(false);
		
		productImage.setImage(image);
		
		this.rate = (int) numRating;
		setRatingStarColor(rate);
		
		if(rate == 0) {
			paneStar.setVisible(false);
		}
	}
	
	private void updateQuantityInProductList() {
		Session session = ConnectDatabase.getSession();
		ProductList product = session.get(ProductList.class, id);
		Transaction transaction = ConnectDatabase.getTransaction(session);
		product.setQuantity(product.getQuantity()-1);
		session.update(product);
		transaction.commit();
	}
	
	public void setRatingStarColor(int rate) {
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
