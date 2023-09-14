package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.query.Query;

import database.configuration.ConnectDatabase;
import database.tables.CustomerWishlist;
import database.tables.ProductList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WishlistController implements Initializable{
	public List<ProductDetails> productDetailsList = null;
	public static WishlistController wishlistController = null;

    @FXML 
    TilePane tilePane;

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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		getWishlistProductFromDatabase();
		displayProducts(productDetailsList);
		
		wishlistController = this;
	}
	
	public void displayProducts(List<ProductDetails> product) {
		getWishlistProductFromDatabase();
		try {
			tilePane.getChildren().clear();
			if (product != null) {
				for (int i = 0; i < product.size(); i++) {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("ShowWishlistProduct.fxml"));
					VBox vbox = loader.load();

					ShowWishlistProductController productController = loader.getController();

					productController.setProductData(product.get(i));

					tilePane.getChildren().add(vbox);
					System.out.println("added");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<ProductDetails> getWishlistProductFromDatabase(){
		if(productDetailsList == null) {
			productDetailsList = new ArrayList<>();
			List<CustomerWishlist> wishlist = null;
			ProductDetails product = null;

			Session session = ConnectDatabase.getSession();

			@SuppressWarnings("unchecked")
			Query<CustomerWishlist> query = (Query<CustomerWishlist>) session
					.createQuery("from CustomerWishlist where customerEmail = :email");
			query.setParameter("email", HomePageController.email);
			wishlist = query.list();
			
			for(CustomerWishlist listp : wishlist) {
				ProductList pList = session.get(ProductList.class, listp.getProductId());
				
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
				product.setCategory(pList.getCategory());
				productDetailsList.add(product);
				
			}
		}
		
		return productDetailsList;
	}

}
