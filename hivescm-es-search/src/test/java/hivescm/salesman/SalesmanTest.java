package hivescm.salesman;

import com.hivescm.common.domain.DataResult;
import com.hivescm.es.common.ESResponse;
import com.hivescm.es.common.NestedESObject;
import com.hivescm.es.common.QueryESObject;
import com.hivescm.es.common.SaveESObject;
import com.hivescm.es.common.UpdateESObject;
import com.hivescm.es.common.conditions.SearchCondition;
import com.hivescm.es.common.enums.ConditionExpressionEnum;
import com.hivescm.es.common.enums.OperateTypeEnum;
import com.hivescm.es.search.Application;
import com.hivescm.es.search.escenter.controller.ESSearchController;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @Author: hanyf
 * @Description:
 * @Date: Created by in 16:09 2017/10/31
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class})
public class SalesmanTest {

    private static final String SYSTEM_NAME = "info";
    private static final String INDEX_NAME = "hockey";
    private static final String TYPE_NAME = "player";

    @Autowired
    private ESSearchController esSearchController;


    @Test // 保存
    public void saveTest() throws IOException {
        esSearchController.esSaveOMS(generateSaveESObject());
    }

    private SaveESObject generateSaveESObject() {
        SaveESObject saveESObject = new SaveESObject();
        saveESObject.setSystemName(SYSTEM_NAME);
        saveESObject.setIndexName(INDEX_NAME);
        saveESObject.setTypeName(TYPE_NAME);

        Map<Object, Object> ukMap = new HashMap<>();
        Random r = new Random();
        final int id = r.nextInt();
        ukMap.put("id", id);
        saveESObject.setUkMap(ukMap);

        Map<Object, Object> dataMap = new HashMap<>();
        dataMap.put("user_id", id);
        String[] array = {"4", "5", "6", "7"};
        List<Integer> array1 = new ArrayList<>();
        array1.add(4);
        array1.add(8);
        array1.add(8);
        array1.add(7);
        Map<String, Object> map = new HashMap<>();
        dataMap.put("array", array1);
        dataMap.put("date", new Date());
        dataMap.put("last", "thinkpad");
        dataMap.put("name", "4,5,6,7");
        dataMap.put("tweet", "thinkpad");
        saveESObject.setDataMap(dataMap);

        return saveESObject;
    }

    @Test
    public void objectDatatypeTest() throws IOException {

        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("array", "4");
        //queryMap.put("testnested.id", "2");
        Map<String, String> queryMapByLike = new HashMap<>();
        QueryESObject queryESObject = new QueryESObject();
        queryESObject.setIndexName(INDEX_NAME);
        queryESObject.setTypeName(TYPE_NAME);
        queryESObject.setSystemName(SYSTEM_NAME);
        //搜索条件
        List<SearchCondition> searchConditions = queryCondition(queryMap, queryMapByLike);
        queryESObject.setSearchConditions(searchConditions);
        DataResult<ESResponse> esResponseDataResult = esSearchController.esQueryOMS(queryESObject);
        System.out.println(esResponseDataResult);
    }

    @Test
    public void updateTest() {
        UpdateESObject updateESObject = new UpdateESObject();
        updateESObject.setSystemName(SYSTEM_NAME);
        updateESObject.setIndexName(INDEX_NAME);
        updateESObject.setTypeName(TYPE_NAME);
        Map<Object, Object> ukMap = new HashMap<>();
        ukMap.put("id", "1603157154");
        updateESObject.setUkMap(ukMap);

        Map<Object, Object> dataMap = new HashMap<>();
        List<Map<Object, Object>> books2 = new ArrayList<>();
        Map<Object, Object> book3 = new HashMap<>();
        book3.put("id", 2);
        books2.add(book3);

        dataMap.put("testnested", books2);
        updateESObject.setDataMap(dataMap);


        NestedESObject nestedESObject = new NestedESObject();
        nestedESObject.setIdValues(new String[]{"1"});
        nestedESObject.setFieldName("testnested");
        nestedESObject.setList(Boolean.TRUE);

        updateESObject.setDataMap(dataMap);

        updateESObject.setNestedESObject(nestedESObject);
        updateESObject.setNestedOperateType(OperateTypeEnum.DELETE);

        final DataResult<Boolean> update = esSearchController.esUpdateOMS(updateESObject);
        System.out.println(update);
    }

    @Test // 删除 （测试通过）
    public void deleteTest() throws Exception {
        UpdateESObject updateESObject = new UpdateESObject();
        updateESObject.setSystemName(SYSTEM_NAME);
        updateESObject.setIndexName(INDEX_NAME);
        updateESObject.setTypeName(TYPE_NAME);

        Map<Object, Object> ukMap = new HashMap<>();
        ukMap.put("id", 1603157144 );
        updateESObject.setUkMap(ukMap);

        NestedESObject nestedESObject = new NestedESObject();
        nestedESObject.setList(Boolean.TRUE);
        nestedESObject.setFieldName("testnested");

        Map<Object, Object> dataMap = new HashMap<>();
        dataMap.put("id", 1);

        updateESObject.setDataMap(dataMap);

        updateESObject.setNestedESObject(nestedESObject);
        updateESObject.setNestedOperateType(OperateTypeEnum.DELETE);

        final DataResult<Boolean> update = esSearchController.esUpdateOMS(updateESObject);
        System.out.println(update);
    }

    public List<SearchCondition> queryCondition(Map<String, String> queryMap, Map<String, String> queryMapByLike) {
        List<SearchCondition> searchConditions = new ArrayList<>();
        if (queryMap != null && !queryMap.isEmpty()) {
            for (Map.Entry<String, String> entry : queryMap.entrySet()) {
                if (StringUtils.isNotBlank(entry.getValue())) {
                    SearchCondition searchCondition = new SearchCondition();
                    searchCondition.setFieldName(entry.getKey());
                    searchCondition.setSingleValue(entry.getValue());
                    //searchCondition.setMultipleValue(true);
                    searchCondition.setConditionExpression(ConditionExpressionEnum.EQUAL);
                    searchConditions.add(searchCondition);
                }
            }
        }
        if (queryMapByLike != null && !queryMapByLike.isEmpty()) {
            for (Map.Entry<String, String> entry : queryMapByLike.entrySet()) {
                if (StringUtils.isNotBlank(entry.getValue())) {
                    SearchCondition searchCondition = new SearchCondition();
                    searchCondition.setFieldName(entry.getKey());
                    searchCondition.setSingleValue(entry.getValue());
                    searchCondition.setConditionExpression(ConditionExpressionEnum.LIKE);
                    searchConditions.add(searchCondition);
                }
            }
        }
        return searchConditions;
    }

}
