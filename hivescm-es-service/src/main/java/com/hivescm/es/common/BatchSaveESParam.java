package com.hivescm.es.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author:
 * @Description: 批量保存es数据
 * @Date: 2018/5/24 14:39
 */
@ApiModel(value = "BatchSaveESParam", description = "批量保存es数据")
@Data
public class BatchSaveESParam implements Serializable {
    private static final long serialVersionUID = 7769203108951662837L;

    @ApiModelProperty(value = "保存es数据参数")
    private List<SaveESParam> saveDatas;
}
