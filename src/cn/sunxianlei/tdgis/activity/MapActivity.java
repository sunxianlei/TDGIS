package cn.sunxianlei.tdgis.activity;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;

import cn.sunxianlei.tdgis.R;
import android.app.Activity;
import android.os.Bundle;

public class MapActivity extends Activity {

	MapView mapView=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		mapView=(MapView)findViewById(R.id.mapviewInMapActivity);
		mapView.addLayer(new ArcGISTiledMapServiceLayer("" + 
				"http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer"));
	}
	protected void onPause() {
		super.onPause();
		mapView.pause();
	}

	protected void onResume() {
		super.onResume();
		mapView.unpause();
	}

}
