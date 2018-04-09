package hanyf.collapse;

import com.google.gson.Gson;
import com.hivescm.common.domain.DataResult;
import com.hivescm.es.common.SaveESObject;
import com.hivescm.es.search.Application;
import com.hivescm.es.search.escenter.service.ESNestedSearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Author: hanyf
 * @Description: 测试collapse
 * @Date: Created by in 13:30 2017/11/10
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class TestCollapse {

    private static final Gson GSON = new Gson();
    private static final String SYSTEM_NAME = "info";
    private static final String INDEX_NAME = "test-collapse";
    private static final String TYPE_NAME = "test";

    @Autowired
    private ESNestedSearchService esSearchService;

    @Test // 新增OOL
    public void saveTest() throws IOException {
        DataResult<Boolean> save = esSearchService.save(generateSaveESObject());
        System.out.println(save);
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
        dataMap.put("goodsId", 1);
        dataMap.put("productId", 1);
        dataMap.put("goodsName", "hanyf");
        dataMap.put("goodsCode", "hanyf");
        Integer[] goodsTags = {3,4,5};
        dataMap.put("goodsTags", goodsTags);

        saveESObject.setDataMap(dataMap);

        return saveESObject;
    }


}
