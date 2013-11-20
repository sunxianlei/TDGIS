package cn.sunxianlei.tdgis.activity;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;

import cn.sunxianlei.tdgis.R;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

public class MapActivity extends Activity {

	MapView mapView=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		//http://192.168.1.150:7080/PBS/rest/services/googlemaphhu/MapServer
		//http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer
		mapView=(MapView)findViewById(R.id.mapviewInMapActivity);
		mapView.addLayer(new ArcGISTiledMapServiceLayer("" + 
				"http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer"));
		
		mapView.setOnLongPressListener(new OnLongPressListener() {
			
			@Override
			public boolean onLongPress(float x, float y) {
				// TODO Auto-generated method stub
				if (mapView.isLoaded()) {
					Point pt=mapView.toMapPoint(x,y);
					String text="X:"+pt.getX()+"Y:"+pt.getY();
					Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
					return true;
				}
				return false;
			}
		});
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
