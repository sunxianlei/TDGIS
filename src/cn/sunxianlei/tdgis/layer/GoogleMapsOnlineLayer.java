package cn.sunxianlei.tdgis.layer;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.RejectedExecutionException;

import android.util.Log;

import com.esri.android.map.TiledServiceLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.io.UserCredentials;

public class GoogleMapsOnlineLayer extends TiledServiceLayer
{
 	public enum  MapType
    {
    	VECTOR,		//矢量标注地图
        IMAGE,		//影像地图
        ROAD 		//道路标注图层
    }
 	
    private TileInfo mTileInfo;
	private GoogleMapsOnlineLayer.MapType mMapType;
    
    public GoogleMapsOnlineLayer(GoogleMapsOnlineLayer.MapType mapType, UserCredentials usercredentials, boolean flag)
    {
        super("");
        this.mMapType=mapType;
        setCredentials(usercredentials);
        
        if(flag == true)
        {
        	try
            {
                getServiceExecutor().submit(new Runnable() 
                {
                    /* (non-Javadoc)
                     * @see java.lang.Runnable#run()
                     */
                    public final void run()
                    {
                    	iGoogleLayer.initLayer();
                    }
                    final GoogleMapsOnlineLayer iGoogleLayer;
                    {
                    	iGoogleLayer = GoogleMapsOnlineLayer.this;
                    }
                });
                return;
            }
            catch(RejectedExecutionException ree) 
            { 
            	//
            }
        }    
    }
    
    protected void initLayer()
    {
         this.buildTileInfo();
         //this.setFullExtent(new Envelope(-20037508.342787, -20037508.342787, 20037508.342787, 20037508.342787));
         //this.setInitialExtent(new Envelope(-20037508.342787, -20037508.342787, 20037508.342787, 20037508.342787));
         this.setFullExtent(new Envelope(13216629.183413, 3762785.57643891, 13229319.6053634, 3774736.15473275));
         this.setInitialExtent(new Envelope(13216629.183413, 3762785.57643891, 13229319.6053634, 3774736.15473275));
         this.setDefaultSpatialReference(SpatialReference.create(102113));
         super.initLayer();
    }
    
    private void buildTileInfo()
    {
        Point iPoint = new Point(-20037508.342787, 20037508.342787);
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
    
    public void refresh()
    {
        try
        {
            getServiceExecutor().submit(new Runnable() 
            {
                public final void run()
                {
                    if(iGoogleLayer.isInitialized())
                        try
                        {
                        	iGoogleLayer.clearTiles();
                            return;
                        }
                        catch(Exception exception)
                        {
                            Log.e("ArcGIS", "Re-initialization of the layer failed.", exception);
                        }
                }
                final GoogleMapsOnlineLayer iGoogleLayer;          
                {
                	iGoogleLayer = GoogleMapsOnlineLayer.this;
                }
            });
            return;
        }
        catch(RejectedExecutionException ree)
        {
            return;
        }
    }  
    
    public MapType getMapType()
    {
        return this.mMapType;
    }
    
    public void setMapType(GoogleMapsOnlineLayer.MapType mapType)
    {
        this.mMapType = mapType;
    }
    
    @Override
    protected byte[] getTile(int level, int col, int row) throws Exception 
    {
		byte[] iResult = null;
		
		try 
		{
			URL iURL = null;
			byte[] iBuffer = new byte[1024];
			HttpURLConnection iHttpURLConnection = null;
			BufferedInputStream iBufferedInputStream = null;
			ByteArrayOutputStream iByteArrayOutputStream = null;

			iURL = new URL(this.getMapUrl(level, col, row));
			iHttpURLConnection = (HttpURLConnection) iURL.openConnection();
			iHttpURLConnection.connect();
			iBufferedInputStream = new BufferedInputStream(iHttpURLConnection.getInputStream());
			iByteArrayOutputStream = new ByteArrayOutputStream();
			while (true)
			{
				int iLength = iBufferedInputStream.read(iBuffer);
				if (iLength > 0) 
				{
					iByteArrayOutputStream.write(iBuffer, 0, iLength);
				} 
				else 
				{
					break;
				}
			};
			iBufferedInputStream.close();
			iHttpURLConnection.disconnect();

			iResult = iByteArrayOutputStream.toByteArray();
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
		return iResult;
    }

    @Override
    public TileInfo getTileInfo()
    {
        return this.mTileInfo;
    }

    private String getMapUrl(int level, int col, int row)
    {
    	String iResult = null;
    	Random iRandom = null;
    	
    	iResult = "http://mt";
    	iRandom = new Random();
    	iResult =iResult + iRandom.nextInt(4);
        switch(this.mMapType)
        {
        	case VECTOR:
        		iResult = iResult + ".google.cn/vt/lyrs=m@212000000&hl=zh-CN&gl=CN&src=app&x=" + col + "&y=" + row + "&z=" +level + "&s=";
        		break;
        	case IMAGE:
        		iResult = iResult + ".google.cn/vt/lyrs=s@126&hl=zh-CN&gl=CN&src=app&x=" + col + "&y=" + row + "&z=" +level + "&s=";
        		break;
        	case ROAD:
        		iResult = iResult + ".google.cn/vt/imgtp=png32&lyrs=h@212000000&hl=zh-CN&gl=CN&src=app&x=" + col + "&y=" + row + "&z=" +level + "&s=";
        		break;
            default:
                return null;
        }
        return iResult;
    }

}
