package application;

import org.hibernate.Session;
import org.hibernate.Transaction;

import database.configuration.ConnectDatabase;
import database.tables.CustomerCart;
import database.tables.CustomerOrders;
import database.tables.CustomerWishlist;
import database.tables.ProductList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ShowOrdersPageController {

	private String productId, orderId, email;
	private int isDelivered;
	private Image image;
	private boolean flagRating = true;
	private double rating;

	@FXML
	private Button btnAddToCart, btnWishlist;

	@FXML
	private Label lblBrand, lblCategory, lblDeliveryCharges, lblNewPrice, lblOrderDate, lblOrderStatus, lblProductTitle,
			lblRating, lblQuantity;

	@FXML
	private Label lbl1Star, lbl2Star, lbl3Star, lbl4Star, lbl5Star, lblFeedback;
	
	@FXML
	private Pane paneStar;

	@FXML
	private Text lblOldPrice;

	@FXML
	private ImageView productImage;

	@FXML
	void AddToCartAction(ActionEvent event) {
		Session session = ConnectDatabase.getSession();
		ProductList productList = session.get(ProductList.class, productId);
		if (productList.getQuantity() < 1) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Add To Cart");
			alert.setHeaderText("Product is not available in stock");
			alert.setContentText("you can add this product to your wishlist");
			alert.show();
			return;
		}

		CustomerCart cart = new CustomerCart();
		cart.setProductId(productId);
		cart.setCustomerEmail(email);
		cart.setProductQuantity(1);

		CustomerCart available = session.get(CustomerCart.class, cart);
		if (available == null) {
			Transaction transaction = ConnectDatabase.getTransaction(session);
			session.save(cart);
			transaction.commit();
		} else {
			available.setProductQuantity(available.getProductQuantity() + 1);
			Transaction transaction = ConnectDatabase.getTransaction(session);
			session.update(available);
			transaction.commit();
		}

		updateQuantityInProductList();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Add To Cart");
		alert.setHeaderText("Product is successfully added to your cart");
		alert.show();
	}

	@FXML
	void AddToWishlistAction(ActionEvent event) {
		if (HomePageController.email.equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Add To Wishlist");
			alert.setHeaderText("Please login to add this product in wishlist.");
			alert.show();
			return;
		}

		Session session = ConnectDatabase.getSession();
		CustomerWishlist wishlist = new CustomerWishlist();
		wishlist.setProductId(productId);
		wishlist.setCustomerEmail(HomePageController.email);
		CustomerWishlist getWishlist = session.get(CustomerWishlist.class, wishlist);

		if (getWishlist != null) {
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
	void openReturnPageAction(MouseEvent event) {
		try {
			ReturnProductController.PRODUCTID = productId;
			ReturnProductController.ORDERID = orderId;
			ReturnProductController.brand = lblBrand.getText();
			ReturnProductController.deliveryCharges = lblDeliveryCharges.getText();
			ReturnProductController.newPrice = lblNewPrice.getText();
			ReturnProductController.oldPrice = lblOldPrice.getText();
			ReturnProductController.orderDate = lblOrderDate.getText();
			ReturnProductController.quantity = lblQuantity.getText();
			ReturnProductController.rating = lblRating.getText();
			ReturnProductController.email = email;
			ReturnProductController.ISDELIVERED = isDelivered;
			ReturnProductController.image = image;
			ReturnProductController.heading = lblProductTitle.getText();
			ReturnProductController.numRating = rating;

			Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Parent root = FXMLLoader.load(Main.class.getResource("ReturnProduct.fxml"));
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/ButtonStyle.css").toExternalForm());
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(primaryStage);
			stage.setResizable(false);
			stage.setTitle(this.lblBrand.getText());
			stage.getIcons().add(this.productImage.getImage());
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setProductData(CustomerOrders customerOrders) {
		orderId = ((Integer) customerOrders.getOrderId()).toString();
		productId = customerOrders.getProductId();
		email = customerOrders.getEmail();
		lblQuantity.setText(((Integer) customerOrders.getProductQuantity()).toString());
		lblOrderDate.setText(customerOrders.getOrderDate().toString());
		isDelivered = customerOrders.getIsDelivered();

		if (customerOrders.getIsDelivered() == 0) {
			lblFeedback.setVisible(false);
			paneStar.setVisible(false);
			lblOrderStatus.setText("Your Order Will Get delivered Soon");
		} else if (customerOrders.getIsDelivered() == 1) {
			if(customerOrders.getRating() != 0.0) {
				setRatingStarColor(customerOrders.getRating());
				flagRating = false;
				changeStarMouseHover();
			}
			else {
				lblFeedback.setText("Please Give Feedback Rating");
			}
			lblOrderStatus.setText("Order Is Delivered Successfully");
		} else if (customerOrders.getIsDelivered() == -1) {
			lblFeedback.setVisible(false);
			paneStar.setVisible(false);
			lblOrderStatus.setText("This Order Is Canceled");
		} else if (customerOrders.getIsDelivered() == 2) {
			lblFeedback.setVisible(false);
			paneStar.setVisible(false);
			lblOrderStatus.setText("You returned This Order");
		}

		Session session = ConnectDatabase.getSession();
		ProductList productList = session.get(ProductList.class, customerOrders.getProductId());

		image = new Image(getClass().getResourceAsStream(
				"/ProductImages/img" + productList.getProductId() + productList.getImageExtension()));
		String deliveryCharges;

		if (Double.parseDouble(productList.getDeliveryCharge()) == 0.0) {
			deliveryCharges = "Free Delivery";
		} else {
			deliveryCharges = "+₹" + productList.getDeliveryCharge();
		}

		productImage.setImage(image);

		lblBrand.setText(productList.getBrand());
		lblCategory.setText(productList.getCategory());
		lblDeliveryCharges.setText(deliveryCharges);
		lblNewPrice.setText(productList.getNewPrice());
		lblOldPrice.setText(productList.getOldPrice());
		lblProductTitle.setText(productList.getTitle());
		lblRating.setText(((Double) productList.getRating()).toString());
		rating = productList.getRating();
	}

	private void updateQuantityInProductList() {
		Session session = ConnectDatabase.getSession();
		ProductList product = session.get(ProductList.class, productId);
		Transaction transaction = ConnectDatabase.getTransaction(session);
		product.setQuantity(product.getQuantity() - 1);
		session.update(product);
		transaction.commit();
	}

	@FXML
	private void click1Star(MouseEvent event) {
		if (flagRating) {
			lbl1Star.setText("★");
			changeStarMouseHover();
			rateProduct(1);
			flagRating = false;
		}
	}

	@FXML
	private void click2Star(MouseEvent event) {
		if (flagRating) {
			lbl1Star.setText("★");
			lbl2Star.setText("★");
			changeStarMouseHover();
			rateProduct(2);
			flagRating = false;
		}
	}

	@FXML
	private void click3Star(MouseEvent event) {
		if (flagRating) {
			lbl1Star.setText("★");
			lbl2Star.setText("★");
			lbl3Star.setText("★");
			changeStarMouseHover();
			rateProduct(3);
			flagRating = false;
		}
	}

	@FXML
	private void click4Star(MouseEvent event) {
		if (flagRating) {
			lbl1Star.setText("★");
			lbl2Star.setText("★");
			lbl3Star.setText("★");
			lbl4Star.setText("★");
			changeStarMouseHover();
			rateProduct(4);
			flagRating = false;
		}
	}

	@FXML
	private void click5Star(MouseEvent event) {
		if (flagRating) {
			lbl1Star.setText("★");
			lbl2Star.setText("★");
			lbl3Star.setText("★");
			lbl4Star.setText("★");
			lbl5Star.setText("★");
			changeStarMouseHover();
			rateProduct(5);
			flagRating = false;
		}
	}
	
	private void changeStarMouseHover() {
		lbl1Star.setCursor(Cursor.DEFAULT);
		lbl2Star.setCursor(Cursor.DEFAULT);
		lbl3Star.setCursor(Cursor.DEFAULT);
		lbl4Star.setCursor(Cursor.DEFAULT);
		lbl5Star.setCursor(Cursor.DEFAULT);
	}
	
	private void rateProduct(int rating) {
		Session session = ConnectDatabase.getSession();
		ProductList product = session.get(ProductList.class, productId);
		double productRating = product.getRating();
		if(productRating == 0.0) {
			product.setRating(rating);
		}
		else {
			double newRating = Math.round(((productRating + rating) / 2.0) * 10.0) / 10.0;
			product.setRating(newRating);
		}
		Transaction transaction = ConnectDatabase.getTransaction(session);
		session.update(product);
		transaction.commit();
		
		Session session2 = ConnectDatabase.getSession();
		CustomerOrders customerOrders = session.get(CustomerOrders.class, Integer.parseInt(orderId));
		customerOrders.setRating(rating);
		Transaction transaction2 = ConnectDatabase.getTransaction(session2);
		session2.update(customerOrders);
		transaction2.commit();
		
		lblFeedback.setText("Thank you for your feedback.");
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Rating");
		alert.setHeaderText("Feedback Submitted Successfully.");
		alert.show();
		
		session.close();
		session2.close();
	}
	
	private void setRatingStarColor(int rating) {
		switch(rating) {
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
