package seller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import application.Main;
import database.configuration.ConnectDatabase;
import database.tables.SellerAddress;
import database.tables.SellerBankDetails;
import database.tables.SellerLogin;
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
import javafx.stage.Stage;

public class SellerDetailsController implements Initializable {
	private boolean flagImagePassword, flagImageConfirmPassword;
	public static boolean registrationFlag = false;

	@FXML
	private ComboBox<String> comboBoxBankState, comboBoxSecurityQuestion, comboBoxState;

	@FXML
	private ImageView imageConfirmPassword, imagePassword;

	@FXML
	private Label lblEditAddress, lblEditBankDetails, lblEditBasicDetails, lblBasicDetailsError, lblAddressError,
			lblBankDetailsError;

	@FXML
	private TextField textAccountHolderName, textAccountNumber, textAnswer, textAreaStreet, textBankCity, textBankName,
			textBankPincode, textBranchName, textCity, textContactNumber, textEmail, textFirstName, textGSTINNumber,
			textHouseNumber, textIFSCCode, textLandmark, textLastName, textPancardNumber, textPincode,
			textVisibleConfirmPassword, textVisiblePassword;

	@FXML
	private PasswordField textInvisibleConfirmPassword, textInvisiblePassword;

	@FXML
	private Button btnsave;

	@FXML
	void editAddressAction(MouseEvent event) {
		if (lblEditAddress.getText().equals("Edit")) {
			setAddressDetailsEditable(true);
			lblEditAddress.setText("Save");
		} else {
			if(AddressValidation()) {
				updateAddressInDatabase();
				lblEditAddress.setText("Edit");
				setAddressDetailsEditable(false);
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Basic Details");
				alert.setHeaderText("Your Address Is Updated Successfully.");
				alert.show();
			}
		}
	}

	@FXML
	void editBankDetailsAction(MouseEvent event) {
		if (lblEditBankDetails.getText().equals("Edit")) {
			setBankDetailsEditable(true);
			lblEditBankDetails.setText("Save");
		} else {
			if(bankDetailsValidaion()) {
				updateBankDetailsInDataBase();
				lblEditBankDetails.setText("Edit");
				setBankDetailsEditable(false);
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Basic Details");
				alert.setHeaderText("Your Bank Details Are Updated Successfully.");
				alert.show();
			}
		}
	}

	@FXML
	void editBasicDetailsAction(MouseEvent event) {
		if (lblEditBasicDetails.getText().equals("Edit")) {
			setBasicDetailsEditable(true);
			lblEditBasicDetails.setText("Save");
		} else {
			if(basicDetailsValidation()) {
				updateBasicDetailsInDatabase();
				lblEditBasicDetails.setText("Edit");
				setBasicDetailsEditable(false);
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Basic Details");
				alert.setHeaderText("Your Basic Details Are Updated Successfully.");
				alert.show();
			}
		}
	}

	@FXML
	void saveDetailsAction(ActionEvent event) {
		if (basicDetailsValidation() && bankDetailsValidaion() && AddressValidation()) {
			updateBasicDetailsInDatabase();
			insertBankDetailsInDatabase();
			insertAddressInDatabase();
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Basic Details");
			alert.setHeaderText("Your Details Are Saved Successfully.");
			alert.show();
			
			btnsave.setVisible(false);
			lblEditBasicDetails.setVisible(true);
			lblEditBankDetails.setVisible(true);
			lblEditAddress.setVisible(true);
		}
	}

	@FXML
	void back(MouseEvent event) {
		if(registrationFlag) {
			try {
				Parent root = FXMLLoader.load(Main.class.getResource("@../../../seller/SellerLoginPage.fxml"));
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
	}

	@FXML
	void echoConfirmPassword(KeyEvent event) {
		textVisibleConfirmPassword.setText(textInvisibleConfirmPassword.getText());
	}

	@FXML
	void echoPassword(KeyEvent event) {
		textVisiblePassword.setText(textInvisiblePassword.getText());
	}

	@FXML
	void showHideConfirmPassword(MouseEvent event) {
		if (!flagImageConfirmPassword) {
			imageConfirmPassword.setImage(new Image("/icons/showPassword.png"));
			flagImageConfirmPassword = !flagImageConfirmPassword;

			textVisibleConfirmPassword.setVisible(true);
			textInvisibleConfirmPassword.setVisible(false);
		} else {
			imageConfirmPassword.setImage(new Image("/icons/hidePassword.png"));
			flagImageConfirmPassword = !flagImageConfirmPassword;

			textVisibleConfirmPassword.setVisible(false);
			textInvisibleConfirmPassword.setVisible(true);
		}
	}

	@FXML
	void showHidePassword(MouseEvent event) {
		if (!flagImagePassword) {
			imagePassword.setImage(new Image("/icons/showPassword.png"));
			flagImagePassword = !flagImagePassword;

			textVisiblePassword.setVisible(true);
			textInvisiblePassword.setVisible(false);
		} else {
			imagePassword.setImage(new Image("/icons/hidePassword.png"));
			flagImagePassword = !flagImagePassword;

			textVisiblePassword.setVisible(false);
			textInvisiblePassword.setVisible(true);
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		insertDetailsInComboBox();
		Session session = ConnectDatabase.getSession();
		SellerAddress address = session.get(SellerAddress.class, SellerLoginPageController.email);
		SellerBankDetails bankDetails = session.get(SellerBankDetails.class, SellerLoginPageController.email);
		if (address == null || bankDetails == null) {
			registrationFlag = true;
		}
		if (registrationFlag) {
			textGSTINNumber.setEditable(true);

			setBasicDetailsEditable(true);
			setAddressDetailsEditable(true);
			setBankDetailsEditable(true);
			lblEditBasicDetails.setVisible(false);
			lblEditAddress.setVisible(false);
			lblEditBankDetails.setVisible(false);
			btnsave.setVisible(true);
		} else {
			setBasicDetailsEditable(false);
			setAddressDetailsEditable(false);
			setBankDetailsEditable(false);
			setAddressDetails(address);
			setBankDetails(bankDetails);
		}
		SellerLogin sellerLogin = session.get(SellerLogin.class, SellerLoginPageController.email);
		setBasicDetails(sellerLogin);
	}

	private void setBasicDetails(SellerLogin seller) {
		textEmail.setText(seller.getEmail());
		textFirstName.setText(seller.getFirstName());
		textLastName.setText(seller.getLastName());
		textContactNumber.setText(seller.getContactNumber());

		textInvisiblePassword.setText(seller.getPassword());
		textVisiblePassword.setText(seller.getPassword());
		textInvisibleConfirmPassword.setText(seller.getPassword());
		textVisibleConfirmPassword.setText(seller.getPassword());

		comboBoxSecurityQuestion.getSelectionModel().select(seller.getSecurityQuestion());
		textAnswer.setText(seller.getAnswer());
	}
	
	private void setBankDetails(SellerBankDetails bankDetails) {
		textGSTINNumber.setText(bankDetails.getGstinNumber());
		
		textAccountNumber.setText(bankDetails.getAccountNumber());
		textAccountHolderName.setText(bankDetails.getAccountHolderName());
		textIFSCCode.setText(bankDetails.getIfscCode());
		textBankName.setText(bankDetails.getBankName());
		textBranchName.setText(bankDetails.getBranchName());
		textPancardNumber.setText(bankDetails.getPancardNumber());
		textBankPincode.setText(bankDetails.getBankPincode());
		textBankCity.setText(bankDetails.getBankCity());
		comboBoxBankState.getSelectionModel().select(bankDetails.getBankState());
	}

	private void setAddressDetails(SellerAddress address) {
		textHouseNumber.setText(address.getHouseNumber());
		textAreaStreet.setText(address.getAreaStreet());
		textLandmark.setText(address.getLandmark());
		textPincode.setText(address.getPincode());
		textCity.setText(address.getCity());
		comboBoxState.getSelectionModel().select(address.getState());
	}

	private void setBasicDetailsEditable(boolean flag) {
		textFirstName.setEditable(flag);
		textLastName.setEditable(flag);
		textContactNumber.setEditable(flag);
		textInvisiblePassword.setEditable(flag);
		textVisiblePassword.setEditable(flag);
		textInvisibleConfirmPassword.setEditable(flag);
		textVisibleConfirmPassword.setEditable(flag);
		textAnswer.setEditable(flag);

		comboBoxSecurityQuestion.setDisable(!flag);
		comboBoxSecurityQuestion.setOpacity(1);
	}

	private void setBankDetailsEditable(boolean flag) {
		textAccountNumber.setEditable(flag);
		textAccountHolderName.setEditable(flag);
		textIFSCCode.setEditable(flag);
		textBankName.setEditable(flag);
		textBranchName.setEditable(flag);
		textPancardNumber.setEditable(flag);
		textBankPincode.setEditable(flag);
		textBankCity.setEditable(flag);

		comboBoxBankState.setDisable(!flag);
		comboBoxBankState.setOpacity(1);
	}

	private void setAddressDetailsEditable(boolean flag) {
		textHouseNumber.setEditable(flag);
		textAreaStreet.setEditable(flag);
		textLandmark.setEditable(flag);
		textPincode.setEditable(flag);
		textCity.setEditable(flag);

		comboBoxState.setDisable(!flag);
		comboBoxState.setOpacity(1);
	}

	private void insertDetailsInComboBox() {
		comboBoxBankState.getItems().clear();
		comboBoxState.getItems().clear();
		comboBoxSecurityQuestion.getItems().clear();

		String[] states = { "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat",
				"Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra",
				"Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu",
				"Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal", "Andaman and Nicobar",
				"Chandigarh", "Dadra and Nagar Haveli and Daman and Diu", "Delhi", "Jammu and Kashmir", "Ladakh",
				"Lakshadweep", "Puducherry" };
		comboBoxState.getItems().addAll(states);
		comboBoxBankState.getItems().addAll(states);

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

	private boolean basicDetailsValidation() {
		if (textFirstName.getText().isBlank()) {
			lblBasicDetailsError.setText("Please Enter First Name.");
			return false;
		}
		if (textLastName.getText().isBlank()) {
			lblBasicDetailsError.setText("Please Enter Last Name.");
			return false;
		}
		if (textContactNumber.getText().isBlank()) {
			lblBasicDetailsError.setText("Please Enter Contact Number.");
			return false;
		}
		Pattern p = Pattern.compile("^\\d{10}$");
		Matcher m = p.matcher(textContactNumber.getText());
		if (!m.matches()) {
			lblBasicDetailsError.setText("Please Enter Valid Contact Number.");
			return false;
		}
		if (textInvisiblePassword.getText().isBlank()) {
			lblBasicDetailsError.setText("Please Enter Password.");
			return false;
		}
		p = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$");
		m = p.matcher(textInvisiblePassword.getText());
		if (!m.matches()) {
			lblBasicDetailsError.setText(
					"Password must have atleast one lowercase, one uppercase, one numeric and one special character and length should between 8 to 20.");
			return false;
		}
		if (textInvisibleConfirmPassword.getText().isBlank()) {
			lblBasicDetailsError.setText("Please Confirm Password.");
			return false;
		}
		if (!textInvisibleConfirmPassword.getText().equals(textInvisiblePassword.getText())) {
			lblBasicDetailsError.setText("Password don't match.");
			return false;
		}
		if (comboBoxSecurityQuestion.getSelectionModel().getSelectedItem() == null) {
			lblBasicDetailsError.setText("Please Select Security Question.");
			return false;
		}
		if (textAnswer.getText().isBlank()) {
			lblBasicDetailsError.setText("Please Enter Answer to Security Question.");
			return false;
		}
		if (textGSTINNumber.getText().isBlank()) {
			lblBasicDetailsError.setText("Please Enter GSTIN Number.");
			return false;
		}
		p = Pattern.compile(
				"^([0][1-9]|[1-2][0-9]|[3][0-7])([a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9a-zA-Z]{1}[zZ]{1}[0-9a-zA-Z]{1})+$");
		m = p.matcher(textGSTINNumber.getText());
		if (!m.matches()) {
			lblBasicDetailsError.setText("Please Enter Valid GSTIN Number.");
			return false;
		}

		if (registrationFlag) {
			Session session = ConnectDatabase.getSession();
			@SuppressWarnings("unchecked")
			Query<SellerBankDetails> query = (Query<SellerBankDetails>) session
					.createQuery("from SellerBankDetails where gstinNumber = :gstinNumber");
			query.setParameter("gstinNumber", textGSTINNumber.getText());
			if (!query.list().isEmpty()) {
				lblBasicDetailsError
						.setText("This GSTIN Number Is Already Registered, Please Enter Unique GSTIN Number.");
				session.close();
				return false;
			}
		}
		lblBasicDetailsError.setText("");
		return true;
	}

	private boolean bankDetailsValidaion() {
		if (textAccountNumber.getText().isBlank()) {
			lblBankDetailsError.setText("Please Enter Account Number.");
			return false;
		}

		Pattern p = Pattern.compile("^[0-9]{9,18}$");
		Matcher m = p.matcher(textAccountNumber.getText());
		if (!m.matches()) {
			lblBankDetailsError.setText("Please Enter Valid Account Number.");
			return false;
		}

		if (textAccountHolderName.getText().isBlank()) {
			lblBankDetailsError.setText("Please Enter Name.");
			return false;
		}
		if (textIFSCCode.getText().isBlank()) {
			lblBankDetailsError.setText("Please Enter IFSC Code Of Bank.");
			return false;
		}

		p = Pattern.compile("^[A-Z]{4}[0][A-Z0-9]{6}$");
		m = p.matcher(textIFSCCode.getText());
		if (!m.matches()) {
			lblBankDetailsError.setText("Please Enter Valid IFSC Code.");
			return false;
		}

		if (textBankName.getText().isBlank()) {
			lblBankDetailsError.setText("Please Enter Bank Name.");
			return false;
		}
		if (textBranchName.getText().isBlank()) {
			lblBankDetailsError.setText("Please Enter Branch Name.");
			return false;
		}
		if (textPancardNumber.getText().isBlank()) {
			lblBankDetailsError.setText("Please Enter Pancard Number.");
			return false;
		}

		p = Pattern.compile("[A-Z]{3}[ABCFGHLJPTF]{1}[A-Z]{1}[0-9]{4}[A-Z]{1}");
		m = p.matcher(textPancardNumber.getText());
		if (!m.matches()) {
			lblBankDetailsError.setText("Please Enter Valid Pan Number.");
			return false;
		}

		if (textBankPincode.getText().isBlank()) {
			lblBankDetailsError.setText("Please Enter Pincode.");
			return false;
		}

		p = Pattern.compile("^[1-9][0-9]{5}$");
		m = p.matcher(textBankPincode.getText());
		if (!m.matches()) {
			lblBankDetailsError.setText("Please Enter Pin Code.");
			return false;
		}

		if (textBankCity.getText().isBlank()) {
			lblBankDetailsError.setText("Please Enter City Of Bank.");
			return false;
		}
		if (comboBoxBankState.getSelectionModel().getSelectedItem() == null) {
			lblBankDetailsError.setText("Please Enter State Of Bank.");
			return false;
		}
		lblBankDetailsError.setText("");
		return true;
	}

	private boolean AddressValidation() {
		if (textHouseNumber.getText().isBlank()) {
			lblAddressError.setText("Please Enter Flat/House Number.");
			return false;
		}
		if (textAreaStreet.getText().isBlank()) {
			lblAddressError.setText("Please Enter Area/Street Name.");
			return false;
		}
		if (textLandmark.getText().isBlank()) {
			lblAddressError.setText("Please Enter Any Landmark.");
			return false;
		}
		if (textPincode.getText().isBlank()) {
			lblAddressError.setText("Please Enter Pincode Of Your Area.");
			return false;
		}
		Pattern p = Pattern.compile("^[1-9][0-9]{5}$");
		Matcher m = p.matcher(textPincode.getText());
		if (!m.matches()) {
			lblAddressError.setText("Please Enter valid Pin Code.");
			return false;
		}

		if (textCity.getText().isBlank()) {
			lblAddressError.setText("Please Enter City.");
			return false;
		}
		if (comboBoxState.getSelectionModel().getSelectedItem() == null) {
			lblAddressError.setText("Please Enter State.");
			return false;
		}
		lblAddressError.setText("");
		return true;
	}
	
	private void updateBasicDetailsInDatabase() {
		Session session = ConnectDatabase.getSession();
		SellerLogin sellerLogin = session.get(SellerLogin.class, SellerLoginPageController.email);
		sellerLogin.setFirstName(textFirstName.getText().trim());
		sellerLogin.setLastName(textLastName.getText().trim());
		sellerLogin.setContactNumber(textContactNumber.getText().trim());
		sellerLogin.setPassword(textInvisiblePassword.getText().trim());
		sellerLogin.setSecurityQuestion(comboBoxSecurityQuestion.getSelectionModel().getSelectedItem());
		sellerLogin.setAnswer(textAnswer.getText().trim());
		
		Transaction transaction = ConnectDatabase.getTransaction(session);
		session.update(sellerLogin);
		transaction.commit();
		session.close();
	}
	
	private void insertBankDetailsInDatabase() {
		Session session = ConnectDatabase.getSession();
		SellerBankDetails sellerBankDetails = new SellerBankDetails();
		sellerBankDetails.setEmail(textEmail.getText().trim());
		sellerBankDetails.setAccountNumber(textAccountNumber.getText().trim());
		sellerBankDetails.setAccountHolderName(textAccountHolderName.getText().trim());
		sellerBankDetails.setIfscCode(textIFSCCode.getText().trim());
		sellerBankDetails.setBankName(textBankName.getText().trim());
		sellerBankDetails.setBranchName(textBranchName.getText().trim());
		sellerBankDetails.setPancardNumber(textPancardNumber.getText().trim());
		sellerBankDetails.setBankPincode(textBankPincode.getText().trim());
		sellerBankDetails.setBankCity(textBankCity.getText().trim());
		sellerBankDetails.setBankState(comboBoxBankState.getSelectionModel().getSelectedItem());
		sellerBankDetails.setGstinNumber(textGSTINNumber.getText().trim());
		
		Transaction transaction = ConnectDatabase.getTransaction(session);
		session.save(sellerBankDetails);
		transaction.commit();
		session.close();
	}
	
	private void insertAddressInDatabase() {
		Session session = ConnectDatabase.getSession();
		SellerAddress sellerAddress = new SellerAddress();
		sellerAddress.setEmail(textEmail.getText().trim());
		sellerAddress.setHouseNumber(textHouseNumber.getText().trim());
		sellerAddress.setAreaStreet(textAreaStreet.getText().trim());
		sellerAddress.setLandmark(textLandmark.getText().trim());
		sellerAddress.setPincode(textPincode.getText().trim());
		sellerAddress.setCity(textCity.getText().trim());
		sellerAddress.setState(comboBoxState.getSelectionModel().getSelectedItem());
		
		Transaction transaction = ConnectDatabase.getTransaction(session);
		session.save(sellerAddress);
		transaction.commit();
		session.close();
	}
	
	private void updateBankDetailsInDataBase() {
		Session session = ConnectDatabase.getSession();
		SellerBankDetails sellerBankDetails = session.get(SellerBankDetails.class, textEmail.getText().trim());
		
		sellerBankDetails.setAccountNumber(textAccountNumber.getText().trim());
		sellerBankDetails.setAccountHolderName(textAccountHolderName.getText().trim());
		sellerBankDetails.setIfscCode(textIFSCCode.getText().trim());
		sellerBankDetails.setBankName(textBankName.getText().trim());
		sellerBankDetails.setBranchName(textBranchName.getText().trim());
		sellerBankDetails.setPancardNumber(textPancardNumber.getText().trim());
		sellerBankDetails.setBankPincode(textBankPincode.getText().trim());
		sellerBankDetails.setBankCity(textBankCity.getText().trim());
		sellerBankDetails.setBankState(comboBoxBankState.getSelectionModel().getSelectedItem());
		sellerBankDetails.setGstinNumber(textGSTINNumber.getText().trim());
		
		Transaction transaction = ConnectDatabase.getTransaction(session);
		session.update(sellerBankDetails);
		transaction.commit();
		session.close();
	}
	
	private void updateAddressInDatabase() {
		Session session = ConnectDatabase.getSession();
		SellerAddress sellerAddress = session.get(SellerAddress.class, textEmail.getText().trim());
		sellerAddress.setEmail(textEmail.getText().trim());
		sellerAddress.setHouseNumber(textHouseNumber.getText().trim());
		sellerAddress.setAreaStreet(textAreaStreet.getText().trim());
		sellerAddress.setLandmark(textLandmark.getText().trim());
		sellerAddress.setPincode(textPincode.getText().trim());
		sellerAddress.setCity(textCity.getText().trim());
		sellerAddress.setState(comboBoxState.getSelectionModel().getSelectedItem());
		
		Transaction transaction = ConnectDatabase.getTransaction(session);
		session.update(sellerAddress);
		transaction.commit();
		session.close();
	}
}


