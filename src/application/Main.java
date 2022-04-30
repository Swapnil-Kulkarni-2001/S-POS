package application;
	
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;


public class Main extends Application {
	public String url="jdbc:h2:~/PosDB";
	public String username="sa";
	public String password="";
	public String secureCheck;
	public String udname="";
	public String udpass="";
	public Connection conn;
	 
	@Override
	public void start(Stage primaryStage) {
		try {
		       Class.forName("org.h2.Driver");
		       conn = DriverManager.getConnection(url,username,password);
		       ResultSet st =  conn.createStatement().executeQuery("select secure from info_table");
		       if(st.next())
		       {
		    	   secureCheck=st.getString("secure");
		       }
		       System.out.println(secureCheck);
		       st.close();
		       conn.close();
		       if(secureCheck.equals("Yes"))
		       {
		    	   edit();

		       }
		       else
		       {
		    	   
					Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
					//System.out.println(getClass().getResource("Main.fxml"));
					Scene scene = new Scene(root);
					scene.getStylesheets().add(getClass().getResource("/view/application.css").toExternalForm());
					primaryStage.setScene(scene);
					//primaryStage.initStyle(StageStyle.UNDECORATED);
					primaryStage.setMaximized(true);
					primaryStage.show();
		       }
			/*Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			//primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.show();*/
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private void edit() throws Exception{
     
		Stage primaryStage = new Stage();
        TextField uname = new TextField();
        PasswordField upass = new PasswordField();


    	try {
     	       Class.forName("org.h2.Driver");
		       conn = DriverManager.getConnection(url,username,password);
		       ResultSet st =  conn.createStatement().executeQuery("select user_name,user_password from info_table");
     	   if(st.next())
     	   {
     		   udname=st.getString("user_name");
     		   udpass=st.getString("user_password");
     		   System.out.println(udname);
     		   System.out.println(udpass);
     	   }
     	}catch(Exception eee){
     		
     	}
    	
    	conn.close();
    	
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(16));


        
        grid.addRow(0, new Label("Name       :"), uname);
        grid.addRow(1, new Label("Password   :"), upass);


        Button okButton = new Button("Login");

        grid.add(okButton, 0, 2, 2, 1);

        ColumnConstraints leftCol = new ColumnConstraints();
        leftCol.setHgrow(Priority.NEVER);
        leftCol.setHalignment(HPos.RIGHT);
        ColumnConstraints rightCol = new ColumnConstraints();
        rightCol.setHgrow(Priority.SOMETIMES);
        grid.getColumnConstraints().addAll(leftCol, rightCol);
        GridPane.setHalignment(okButton, HPos.CENTER);

        Scene scene = new Scene(grid);
        Stage stage = new Stage();
        stage.setResizable(false);
        //grid.setStyle("-fx-background-color:BLACK");
        //stage.setOpacity(0.8);

        
        scene.setFill(Color.TRANSPARENT);
        okButton.setOnAction(e -> {

        	if(uname.getText().equals(udname) && upass.getText().equals(udpass))
        	{
        		stage.close();
            	try
            	{
            		
            		
        			Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
            		
        			Scene scene2 = new Scene(root);
        			scene.getStylesheets().add(getClass().getResource("/view/application.css").toExternalForm());
        			primaryStage.setScene(scene2);
        			//primaryStage.initStyle(StageStyle.UNDECORATED);
        			
        			primaryStage.show();
            	}
            	catch(Exception ex)
            	{
            		
            	}
        	}
        	else
        	{
        		System.out.println("Wrong username and password");
	    		   System.out.println("Password incorrecrt!!");
		    	   Alert alert = new Alert(AlertType.ERROR);
		    	   alert.setTitle("S-PoS");
		    	   alert.setContentText("incorrect password!!");
		    	   alert.setHeaderText(null);
		    	   alert.show(); 
        	}
		   
        	
        	

        });


        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(null);
        stage.setScene(scene);
        stage.show();
    }
}
