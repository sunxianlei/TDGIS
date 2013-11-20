package cn.sunxianlei.tdgis.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.concurrent.RejectedExecutionException;

import org.tilespitter.mapboxtiles.MBTilesDroidSpitter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.esri.android.map.TiledServiceLayer;
import com.esri.android.map.TiledServiceLayer.TileInfo;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

public class MBTilesGoogleMapsOfflineLayer extends TiledServiceLayer {
	private TileInfo mTileInfo;
	
	private final File sdcard = Environment.getExternalStorageDirectory();
	private final String db_name = "map.mbtiles";	
	private final File sqlitefile = new File(sdcard, db_name); // sqlite file to load	
	private MBTilesDroidSpitter sqlcli;
	private Context context;
	
	public MBTilesGoogleMapsOfflineLayer(Context context) {
		super("");
		this.context=context;
		// TODO Auto-generated constructor stub
		sqlcli = new MBTilesDroidSpitter(context,sqlitefile);
		
		boolean fetchMetadata = true;
    	String versionOfMbtileSpec = "1.0";
    	sqlcli.open(fetchMetadata, versionOfMbtileSpec);

		try
        {
            getServiceExecutor().submit(new Runnable() 
            {
                public final void run()
                {
                	MBTilesGoogleLayer.initLayer();
                }
                final MBTilesGoogleMapsOfflineLayer MBTilesGoogleLayer;
                {
                	MBTilesGoogleLayer = MBTilesGoogleMapsOfflineLayer.this;
                }
            });
            return;
        }
        catch(RejectedExecutionException ree) 
        { 
        	//
        }
	}
	
	protected void initLayer()
    {
         this.buildTileInfo();
         this.setFullExtent(new Envelope(13218219.740283, 3768868.88604863, 13223149.9286074, 3772442.31712096));
         this.setInitialExtent(new Envelope(13218219.740283, 3768868.88604863, 13223149.9286074, 3772442.31712096));
         this.setDefaultSpatialReference(SpatialReference.create(102113));
         super.initLayer();
    }
	
	private void buildTileInfo()
    {
        Point iPoint = new Point(13218219.740283, 3768868.88604863);
        double[] iScale =
    	{
    		591657527.591555,
    		295828763.795777,
    		147914381.897889,
    		73957190.948944,
    		36978595.474472,
    		18489297.737236,
    		9244648.868618,
    		4622324.434309,
    		2311162.217155,
    		1155581.108577,
    		577790.554289,
            288895.277144,
            144447.638572,
            72223.819286,
            36111.909643,
            18055.954822,
            9027.977411,
            4513.988705,
            2256.994353,
            1128.497176,
        }; 
        double[] iRes = 
    	{
    		156543.033928,
    		78271.5169639999,
    		39135.7584820001,
    		19567.8792409999,
    		9783.93962049996,
    		4891.96981024998,
    		2445.98490512499,
    		1222.99245256249,
    		611.49622628138,
    		305.748113140558,
    		152.874056570411,
    		76.4370282850732,
    		38.2185141425366,
    		19.1092570712683,
    		9.55462853563415,
    		4.77731426794937,
    		2.38865713397468,
    		1.19432856685505,
    		0.597164283559817,
    		0.298582141647617,
        };
        this.mTileInfo=new com.esri.android.map.TiledServiceLayer.TileInfo(iPoint, iScale, iRes, 20, 96, 256, 256);
        this.setTileInfo(this.mTileInfo);
    }

	@Override
	protected byte[] getTile(int level, int col, int row) throws Exception {
		// TODO Auto-generated method stub
		byte[] tileImage=null;
		Bitmap bitmap=sqlcli.getTileAsBitmap(Integer.toString(col),Integer.toString(row),Integer.toString(level));
		if (bitmap!=null) {
			tileImage=Bitmap2Bytes(bitmap);
			Log.i("MBTilesLayer", bitmap.toString());
		}
		Log.i("MBTilesLayer", "no");
		return tileImage;
	}
	
	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}
}
