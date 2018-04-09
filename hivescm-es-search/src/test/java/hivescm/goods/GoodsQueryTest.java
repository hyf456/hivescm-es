package hivescm.goods;

import com.hivescm.common.domain.DataResult;
import com.hivescm.es.common.CollapseQueryObject;
import com.hivescm.es.common.ESResponse;
import com.hivescm.es.common.QueryESObject;
import com.hivescm.es.common.conditions.FunctionCondition;
import com.hivescm.es.common.conditions.GroupByCondition;
import com.hivescm.es.common.conditions.InnerHitsCondition;
import com.hivescm.es.common.conditions.OrderCondition;
import com.hivescm.es.common.conditions.PageCondition;
import com.hivescm.es.common.conditions.SearchCondition;
import com.hivescm.es.common.enums.ConditionExpressionEnum;
import com.hivescm.es.common.enums.SortEnum;
import com.hivescm.es.common.enums.SqlFunctionEnum;
import com.hivescm.es.search.Application;
import com.hivescm.es.search.escenter.controller.ESSearchController;
import com.hivescm.es.search.escenter.service.ESStatisticServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: hanyf
 * @Description:
 * @Date: Created by in 19:19 2017/10/19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class GoodsQueryTest {

    private static final String INDEX_NAME = "scm-info-goods";
    private static final String TYPE_NAME = "goods-type";
    private static final String SYSTEM_NAME = "info";

    //@Resource
    //private ESNestedSearchService esNestedSearchService;
    @Autowired
    private ESSearchController esSearchController;
    @Autowired
    private ESStatisticServiceImpl esStatisticService;

    @Test
    public void query() throws IOException {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("managementCategoryId3", "3");
        queryMap.put("companyId", "1030");
        Map<String, String> queryMapByLike = new HashMap<>();
        queryMapByLike.put("goodsName", "3");
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
    public void queryAndOr() throws IOException {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("managementCategoryId3", "3");
        queryMap.put("companyId", "1030");
        Map<String, String> queryMapByLike = new HashMap<>();
        queryMapByLike.put("goodsName", "3");
        QueryESObject queryESObject = new QueryESObject();
        queryESObject.setIndexName(INDEX_NAME);
        queryESObject.setTypeName(TYPE_NAME);
        queryESObject.setSystemName(SYSTEM_NAME);
        //搜索条件
        List<SearchCondition> searchConditions = queryCondition(queryMap, queryMapByLike);
        queryESObject.setSearchConditions(searchConditions);
        //分页拼接
        PageCondition pageCondition = new PageCondition();
        pageCondition.setCurrentPage(1);
        pageCondition.setPageSize(10);
        queryESObject.setPageCondition(pageCondition);
        //or条件查询
        QueryESObject queryESObjectSecond = new QueryESObject();
        Map<String, String> queryMapByLikeOr = new HashMap<>();
        queryMapByLikeOr.put("goodsCode", "3");
        List<SearchCondition> searchConditions1 = queryCondition(queryMap,queryMapByLikeOr);
        queryESObjectSecond.setSearchConditions(searchConditions1);
        queryESObject.setNextGroupQuery(queryESObjectSecond);
        DataResult<ESResponse> esResponseDataResult = esSearchController.esQueryOMS(queryESObject);
        System.out.println(esResponseDataResult);
    }

    @Test
    public void queryAndOrAndSort() throws IOException {
        String categoryId = "3";
        String companyId = "1030";
        String keyword = "3";
        Integer pageNo = 1;
        Integer pageSize = 10;
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("managementCategoryId3", categoryId);
        queryMap.put("companyId", companyId);
        Map<String, String> queryMapByLike = new HashMap<>();
        if (StringUtils.isNotBlank(keyword)) {
            queryMapByLike.put("goodsName", keyword);
        }
        QueryESObject queryESObject = new QueryESObject();
        queryESObject.setIndexName(INDEX_NAME);
        queryESObject.setTypeName(TYPE_NAME);
        queryESObject.setSystemName(SYSTEM_NAME);
        //搜索条件
        List<SearchCondition> searchConditions = queryCondition(queryMap, queryMapByLike);
        queryESObject.setSearchConditions(searchConditions);
        //分页拼接
        PageCondition pageCondition = pageCondition(pageNo, pageSize);
        queryESObject.setPageCondition(pageCondition);
        //or条件查询
        if (StringUtils.isNotBlank(keyword)) {
            QueryESObject queryESObjectSecond = nextGroupQuery(queryMap, "goodsCode", keyword, null);
            QueryESObject queryESObjectThree = nextGroupQuery(queryMap, "goodsId", keyword, null);
            QueryESObject queryESObjectFour = nextGroupQuery(queryMap, "mnemonicCode", keyword, null);
            queryESObjectThree.setNextGroupQuery(queryESObjectFour);
            queryESObjectSecond.setNextGroupQuery(queryESObjectThree);
            queryESObject.setNextGroupQuery(queryESObjectSecond);
        }
        //默认排序
        defaultSort(queryESObject, "goodsId");
        DataResult<ESResponse> esResponseDataResult = esSearchController.esQueryOMS(queryESObject);
        System.out.println(esResponseDataResult);
    }


    @Test
    public void queryAndOrAndSort2() throws IOException {
        String categoryId = "3";
        String companyId = "1030";
        String keyword = "3";
        Integer pageNo = 1;
        Integer pageSize = 10;
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("managementCategoryId3", categoryId);
        queryMap.put("companyId", companyId);
        QueryESObject queryESObject = new QueryESObject();
        queryESObject.setIndexName(INDEX_NAME);
        queryESObject.setTypeName(TYPE_NAME);
        queryESObject.setSystemName(SYSTEM_NAME);
        //搜索条件
        List<SearchCondition> searchConditions = queryCondition(queryMap, null);
        queryESObject.setSearchConditions(searchConditions);
        //分页拼接
        PageCondition pageCondition = pageCondition(pageNo, pageSize);
        queryESObject.setPageCondition(pageCondition);
        //or条件查询
        if (StringUtils.isNotBlank(keyword)) {
            QueryESObject queryESObjectSecond = new QueryESObject();
            Map<String, String> queryMapByLike = new HashMap<>();
            queryMapByLike.put("goodsName", keyword);
            queryMapByLike.put("goodsCode", keyword);
            queryMapByLike.put("goodsId", keyword);
            queryMapByLike.put("mnemonicCode", keyword);
            List<SearchCondition> conditions = queryCondition(null, queryMapByLike);
            queryESObjectSecond.setSearchConditions(conditions);
            queryESObject.setNextGroupQuery(queryESObjectSecond);
        }
        //默认排序
        defaultSort(queryESObject, "goodsId");
        DataResult<ESResponse> esResponseDataResult = esSearchController.esQueryOrOMS(queryESObject);
        System.out.println(esResponseDataResult);
    }

    public List<SearchCondition> queryCondition(Map<String, String> queryMap, Map<String, String> queryMapByLike) {
        List<SearchCondition> searchConditions = new ArrayList<>();
        if (queryMap != null && !queryMap.isEmpty()) {
            for (Map.Entry<String, String> entry : queryMap.entrySet()) {
                if (StringUtils.isNotBlank(entry.getValue())) {
                    SearchCondition searchCondition = new SearchCondition();
                    searchCondition.setFieldName(entry.getKey());
                    searchCondition.setSingleValue(entry.getValue());
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

    /**
     *
     * @Author: hanyf
     * @Description: or条件嵌套
     * @Date: 2017/10/19 20:08
     * @param queryMap
     * @param fieldName
     * @param value
     * @return
     */
    public QueryESObject nextGroupQuery(Map<String, String> queryMap, String fieldName, String value, SearchCondition searchCondition) {
        QueryESObject queryESObjectSecond = new QueryESObject();
        Map<String, String> queryMapByLikeOr = new HashMap<>();
        if (StringUtils.isNotBlank(fieldName) && StringUtils.isNotBlank(value)) {
            queryMapByLikeOr.put(fieldName, value);
        }
        List<SearchCondition> searchConditions = null;
        if (null != queryMap && !queryMap.isEmpty()) {
            searchConditions = queryCondition(queryMap,queryMapByLikeOr);
        } else {
            searchConditions = queryConditionByLike(queryMapByLikeOr);
        }
        if (searchCondition != null) {
            searchConditions.add(searchCondition);
        }
        queryESObjectSecond.setSearchConditions(searchConditions);
        return queryESObjectSecond;
    }

    /**
     * @Author: hanyf
     * @Description: 默认排序条件
     * @Date: 2017/10/20 15:20
     */
    public void defaultSort(QueryESObject queryESObject, String fieldName) {
        //排序
        List<OrderCondition> orderConditionList = new ArrayList<>();
        OrderCondition orderCondition = orderCondition(fieldName, SortEnum.ASC);
        orderConditionList.add(orderCondition);
        queryESObject.setOrderConditions(orderConditionList);
    }

    /**
     * @Author: hanyf
     * @Description: 排序赋值
     * @Date: 2017/10/20 15:15
     */
    public static OrderCondition orderCondition(String fieldName, SortEnum sortEnum) {
        OrderCondition orderCondition = new OrderCondition();
        orderCondition.setFieldName(fieldName);
        orderCondition.setOrderCondition(sortEnum);
        return orderCondition;
    }

    /**
     * @Author: hanyf
     * @Description: 模糊搜索条件拼接
     * @Date: 2017/10/17 13:37
     */
    public List<SearchCondition> queryConditionByLike(Map<String, String> queryMap) {
        List<SearchCondition> searchConditions = new ArrayList<>();
        if (queryMap != null && !queryMap.isEmpty()) {
            for (Map.Entry<String, String> entry : queryMap.entrySet()) {
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

    /**
     * @Author: hanyf
     * @Description: 分页赋值
     * @Date: 2017/10/19 20:27
     */
    public static PageCondition pageCondition(Integer pageNo, Integer pageSize) {
        PageCondition pageCondition = new PageCondition();
        pageCondition.setCurrentPage(pageNo);
        pageCondition.setPageSize(pageSize);
        return pageCondition;
    }

    /**
     *
     * @Author: hanyf
     * @Description: or条件嵌套
     * @Date: 2017/10/19 20:08
     * @param queryMap
     * @param fieldName
     * @param value
     * @return
     */
    public QueryESObject nextGroupQueryList(Map<String, String> queryMap, String fieldName, String value, List<SearchCondition> searchConditionList) {
        QueryESObject queryESObjectSecond = new QueryESObject();
        Map<String, String> queryMapByLikeOr = new HashMap<>();
        if (StringUtils.isNotBlank(fieldName) && StringUtils.isNotBlank(value)) {
            queryMapByLikeOr.put(fieldName, value);
        }
        List<SearchCondition> searchConditions = null;
        if (null != queryMap && !queryMap.isEmpty()) {
            searchConditions = queryCondition(queryMap,queryMapByLikeOr);
        } else {
            searchConditions = queryConditionByLike(queryMapByLikeOr);
        }
        if (searchConditionList != null && !searchConditionList.isEmpty()) {
            for (SearchCondition searchCondition : searchConditionList) {
                searchConditions.add(searchCondition);
            }
        }
        queryESObjectSecond.setSearchConditions(searchConditions);
        return queryESObjectSecond;
    }

    @Test
    public void vssSearchByGoodsName() throws IOException {
        String name = "1";
        String goodsTags = "2,3";
        String businessCategoryId = null;
        Map<String, String> queryMap = new HashMap<>();
        if (StringUtils.isNoneBlank(businessCategoryId)) {
            queryMap.put("managementCategoryId3", String.valueOf(businessCategoryId));
        }
        Map<String, String> queryMapByLike = new HashMap<>();
        if (StringUtils.isNotBlank(name)) {
            queryMapByLike.put("goodsName", name);
        }
        QueryESObject queryESObject = new QueryESObject();
        queryESObject.setIndexName(INDEX_NAME);
        queryESObject.setTypeName(TYPE_NAME);
        queryESObject.setSystemName(SYSTEM_NAME);
        //搜索条件
        List<SearchCondition> searchConditions = queryCondition(queryMap, queryMapByLike);
        List<SearchCondition> conditions = new ArrayList<>();
        //拼接商品tag
        if (StringUtils.isNotBlank(goodsTags)) {
            String[] value = goodsTags.split(",");
            for (int i=0; i < value.length; i++) {
                SearchCondition searchCondition = new SearchCondition();
                searchCondition.setFieldName("goodsTags");
                searchCondition.setSingleValue(value[i]);
                searchCondition.setConditionExpression(ConditionExpressionEnum.EQUAL);
                searchConditions.add(searchCondition);
                conditions.add(searchCondition);
            }
        }
        queryESObject.setSearchConditions(searchConditions);
        //分页拼接
        PageCondition pageCondition = pageCondition(1, 10);
        queryESObject.setPageCondition(pageCondition);
        //or条件查询
        if (StringUtils.isNotBlank(name)) {
            QueryESObject queryESObjectSecond = nextGroupQueryList(queryMap, "mnemonicCode", name, conditions);
            queryESObject.setNextGroupQuery(queryESObjectSecond);
        }
        //排序
        List<OrderCondition> orderConditionList = new ArrayList<>();
        OrderCondition orderCondition = orderCondition("orderPrice", SortEnum.ASC);
        OrderCondition orderCondition1 = orderCondition("createTime", SortEnum.DESC);
        orderConditionList.add(orderCondition);
        orderConditionList.add(orderCondition1);
        queryESObject.setOrderConditions(orderConditionList);
        //分组
        GroupByCondition groupByCondition = new GroupByCondition();
        List<String> groupFieldName = new ArrayList<>();
        groupFieldName.add("productId");
        groupByCondition.setGroupFields(groupFieldName);
        List<FunctionCondition> functionConditions = new ArrayList<>();
        FunctionCondition functionCondition = new FunctionCondition();
        functionCondition.setField("orderPrice");
        functionCondition.setFunction(SqlFunctionEnum.MIN);
        functionCondition.setFunctionName("orderPrice");
        FunctionCondition functionCondition1 = new FunctionCondition();
        functionCondition1.setField("goodsId");
        functionCondition1.setFunction(SqlFunctionEnum.COUNT);
        functionCondition1.setFunctionName("goodsId");
        functionConditions.add(functionCondition1);
        functionConditions.add(functionCondition);
        groupByCondition.setFunctionConditions(functionConditions);
        queryESObject.setGroupByCondition(groupByCondition);
        queryESObject.setGroupAndNeedSource(true);
        DataResult<ESResponse> esResponseDataResult = esSearchController.esQueryOMS(queryESObject);
            System.out.println(esResponseDataResult);
    }


    @Test
    public void collapse() {
        String name = "1";
        String goodsTags = "2,3";
        String businessCategoryId = null;
        Map<String, String> queryMap = new HashMap<>();
        if (StringUtils.isNotBlank(businessCategoryId)) {
            queryMap.put("managementCategoryId3", String.valueOf(businessCategoryId));
        }
        Map<String, String> queryMapByLike = new HashMap<>();
        if (StringUtils.isNotBlank(name)) {
            queryMapByLike.put("goodsName", name);
        }
        CollapseQueryObject collapseQueryObject = new CollapseQueryObject();
        collapseQueryObject.setIndexName(INDEX_NAME);
        collapseQueryObject.setTypeName(TYPE_NAME);
        collapseQueryObject.setSystemName(SYSTEM_NAME);
        collapseQueryObject.setFieldName("productId");
        //搜索条件
        List<SearchCondition> searchConditions = queryCondition(queryMap, queryMapByLike);
        List<SearchCondition> conditions = new ArrayList<>();
        //拼接商品tag
        if (StringUtils.isNotBlank(goodsTags)) {
            String[] value = goodsTags.split(",");
            for (int i=0; i < value.length; i++) {
                SearchCondition searchCondition = new SearchCondition();
                searchCondition.setFieldName("goodsTags");
                searchCondition.setSingleValue(value[i]);
                searchCondition.setConditionExpression(ConditionExpressionEnum.EQUAL);
                searchConditions.add(searchCondition);
                conditions.add(searchCondition);
            }
        }
        collapseQueryObject.setSearchConditions(searchConditions);
        //分页拼接
        PageCondition pageCondition = pageCondition(1, 10);
        collapseQueryObject.setPageCondition(pageCondition);
        //排序
        List<OrderCondition> orderConditionList = new ArrayList<>();
        OrderCondition orderCondition = orderCondition("orderPrice", SortEnum.ASC);
        OrderCondition orderCondition1 = orderCondition("createTime", SortEnum.DESC);
        orderConditionList.add(orderCondition);
        orderConditionList.add(orderCondition1);
        collapseQueryObject.setOrderConditions(orderConditionList);
        //组内控制条件
        List<InnerHitsCondition> innerHitsConditionList = new ArrayList<>();
        InnerHitsCondition innerHitsCondition = new InnerHitsCondition();
        innerHitsCondition.setHitName("productId");
        List<OrderCondition> orderConditions = new ArrayList<>();
        OrderCondition orderCondition2 = new OrderCondition();
        orderCondition2.setFieldName("orderPrice");
        orderCondition2.setOrderCondition(SortEnum.ASC);
        orderConditions.add(orderCondition2);
        innerHitsCondition.setOrderConditions(orderConditions);
        innerHitsConditionList.add(innerHitsCondition);
        collapseQueryObject.setInnerHitsConditions(innerHitsConditionList);
        DataResult<Map<String, ESResponse>> collapse = esStatisticService.collapse(collapseQueryObject);
        System.out.println(collapse);
    }
}
