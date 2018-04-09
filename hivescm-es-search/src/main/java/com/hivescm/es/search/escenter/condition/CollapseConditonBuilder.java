package com.hivescm.es.search.escenter.condition;

import com.hivescm.es.common.CollapseQueryObject;
import com.hivescm.es.common.conditions.InnerHitsCondition;
import com.hivescm.es.common.conditions.OrderCondition;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.InnerHitBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by DongChunfu on 2017/9/7
 * <p>
 * 瓦解查询条件构建器
 */
@Component(value = "collapseConditonBuilder")
public class CollapseConditonBuilder {

	@Resource
	private SerchSourceBuilder serchSourceBuilder;

	public CollapseBuilder builde(final SearchRequestBuilder searchRequestBuilder, CollapseQueryObject esObject) {
		CollapseBuilder collapseBuilder = new CollapseBuilder(esObject.getFieldName());

		for (InnerHitsCondition innerHitsCondition : esObject.getInnerHitsConditions()) {
			InnerHitBuilder innerHitBuilder = new InnerHitBuilder();

			innerHitBuilder.setName(innerHitsCondition.getHitName());
			innerHitBuilder.setSize(innerHitsCondition.getHitSize());

			final List<OrderCondition> orderConditions = innerHitsCondition.getOrderConditions();
			if (orderConditions != null && orderConditions.size() > 0) {
				for (OrderCondition orderCondition : orderConditions) {
					final FieldSortBuilder sort = serchSourceBuilder.getSort(orderCondition);
					innerHitBuilder.addSort(sort);
				}
			}

			FetchSourceContext fetchSourceContext;
			final List<String> fieldNames = innerHitsCondition.getFieldNames();
			if (fieldNames == null || fieldNames.size() == 0) {
				fetchSourceContext = new FetchSourceContext(Boolean.TRUE, Strings.EMPTY_ARRAY, Strings.EMPTY_ARRAY);
			} else {
				fetchSourceContext = new FetchSourceContext(Boolean.TRUE, fieldNames.toArray(new String[0]), Strings.EMPTY_ARRAY);
			}
			innerHitBuilder.setFetchSourceContext(fetchSourceContext);
			collapseBuilder.setInnerHits(innerHitBuilder);
		}
		return collapseBuilder;
	}
}
