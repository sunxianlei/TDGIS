package cn.sunxianlei.tdgis.activity;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;

import cn.sunxianlei.tdgis.R;
import android.app.Activity;
import android.os.Bundle;

public class LocateExampleActivity extends Activity {
	MapView mapView=null;
	ArcGISLocalTiledLayer localTiledLayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locate);
		
		mapView=(MapView)findViewById(R.id.mapviewInLocateActivity);
		localTiledLayer=new ArcGISLocalTiledLayer("file://mnt/sdcard/layers");
		mapView.addLayer(localTiledLayer);
		
	}

}
