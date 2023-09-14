package seller;

import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import application.ShowOrderProductController;
import database.configuration.ConnectDatabase;
import database.tables.CustomerAddress;
import database.tables.CustomerOrders;
import database.tables.ProductList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class ShowOrdersCardController extends ShowOrderProductController {
	@FXML
	protected Label lblDeliveredCanceled, lblName, lblPhoneNumber, lblHouseNumber, lblAreaStreet, lblLandmark, lblCity, lblPincode, lblState, lblOrderDate, lblProductId, lblOrderId;

	@FXML
	protected Pane paneDeliveredCanceled;
	
	@FXML
	void btnDeliver(ActionEvent event) {
		CustomerOrders customerOrders = null;
		Session session = ConnectDatabase.getSession();
		customerOrders = session.get(CustomerOrders.class, Integer.parseInt(lblOrderId.getText()));
		customerOrders.setIsDelivered(1);
		Transaction transaction = ConnectDatabase.getTransaction(session);
		session.update(customerOrders);
		transaction.commit();
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Delivered");
		alert.setHeaderText("Marked As Delivered.");
		alert.show();
		
		lblDeliveredCanceled.setText("Marked As Delivered");
		paneDeliveredCanceled.setVisible(true);
	}
	
	@FXML
	void btnCancel(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Cancel");
		alert.setHeaderText("  Are you sure, you want to cancel this order?");
		ButtonType btnYes = new ButtonType("Yes");
		ButtonType btnNo = new ButtonType("No");
		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(btnYes, btnNo);
		Optional<ButtonType> option = alert.showAndWait();
		if (option.get() == btnNo) {
			return;
		}
		
		CustomerOrders customerOrders = null;
		Session session = ConnectDatabase.getSession();
		customerOrders = session.get(CustomerOrders.class, Integer.parseInt(lblOrderId.getText()));
		customerOrders.setIsDelivered(-1);
		Transaction transaction = ConnectDatabase.getTransaction(session);
		session.update(customerOrders);
		transaction.commit();
		
		Session session2 = ConnectDatabase.getSession();
		ProductList productList = session2.get(ProductList.class, lblProductId.getText());
		productList.setQuantity(productList.getQuantity() + Integer.parseInt(lblQuantity.getText()));
		session2.update(productList);
		Transaction transaction2 = ConnectDatabase.getTransaction(session2);
		transaction2.commit();
		
		alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Canceled");
		alert.setHeaderText("Marked As Canceled.");
		alert.show();
		
		lblDeliveredCanceled.setText("Marked As Canceled");
		paneDeliveredCanceled.setVisible(true);
	}
	
	public void setProductData(CustomerOrders customerOrders) {
		lblProductId.setText(customerOrders.getProductId());
		lblOrderId.setText(((Integer)customerOrders.getOrderId()).toString());
		lblQuantity.setText(((Integer)customerOrders.getProductQuantity()).toString());
		lblOrderDate.setText(customerOrders.getOrderDate().toString());
		
		Session session = ConnectDatabase.getSession();
		ProductList product = session.get(ProductList.class, customerOrders.getProductId());
		CustomerAddress address = session.get(CustomerAddress.class, customerOrders.getEmail());
		
		Image image = new Image(getClass().getResourceAsStream("/ProductImages/img" + product.getProductId() + product.getImageExtension()));
		String deliveryCharges;
		
		if(Double.parseDouble(product.getDeliveryCharge()) == 0.0) {
			deliveryCharges = "Free Delivery";
		}
		else {
			deliveryCharges = "+₹"+product.getDeliveryCharge();
		}
		
		imageView.setImage(image);
    	
    	productId = product.getProductId();
    	lblHeading.setText(product.getTitle());
    	lblNewPrice.setText(product.getNewPrice());
    	lblOldPrice.setText(product.getOldPrice());
    	lblDeliveryCharges.setText(deliveryCharges);
    	lblBrand.setText(product.getBrand());
    	
    	lblName.setText(address.getFirstName() + " " + address.getLastName());
    	lblPhoneNumber.setText(address.getPhoneNumber());
    	lblHouseNumber.setText(address.getHouseNumber());
    	lblAreaStreet.setText(address.getAreaStreet());
    	lblLandmark.setText(address.getLandmark());
    	lblCity.setText(address.getCity());
		lblPincode.setText(address.getPincode());
		lblState.setText(address.getState());
	}
}
