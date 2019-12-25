package com.github.lonelyleaf.gis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimplePoint {

    private double x;
    private double y;

    public SimplePoint(Point point) {
        this.x = point.getX();
        this.y = point.getY();
    }
}
