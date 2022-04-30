package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class InvoiceController implements Initializable{
	
	public String url="jdbc:h2:~/PosDB";
	public String username="sa";
	public String password="";
	public Connection conn;
	public int table_serial_no_counter = 0;
	
	public double grand_total = 0.0;
	public double itemtot = 0.0;
	public int itemCounter = 0;
	public java.util.Date cd = new java.util.Date();
	public java.sql.Date sqlDate; 
	
	
	String custname;
	
	public String pattern = "^[6-9]\\d{9}$";
	public String usernameValidate = "^[\\p{L} .'-]+$";
	
	List<Items> listitems = new ArrayList<Items>();
	
	
	@FXML private TextField barcodefield; 
	@FXML private Label labelTotal;
	@FXML private RadioButton radiobuttonCash;
	@FXML private RadioButton radiobuttonCD;
	@FXML private RadioButton radiobuttonUPI;
	@FXML private TextField item_name_invoice;
	
	@FXML private TextField cust_mob_no;
	@FXML private TextField cust_name;
	

	@FXML private TableView<ShopItems> billTableView;
	@FXML private TableColumn<ShopItems,Integer> serial_no;
	@FXML private TableColumn<ShopItems,String> item_name;
	@FXML private TableColumn<ShopItems,Integer> item_qty;
	@FXML private TableColumn<ShopItems,Double> item_unit_price;
	@FXML private TableColumn<ShopItems,Double> item_total_price;
	
	
	public ObservableList<Table2Items> list = FXCollections.observableArrayList();
	private int res;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		serial_no.setCellValueFactory(new PropertyValueFactory<ShopItems,Integer>("serial_no"));
		item_name.setCellValueFactory(new PropertyValueFactory<ShopItems,String>("item_name"));
		item_qty.setCellValueFactory(new PropertyValueFactory<ShopItems,Integer>("item_qty"));
		item_unit_price.setCellValueFactory(new PropertyValueFactory<ShopItems,Double>("item_unit_price"));
		item_total_price.setCellValueFactory(new PropertyValueFactory<ShopItems,Double>("item_total_price"));
	}

	public void barcodeEvent(KeyEvent ke) throws Exception  
	{
		 if (ke.getCode().equals(KeyCode.ENTER)) {
				String barcode = barcodefield.getText();
				String query = "select * from items_table where item_barcode=?";
				Boolean check_item = false;
				int item_counter = 0;
			    int sno = 0;
			    
			    if(barcode != "")
				{
					
				       Class.forName("org.h2.Driver");
				       conn = DriverManager.getConnection(url,username,password);
				       PreparedStatement st = conn.prepareStatement(query);
				       st.setString(1,barcode);
				       ResultSet rs = st.executeQuery();
				       
				       if(rs.next())
				       {  
				    	    String item_name = rs.getString("item_name");
				    	    Double item_price = rs.getDouble("item_price");
				    	    int checkqty = rs.getInt("item_qty");
				    	    
				    	    if(checkqty!=0)
				    	    {
				    	    	if(checkqty<=5)
				    	    	{
				    	    		Notifications n = Notifications.create()
				    	    				.title("Warning")
				    	    				.text(item_name+" : "+"Only "+checkqty+" Remaining")
				    	    				.graphic(null)
				    	    				.hideAfter(Duration.seconds(5))
				    	    				.position(Pos.TOP_RIGHT);
				    	    		n.showWarning();
				    	    			
				    	    	
				    	    	}
					    	    grand_total = grand_total + item_price;
						    	  
					    	    int index=0;
					    	    itemCounter=itemCounter+1;
					    	
					    	    
					    	    for(ShopItems si : billTableView.getItems())
				    	    	{
					    	    	 index = index+1;
					    	    	 if(si.getItem_name().equals(item_name))
					    	    	 {
					    	    		    check_item = true;
					    	    		   
					    	    		    item_counter=si.getItem_qty()+1;
					    	    		    sno = si.getSerial_no();
					    	    		    itemtot = si.getItem_unit_price() + si.getItem_total_price();
							    	    	
					    	    		    break;
					    	    	 }
						
				    	    	}
					    	    if(check_item)
					    	    {

			    	    		     billTableView.getItems().set(index-1,new ShopItems(sno,item_name,item_counter,item_price,itemtot));
			    	    		     list.get(index-1).setItem_qty(item_counter);
			    	   
					    	    }
					    	    else
					    	    {
					    	    	table_serial_no_counter = table_serial_no_counter+1;
					    	    	itemtot = item_price;
					    	    	billTableView.getItems().add(new ShopItems(table_serial_no_counter,item_name,1,item_price,itemtot));
					    	    	list.add(new Table2Items(barcode,item_name,1,item_price));
					    	    }
					    	    
					    	 
					    	    AudioClip note1 = new AudioClip(getClass().getResource("itemFound.wav").toURI().toString());
					    	    note1.play();
					    	    
					    	    
					    	    labelTotal.setText(String.valueOf(grand_total));
					    	    item_name_invoice.setText(item_name);
					    	    
					      
					    	    Boolean check = false;
					    	    int indexoflist = 0;
					    	    for(int i = 0;i<listitems.size();i++)
					    	    {
					    	    	if(listitems.get(i).getItemname().equals(item_name))
					    	    	{
					    	    		check=true;
					    	    		indexoflist = i;
					    	    		break;
					    	    	}
					    	    } 
					    	    if(check)
					    	    {
					    	    	listitems.get(indexoflist).setItemqty(item_counter);
					    	    	listitems.get(indexoflist).setItemtotal(itemtot);
					    	    }
					    	    else
					    	    {
					    	    	 listitems.add(new Items(item_name,1,item_price,item_price));
					    	    } 
				    	    }
				    	  
				    	    else {
						    	   AudioClip note2 = new AudioClip(getClass().getResource("error.wav").toURI().toString());
						    	   note2.play();
						    	   Alert alert = new Alert(AlertType.ERROR);
						    	   alert.setTitle("S-PoS");
						    	   alert.setContentText("Item run out of quantity!!");
						    	   alert.setHeaderText(null);
						    	   alert.show(); 
				    	    }
      
				       }
				       else {
				    	   AudioClip note2 = new AudioClip(getClass().getResource("error.wav").toURI().toString());
				    	   note2.play();
				    	   Alert alert = new Alert(AlertType.ERROR);
				    	   alert.setTitle("StarPoS");
				    	   alert.setContentText("Item not found");
				    	   alert.setHeaderText(null);
				    	   alert.show();   
				       }
				            st.close();
					        conn.close();
					        barcodefield.setText("");	     
				}			    
		 }       
	}
    	
	public void billClicked(ActionEvent ae1) throws Exception 
	{    
		String shopName="";
		String shopLogo="";
		String cono="";
		String pay;
		if(radiobuttonCash.isSelected())
		{
			pay = "cash";
		}
		else if(radiobuttonUPI.isSelected())
		{
			pay = "UPI";
		}
		else
		{
			pay = "Credit/Debit";
		}
		String cname = cust_name.getText();
		String phonno = cust_mob_no.getText();
		sqlDate = new java.sql.Date(cd.getTime());
		LocalTime lt = LocalTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a");
		String time2 = lt.format(dtf);
		if(phonno.matches(pattern) && cname.matches(usernameValidate) && grand_total>0)
		{	
		  	 String query = "insert into customer_table(customer_name,customer_bill,bill_date,customer_mobile_no,bill_time,payment) values(?,?,?,?,?,?);";
		     Class.forName("org.h2.Driver");
		     conn = DriverManager.getConnection(url,username,password);
	         PreparedStatement st = conn.prepareStatement(query);
    	     st.setString(1,cname);
    	     st.setDouble(2,grand_total);
    	     st.setDate(3,sqlDate);
    	     st.setString(4,phonno);
    	     st.setString(5,time2);
    	     st.setString(6, pay);
	         res = st.executeUpdate();
	         st.close();
	         System.out.println(res);
	         



	         conn.setAutoCommit(false);
	         String query2 = "update sales_table set times_sold=? where item_barcode=? and sold_date=?;"; 
	         PreparedStatement st2 = conn.prepareStatement(query2);
	         String query3 = "insert into sales_table(item_barcode,times_sold,sold_date)values(?,?,?);";
        	 PreparedStatement st3  = conn.prepareStatement(query3);
        	 
		     String query4 = "select item_qty from items_table where item_barcode=?";
		     PreparedStatement st4 = conn.prepareStatement(query4);
        	 String query5 = "update items_table set item_qty=? where item_barcode=?";
		     PreparedStatement st5 = conn.prepareStatement(query5);

	         for(Table2Items si : list)
 	    	{
		        
		         String query1 = "select times_sold from sales_table where item_barcode=? and sold_date=?";
		         PreparedStatement st1 = conn.prepareStatement(query1);
		         st1.setString(1,si.getItem_barcode());
		         st1.setDate(2, sqlDate);
		         ResultSet rset = st1.executeQuery();
		       
		         if(rset.next())
		         { 
		        	 st2.setInt(1,rset.getInt("times_sold")+si.getItem_qty());
		        	 st2.setString(2,si.getItem_barcode());
		        	 st2.setDate(3, sqlDate);
		        	
		        	 st2.addBatch();
		        	 
		        	
		         }
		         else {
		        	 

		        	 st3.setString(1,si.getItem_barcode());
		        	 st3.setInt(2, si.getItem_qty());
		        	 st3.setDate(3, sqlDate);
		        	 
		        	 st3.addBatch();
		        	 
		        	
		         }
		         
		         
		         st4.setString(1, si.getItem_barcode());
		         ResultSet rset2 = st4.executeQuery();
		         if(rset2.next())
		         {
		        	 st5.setInt(1, rset2.getInt("item_qty")-si.getItem_qty());
		        	 st5.setString(2, si.getItem_barcode());
		        	 st5.addBatch();
		         }
		         
		         

 	    	}
	         
	         try
	         {
		         st2.executeBatch();
		         st3.executeBatch();
		         st5.executeBatch();
		         conn.commit();
		         st2.close();
		         st3.close();
		         st5.close();
	         }
	         catch(Exception ecx)
	         {
	        	 
	         }


	         conn.close();
	         
	         
	         
		  	 int bill_no = 1;
		     Class.forName("org.h2.Driver");
		     conn = DriverManager.getConnection(url,username,password);
	         ResultSet rst = conn.createStatement().executeQuery("select max(bill_no) from customer_table");
	         if(rst.next())
	         {
	        	 bill_no=rst.getInt("max(bill_no)");
	        	 bill_no=bill_no+1;
	         }
	         
		        ResultSet rst2 = conn.createStatement().executeQuery( "select shop_name,shop_logo,counter_no from info_table");
		        if(rst2.next())
		        {
		        	shopName=rst2.getString("shop_name");
		        	shopLogo=rst2.getString("shop_logo");
		        	cono=rst2.getString("counter_no");
		        	
		        }
		        int c=1;
		        try {
		        	c = Integer.parseInt(cono);
		        }
		        catch(Exception ex)
		        {
		        	c=0;
		        }
	         conn.close();
 			JRBeanCollectionDataSource  itemsJRBean = new JRBeanCollectionDataSource(listitems);
			
			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("CollectionBeanParam", itemsJRBean);
			parameters.put("CounterNo",c);
			parameters.put("BillNo",bill_no);
			parameters.put("CustomerName",cust_name.getText());
			
			
			parameters.put("TotalItems",itemCounter);
			parameters.put("TotalPrice",grand_total);
			parameters.put("TotalPay",(int)grand_total);
			parameters.put("shopname", shopName);
			parameters.put("shoplogo", shopLogo);
			
			
			
			InputStream input = new FileInputStream(new File("C:\\Users\\Acer Aspire 5\\JaspersoftWorkspace\\MyReports\\TableInvoice.jrxml"));
			
			JasperDesign jasperdesign = JRXmlLoader.load(input);
			
			JasperReport jasperreport = JasperCompileManager.compileReport(jasperdesign);
			
			JasperPrint jasperprint = JasperFillManager.fillReport(jasperreport,parameters,new JREmptyDataSource());
			
			//JasperViewer.viewReport(jasperprint,false);
			JasperPrintManager.printReport(jasperprint, true);
			clearVariables();
			clearFields();

			
			
		}
		else
		{
	      	   Alert alert = new Alert(AlertType.ERROR);
	    	   alert.setTitle("StarPoS");
	    	   alert.setContentText("You must enter correct details in specified fields\nAnd make sure that the Cart is not Empty");
	    	   alert.setHeaderText(null);
	    	   alert.show(); 
		}
	}
	
	
	public void printPreviewClicked(ActionEvent ae2) throws Exception
	{
		String shopName="";
		String shopLogo="";
		String cono="";
		if(grand_total>0)
		{
		  	 int bill_no = 1;
		     Class.forName("org.h2.Driver");
		     conn = DriverManager.getConnection(url,username,password);
	         ResultSet rst = conn.createStatement().executeQuery("select max(bill_no) from customer_table");
	         if(rst.next())
	         {
	        	 bill_no=rst.getInt("max(bill_no)");
	        	 bill_no=bill_no+1;
	         }
	        
	        ResultSet rst2 = conn.createStatement().executeQuery( "select shop_name,shop_logo,counter_no from info_table");
	        if(rst2.next())
	        {
	        	shopName=rst2.getString("shop_name");
	        	shopLogo=rst2.getString("shop_logo");
	        	cono=rst2.getString("counter_no");
	        }
	        
	        int c=1;
	        try {
	        	c = Integer.parseInt(cono);
	        }
	        catch(Exception ex)
	        {
	        	c=0;
	        }
	         conn.close();
			JRBeanCollectionDataSource  itemsJRBean = new JRBeanCollectionDataSource(listitems);
			
			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("CollectionBeanParam", itemsJRBean);
			parameters.put("CounterNo",c);
			parameters.put("BillNo",bill_no);
			parameters.put("CustomerName",cust_name.getText());
			
			
			parameters.put("TotalItems",itemCounter);
			parameters.put("TotalPrice",grand_total);
			parameters.put("TotalPay",(int)grand_total);
			parameters.put("shopname", shopName);
			parameters.put("shoplogo", shopLogo);
			
			
			
			InputStream input = new FileInputStream(new File("C:\\Users\\Acer Aspire 5\\JaspersoftWorkspace\\MyReports\\TableInvoice.jrxml"));
			
			JasperDesign jasperdesign = JRXmlLoader.load(input);
			
			JasperReport jasperreport = JasperCompileManager.compileReport(jasperdesign);
			
			JasperPrint jasperprint = JasperFillManager.fillReport(jasperreport,parameters,new JREmptyDataSource());
			
			JasperViewer.viewReport(jasperprint,false);	
		}
		else
		{
	      	   Alert alert = new Alert(AlertType.ERROR);
	    	   alert.setTitle("S-PoS");
	    	   alert.setContentText("Cart is Empty!!");
	    	   alert.setHeaderText(null);
	    	   alert.show(); 
		}

		
	}
	
	public void namecheck(KeyEvent ket) throws Exception
	{
		System.out.println("keyEvent");
		String number = cust_mob_no.getText();
		 if(number.matches(pattern))
		 {			
				 if(ket.getCode().equals(KeyCode.ENTER))
				 {
				  	 String query = "select customer_name from customer_table where customer_mobile_no=?;";
				     Class.forName("org.h2.Driver");
				     conn = DriverManager.getConnection(url,username,password);
			         PreparedStatement st = conn.prepareStatement(query);
		    	     st.setString(1,number);
			         ResultSet rs = st.executeQuery();
			         if(rs.next())
			         {
			        	 custname = rs.getString("customer_name");
			        	 cust_name.setText(custname);
			        	 st.close();
			 	         conn.close();
			         }
			         else
			         {
			           System.out.println("Record not found!!");
			      	   Alert alert = new Alert(AlertType.ERROR);
			    	   alert.setTitle("StarPoS");
			    	   alert.setContentText("Record Not Found!!!\nYou can enter the name of customer and generate the invoice");
			    	   alert.setHeaderText(null);
			    	   alert.show(); 
			         }
				 }				 			 	 
		 }
	}
	
	public void minusClicked(ActionEvent ae4)
	{
		ShopItems si = null;
		String name = null;
		System.out.println("minus clicked");
		try
		{
			si = billTableView.getSelectionModel().getSelectedItem();
			System.out.println(si);
			name= si.getItem_name();
		}
		catch(Exception exec)
		{
			System.out.println("Select row first!!");
		}

		if(si!=null)
		{
			for(int i = 0;i<listitems.size();i++)
		    {
		    	if(listitems.get(i).getItemname().equals(name))
		    	{
		    		int qty = listitems.get(i).getItemqty();
		    		double totalitemprice = listitems.get(i).getItemtotal();
		    		double itemprice = listitems.get(i).getItemprice();
		    		if(qty>1)
		    		{
		    			qty--;
		    			listitems.get(i).setItemqty(qty);
		    			listitems.get(i).setItemtotal(totalitemprice-itemprice);
		    			//list
		    			list.get(i).setItem_qty(qty);
		    			//si.setItem_qty(qty);
		    			int index = billTableView.getSelectionModel().getSelectedIndex();
		    			billTableView.getItems().set(index, new ShopItems(si.getSerial_no(),si.getItem_name(),qty,si.getItem_unit_price(),totalitemprice-itemprice));
		    			//billTableView.getSelectionModel().getSelectedItem().setItem_total_price(totalitemprice-itemprice);
		    			
		    			itemCounter--;
		    			grand_total = grand_total-itemprice;
		    			labelTotal.setText(String.valueOf(grand_total));
		    			break;
		    		}
		    		else
		    		{
		    			listitems.remove(i);
		    			list.remove(i);
		    			billTableView.getItems().removeAll(billTableView.getSelectionModel().getSelectedItems());
		    			table_serial_no_counter--;
		    			itemCounter--;
		    			grand_total = grand_total-itemprice;
		    			labelTotal.setText(String.valueOf(grand_total));
			    		break;
		    		}
		    	}
		    }	
			
		}
		else
		{
			System.out.println("Select row first!!");
		}
	
		
		
	}
	
	public void cancelClicked(ActionEvent ae)
	{
		clearVariables();
		clearFields();
	}
	
	public void clearVariables()
	{
		grand_total = 0.0;
		table_serial_no_counter = 0;
		itemtot = 0.0;
		itemCounter = 0;
	}
	
	public void clearFields()
	{
	     labelTotal.setText("");
		 radiobuttonCash.setSelected(true);
		 radiobuttonUPI.setSelected(false);
		 radiobuttonCD.setSelected(false);
		 item_name_invoice.setText("");
		
		 cust_mob_no.setText("");
		 cust_name.setText("");
		 billTableView.getItems().clear();
	}
	
	
			
}