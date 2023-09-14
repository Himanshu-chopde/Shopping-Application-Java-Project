package seller;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import application.ProductDetails;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProductCardController extends application.ProductCardController {
	@FXML
	private Label productId, productQuantity;

	@FXML
	protected void imageReleased(MouseEvent event) {


		try {
			setDetailsInShowProduct();
			Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Parent root = FXMLLoader.load(Main.class.getResource("@../../../seller/ShowUpdateProduct.fxml"));
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/ButtonStyle.css").toExternalForm());
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(primaryStage);
			stage.setResizable(false);
			stage.setTitle(this.lblProductHeading.getText());
			stage.getIcons().add(this.productImage.getImage());
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		super.initialize(arg0, arg1);
		productId.setText(productID);
		productQuantity.setText(quantity);
	}
	
	@Override
	public void setProductData(ProductDetails product) {
		super.setProductData(product);
		productQuantity.setText(quantity);
		productId.setText(productID);
	}

	@Override
	public void setDetailsInShowProduct() {
		ShowUpdateProductController.id = this.productID;
		ShowUpdateProductController.heading = this.lblProductHeading.getText();
		ShowUpdateProductController.Brand = this.lblBrand.getText();
		ShowUpdateProductController.newPrice = this.lblNewPrice.getText();
		ShowUpdateProductController.oldPrice = this.lblOldPrice.getText();
		ShowUpdateProductController.deliveryCharges = this.lblDeliveryCharges.getText();
		ShowUpdateProductController.rating = this.lblRating.getText();
		ShowUpdateProductController.description = this.description;
		ShowUpdateProductController.image = this.productImage.getImage();
		ShowUpdateProductController.category = this.category;
		ShowUpdateProductController.quantity = this.quantity;
	}

}
