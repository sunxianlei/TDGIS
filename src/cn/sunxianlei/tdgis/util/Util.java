package cn.sunxianlei.tdgis.util;

import com.esri.core.geometry.Point;

public class Util {
	
	public static Point GeographicToWebMercator(Point p)
    {
        double x = p.getX();
        double y = p.getY();
        double num = x * 0.017453292519943295;
        double xx = 6378137.0 * num;
        double a = y * 0.017453292519943295;
        return new Point(xx, 3189068.5 * Math.log((1.0 + Math.sin(a)) / (1.0 - Math.sin(a))));
    }

    public static Point WebMercatorToGeographic(Point p)
    {
        double originShift = 2 * Math.PI * 6378137 / 2.0;
        double lon = (p.getX()/ originShift) * 180.0;
        double lat = (p.getY() / originShift) * 180.0;

        lat = 180 / Math.PI * (2 * Math.atan(Math.exp(lat * Math.PI / 180.0)) - Math.PI / 2.0);
        return new Point(lon, lat);
    }

}
