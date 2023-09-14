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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ShowCartProductController {
	public String productId;
	private int availableQuantity;

	@FXML
	private Button btnMinus, btnPlus, btnRemove, btnAddToWishlist;

	@FXML
	private ImageView image;

	@FXML
	private Label lblBrand, lblDeliveryCharges, lblHeading, lblNewPrice, lblProductCount, lblRating, lblCategory,
			lblOutOfStock;

	@FXML
	private Text lblOldPrice;

	@FXML
	VBox mainVBox;

	@FXML
	private Label lbl1Star, lbl2Star, lbl3Star, lbl4Star, lbl5Star, lblFeedback;
	
	@FXML
	private Pane paneStar;
	
	@FXML
	void decreaseProductCount(ActionEvent event) {
		if (!lblProductCount.getText().equals("1")) {
			lblProductCount.setText(Integer.toString(Integer.parseInt(lblProductCount.getText()) - 1));

			Session session = ConnectDatabase.getSession();
			CustomerCart customeCart = new CustomerCart();
			customeCart.setCustomerEmail(HomePageController.email);
			customeCart.setProductId(productId);
			customeCart.setProductQuantity(Integer.parseInt(lblProductCount.getText()));
			Transaction transaction = ConnectDatabase.getTransaction(session);
			session.update(customeCart);
			transaction.commit();
			
			plusQuantityInProductList();

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Cart");
			alert.setHeaderText("Product Removed Successfully");
			alert.show();
		}
	}

	@FXML
	void increaseProductCount(ActionEvent event) {
		if (!lblProductCount.getText().equals("50")
				&& !(Integer.parseInt(lblProductCount.getText()) == availableQuantity))
			lblProductCount.setText(Integer.toString(Integer.parseInt(lblProductCount.getText()) + 1));

		Session session = ConnectDatabase.getSession();
		CustomerCart customeCart = new CustomerCart();
		customeCart.setCustomerEmail(HomePageController.email);
		customeCart.setProductId(productId);
		customeCart.setProductQuantity(Integer.parseInt(lblProductCount.getText()));
		Transaction transaction = ConnectDatabase.getTransaction(session);
		session.update(customeCart);
		transaction.commit();
		
		minusQuantityInProductList();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Cart");
		alert.setHeaderText("Product Saved Successfully");
		alert.show();
	}

	@FXML
	void addToWishlist(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);

		CustomerWishlist customerWishlist = new CustomerWishlist();
		customerWishlist.setCustomerEmail(HomePageController.email);
		customerWishlist.setProductId(productId);

		Session session = ConnectDatabase.getSession();
		CustomerWishlist availableWishlist = null;
		availableWishlist = session.get(CustomerWishlist.class, customerWishlist);
		if (availableWishlist != null) {
			alert.setTitle("Wishlist");
			alert.setHeaderText("This product is already added to your wishlist.");
			alert.show();
		} else {
			Transaction transaction = ConnectDatabase.getTransaction(session);
			session.save(customerWishlist);
			transaction.commit();

			alert.setTitle("Wishlist");
			alert.setHeaderText("This product is added to your wishlist.");
			alert.show();
		}
	}

	@FXML
	void removeProduct(ActionEvent event) {
		CustomerCart customerCart = new CustomerCart();
		customerCart.setCustomerEmail(HomePageController.email);
		customerCart.setProductId(productId);
		Session session = ConnectDatabase.getSession();
		Transaction transaction = ConnectDatabase.getTransaction(session);
		session.delete(customerCart);
		transaction.commit();

		CartController.cartController.productDetailsList = null;
		CartController.cartController.tilePane.getChildren().clear();
		CartController.cartController.displayProducts(CartController.cartController.getCartProdoductFromDatabase());
		
		int c = Integer.parseInt(lblProductCount.getText());
		for(int i = 0; i < c; i++) {
			plusQuantityInProductList();
		}

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Removed From Cart");
		alert.setHeaderText("Product is successfully removed from your cart.");
		alert.show();
	}

	public void setProductData(ProductDetails productDetails) {
		System.out.println("path = "+productDetails.getImageSrc());
		Image productImage = new Image(getClass().getResourceAsStream(productDetails.getImageSrc()));
		String deliveryCharges;
		String rating;

		if (productDetails.getDeliveryCharges() == 0.0) {
			deliveryCharges = "Free Delivery";
		} else {
			deliveryCharges = "+₹" + productDetails.getDeliveryCharges();
		}

		if (productDetails.getRating() == 0.0) {
			rating = "No Rating";
			paneStar.setVisible(false);
		} else {
			rating = "Rating: " + productDetails.getRating();
			setRatingStarColor(productDetails.getRating());
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
		lblProductCount.setText(productDetails.getQuantity());

		Session session = ConnectDatabase.getSession();
		ProductList productList = session.get(ProductList.class, productId);
		availableQuantity = productList.getQuantity();
		if (Integer.parseInt(lblProductCount.getText()) > availableQuantity) {
			lblProductCount.setText(((Integer) availableQuantity).toString());
			CustomerCart cart = new CustomerCart();
			cart.setCustomerEmail(HomePageController.email);
			cart.setProductId(productId);
			cart = session.get(CustomerCart.class, cart);
			cart.setProductQuantity(availableQuantity);
			Transaction transaction = ConnectDatabase.getTransaction(session);
			session.update(cart);
			transaction.commit();
		}
		if (availableQuantity <= 0) {
			lblOutOfStock.setVisible(true);
		}
	}

	private void minusQuantityInProductList() {
		Session session = ConnectDatabase.getSession();
		ProductList product = session.get(ProductList.class, productId);
		Transaction transaction = ConnectDatabase.getTransaction(session);
		product.setQuantity(product.getQuantity() - 1);
		session.update(product);
		transaction.commit();
	}

	private void plusQuantityInProductList() {
		Session session = ConnectDatabase.getSession();
		ProductList product = session.get(ProductList.class, productId);
		Transaction transaction = ConnectDatabase.getTransaction(session);
		product.setQuantity(product.getQuantity() + 1);
		session.update(product);
		transaction.commit();
	}

	public void setRatingStarColor(double rating) {
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
