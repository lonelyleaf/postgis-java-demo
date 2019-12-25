-- 启用postgis
-- CREATE EXTENSION postgis;

-- 创建gps数据表
CREATE TABLE "t_gps"
(
    "time"           timestamptz(3)         NOT NULL,
    "dev_id"         varchar(36)            NOT NULL,
    "location"       GEOGRAPHY(Point, 4326) NOT NULL,
    "gps_num"        int4,
    "gps_type"       varchar(10)            NOT NULL,
    "azimuth"        float4,
    "gnd_rate"       float4
) WITHOUT OIDS;

COMMENT ON COLUMN "t_gps"."time" IS '时间';
COMMENT ON COLUMN "t_gps"."dev_id" IS '设备ID';
COMMENT ON COLUMN "t_gps"."gps_num" IS '卫星定位数';
COMMENT ON COLUMN "t_gps"."gps_type" IS 'GPS定位信息';
COMMENT ON COLUMN "t_gps"."azimuth" IS '对地真北航向角';

