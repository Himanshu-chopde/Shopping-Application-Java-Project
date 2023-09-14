package seller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;

import database.configuration.ConnectDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.input.KeyEvent;

public class AddProductController extends ShowUpdateProductController {
//	String heading, newPrice, oldPrice, deliveryCharges, brand, productId, category, description, imagePath;
//	int quantity;
//	Image image;
	
	@FXML
	ComboBox<String> comboBoxCategory;
	
	private int i = 1;
	
	@FXML
    void checkSpinnerValue(KeyEvent event) {
		try {
			i = Integer.parseInt(spinnerQuantity.getEditor().textProperty().get());
		} catch (Exception e) {
			spinnerQuantity.getEditor().textProperty().set(((Integer)i).toString());
		}
    }
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		IntegerSpinnerValueFactory spinnerValue = new IntegerSpinnerValueFactory(1, 5000);
		spinnerQuantity.setValueFactory(spinnerValue);
		
		productImage.setImage(null);
		lblProductId.setText("");
		lblProductHeading.setText("");
		lblProductHeading.setPromptText("Enter title of product");
		lblBrand.setText("");
		lblNewPrice.setText("");
		lblOldPrice.setText("");
		lblDeliveryCharges.setText("");
		lblRating.setText("");
		textDescription.setText("");
		textDescription.setPromptText("Enter description of the product");
		
		addCategoryCombobox();
		comboBoxCategory.getItems().clear();
		comboBoxCategory.getItems().addAll(categories);
	}
	
	protected void addCategoryCombobox() {
		if (categories == null) {
			Session session = ConnectDatabase.getSession();
			@SuppressWarnings("unchecked")
			List<String> categoryList = session.createSQLQuery("select * from productcategory").list();
			categories = categoryList;
			ConnectDatabase.closeSession();
			comboBoxCategory.getItems().addAll(categories);
		}
	}

	@FXML
	void clearDetailsAction(ActionEvent event) {
		productImage.setImage(null);
		lblProductId.setText("");
		lblProductHeading.setText("");
		lblBrand.setText("");
		lblNewPrice.setText("");
		lblOldPrice.setText("");
		lblDeliveryCharges.setText("");
		lblRating.setText("");
		textDescription.setText("");
	}

	@FXML
	void saveProductAction(ActionEvent event) {
		addProductIdflag = true;
		if(validation()) {
			saveImageInFolder();
			insertProductInDatabase();
		}
	}
	
}
