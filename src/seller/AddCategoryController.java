package seller;

import org.hibernate.Session;
import org.hibernate.Transaction;

import database.configuration.ConnectDatabase;
import database.tables.ProductCategory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCategoryController {
	Alert alert;
	
    @FXML
    private Button btnAdd;

    @FXML
    protected Button btnCancel;

    @FXML
    private TextField textCategory;

    @FXML
    private void addCategory(ActionEvent event) {
    	if(categoryNameValidation() && checkDuplicateCategory()) {
    		ProductCategory category = new ProductCategory();
    		category.setCategoryName(textCategory.getText());
    		
    		Session session = ConnectDatabase.getSession();
    		Transaction transaction = ConnectDatabase.getTransaction(session);
    		session.save(category);
    		transaction.commit();
    		
    		alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Category");
    		alert.setHeaderText("Product category successfully added.");
    		alert.show();
    	}
    }
    
    protected boolean checkDuplicateCategory() {
    	if(checkCategoryInDatabase(textCategory.getText()) != null) {
    		alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Duplicate Category");
    		alert.setHeaderText("This product category already exists in category list.");
    		alert.show();
    		
    		return false;
    	}
    	return true;
    }
    
    protected ProductCategory checkCategoryInDatabase(String categoryName) {
    	ProductCategory category = new ProductCategory();
    	Session session = ConnectDatabase.getSession();
    	category = session.get(ProductCategory.class, categoryName);
    	ConnectDatabase.closeSession();
    	return category;
    }
    
    protected boolean categoryNameValidation() {
    	if(textCategory.getText().equals("")) {
    		alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Empty Text");
    		alert.setHeaderText("Please enter category.");
    		alert.show();
    		return false;
    	}
    	return true;
    }

    @FXML
    protected void cancelAction(ActionEvent event) {
    	Node  source = (Node)  event.getSource(); 
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

}
