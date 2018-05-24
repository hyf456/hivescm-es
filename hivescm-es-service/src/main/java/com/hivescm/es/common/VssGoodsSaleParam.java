package com.hivescm.es.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author:
 * @Description: 访销商品中的业务员参数
 * @Date: 2018/5/24 13:29
 */
@ApiModel(value = "VssGoodsSaleParam", description = "访销商品中的业务员参数")
@Data
public class VssGoodsSaleParam implements Serializable{
    private static final long serialVersionUID = 7156756558681830266L;

    @ApiModelProperty(value = "业务员id")
    private Long id;

    @ApiModelProperty(value = "业务员名称")
    private String salesmanName;
}
