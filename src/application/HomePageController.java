package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import org.hibernate.Session;

import database.configuration.ConnectDatabase;
import database.tables.ProductList;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomePageController implements Initializable {
	public static String email = "";
	public static String notExistsImage = "";

	@FXML
	Label lblItemsCount, lblErrorImage;

	@FXML
	private Button btnProfile, btnSearch, btnLoginProfile, btnSignupLogout;

	@FXML
	TextField textSearch;

	@FXML
	private VBox profileVBox;

	@FXML
	private TilePane tilePane;
	
	@FXML
	private Pane paneErrorImage1, paneErrorImage2;

	@FXML
	private ComboBox<String> comboBoxSearchCategory, comboBoxSearchBrand, comboBoxSearchPrice;

	@FXML
	private ImageView imgSignupLogout;

	private List<ProductDetails> addProduct = null;

	public static List<ProductDetails> ls = null;

	@FXML
	void hideProfileOption(MouseEvent event) {
		profileVBox.setVisible(false);
	}

	@FXML
	void showProfileOption(MouseEvent event) {
		if (email.equals("")) {
			btnLoginProfile.setText("Login");
			btnSignupLogout.setText("Signup");
		} else {
			btnLoginProfile.setText("Account");
			btnSignupLogout.setText("Logout");
		}

		profileVBox.setVisible(true);
	}

	@FXML
	void showWishlist(ActionEvent event) {
		if (email.equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Wishlist");
			alert.setHeaderText("Please login to see your wishlist.");
			loginOrShowProfile(event);
			alert.showAndWait();
		}
		// open wish list page
		else {
			try {
				Parent root = FXMLLoader.load(Main.class.getResource("Wishlist.fxml"));
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
	void clearFiltersAction(ActionEvent event) {
		comboBoxSearchCategory.getSelectionModel().clearSelection();
		comboBoxSearchBrand.getSelectionModel().clearSelection();
		comboBoxSearchPrice.getSelectionModel().clearSelection();

		displayProducts(addProduct);

		comboBoxSearchCategory.setPromptText("Search Category");
		comboBoxSearchBrand.setPromptText("Search Brand");
		comboBoxSearchPrice.setPromptText("Search Price");
	}

	@FXML
	void showOrders(ActionEvent event) {
		if (email.equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Orders");
			alert.setHeaderText("Please login to see your orders.");
			loginOrShowProfile(event);
			alert.showAndWait();
		}
		// open orders page
		else {
			try {
				Parent root = FXMLLoader.load(Main.class.getResource("OrdersPage.fxml"));
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
	void openCart(ActionEvent event) {
		if (email.equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Cart");
			alert.setHeaderText("Please login to see your cart.");
			loginOrShowProfile(event);
			alert.showAndWait();
		}
		// open cart page
		else {
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
	}

	@FXML
	void sellerLoginPage(ActionEvent event) {
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

	@FXML
	void signupOrLogout(ActionEvent event) {
		if (email.equals("")) {
			try {
				LoginPageController.signupFlag = true;
				Parent root = FXMLLoader.load(Main.class.getResource("LoginPage.fxml"));
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
		} else {
			// Logout
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Logout");
			alert.setHeaderText("  Are you sure, you want to Logout?");
			ButtonType btnYes = new ButtonType("Yes");
			ButtonType btnNo = new ButtonType("No");
			alert.getButtonTypes().clear();
			alert.getButtonTypes().addAll(btnYes, btnNo);
			Optional<ButtonType> option = alert.showAndWait();
			if (option.get() == btnYes) {
				email = "";
			}
		}
	}

	@FXML
	private void loginOrShowProfile(ActionEvent event) {
		if (email.equals("")) {
			try {
				Parent root = FXMLLoader.load(Main.class.getResource("LoginPage.fxml"));
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
		} else {
			// Show account details
			try {
				Parent root = FXMLLoader.load(Main.class.getResource("CustomerDetails.fxml"));
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
	void exit() {
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
	void applyFiltersAction(ActionEvent event) {
		List<ProductDetails> list = new ArrayList<>();
		List<ProductDetails> list1 = new ArrayList<>();
		List<ProductDetails> list2 = new ArrayList<>();
		if (comboBoxSearchBrand.getSelectionModel().getSelectedItem() != null) {
			String brand = comboBoxSearchBrand.getSelectionModel().getSelectedItem();
			for (int i = 0; i < addProduct.size(); i++) {
				if (addProduct.get(i).getProductBrand().equals(brand)) {
					list.add(addProduct.get(i));
				}
			}
			if (comboBoxSearchCategory.getSelectionModel().getSelectedItem() != null) {
				String category = comboBoxSearchCategory.getSelectionModel().getSelectedItem();
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getCategory().equals(category)) {
						list1.add(list.get(i));
					}
				}
				if(comboBoxSearchPrice.getSelectionModel().getSelectedItem() != null) {
					list2 = addPriceFilteredProducts(list1);
					displayProducts(list2);
					return;
				}
				displayProducts(list1);
				return;
			}
			if(comboBoxSearchPrice.getSelectionModel().getSelectedItem() != null) {
				list2 = addPriceFilteredProducts(list);
				displayProducts(list2);
				return;
			}
			displayProducts(list);
			return;
		} else if (comboBoxSearchCategory.getSelectionModel().getSelectedItem() != null) {
			String category = comboBoxSearchCategory.getSelectionModel().getSelectedItem();
			for (int i = 0; i < addProduct.size(); i++) {
				if (addProduct.get(i).getCategory().equals(category)) {
					list1.add(addProduct.get(i));
				}
			}

			displayProducts(list1);
			return;
		}
		else if(comboBoxSearchPrice.getSelectionModel().getSelectedItem() != null) {
			list2 = addPriceFilteredProducts(addProduct);
			displayProducts(list2);
			return;
		}
		else {
			displayProducts(addProduct);
		}
	}

	@FXML
	private void searchProducts(ActionEvent event) {
		addProduct = new ArrayList<>(addProducts());
		if (textSearch.getText().equals("")) {
			addProduct = ls;
			displayProducts(addProduct);
			return;
		}
		String[] str = textSearch.getText().toLowerCase().split(" ");
		List<ProductDetails> addSearchProduct = new ArrayList<>();
		for (ProductDetails p : addProduct) {
			for (int j = 0; j < str.length; j++) {
				if ((p.getProductHeading().toLowerCase().contains(str[j]))) {
					if (!addSearchProduct.contains(p)) {
						addSearchProduct.add(p);
					}
				}
			}
		}
		addProduct = addSearchProduct;

		displayProducts(addProduct);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ProductCardController.selectFlag = false;
		addProduct = new ArrayList<>(addProducts());
		displayProducts(addProduct);

		Set<String> brands = new HashSet<>();
		for (ProductDetails productDetails : addProduct) {
			brands.add(productDetails.getProductBrand());
		}

		comboBoxSearchBrand.getItems().clear();
		comboBoxSearchBrand.setPromptText("Select Brand");
		comboBoxSearchBrand.getItems().addAll(brands);

		Session session1 = ConnectDatabase.getSession();
		@SuppressWarnings("unchecked")
		List<String> categoryList = session1.createSQLQuery("select * from productcategory").list();
		comboBoxSearchCategory.getItems().clear();
		comboBoxSearchCategory.setPromptText("Select category");
		comboBoxSearchCategory.getItems().addAll(categoryList);
		ConnectDatabase.closeSession();

		addPriceToComboBoxSearchPrice();

		if (email.equals("")) {
			imgSignupLogout.setImage(new Image("/icons/signup.png"));
		} else {
			imgSignupLogout.setImage(new Image("/icons/logout.png"));
		}
		
		if(ProductCardController.imageNotFoundFlag) {
			lblErrorImage.setText("Please add an image in 'ProductImage' folder with name " + notExistsImage.substring(notExistsImage.lastIndexOf('/')+1));
			paneErrorImage1.setVisible(true);
			paneErrorImage2.setVisible(true);
			tilePane.getChildren().clear();
		}
	}

	public List<ProductDetails> addPriceFilteredProducts(List<ProductDetails> productDetailsList) {
		if (comboBoxSearchPrice.getSelectionModel().getSelectedItem() != null) {
			String limits = comboBoxSearchPrice.getSelectionModel().getSelectedItem();
			int[] limit = getPriceLimit(limits);
			int lower = limit[0];
			int upper = limit[1];
			List<ProductDetails> list = new ArrayList<ProductDetails>();
			for (ProductDetails productDetails : productDetailsList) {
				if (productDetails.getNewPrice() >= lower && productDetails.getNewPrice() <= upper) {
					list.add(productDetails);
				}
			}
			return list;
		}
		return productDetailsList;
	}

	public void addPriceToComboBoxSearchPrice() {
		String[] str = { "Under 500", "500 - 1000", "1000 - 5000", "5000 - 10000", "10000 - 15000", "15000 - 20000",
				"20000 - 30000", "40000 - 50000", "50000 - 70000", "70000 - 100000", "100000 - 150000",
				"150000 - 300000", "Over 300000" };
		List<String> prices = Arrays.asList(str);
		comboBoxSearchPrice.getItems().clear();
		comboBoxSearchPrice.setPromptText("Search Price");
		comboBoxSearchPrice.getItems().addAll(prices);
	}

	public int[] getPriceLimit(String str) {
		int[] arr = new int[2];
		if (str.equals("Under 500")) {
			arr[0] = 0;
			arr[1] = 500;
			return arr;
		}
		if (str.equals("500 - 1000")) {
			arr[0] = 500;
			arr[1] = 1000;
			return arr;
		}
		if (str.equals("1000 - 5000")) {
			arr[0] = 1000;
			arr[1] = 5000;
			return arr;
		}
		if (str.equals("5000 - 10000")) {
			arr[0] = 5000;
			arr[1] = 10000;
			return arr;
		}
		if (str.equals("10000 - 15000")) {
			arr[0] = 10000;
			arr[1] = 15000;
			return arr;
		}
		if (str.equals("15000 - 20000")) {
			arr[0] = 15000;
			arr[1] = 20000;
			return arr;
		}
		if (str.equals("20000 - 30000")) {
			arr[0] = 20000;
			arr[1] = 30000;
			return arr;
		}
		if (str.equals("40000 - 50000")) {
			arr[0] = 40000;
			arr[1] = 50000;
			return arr;
		}
		if (str.equals("50000 - 70000")) {
			arr[0] = 50000;
			arr[1] = 70000;
			return arr;
		}
		if (str.equals("70000 - 100000")) {
			arr[0] = 70000;
			arr[1] = 100000;
			return arr;
		}
		if (str.equals("100000 - 150000")) {
			arr[0] = 100000;
			arr[1] = 150000;
			return arr;
		}
		if (str.equals("150000 - 300000")) {
			arr[0] = 150000;
			arr[1] = 300000;
			return arr;
		}
		if (str.equals("Over 300000")) {
			arr[0] = 300000;
			arr[1] = Integer.MAX_VALUE;
			return arr;
		}
		return arr;
	}

	private void displayProducts(List<ProductDetails> product) {
		int count = 0;
		try {
			tilePane.getChildren().clear();
			for (int i = 0; i < product.size(); i++) {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("ProductCard.fxml"));
				HBox hbox = loader.load();

				ProductCardController productController = loader.getController();

				productController.setProductData(product.get(i));

				tilePane.getChildren().add(hbox);

				count++;
			}
			lblItemsCount.setText(((Integer) count).toString() + " Items");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<ProductDetails> addProducts() {
		if (ls == null) {
			ls = new ArrayList<>();
			ProductDetails product = null;

			Session session = ConnectDatabase.getSession();

			@SuppressWarnings("unchecked")
			List<ProductList> productList = session.createQuery("from ProductList").list();

			for (int i = 0; i < productList.size(); i++) {
				ProductList list = productList.get(i);

				product = new ProductDetails();

				product.setId(list.getProductId());
				
				product.setImageSrc("/ProductImages/img" + list.getProductId() + list.getImageExtension());
				
				product.setProductHeading(list.getTitle());
				product.setNewPrice(Double.parseDouble(list.getNewPrice()));
				product.setOldPrice(Double.parseDouble(list.getOldPrice()));
				product.setDeliveryCharges(Double.parseDouble(list.getDeliveryCharge()));
				product.setProductBrand(list.getBrand());
				product.setRating(list.getRating());
				product.setDescription(list.getDescription());
				product.setQuantity(((Integer) list.getQuantity()).toString());
				product.setCategory(list.getCategory());
				ls.add(product);
			}
		}

		return ls;
	}
	
}
