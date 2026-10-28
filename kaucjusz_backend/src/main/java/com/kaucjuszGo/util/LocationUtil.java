package com.kaucjuszGo.util;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class LocationUtil {
    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    private static Point createPoint(double latitude, double longitude) {
        return geometryFactory.createPoint(new org.locationtech.jts.geom.Coordinate(longitude, latitude));
    }
}
