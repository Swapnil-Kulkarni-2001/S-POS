package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InventoryController implements Initializable{
	
	public String Query;
	public String url="jdbc:h2:~/PosDB";
	public String username="sa";
	public String password="";
	public Connection conn;
	public ObservableList<Table2Items> list = FXCollections.observableArrayList();
	public SortedList<Table2Items> sortedData;
	
	@FXML TextField itemBarcode;
	@FXML TextField itemName;
	@FXML TextField itemQty;
	@FXML TextField itemPrice;
	@FXML TextField tablefilter;
	
	@FXML private TableView<Table2Items> tableview2;
	@FXML private TableColumn<Table2Items,Integer> iB;
	@FXML private TableColumn<Table2Items,String> iN;
	@FXML private TableColumn<Table2Items,Integer> iQ;
	@FXML private TableColumn<Table2Items,Double> iP;
	
	
	
	
	
	
	
	public void btnAddClicked(ActionEvent ae1) throws Exception
	{
		int iqty = 0;
		double iprice = 0;
		Boolean checkqty = true;
		String iname = itemName.getText();
		String barcode = itemBarcode.getText();
		String price = itemPrice.getText();
		String qty = itemQty.getText();
		
		try {
			iqty = Integer.parseInt(qty);
			iprice = Double.parseDouble(price);
		}catch(Exception eee) {
			checkqty = false;
		}
		
		System.out.println("btnAddClicked");
		
		if(barcode.length()>5 && iname!="" && checkqty )
		{
			Query = "insert into items_table(item_barcode,item_name,item_qty,item_price) values(?,?,?,?);";
		    Class.forName("org.h2.Driver");
		    conn = DriverManager.getConnection(url,username,password);
	        PreparedStatement st = conn.prepareStatement(Query);
	        st.setString(1, barcode);
	        st.setString(2, iname);
	        st.setInt(3, iqty);
	        st.setDouble(4, iprice);
	        int rt = st.executeUpdate();
	        System.out.println(rt);
	        st.close();
	        conn.close();
	        tableview2.getItems().add(new Table2Items(barcode,iname,iqty,iprice));
	    	   Alert alert = new Alert(AlertType.INFORMATION);
	    	   alert.setTitle("S-PoS");
	    	   alert.setContentText("Product Added!!");
	    	   alert.setHeaderText(null);
	    	   alert.show(); 
		}
	    
	}
	
	
	public void btnUpdateClicked(ActionEvent ae4) 
	{
		Table2Items si = null;
		String barcode = null;
		String name= null;
		String qty= null;
		String price= null;
		Boolean ch=false;
		System.out.println("minus clicked");
		try
		{
			si = tableview2.getSelectionModel().getSelectedItem();
			System.out.println(si);
			barcode= si.getItem_barcode();
			name=si.getItem_name();
			qty=String.valueOf(si.getItem_qty());
			price=String.valueOf(si.getItem_price());
			ch=true;
		}
		catch(Exception exec)
		{
			System.out.println("Select row first!!");
			ch=false;
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("StarPoS");
	   	    alert.setContentText("Select row first!!");
	   	    alert.setHeaderText(null);
	        alert.show(); 
		}
		if(ch)
		{
			try
			{
				edit(barcode,name,qty,price);
			}
			catch(Exception exe)
			{
				
			}
			
		}
	}
	
	
	public void btnClearClicked(ActionEvent ae2)
	{
		System.out.println("btnClearClicked");
		itemBarcode.setText("");
		itemName.setText("");
		itemQty.setText("");
		itemPrice.setText("");
	}
	
	
	public void btnMinusClicked(ActionEvent ae3) throws Exception
	{
		System.out.println("btnMinusClicked");
		
		Table2Items si = null;
		String barcode = null;
		Boolean ch=false;
		System.out.println("minus clicked");
		try
		{
			si = tableview2.getSelectionModel().getSelectedItem();
			System.out.println(si);
			barcode= si.getItem_barcode();
			ch=true;
		}
		catch(Exception exec)
		{
			System.out.println("Select row first!!");
			ch=false;
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("StarPoS");
	   	    alert.setContentText("Select row first!!");
	   	    alert.setHeaderText(null);
	        alert.show(); 
		}
		if(ch)
		{
		    		System.out.println("Password correcrt!!");
		    		
					Query = "delete from items_table where item_barcode=?";
				    Class.forName("org.h2.Driver");
				    conn = DriverManager.getConnection(url,username,password);
				    PreparedStatement st = conn.prepareStatement(Query);
				    st.setString(1, barcode);
				    int rset = st.executeUpdate();
				    System.out.println(rset+" row affected");
				    int index = tableview2.getSelectionModel().getSelectedIndex();
				    tableview2.getItems().remove(index);
			    	Alert alert = new Alert(AlertType.CONFIRMATION);
			        alert.setTitle("StarPoS");
			   	    alert.setContentText("Record deleted Successfully!");
			   	    alert.setHeaderText(null);
			        alert.show(); 			
		}
		
	}
	
	public void btnRefreshClicked(ActionEvent ae3) throws Exception
	{
		System.out.println("btnRefreshClicked");
		tableview2.getItems().clear();

	    Class.forName("org.h2.Driver");
	    conn = DriverManager.getConnection(url,username,password);
	    ResultSet rset = conn.createStatement().executeQuery("select * from items_table");
	    while(rset.next())
	    {
	    	list.add(new Table2Items(rset.getString("item_barcode"),rset.getString("item_name"),rset.getInt("item_qty"),rset.getDouble("item_price")));
	    	
	    }
	    rset.close();
	    conn.close();
	    tableview2.setItems(list);
		
	}


	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		iB.setCellValueFactory(new PropertyValueFactory<Table2Items,Integer>("item_barcode"));
		iN.setCellValueFactory(new PropertyValueFactory<Table2Items,String>("item_name"));
		iQ.setCellValueFactory(new PropertyValueFactory<Table2Items,Integer>("item_qty"));
		iP.setCellValueFactory(new PropertyValueFactory<Table2Items,Double>("item_price"));
		
		tableview2.getItems().clear();

		try {
			 Class.forName("org.h2.Driver");
			    conn = DriverManager.getConnection(url,username,password);
			    ResultSet rset = conn.createStatement().executeQuery("select * from items_table");
			    while(rset.next())
			    {
			    	list.add(new Table2Items(rset.getString("item_barcode"),rset.getString("item_name"),rset.getInt("item_qty"),rset.getDouble("item_price")));
			    	
			    }
			    rset.close();
			    conn.close();
		}
		catch(Exception eexx)
		{
			System.out.println("Error");
		}
	   
	    tableview2.setItems(list);
		
		

		
	}
	private void edit(String barcode,String name,String qty,String price) throws Exception{
        TextField ibarcode = new TextField();
        TextField iname = new TextField();
        TextField iqty = new TextField();
        TextField iprice = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(16));


        ibarcode.setText(barcode);
        iname.setText(name);
        iqty.setText(qty);
        iprice.setText(price);
        
        grid.addRow(0, new Label("Barcode:"), ibarcode);
        grid.addRow(1, new Label("Name   :"), iname);
        grid.addRow(2, new Label("Qty    :"), iqty);
        grid.addRow(3, new Label("Price  :"), iprice);

        Button okButton = new Button("Update");

        grid.add(okButton, 0, 4, 2, 1);

        ColumnConstraints leftCol = new ColumnConstraints();
        leftCol.setHgrow(Priority.NEVER);
        leftCol.setHalignment(HPos.RIGHT);
        ColumnConstraints rightCol = new ColumnConstraints();
        rightCol.setHgrow(Priority.SOMETIMES);
        grid.getColumnConstraints().addAll(leftCol, rightCol);
        GridPane.setHalignment(okButton, HPos.CENTER);

        Scene scene = new Scene(grid);
        Stage stage = new Stage();

        okButton.setOnAction(e -> {
        	int index=0;
    		int item_qty = 0;
    		double item_price = 0;
    		Boolean checkqty = true;
        	System.out.println("Updated");
        	
    		try {
    			item_qty = Integer.parseInt(iqty.getText());
    			item_price = Double.parseDouble(iprice.getText());
    		}
    		catch(Exception eee) 
    		{
    			checkqty = false;
    		}

    		if(barcode.length()>5 && iname.getText()!="" && checkqty )
    		{
    			try
    			{
        			Query = "update items_table set item_name = ?,item_qty = ?,item_price = ?,item_barcode=? where item_barcode = ?";
        		    Class.forName("org.h2.Driver");
        		    conn = DriverManager.getConnection(url,username,password);
        	        PreparedStatement st = conn.prepareStatement(Query);
        	        st.setString(1, iname.getText());
        	        st.setInt(2, item_qty);
        	        st.setDouble(3, item_price);
        	        st.setString(4, ibarcode.getText());
        	        st.setString(5, barcode);
        	        int res = st.executeUpdate();
        	        st.close();
        	        conn.close();
        	        System.out.println(res+" rows updated");
        	      	Alert alert = new Alert(AlertType.CONFIRMATION);
    		        alert.setTitle("StarPoS");
    		   	    alert.setContentText("Record Updated Successfully!");
    		   	    alert.setHeaderText(null);
    		        alert.show(); 
    			}
    			catch(Exception ea)
    			{
    				System.out.println(ea);

    			}

    	        
    		}
    		else {
			  	Alert alert = new Alert(AlertType.ERROR);
		        alert.setTitle("S-Pos");
		   	    alert.setContentText("Enter valid details");
		   	    alert.setHeaderText(null);
		        alert.show(); 
		  
    		}
        	
        	
	        for(Table2Items si : tableview2.getItems())
	    	{
    	    	 index = index+1;
    	    	 if(si.getItem_barcode().equals(barcode))
    	    	 {
    	    		    
    	    		    tableview2.getItems().set(index-1,new Table2Items(ibarcode.getText(),iname.getText(),Integer.parseInt(iqty.getText()),Double.parseDouble(iprice.getText())));
    	    		    break;
    	    	 }
	
	    	}
        	
        	stage.hide();
        });


        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(null);
        stage.setScene(scene);
        stage.show();
    }
	
	public void search(KeyEvent ae)
	{
		
		Table2Items itms = new Table2Items();
		if(ae.getCode().equals(KeyCode.ENTER))
		{


			String search = tablefilter.getText();
			System.out.println(search);
			
			
	        int index=0;
    	    for(Table2Items si : tableview2.getItems())
	    	{
    	    	 index = index+1;
    	    	 if(si.getItem_barcode().equals(search))
    	    	 {
    	    		  itms.setItem_barcode(si.getItem_barcode());
    	    		  itms.setItem_name(si.getItem_name());
    	    		  itms.setItem_qty(si.getItem_qty());
    	    		  itms.setItem_price(si.getItem_price());
    	    		  tableview2.getItems().clear();
    	    		  tableview2.getItems().add(itms);
    	    		  break;
    	    	 }
	    	}
		}
	}
}
