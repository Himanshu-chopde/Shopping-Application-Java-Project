package seller;

import org.hibernate.Session;

import database.configuration.ConnectDatabase;
import database.tables.CustomerOrders;
import database.tables.ReturnedProducts;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ReturnedProductsCardController extends ShowOrdersCardController{
	@FXML
	TextArea textReason;
	
	public void setProductData(CustomerOrders customerOrders) {
		super.setProductData(customerOrders);
		Session session = ConnectDatabase.getSession();
		ReturnedProducts returnedProducts = session.get(ReturnedProducts.class, customerOrders.getOrderId());
		textReason.setText(returnedProducts.getReason());
	}
}
