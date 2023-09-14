package seller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Session;
import org.hibernate.Transaction;

import application.Main;
import database.configuration.ConnectDatabase;
import database.tables.SellerAddress;
import database.tables.SellerBankDetails;
import database.tables.SellerLogin;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SellerLoginPageController implements Initializable {
	protected AnimationTimer at = null;
	protected double an = 5, t = 300;
	protected boolean btnflag = true;
	protected String databasePassword = "", databaseQuestion = "", databaseAnswer = "";

	protected static String email;
	protected String firstName;
	protected String lastName;
	protected String contactNumber;
	protected String password;
	protected String confirmPassword;
	protected String securityQuestion;
	protected String answer;
	protected int question;

	@FXML
	protected TextField textShowPassword, textHidePassword, textShowPassword1, textHidePassword1, textShowPassword2,
			textHidePassword2;

	@FXML
	protected TextField textEmailR, textFirstNameR, textLastNameR, textAnswerR, textContactNumberR, textConfirmPasswordR;

	@FXML
	protected TextField textEmailLogin, textEmailForgot, textAnswerForgot, textConfirmPassword1;

	@FXML
	protected Button btnLogin;

	@FXML
	protected ToggleButton btnShowPassword, btnShowPassword1, btnShowPassword2;

	@FXML
	protected ImageView imgShowPassword, imgShowPassword1, imgShowPassword2;

	@FXML
	protected Pane loginPane, signupPane, paneForgotPassword, paneResetPassword;

	@FXML
	protected ComboBox<String> comboBoxSecurityQuestionForgotPassword;
	@FXML
	protected ComboBox<String> comboBoxSecurityQuestionRegister;

	@FXML
	protected TextArea textValidPassword2, textValidPassword1;

	@FXML
	protected void forgotPasswordAction(ActionEvent event) {
		FadeTransition fade = new FadeTransition();
		fade.setDuration(Duration.millis(t));
		fade.setFromValue(0);
		paneForgotPassword.setVisible(true);
		fade.setToValue(10);
		fade.setNode(paneForgotPassword);
		fade.play();

		loginPane.setVisible(false);
	}

	@FXML
	protected void back(ActionEvent event) {
		FadeTransition fade = new FadeTransition();
		fade.setDuration(Duration.millis(t));
		fade.setFromValue(0);
		loginPane.setVisible(true);
		fade.setToValue(10);
		fade.setNode(loginPane);
		fade.play();

		paneForgotPassword.setVisible(false);
		paneResetPassword.setVisible(false);
	}

	@FXML
	protected void okButtonAction(ActionEvent event) {
		if (forgotPasswordValidation()) {
			FadeTransition fade = new FadeTransition();
			fade.setDuration(Duration.millis(t));
			fade.setFromValue(0);
			paneResetPassword.setVisible(true);
			fade.setToValue(10);
			fade.setNode(paneResetPassword);
			fade.play();

			paneForgotPassword.setVisible(false);
		}
	}

	@FXML
	protected void resetPasswordAction(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		if (resetPasswordValidation()) {
			updatePasswordInDatabase();
			alert.setTitle("Password reset");
			alert.setHeaderText("Reset password successfully.");
			alert.setContentText("Now you can login with new password.");
			alert.show();
		}
	}

	@FXML
	protected void openLoginPane(ActionEvent event) {

		loginPane.setRotationAxis(Rotate.Y_AXIS);
		signupPane.setRotationAxis(Rotate.Y_AXIS);
		at = new AnimationTimer() {
			@Override
			public void handle(long arg0) {
				btnflag = false;
				loginPane.setRotate(loginPane.getRotate() - an);
				if (loginPane.getRotate() <= 0) {
					btnflag = true;
					at.stop();
				}

				signupPane.setRotate(signupPane.getRotate() + an);

				if (signupPane.getRotate() >= 180) {
					btnflag = true;
					at.stop();
				}

				if (loginPane.getRotate() <= 90) {
					signupPane.setVisible(false);
					loginPane.setVisible(true);
				}
			}
		};
		if (btnflag) {
			btnflag = false;
			at.start();
		}

	}

	@FXML
	protected void openSignupPane(ActionEvent event) {

		loginPane.setRotationAxis(Rotate.Y_AXIS);
		signupPane.setRotationAxis(Rotate.Y_AXIS);
		at = new AnimationTimer() {
			@Override
			public void handle(long arg0) {
				btnflag = false;
				loginPane.setRotate(loginPane.getRotate() + an);

				if (loginPane.getRotate() >= 90) {
					signupPane.setVisible(true);
					loginPane.setVisible(false);
				}

				if (loginPane.getRotate() >= 180) {
					btnflag = true;
					at.stop();
				}

				signupPane.setRotate(signupPane.getRotate() - an);

				if (signupPane.getRotate() <= 0) {
					btnflag = true;
					at.stop();
				}
			}
		};
		if (btnflag) {
			btnflag = false;
			at.start();
		}

	}

	@FXML
	protected void exit() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Exit");
		alert.setHeaderText("  Are you sure, you want to quit the app?");
		ButtonType btnYes = new ButtonType("Yes");
		ButtonType btnNo = new ButtonType("No");
		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(btnYes, btnNo);
		Optional<ButtonType> option = alert.showAndWait();
		if (option.get() == btnYes) {
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Exit");
			alert.setHeaderText("  Bye!");
			alert.showAndWait();
			System.exit(0);
		}
	}

	@FXML
	protected void backToHome(Event event) {
		try {
			textValidPassword2.setVisible(false);
			textValidPassword1.setVisible(false);

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
	protected void loginAction(ActionEvent event) {
		if (loginValidation()) {
			
			Session session = ConnectDatabase.getSession();
			SellerAddress sellerAddress = session.get(SellerAddress.class, textEmailLogin.getText().trim());
			SellerBankDetails sellerBankDetails = session.get(SellerBankDetails.class, textEmailLogin.getText().trim());
			
			if(sellerAddress == null || sellerBankDetails == null) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Complete The Regisrtation");
				alert.setHeaderText("Please Complete The Registration Before Start Selling Products.");
				alert.show();
				
				email = textEmailLogin.getText().trim();
				SellerDetailsController.registrationFlag = true;
				try {
					Parent root = FXMLLoader.load(Main.class.getResource("/seller/SellerDetails.fxml"));
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
					textValidPassword2.setVisible(false);
					textValidPassword1.setVisible(false);
					email = textEmailLogin.getText().trim();

					Parent root = FXMLLoader.load(Main.class.getResource("/seller/SellerPage.fxml"));
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
	}

	@FXML
	protected void registerAction(ActionEvent event) {
		if (registrationValidation()) {
			insertRegistrationDetailsInDatabase();
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Complete The Regisrtation");
			alert.setHeaderText("Please Fill Few More Details Before Start Selling Products.");
			alert.show();
			
			email = textEmailR.getText();
			SellerDetailsController.registrationFlag = true;
			try {
				Parent root = FXMLLoader.load(Main.class.getResource("/seller/SellerDetails.fxml"));
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
	protected void showPasswordAction(ActionEvent event) {
		showAndHidePassword(imgShowPassword, btnShowPassword, textHidePassword, textShowPassword);
	}

	@FXML
	protected void passwordToText(KeyEvent event) {
		copyPassword(textHidePassword, textShowPassword);
	}

	@FXML
	protected void textToPassword(KeyEvent event) {
		copyPassword(textShowPassword, textHidePassword);
	}

	@FXML
	protected void showPasswordAction1(ActionEvent event) {
		showAndHidePassword(imgShowPassword1, btnShowPassword1, textHidePassword1, textShowPassword1);
	}

	@FXML
	protected void passwordToText1(KeyEvent event) {
		copyPassword(textHidePassword1, textShowPassword1);
		checkPassword(textHidePassword1.getText(), textValidPassword1);
	}

	@FXML
	protected void textToPassword1(KeyEvent event) {
		copyPassword(textShowPassword1, textHidePassword1);
		checkPassword(textShowPassword1.getText(), textValidPassword1);
	}

	@FXML
	protected void showPasswordAction2(ActionEvent event) {
		showAndHidePassword(imgShowPassword2, btnShowPassword2, textHidePassword2, textShowPassword2);
	}

	@FXML
	protected void passwordToText2(KeyEvent event) {
		copyPassword(textHidePassword2, textShowPassword2);
		checkPassword(textHidePassword2.getText(), textValidPassword2);
	}

	@FXML
	protected void textToPassword2(KeyEvent event) {
		copyPassword(textShowPassword2, textHidePassword2);
		checkPassword(textShowPassword2.getText(), textValidPassword2);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		insertQuestionsInComboBox(comboBoxSecurityQuestionForgotPassword);
		insertQuestionsInComboBox(comboBoxSecurityQuestionRegister);
	}

	protected void showAndHidePassword(ImageView image, ToggleButton button, TextField pwd, TextField text) {
		if (button.isSelected()) {
			image.setImage(new Image("/icons/hidePassword.png"));
			text.setVisible(true);
			pwd.setVisible(false);
		} else {
			image.setImage(new Image("/icons/showPassword.png"));
			pwd.setVisible(true);
			text.setVisible(false);
		}
	}

	protected void copyPassword(TextField from, TextField to) {
		to.setText(from.getText());
	}

	protected void insertQuestionsInComboBox(ComboBox<String> comboBox) {
		comboBox.getItems().removeAll(comboBox.getItems());
		comboBox.getItems().addAll("Select a security question", "Your favorite movie?", "Your favorite food?",
				"Your favourite restaurant?", "What is your favorite sport?",
				"What is the first name of your favorite uncle?", "What is your oldest cousin's name?",
				"Mother's maiden name?", "What is the first name of your favorite aunt?",
				"Where did you spend your childhood summers?", "What is your skin color?",
				"What was the last name of your favorite teacher?", "What was the last name of your childhood friend?",
				"What was your favorite food as a child?", "What was the last name of your first boss?",
				"Where did you meet your spouse?", "What is the name of your first shcool?",
				"What is the name of the hospital you were born?", "What is your main frequent flier number?",
				"What was the model of your first car?", "What was the name of your favorite childhood pet?");
		comboBox.getSelectionModel().select(0);
	}

	protected boolean loginValidation() {
		email = textEmailLogin.getText().trim();
		password = textHidePassword.getText().trim();

		Alert alert = new Alert(AlertType.ERROR);

		if (!emailValidation()) {
			return false;
		}

		if (password.equals("")) {
			alert.setTitle("Password");
			alert.setHeaderText("Empty Password");
			alert.setContentText("Password cannot be empty");
			alert.show();
			return false;
		}

		if (!checkEmailInDatabase() || !password.equals(databasePassword)) {
			alert.setTitle("Error");
			alert.setHeaderText("Wrong email or password.");
			alert.setContentText("Please enter correct email and password.");
			alert.show();
			return false;
		}

		alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Login");
		alert.setHeaderText("Login Successful");
		alert.setContentText("");
		alert.showAndWait();

		return true;
	}

	protected boolean registrationValidation() {
		email = textEmailR.getText().trim();
		firstName = textFirstNameR.getText().trim();
		lastName = textLastNameR.getText().trim();
		contactNumber = textContactNumberR.getText().trim();
		password = textHidePassword2.getText().trim();
//		String passwordShow = textShowPassword2.getText();
		confirmPassword = textConfirmPasswordR.getText().trim();
		question = comboBoxSecurityQuestionRegister.getSelectionModel().getSelectedIndex();
		securityQuestion = comboBoxSecurityQuestionRegister.getSelectionModel().getSelectedItem();
		answer = textAnswerR.getText().trim();

		Alert alert = new Alert(AlertType.ERROR);

		if (firstName.equals("")) {
			alert.setTitle("First Name");
			alert.setHeaderText("Please enter your first name.");
			alert.setContentText("First name cannot be blank.");
			alert.show();
			return false;
		}

		if (lastName.equals("")) {
			alert.setTitle("Last Name");
			alert.setHeaderText("Please enter your last name.");
			alert.setContentText("Last name cannot be blank.");
			alert.show();
			return false;
		}

		if (!emailValidation()) {
			return false;
		}

		if (checkEmailInDatabase()) {
			alert.setTitle("Email");
			alert.setHeaderText("This email already exist");
			alert.setContentText("Please try to login or try with another email");
			alert.show();
			return false;
		}

		if (!checkContactNumber()) {
			return false;
		}

		if (!checkPassword()) {
			return false;
		}

		if (question == 0) {
			alert.setTitle("Security Question");
			alert.setHeaderText("Please select a security question.");
			alert.setContentText("");
			alert.show();
			return false;
		}

		if (answer.equals("")) {
			alert.setTitle("answer");
			alert.setHeaderText("Please enter answer for security question.");
			alert.setContentText("answer cannot be blank.");
			alert.show();
			return false;
		}

		return true;
	}

	protected boolean forgotPasswordValidation() {
		email = textEmailForgot.getText().trim();
		securityQuestion = comboBoxSecurityQuestionForgotPassword.getSelectionModel().getSelectedItem();
		question = comboBoxSecurityQuestionForgotPassword.getSelectionModel().getSelectedIndex();
		answer = textAnswerForgot.getText().trim();

		Alert alert = new Alert(AlertType.ERROR);

		if (!emailValidation()) {
			return false;
		}

		if (!checkEmailInDatabase()) {
			alert.setTitle("Wrong email");
			alert.setHeaderText("This email id does not exists, Please use correct email");
			alert.setContentText("");
			alert.show();
			return false;
		}

		if (question == 0) {
			alert.setTitle("Security Question");
			alert.setHeaderText("Please select a security question.");
			alert.setContentText("");
			alert.show();
			return false;
		}

		if (answer.equals("")) {
			alert.setTitle("answer");
			alert.setHeaderText("Please enter answer for security question.");
			alert.setContentText("answer cannot be blank.");
			alert.show();
			return false;
		}

		if (!(databaseQuestion.equals(securityQuestion) && databaseAnswer.equals(answer))) {
			alert.setTitle("Error");
			alert.setHeaderText("security question or answer do not match.");
			alert.setContentText("Please enter correct security question and answer.");
			alert.show();
			return false;
		}

		return true;
	}

	protected boolean resetPasswordValidation() {
		password = textHidePassword1.getText().trim();
		confirmPassword = textConfirmPassword1.getText().trim();

		if (!checkPassword()) {
			return false;
		}

		return true;
	}

	protected boolean emailValidation() {
		Alert alert = new Alert(AlertType.ERROR);

		if (email.equals("")) {
			alert.setTitle("Email");
			alert.setHeaderText("Email cannot be empty.");
			alert.setContentText("Email is empty.");
			alert.show();
			return false;
		}

		String regex = "^(.+)@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);

		if (!matcher.matches()) {
			alert.setTitle("Email");
			alert.setHeaderText("Invalid Email");
			alert.setContentText("Email id is not valid");
			alert.show();
			return false;
		}

		return true;

	}

	protected boolean checkEmailInDatabase() {
		Session session = ConnectDatabase.getSession();
		SellerLogin seller = session.get(SellerLogin.class, email);
		if (seller != null) {
			databasePassword = seller.getPassword();
			databaseQuestion = seller.getSecurityQuestion();
			databaseAnswer = seller.getAnswer();

			ConnectDatabase.closeSession();
			return true;
		}

		ConnectDatabase.closeSession();
		return false;
	}

	public void checkPassword(String pwd, TextArea passwordError) {
		if (isValidPassword(pwd)) {
			passwordError.setVisible(false);
		} else {
			passwordError.setVisible(true);
		}
	}

	public boolean isValidPassword(String pwd) {
		Pattern p = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$");
		Matcher m = p.matcher(pwd);
		return m.matches();
	}

	protected boolean checkPassword() {
		Alert alert = new Alert(AlertType.ERROR);
		if (password.equals("")) {
			alert.setTitle("Password");
			alert.setHeaderText("Empty Password");
			alert.setContentText("Password cannot be empty");
			alert.show();
			return false;
		}

		if (!isValidPassword(password)) {
			alert.setTitle("Password");
			alert.setHeaderText("Invalid password");
			alert.setContentText("Password is not valid");
			alert.show();
			return false;
		}

		if (confirmPassword.equals("")) {
			alert.setTitle("Confirm Password");
			alert.setHeaderText("Please confirm the password");
			alert.setContentText("Confirm password cannot be empty");
			alert.show();
			return false;
		}

		if (!confirmPassword.equals(password)) {
			alert.setTitle("Confirm Password");
			alert.setHeaderText("Password don't match");
			alert.setContentText("Confirm password do not match to password");
			alert.show();
			return false;
		}

		return true;
	}

	protected boolean checkContactNumber() {
		Alert alert = new Alert(AlertType.ERROR);
		if (contactNumber.equals("")) {
			alert.setTitle("Contact Number");
			alert.setHeaderText("Contact number cannot be blank");
			alert.setContentText("Contact number is empty");
			alert.show();
			return false;
		}

		Pattern p = Pattern.compile("^\\d{10}$");
		Matcher m = p.matcher(contactNumber);
		if (!m.matches()) {
			alert.setTitle("Contact Number");
			alert.setHeaderText("Invalid Contact Number");
			alert.setContentText("Please enter valid contact number");
			alert.show();
			return false;
		}

		return true;
	}

	protected void updatePasswordInDatabase() {
		Session session = ConnectDatabase.getSession();
		SellerLogin seller = session.get(SellerLogin.class, email);
		Transaction transaction = ConnectDatabase.getTransaction(session);
		seller.setPassword(password);
		//session.update(seller);
		transaction.commit();
	}

	protected void insertRegistrationDetailsInDatabase() {
		SellerLogin sellerLogin = new SellerLogin();
		sellerLogin.setEmail(email);
		sellerLogin.setFirstName(firstName);
		sellerLogin.setLastName(lastName);
		sellerLogin.setContactNumber(contactNumber);
		sellerLogin.setPassword(password);
		sellerLogin.setSecurityQuestion(securityQuestion);
		sellerLogin.setAnswer(answer);

		Session session = ConnectDatabase.getSession();
		Transaction transaction = ConnectDatabase.getTransaction(session);

		session.save(sellerLogin);
		transaction.commit();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Registration Successful");
		alert.setHeaderText("You have registered successfully");
		alert.setContentText("Please Fill Some More Deatils To Begin.");
		alert.show();

		ConnectDatabase.closeSession();
	}

}
