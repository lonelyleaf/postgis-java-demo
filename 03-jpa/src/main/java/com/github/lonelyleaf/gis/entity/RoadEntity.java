package com.github.lonelyleaf.gis.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.geolatte.geom.G2D;
import org.geolatte.geom.LineString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@ApiModel("有行政边界的行政区")
@Entity
@Table(name = "t_zunyi_roads")
public class RoadEntity {

    @Id
    private int gid;
    private String name;
    private String level;
    private LineString<G2D> geom;

}
