package seller;

import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.query.Query;

import application.Main;
import database.configuration.ConnectDatabase;
import database.tables.Chart;
import database.tables.CustomerOrders;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SellingHistoryController implements Initializable {
	List<CustomerOrders> customerOrders = null;
	private List<Chart> chartList = null;
	private LocalDate date = LocalDate.now();
	private Month month = date.getMonth();
	private int year = date.getYear();

	@FXML
	FlowPane flowPane;

	@FXML
	Pane chartPane;

	@FXML
	ImageView imgBack;

	@FXML
	LineChart<String, Double> areaChart;

	@FXML
	ComboBox<Integer> comboBoxYear;

	@FXML
	ComboBox<Month> comboBoxMonth;

	@FXML
	void back(MouseEvent event) {
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
	
	@FXML
	void btnViewChartAction(ActionEvent event) {
		getPrices();
		addDetailsToAreaChart();
		addDetailsToAreaChart();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//areaChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");
		getDetailsFromDatabase();
		displayProducts(customerOrders);
		insertMonthAndYearInComboBox();
		getPrices();
		addDetailsToAreaChart();
	}

	void getDetailsFromDatabase() {
		customerOrders = new ArrayList<CustomerOrders>();
		Session session = ConnectDatabase.getSession();
		@SuppressWarnings("unchecked")
		Query<CustomerOrders> query = (Query<CustomerOrders>) session
				.createQuery("from CustomerOrders where isDelivered = 1");
		customerOrders.addAll(query.list());
	}

	void displayProducts(List<CustomerOrders> orders) {
		if (customerOrders == null) {
			getDetailsFromDatabase();
		}

		try {
			flowPane.getChildren().clear();
			flowPane.getChildren().add(chartPane);
			for (CustomerOrders custOrders : orders) {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("ShowOrdersHistory.fxml"));
				HBox hbox = loader.load();

				ShowOrdersHistoryController controller = loader.getController();

				controller.setProductData(custOrders);

				flowPane.getChildren().add(hbox);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void addDetailsToAreaChart() {
		Series<String, Double> series = new Series<String, Double>();
		for (Object ch : chartList) {
			series.getData().add(new Data<String, Double>(((Chart) ch).getDate().toString(), ((Chart) ch).getTotal()));
		}
		areaChart.getData().clear();
		areaChart.getData().add(series);
	}

	@SuppressWarnings("unchecked")
	private void getPrices() {
		int m = comboBoxMonth.getSelectionModel().getSelectedIndex()+1;
		int y = comboBoxYear.getSelectionModel().getSelectedItem();
		int days = 30;
		if (m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10 || m == 12) {
			days = 31;
		} else if (m == 2) {
			days = 28;
			if (y % 400 == 0 || (y % 4 == 0 && y % 100 != 0)) {
				days = 29;
			}
		}
		java.util.Date d1 = null, d2 = null;
		try {
			d1 = (java.util.Date) new SimpleDateFormat("yyyy-MM-dd").parse(y + "-" + m + "-1");
			d2 = (java.util.Date) new SimpleDateFormat("yyyy-MM-dd").parse(y + "-" + m + "-" + days);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Session session = ConnectDatabase.getSession();

		Query<Object[]> query = (Query<Object[]>) session.createQuery(
				"select c.orderDate, sum(p.newPrice * c.productQuantity) from ProductList as p inner join CustomerOrders as c on p.productId = c.productId where (c.orderDate between :d1 and :d2) and c.isDelivered = 1 group by c.orderDate");
		query.setParameter("d1", d1);
		query.setParameter("d2", d2);
		List<Object[]> object = query.list();
		chartList = new ArrayList<>();
		for (Object[] obj : object) {
			Chart ch = new Chart();
			ch.setDate(Date.valueOf(obj[0].toString()));
			ch.setTotal(Double.valueOf(obj[1].toString()));
			chartList.add(ch);
		}
	}

	private void insertMonthAndYearInComboBox() {
		comboBoxYear.getItems().clear();
		comboBoxMonth.getItems().clear();
		for (int i = 2020; i <= this.year; i++) {
			comboBoxYear.getItems().add(i);
		}
		comboBoxYear.getSelectionModel().select(Integer.valueOf(this.year));

		comboBoxMonth.getItems().addAll(Month.JANUARY, Month.FEBRUARY, Month.MARCH, Month.APRIL, Month.MAY, Month.JUNE,
				Month.JULY, Month.AUGUST, Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER);

		comboBoxMonth.getSelectionModel().select(this.month);
	}

}
