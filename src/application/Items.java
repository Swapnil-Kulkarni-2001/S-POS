package application;

public class Items {
	
	public String itemname;
	public int itemqty;
	public double itemprice;
	
	
	
	public Items(String itemname, int itemqty, double itemprice, double itemtotal) {
		super();
		this.itemname = itemname;
		this.itemqty = itemqty;
		this.itemprice = itemprice;
		this.itemtotal = itemtotal;
	}
	
	public String getItemname() {
		return itemname;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	public int getItemqty() {
		return itemqty;
	}
	public void setItemqty(int itemqty) {
		this.itemqty = itemqty;
	}
	public double getItemprice() {
		return itemprice;
	}
	public void setItemprice(double itemprice) {
		this.itemprice = itemprice;
	}
	public double getItemtotal() {
		return itemtotal;
	}
	public void setItemtotal(double itemtotal) {
		this.itemtotal = itemtotal;
	}
	public double itemtotal;
	

	
	
	
	

}

