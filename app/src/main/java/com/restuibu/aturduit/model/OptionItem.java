package com.restuibu.aturduit.model;

public class OptionItem {
	private String name;
	private int img;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getImg() {
		return img;
	}

	public void setImg(int img) {
		this.img = img;
	}

	public OptionItem(String name, int img) {
		super();
		this.name = name;
		this.img = img;
	}
}
