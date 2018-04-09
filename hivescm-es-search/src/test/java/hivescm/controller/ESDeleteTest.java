package hivescm.controller;

import com.hivescm.common.domain.DataResult;
import com.hivescm.es.common.BatchDeleteESObject;
import com.hivescm.es.common.ConditionDeleteESObject;
import com.hivescm.es.common.DeleteESObject;
import com.hivescm.es.common.conditions.SearchCondition;
import com.hivescm.es.common.enums.ConditionExpressionEnum;
import com.hivescm.es.search.Application;
import com.hivescm.es.search.escenter.controller.ESSearchController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DongChunfu on 2017/8/16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ESDeleteTest {

	private static final String SYSTEM_NAME = "ES";
	private static final String INDEX_NAME = "escenter";
	private static final String TYPE_NAME = "test";
	private DeleteESObject deleteESObject = new DeleteESObject(SYSTEM_NAME, INDEX_NAME, TYPE_NAME);
	@Autowired
	private ESSearchController esSearchController;

	@Test // 单一删除
	public void deleteTest() throws Exception {
		Map<Object, Object> objectObjectMap = new HashMap<>();
		objectObjectMap.put("id", -881143142);
		deleteESObject.setUkMap(objectObjectMap);
		final DataResult<Boolean> dataResult = esSearchController.esDeleteOMS(deleteESObject);

		System.out.println("------->" + dataResult.getResult());
	}

	@Test // 批量删除
	public void batchDeleteTest() {
		BatchDeleteESObject batchDeleteESObject = new BatchDeleteESObject();
		List<DeleteESObject> deleteESObjects = new ArrayList<>();

		DeleteESObject deleteESObject1 = new DeleteESObject(SYSTEM_NAME, INDEX_NAME, TYPE_NAME);
		Map<Object, Object> objectObjectMap1 = new HashMap<>();
		objectObjectMap1.put("id", 2085151053);
		deleteESObject1.setUkMap(objectObjectMap1);
		deleteESObjects.add(deleteESObject1);

		DeleteESObject deleteESObject2 = new DeleteESObject(SYSTEM_NAME, INDEX_NAME, TYPE_NAME);
		Map<Object, Object> objectObjectMap2 = new HashMap<>();
		objectObjectMap2.put("id", -668283549);
		deleteESObject2.setUkMap(objectObjectMap2);
		deleteESObjects.add(deleteESObject2);

		batchDeleteESObject.setDeleteDatas(deleteESObjects);

		final DataResult<Boolean> dataResult = esSearchController.esBatchDeleteOMS(batchDeleteESObject);
		System.out.println("------->" + dataResult.getResult());
	}

	@Test // 根据条件删除
	public void conditionDeleteTest() throws Exception {
		ConditionDeleteESObject conditionDeleteESObject = new ConditionDeleteESObject();
		List<SearchCondition> searchConditions = new ArrayList<>();
		SearchCondition searchCondition = new SearchCondition.Builder().setFieldName("age")
				.setConditionExpression(ConditionExpressionEnum.EQUAL).setSingleValue("35")
				.build();
		searchConditions.add(searchCondition);
		conditionDeleteESObject.setConditions(searchConditions);

		conditionDeleteESObject.setIndexName(INDEX_NAME);
		conditionDeleteESObject.setTypeName(TYPE_NAME);
		conditionDeleteESObject.setSystemName(SYSTEM_NAME);

		final DataResult<Boolean> dataResult = esSearchController.conditionDeleteOMS(conditionDeleteESObject);
		System.out.println("------->" + dataResult.getStatus());
	}
}
