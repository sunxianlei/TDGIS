package cn.sunxianlei.tdgis.activity;

import com.esri.android.map.MapView;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.core.geometry.Point;

import cn.sunxianlei.tdgis.R;
import cn.sunxianlei.tdgis.layer.GoogleMapsOnlineLayer;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class VectorExampleActivity extends Activity {

	private MapView mapView=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vector);
		mapView=(MapView)findViewById(R.id.mapviewInVectorActivity);
		
		GoogleMapsOnlineLayer googleMapsLayer=new GoogleMapsOnlineLayer(GoogleMapsOnlineLayer.MapType.VECTOR, null, true);
		mapView.addLayer(googleMapsLayer);
		
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

}
