package cn.sunxianlei.tdgis.activity;

import java.util.ArrayList;
import java.util.Date;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnStatusChangedListener.STATUS;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureEditResult;
import com.esri.core.map.FeatureTemplate;
import com.esri.core.map.FeatureType;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.Renderer;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.LineSymbol;
import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.core.symbol.SymbolHelper;

import cn.sunxianlei.tdgis.R;
import cn.sunxianlei.tdgis.layer.GoogleMapsOnlineLayer;
import cn.sunxianlei.tdgis.layer.MBTilesGoogleMapsOfflineLayer;
import cn.sunxianlei.tdgis.util.Legend;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class VectorExampleActivity extends Activity {

	protected static final String TAG = "VectorExampleActivity";
	private static final String CLOSE = "Close";
	private static final String EDIT_APPLIED_SUCCESSFULLY = "Edit applied successfully";
	private static final int POINT = 0;
	private static final int POLYLINE = 1;
	private static final int POLYGON = 2;
	
	private static final int DIALOG_FEATURE_TYPE = 0;
	private static final int DIALOG_EDIT_FAILED = 1;
	
	private MapView mapView=null;
	GraphicsLayer graphicsLayerEditing;
	TabHost mTabHost;
	LinearLayout buttonContainer;
	MyTouchListener myListener;
	
	
	Button newButton;
	
	Button editButton;
	Button removeButton;
	Button clearButton;
	Button cancelButton;
	Button undoButton;
	Button saveButton;
	
	ArrayList<Point> points = new ArrayList<Point>();
	ArrayList<Point> mpoints = new ArrayList<Point>();
	boolean midpointselected = false;
	boolean vertexselected = false;
	int insertingindex;
	int editingmode;
	protected String editingerrormessage;
	FeatureTemplate template;
	ArcGISFeatureLayer featurelayer;
	
	ArrayList<FeatureTemplate> templatelist;
	ArrayList<Legend> legendlist;
	ArrayList<ArcGISFeatureLayer> featurelayerlist;
	ArrayList<EditingStates> editingstates = new ArrayList<EditingStates>();
	
	SimpleMarkerSymbol redMarkerSymbol = new SimpleMarkerSymbol(Color.RED, 20,
			SimpleMarkerSymbol.STYLE.CIRCLE);
	SimpleMarkerSymbol blkMarkerSymbol = new SimpleMarkerSymbol(Color.BLACK,
			20, SimpleMarkerSymbol.STYLE.CIRCLE);
	SimpleMarkerSymbol grnMarkerSymbol = new SimpleMarkerSymbol(Color.GREEN,
			15, SimpleMarkerSymbol.STYLE.CIRCLE);
	
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
		//MBTilesGoogleMapsOfflineLayer mbTilesGoogleMapsOfflineLayer=new MBTilesGoogleMapsOfflineLayer(getApplicationContext());
		//mapView.addLayer(mbTilesGoogleMapsOfflineLayer);
		ArcGISTiledMapServiceLayer baseMap=new ArcGISTiledMapServiceLayer(
				"http://services.arcgisonline.com/ArcGIS/rest/services/ESRI_StreetMap_World_2D/MapServer");
		ArcGISFeatureLayer fl1 = new ArcGISFeatureLayer(
				"http://sampleserver5.arcgisonline.com/ArcGIS/rest/services/LocalGovernment/Recreation/FeatureServer/2",
				ArcGISFeatureLayer.MODE.ONDEMAND);
		fl1.setOnStatusChangedListener(statusChangedListener);
		ArcGISFeatureLayer fl2 = new ArcGISFeatureLayer(
				"http://sampleserver5.arcgisonline.com/ArcGIS/rest/services/LocalGovernment/Recreation/FeatureServer/0",
				ArcGISFeatureLayer.MODE.ONDEMAND);
		fl2.setOnStatusChangedListener(statusChangedListener);
		ArcGISFeatureLayer fl3 = new ArcGISFeatureLayer(
				"http://sampleserver5.arcgisonline.com/ArcGIS/rest/services/LocalGovernment/Recreation/FeatureServer/1",
				ArcGISFeatureLayer.MODE.ONDEMAND);
		fl3.setOnStatusChangedListener(statusChangedListener);
		mapView.addLayer(baseMap);
		mapView.addLayer(fl1);
		mapView.addLayer(fl2);
		mapView.addLayer(fl3);
		
		buttonContainer=(LinearLayout)findViewById(R.id.editButtonContainer);
		
		newButton=(Button)findViewById(R.id.editButtonInVectorActivity);
		newButton.setOnClickListener(newButtonClickListener);
		editButton=(Button)findViewById(R.id.editButtonInButtonContainer);
		editButton.setOnClickListener(editButtonClickListener);
		removeButton=(Button)findViewById(R.id.removeButtonInButtonContainer);
		removeButton.setOnClickListener(removeButtonClickListener);
		cancelButton=(Button)findViewById(R.id.cancelButtonInButtonContainer);
		cancelButton.setOnClickListener(cancelButtonClickListener);
		clearButton=(Button)findViewById(R.id.clearButtonInButtonContainer);
		clearButton.setOnClickListener(clearButtOnClickListener);
		undoButton=(Button)findViewById(R.id.undoButtonInButtonContainer);
		undoButton.setOnClickListener(undoButtonClickListener);
		saveButton=(Button)findViewById(R.id.saveButtonInButtonContainer);
		saveButton.setOnClickListener(saveButtonClickListener);
	}
	
	private OnStatusChangedListener statusChangedListener=new OnStatusChangedListener() {
		
		@Override
		public void onStatusChanged(Object source, STATUS status) {
			// TODO Auto-generated method stub
			editButton.setEnabled(status==STATUS.INITIALIZED?true:false);
		}
	};
	
	private OnClickListener newButtonClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			buttonContainer.setVisibility(View.VISIBLE);
			//editButton.setEnabled(true);
		}
	};
	
	private OnClickListener editButtonClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (myListener == null) {
				myListener = new MyTouchListener(VectorExampleActivity.this, mapView);
				mapView.setOnTouchListener(myListener);
			}
			showDialog(DIALOG_FEATURE_TYPE);
		}
	};
	
	private OnClickListener removeButtonClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (!vertexselected)
				points.remove(points.size() - 1); // remove last vertex
			else
				points.remove(insertingindex);
			midpointselected = false;
			vertexselected = false;
			editingstates.add(new EditingStates(points, midpointselected,
					vertexselected, insertingindex));
			refresh();
		}
	};
	
	private OnClickListener cancelButtonClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			midpointselected = false;
			vertexselected = false;
			refresh();
		}
	};
	
	private OnClickListener clearButtOnClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			clear();
		}
	};
	
	private OnClickListener undoButtonClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			editingstates.remove(editingstates.size() - 1);
			EditingStates state = editingstates.get(editingstates.size() - 1);
			points.clear();
			points.addAll(state.points1);
			Log.d(TAG, "# of points = " + points.size());
			midpointselected = state.midpointselected1;
			vertexselected = state.vertexselected1;
			insertingindex = state.insertingindex1;
			refresh();

		}
	};
	
	private OnClickListener saveButtonClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			save();
		}
	};
	
	private void save() {
		saveButton.setEnabled(false);

		Graphic g;
		MultiPath multipath;

		if (editingmode == POINT)
			g = featurelayer.createFeatureWithTemplate(template, points.get(0));
		else {
			if (editingmode == POLYLINE)
				multipath = new Polyline();
			else if (editingmode == POLYGON)
				multipath = new Polygon();
			else
				return;
			multipath.startPath(points.get(0));
			for (int i = 1; i < points.size(); i++) {
				multipath.lineTo(points.get(i));
			}

			// Simplify the geometry that is to be set on the graphics.
			// Note this call is local not made to the server.
			Geometry geom = GeometryEngine.simplify(multipath,
					mapView.getSpatialReference());
			g = featurelayer.createFeatureWithTemplate(template, geom);
		}
		featurelayer.applyEdits(new Graphic[] { g }, null, null,
				new CallbackListener<FeatureEditResult[][]>() {

					@Override
					public void onError(Throwable e) {
						Log.d(TAG, e.getMessage());
						doClear(null);
					}

					public void onCallback(FeatureEditResult[][] objs) {
						doClear(objs);
					}
				});

	}
	
	void doClear(final FeatureEditResult[][] objs) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (objs != null) {
					if (objs[0][0].isSuccess()) {
						Toast.makeText(VectorExampleActivity.this,
								EDIT_APPLIED_SUCCESSFULLY, Toast.LENGTH_SHORT)
								.show();
						// graphicsLayerEditing.removeAll();
					} else {
						editingerrormessage = objs[0][0].getError()
								.getDescription();
						showDialog(DIALOG_EDIT_FAILED);
					}
				}
				clear();
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final LayoutInflater inflater = (LayoutInflater) VectorExampleActivity.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Dialog dialog;

		switch (id) {
		case DIALOG_EDIT_FAILED:
			builder.setMessage(editingerrormessage);
			builder.setNegativeButton(CLOSE,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog1, int id1) {
							dialog1.cancel();
						}
					});
			dialog = builder.create();

			return dialog;
		case DIALOG_FEATURE_TYPE:
			View view = inflater.inflate(R.layout.selectfeaturetype, null);
			builder.setView(view);
			ListView listview = (ListView) view.findViewById(R.id.listView1);
			listview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					template = templatelist.get(position);
					featurelayer = featurelayerlist.get(position);

					Legend legend = legendlist.get(position);
					Symbol symbol = legend.getSymbol();
					if (symbol instanceof MarkerSymbol) {
						editButton.setText("Point");
						editingmode = POINT;
					} else if (symbol instanceof LineSymbol) {
						editButton.setText("Polyline");
						editingmode = POLYLINE;
					} else if (symbol instanceof FillSymbol) {
						editButton.setText("Polygon");
						editingmode = POLYGON;
					} else
						editButton.setText("Edit");
					dismissDialog(DIALOG_FEATURE_TYPE);
					clear();
				}
			});
			listTemplates();

			listview.setAdapter(new BaseAdapter() {

				public int getCount() {
					return legendlist.size();
				}

				public Object getItem(int position) {
					return legendlist.get(position);
				}

				public long getItemId(int position) {
					return 0;
				}

				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					ListViewHolder holder = null;
					if (convertView == null) {
						convertView = inflater.inflate(R.layout.listitem, null);
						holder = new ListViewHolder();
						holder.legendview = (ImageView) convertView
								.findViewById(R.id.legend);
						holder.textview = (TextView) convertView
								.findViewById(R.id.label);
					} else
						holder = (ListViewHolder) convertView.getTag();

					Legend legend = (Legend) getItem(position);
					holder.legendview.setImageBitmap(legend.getBitmap());
					holder.textview.setText(legendlist.get(position).getName());

					convertView.setTag(holder);
					return convertView;
				}
			});

			dialog = builder.create();
			dialog.setOnCancelListener(new OnCancelListener() {

				public void onCancel(DialogInterface dialog) {
				}
			});
			return dialog;
		default:
			return null;
		}
	}
	static class ListViewHolder {
		ImageView legendview;
		TextView textview;
	}
	
	public synchronized void listTemplates() {
		Log.d(TAG, new Date(System.currentTimeMillis()).toLocaleString());
		legendlist = new ArrayList<Legend>();
		templatelist = new ArrayList<FeatureTemplate>();
		featurelayerlist = new ArrayList<ArcGISFeatureLayer>();

		Layer[] layers = mapView.getLayers();
		for (Layer l : layers) {

			if (l instanceof ArcGISFeatureLayer) {
				Log.d(TAG, l.getUrl());
				ArcGISFeatureLayer featurelayer1 = (ArcGISFeatureLayer) l;

				FeatureType[] types = featurelayer1.getTypes();
				for (FeatureType featureType : types) {
					FeatureTemplate[] templates = featureType.getTemplates();
					for (FeatureTemplate featureTemplate : templates) {
						String name = featureTemplate.getName();
						Graphic g = featurelayer1.createFeatureWithTemplate(
								featureTemplate, null);
						Renderer renderer = featurelayer1.getRenderer();
						Symbol symbol = renderer.getSymbol(g);
						Bitmap bitmap = createSymbolBitmap(featurelayer1,
								featureTemplate);

						legendlist.add(new Legend(bitmap, name, symbol));
						templatelist.add(featureTemplate);
						featurelayerlist.add((ArcGISFeatureLayer) l);
					}
				}
				if (legendlist.size() == 0) { // no types
					FeatureTemplate[] templates = featurelayer1.getTemplates();
					for (FeatureTemplate featureTemplate : templates) {
						String name = featureTemplate.getName();
						Graphic g = featurelayer1.createFeatureWithTemplate(
								featureTemplate, null);
						Renderer renderer = featurelayer1.getRenderer();
						Symbol symbol = renderer.getSymbol(g);
						Bitmap bitmap = createSymbolBitmap(featurelayer1,
								featureTemplate);
						legendlist.add(new Legend(bitmap, name, symbol));
						templatelist.add(featureTemplate);
						featurelayerlist.add((ArcGISFeatureLayer) l);
					}
				}
			}
		}
		Log.d(TAG, new Date(System.currentTimeMillis()).toLocaleString());
	}// list templates
	
	private Bitmap createSymbolBitmap(ArcGISFeatureLayer featurelayer,
			FeatureTemplate featureTemplate) {
		// determine feature type
		int widthInPixels = 40;

		Graphic g = featurelayer.createFeatureWithTemplate(featureTemplate,
				null);
		Renderer renderer = featurelayer.getRenderer();
		Symbol symbol = renderer.getSymbol(g);

		return SymbolHelper
				.getLegendImage(symbol, widthInPixels, widthInPixels);
	}
	
	class MyTouchListener extends MapOnTouchListener {
		MapView map;
		Context context;

		public MyTouchListener(Context context, MapView view) {
			super(context, view);
			this.context = context;
			map = view;
		}

		@Override
		public boolean onDragPointerMove(MotionEvent from, final MotionEvent to) {
			return super.onDragPointerMove(from, to);
		}

		@Override
		public boolean onDragPointerUp(MotionEvent from, final MotionEvent to) {
			return super.onDragPointerUp(from, to);
		}

		/**
		 * In this method we check if the point clicked on the map denotes a new
		 * point or means an existing vertex must be moved.
		 */
		@Override
		public boolean onSingleTap(final MotionEvent e) {

			Point point = map.toMapPoint(new Point(e.getX(), e.getY()));

			if (editingmode == POINT)
				points.clear();

			if (!midpointselected && !vertexselected) {
				// check if user tries to select an existing point.

				int idx1 = getSelectedIndex(e.getX(), e.getY(), mpoints, map);
				if (idx1 != -1) {
					midpointselected = true;
					insertingindex = idx1;
				}

				if (!midpointselected) { // check vertices
					int idx2 = getSelectedIndex(e.getX(), e.getY(), points, map);
					if (idx2 != -1) {
						vertexselected = true;
						insertingindex = idx2;
					}
				}

				if (!midpointselected && !vertexselected) {
					// no match, add new vertex at the location
					points.add(point);
					editingstates.add(new EditingStates(points,
							midpointselected, vertexselected, insertingindex));
				}
			} else { // an existing point has been selected previously.
				movePoint(point);
			}
			refresh();
			return true;
		}

	}
	
	void movePoint(Point point) {
		if (midpointselected) {// Move mid-point to the new location and make it
			// a vertex.
			points.add(insertingindex + 1, point);
			editingstates.add(new EditingStates(points, midpointselected,
					vertexselected, insertingindex));
		} else if (vertexselected) {
			ArrayList<Point> temp = new ArrayList<Point>();
			for (int i = 0; i < points.size(); i++) {
				if (i == insertingindex)
					temp.add(point);
				else
					temp.add(points.get(i));
			}
			points.clear();
			points.addAll(temp);
			editingstates.add(new EditingStates(points, midpointselected,
					vertexselected, insertingindex));
		}
		midpointselected = false; // back to the normal drawing mode.
		vertexselected = false;
	}

	void refresh() {
		if (graphicsLayerEditing != null)
			graphicsLayerEditing.removeAll();
		drawPolyline();
		drawMidPoints();
		drawVertices();

		clearButton.setEnabled(true);
		removeButton.setEnabled(points.size() > 1 && !midpointselected);
		cancelButton.setEnabled(midpointselected || vertexselected);
		undoButton.setEnabled(editingstates.size() > 1);
		saveButton.setEnabled((editingmode == POINT && points.size() > 0)
				|| (editingmode == POLYLINE && points.size() > 1)
				|| (editingmode == POLYGON && points.size() > 2));
	}
	
	private void drawMidPoints() {
		int index;
		Graphic graphic;

		// draw mid-point
		if (points.size() > 1) {
			mpoints.clear();
			for (int i = 1; i < points.size(); i++) {
				Point p1 = points.get(i - 1);
				Point p2 = points.get(i);
				mpoints.add(new Point((p1.getX() + p2.getX()) / 2,
						(p1.getY() + p2.getY()) / 2));
			}
			if (editingmode == POLYGON) { // complete the circle
				Point p1 = points.get(0);
				Point p2 = points.get(points.size() - 1);
				mpoints.add(new Point((p1.getX() + p2.getX()) / 2,
						(p1.getY() + p2.getY()) / 2));
			}
			index = 0;
			for (Point pt : mpoints) {
				if (midpointselected && insertingindex == index)
					graphic = new Graphic(pt, redMarkerSymbol);
				else
					graphic = new Graphic(pt, grnMarkerSymbol);
				graphicsLayerEditing.addGraphic(graphic);
				index++;
			}
		}
	}

	private void drawVertices() {
		int index;
		// draw vertices
		index = 0;
		if (graphicsLayerEditing == null) {
			graphicsLayerEditing = new GraphicsLayer();
			mapView.addLayer(graphicsLayerEditing);
		}

		for (Point pt : points) {
			if (vertexselected && index == insertingindex) {
				Graphic graphic = new Graphic(pt, redMarkerSymbol);
				Log.d(TAG, "Add Graphic vertex");
				graphicsLayerEditing.addGraphic(graphic);

			} else if (index == points.size() - 1 && !midpointselected
					&& !vertexselected) {
				Graphic graphic = new Graphic(pt, redMarkerSymbol);
				int id = graphicsLayerEditing.addGraphic(graphic);
				Log.d(TAG,
						"Add Graphic mid point" + pt.getX() + " " + pt.getY()
								+ " id = " + id);

			} else {
				Graphic graphic = new Graphic(pt, blkMarkerSymbol);
				Log.d(TAG, "Add Graphic point");
				graphicsLayerEditing.addGraphic(graphic);
			}

			index++;
		}
	}

	private void drawPolyline() {
		if (graphicsLayerEditing == null) {
			graphicsLayerEditing = new GraphicsLayer();
			mapView.addLayer(graphicsLayerEditing);
		}
		if (points.size() <= 1)
			return;

		Graphic graphic;
		MultiPath multipath;
		if (editingmode == POLYLINE)
			multipath = new Polyline();
		else
			multipath = new Polygon();
		multipath.startPath(points.get(0));
		for (int i = 1; i < points.size(); i++) {
			multipath.lineTo(points.get(i));
		}
		Log.d(TAG, "DrawPolyline: Array count = " + points.size());
		if (editingmode == POLYLINE)
			graphic = new Graphic(multipath, new SimpleLineSymbol(Color.BLACK,
					4));
		else {
			SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(
					Color.YELLOW);
			simpleFillSymbol.setAlpha(100);
			simpleFillSymbol.setOutline(new SimpleLineSymbol(Color.BLACK, 4));
			graphic = new Graphic(multipath, (simpleFillSymbol));
		}
		Log.d(TAG, "Add Graphic Line in DrawPolyline");
		graphicsLayerEditing.addGraphic(graphic);
	}
	
	class EditingStates {
		ArrayList<Point> points1 = new ArrayList<Point>();
		boolean midpointselected1 = false;
		boolean vertexselected1 = false;
		int insertingindex1;

		public EditingStates(ArrayList<Point> points, boolean midpointselected,
				boolean vertexselected, int insertingindex) {
			this.points1.addAll(points);
			this.midpointselected1 = midpointselected;
			this.vertexselected1 = vertexselected;
			this.insertingindex1 = insertingindex;
		}
	}
	int getSelectedIndex(double x, double y, ArrayList<Point> points1,
			MapView map) {

		if (points1 == null || points1.size() == 0)
			return -1;

		int index = -1;
		double distSQ_Small = Double.MAX_VALUE;
		for (int i = 0; i < points1.size(); i++) {
			Point p = map.toScreenPoint(points1.get(i));
			double diffx = p.getX() - x;
			double diffy = p.getY() - y;
			double distSQ = diffx * diffx + diffy * diffy;
			if (distSQ < distSQ_Small) {
				index = i;
				distSQ_Small = distSQ;
			}
		}

		if (distSQ_Small < (40 * 40)) {
			return index;
		}
		return -1;

	}// end of method
	
	void clear() {
		points.clear();
		mpoints.clear();
		editingstates.clear();

		midpointselected = false;
		vertexselected = false;
		insertingindex = 0;
		clearButton.setEnabled(false);
		removeButton.setEnabled(false);
		cancelButton.setEnabled(false);
		undoButton.setEnabled(false);
		saveButton.setEnabled(false);

		if (graphicsLayerEditing != null)
			graphicsLayerEditing.removeAll();

	}

}

