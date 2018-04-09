package com.hivescm.es.search.escenter.condition;

import com.hivescm.es.common.QueryESObject;
import com.hivescm.es.common.conditions.SearchCondition;
import com.hivescm.es.search.escenter.util.CollectionUtil;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by DongChunfu on 2017/8/7
 * <p>
 * 检索条件构建器
 */
@Component(value = "queryConditionBuilder")
public class QueryConditionBuilder {

	@Resource(name = "searchConditionBuilder")
	private SearchConditionBuilder searchConditionBuilder;

	@Resource(name = "relationConditionBuilder")
	private RelationConditionBuilder relationConditionBuilder;

	public void builde(final SearchRequestBuilder searchRequestBuilder, final QueryESObject esObject) {
		BoolQueryBuilder rootQuery = QueryBuilders.boolQuery();

		QueryESObject termQuery = esObject;
		do {
			final BoolQueryBuilder currentQuery = innerBuilder(termQuery.getSearchConditions());

			relationConditionBuilder.builder(esObject.groupCondition(), currentQuery, rootQuery);

			termQuery = termQuery.getNextGroupQuery();
		} while (termQuery != null);

		searchRequestBuilder.setQuery(rootQuery);
	}

	/**
	 * 根据查询条件构建 QueryBuilder
	 *
	 * @param currentConditions 检索条件
	 * @return 查询条件
	 */
	public BoolQueryBuilder innerBuilder(final List<SearchCondition> currentConditions) {
		final BoolQueryBuilder rootQuery = QueryBuilders.boolQuery();

		if (CollectionUtil.isEmpty(currentConditions)) {
			return rootQuery;
		}

		for (SearchCondition currentCondition : currentConditions) {
			QueryBuilder currentQuery = searchConditionBuilder.builder(currentCondition);
			relationConditionBuilder.builder(Boolean.FALSE, currentQuery, rootQuery);// 同一条件集合内均为 and 链接
		}

		return rootQuery;
	}

	/**
	 * @Author: hanyf
	 * @Description: shoul最小匹配数
	 * @Date: 2017/10/23 9:53
	 */
	public void buildeOr(final SearchRequestBuilder searchRequestBuilder, final QueryESObject esObject) {
		QueryESObject termQuery = esObject;
		//must中and的匹配
		final BoolQueryBuilder rootQuery = innerBuilder(termQuery.getSearchConditions());

		//or的搜索条件
		QueryESObject nextGroupQuery = termQuery.getNextGroupQuery();
		if (nextGroupQuery != null) {
			List<SearchCondition> nextGroupQuerySearchConditions = nextGroupQuery.getSearchConditions();
			if (nextGroupQuerySearchConditions != null && !nextGroupQuerySearchConditions.isEmpty()) {
				for (SearchCondition currentCondition : nextGroupQuerySearchConditions) {
					QueryBuilder builder = searchConditionBuilder.builder(currentCondition);
					builderOr(builder, rootQuery);// 同一条件集合内均为 and 链接
				}
				rootQuery.minimumNumberShouldMatch(1);
			}
		}
		searchRequestBuilder.setQuery(rootQuery);
	}


	/**
	 * @Author: hanyf
	 * @Description: or条件拼接
	 * @Date: 2017/10/23 9:59
	 */
	private QueryBuilder builderOr(QueryBuilder currentQuery, BoolQueryBuilder rootQuery) {
		return rootQuery.should(currentQuery);
	}
}
