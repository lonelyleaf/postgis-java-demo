package com.github.lonelyleaf.gis.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.geolatte.geom.Feature;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Position;
import org.geolatte.geom.crs.CoordinateReferenceSystem;
import org.geolatte.geom.crs.CoordinateReferenceSystems;

import java.util.List;

@Data
@NoArgsConstructor
public class FeatureCollections<P extends Position, ID> {

    private String type = "FeatureCollection";
    private CoordinateReferenceSystem<G2D> crs = CoordinateReferenceSystems.WGS84;
    private List<? extends Feature<P, ID>> features;

    public FeatureCollections(CoordinateReferenceSystem<G2D> crs, List<? extends Feature<P, ID>> features) {
        this.crs = crs;
        this.features = features;
    }

    public FeatureCollections(List<? extends Feature<P, ID>> features) {
        this.features = features;
    }

}
