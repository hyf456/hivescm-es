package com.hivescm.es.search.escenter.condition;

import com.hivescm.es.common.QueryESObject;
import com.hivescm.es.common.conditions.PageCondition;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by DongChunfu on 2017/8/7
 * <p>
 * 分页条件 构建器
 */
@Component(value = "pageConditionBuilder")
public class PageConditionBuilder {

	@Value(value = "${escenter.page.max.size}")
	private int pageMaxSize;

	public SearchRequestBuilder builde(final SearchRequestBuilder searchRequestBuilder, final QueryESObject esObject) {

		PageCondition pageCondition = esObject.getPageCondition();
		if (null == pageCondition) {
			pageCondition = new PageCondition();
			pageCondition.setPageSize(1000);
			pageCondition.setCurrentPage(1);
			esObject.setPageCondition(pageCondition);
			return searchRequestBuilder.setFrom(0).setSize(pageMaxSize);
		}

		Integer currentPage = pageCondition.getCurrentPage();
		currentPage = currentPage == null ? 1 : currentPage < 1 ? 1 : currentPage;

		Integer pageSize = pageCondition.getPageSize();
		pageSize = pageSize == null ? 1 : pageSize > pageMaxSize ? pageMaxSize : pageSize < 1 ? 1 : pageSize;
		pageCondition.setCurrentPage(currentPage);
		pageCondition.setPageSize(pageSize);

		return searchRequestBuilder.setFrom((currentPage - 1) * pageSize).setSize(pageSize);
	}
}
