package seller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.hibernate.Session;

import application.Main;
import application.ProductDetails;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SellerPageController implements Initializable {
	public static String email = "";

	@FXML
	private Button btnProfile, btnSearch;

	@FXML
	TextField textSearch;

	@FXML
	TilePane tilePane;

	private List<ProductDetails> addProduct;

	@FXML
	void logout(ActionEvent event) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText("  Are you sure, you want to Logout?");
		ButtonType btnYes = new ButtonType("Yes");
		ButtonType btnNo = new ButtonType("No");
		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(btnYes, btnNo);
		Optional<ButtonType> option = alert.showAndWait();
		if (option.get() == btnYes) {
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
	private void searchProducts(ActionEvent event) {
		addProduct = new ArrayList<>(addProducts());
		if (textSearch.getText().equals("")) {
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

		displayProducts(addSearchProduct);
	}

	@FXML
	void addProductAction(ActionEvent event) {
		try {
			Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Parent root = FXMLLoader.load(Main.class.getResource("@../../../seller/AddProduct.fxml"));
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/ButtonStyle.css").toExternalForm());
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(primaryStage);
			stage.setResizable(false);
			stage.setTitle("Add Product");
			// stage.getIcons().add(this.productImage.getImage());
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void addCategoryAction(ActionEvent event) {
		try {
			Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Parent root = FXMLLoader.load(Main.class.getResource("@../../../seller/AddCategory.fxml"));
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/ButtonStyle.css").toExternalForm());
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(primaryStage);
			stage.setResizable(false);
			stage.setTitle("Add Product");
			// stage.getIcons().add(this.productImage.getImage());
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void deleteCategoryAction(ActionEvent event) {
		try {
			Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Parent root = FXMLLoader.load(Main.class.getResource("@../../../seller/UpdateCategory.fxml"));
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/ButtonStyle.css").toExternalForm());
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(primaryStage);
			stage.setResizable(false);
			stage.setTitle("Add Product");
			// stage.getIcons().add(this.productImage.getImage());
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void ViewOrdersAction(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(Main.class.getResource("@../../../seller/ViewOrders.fxml"));
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
	void showSellingHistoryAction(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(Main.class.getResource("@../../../seller/SellingHistory.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setMaximized(true);
			stage.setResizable(false);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void showReturnedProductsAction(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(Main.class.getResource("@../../../seller/ShowReturnedProducts.fxml"));
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
	void showSellerDetailsAction(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(Main.class.getResource("@../../../seller/SellerDetails.fxml"));
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		email = SellerLoginPageController.email;
		ProductCardController.selectFlag = true;
		addProduct = new ArrayList<>(addProducts());
		displayProducts(addProduct);
	}

	private void displayProducts(List<ProductDetails> product) {
		try {
			tilePane.getChildren().clear();
			for (int i = 0; i < product.size(); i++) {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("ProductCard.fxml"));
				HBox hbox = loader.load();

				ProductCardController productController = loader.getController();

				productController.setProductData(product.get(i));

				tilePane.getChildren().add(hbox);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<ProductDetails> addProducts() {
		List<ProductDetails> ls = new ArrayList<>();
		ProductDetails product = null;

		Session session = ConnectDatabase.getSession();

		@SuppressWarnings("unchecked")
		List<ProductList> productList = (List<ProductList>) session.createQuery("from ProductList").list();

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

		return ls;
	}

}
