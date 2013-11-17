package cn.sunxianlei.tdgis;

import java.util.ArrayList;
import java.util.List;

import cn.sunxianlei.tdgis.activity.AboutActivity;
import cn.sunxianlei.tdgis.activity.GridExampleActivity;
import cn.sunxianlei.tdgis.activity.LocateExampleActivity;
import cn.sunxianlei.tdgis.activity.MapActivity;
import cn.sunxianlei.tdgis.activity.VectorExampleActivity;
import cn.sunxianlei.tdgis.adapter.MenuGridAdapter;
import cn.sunxianlei.tdgis.vo.MenuItem;

import android.os.Bundle;
import android.R.anim;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
	private GridView menuGridView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		menuGridView=(GridView)findViewById(R.id.mainMenuGridView);
		List<MenuItem> menuItems = new ArrayList<MenuItem>();
		menuItems.add(new MenuItem("矢量演示",R.drawable.menuvector));
		menuItems.add(new MenuItem("定位演示",R.drawable.menulocate));
		menuItems.add(new MenuItem("栅格演示",R.drawable.menugrid));
		menuItems.add(new MenuItem("地图",R.drawable.menumap));
		menuItems.add(new MenuItem("关于",R.drawable.menuabout));
		
		MenuGridAdapter adapter = new MenuGridAdapter(this);
		
		adapter.setMenuItems(menuItems);
		menuGridView.setAdapter(adapter);
		menuGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				try {
					if (getClassByPosition(position) != null)
					{
						startActivity(new Intent(MainActivity.this, getClassByPosition(position)));
						overridePendingTransition(anim.slide_in_left, anim.slide_out_right);
					}else {
						Toast.makeText(MainActivity.this, "该功能尚未开放", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
			}
			
		});
	}
	private Class<?> getClassByPosition(int position) {
		Class<?> startClass = null;
		switch (position) {
		case 0:
			startClass = VectorExampleActivity.class;
			break;
		case 1:
			startClass = LocateExampleActivity.class;
			break;
		case 2:
			startClass = GridExampleActivity.class;
			break;
		case 3:
			startClass = MapActivity.class;
			break;
		case 4:
			startClass = AboutActivity.class;
			break;
		default:
			//startClass = VersionInfoActivity.class;
			break;
		}
		return startClass;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
