package com.hivescm.es.search.vss;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hivescm.common.domain.DataResult;
import com.hivescm.common.domain.Status;
import com.hivescm.es.common.BatchSaveESParam;
import com.hivescm.es.common.ESErrorCode;
import com.hivescm.es.common.ESSearchConvertor;
import com.hivescm.es.common.SaveESParam;
import com.hivescm.es.common.VssNestedScritpUpdateParam;
import com.hivescm.es.search.util.UtilObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:
 * @Description: VssOperationService
 * @Date: 2018/4/27 17:18
 */
@Api(value = "vss增删改操作", tags = {"vss增删改操作"})
@RestController
@RequestMapping(value = "/vssOperation")
public class VssOperationService {
    private static final Logger logger = LoggerFactory.getLogger(VssOperationService.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String SYSTEM_NAME = "vss";
    private static final String INDEX_NAME = "vss-goods-sale-1";
    private static final String TYPE_NAME = "goods";

    @Value("${es.auto.refresh:true}")
    private Boolean autoRefresh;
    @Value(value = "${escenter.doc.as.upsert:true}")
    private boolean docAsUpsert;
    @Resource
    private TransportClient esClient;

    public DataResult<Boolean> validate(SaveESParam obj) {
        final DataResult<Boolean> baseValidateResult = null;
        final Object dataMap = obj.getDataMap();
        if (dataMap == null) {
            logger.warn("save es object ,dataMap is null!");
            baseValidateResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "存储ES, dataMap 为空"));
        }
        return baseValidateResult;
    }

    public DataResult<Boolean> baseValidate(String indexName, String typeName, Map<Object, Object> dataMap) {
        DataResult<Boolean> dataResult = new DataResult<>();
        if (StringUtils.isBlank(indexName)) {
            logger.warn("ES 基础请求参数 indexName 为空。");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "请求参数【indexName】为空"));
        }
        if (StringUtils.isBlank(typeName)) {
            logger.warn("ES 基础请求参数 typeName 为空。");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "请求参数【typeName】为空"));
        }
        if (dataMap == null || dataMap.isEmpty()) {
            logger.warn("save es object ,dataMap is null!");
            dataResult.setStatus(new Status(1001, "存储ES, dataMap 为空"));
        }
        return dataResult;
    }

    private String getId(Map<Object, Object> ukMap) {
        return !CollectionUtils.isEmpty(ukMap) && ukMap.containsKey("id") ? String.valueOf(ukMap.get("id")) : null;
    }

    /**
     * @Author: hanyf
     * @Description: 更新如果不存在就新增，存在就更新
     * @Date: 2018/5/23 17:22
     */
    private UpdateRequestBuilder getUpdateRequest(String indexName, String typeName, Map<Object, Object> ukMap, Map<Object, Object> data) {
        String dataId = this.getId(ukMap);
        byte[] dataBytes = null;
        try {
            dataBytes = OBJECT_MAPPER.writeValueAsBytes(data);
        } catch (JsonProcessingException var5) {
            logger.error("", var5);
        }
        UpdateRequestBuilder updateRequestBuilder = (this.esClient.prepareUpdate().setIndex(indexName)).setType(typeName).setId(String.valueOf(ukMap.get("Id")));
        if (StringUtils.isNotBlank(dataId)) {
            updateRequestBuilder.setId(dataId);
        }
        updateRequestBuilder.setDocAsUpsert(docAsUpsert);
        updateRequestBuilder.setDoc(dataBytes, XContentType.JSON);
        return updateRequestBuilder;
    }

    /**
     * 新增操作
     *
     * @param obj 存储请求参数，如果不存在就新增，存在就更新
     * @return <code>true</code>操作成功，<code>false</code>重复操作
     */
    @ApiOperation(value = "访销保存接口", notes = "访销保存接口")
    @RequestMapping(value = "/vssSave", method = RequestMethod.POST)
    DataResult<Boolean> vssSave(@RequestBody SaveESParam obj) {
        try {
            final DataResult<Boolean> dataResult = this.validate(obj);
            if (dataResult.isFailed()) {
                return dataResult;
            }
            Map<Object, Object> objectObjectMap = null;
            if (obj.getExcludeNullFlag()) {
                objectObjectMap = ESSearchConvertor.object2Map(obj.getDataMap());
            } else {
                objectObjectMap = ESSearchConvertor.object2MapExcludeNullValue(obj.getDataMap());
            }
            this.baseValidate(INDEX_NAME, TYPE_NAME, objectObjectMap);
            UpdateRequestBuilder ex = this.getUpdateRequest(INDEX_NAME, TYPE_NAME, obj.getUkMap(), objectObjectMap);
            //es写入数据是否实时刷新
            if (this.autoRefresh) {
                ex.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
            }
            UpdateResponse updateResponse = ex.execute().get();
            logger.info(UtilObject.objectToString(updateResponse));
            return DataResult.success(DocWriteResponse.Result.CREATED == updateResponse.getResult(), Boolean.class);
        } catch (Exception var4) {
            logger.error("save", var4);
            final String message = var4.getMessage();
            if (message != null && message.contains("document missing")) {
                return DataResult.faild(ESErrorCode.DOC_NOT_EXIST_ERROR_CODE, "更新文档不存在");
            }
            return DataResult.faild(9999, "esMsg:" + var4.getMessage());
        }
    }

    /**
     * 删除操作（根据 {@link SaveESParam#ukMap} 删除文档）
     *
     * @param id 删除请求参数
     * @return <code>true</code>操作成功，<code>false</code>操作失败
     */
    @RequestMapping(value = "/vssDelete", method = RequestMethod.DELETE)
    DataResult<Boolean> vssDelete(@RequestParam(value = "id") String id) {
        DataResult<Boolean> dataResult = new DataResult<>();
        DeleteResponse deleteResponse = this.esClient.prepareDelete().setIndex(INDEX_NAME)
                .setType(TYPE_NAME).setId(id).execute().actionGet();
        if (DocWriteResponse.Result.DELETED == deleteResponse.getResult()) {
            return DataResult.success(Boolean.TRUE, Boolean.class);
        }
        if (DocWriteResponse.Result.NOT_FOUND == deleteResponse.getResult()) {
            return DataResult.success(Boolean.FALSE, Boolean.class);
        }
        return DataResult.faild(ESErrorCode.ELASTIC_ERROR_CODE, "ES返回结果不在预期范围内");
    }


    /**
     * 批量新增操作
     *
     * @param batchSaveESParam 批量新增请求参数，更新如果不存在就新增，存在就更新，参照{@link #vssSave(SaveESParam)}
     * @return <code>true</code>全部操作成功，<code>false</code>部分操作失败
     */
    @RequestMapping(value = "/vssBatchSave", method = RequestMethod.PUT)
    DataResult<Boolean> vssBatchSave(@RequestBody final BatchSaveESParam batchSaveESParam) {
        final BulkRequestBuilder bulkRequestBuilder = esClient.prepareBulk();
        try {
            List<SaveESParam> saveDatas = batchSaveESParam.getSaveDatas();
            if (null != saveDatas && !saveDatas.isEmpty()) {
                for (SaveESParam data : saveDatas) {
                    Map<Object, Object> objectObjectMap = null;
                    if (data.getExcludeNullFlag()) {
                        objectObjectMap = ESSearchConvertor.object2Map(data.getDataMap());
                    } else {
                        objectObjectMap = ESSearchConvertor.object2MapExcludeNullValue(data.getDataMap());
                    }
                    this.baseValidate(INDEX_NAME, TYPE_NAME, objectObjectMap);
                    UpdateRequestBuilder updateRequest = this.getUpdateRequest(INDEX_NAME, TYPE_NAME, data.getUkMap(), objectObjectMap);
                    bulkRequestBuilder.add(updateRequest);
                }
            } else {
                logger.error("vssBatchSave参数为空");
                return DataResult.faild(9999, "esMsg:vssBatchSave参数为空");
            }
            //es写入数据是否实时刷新
            if (this.autoRefresh) {
                bulkRequestBuilder.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
            }
            logger.info(UtilObject.objectToString(bulkRequestBuilder));
            BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
            logger.info(UtilObject.objectToString(bulkResponse));
            return DataResult.success(!bulkResponse.hasFailures(), Boolean.class);
        } catch (Exception var4) {
            logger.error("save", var4);
            return DataResult.faild(9999, "esMsg:" + var4.getMessage());
        }
    }

    /**
     * 根据条件在嵌套集合中添加某个值（此 {@link SaveESParam#ukMap}条件废弃）
     *
     * @param vssNestedScritpUpdateParam
     * @return <code>true</code>操作成功，<code>false</code>操作失败
     */
    @RequestMapping(value = "/vssConditionUpdate", method = RequestMethod.PUT)
    DataResult<Boolean> vssConditionUpdate(@RequestBody VssNestedScritpUpdateParam vssNestedScritpUpdateParam) {
        try {
            if (null == vssNestedScritpUpdateParam) {
                logger.warn("基础请求参数为空。");
                return DataResult.faild(9999, "基础请求参数为空。");
            }
            if (null == vssNestedScritpUpdateParam.getSalesmanId()) {
                logger.warn("基础请求参数 salesmanId 为空。");
                return DataResult.faild(9999, "基础请求参数 salesmanId 为空。");
            }
            if (null == vssNestedScritpUpdateParam.getVssGoodsSaleParam()) {
                logger.warn("基础请求参数 VssGoodsSaleParam 为空。");
                return DataResult.faild(9999, "基础请求参数 VssGoodsSaleParam 为空。");
            }
            if (null == vssNestedScritpUpdateParam.getVssGoodsSaleParam().getId()) {
                logger.warn("基础请求参数 VssGoodsSaleParam.id 为空。");
                return DataResult.faild(9999, "基础请求参数 VssGoodsSaleParam.id 为空。");
            }
            String scriptString = "ctx._source.salesman.add(params.salesman)";
            String queryString = QueryBuilders.termQuery("goodsCategoryId", vssNestedScritpUpdateParam.getGoodsCategoryId()).toString();
            Map<String, Object> salesman = new HashMap(2);
            salesman.put("id", 10);
            salesman.put("salesmanName", "业务员10");
            Map<String, Object> params = new HashMap<>(1);
            params.put("salesman", ESSearchConvertor.object2Map(vssNestedScritpUpdateParam.getVssGoodsSaleParam()));
            boolean b = this.multiUpdateByQueryAction(INDEX_NAME, TYPE_NAME,
                    queryString, scriptString, params);
            if (b) {
                return DataResult.success(b, Boolean.class);
            } else {
                return DataResult.faild(9999, "按条件进行更新失败！");
            }
        } catch (IOException ioe) {
            logger.error("vssConditionUpdate", ioe);
            return DataResult.faild(9999, "esMsg:" + ioe.getMessage());
        } catch (Exception e) {
            logger.error("vssConditionUpdate", e);
            return DataResult.faild(9999, "esMsg:" + e.getMessage());
        }
    }

    /**
     * 按条件删除操作（此 {@link SaveESParam#ukMap}条件废弃）
     *
     * @param vssNestedScritpUpdateParam 删除请求参数
     * @return <code>true</code>操作成功，<code>false</code>操作失败
     */
    @RequestMapping(value = "/vssConditionDelete", method = RequestMethod.DELETE)
    DataResult<Boolean> vssConditionDelete(@RequestBody VssNestedScritpUpdateParam vssNestedScritpUpdateParam) {
        if (null == vssNestedScritpUpdateParam) {
            logger.warn("基础请求参数为空。");
            return DataResult.faild(9999, "基础请求参数为空。");
        }
        if (null == vssNestedScritpUpdateParam.getGoodsCategoryId()) {
            logger.warn("基础请求参数 goodsCategoryId 为空。");
            return DataResult.faild(9999, "基础请求参数 goodsCategoryId 为空。");
        }
        String scriptString = "ctx._source.salesman.removeIf(salesman -> salesman.id == params.id)";
        String queryString = QueryBuilders.nestedQuery("salesman", QueryBuilders.termQuery("salesman.id", vssNestedScritpUpdateParam.getSalesmanId()), ScoreMode.None).toString();
        Map<String, Object> params = new HashMap<>(2);
        params.put("id", vssNestedScritpUpdateParam.getSalesmanId());
        boolean b = this.multiUpdateByQueryAction(INDEX_NAME, TYPE_NAME,
                queryString, scriptString, params);
        if (b) {
            return DataResult.success(b, Boolean.class);
        } else {
            return DataResult.faild(9999, "按条件进行更新失败！");
        }
    }


    /**
     * 根据查询条件和脚本实现修改内容，可支持修改嵌套类型修改
     * @param index
     * @param type
     * @param queryString json格式化查询语句
     * @param scriptString script脚本
     * @param params 参数集合
     * @return
     */
    public Boolean multiUpdateByQueryAction(String index, String type, String queryString, String scriptString, Map<String, Object> params) {
        if (StringUtils.isEmpty(scriptString)) {
            return false;
        }
        Script script = new Script(ScriptType.INLINE, "painless", scriptString, params);
        UpdateByQueryRequestBuilder builder = UpdateByQueryAction.INSTANCE.newRequestBuilder(esClient);
        builder.source(index).script(script);
        if (!StringUtils.isEmpty(queryString)) {
            QueryBuilder queryBuilder = QueryBuilders.wrapperQuery(queryString);
            builder.filter(queryBuilder);
        }
        builder.source().setTypes(type);
        BulkByScrollResponse response = builder.execute().actionGet();
        System.out.println("builder"+builder);
        TimeValue took = response.getTook();
        System.out.println("took" + took);
        boolean b = !(response.getBulkFailures().size() > 0);
        return b;
    }

}
