package seller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import database.configuration.ConnectDatabase;
import database.tables.ProductCategory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class UpdateCategoryController extends AddCategoryController implements Initializable{

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnDelete;

    @FXML
    private ComboBox<String> comboBoxCategory;

    @FXML
    @Override
	protected void cancelAction(ActionEvent event) {
    	Node  source = (Node)  event.getSource(); 
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    void deleteCategory(ActionEvent event) {
    	if(comboBoxCategory.getSelectionModel().getSelectedItem() == null) {
    		alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Delete Product Category");
    		alert.setHeaderText("Please select category.");
    		alert.show();
    		return;
    	}
    	Session session = ConnectDatabase.getSession();
    	
    	String categoryExist = comboBoxCategory.getSelectionModel().getSelectedItem();
    	@SuppressWarnings("unchecked")
		Query<String> query = (Query<String>)session.createSQLQuery("select category from ProductList where category = :categoryExist");
    	query.setParameter("categoryExist", categoryExist);
    	if(!query.list().isEmpty()) {
    		alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Delete Product Category");
    		alert.setHeaderText("Cannot delete This Category.\nBecause This Category Has Some Products.");
    		alert.show();
    		return;
    	}
    	
    	ProductCategory category = new ProductCategory();
    	category.setCategoryName(comboBoxCategory.getSelectionModel().getSelectedItem());
    	
    	Transaction transaction = ConnectDatabase.getTransaction(session);
    	session.delete(category);
    	transaction.commit();
    	ConnectDatabase.closeSession();
    	
    	alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Delete Product Category");
		alert.setHeaderText("Category deleted successfully.");
		alert.show();
		
		Session session1 = ConnectDatabase.getSession();
		@SuppressWarnings("unchecked")
		List<String> categoryList = session1.createSQLQuery("select * from productcategory").list();
		categoryList.remove("Others");
		comboBoxCategory.getItems().clear();
		comboBoxCategory.setPromptText("Select a category");
		comboBoxCategory.getItems().addAll(categoryList);
		ConnectDatabase.closeSession();
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Session session = ConnectDatabase.getSession();
		@SuppressWarnings("unchecked")
		List<String> categoryList = session.createSQLQuery("select * from productcategory").list();
		categoryList.remove("Others");
		comboBoxCategory.getItems().addAll(categoryList);
		ConnectDatabase.closeSession();
	}

}
