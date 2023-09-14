package seller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.Transaction;

import database.configuration.ConnectDatabase;
import database.tables.ProductList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ShowUpdateProductController implements Initializable {
	public static String id, heading, Brand, newPrice, oldPrice, deliveryCharges, rating, description, category,
			quantity;
	public static Image image;
	public static List<String> categories = null;

	protected String productId, pHeading, pNewPrice, pOldPrice, pDeliveryCharges, pBrand, pCategory, pDescription,
			imageExtension;
	
	protected boolean isImageChanged = false;
	
	public static boolean addProductIdflag = false;

	protected Image pImage;

	protected int pQuantity;

	@FXML
	protected Button btnAddToCart, btnAddToWishlist, btnCancel;

	@FXML
	protected TextField lblProductId, lblBrand, lblDeliveryCharges, lblNewPrice, lblOldPrice;

	@FXML
	protected Label lblRating;

	@FXML
	protected ImageView productImage;

	@FXML
	protected TextArea textDescription, lblProductHeading;

	@FXML
	ComboBox<String> comboBoxCategory;

	@FXML
	protected Spinner<Integer> spinnerQuantity;

	@FXML
	void chooseImageAction(ActionEvent event) {
		FileChooser choose = new FileChooser();
		choose.setTitle("Select an image");
		choose.setInitialDirectory(new File(System.getProperty("user.home")));
		FileChooser.ExtensionFilter jpgfilter = new ExtensionFilter(".jpg", "*.jpg");
		FileChooser.ExtensionFilter jpegfilter = new ExtensionFilter(".jpeg", "*.jpeg");
		FileChooser.ExtensionFilter pngfilter = new ExtensionFilter(".png", "*.png");
		choose.getExtensionFilters().addAll(jpgfilter, jpegfilter, pngfilter);

		File imgPath = choose.showOpenDialog(null);
		if (imgPath != null) {
			productImage.setImage(new Image(imgPath.toString()));
			isImageChanged = true;
		}
	}

	@FXML
	private void updateProductAction(ActionEvent event) {
		if (validation()) {
			if(isImageChanged) {
				saveImageInFolder();
			}
			updateProductInDatabase();
		}
	}

	@FXML
	protected void cancelAction(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		addProductIdflag = false;
		
		IntegerSpinnerValueFactory spinnerValue = new IntegerSpinnerValueFactory(1, 5000);
		spinnerQuantity.setValueFactory(spinnerValue);
		
		addCategoryCombobox();
		comboBoxCategory.getItems().addAll(categories);

		lblProductId.setText(id);
		lblProductHeading.setText(heading);
		lblBrand.setText(Brand);
		lblNewPrice.setText(newPrice);
		lblOldPrice.setText(oldPrice);

		if (deliveryCharges.equals("Free Delivery")) {
			lblDeliveryCharges.setText("0.0");
		} else {
			deliveryCharges = deliveryCharges.substring(2, deliveryCharges.indexOf(' '));
			lblDeliveryCharges.setText(deliveryCharges);
		}

		lblRating.setText(rating);
		textDescription.setText(description);
		
		comboBoxCategory.getSelectionModel().select(category);
		productImage.setImage(image);
		spinnerQuantity.setValueFactory(new IntegerSpinnerValueFactory(1, 5000, Integer.parseInt(quantity)));

	}

	protected void addCategoryCombobox() {
		if (categories == null) {
			Session session = ConnectDatabase.getSession();
			@SuppressWarnings("unchecked")
			List<String> categoryList = session.createSQLQuery("select * from productcategory").list();
			categories = categoryList;
			ConnectDatabase.closeSession();
		}
	}

	protected boolean validation() {
		Alert alert;

		productId = lblProductId.getText().trim();
		pHeading = lblProductHeading.getText().trim();
		pNewPrice = lblNewPrice.getText().trim();
		pOldPrice = lblOldPrice.getText().trim();
		pDeliveryCharges = lblDeliveryCharges.getText().trim();
		pBrand = lblBrand.getText().trim();
		pCategory = comboBoxCategory.getSelectionModel().getSelectedItem();
		pDescription = textDescription.getText().trim();
		pQuantity = spinnerQuantity.getValue();
		pImage = productImage.getImage();
		if (!productIdValidation(productId)) {
			return false;
		}

		if (!productTitleValidation(pHeading)) {
			return false;
		}
		
		if (!productPriceValidation(pNewPrice)) {
			return false;
		}
		
		if (!productPriceValidation(pOldPrice)) {
			return false;
		}

		if (!productPriceValidation(pDeliveryCharges)) {
			return false;
		}
		
		if (pBrand.equals("")) {
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Empty brand name");
			alert.setHeaderText("Please enter brand name / manufacturer company name.");
			alert.show();
			return false;
		}
		
		if (comboBoxCategory.getSelectionModel().getSelectedItem() == null) {
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Empty product category");
			alert.setHeaderText("Please select the product category.");
			alert.show();
			return false;
		}

		if (!productDescriptionValidation(pDescription)) {
			return false;
		}

		if (pImage == null) {
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("No image selected");
			alert.setHeaderText("Please add an image of product.");
			alert.show();
			return false;
		}
		return true;
	}

	private boolean productIdValidation(String pId) {
		Alert alert;
		if (pId.equals("")) {
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Empty Product ID");
			alert.setHeaderText("Please enter product Id");
			alert.show();
			return false;
		}

		if (!addProductIdflag && checkProductIdInDatabase(pId)) {
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Product ID");
			alert.setHeaderText("Product Id Not Found");
			alert.show();
			return false;
		}
		else if(addProductIdflag && !checkProductIdInDatabase(pId)) {
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Product ID");
			alert.setHeaderText("Product Id already exists.");
			alert.show();
			return false;
		}

		return true;
	}

	private boolean checkProductIdInDatabase(String id) {
		Session session = ConnectDatabase.getSession();
		@SuppressWarnings("unchecked")
		List<String> list = session.createSQLQuery("select productId from ProductList").list();
		for (String pid : list) {
			if (id.equals(pid)) {
				return false;
			}
		}
		return true;
	}

	private boolean productTitleValidation(String title) {
		Alert alert;

		if (title.length() < 50) {
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Too short product title");
			alert.setHeaderText("Product title should be greater than 50 characters.");
			alert.show();
			return false;
		}

		else if (title.length() > 200) {
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Too long product title");
			alert.setHeaderText("Product title should be less than 200 characters.");
			alert.show();
			return false;
		}

		return true;
	}

	private boolean productPriceValidation(String price) {
		Alert alert;
		if (price.equals("")) {
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Amount");
			alert.setHeaderText("Please enter amount.");
			alert.show();
			return false;
		}

		else {
			try {
				Double.parseDouble(price);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
	}

	private boolean productDescriptionValidation(String description) {
		Alert alert;

		if (description.length() < 200) {
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Too short product description");
			alert.setHeaderText("Product description should be greater than 200 characters.");
			alert.show();
			return false;
		}

		else if (description.length() > 1000) {
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Too long product description");
			alert.setHeaderText("Product description should be less than 1000 characters.");
			alert.show();
			return false;
		}

		return true;
	}

	protected void insertProductInDatabase() {
		Session session = ConnectDatabase.getSession();
		Transaction transaction = ConnectDatabase.getTransaction(session);

		ProductList product = new ProductList();
		product.setProductId(productId);
		product.setTitle(pHeading);
		product.setNewPrice(pNewPrice);
		product.setOldPrice(pOldPrice);
		product.setDeliveryCharge(pDeliveryCharges);
		product.setBrand(pBrand);
		product.setCategory(pCategory);
		product.setDescription(pDescription);
		product.setImageExtension(imageExtension);
		product.setQuantity(pQuantity);

		session.save(product);
		transaction.commit();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Product Added");
		alert.setHeaderText("Product is successfully added.");
		alert.show();
	}

	private void updateProductInDatabase() {
		Session session = ConnectDatabase.getSession();

		ProductList product = session.get(ProductList.class, productId);
		product.setTitle(pHeading);
		product.setNewPrice(pNewPrice);
		product.setOldPrice(pOldPrice);
		product.setDeliveryCharge(pDeliveryCharges);
		product.setBrand(pBrand);
		product.setCategory(pCategory);
		product.setDescription(pDescription);
		if(isImageChanged) {
			product.setImageExtension(imageExtension);
		}
		product.setQuantity(pQuantity);
		
		Transaction transaction = ConnectDatabase.getTransaction(session);
		session.update(product);
		transaction.commit();
		session.close();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Product Updated");
		alert.setHeaderText("Product is successfully updated.");
		alert.show();
	}

	protected void saveImageInFolder() {
		try {
			String imgUrl = pImage.getUrl();
			imageExtension = imgUrl.substring(imgUrl.lastIndexOf('.'));
			FileInputStream fis = new FileInputStream(new File(imgUrl));
			String newImgName = "img" + productId + imageExtension;
			FileOutputStream fos = new FileOutputStream(new File("src/ProductImages/" + newImgName));

			byte[] buf = new byte[100000];

			int byteread = 0;

			while ((byteread = fis.read(buf)) > 0) {
				fos.write(buf, 0, byteread);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
