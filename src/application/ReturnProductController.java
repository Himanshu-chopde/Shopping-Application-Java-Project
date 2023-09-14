package application;

import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.Transaction;

import database.configuration.ConnectDatabase;
import database.tables.CustomerOrders;
import database.tables.ReturnedProducts;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ReturnProductController implements Initializable{
	public static String PRODUCTID, ORDERID, heading, brand, newPrice, oldPrice, deliveryCharges, rating, orderDate, quantity, email;
	public static int ISDELIVERED;
	public static Image image;
	public static double numRating;
	
	public String productId, orderId;
	public int isDelivered;
	public int rate;
	
	public boolean refreshHomePage = false, flagPane = true;

	@FXML
	private Button btnBack, btnReturnProduct, btnSubmit;
	
	@FXML
	private TextArea textAreaReason;

	@FXML
	private Label lblBrand, lblDeliveryCharges, lblNewPrice, lblOrderDate, lblProductHeading, lblProductQuantity,
			lblRating;

	@FXML
	private Text lblOldPrice;

	@FXML
	private ImageView productImage;
	
	@FXML
	private VBox vbox;
	
	@FXML
    private Pane pane;
	
	@FXML
	private Label lbl1Star, lbl2Star, lbl3Star, lbl4Star, lbl5Star, lblFeedback;
	
	@FXML
	private Pane paneStar;

	@FXML
	void backAction(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

	@FXML
	void returnProductAction(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.setHeight(590);
		
		vbox.setPrefHeight(550);
		if(flagPane) {
			vbox.getChildren().add(pane);
			flagPane = false;
		}
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Return Item");
		alert.setHeaderText("Please Give The Reason To Return This Item.");
		alert.setContentText("Reason Should Be Valid.");
		alert.show();
		
		
	}
	
	@FXML
	void submitAction(ActionEvent event) {
		if(reasonValidation()) {
			insertInReturnedProductsTable();
			updateCustomerOrdersTable();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Returned Item");
			alert.setHeaderText("Your Product Is Returned.");
			alert.setContentText("Reason Should Be Valid.");
			alert.show();
			
			OrdersPageController.ordersPageController.getDetailsFromDatabase();
			OrdersPageController.ordersPageController.displayProducts(OrdersPageController.customerOrders);
			
			Node source = (Node) event.getSource();
			Stage stage = (Stage) source.getScene().getWindow();
			stage.close();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		productId = PRODUCTID;
		orderId = ORDERID;
		lblBrand.setText(brand);
		lblDeliveryCharges.setText(deliveryCharges);
		lblNewPrice.setText(newPrice);
		lblOldPrice.setText(oldPrice);
		lblOrderDate.setText(orderDate);
		lblProductHeading.setText(heading);
		lblProductQuantity.setText(quantity);
		lblRating.setText(rating);
		productImage.setImage(image);
		isDelivered = ISDELIVERED;
		rate = (int) numRating;
		setRatingStarColor(rate);
		
		if(isDelivered != 1) {
			btnReturnProduct.setVisible(false);
		}
		if(!compareDate()) {
			btnReturnProduct.setVisible(false);
		}
		
		vbox.getChildren().remove(pane);
		vbox.setPrefHeight(433);
	}
	
	public void insertInReturnedProductsTable() {
		Session session = ConnectDatabase.getSession();
		ReturnedProducts returnedProducts = new ReturnedProducts();
		returnedProducts.setOrderId(Integer.parseInt(orderId));
		returnedProducts.setReason(textAreaReason.getText());
		Transaction transaction = ConnectDatabase.getTransaction(session);
		session.save(returnedProducts);
		transaction.commit();
	}
	
	public void updateCustomerOrdersTable() {
		Session session = ConnectDatabase.getSession();
		CustomerOrders customerOrders = session.get(CustomerOrders.class, Integer.parseInt(orderId));
		customerOrders.setIsDelivered(2);
		Transaction transaction = ConnectDatabase.getTransaction(session);
		session.update(customerOrders);
		transaction.commit();
	}
	
	public boolean reasonValidation() {
		if(textAreaReason.getText().isBlank()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Blank field");
			alert.setHeaderText("Please Give The Reason To Return This Item.");
			alert.setContentText("Reason Should Be Valid.");
			alert.show();
			return false;
		}
		if(textAreaReason.getText().length() < 50) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Reason");
			alert.setHeaderText("The Reason Should Be Of Minimum 50 Characters.");
			alert.setContentText("Reason Should Be Valid.");
			alert.show();
			return false;
		}
		if(textAreaReason.getText().length() > 250) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Reason");
			alert.setHeaderText("The Reason Should Be Of Maximum 250 Characters.");
			alert.setContentText("Reason Should Be Valid.");
			alert.show();
			return false;
		}
		return true;
	}
	
	public boolean compareDate() {
		long millis = System.currentTimeMillis();
		Date curr = new Date(millis);
		String s = lblOrderDate.getText();
		Date ds = null;
		try {
			java.util.Date d = new SimpleDateFormat("yyyy-MM-dd").parse(s);
			ds = new Date(d.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(curr.getTime() - ds.getTime() <= 864000000)
			return true;
		return false;
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
