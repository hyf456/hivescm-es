package com.hivescm.es.service;

import com.hivescm.common.domain.DataResult;
import com.hivescm.es.common.CollapseQueryObject;
import com.hivescm.es.common.ESResponse;
import com.hivescm.es.common.StatisticESObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Created by DongChunfu on 2017/8/29
 * <p>
 * ES 统计相关服务
 */
@FeignClient(value = "hivescm-es")
public interface ESStatisticService {

	/**
	 * 全局统计服务，不受分页效果影响
	 *
	 * @param esObject ES 统计请求参数
	 * @return key:functionName;value:statistic value
	 */
	@RequestMapping(value = "/hivescmes/statisticByConditionsOMS", method = RequestMethod.POST)
	DataResult<Map<String, Number>> statisticByConditionsOMS(@RequestBody StatisticESObject esObject);

	/**
	 * 按 field value 去重，返回指定数目的 doc
	 *
	 * @param esObject 去重请求参数
	 * @return ES 通用响应结果
	 */
	@RequestMapping(value = "/hivescmes/collapseOMS", method = RequestMethod.POST)
	DataResult<Map<String, ESResponse>> collapseOMS(@RequestBody CollapseQueryObject esObject);
}
