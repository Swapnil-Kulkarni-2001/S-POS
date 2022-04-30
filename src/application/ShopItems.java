package application;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ShopItems {
	private SimpleIntegerProperty serial_no;
	private SimpleStringProperty item_name;
	private SimpleIntegerProperty item_qty;
	private SimpleDoubleProperty item_unit_price;
	private SimpleDoubleProperty item_total_price;
	
	public ShopItems(Integer serial_no, String item_name, Integer item_qty, Double item_unit_price, Double item_total_price) {
		super();
		this.serial_no = new SimpleIntegerProperty(serial_no);
		this.item_name = new SimpleStringProperty(item_name);
		this.item_qty = new SimpleIntegerProperty(item_qty);
		this.item_unit_price = new SimpleDoubleProperty(item_unit_price);
		this.item_total_price = new SimpleDoubleProperty(item_total_price);
	}
	
	public Double getItem_total_price() {
		return item_total_price.get();
	}

	public void setItem_total_price(Double item_total_price) {
		this.item_total_price = new SimpleDoubleProperty(item_total_price);
	}

	public Integer getSerial_no() {
		return serial_no.get();
	}
	public void setSerial_no(Integer serial_no) {
		this.serial_no = new SimpleIntegerProperty(serial_no);
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
	public Double getItem_unit_price() {
		return item_unit_price.get();
	}
	public void setItem_unit_price(Double item_unit_price) {
		this.item_unit_price = new SimpleDoubleProperty(item_unit_price);
	}
	
	

	

}
