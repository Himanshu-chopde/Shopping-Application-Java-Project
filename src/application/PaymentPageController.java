package application;

import java.net.URL;
import java.sql.Date;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import database.configuration.ConnectDatabase;
import database.tables.CustomerCart;
import database.tables.CustomerOrders;
import database.tables.ProductList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PaymentPageController implements Initializable {
	private ToggleGroup radioGroup = new ToggleGroup();
	public List<ProductDetails> productDetailsList = null;
	List<CustomerCart> cartList;
	static double totalAmount = 0;
	private int currYear, currMonth;
	private List<Integer> orderCountId = new ArrayList<Integer>();

	@FXML
	private Label lblAmount, lblTimer;

	@FXML
	private Pane paneCard, paneCod, paneUPI, orderPane;

	@FXML
	private RadioButton radioCOD, radioCard, radioUPI;

	@FXML
	private Button btnCancelOrder;

	@FXML
	private TextField textCardCvv, textCardName, textCardNumber, textUPI;

	@FXML
	Spinner<Integer> spinnerMonth, spinnerYear;

	@FXML
	private TilePane tilePane;

	@FXML
	void back(MouseEvent event) {
		try {
			Parent root = FXMLLoader.load(Main.class.getResource("Cart.fxml"));
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
	void showCODPane(MouseEvent event) {
		paneCod.setVisible(true);
	}

	@FXML
	void showCardPane(MouseEvent event) {
		paneCard.setVisible(true);
	}

	@FXML
	void showUPIPane(MouseEvent event) {
		paneUPI.setVisible(true);
	}

	@FXML
	void changePaymentMethod(ActionEvent event) {
		paneCod.setVisible(false);
		paneCard.setVisible(false);
		paneUPI.setVisible(false);

		radioCOD.selectedProperty().set(false);
		radioCard.selectedProperty().set(false);
		radioUPI.selectedProperty().set(false);
	}

	@FXML
	void payAmountUPI(ActionEvent event) {
		if (upiValidation()) {
			insertOrderDetailsInDatabase();
			orderPane.setVisible(true);
			startTimer();
		}
	}

	private boolean upiValidation() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("UPI Error");
		Pattern p = Pattern.compile("[a-zA-Z0-9\\.\\-]{2,256}\\@[a-zA-Z][a-zA-Z]{2,64}");
		Matcher m = p.matcher(textUPI.getText().trim());

		if (textUPI.getText().trim().equals("")) {
			alert.setHeaderText("Please Enter UPI Id");
			alert.show();
			return false;
		}

		if (!m.matches()) {
			alert.setHeaderText("Please Enter Valid UPI Id");
			alert.show();
			return false;
		}
		return true;
	}

	@FXML
	void payAmountCard(ActionEvent event) {
		if (cardValidation()) {
			insertOrderDetailsInDatabase();
			orderPane.setVisible(true);
			startTimer();
		}
	}

	int t = 30;

	void startTimer() {
		SimpleIntegerProperty i = new SimpleIntegerProperty(0);
		Timeline timeline = new Timeline();
		t = 30;
		KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), e -> {
			if (i.get() > 30) {
				timeline.stop();
				lblTimer.setVisible(false);
				btnCancelOrder.setVisible(false);
			} else {
				lblTimer.setText(((Integer) t).toString());
				t--;
				i.set(i.get() + 1);
			}
		});
		timeline.getKeyFrames().add(keyFrame);
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	@FXML
	void placeOrderAction(ActionEvent event) {
		insertOrderDetailsInDatabase();
		orderPane.setVisible(true);
		startTimer();
	}

	@FXML
	void cancelOrderAction(ActionEvent event) {
		cancelOrderUpdateInDatabase();
		orderPane.setVisible(false);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		OffsetDateTime offsetDateTime = OffsetDateTime.now();
		currYear = offsetDateTime.getYear();
		currMonth = offsetDateTime.getMonthValue();
		IntegerSpinnerValueFactory spinnerYearFactory = new IntegerSpinnerValueFactory(currYear, currYear + 6);
		spinnerYear.setValueFactory(spinnerYearFactory);

		IntegerSpinnerValueFactory spinnerMonthFactory = new IntegerSpinnerValueFactory(1, 12);
		spinnerMonth.setValueFactory(spinnerMonthFactory);

		radioUPI.setToggleGroup(radioGroup);
		radioCard.setToggleGroup(radioGroup);
		radioCOD.setToggleGroup(radioGroup);

		getCartProdoductFromDatabase();
		displayProducts(productDetailsList);

		lblAmount.setText(((Double) totalAmount).toString());
		
		totalAmount = 0.0;
	}

	public void displayProducts(List<ProductDetails> product) {
		getCartProdoductFromDatabase();
		try {
			tilePane.getChildren().clear();
			if (product != null) {
				for (int i = 0; i < product.size(); i++) {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("ShowOrderProduct.fxml"));
					HBox hbox = loader.load();

					ShowOrderProductController productController = loader.getController();

					productController.setProductData(product.get(i));

					tilePane.getChildren().add(hbox);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<ProductDetails> getCartProdoductFromDatabase() {
		if (productDetailsList == null) {
			productDetailsList = new ArrayList<>();
			cartList = null;
			ProductDetails product = null;

			Session session = ConnectDatabase.getSession();

			@SuppressWarnings("unchecked")
			Query<CustomerCart> query = (Query<CustomerCart>) session
					.createQuery("from CustomerCart where customerEmail = :email and productQuantity > 0");
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

				totalAmount += Double.parseDouble(pList.getNewPrice()) * cartp.getProductQuantity();
				totalAmount += Double.parseDouble(pList.getDeliveryCharge());
			}
		}
		return productDetailsList;
	}

	private void insertOrderDetailsInDatabase() {
		Session session = ConnectDatabase.getSession();

		CustomerOrders customerOrders = null;
		for (ProductDetails productDetails : productDetailsList) {
			customerOrders = new CustomerOrders();
			customerOrders.setEmail(HomePageController.email);
			customerOrders.setProductId(productDetails.getId());
			customerOrders.setProductQuantity(Integer.parseInt(productDetails.getQuantity()));
			long millis = System.currentTimeMillis();
			customerOrders.setOrderDate(new Date(millis));
			Transaction transaction = ConnectDatabase.getTransaction(session);
			session.save(customerOrders);
			transaction.commit();
			deleteCartDetails(productDetails.getId());

			@SuppressWarnings("unchecked")
			Query<Integer> query = (Query<Integer>)session.createSQLQuery(
					"select orderId from CustomerOrders where email = :email and productId = :productId and productQuantity = :productQuantity and isDelivered = :isDelivered and orderDate = :orderDate");
			query.setParameter("email", HomePageController.email);
			query.setParameter("productId", productDetails.getId());
			query.setParameter("productQuantity", productDetails.getQuantity());
			query.setParameter("isDelivered", 0);
			query.setParameter("orderDate", new Date(millis));
			orderCountId.addAll(query.list());
		}
	}

	private void deleteCartDetails(String productId) {
		CustomerCart customerCart = new CustomerCart();
		customerCart.setCustomerEmail(HomePageController.email);
		customerCart.setProductId(productId);
		Session session = ConnectDatabase.getSession();
		Transaction transaction = ConnectDatabase.getTransaction(session);
		session.delete(customerCart);
		transaction.commit();
	}

	private void cancelOrderUpdateInDatabase() {
		Session session = ConnectDatabase.getSession();
		Session session2 = ConnectDatabase.getSession();
		Session session3 = ConnectDatabase.getSession();
		Transaction transaction = ConnectDatabase.getTransaction(session);
		Transaction transaction2 = ConnectDatabase.getTransaction(session2);
		for (int id : orderCountId) {
			CustomerOrders customerOrders = new CustomerOrders();
			CustomerCart customerCart = new CustomerCart();
			customerOrders.setOrderId(id);
			customerOrders = session3.get(CustomerOrders.class, id);
			customerCart.setCustomerEmail(customerOrders.getEmail());
			customerCart.setProductId(customerOrders.getProductId());
			customerCart.setProductQuantity(customerOrders.getProductQuantity());
			session2.save(customerCart);
			transaction2.commit();
			session.delete(customerOrders);
			transaction.commit();
		}
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Order Cancellation");
		alert.setHeaderText("Your Order Is Cancelled.");
		alert.show();
	}

	private boolean cardValidation() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Card Error");
		if (textCardNumber.getText().trim().isBlank() || !(textCardNumber.getText().trim().length() >= 12
				&& textCardNumber.getText().trim().length() <= 19)) {
			alert.setHeaderText("Please Enter valid Card Number");
			alert.show();
			return false;
		}
		try {
			Long.parseLong(textCardNumber.getText().trim());
		} catch (Exception e) {
			alert.setHeaderText("Please Enter Valid Card Number.");
			alert.show();
			return false;
		}

		if (textCardName.getText().isBlank() || textCardName.getText().trim().equals("")) {
			alert.setHeaderText("Please Enter Name As Per Your Card.");
			alert.show();
			return false;
		}

		if (textCardCvv.getText().trim().length() != 3) {
			alert.setHeaderText("Please Enter Valid CVV.");
			alert.show();
			return false;
		}
		try {
			Integer.parseInt(textCardCvv.getText().trim());
		} catch (Exception e) {
			alert.setHeaderText("Please Enter Valid CVV.");
			alert.show();
			return false;
		}

		if (spinnerYear.getValue() <= currYear && spinnerMonth.getValue() < currMonth) {
			alert.setHeaderText("Your Card Has Been Expired. \nOr You have Entered Wrong Expiration Year or Month.");
			alert.show();
			return false;
		}

		return true;
	}
}
