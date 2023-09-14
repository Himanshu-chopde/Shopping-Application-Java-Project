package seller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.query.Query;

import application.Main;
import database.configuration.ConnectDatabase;
import database.tables.CustomerOrders;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class ShowReturnedProductsController implements Initializable{
	List<CustomerOrders> customerOrders = null;
    @FXML
    private Pane paneNoOrders;

    @FXML
    private TilePane tilePane;

    @FXML
    void back(MouseEvent event) {
    	try {
			Parent root = FXMLLoader.load(Main.class.getResource("@../../../seller/SellerPage.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/ButtonStyle.css").toExternalForm());
			stage.setScene(scene);
			stage.setMaximized(true);
			stage.setResizable(false);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		getDetailsFromDatabase();
		displayProducts(customerOrders);
		if(customerOrders.isEmpty()) {
			tilePane.getChildren().add(paneNoOrders);
		}
	}

	void getDetailsFromDatabase() {
		customerOrders = new ArrayList<CustomerOrders>();
		Session session = ConnectDatabase.getSession();
		@SuppressWarnings("unchecked")
		Query<CustomerOrders> query = (Query<CustomerOrders>) session.createQuery("from CustomerOrders where isDelivered = 2");
		customerOrders.addAll(query.list());
	}
	
	void displayProducts(List<CustomerOrders> orders) {
		if(customerOrders == null) {
			getDetailsFromDatabase();
		}
		
		try {
			tilePane.getChildren().clear();
			for(CustomerOrders custOrders : orders) {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("ReturnedProductsCard.fxml"));
				HBox hbox = loader.load();
				
				ReturnedProductsCardController controller = loader.getController();
				
				controller.setProductData(custOrders);
				
				tilePane.getChildren().add(hbox);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
