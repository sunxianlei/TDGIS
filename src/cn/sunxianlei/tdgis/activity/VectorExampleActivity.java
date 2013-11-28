package cn.sunxianlei.tdgis.activity;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.core.geometry.Point;

import cn.sunxianlei.tdgis.R;
import cn.sunxianlei.tdgis.layer.GoogleMapsOnlineLayer;
import cn.sunxianlei.tdgis.layer.MBTilesGoogleMapsOfflineLayer;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

public class VectorExampleActivity extends Activity {

	private MapView mapView=null;
	TabHost mTabHost;
	LinearLayout buttonContainer;
	
	Button newButton;
	
	Button editButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vector);
		
		mTabHost = (TabHost) findViewById(R.id.tabHost);
		mTabHost.setup();

		TabHost.TabSpec editSpec = mTabHost.newTabSpec("Edit");
		editSpec.setContent(R.id.offlinereditlayout);
		editSpec.setIndicator("±à¼­");
		mTabHost.addTab(editSpec);

		TabHost.TabSpec syncSpec = mTabHost.newTabSpec("Sync");
		syncSpec.setContent(R.id.offlinersynclayout);
		syncSpec.setIndicator("Í¬²½");
		mTabHost.addTab(syncSpec);
		
		mapView = ((MapView) findViewById(R.id.map));
		MBTilesGoogleMapsOfflineLayer mbTilesGoogleMapsOfflineLayer=new MBTilesGoogleMapsOfflineLayer(getApplicationContext());
		mapView.addLayer(mbTilesGoogleMapsOfflineLayer);
		
		buttonContainer=(LinearLayout)findViewById(R.id.editButtonContainer);
		
		newButton=(Button)findViewById(R.id.editButtonInVectorActivity);
		newButton.setOnClickListener(newButtonClickListener);
		editButton=(Button)findViewById(R.id.editButtonInButtonContainer);
		editButton.setOnClickListener(editButtonClickListener);
	}
	
	private OnClickListener newButtonClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			buttonContainer.setVisibility(View.VISIBLE);
			editButton.setEnabled(true);
		}
	};
	
	private OnClickListener editButtonClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			buttonContainer.setVisibility(View.GONE);
		}
	};

}

