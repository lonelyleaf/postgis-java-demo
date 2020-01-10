/*
 * Copyright [2019] [lonelyleaf]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.lonelyleaf.gis.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@ApiModel("行政区，这里只有贵州的")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_guizhou_boundary")
public class DistrictEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int gid;
    @ApiModelProperty("行政区名")
    private String name;
    @ApiModelProperty("行政区等级，省、市、区县、乡镇")
    private String layer;
    @ApiModelProperty("编码")
    private String code;

}
