package cn.sunxianlei.tdgis.vo;

import android.R.integer;
import android.R.string;

public class MenuItem {
	private String menuName;
	private int menuIcon;
	
	public MenuItem(String menuName, int menuIcon)
	{
		this.menuName = menuName;
		this.menuIcon = menuIcon;
	}
	
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public int getMenuIcon() {
		return menuIcon;
	}
	public void setMenuIcon(int menuIcon) {
		this.menuIcon = menuIcon;
	}
	
	
}
