package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class DashBoard1Controller implements Initializable {

	public String url="jdbc:h2:~/PosDB";
	public String username="sa";
	public String password="";
	public String query;
	public Connection conn;
	public java.util.Date cd = new java.util.Date();
	public java.sql.Date sqlDate;
	
	@FXML public BorderPane borderpane;
	@FXML public LineChart<String,Number> lineRevenueChart;
	@FXML public BarChart<String,Number> salesbarChart;
	@FXML public Label sales;
	@FXML public Label productsold;
	@FXML public Label activeproducts;
	@FXML public Label todaysrevenue;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		sqlDate = new java.sql.Date(cd.getTime());
		query="select count(*) from customer_table where bill_date=?";
		try {
		    Class.forName("org.h2.Driver");
	        conn = DriverManager.getConnection(url,username,password);
	        PreparedStatement pst0 = conn.prepareStatement(query);
	        pst0.setDate(1, sqlDate);
	        ResultSet rset = pst0.executeQuery();	        
	        if(rset.next())
	        {
	        	sales.setText(String.valueOf(rset.getInt("count(*)")));
	        	
	        }
	        query = "select sum(times_sold) from sales_table where sold_date = ?";
	        PreparedStatement pst = conn.prepareStatement(query);
	        pst.setDate(1, sqlDate);
	        rset =  pst.executeQuery();
	        if(rset.next())
	        {
	        	productsold.setText(String.valueOf(rset.getInt("sum(times_sold)")));
	        }
	        query = "select count(*) from items_table";
	        rset = conn.createStatement().executeQuery(query);
	        if(rset.next())
	        {
	        	activeproducts.setText(String.valueOf(rset.getInt("count(*)")));
	        	
	        }
	        
	        query = "select sum(customer_bill) from customer_table where bill_date=?";
	        PreparedStatement pst1 = conn.prepareStatement(query);
	        pst1.setDate(1, sqlDate);
	        rset =  pst1.executeQuery();
	        if(rset.next())
	        {
	        	todaysrevenue.setText(String.valueOf(rset.getInt("sum(customer_bill)")));
	        }
	        conn.close();
	        
	        
	        
		}catch(Exception exxx) {
			System.out.println(exxx);
		}
		
		
		XYChart.Series<String,Number> series = new XYChart.Series<String,Number>();
		XYChart.Series<String,Number> series1 = new XYChart.Series<String,Number>();
		/*series.getData().add(new XYChart.Data<String, Number>("Jan",200));
		series.getData().add(new XYChart.Data<String, Number>("Feb",500));
		series.getData().add(new XYChart.Data<String, Number>("Mar",300));
		series.getData().add(new XYChart.Data<String, Number>("Apr",600));
		series.getData().add(new XYChart.Data<String, Number>("May",600));
		series.getData().add(new XYChart.Data<String, Number>("Jun",650));
		series.getData().add(new XYChart.Data<String, Number>("Jul",1000));
		series.getData().add(new XYChart.Data<String, Number>("Aug",884));
		series.getData().add(new XYChart.Data<String, Number>("Sep",967));
		series.getData().add(new XYChart.Data<String, Number>("Oct",760));
		series.getData().add(new XYChart.Data<String, Number>("Nov",689));
		series.getData().add(new XYChart.Data<String, Number>("Dec",2000));*/
		
		try {
		    Class.forName("org.h2.Driver");
	        conn = DriverManager.getConnection(url,username,password);
	        query="select sum(customer_bill) from customer_table where monthname(bill_date)=? and year(bill_date)=?";
	        PreparedStatement st = conn.prepareStatement(query);
	        int year = Calendar.getInstance().get(Calendar.YEAR);


	        //jan
	       /* String date1 = year+"-"+"01"+"-"+"01";
	        st.setString(1, date1);
	        String date2 = year+"-"+"02"+"-"+"01";
	        st.setString(2, date2);
	        ResultSet rset =  st.executeQuery();
	        if(rset.next())
	        {
	        	series.getData().add(new XYChart.Data<String, Number>("Jan",rset.getInt("sum(customer_bill)")));
	        }
	        
	        //feb
	        query="select sum(customer_bill) from customer_table where bill_date between ? and ?";
	         st = conn.prepareStatement(query);
	         year = Calendar.getInstance().get(Calendar.YEAR);


	         date1 = year+"-"+"02"+"-"+"01";
	         st.setString(1, date1);
	         date2 = year+"-"+"03"+"-"+"01";
	         st.setString(2, date2);
	         rset =  st.executeQuery();
	        if(rset.next())
	        {
	        	series.getData().add(new XYChart.Data<String, Number>("Feb",rset.getInt("sum(customer_bill)")));
	        }*/
	        
	        //using loop
	        String month="";
	        int i;
	        for(i=1;i<=12;i++)
	        {
	        	switch(i)
	        	{
	        	case 1:month="January";
	        	       break;
	        	case 2:month="February";
     	               break;
	        	case 3:month="March";
     	               break;
	        	case 4:month="April";
     	               break;
	        	case 5:month="May";
     	               break;
	        	case 6:month="June";
     	               break;
	        	case 7:month="July";
     	               break;
	        	case 8:month="August";
     	               break;
	        	case 9:month="September";
     	               break;
	        	case 10:month="October";
     	               break;
	        	case 11:month="November";
     	               break;
	        	case 12:month="December";
     	               break;       
	        	}
	        	st.setString(1, month);
	        	st.setInt(2, year);
	 	        ResultSet rset =  st.executeQuery();
	 	        if(rset.next())
	 	        {
	 	        	series.getData().add(new XYChart.Data<String, Number>(month,rset.getInt("sum(customer_bill)")));
	 	        }
	 	        
	        }
	        
	        
	        
	        query="select count(*) from customer_table where monthname(bill_date)=? and year(bill_date)=?";
	        st=conn.prepareStatement(query);
	        
	        month="";
	        for(i=1;i<=12;i++)
	        {
	        	switch(i)
	        	{
	        	case 1:month="January";
	        	       break;
	        	case 2:month="February";
     	               break;
	        	case 3:month="March";
     	               break;
	        	case 4:month="April";
     	               break;
	        	case 5:month="May";
     	               break;
	        	case 6:month="June";
     	               break;
	        	case 7:month="July";
     	               break;
	        	case 8:month="August";
     	               break;
	        	case 9:month="September";
     	               break;
	        	case 10:month="October";
     	               break;
	        	case 11:month="November";
     	               break;
	        	case 12:month="December";
     	               break;       
	        	}
	        	st.setString(1, month);
	        	st.setInt(2, year);
	 	        ResultSet rset =  st.executeQuery();
	 	        if(rset.next())
	 	        {
	 	        	series1.getData().add(new XYChart.Data<String, Number>(month,rset.getInt("count(*)")));
	 	        }
	 	        
	        }
	
			conn.close();
		}catch(Exception exe) {
			System.err.println(exe);
		}


		series.setName("Revenue");	
		series1.setName("Sales");
		lineRevenueChart.getData().add(series);
		salesbarChart.getData().add(series1);
		
	}
	
	

}
