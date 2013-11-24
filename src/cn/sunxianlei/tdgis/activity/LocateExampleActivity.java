package cn.sunxianlei.tdgis.activity;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnStatusChangedListener.STATUS;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;

import cn.sunxianlei.tdgis.layer.MBTilesGoogleMapsOfflineLayer;
import cn.sunxianlei.tdgis.util.Location;
import cn.sunxianlei.tdgis.util.Util;
import cn.sunxianlei.tdgis.R;
import cn.sunxianlei.tdgis.R.id;
import cn.sunxianlei.tdgis.R.layout;
import cn.sunxianlei.tdgis.util.Config;
import android.app.Activity;
import android.app.Service;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Process;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LocateExampleActivity extends Activity {
	private MapView mapView=null;
	//private ArcGISLocalTiledLayer localTiledLayer;
	private MBTilesGoogleMapsOfflineLayer googleLayer;
	
	private TextView locationInfoTextView=null;
	private Button startButton;
	private LocationClient locationClient;
	private boolean  mIsStart;
	private static int count = 1;
	private GraphicsLayer gLayer;
	
	public static String TAG="LocateExample";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locate);
		
		mapView=(MapView)findViewById(R.id.mapviewInLocateActivity);
		//localTiledLayer=new ArcGISLocalTiledLayer(Config.LOCATE_BASEMAP_PATH);
		//mapView.addLayer(localTiledLayer);
		googleLayer=new MBTilesGoogleMapsOfflineLayer(getApplicationContext());
		mapView.addLayer(googleLayer);
		gLayer=new GraphicsLayer();
		mapView.addLayer(gLayer);
		
		locationInfoTextView=(TextView)findViewById(R.id.locationInfoTextViewInLocateActivity);
		startButton=(Button)findViewById(R.id.startBtnInLocateActivity);
		mIsStart = false;
		
		locationClient = ((Location)getApplication()).mLocationClient;
		((Location)getApplication()).mTv = locationInfoTextView;
		((Location)getApplication()).mapView=mapView;
		
		startButton.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mIsStart) {
					setLocationOption();
					locationClient.start();
					startButton.setText("停止定位");
					mIsStart = true;

				} else {
					locationClient.stop();
					mIsStart = false;
					startButton.setText("开始定位");
				} 
				Log.d(TAG, "... mStartBtn onClick... pid="+Process.myPid()+" count="+count++);
			}
		});
		
		mapView.setOnLongPressListener(new OnLongPressListener() {
			
			@Override
			public boolean onLongPress(float x, float y) {
				// TODO Auto-generated method stub
				
				if (mapView.isLoaded()) {
					
					Point pt=mapView.toMapPoint(x,y);
					String text="X:"+pt.getX()+"Y:"+pt.getY();
					Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
					
					gLayer.removeAll();
					SimpleMarkerSymbol smsMarkerSymbol=new SimpleMarkerSymbol(Color.RED, 20, STYLE.CROSS);
					Graphic graphic=new Graphic(pt, smsMarkerSymbol);
					gLayer.addGraphic(graphic);
					
					return true;
					
				}
				return false;
			}
		});

	}
	
	//设置相关参数
		private void setLocationOption(){
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true);				//打开gps
			option.setCoorType("gcj02");		//设置坐标类型
			option.setServiceName("com.baidu.location.service_v2.9");	
			option.setAddrType("all");		
			option.setScanSpan(10000);
			option.setPriority(LocationClientOption.GpsFirst);
			option.disableCache(true);		
			locationClient.setLocOption(option);
		}
		
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		locationClient.stop();
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		LocateExampleActivity.this.finish();
	}
	
}
