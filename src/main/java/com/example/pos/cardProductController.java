package com.example.pos;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class cardProductController implements Initializable {

    @FXML
    private AnchorPane card_form;

    @FXML
    private Button prod_addBtn;

    @FXML
    private ImageView prod_imageView;

    @FXML
    private Label prod_name;

    @FXML
    private Label prod_price;

    @FXML
    private Spinner<Integer> prod_spinner;

    private productData prodData;
    private Image image;
    private String detail;
    private String category;
    private String prod_image;
    private String prod_date;


    private SpinnerValueFactory<Integer> spin;
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Alert alert;
    private String prodID;
    public  void setData(productData prodData){
        this.prodData = prodData;

        prod_date = String.valueOf(prodData.getDate());
        prod_image = prodData.getImage();
        category = prodData.getCategory();
        detail = prodData.getDetail();
        prodID = prodData.getProductID();
        prod_name.setText(prodData.getProductName());
        prod_price.setText("฿"+String.valueOf(prodData.getPrice()));
        String path = "File:" + prodData.getImage();
        image = new Image(prodData.getImage(), 219, 95, false, true);
        prod_imageView.setImage(image);
        pr = prodData.getPrice();

    }
    private int qty;
    public void setQuantity(){
        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100 ,0);
        prod_spinner.setValueFactory(spin);
    }

    private  double totalP;
    private  double pr;
    public void addBtn(){

        mainFormController mForm = new mainFormController();
        mForm.customerID();

        qty = prod_spinner.getValue();
        String check = "";
        String checkAvailable = "SELECT status FROM product WHERE product_id = '"
                + prodID + "'";

        connect = database.connectionDb();

        try {
            int checkStck = 0;
            String checkStock = "SELECT stock FROM product WHERE product_id = '"
                    + prodID + "'";

            prepare = connect.prepareStatement(checkStock);
            result = prepare.executeQuery();

            if (result.next()){
                checkStck = result.getInt("stock");
            }

            if (checkStck == 0){
                String updateStock = "UPDATE product SET product_name = '"
                        + prod_name.getText() + "', category = '"
                        + category + "', stock = 0, price = '" + pr
                        + "', status = 'Unavailable', image = '"
                        + prod_image + "', Where prod_id = '"
                        + prodID + "'";
                prepare = connect.prepareStatement(updateStock);
                prepare.executeUpdate();

            }
            prepare = connect.prepareStatement(checkAvailable);
            result = prepare.executeQuery();

            if (result.next()){
                check = result.getString("status");
            }
            if (!check.equals("Available") || qty == 0){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Somthing Wrong :(");
            }else {


                if (checkStck < qty){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid. This Product is out of stock");
                    alert.showAndWait();

                }else {

                    prod_image = prod_image.replace("\\", "\\\\");

                    String insertData = "INSERT INTO customer "
                            + "(customer_id, product_id, product_name, quantity, price, category, image, date, user)"
                            + "VALUE(?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, String.valueOf(data.cID));
                    prepare.setString(2, prodID);
                    prepare.setString(3, prod_name.getText());
                    prepare.setString(4, String.valueOf(qty));
                    totalP = (qty * pr);
                    prepare.setString(5, String.valueOf(totalP));
                    prepare.setString(6, category);
                    prepare.setString(7, prod_image);
                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepare.setString(8, String.valueOf(sqlDate));
                    prepare.setString(9, data.username);

                    prepare.executeUpdate();

                    int upStock = checkStck - qty;

                    prod_image = prod_image.replace("\\", "\\\\");

                    String updateStock = "UPDATE product SET product_name = '"
                            + prod_name.getText() + "', category = '"
                            + category + "', stock = " + upStock + ", price = '" + pr
                            + "', status = '"
                            + check + "', image = '"
                            + prod_image + "', date = '"
                            + prod_date + "' WHERE product_id = '"
                            + prodID + "'";


                    prepare = connect.prepareStatement(updateStock);
                    prepare.executeUpdate();

                    alert= new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();

                    mForm.menuGetTotal();
                }

            }
        }catch (Exception e){e.printStackTrace();}
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setQuantity();
    }
}
