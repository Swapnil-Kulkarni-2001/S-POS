package application;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Table2Items {
	private SimpleStringProperty item_barcode;
	private SimpleStringProperty item_name;
	private SimpleIntegerProperty item_qty;
	private SimpleDoubleProperty item_price;
	
	public Table2Items(String item_barcode, String item_name, Integer item_qty, Double item_price ) {
		super();
		this.item_barcode = new SimpleStringProperty(item_barcode);
		this.item_name = new SimpleStringProperty(item_name);
		this.item_qty = new SimpleIntegerProperty(item_qty);
		this.item_price = new SimpleDoubleProperty(item_price);
	}
	
	


	public Table2Items() {
		super();
		// TODO Auto-generated constructor stub
	}




	public String getItem_barcode() {
		return item_barcode.get();
	}
	public void setItem_barcode(String item_barcode) {
		this.item_barcode = new SimpleStringProperty(item_barcode);
	}
	
	public String getItem_name() {
		return item_name.get();
	}
	public void setItem_name(String item_name) {
		this.item_name = new SimpleStringProperty(item_name);
	}
	public Integer getItem_qty() {
		return item_qty.get();
	}
	public void setItem_qty(Integer item_qty) {
		this.item_qty = new SimpleIntegerProperty(item_qty);
	}
	public Double getItem_price() {
		return item_price.get();
	}
	public void setItem_price(Double item_price) {
		this.item_price = new SimpleDoubleProperty(item_price);
	}
	

}
