package cn.sunxianlei.tdgis.util;

import android.content.Context;
import android.view.MotionEvent;

import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;

public class MapTouchListener extends MapOnTouchListener {
	MapView map;
	Context context;

	public MapTouchListener(Context context, MapView view) {
		super(context, view);
		// TODO Auto-generated constructor stub
		this.context=context;
		map=view;
	}

	@Override
	public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
		// TODO Auto-generated method stub
		return super.onDragPointerMove(from, to);
	}

	@Override
	public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {
		// TODO Auto-generated method stub
		return super.onDragPointerUp(from, to);
	}

	@Override
	public boolean onSingleTap(MotionEvent point) {
		// TODO Auto-generated method stub
		return super.onSingleTap(point);
	}

}
