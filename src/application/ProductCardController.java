package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProductCardController implements Initializable {
	protected boolean flag = false;
	protected static ProductCardController pdc = null;
	public static boolean selectFlag = false;
	protected String productID;
	protected String description;
	protected String category;
	protected String quantity = "1";
	protected double rating;
	public static boolean imageNotFoundFlag = false;
	
	@FXML
	protected Text lblOldPrice;

	@FXML
	protected ImageView productImage;

	@FXML
	protected Label lblNewPrice, lblBrand, lblDeliveryCharges, lblRating;
	
	@FXML
	protected Label lblProductHeading;

	@FXML
	protected HBox hBox;
	
	@FXML
	private Label lbl1Star, lbl2Star, lbl3Star, lbl4Star, lbl5Star, lblFeedback;
	
	@FXML
	private Pane paneStar;
	
	@FXML
	protected void imageReleased(MouseEvent event) {


		try {
			setDetailsInShowProduct();
			Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Parent root = FXMLLoader.load(Main.class.getResource("ShowProduct.fxml"));
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
	
	@FXML
    void imageEntered(MouseEvent event) {
		hBox.setBackground(new Background(new BackgroundFill(Color.valueOf("#AACCFF"), null, null)));
    }

    @FXML
    void imageExited(MouseEvent event) {
    	hBox.setBackground(new Background(new BackgroundFill(Color.valueOf("#00AAFF"), null, null)));
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		lblOldPrice.setStrikethrough(true);
	}

	public void setProductData(ProductDetails product) {
		Image image = null;
		try {
			image = new Image(getClass().getResourceAsStream(product.getImageSrc()));
		}catch(Exception e) {
			if(!imageNotFoundFlag) {
				imageNotFoundFlag = true;
				HomePageController.notExistsImage = product.getImageSrc();
			}
			return;
		}
		
		String deliveryCharges;
		String rating;
		
		if(product.getDeliveryCharges() == 0.0) {
			deliveryCharges = "Free Delivery";
		}
		else {
			deliveryCharges = "+₹"+product.getDeliveryCharges()+" Delivery";
		}
		
		if(product.getRating() == 0.0) {
			rating = "No Rating";
			paneStar.setVisible(false);
		}
		else {
			rating = "Rating: "+product.getRating();
		}
		setRatingStarColor(product.getRating());
		
		productImage.setImage(image);

		productID = product.getId();
		lblProductHeading.setText(product.getProductHeading());
		lblNewPrice.setText(((Double) product.getNewPrice()).toString());
		lblOldPrice.setText(((Double) product.getOldPrice()).toString());
		lblBrand.setText(product.getProductBrand());
		lblDeliveryCharges.setText(deliveryCharges);
		lblRating.setText(rating);
		description = product.getDescription();
		category = product.getCategory();
		quantity = product.getQuantity();
		this.rating = product.getRating();
	}
	
	public void setDetailsInShowProduct() {
		ShowProductController.id = this.productID;
		ShowProductController.heading = this.lblProductHeading.getText();
		ShowProductController.Brand = this.lblBrand.getText();
		ShowProductController.newPrice = this.lblNewPrice.getText();
		ShowProductController.oldPrice = this.lblOldPrice.getText();
		ShowProductController.deliveryCharges = this.lblDeliveryCharges.getText();
		ShowProductController.rating = this.lblRating.getText();
		ShowProductController.description = this.description;
		ShowProductController.image = this.productImage.getImage();
		ShowProductController.quantity = this.quantity;
		ShowProductController.numRating = this.rating;
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
