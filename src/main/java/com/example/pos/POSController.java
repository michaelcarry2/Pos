package com.example.pos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class POSController {
    @FXML
    private Hyperlink admin_hyperlink;

    @FXML
    private Button admin_loginbtn;

    @FXML
    private PasswordField admin_password;

    @FXML
    private TextField admin_username;

    @FXML
    private Button close;

    @FXML
    private Button close1;

    @FXML
    private Hyperlink employee_hyperlink;

    public void switchForm(ActionEvent event) {
        if(event.getSource() == admin_hyperlink){
            main_form.setVisible(false);
            employee_form.setVisible(true);
        }else if (event.getSource() == employee_hyperlink){
            main_form.setVisible(true);
            employee_form.setVisible(false);
        }
    }

    @FXML
    private TextField employee_id;

    @FXML
    private Button employee_loginbtn;

    @FXML
    private PasswordField employee_password;

    @FXML
    private AnchorPane main_form;

    @FXML
    private AnchorPane employee_form;


    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    public void loginAdmin(){
        String sql = "SELECT * FROM admin WHERE username = ? and password = ?";
        connect = database.connectionDb();

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, admin_username.getText());
            prepare.setString(2, admin_password.getText());

            result =prepare.executeQuery();

            if (admin_loginbtn.getText().isEmpty()
                    || admin_password.getText().isEmpty()){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else{

                if (result.next()){
                    // To get the username that user used
                    data.username = admin_username.getText();

                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Login");
                    alert.showAndWait();

                    admin_loginbtn.getScene().getWindow().hide();

                    Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);


                    stage.setScene(scene);
                    stage.show();

                }else{
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong Username/Password");
                    alert.showAndWait();
                }
            }

        }catch (Exception e){e.printStackTrace();}
    }

    public void LoginEmployee(){

        String employeeData = "SELECT * FROM employee WHERE employee_id = ? and password = ?";

        connect = database.connectionDb();

        try {
            prepare = connect.prepareStatement(employeeData);
            if (employee_id.getText().isEmpty()
                    || employee_password.getText().isEmpty()){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else {
                prepare.setString(1, employee_id.getText());
                prepare.setString(2, employee_password.getText());

                result = prepare.executeQuery();

                if (result.next()){

                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Login!");
                    alert.showAndWait();

                    employee_loginbtn.getScene().getWindow().hide();

                    Parent root = FXMLLoader.load(getClass().getResource("employeeDashboard.fxml"));

                    Stage stage = new Stage();
                    Scene scene = new Scene(root);

                    stage.setScene(scene);
                    stage.show();

                }else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong EmployeeID/Password");
                    alert.showAndWait();
                }
            }

        }catch (Exception e) {e.printStackTrace();}

    }
    public void close(){
        System.exit(0);
    }

    public void POSController(URL url, ResourceBundle rb){

    }
}