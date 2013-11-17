package cn.sunxianlei.tdgis.adapter;

import java.util.List;

import cn.sunxianlei.tdgis.R;
import cn.sunxianlei.tdgis.vo.MenuItem;
import cn.sunxianlei.tdgis.vo.MenuItemHolder;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuGridAdapter extends BaseAdapter {
	private Context context;
	private List<MenuItem> menuItems;
	private LayoutInflater mInflater;
	
	/**
	 *
	 * @param context
	 */
	public MenuGridAdapter(Context context){
		super();
		this.context = context;
	}
	
	public List<MenuItem> getMenuItems() {
		return menuItems;
	}

	public void setMenuItems(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return menuItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return menuItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MenuItemHolder holder;
		
		try {
			if (convertView == null)
			{
				convertView = mInflater.inflate(R.layout.menu_item, null);
				holder = new MenuItemHolder();
				holder.appIcon = (ImageView)convertView.findViewById(R.id.menuItemImageView);
				holder.appName = (TextView)convertView.findViewById(R.id.menuItemNameTextView);
				convertView.setTag(holder);
			}else {
				holder = (MenuItemHolder)convertView.getTag();
			}
			
			MenuItem menuItem = menuItems.get(position);
			if (menuItem != null)
			{
				holder.appName.setText(menuItem.getMenuName());
				holder.appIcon.setBackgroundResource(menuItem.getMenuIcon());
			}
			return convertView;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
}
