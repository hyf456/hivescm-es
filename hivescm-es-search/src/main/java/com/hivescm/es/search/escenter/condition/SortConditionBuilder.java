package com.hivescm.es.search.escenter.condition;

import com.hivescm.es.common.QueryESObject;
import com.hivescm.es.common.conditions.OrderCondition;
import com.hivescm.es.common.enums.SortEnum;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by DongChunfu on 2017/8/8
 * <p>
 * 排序条件构建器
 */
@Component(value = "sortConditionBuilder")
public class SortConditionBuilder {
	public SearchRequestBuilder builde(final SearchRequestBuilder searchRequestBuilder,
			final QueryESObject esObject) {

		final List<OrderCondition> orderConditions = esObject.getOrderConditions();
		if (null == orderConditions || orderConditions.isEmpty()) {
			return searchRequestBuilder;
		}

		for (OrderCondition orderCondition : orderConditions) {
			final String fieldName = orderCondition.getFieldName();
			final SortEnum sort = orderCondition.getOrderCondition();

			switch (sort) {
			case ASC:
				searchRequestBuilder.addSort(fieldName, SortOrder.ASC);
				break;
			case DESC:
				searchRequestBuilder.addSort(fieldName, SortOrder.DESC);
				break;
			default:
				return searchRequestBuilder;
			}
		}
		return searchRequestBuilder;
	}
}
