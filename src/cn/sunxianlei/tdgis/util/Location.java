package cn.sunxianlei.tdgis.util;

import java.util.HashMap;
import java.util.Map;

import android.R.drawable;
import android.app.Application;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Process;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import cn.sunxianlei.tdgis.R;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnStatusChangedListener.STATUS;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;

public class Location extends Application {

	public LocationClient mLocationClient = null;
	public GeofenceClient mGeofenceClient;
	private String mData;
	public MyLocationListenner myListener = new MyLocationListenner();
	public TextView mTv;
	public NotifyLister mNotifyer=null;
	public Vibrator mVibrator01;
	public static String TAG = "LocTestDemo";
	
	public MapView mapView=null;
	public Point pinPoint=null;
	private GraphicsLayer gLayer;
	@Override
	public void onCreate() {
		mLocationClient = new LocationClient(this);
		mLocationClient.setAK(Config.BAIDU_AK);
		mLocationClient.registerLocationListener( myListener );
		mGeofenceClient = new GeofenceClient(this);
		
		super.onCreate(); 
		Log.d(TAG, "... Application onCreate... pid=" + Process.myPid());
		gLayer=new GraphicsLayer();
	}
	
	/**
	 * 鏄剧ず璇锋眰瀛楃涓�
	 * @param str
	 */
	public void logMsg(String str) {
		try {
			mData = str;
			if ( mTv != null )
				mTv.setText(mData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 鐩戝惉鍑芥暟锛屾湁鏇存柊浣嶇疆鐨勬椂鍊欙紝鏍煎紡鍖栨垚瀛楃涓诧紝杈撳嚭鍒板睆骞曚腑
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return ;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			//sb.append("\nerror code : ");
			//sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				//sb.append("\naddr : ");
				//sb.append(location.getAddrStr());
			}
			//sb.append("\nsdk version : ");
			//sb.append(mLocationClient.getVersion());
			sb.append("\nisCellChangeFlag : ");
			sb.append(location.isCellChangeFlag());
			logMsg(sb.toString());
			Log.i(TAG, sb.toString());
			if (checkLocationReasonable(location) && (mapView!=null)) {
				
				Point pt=new Point(location.getLongitude(),location.getLatitude());
				Point ptMercator=Util.GeographicToWebMercator(pt);
				
				gLayer.removeAll();
				//SimpleMarkerSymbol smsMarkerSymbol=new SimpleMarkerSymbol(Color.RED, 15, STYLE.CROSS);
				Drawable pinImage=getApplicationContext().getResources().getDrawable(R.drawable.pin_red);
				PictureMarkerSymbol symbol=new PictureMarkerSymbol(pinImage);
				Graphic graphic=new Graphic(ptMercator, symbol);
				Log.i(TAG, ptMercator.getX()+","+ptMercator.getY());
				gLayer.addGraphic(graphic);
				mapView.addLayer(gLayer);
				mapView.centerAt(ptMercator, true);
			}
		}
		
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null){
				return ; 
			}
			StringBuffer sb = new StringBuffer(256);
			sb.append("Poi time : ");
			sb.append(poiLocation.getTime());
			//sb.append("\nerror code : "); 
			//sb.append(poiLocation.getLocType());
			sb.append("\nlatitude : ");
			sb.append(poiLocation.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(poiLocation.getLongitude());
			sb.append("\nradius : ");
			sb.append(poiLocation.getRadius());
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
				//sb.append("\naddr : ");
				//sb.append(poiLocation.getAddrStr());
			} 
			if(poiLocation.hasPoi()){
				sb.append("\nPoi:");
				sb.append(poiLocation.getPoi());
			}else{				
				sb.append("noPoi information");
			}
			logMsg(sb.toString());
		}
	}
	
	public class NotifyLister extends BDNotifyListener{
		public void onNotify(BDLocation mlocation, float distance){
			mVibrator01.vibrate(1000);
		}
	}
	private boolean checkLocationReasonable(BDLocation location) {
		// TODO Auto-generated method stub
		if (location.getLatitude()<1) {
			return false;
		}
		if (location.getLongitude()<1) {
			return false;
		}
		if(location.getLatitude()>180){
			return false;
		}
		if(location.getLongitude()>180){
			return false;
		}
		return true;
	}
	
}