package application;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Session;
import org.hibernate.Transaction;

import database.configuration.ConnectDatabase;
import database.tables.CustomerAddress;
import database.tables.CustomerLogin;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CustomerDetailsController implements Initializable {
	private boolean flagImagePassword, flagImageConfirmPassword;
	private CustomerLogin customerLogin = null;
	private CustomerAddress customerAddress = null;

	@FXML
	private Button btnSaveDetails;

	@FXML
	private ComboBox<String> comboBoxSecurityQuestion, comboBoxState;

	@FXML
	private ImageView imageConfirmPassword, imagePassword;

	@FXML
	private Label lblPasswordError;

	@FXML
	private TextField textAnswer, textAreaStreet, textCity, textContactNumber, textEmail, textFirstName,
			textHouseNumber, textLandmark, textLastName, textPincode, textVisibleConfirmPassword, textVisiblePassword;

	@FXML
	private PasswordField textInvisibleConfirmPassword, textInvisiblePassword;

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
	void echoConfirmPassword(KeyEvent event) {
		textVisibleConfirmPassword.setText(textInvisibleConfirmPassword.getText().trim());
	}

	@FXML
	void echoPassword(KeyEvent event) {
		textVisiblePassword.setText(textInvisiblePassword.getText().trim());
		lblPasswordError.setVisible(!checkPassword(textInvisiblePassword.getText()));
		lblPasswordError.setVisible(!checkPassword(textInvisiblePassword.getText()));
	}

	@FXML
	void showHideConfirmPassword(MouseEvent event) {
		if (flagImageConfirmPassword) {
			textVisibleConfirmPassword.setVisible(!flagImageConfirmPassword);
			textInvisibleConfirmPassword.setVisible(flagImageConfirmPassword);
			imageConfirmPassword.setImage(new Image("/icons/hidePassword.png"));
			flagImageConfirmPassword = !flagImageConfirmPassword;
		} else {
			textVisibleConfirmPassword.setVisible(!flagImageConfirmPassword);
			textInvisibleConfirmPassword.setVisible(flagImageConfirmPassword);
			imageConfirmPassword.setImage(new Image("/icons/showPassword.png"));
			flagImageConfirmPassword = !flagImageConfirmPassword;
		}
	}

	@FXML
	void showHidePassword(MouseEvent event) {
		if (flagImagePassword) {
			textVisiblePassword.setVisible(!flagImagePassword);
			textInvisiblePassword.setVisible(flagImagePassword);
			imagePassword.setImage(new Image("/icons/hidePassword.png"));
			flagImagePassword = !flagImagePassword;
		} else {
			textVisiblePassword.setVisible(!flagImagePassword);
			textInvisiblePassword.setVisible(flagImagePassword);
			imagePassword.setImage(new Image("/icons/showPassword.png"));
			flagImagePassword = !flagImagePassword;
		}
	}

	@FXML
	void saveDeteailsAction(ActionEvent event) {
		if(validation()) {
			updateCustomerLoginInDatabase();
			updateCustomerAddressInDatabase();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Details");
			alert.setHeaderText("Your Details Are Updated Successfully.");
			alert.show();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		btnSaveDetails.backgroundProperty().set(new Background(new BackgroundFill(Color.WHITE, null, null)));
		insertDetailsInComboBox();
		setDetails();
	}
	
	private void insertDetailsInComboBox() {
		comboBoxState.getItems().clear();
		comboBoxSecurityQuestion.getItems().clear();

		String[] states = { "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat",
				"Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra",
				"Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu",
				"Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal", "Andaman and Nicobar",
				"Chandigarh", "Dadra and Nagar Haveli and Daman and Diu", "Delhi", "Jammu and Kashmir", "Ladakh",
				"Lakshadweep", "Puducherry" };
		comboBoxState.getItems().addAll(states);

		comboBoxSecurityQuestion.getItems().addAll("Select a security question", "Your favorite movie?",
				"Your favorite food?", "Your favourite restaurant?", "What is your favorite sport?",
				"What is the first name of your favorite uncle?", "What is your oldest cousin's name?",
				"Mother's maiden name?", "What is the first name of your favorite aunt?",
				"Where did you spend your childhood summers?", "What is your skin color?",
				"What was the last name of your favorite teacher?", "What was the last name of your childhood friend?",
				"What was your favorite food as a child?", "What was the last name of your first boss?",
				"Where did you meet your spouse?", "What is the name of your first shcool?",
				"What is the name of the hospital you were born?", "What is your main frequent flier number?",
				"What was the model of your first car?", "What was the name of your favorite childhood pet?");
	}
	
	private void setDetails() {
		getCustomerDetailsFromDatabase();
		getCustomerAddressFromDatabase();
		
		textEmail.setText(HomePageController.email);
		textFirstName.setText(customerLogin.getFirstName());
		textLastName.setText(customerLogin.getLastName());
		
		textInvisiblePassword.setText(customerLogin.getPassword());
		textVisiblePassword.setText(customerLogin.getPassword());
		textInvisibleConfirmPassword.setText(customerLogin.getPassword());
		textVisibleConfirmPassword.setText(customerLogin.getPassword());
		
		textContactNumber.setText(customerLogin.getContactNumber());
		comboBoxSecurityQuestion.getSelectionModel().select(customerLogin.getSecurityQuestion());
		textAnswer.setText(customerLogin.getAnswer());
		if(customerAddress != null) {
			textHouseNumber.setText(customerAddress.getHouseNumber());
			textAreaStreet.setText(customerAddress.getAreaStreet());
			textLandmark.setText(customerAddress.getLandmark());
			textCity.setText(customerAddress.getCity());
			textPincode.setText(customerAddress.getPincode());
			comboBoxState.getSelectionModel().select(customerAddress.getState());
		}
	}
	
	private void getCustomerDetailsFromDatabase() {
		Session session = ConnectDatabase.getSession();
		customerLogin = session.get(CustomerLogin.class, HomePageController.email);
		ConnectDatabase.closeSession();
	}
	
	private void getCustomerAddressFromDatabase() {
		Session session = ConnectDatabase.getSession();
		customerAddress = session.get(CustomerAddress.class, HomePageController.email);
		ConnectDatabase.closeSession();
	}

	private boolean checkPassword(String pwd) {
		Pattern p = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$");
		Matcher m = p.matcher(textInvisiblePassword.getText());
		if (!m.matches())
			return false;
		return true;
	}
	
	private void updateCustomerLoginInDatabase() {
		Session session = ConnectDatabase.getSession();
		customerLogin.setFirstName(textFirstName.getText().trim());
		customerLogin.setLastName(textLastName.getText().trim());
		customerLogin.setContactNumber(textContactNumber.getText().trim());
		customerLogin.setPassword(textInvisiblePassword.getText().trim());
		customerLogin.setSecurityQuestion(comboBoxSecurityQuestion.getSelectionModel().getSelectedItem());
		customerLogin.setAnswer(textAnswer.getText().trim());
		Transaction transaction = ConnectDatabase.getTransaction(session);
		session.update(customerLogin);
		transaction.commit();
	}
	
	private void updateCustomerAddressInDatabase() {
		boolean isExistAddress = false;
		if(customerAddress == null) {
			isExistAddress = true;
			customerAddress = new CustomerAddress();
		}
		customerAddress.setEmail(textEmail.getText().trim());
		customerAddress.setFirstName(textFirstName.getText().trim());
		customerAddress.setLastName(textLastName.getText().trim());
		customerAddress.setPhoneNumber(textContactNumber.getText().trim());
		customerAddress.setHouseNumber(textHouseNumber.getText().trim());
		customerAddress.setAreaStreet(textAreaStreet.getText().trim());
		customerAddress.setLandmark(textLandmark.getText().trim());
		customerAddress.setPincode(textPincode.getText().trim());
		customerAddress.setCity(textCity.getText().trim());
		customerAddress.setState(comboBoxState.getSelectionModel().getSelectedItem());
		
		Session session = ConnectDatabase.getSession();
		Transaction transaction = ConnectDatabase.getTransaction(session);
		if(isExistAddress) {
			session.save(customerAddress);
			transaction.commit();
		}
		else {
			session.update(customerAddress);
			transaction.commit();
		}
	}

	private boolean validation() {
		if(textFirstName.getText().isBlank()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("First Name");
			alert.setHeaderText("Please Enter First Name.");
			alert.show();
			return false;
		}
		if(textLastName.getText().isBlank()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Last Name");
			alert.setHeaderText("Please Enter Last Name.");
			alert.show();
			return false;
		}
		if (textContactNumber.getText().isBlank()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Contact Number");
			alert.setHeaderText("Please Enter Contact Number.");
			alert.show();
			return false;
		}
		Pattern p = Pattern.compile("^\\d{10}$");
		Matcher m = p.matcher(textContactNumber.getText());
		if (!m.matches()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Contact Number");
			alert.setHeaderText("Please Enter Valid Contact Number.");
			alert.show();
			return false;
		}
		if (textInvisiblePassword.getText().isBlank()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Password");
			alert.setHeaderText("Please Enter Password.");
			alert.show();
			return false;
		}
		p = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$");
		m = p.matcher(textInvisiblePassword.getText());
		if (!m.matches()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Password");
			alert.setHeaderText("Please Enter Valid Password.");
			alert.show();
			return false;
		}
		if (textInvisibleConfirmPassword.getText().isBlank()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Confirm Password");
			alert.setHeaderText("Please Confirm Password.");
			alert.show();
			return false;
		}
		if (!textInvisibleConfirmPassword.getText().equals(textInvisiblePassword.getText())) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Password");
			alert.setHeaderText("Password don't match.");
			alert.show();
			return false;
		}
		if (comboBoxSecurityQuestion.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Security Question");
			alert.setHeaderText("Please Select Security Question.");
			alert.show();
			return false;
		}
		if (textAnswer.getText().isBlank()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Answer");
			alert.setHeaderText("Please Enter Answer to Security Question.");
			alert.show();
			return false;
		}
		if (textHouseNumber.getText().isBlank()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Address");
			alert.setHeaderText("Please Enter Flat/House Number.");
			alert.show();
			return false;
		}
		if (textAreaStreet.getText().isBlank()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Address");
			alert.setHeaderText("Please Enter Area/Street Name.");
			alert.show();
			return false;
		}
		if (textLandmark.getText().isBlank()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Address");
			alert.setHeaderText("Please Enter Any Landmark.");
			alert.show();
			return false;
		}
		if (textPincode.getText().isBlank()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Address");
			alert.setHeaderText("Please Enter Pincode Of Your Area.");
			alert.show();
			return false;
		}
		p = Pattern.compile("^[1-9][0-9]{5}$");
		m = p.matcher(textPincode.getText());
		if (!m.matches()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Address");
			alert.setHeaderText("Please Enter valid Pin Code.");
			alert.show();
			return false;
		}

		if (textCity.getText().isBlank()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Address");
			alert.setHeaderText("Please Enter City.");
			alert.show();
			return false;
		}
		if (comboBoxState.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Address");
			alert.setHeaderText("Please Enter State.");
			alert.show();
			return false;
		}
		return true;
	}
}
