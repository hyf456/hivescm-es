package com.hivescm.es.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author:
 * @Description: 访销嵌套脚本更新参数
 * @Date: 2018/5/24 13:38
 */
@ApiModel(value = "VssNestedScritpUpdateParam", description = "访销嵌套脚本更新参数")
@Data
public class VssNestedScritpUpdateParam implements Serializable {
    private static final long serialVersionUID = 5587768114527060469L;

    @ApiModelProperty(value = "根据业务员Id删除对应的业务员")
    private Long salesmanId;

    @ApiModelProperty(value = "根据商品类目添加业务员")
    private Long goodsCategoryId;

    @ApiModelProperty(value = "添加的业务员对象")
    private VssGoodsSaleParam vssGoodsSaleParam;
}
