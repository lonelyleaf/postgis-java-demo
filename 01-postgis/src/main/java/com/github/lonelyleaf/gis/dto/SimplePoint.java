package com.github.lonelyleaf.gis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimplePoint {

    private double x;
    private double y;

    public SimplePoint(org.postgis.Point point) {
        this.x = point.x;
        this.y = point.y;
    }
}
