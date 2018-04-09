package hivescm.controller;

import com.hivescm.common.domain.DataResult;
import com.hivescm.es.common.CollapseQueryObject;
import com.hivescm.es.common.ESResponse;
import com.hivescm.es.common.StatisticESObject;
import com.hivescm.es.common.conditions.FunctionCondition;
import com.hivescm.es.common.conditions.InnerHitsCondition;
import com.hivescm.es.common.conditions.OrderCondition;
import com.hivescm.es.common.conditions.SearchCondition;
import com.hivescm.es.common.enums.ConditionExpressionEnum;
import com.hivescm.es.common.enums.SortEnum;
import com.hivescm.es.common.enums.SqlFunctionEnum;
import com.hivescm.es.search.Application;
import com.hivescm.es.search.escenter.controller.ESStatisticController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by DongChunfu on 2017/8/31
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ESStasticControllerTest {

	private String systemName = "TMS";
	private String indexName = "tms-distribution-driver";
	private String typeName = "tms-distribution-driver-list";

	@Resource
	private ESStatisticController esStatisticController;

	@Test
	public void stasticTest() {
		StatisticESObject statisticESObject = new StatisticESObject();
		statisticESObject.setSystemName(systemName);
		statisticESObject.setIndexName(indexName);

		List<FunctionCondition> functionConditions = new ArrayList<>();

		FunctionCondition sumCondition = new FunctionCondition();
		sumCondition.setFunctionName("test-sum");
		sumCondition.setFunction(SqlFunctionEnum.SUM);
		sumCondition.setField("companyId");
		functionConditions.add(sumCondition);

		FunctionCondition countCondition = new FunctionCondition();
		countCondition.setFunctionName("test-count");
		countCondition.setFunction(SqlFunctionEnum.COUNT);
		countCondition.setField("companyId");
		functionConditions.add(countCondition);

		statisticESObject.setFunctionConditions(functionConditions);

		final DataResult<Map<String, Number>> dataResult = esStatisticController.statisticByConditionsOMS(statisticESObject);
		if (dataResult.isFailed()) {
			System.out.println(dataResult.getStatus());
		} else {
			System.out.println(dataResult.getResult());
		}
	}
	@Test
	public void collatorTest() {
		CollapseQueryObject collapseQueryObject = new CollapseQueryObject();
		collapseQueryObject.setSystemName(systemName);
		collapseQueryObject.setIndexName(indexName);
		collapseQueryObject.setTypeName(typeName);

		collapseQueryObject.setFieldName("pushDotId");

//		PageCondition pageCondition = new PageCondition();
//		pageCondition.setPageSize(100);
//		pageCondition.setCurrentPage(1);
//		collapseQueryObject.setPageCondition(pageCondition);

		List<OrderCondition> orderConditions = new ArrayList<>();
		OrderCondition orderCondition = new OrderCondition();
		orderCondition.setFieldName("pushTime");
		orderCondition.setOrderCondition(SortEnum.DESC);
		orderConditions.add(orderCondition);
		collapseQueryObject.setOrderConditions(orderConditions);

		List<SearchCondition> searchConditions = new ArrayList<>();
		SearchCondition searchCondition = new SearchCondition();
		searchCondition.setFieldName("pushDotId");
		searchCondition.setConditionExpression(ConditionExpressionEnum.EQUAL);
		searchCondition.setSingleValue("1");
		searchConditions.add(searchCondition);
		collapseQueryObject.setSearchConditions(searchConditions);

		List<InnerHitsCondition> innerHitsConditions = new ArrayList<>();
		InnerHitsCondition innerHitsCondition = new InnerHitsCondition();
		innerHitsCondition.setHitName("oneHitName");

		innerHitsCondition.setHitSize(1);
		final List<String> strings = Arrays.asList(new String[] { "pushDotId" });
		innerHitsCondition.setFieldNames(strings);
		innerHitsCondition.setOrderConditions(orderConditions);

		innerHitsConditions.add(innerHitsCondition);
		collapseQueryObject.setInnerHitsConditions(innerHitsConditions);
		final DataResult<Map<String, ESResponse>> collapse = esStatisticController.collapseOMS(collapseQueryObject);
		System.out.println("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
		if (collapse.isSuccess()) {
			System.out.println(collapse.getResult());
		} else {
			System.out.println(collapse.getStatus());
		}
	}
}
