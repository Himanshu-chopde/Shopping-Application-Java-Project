package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class ShowOrderProductController implements Initializable{
	
	public String productId;

    @FXML
    protected ImageView imageView;

    @FXML
    protected Label lblDeliveryCharges, lblHeading, lblNewPrice, lblBrand, lblQuantity;

    @FXML
    protected Text lblOldPrice;

    public void setProductData(ProductDetails product) {
    	Image image = new Image(getClass().getResourceAsStream(product.getImageSrc()));
		String deliveryCharges;
		
		if(product.getDeliveryCharges() == 0.0) {
			deliveryCharges = "Free Delivery";
		}
		else {
			deliveryCharges = "+₹"+product.getDeliveryCharges();
		}
		
		imageView.setImage(image);
    	
    	productId = product.getId();
    	lblHeading.setText(product.getProductHeading());
    	lblNewPrice.setText(((Double)product.getNewPrice()).toString());
    	lblOldPrice.setText(((Double)product.getOldPrice()).toString());
    	lblDeliveryCharges.setText(deliveryCharges);
    	lblBrand.setText(product.getProductBrand());
    	lblQuantity.setText(product.getQuantity());
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		lblOldPrice.strikethroughProperty().set(true);
	}
}
