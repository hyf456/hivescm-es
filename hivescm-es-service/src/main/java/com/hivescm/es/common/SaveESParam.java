package com.hivescm.es.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author:
 * @Description: 保存es参数
 * @Date: 2018/5/24 14:27
 */
@ApiModel(value = "SaveESParam", description = "保存es参数")
@Data
public class SaveESParam implements Serializable {

    private static final long serialVersionUID = -8767995903483719394L;

    @ApiModelProperty(value = "唯一标志")
    private Map<Object, Object> ukMap;

    @ApiModelProperty(value = "文档数据内容")
    private Object dataMap;

    @ApiModelProperty(value = "对象是否排除null字段")
    private Boolean excludeNullFlag = false;
}
