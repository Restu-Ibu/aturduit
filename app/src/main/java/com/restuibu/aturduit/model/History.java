package com.restuibu.aturduit.model;

public class History {
	private String tanggal;
	private String total;
	private String jumlah;
	private String total_food;
	private String total_transport;
	private String total_entertain;
	private String total_groceries;
	private String total_other;

	public History(String tanggal, String total, String jumlah) {
		this.tanggal = tanggal;
		this.total = total;
		this.jumlah = jumlah;
	}

	public History(String tanggal, String total, String jumlah, String total_food, String total_transport, String total_entertain, String total_groceries, String total_other) {
		this.tanggal = tanggal;
		this.total = total;
		this.jumlah = jumlah;
		this.total_food = total_food;
		this.total_transport = total_transport;
		this.total_entertain = total_entertain;
		this.total_groceries = total_groceries;
		this.total_other = total_other;
	}

	public void setTotal_food(String total_food) {
		this.total_food = total_food;
	}

	public void setTotal_transport(String total_transport) {
		this.total_transport = total_transport;
	}

	public void setTotal_entertain(String total_entertain) {
		this.total_entertain = total_entertain;
	}

	public void setTotal_groceries(String total_groceries) {
		this.total_groceries = total_groceries;
	}

	public void setTotal_other(String total_other) {
		this.total_other = total_other;
	}

	public String getTotal_food() {
		return total_food;
	}

	public String getTotal_transport() {
		return total_transport;
	}

	public String getTotal_entertain() {
		return total_entertain;
	}

	public String getTotal_groceries() {
		return total_groceries;
	}

	public String getTotal_other() {
		return total_other;
	}

	public String getTanggal() {
		return tanggal;
	}

	public void setTanggal(String tanggal) {
		this.tanggal = tanggal;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getJumlah() {
		return jumlah;
	}

	public void setJumlah(String jumlah) {
		this.jumlah = jumlah;
	}

}
