package com.example.pos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;




public class mainFormController implements Initializable {
    @FXML
    private Button dashboard_btn;

    @FXML
    private Button inventory_addBtn;

    @FXML
    private Button inventory_btn;

    @FXML
    private Button inventory_clearBtn;

    @FXML
    private TableColumn<productData, String> inventory_col_category;

    @FXML
    private TableColumn<productData, String> inventory_col_detail;

    @FXML
    private TableColumn<productData, String> inventory_col_location;

    @FXML
    private TableColumn<productData, String> inventory_col_price;

    @FXML
    private TableColumn<productData, String> inventory_col_productID;

    @FXML
    private TableColumn<productData, String> inventory_col_productName;

    @FXML
    private TableColumn<productData, String> inventory_col_stock;

    @FXML
    private TableColumn<productData, String> inventory_col_status;

    @FXML
    private TableColumn<productData, String> inventory_col_date;

    @FXML
    private Button inventory_deleteBtn;

    @FXML
    private AnchorPane inventory_form;

    @FXML
    private TextField inventory_price;

    @FXML
    private TextField inventory_productID;

    @FXML
    private TextField inventory_productName;

    @FXML
    private TextField inventory_detail;

    @FXML
    private TextField inventory_stock;

    @FXML
    private ImageView inventory_imageView;

    @FXML
    private Button inventory_importBtn;

    @FXML
    private TableView<productData> inventory_tableView;

    @FXML
    private Button inventory_updateBtn;

    @FXML
    private Button logout_btn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button selling_btn;

    @FXML
    private Label username;

    private Alert alert;

    @FXML
    private ComboBox<?> inventory_category;

    @FXML
    private ComboBox<?> inventory_location;

    @FXML
    private ComboBox<?> inventory_status;

    @FXML
    private TextField menu_amount;

    @FXML
    private Label menu_change;

    @FXML
    private Button customer_btn;

    @FXML
    private AnchorPane customer_form;

    @FXML
    private TableView<customersData> customers_tableView;

    @FXML
    private TableColumn<customersData, String> customers_col_cashier;

    @FXML
    private TableColumn<customersData, String> customers_col_customerID;

    @FXML
    private TableColumn<customersData, String> customers_col_date;

    @FXML
    private TableColumn<customersData, String> customers_col_total;


    @FXML
    private TableColumn<productData, String> menu_col_price;

    @FXML
    private TableColumn<productData, String> menu_col_productName;

    @FXML
    private TableColumn<productData, String> menu_col_quantity;

    @FXML
    private GridPane menu_gridPane;

    @FXML
    private Button menu_payBtn;

    @FXML
    private Button menu_receiptBtn;

    @FXML
    private Button menu_removeBtn;

    @FXML
    private ScrollPane menu_scrollPane;

    @FXML
    private TableView<productData> menu_tableView;

    @FXML
    private Label menu_total;

    @FXML
    private AnchorPane sale_form;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private BarChart<?, ?> dashboard_CustomerChart;

    @FXML
    private AreaChart<?, ?> dashboard_incomeChart;

    @FXML
    private Label dashboard_NC;

    @FXML
    private Label dashboard_TI;

    @FXML
    private Label dsahboard_TotalI;


    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    private Image image;

    private ObservableList<productData> cardListData = FXCollections.observableArrayList();

    public void dashboardDisplayNC(){

        String sql = "SELECT COUNT(id) FROM receipt";

        connect = database.connectionDb();

        try {

            int nc = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()){
                nc = result.getInt("COUNT(id)");
            }
            dashboard_NC.setText(String.valueOf(nc));
        }catch (Exception e){e.printStackTrace();}
    }

    public void dashboardTotalI(){
        String sql = "SELECT SUM(total) FROM receipt";

        connect = database.connectionDb();

        try {
            float ti = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()){
                ti = result.getFloat("SUM(total)");
            }
            dsahboard_TotalI.setText("฿" + ti);
        }catch (Exception e){e.printStackTrace();}
    }


    public void dashboardDisplayTI() {
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "SELECT SUM(total) FROM receipt WHERE date = '"
                + sqlDate + "'";

        connect = database.connectionDb();

        double ti = 0;
        try {
            ti = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            if (result.next()) {
                ti = result.getDouble("SUM(total)");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        dashboard_TI.setText("฿" + ti);
    }

    public void dashboardIncomeChart(){
        dashboard_incomeChart.getData().clear();
        String sql = "SELECT date, SUM(total) FROM receipt GROUP BY date ORDER BY TIMESTAMP(date)";
        connect = database.connectionDb();
        XYChart.Series chart = new XYChart.Series();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                chart.getData().add(new XYChart.Data<>(result.getString(1), result.getFloat(2)));
            }
            dashboard_incomeChart.getData().add(chart);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void dashboardCustomerChart(){
        dashboard_CustomerChart.getData().clear();
        String sql = "SELECT date, COUNT(id) FROM receipt GROUP BY date ORDER BY TIMESTAMP(date)";
        connect = database.connectionDb();
        XYChart.Series chart = new XYChart.Series<>();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                chart.getData().add(new XYChart.Data<>(result.getString(1), result.getInt(2)));
            }
            dashboard_CustomerChart.getData().add(chart);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public void setInventoryAddBtn(){

        if(inventory_productID.getText().isEmpty()
                || inventory_productName.getText().isEmpty()
                || inventory_detail.getText().isEmpty()
                || inventory_stock.getText().isEmpty()
                || inventory_price.getText().isEmpty()
                || inventory_category.getSelectionModel().getSelectedItem() == null
                || inventory_location.getSelectionModel().getSelectedItem() == null
                || inventory_status.getSelectionModel().getSelectedItem() == null
                || data.path == null){

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please  fill all blank fields");
            alert.showAndWait();

        }else {
            // Check Product ID
            String checkProdID = "SELECT product_id FROM product WHERE product_id = '"
                    + inventory_productID.getText() + "'";

            connect = database.connectionDb();

            try{

                statement = connect.createStatement();
                result = statement.executeQuery(checkProdID);

                if(result.next()){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText( inventory_productID.getText() + "is already taken");
                    alert.showAndWait();
                }else {
                    String insertData = "INSERT INTO product " +
                            "(product_id, product_name, detail, stock, price, category, location, status, image, date) " +
                            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, inventory_productID.getText());
                    prepare.setString(2, inventory_productName.getText());
                    prepare.setString(3, inventory_detail.getText());
                    prepare.setString(4, inventory_stock.getText());
                    prepare.setString(5, inventory_price.getText());
                    prepare.setString(6, (String)inventory_category.getSelectionModel().getSelectedItem());
                    prepare.setString(7, (String)inventory_location.getSelectionModel().getSelectedItem());
                    prepare.setString(8, (String)inventory_status.getSelectionModel().getSelectedItem());

                    String path = data.path;
                    path = path.replace("\\", "\\\\");

                    prepare.setString(9, path);
                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepare.setString(10, String.valueOf(sqlDate));

                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();

                    inventoryShowData();
                    inventoryClearBtn();
                }
            }catch (Exception e){
                e.printStackTrace();}
        }
    }

    public void inventoryUpdateBtn(){

        if(inventory_productID.getText().isEmpty()
                || inventory_productName.getText().isEmpty()
                || inventory_detail.getText().isEmpty()
                || inventory_stock.getText().isEmpty()
                || inventory_price.getText().isEmpty()
                || inventory_category.getSelectionModel().getSelectedItem() == null
                || inventory_location.getSelectionModel().getSelectedItem() == null
                || inventory_status.getSelectionModel().getSelectedItem() == null
                || data.path == null || data.id == 0){

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please  fill all blank fields");
            alert.showAndWait();

        }else {

            String path = data.path;
            path = path.replace("\\", "\\\\");

            String updateData = "UPDATE product SET "
                    + "product_id = '" + inventory_productID.getText() + "', product_name = '"
                    + inventory_productName.getText() + "', detail = '"
                    + inventory_detail.getText() + "', stock = '"
                    + inventory_stock.getText() + "', price = '"
                    + Double.parseDouble(inventory_price.getText()) + "', category = '"
                    + inventory_category.getSelectionModel().getSelectedItem() + "', location = '"
                    + inventory_location.getSelectionModel().getSelectedItem() + "', status = '"
                    + inventory_status.getSelectionModel().getSelectedItem() + "', image = '"
                    + path + "' WHERE id = " + data.id;


            connect = database.connectionDb();

            try {

                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to Update Product ID : " + inventory_productID.getText() + "?" );
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get().equals(ButtonType.OK)){
                    prepare = connect.prepareStatement(updateData);
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    // To Update your table view
                    inventoryShowData();
                    // to clear your fields
                    inventoryClearBtn();
                }else {

                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled.");
                    alert.showAndWait();
                }
            }catch (Exception e){e.printStackTrace();
            }
        }
    }

    public void inventoryDeleteBtn(){
        if(data.id == 0){

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please  fill all blank fields");
            alert.showAndWait();

        }else {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to Delete Product ID: "+ inventory_productID.getText() + "?");
            Optional<ButtonType>option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)){

                String deleteData = "DELETE FROM product WHERE id = " + data.id;
                try {
                    prepare = connect.prepareStatement(deleteData);
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted");
                    alert.showAndWait();

                    // To Update your table view
                    inventoryShowData();
                    // to clear your fields
                    inventoryClearBtn();

                }catch (Exception e){e.printStackTrace();}
            }else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Cancelled");
                alert.showAndWait();
            }



        }
    }

    public  void inventoryClearBtn(){

        inventory_productID.setText("");
        inventory_productName.setText("");
        inventory_detail.setText("");
        inventory_stock.setText("");
        inventory_price.setText("");
        inventory_category.getSelectionModel().clearSelection();
        inventory_location.getSelectionModel().clearSelection();
        inventory_status.getSelectionModel().clearSelection();
        data.path = "";
        data.id = 0;
        inventory_imageView.setImage(null);
    }

    public void inventoryImportBtn(){

        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File", "*png", "*jpg"));

        File file = openFile.showOpenDialog(main_form.getScene().getWindow());
        if(file != null){
            data.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(),127, 134, false, true);

            inventory_imageView.setImage(image);
        }
    }

    // Merge All Datas
    public ObservableList<productData> inventoryDataList(){

        ObservableList<productData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM product";

        connect = database.connectionDb();

        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            productData prodData;

            while (result.next()){

                prodData = new productData(result.getInt("id"),
                        result.getString("product_id"),
                        result.getString("product_name"),
                        result.getString("detail"),
                        result.getInt("stock"),
                        result.getDouble("price"),
                        result.getString("category"),
                        result.getString("location"),
                        result.getString("status"),
                        result.getString("image"),
                        result.getDate("date"));

                listData.add(prodData);
            }

        }catch (Exception e){e.printStackTrace();

        }
        return listData;
    }

    //To Show Data On Our Table
    private ObservableList<productData> inventoryListData;
    public void inventoryShowData(){
        inventoryListData = inventoryDataList();

        inventory_col_productID.setCellValueFactory(new PropertyValueFactory<>("productID"));
        inventory_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        inventory_col_detail.setCellValueFactory(new PropertyValueFactory<>("detail"));
        inventory_col_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        inventory_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        inventory_col_category.setCellValueFactory(new PropertyValueFactory<>("category"));
        inventory_col_location.setCellValueFactory(new PropertyValueFactory<>("location"));
        inventory_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        inventory_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        inventory_tableView.setItems(inventoryListData);
    }

    public void inventorySelectData() {

        productData prodData = inventory_tableView.getSelectionModel().getSelectedItem();
        int num = inventory_tableView.getSelectionModel().getSelectedIndex();

        if((num - 1) < -1) return;

        inventory_productID.setText(prodData.getProductID());
        inventory_productName.setText(prodData.getProductName());
        inventory_detail.setText(prodData.getDetail());
        inventory_stock.setText(String.valueOf(prodData.getStock()));
        inventory_price.setText(String.valueOf(prodData.getPrice()));

        data.path = prodData.getImage();

        String path = "File:" + prodData.getImage();
        data.id = prodData.getId();

        image = new Image(path, 127, 134, false, true);
        inventory_imageView.setImage(image);

    }

    private String[] categoryList = {"Snacks", "Drinks", "Foods", "Others"};
    public void inventoryCategoryList(){

        List<String> categoryL = new ArrayList<>();

        for(String data: categoryList){
            categoryL.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(categoryL);
        inventory_category.setItems(listData);
    }
    
    private String[] locationList = {"หน้าร้าน", "หลังร้าน", "ข้างร้าน"};
    public void inventoryLocationList(){
        
        List<String> locationL = new ArrayList<>();
        
        for(String data: locationList){
            locationL.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(locationL);
        inventory_location.setItems(listData);
    }

    private String[] statusList = {"Available", "Unavailable"};
    public void inventoryStatusList(){

        List<String> statusL = new ArrayList<>();
        for (String data : statusList){
            statusL.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(statusL);
        inventory_status.setItems(listData);
    }

    public ObservableList<productData> menuGetData() {

         String sql = "SELECT * FROM product";

         ObservableList<productData> listData = FXCollections.observableArrayList();
         connect = database.connectionDb();

         try{
             prepare = connect.prepareStatement(sql);
             result = prepare.executeQuery();

             productData prod;

             while (result.next()){
                 prod = new productData(result.getInt("id"),
                         result.getString("product_id"),
                         result.getString("product_name"),
                         result.getInt("stock"),
                         result.getDouble("price"),
                         result.getString("category"),
                         result.getString("image"),
                         result.getDate("date"));

                 listData.add(prod);
             }

         }catch (Exception e){e.printStackTrace();}

        return listData;
    }

    public  void menuDisplayCard(){

        cardListData.clear();
        cardListData.addAll(menuGetData());

        int row = 0;
        int column = 0;

        menu_gridPane.getChildren().clear();
        menu_gridPane.getRowConstraints().clear();
        menu_gridPane.getColumnConstraints().clear();

        for (int q= 0; q < cardListData.size(); q++){

            try{
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("cardProduct.fxml"));
                AnchorPane pane = load.load();
                cardProductController cardC = load.getController();
                cardC.setData(cardListData.get(q));

                if (column == 3) {
                    column = 0;
                    row += 1;
                }

                menu_gridPane.add(pane, column++, row);
                GridPane.setMargin(pane, new Insets(3));

            }catch (Exception e){e.printStackTrace();}

        }

    }

    public void displayUsername() {
        String user = data.username;
        user = user.substring(0, 1).toUpperCase() + user.substring(1);

        username.setText(user);
    }

    public void switchForm(ActionEvent event){

        if(event.getSource() == dashboard_btn){
            dashboard_form.setVisible(true);
            inventory_form.setVisible(false);
            sale_form.setVisible(false);
            customer_form.setVisible(false);

            dashboardDisplayNC();
            dashboardTotalI();
            dashboardDisplayTI();
            dashboardIncomeChart();
            dashboardCustomerChart();

        }else if (event.getSource() == inventory_btn){
            inventory_form.setVisible(true);
            dashboard_form.setVisible(false);
            sale_form.setVisible(false);
            customer_form.setVisible(false);

            inventoryCategoryList();
            inventoryLocationList();
            inventoryShowData();
        }else if (event.getSource() == selling_btn){
            sale_form.setVisible(true);
            dashboard_form.setVisible(false);
            inventory_form.setVisible(false);
            customer_form.setVisible(false);

            menuDisplayCard();
            menuDisplayTotal();
            menuShowOrderData();
        }else if (event.getSource() == customer_btn){
            dashboard_form.setVisible(false);
            sale_form.setVisible(false);
            inventory_form.setVisible(false);
            customer_form.setVisible(true);

            customersShowData();
        }
    }

    public void logout(){

        try{

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are You Want To Logout ?");
            Optional<ButtonType> option = alert.showAndWait();

            if(option.get().equals(ButtonType.OK)){

                // To hide main form
                logout_btn.getScene().getWindow().hide();

                //Link your login form and show it
                Parent root = FXMLLoader.load(getClass().getResource("pos-view.fxml"));

                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.setTitle("Point of Sale System");

                stage.setScene(scene);
                stage.show();

            }
        }catch (Exception e){e.printStackTrace();}
    }

    public ObservableList<productData> menuGetOrder(){
        customerID();
        ObservableList<productData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM customer WHERE customer_id = " + cID;

        connect = database.connectionDb();

        try{

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            productData prod;

            while (result.next()){
                prod = new productData(result.getInt("id"),
                        result.getString("product_id"),
                        result.getString("product_name"),
                        result.getInt("quantity"),
                        result.getDouble("price"),
                        result.getString("category"),
                        result.getString("image"),
                        result.getDate("date"));
                listData.add(prod);

            }

        }catch (Exception e){e.printStackTrace();}
        return listData;
    }

    private  ObservableList<productData> menuOrderListData;
    public void menuShowOrderData(){
        menuOrderListData = menuGetOrder();

        menu_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        menu_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        menu_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        menu_tableView.setItems(menuOrderListData);

    }

    private int getid;
    public void menuSelectOrder(){

        productData prod = menu_tableView.getSelectionModel().getSelectedItem();
        int num = menu_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) return;
        // TO GET THE ID PER ORDER
        getid = prod.getId();
    }

    private  double totalP;
    public void menuGetTotal(){
        customerID();
        String total = "SELECT SUM(price) FROM customer WHERE customer_id = " + cID;

        connect = database.connectionDb();

        try {

            prepare = connect.prepareStatement(total);
            result = prepare.executeQuery();

            if (result.next()){
                totalP = result.getDouble("SUM(price)");
            }

        }catch (Exception e){e.printStackTrace();}
    }

    public void menuDisplayTotal(){
        menuGetTotal();
        menu_total.setText("฿" + totalP);
    }

    private double amount;
    private double change;
    public void menuAmount(){
        menuGetTotal();
        if (menu_amount.getText().isEmpty() || totalP == 0){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid :(");
            alert.showAndWait();
        }else {
            amount = Double.parseDouble(menu_amount.getText());
            if (amount < totalP){
                menu_amount.setText("");
            }else{
                change = (amount - totalP);
                menu_change.setText("฿" + change);
            }
        }
    }

    public void  menuPayBtn(){

        if (totalP == 0){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("please choose your order first !");
            alert.showAndWait();
        }else {
            menuGetTotal();
            String insertPay = "INSERT INTO receipt (customer_id, total, date, user)"
                    + "VALUES(?, ?, ?, ?)";

            connect = database.connectionDb();

            try{

                if (amount == 0){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Something Wrong :(");
                    alert.showAndWait();
                }else {
                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Are U Sure?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if(option.get().equals(ButtonType.OK)) {
                        customerID();
                        menuGetTotal();
                        prepare = connect.prepareStatement(insertPay);
                        prepare.setString(1, String.valueOf(cID));
                        prepare.setString(2, String.valueOf(totalP));
                        Date date = new Date();
                        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                        prepare.setString(3, String.valueOf(sqlDate));
                        prepare.setString(4, data.username);
                        prepare.executeUpdate();

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successful.");
                        alert.showAndWait();

                        menuShowOrderData();
                        menuRestart();
                    }else{
                        alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Cancelled.");
                        alert.showAndWait();
                    }
                }
            }catch (Exception e){e.printStackTrace();}
        }
    }

    public void menuRemoveBtn(){

        if (getid == 0){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select the order you want to remove");
            alert.showAndWait();
        }else {
            String deleteData = "DELETE FROM customer WHERE id = " + getid;
            connect = database.connectionDb();
            try{
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are U sure U want to delete this order?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)){
                    prepare = connect.prepareStatement(deleteData);
                    prepare.executeUpdate();
                }

                menuShowOrderData();
            }catch (Exception e){e.printStackTrace();}
        }
    }

    public ObservableList<customersData> customersDataList(){

        ObservableList<customersData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM receipt";
        connect = database.connectionDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            customersData cData;

            while (result.next()){
                cData = new customersData(result.getInt("id"),
                        result.getInt("customer_id"),
                        result.getDouble("total"),
                        result.getDate("date"),
                        result.getString("user"));

                listData.add(cData);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<customersData>customersListData;
    public void customersShowData(){
        customersListData = customersDataList();

        customers_col_customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customers_col_total.setCellValueFactory(new PropertyValueFactory<>("total"));
        customers_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        customers_col_cashier.setCellValueFactory(new PropertyValueFactory<>("user"));

        customers_tableView.setItems(customersListData);
    }

    public void menuRestart(){
        totalP = 0;
        change = 0;
        amount = 0;
        menu_total.setText("฿0.0");
        menu_change.setText("฿0.0");
        menu_amount.setText("");
    }

//    public void menuReceiptBtn(){
//
//        if (totalP == 0 || menu_amount.getText().isEmpty()){
//            alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error Message");
//            alert.setHeaderText(null);
//            alert.setContentText("Please order first");
//            alert.showAndWait();
//        }else {
//            customerID();
//            HashMap map = new HashMap();
//            map.put("",cID);
//        }
//
//    }

    private int cID;
    public void customerID(){

        String sql = "SELECT MAX(customer_id) FROM customer";
        connect = database.connectionDb();

        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            if (result.next()){
                cID = result.getInt("MAX(customer_id)");
            }

            String checkCID = "SELECT MAX(customer_id) FROM receipt";
            prepare = connect.prepareStatement(checkCID);
            result = prepare.executeQuery();
            int checkID = 0;

            if (result.next()){
                checkID = result.getInt("MAX(customer_id)");
            }

            if (cID == 0){
                cID+=1;
            }else if (cID == checkID){
                cID += 1;
            }

            data.cID = cID;

        }catch (Exception e){e.printStackTrace();}

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        displayUsername();

        dashboardDisplayNC();
        dashboardTotalI();
        dashboardDisplayTI();
        dashboardIncomeChart();
        dashboardCustomerChart();

        inventoryCategoryList();
        inventoryLocationList();
        inventoryStatusList();
        inventoryShowData();

        menuDisplayCard();
        menuGetOrder();
        menuDisplayTotal();
        menuShowOrderData();

        customersShowData();
    }
}
