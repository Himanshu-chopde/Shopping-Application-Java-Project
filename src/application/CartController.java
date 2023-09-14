package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import database.configuration.ConnectDatabase;
import database.tables.CustomerAddress;
import database.tables.CustomerCart;
import database.tables.CustomerLogin;
import database.tables.ProductList;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CartController implements Initializable {
	private int time = 200;
	public List<ProductDetails> productDetailsList = null;
	
	public static CartController cartController = null;

	CustomerLogin customerLogin;

	@FXML
	private Label lblFirstName, lblLastName, lblCity, lblHouseNumber, lblLandmark, lblPhoneNumber, lblPincode, lblState,
			lblStreetArea;

	@FXML
	private TextField textFirstName, textLastName, textCity, textHouseNumber, textLandmark, textPhoneNumber,
			textPincode, textStreetArea;

	@FXML
	private ComboBox<String> comboBoxState;

	@FXML
	private Button btnCheckout, btnSaveAddress;

	@FXML
	ImageView imgBack;

	@FXML
	public TilePane tilePane;
	
	@FXML
	private Pane emptyPane;
	
	private boolean isAddressAvailable;

	@FXML
	void proceedToCheckout(ActionEvent event) {
		Session session = ConnectDatabase.getSession();
		CustomerAddress address = null;
		address = session.get(CustomerAddress.class, HomePageController.email);
		if(address == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Address");
			alert.setHeaderText("Please enter and save your delivery address.");
			alert.show();
			return;
		}
		
		int count = 0;
		for(int i = 0; i < productDetailsList.size(); i++) {
			if(Integer.parseInt(productDetailsList.get(i).getQuantity()) > 0) {
				count++;
			}
		}
	
		if(count > 0) {
			try {
				Parent root = FXMLLoader.load(Main.class.getResource("PaymentPage.fxml"));
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
		else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Empty Cart");
			alert.setHeaderText("Your cart is empty!");
			alert.setContentText("Please add products to checkout.");
			alert.show();
		}
		
	}

	@FXML
	void saveAddress(ActionEvent event) {
		if (addressValidation()) {
			insertAddressInDatabase();
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Address");
			alert.setHeaderText("Address saved successfully");
			alert.show();
		}
	}

	@FXML
	void back(MouseEvent event) {
		try {
			Parent root = FXMLLoader.load(Main.class.getResource("HomePage.fxml"));
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

	@FXML
	void moveFirstName(MouseEvent event) {
		if (textFirstName.getText().equals("")) {
			moveUp(lblFirstName);
		}
	}

	@FXML
	void moveLastName(MouseEvent event) {
		if (textLastName.getText().equals("")) {
			moveUp(lblLastName);
		}
	}

	@FXML
	void moveCity(MouseEvent event) {
		if (textCity.getText().equals("")) {
			moveUp(lblCity);
		}
	}

	@FXML
	void moveHouseNumber(MouseEvent event) {
		if (textHouseNumber.getText().equals("")) {
			moveUp(lblHouseNumber);
		}
	}

	@FXML
	void moveLandmark(MouseEvent event) {
		if (textLandmark.getText().equals("")) {
			moveUp(lblLandmark);
		}
	}

	@FXML
	void movePhoneNumber(MouseEvent event) {
		if (textPhoneNumber.getText().equals("")) {
			moveUp(lblPhoneNumber);
		}
	}

	@FXML
	void movePincode(MouseEvent event) {
		if (textPincode.getText().equals("")) {
			moveUp(lblPincode);
		}
	}

	@FXML
	void moveState(MouseEvent event) {
		if (comboBoxState.getSelectionModel().getSelectedItem() == null) {
			moveUp(lblState);
		}
	}

	@FXML
	void moveStreetArea(MouseEvent event) {
		if (textStreetArea.getText().equals("")) {
			moveUp(lblStreetArea);
		}
	}

	private void moveUp(Label label) {
		if (label.getTranslateX() == 0.0) {
			TranslateTransition translate = new TranslateTransition();
			translate.setByX(-10);
			translate.setByY(-20);
			translate.setDuration(Duration.millis(time));
			translate.setCycleCount(1);
			translate.setAutoReverse(true);
			translate.setNode(label);
			translate.play();
		}
	}

	private void addFocusTextField(TextField node1, Label node2) {
		node1.focusedProperty().addListener((arg0, arg1, arg2) -> {
			if (!arg2) {
				if (node1.getText().equals("")) {
					TranslateTransition translate = new TranslateTransition();
					translate.setByX(10);
					translate.setByY(20);
					translate.setDuration(Duration.millis(time));
					translate.setCycleCount(1);
					translate.setAutoReverse(true);
					translate.setNode(node2);
					translate.play();
				}
			}
		});
	}

	private void addFocusComboBox(ComboBox<String> node1, Label node2) {
		node1.focusedProperty().addListener((arg0, arg1, arg2) -> {
			if (!arg2) {
				if (node1.getSelectionModel().getSelectedItem() == null) {
					TranslateTransition translate = new TranslateTransition();
					translate.setByX(10);
					translate.setByY(20);
					translate.setDuration(Duration.millis(time));
					translate.setCycleCount(1);
					translate.setAutoReverse(true);
					translate.setNode(node2);
					translate.play();
				}
			}
		});
	}

	@Override
	public void initialize(URL arg3, ResourceBundle arg4) {
		getAddressFromDatabase();
		addFocusTextField(textFirstName, lblFirstName);
		addFocusTextField(textLastName, lblLastName);
		addFocusTextField(textCity, lblCity);
		addFocusTextField(textHouseNumber, lblHouseNumber);
		addFocusTextField(textLandmark, lblLandmark);
		addFocusTextField(textPincode, lblPincode);
		addFocusTextField(textPhoneNumber, lblPhoneNumber);
		addFocusTextField(textStreetArea, lblStreetArea);
		addFocusComboBox(comboBoxState, lblState);
		comboBoxState.getItems().addAll("Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa",
				"Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh",
				"Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim",
				"Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal",
				"Andaman and Nicobar", "Chandigarh", "Dadra and Nagar Haveli and Daman and Diu", "Delhi",
				"Jammu and Kashmir", "Ladakh", "Lakshadweep", "Puducherry");

		getCartProdoductFromDatabase();
		displayProducts(productDetailsList);
		if(productDetailsList.isEmpty()) {
			tilePane.getChildren().add(emptyPane);
		}
		cartController = this;
	}

	public void displayProducts(List<ProductDetails> product) {
		getCartProdoductFromDatabase();
		try {
			tilePane.getChildren().clear();
			if (product != null) {
				for (int i = 0; i < product.size(); i++) {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("ShowCartProduct.fxml"));
					VBox vbox = loader.load();

					ShowCartProductController productController = loader.getController();

					productController.setProductData(product.get(i));

					tilePane.getChildren().add(vbox);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<ProductDetails> getCartProdoductFromDatabase() {
		if (productDetailsList == null) {
			productDetailsList = new ArrayList<>();
			List<CustomerCart> cartList = null;
			ProductDetails product = null;

			Session session = ConnectDatabase.getSession();

			@SuppressWarnings("unchecked")
			Query<CustomerCart> query = (Query<CustomerCart>) session
					.createQuery("from CustomerCart where customerEmail = :email");
			query.setParameter("email", HomePageController.email);
			cartList = query.list();

			for (CustomerCart cartp : cartList) {
				ProductList pList = session.get(ProductList.class, cartp.getProductId());
				product = new ProductDetails();

				product.setId(pList.getProductId());
				product.setImageSrc("/ProductImages/img" + pList.getProductId() + pList.getImageExtension());
				product.setProductHeading(pList.getTitle());
				product.setNewPrice(Double.parseDouble(pList.getNewPrice()));
				product.setOldPrice(Double.parseDouble(pList.getOldPrice()));
				product.setDeliveryCharges(Double.parseDouble(pList.getDeliveryCharge()));
				product.setProductBrand(pList.getBrand());
				product.setRating(pList.getRating());
				product.setDescription(pList.getDescription());
				product.setQuantity(((Integer) cartp.getProductQuantity()).toString());
				product.setCategory(pList.getCategory());
				productDetailsList.add(product);
			}
		}
		return productDetailsList;
	}

	private void getAddressFromDatabase() {
		Session session = ConnectDatabase.getSession();
		CustomerAddress address = session.get(CustomerAddress.class, HomePageController.email);
		if (address == null) {
			isAddressAvailable = false;
			customerLogin = session.get(CustomerLogin.class, HomePageController.email);
			if (customerLogin != null) {
				textFirstName.setText(customerLogin.getFirstName());
				textLastName.setText(customerLogin.getLastName());
				textPhoneNumber.setText(customerLogin.getContactNumber());

				moveUp(lblFirstName);
				moveUp(lblLastName);
				moveUp(lblPhoneNumber);
			}
		} else {
			isAddressAvailable = true;
			textFirstName.setText(address.getFirstName());
			textLastName.setText(address.getLastName());
			textPhoneNumber.setText(address.getPhoneNumber());
			textHouseNumber.setText(address.getHouseNumber());
			textStreetArea.setText(address.getAreaStreet());
			textLandmark.setText(address.getLandmark());
			textPincode.setText(address.getPincode());
			textCity.setText(address.getCity());
			comboBoxState.getSelectionModel().select(address.getState());

			moveUp(lblFirstName);
			moveUp(lblLastName);
			moveUp(lblPhoneNumber);
			moveUp(lblHouseNumber);
			moveUp(lblStreetArea);
			moveUp(lblLandmark);
			moveUp(lblPincode);
			moveUp(lblCity);
			moveUp(lblState);
		}
	}

	private boolean addressValidation() {
		Alert alert = new Alert(AlertType.WARNING);
		if (textFirstName.getText().trim().isBlank()) {
			alert.setTitle("First Name");
			alert.setHeaderText("Please enter first name");
			alert.show();
			return false;
		}

		if (textLastName.getText().trim().isBlank()) {
			alert.setTitle("Last Name");
			alert.setHeaderText("Please enter last name");
			alert.show();
			return false;
		}

		if (textPhoneNumber.getText().trim().isBlank()) {
			alert.setTitle("Phone Number");
			alert.setHeaderText("Please enter phone number");
			alert.show();
			return false;
		}

		Pattern p = Pattern.compile("^\\d{10}$");
		Matcher m = p.matcher(textPhoneNumber.getText().trim());
		if (!m.matches()) {
			alert.setTitle("Contact Number");
			alert.setHeaderText("Invalid Contact Number");
			alert.setContentText("Please enter valid contact number");
			alert.show();
			return false;

		}

		if (textHouseNumber.getText().trim().isBlank()) {
			alert.setTitle("Flat/House Number");
			alert.setHeaderText("Please enter flat/house number");
			alert.show();
			return false;
		}

		if (textStreetArea.getText().trim().isBlank()) {
			alert.setTitle("Area, Street, Village");
			alert.setHeaderText("Please enter area/street/village");
			alert.show();
			return false;
		}

		if (textLandmark.getText().trim().isBlank()) {
			alert.setTitle("Landmark");
			alert.setHeaderText("Please enter landmark");
			alert.show();
			return false;
		}

		if (textPincode.getText().trim().isBlank()) {
			alert.setTitle("Pincode");
			alert.setHeaderText("Please enter pincode");
			alert.show();
			return false;
		}

		if (textCity.getText().trim().isBlank()) {
			alert.setTitle("Town/City");
			alert.setHeaderText("Please enter town/city");
			alert.show();
			return false;
		}

		if (comboBoxState.getSelectionModel().getSelectedItem() == null) {
			alert.setTitle("State");
			alert.setHeaderText("Please enter state");
			alert.show();
			return false;
		}

		return true;
	}
	
	private void insertAddressInDatabase() {
		Session session = ConnectDatabase.getSession();
		Transaction transaction = ConnectDatabase.getTransaction(session);
		CustomerAddress address = new CustomerAddress();
		address.setEmail(HomePageController.email);
		address.setFirstName(textFirstName.getText().trim());
		address.setLastName(textLastName.getText().trim());
		address.setPhoneNumber(textPhoneNumber.getText().trim());
		address.setHouseNumber(textHouseNumber.getText().trim());
		address.setAreaStreet(textStreetArea.getText().trim());
		address.setLandmark(textLandmark.getText().trim());
		address.setPincode(textPincode.getText().trim());
		address.setCity(textCity.getText().trim());
		address.setState(comboBoxState.getSelectionModel().getSelectedItem());
		
		if(isAddressAvailable) {
			session.update(address);
		}
		else {
			session.save(address);
		}
		transaction.commit();
	}

}
