//package com.hivescm.es.api.vss;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.hivescm.common.domain.DataResult;
//import com.hivescm.common.domain.Status;
//import com.hivescm.es.ESErrorCode;
//import com.hivescm.es.api.util.UtilObject;
//import com.hivescm.es.common.BatchDeleteESObject;
//import com.hivescm.es.common.BatchSaveESObject;
//import com.hivescm.es.common.BatchUpdateESObject;
//import com.hivescm.es.common.ConditionDeleteESObject;
//import com.hivescm.es.common.ConditionUpdateESObject;
//import com.hivescm.es.common.DeleteESObject;
//import com.hivescm.es.common.ESResponse;
//import com.hivescm.es.common.QueryESObject;
//import com.hivescm.es.common.SaveESObject;
//import com.hivescm.es.common.UpdateESObject;
//import com.rabbitmq.client.impl.nio.WriteRequest;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.apache.commons.lang3.StringUtils;
//import org.elasticsearch.action.update.UpdateRequestBuilder;
//import org.elasticsearch.action.update.UpdateResponse;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.xcontent.XContentType;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.util.CollectionUtils;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//import java.util.Map;
//
///**
// * @Author:
// * @Description: VssOperationService
// * @Date: 2018/4/27 17:18
// */
//@Api(value = "vss增删改操作", tags = {"vss增删改操作"})
//@RestController
//@RequestMapping(value = "/vssOperation")
//public class VssOperationService {
//    private static final Logger logger = LoggerFactory.getLogger(VssOperationService.class);
//    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
//    private static final String SYSTEM_NAME = "vss";
//    private static final String INDEX_NAME = "vss-goods-sale-1";
//    private static final String TYPE_NAME = "goods";
//
//    @Value("${es.auto.refresh:true}")
//    private Boolean autoRefresh;
//    @Value(value = "${escenter.doc.as.upsert:false}")
//    private boolean docAsUpsert;
//    @Resource
//    private TransportClient esClient;
//
//    public DataResult<Boolean> validate(SaveESObject obj) {
//        final DataResult<Boolean> baseValidateResult = null;
//        final Map<?, ?> dataMap = obj.getDataMap();
//        if (dataMap == null || dataMap.isEmpty()) {
//            logger.warn("save es object ,dataMap is null!");
//            baseValidateResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE,"存储ES, dataMap 为空"));
//        }
//        return baseValidateResult;
//    }
//
//    public DataResult<Boolean> baseValidate(String indexName, String typeName, Map<Object, Object> dataMap) {
//        DataResult<Boolean> dataResult = new DataResult<>();
//        if (StringUtils.isBlank(indexName)) {
//            logger.warn("ES 基础请求参数 indexName 为空。");
//            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "请求参数【indexName】为空"));
//        }
//        if (StringUtils.isBlank(typeName)) {
//            logger.warn("ES 基础请求参数 typeName 为空。");
//            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "请求参数【typeName】为空"));
//        }
//        if (dataMap == null || dataMap.isEmpty()) {
//            logger.warn("save es object ,dataMap is null!");
//            dataResult.setStatus(new Status(1001, "存储ES, dataMap 为空"));
//        }
//        return dataResult;
//    }
//
//    private String getId(Map<Object, Object> ukMap) {
//        return !CollectionUtils.isEmpty(ukMap) && ukMap.containsKey("id") ? String.valueOf(ukMap.get("id")) : null;
//    }
//
//    /**
//     * @Author: hanyf
//     * @Description: 更新如果不存在就新增，存在就更新
//     * @Date: 2018/5/23 17:22
//     */
//    private UpdateRequestBuilder getUpdateRequest(String indexName, String typeName, Map<Object, Object> ukMap, Map<Object, Object> data) {
//        String dataId = this.getId(ukMap);
//        byte[] dataBytes = null;
//        try {
//            dataBytes = OBJECT_MAPPER.writeValueAsBytes(data);
//        } catch (JsonProcessingException var5) {
//            logger.error("", var5);
//        }
//        UpdateRequestBuilder updateRequestBuilder = (this.esClient.prepareUpdate().setIndex(indexName)).setType(typeName).setId(String.valueOf(ukMap.get("Id")));
//        if (StringUtils.isNotBlank(dataId)) {
//            updateRequestBuilder.setId(dataId);
//        }
//        updateRequestBuilder.setDocAsUpsert(docAsUpsert);
//        updateRequestBuilder.setDoc(dataBytes, XContentType.JSON);
//        return updateRequestBuilder;
//    }
//
//    /**
//     * 新增操作
//     *
//     * @param obj 存储请求参数
//     * @return <code>true</code>操作成功，<code>false</code>重复操作
//     */
//    @ApiOperation(value = "访销保存接口", notes = "访销保存接口")
//	@RequestMapping(value = "/vssSave", method = RequestMethod.POST)
//    DataResult<Boolean> vssSave(@RequestBody SaveESObject obj) {
//        try {
//        final DataResult<Boolean> dataResult = this.validate(obj);
//        if (dataResult.isFailed()) {
//            return dataResult;
//        }
//            this.baseValidate(INDEX_NAME, TYPE_NAME, obj.getDataMap());
//            UpdateRequestBuilder ex = this.getUpdateRequest(INDEX_NAME, TYPE_NAME, obj.getUkMap(), obj.getDataMap());
//            //es写入数据是否实时刷新
//            if (this.autoRefresh) {
//                ex.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
//            }
//            UpdateResponse updateResponse = ex.execute().get();
//            logger.info(UtilObject.objectToString(updateResponse));
//            return DataResult.success(DocWriteResponse.Result.CREATED == updateResponse.getResult(), Boolean.class);
//        } catch (Exception var4) {
//            logger.error("save", var4);
//            return DataResult.faild(9999, "esMsg:" + var4.getMessage());
//        }
//    }
//
//    /**
//     * 查询操作
//     *
//     * @param obj 查询参数
//     * @return 检索到的文档集
//     */
//	@RequestMapping(value = "/vssQuery", method = RequestMethod.POST)
//    DataResult<ESResponse> vssQuery(@RequestBody final QueryESObject obj) {
//        DataResult<ESResponse> dataResult = new DataResult<>();
//        return dataResult;
//    }
//
//    /**
//     * 删除操作（根据 {@link SaveESObject#ukMap} 删除文档）
//     *
//     * @param obj 删除请求参数
//     * @return <code>true</code>操作成功，<code>false</code>操作失败
//     */
//	@RequestMapping(value = "/vssDelete", method = RequestMethod.DELETE)
//    DataResult<Boolean> vssDelete(@RequestBody final DeleteESObject obj) {
//        DataResult<Boolean> dataResult = new DataResult<>();
//        return dataResult;
//    }
//
//    /**
//     * 更新操作（根据 {@link SaveESObject#ukMap} 更新文档）
//     * <p>
//     * 1：该操作会根据{@link UpdateESObject#dataMap} 的key-value键值对更新es文档 ，当前key不存在进行新增操作，当key存在进行更新操作。
//     * 可将map中value==null值过滤掉，注意基本类型默认值问题
//     *
//     * @param obj 参数，具体参见参数属性说明
//     * @return <code>true</code>操作成功，<code>false</code>操作失败
//     */
//	@RequestMapping(value = "/vssUpdate", method = RequestMethod.PUT)
//    DataResult<Boolean> vssUpdate(@RequestBody final UpdateESObject obj) {
//        DataResult<Boolean> dataResult = new DataResult<>();
//        return dataResult;
//    }
//
//
//    /**
//     * 批量新增操作
//     *
//     * @param obj 批量新增请求参数，参照{@link #vssSave(SaveESObject)}
//     * @return <code>true</code>全部操作成功，<code>false</code>部分操作失败
//     */
//	@RequestMapping(value = "/vssBatchSave", method = RequestMethod.PUT)
//    DataResult<Boolean> vssBatchSave(@RequestBody final BatchSaveESObject obj) {
//        DataResult<Boolean> dataResult = new DataResult<>();
//        return dataResult;
//    }
//
//    /**
//     * 批量更新操作（根据 {@link SaveESObject#ukMap} 更新文档）
//     *
//     * @param obj 批量更新请求参数，参照{@link #vssUpdate(UpdateESObject)}
//     * @return <code>true</code>全部操作成功，<code>false</code>部分操作失败
//     */
//	@RequestMapping(value = "/vssBatchUpdate", method = RequestMethod.PUT)
//    DataResult<Boolean> vssBatchUpdate(@RequestBody final BatchUpdateESObject obj) {
//        DataResult<Boolean> dataResult = new DataResult<>();
//        return dataResult;
//    }
//
//    /**
//     * 批量删除操作（根据 {@link DeleteESObject#ukMap} 删除文档）
//     *
//     * @param obj 批量删除请求参数，参照{@link #vssDelete(DeleteESObject)}
//     * @return <code>true</code>全部操作成功，<code>false</code>部分操作失败
//     */
//	@RequestMapping(value = "/vssBatchDelete", method = RequestMethod.PUT)
//    DataResult<Boolean> vssBatchDelete(@RequestBody final BatchDeleteESObject obj) {
//        DataResult<Boolean> dataResult = new DataResult<>();
//        return dataResult;
//    }
//
//    /**
//     * 按条件进行更新（此 {@link SaveESObject#ukMap}条件废弃）
//     *
//     * @param obj 更新请求参数
//     * @return <code>true</code>操作成功，<code>false</code>操作失败
//     */
//	@RequestMapping(value = "/vssConditionUpdate", method = RequestMethod.PUT)
//    DataResult<Boolean> vssConditionUpdate(@RequestBody final ConditionUpdateESObject obj) {
//        DataResult<Boolean> dataResult = new DataResult<>();
//        return dataResult;
//    }
//
//    /**
//     * 按条件删除操作（此 {@link SaveESObject#ukMap}条件废弃）
//     *
//     * @param obj 删除请求参数
//     * @return <code>true</code>操作成功，<code>false</code>操作失败
//     */
//	@RequestMapping(value = "/vssConditionDelete", method = RequestMethod.DELETE)
//    DataResult<Boolean> vssConditionDelete(@RequestBody final ConditionDeleteESObject obj) {
//        DataResult<Boolean> dataResult = new DataResult<>();
//        return dataResult;
//    }
//
//}
