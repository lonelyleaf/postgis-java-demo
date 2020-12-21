package com.github.lonelyleaf.gis.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "t_gps")
public class GpsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer gid;
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            timezone = "+08")
    @ApiModelProperty("时间")
    private Date time;
    @ApiModelProperty("设备id")
    private String devId;
    @ApiModelProperty("位置")
    private Point<G2D> location;
    @ApiModelProperty("卫星定位数")
    private int gpsNum;
    @ApiModelProperty("GPS定位信息")
    private String gpsType;
    @ApiModelProperty("对地真北航向角")
    private double azimuth;
    @ApiModelProperty("地面速率")
    private double gndRate;
}
