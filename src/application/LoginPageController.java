package application;

import java.net.URL;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.Transaction;

import database.configuration.ConnectDatabase;
import database.tables.CustomerLogin;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import seller.SellerLoginPageController;

public class LoginPageController extends SellerLoginPageController {

	public static String loginEmail;

	public static boolean signupFlag;

	@FXML
	private Button btnBack, btnBack1, btnForgot, btnLogin, btnLoginPane, btnRegister, btnSignup, btnlogin2;

	@FXML
	private ImageView btnHome, imgShowPassword, imgShowPassword1, imgShowPassword2;

	@FXML
	private ToggleButton btnShowPassword, btnShowPassword1, btnShowPassword2;

	@FXML
	private ComboBox<String> comboBoxSecurityQuestionRegister, comboBoxSecurityQuestionForgotPassword;

	@FXML
	private Pane loginPane, paneForgotPassword, paneResetPassword, signupPane;

	@FXML
	private TextField textEmailForgot, textAnswerForgot;

	@FXML
	private TextField textEmailR, textFirstNameR, textLastNameR, textContactNumberR, textConfirmPasswordR, textAnswerR;

	@FXML
	private TextField textConfirmPassword1;

	@FXML
	private TextField textEmailLogin;

	@FXML
	private TextField textHidePassword, textShowPassword, textHidePassword1, textShowPassword1, textHidePassword2,
			textShowPassword2;

	@FXML
	private TextArea textValidPassword1, textValidPassword2;

	@Override
	@FXML
	protected void loginAction(ActionEvent event) {
		if (loginValidation()) {
			try {
				loginEmail = SellerLoginPageController.email;
				HomePageController.email = loginEmail;
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
	}

	@Override
	protected boolean checkEmailInDatabase() {
		// SellerLoginPageController.email = email;
		Session session = ConnectDatabase.getSession();
		CustomerLogin customer = session.get(CustomerLogin.class, email);
		if (customer != null) {
			databasePassword = customer.getPassword();
			databaseQuestion = customer.getSecurityQuestion();
			databaseAnswer = customer.getAnswer();

			ConnectDatabase.closeSession();
			return true;
		}

		ConnectDatabase.closeSession();
		return false;
	}

	@Override
	protected void updatePasswordInDatabase() {
		// SellerLoginPageController.email = email;
		Session session = ConnectDatabase.getSession();
		CustomerLogin customer = session.get(CustomerLogin.class, email);
		Transaction transaction = ConnectDatabase.getTransaction(session);
		customer.setPassword(password);
		session.update(customer);
		transaction.commit();
	}

	@FXML
	protected void registerAction(ActionEvent event) {
		if (registrationValidation()) {
			// SellerLoginPageController.email = email;
			CustomerLogin customer = new CustomerLogin();
			customer.setEmail(email);
			customer.setFirstName(firstName);
			customer.setLastName(lastName);
			customer.setContactNumber(contactNumber);
			customer.setPassword(password);
			customer.setSecurityQuestion(securityQuestion);
			customer.setAnswer(answer);

			Session session = ConnectDatabase.getSession();
			Transaction transaction = ConnectDatabase.getTransaction(session);

			session.save(customer);
			transaction.commit();

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Registration Successful");
			alert.setHeaderText("You have registered successfully");
			alert.setContentText("Please login to continue");
			alert.show();

			ConnectDatabase.closeSession();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		super.initialize(arg0, arg1);

		if (signupFlag) {
			signupFlag = false;
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
	}

}