package hanyf.nested;

import com.google.gson.Gson;
import com.hivescm.common.domain.DataResult;
import com.hivescm.es.common.ESDocument;
import com.hivescm.es.common.ESResponse;
import com.hivescm.es.common.NestedESObject;
import com.hivescm.es.common.QueryESObject;
import com.hivescm.es.common.SaveESObject;
import com.hivescm.es.common.UpdateESObject;
import com.hivescm.es.common.conditions.SearchCondition;
import com.hivescm.es.common.enums.ConditionExpressionEnum;
import com.hivescm.es.common.enums.OperateTypeEnum;
import com.hivescm.es.search.Application;
import com.hivescm.es.search.escenter.service.ESNestedSearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @Author: hanyf
 * @Description:  测试nested 多层嵌套
 * @Date: Created by in 10:16 2017/11/9
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class TestNested {

    private static final Gson GSON = new Gson();
    private static final String SYSTEM_NAME = "info";
    private static final String INDEX_NAME = "test-nested";
    private static final String TYPE_NAME = "test";

    @Autowired
    private ESNestedSearchService esSearchService;

    @Test // 新增OOL
    public void saveTest() throws IOException {
        DataResult<Boolean> save = esSearchService.save(generateSaveESObject());
        System.out.println(save);
    }

    @Test // 删除 5.5.2版本不好用
    public void deleteTest() throws Exception {
        UpdateESObject updateESObject = new UpdateESObject();
        updateESObject.setSystemName(SYSTEM_NAME);
        updateESObject.setIndexName(INDEX_NAME);
        updateESObject.setTypeName(TYPE_NAME);
        Map<Object, Object> ukMap = new HashMap<>();
        ukMap.put("id", 2043865209);
        updateESObject.setUkMap(ukMap);

        NestedESObject nestedESObject = new NestedESObject();
        nestedESObject.setFieldName("student");

        NestedESObject nextNestedESObject = new NestedESObject();
        nextNestedESObject.setFieldName("books");
        nestedESObject.setNextNestedESObject(nextNestedESObject);

        Map<Object, Object> dataMap = new HashMap<>();
        dataMap.put("id", 1);
        updateESObject.setDataMap(dataMap);

        updateESObject.setNestedESObject(nestedESObject);
        updateESObject.setNestedOperateType(OperateTypeEnum.DELETE);
        final DataResult<Boolean> update = esSearchService.update(updateESObject);
        System.out.println(update);
    }

    @Test // 新增  5.5.2版本不好用
    public void addTagTest() throws Exception {
        UpdateESObject updateESObject = new UpdateESObject();
        updateESObject.setSystemName(SYSTEM_NAME);
        updateESObject.setIndexName(INDEX_NAME);
        updateESObject.setTypeName(TYPE_NAME);

        Map<Object, Object> ukMap = new HashMap<>();
        ukMap.put("id", 548066283);
        updateESObject.setUkMap(ukMap);

        NestedESObject nestedESObject = new NestedESObject();
        nestedESObject.setFieldName("student");
        nestedESObject.setList(Boolean.TRUE);

        NestedESObject nextNestedESObject = new NestedESObject();
        nextNestedESObject.setFieldName("books");
        nestedESObject.setList(Boolean.TRUE);
        nestedESObject.setNextNestedESObject(nextNestedESObject);

        Map<Object, Object> dataMap = new HashMap<>();
        dataMap.put("student.id", 1);
        //dataMap.put("student.books.name", "redis");

        updateESObject.setDataMap(dataMap);

        updateESObject.setNestedESObject(nestedESObject);
        updateESObject.setNestedOperateType(OperateTypeEnum.ADD);

        final DataResult<Boolean> update = esSearchService.update(updateESObject);
        System.out.println(update);
    }

    @Test // 更新 5.5.2版本不好用
    public void updateTagTest() throws Exception {
        UpdateESObject updateESObject = new UpdateESObject();
        updateESObject.setSystemName(SYSTEM_NAME);
        updateESObject.setIndexName(INDEX_NAME);
        updateESObject.setTypeName(TYPE_NAME);
        Map<Object, Object> ukMap = new HashMap<>();
        ukMap.put("id", "733432282");
        updateESObject.setUkMap(ukMap);

        NestedESObject nestedESObject = new NestedESObject();
        nestedESObject.setFieldName("student");

        NestedESObject nextNestedESObject = new NestedESObject();
        nextNestedESObject.setFieldName("books");
        nestedESObject.setNextNestedESObject(nextNestedESObject);

        Map<Object, Object> dataMap = new HashMap<>();
        dataMap.put("id", 2);
        dataMap.put("name", "冰封王座");
        updateESObject.setDataMap(dataMap);

        updateESObject.setNestedESObject(nestedESObject);
        updateESObject.setNestedOperateType(OperateTypeEnum.UPDATE);

        final DataResult<Boolean> update = esSearchService.update(updateESObject);
        System.out.println(update);
    }

    @Test
    public void queryNested() throws IOException {
        QueryESObject queryOne = new QueryESObject();
        queryOne.setIndexName(INDEX_NAME);
        queryOne.setTypeName(TYPE_NAME);
        List<SearchCondition> searchConditions = new ArrayList<>();
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setFieldName("student.books.id");
        searchCondition.setConditionExpression(ConditionExpressionEnum.EQUAL);
        searchCondition.setSingleValue("1");

        SearchCondition searchCondition1 = new SearchCondition();
        searchCondition1.setFieldName("student.id");
        searchCondition1.setConditionExpression(ConditionExpressionEnum.EQUAL);
        searchCondition1.setSingleValue("1");

        searchConditions.add(searchCondition);
        searchConditions.add(searchCondition1);

        queryOne.setSearchConditions(searchConditions);

        final DataResult<ESResponse> query = esSearchService.query(queryOne);

        System.out.println(query);
    }


    @Test
    public void queryTest() throws IOException {
        QueryESObject queryOne = new QueryESObject();
        queryOne.setIndexName(INDEX_NAME);
        queryOne.setTypeName(TYPE_NAME);

        List<SearchCondition> searchConditions = new ArrayList<>();

        final SearchCondition sc1 = new SearchCondition.Builder().setFieldName("id")
                .setConditionExpression(ConditionExpressionEnum.EQUAL).setSingleValue("548066283").build();
        searchConditions.add(sc1);

        queryOne.setSearchConditions(searchConditions);
        String[] needFields = new String[]{"student.books.id","student.books.name"};
        queryOne.setNeedFields(Arrays.asList(needFields));

        final DataResult<ESResponse> query = esSearchService.query(queryOne);

        System.out.println("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
        final List<ESDocument> esDocuments = query.getResult().getEsDocuments();
        if (null != esDocuments) {
            for (ESDocument esDocument : esDocuments) {
                System.out.println("------>" + esDocument);
            }
        }
        System.out.println("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
    }

    private SaveESObject generateSaveESObject() {
        SaveESObject saveESObject = new SaveESObject();
        saveESObject.setSystemName(SYSTEM_NAME);
        saveESObject.setIndexName(INDEX_NAME);
        saveESObject.setTypeName(TYPE_NAME);

        Map<Object, Object> ukMap = new HashMap<>();
        Random r = new Random();
        final int id = Math.abs(r.nextInt());
        ukMap.put("id", id);
        saveESObject.setUkMap(ukMap);

        Map<Object, Object> dataMap = new HashMap<>();
        dataMap.put("id", id);
        dataMap.put("logol", "hanyf");

        List<Map<Object, Object>> students = new ArrayList<>();
        Map<Object, Object> student = new HashMap<>();
        student.put("id", 3);
        student.put("name", "小明");

        List<Map<Object, Object>> books = new ArrayList<>();
        Map<Object, Object> book1 = new HashMap<>();
        book1.put("id", 3);
        book1.put("name", "西游记");
        books.add(book1);

        Map<Object, Object> book2 = new HashMap<>();
        book2.put("id", 2);
        book2.put("name", "水浒传");
        books.add(book2);

        student.put("books", books);
        students.add(student);
        //dataMap.put("student", student);

        Map<Object, Object> student2 = new HashMap<>();
        student2.put("id", 1);
        student2.put("name", "hanyf");

        List<Map<Object, Object>> books2 = new ArrayList<>();
        Map<Object, Object> books21 = new HashMap<>();
        books21.put("id", 1);
        books21.put("name", "射雕");
        books2.add(books21);

        Map<Object, Object> book22 = new HashMap<>();
        book22.put("id", 2);
        book22.put("name", "神雕");
        books2.add(book22);

        student2.put("books", books2);
        students.add(student2);
        dataMap.put("student", students);
        dataMap.put("student2", students);


        saveESObject.setDataMap(dataMap);

        return saveESObject;
    }
}
