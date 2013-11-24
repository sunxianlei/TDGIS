package cn.sunxianlei.tdgis.activity;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnStatusChangedListener.STATUS;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;

import cn.sunxianlei.tdgis.R;
import cn.sunxianlei.tdgis.layer.GoogleMapsOnlineLayer;
import cn.sunxianlei.tdgis.layer.MBTilesGoogleMapsOfflineLayer;
import cn.sunxianlei.tdgis.util.Config;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

public class GridExampleActivity extends Activity {

	private MapView mapView=null;
	private ArcGISLocalTiledLayer localTiledLayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grid);
		mapView=(MapView)findViewById(R.id.mapviewInGridActivity);
		//localTiledLayer=new ArcGISLocalTiledLayer(Config.LOCATE_BASEMAP_PATH);
		//mapView.addLayer(localTiledLayer);
		
		//GoogleMapsOnlineLayer googleMapsLayer=new GoogleMapsOnlineLayer(GoogleMapsOnlineLayer.MapType.VECTOR, null, true);
		//mapView.addLayer(googleMapsLayer);
		MBTilesGoogleMapsOfflineLayer mbTilesGoogleMapsOfflineLayer=new MBTilesGoogleMapsOfflineLayer(getApplicationContext());
		mapView.addLayer(mbTilesGoogleMapsOfflineLayer);
		
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
		mapView.setOnStatusChangedListener(new OnStatusChangedListener() {
			
			@Override
			public void onStatusChanged(Object source, STATUS status) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
