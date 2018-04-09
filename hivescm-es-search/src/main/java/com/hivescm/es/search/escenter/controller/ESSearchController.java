package com.hivescm.es.search.escenter.controller;

import com.hivescm.common.domain.DataResult;
import com.hivescm.common.domain.Status;
import com.hivescm.es.ESErrorCode;
import com.hivescm.es.common.*;
import com.hivescm.es.convert.ESSearchConvertor;
import com.hivescm.es.search.escenter.util.ESUtil;
import com.hivescm.es.search.escenter.validator.ESBatchDeleteValidator;
import com.hivescm.es.search.escenter.validator.ESBatchSaveValidator;
import com.hivescm.es.search.escenter.validator.ESBatchUpdateValidator;
import com.hivescm.es.search.escenter.service.ESNestedSearchService;
import com.hivescm.es.search.escenter.service.MQService;
import com.hivescm.es.search.escenter.service.RedisService;
import com.hivescm.es.search.escenter.validator.ESConditionDeleteValidator;
import com.hivescm.es.search.escenter.validator.ESConditionUpdateValidator;
import com.hivescm.es.search.escenter.validator.ESDeleteValidator;
import com.hivescm.es.search.escenter.validator.ESQueryValidator;
import com.hivescm.es.search.escenter.validator.ESSaveValidator;
import com.hivescm.es.search.escenter.validator.ESUpdateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by dongchunfu on 2017/7/18.
 * <p>
 * es 结构化查询 控制器
 */
@RestController
@RequestMapping(value = "/hivescmes")
public class ESSearchController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ESSearchController.class);

	@Value(value = "${escenter.save.mq.exchange}")
	private String exchange;

	@Value(value = "${escenter.save.mq.routekey}")
	private String routekey = "common-es-save";

	@Resource
	private ESSaveValidator esSaveValidator;

	@Resource
	private ESQueryValidator esQueryValidator;

	@Resource
	private ESUpdateValidator esUpdateValidator;

	@Resource
	private ESDeleteValidator esDeleteValidator;

	@Resource
	private ESBatchUpdateValidator esBatchUpdateValidator;

	@Resource
	private ESBatchSaveValidator esBatchSaveValidator;

	@Resource
	private ESBatchDeleteValidator esBatchDeleteValidator;

	@Resource
	private ESConditionUpdateValidator esConditionUpdateValidator;

	@Resource
	private ESConditionDeleteValidator esConditionDeleteValidator;

	@Resource
	private ESNestedSearchService esNestedSearchService;

	@Resource
	private RedisService redisService;

	@Resource
	private MQService mqService;

	@RequestMapping(value = "/esSaveOMS", method = RequestMethod.POST)
	public DataResult<Boolean> esSaveOMS(@RequestBody SaveESObject obj) {
		LOGGER.info("esclient index request param:{}.", obj);
		final DataResult<Boolean> dataResult = esSaveValidator.validate(obj);
		if (dataResult.isFailed()) {
			return dataResult;
		}

		final String esSaveRedisKye = ESUtil.redisRepetitionKey(obj);
		final boolean needSendMq = redisService.needSendMq(esSaveRedisKye, obj);
		if (!needSendMq) {
			LOGGER.info("esclient repetition index request param:{}.", obj);
			dataResult.setStatus(new Status(ESErrorCode.REPETITION_INDEX_ERROR_CODE, "重复索引请求"));
			return dataResult;
		}

		String objJson;
		try {
			objJson = ESSearchConvertor.object2Json(obj);
		} catch (IOException ex) {
			LOGGER.error("esclient  index  obj serialize to json error,param:" + obj, ex);
			dataResult.setStatus(new Status(ESErrorCode.ESCENTER_ERROR_CODE, "参数序列化json错误" + ex.getMessage()));
			return dataResult;
		}

		try {
			final boolean sendMsg = mqService.sendMsg(exchange, routekey, objJson);
			if (!sendMsg) {
				LOGGER.info("esclient index send mq failed, param:{}.", obj);
				dataResult.setStatus(new Status(ESErrorCode.INDEX_SEND_MQ_ERROR_CODE, "发生MQ消息失败"));
				// TODO 若消息发送失败，回滚redis判重信息
				//redisService.deleteJudgeRepetitiveMq(esSaveRedisKye, obj);// 若消息发送失败，回滚redis判重信息
				return dataResult;
			}

			LOGGER.info("es save, send mq successed, param:{}.", obj);
			dataResult.setResult(Boolean.TRUE);
			return dataResult;
		} catch (Exception ex) {
			LOGGER.error("es save error, send mq failed,param:" + obj, ex);
			dataResult.setStatus(new Status(ESErrorCode.ESCENTER_ERROR_CODE, "搜索引擎异常"));
			// TODO 若消息发送失败，回滚redis判重信息
			//redisService.deleteJudgeRepetitiveMq(esSaveRedisKye, obj);// 若消息发送失败，回滚redis判重信息
			return dataResult;
		}
	}

	@RequestMapping(value = "/esQueryOMS", method = RequestMethod.POST)
	public DataResult<ESResponse> esQueryOMS(@RequestBody final QueryESObject obj) {
		LOGGER.info("esclient query req param:{}.", obj);
		DataResult<ESResponse> dataResult = new DataResult<>();

		final DataResult<Boolean> validateResult = esQueryValidator.validate(obj);
		if (validateResult.isFailed()) {
			LOGGER.warn("esclient query req param error, illegal param:{}.", obj);
			dataResult.setStatus(validateResult.getStatus());
			return dataResult;
		}
		return esNestedSearchService.query(obj);
	}

	/**
	 * @Author: hanyf
	 * @Description: 搜素条件or关联
	 * @Date: 2017/10/27 15:11
	 */
	@RequestMapping(value = "/esQueryOrOMS", method = RequestMethod.POST)
	public DataResult<ESResponse> esQueryOrOMS(@RequestBody final QueryESObject obj) {
		LOGGER.info("esclient query req param:{}.", obj);
		DataResult<ESResponse> dataResult = new DataResult<>();

		final DataResult<Boolean> validateResult = esQueryValidator.validate(obj);
		if (validateResult.isFailed()) {
			LOGGER.warn("esclient query req param error, illegal param:{}.", obj);
			dataResult.setStatus(validateResult.getStatus());
			return dataResult;
		}
		return esNestedSearchService.queryOr(obj);
	}

	@RequestMapping(value = "/esDeleteOMS", method = RequestMethod.DELETE)
	public DataResult<Boolean> esDeleteOMS(@RequestBody final DeleteESObject obj) throws IOException {
		LOGGER.info("es delete , param:{}.", obj);
		final DataResult<Boolean> dataResult = esDeleteValidator.validate(obj);
		if (dataResult.isFailed()) {
			return dataResult;
		}
		return esNestedSearchService.delete(obj);
	}

	/**
	 * ES 更新
	 * @param obj 更新es通用请求参数
	 * @return
	 */
	@RequestMapping(value = "/esUpdateOMS", method = RequestMethod.PUT)
	public DataResult<Boolean> esUpdateOMS(@RequestBody final UpdateESObject obj) {
		LOGGER.info("es update , param:{}.", obj);

		final DataResult<Boolean> dataResult = esUpdateValidator.validate(obj);
		if (dataResult.isFailed()) {
			return dataResult;
		}
		return esNestedSearchService.update(obj);
	}

	@RequestMapping(value = "/esBatchSaveOMS", method = RequestMethod.PUT)
	public DataResult<Boolean> esBatchSaveOMS(@RequestBody final BatchSaveESObject obj) {
		LOGGER.info("es batch save , param:{}.", obj);
		final DataResult<Boolean> dataResult = esBatchSaveValidator.validate(obj);
		if (dataResult.isFailed()) {
			return dataResult;
		}

		return esNestedSearchService.batchSave(obj);
	}

	@RequestMapping(value = "/esBatchUpdateOMS", method = RequestMethod.PUT)
	public DataResult<Boolean> esBatchUpdateOMS(@RequestBody final BatchUpdateESObject obj) {
		LOGGER.info("es batch update , param:{}.", obj);
		final DataResult<Boolean> dataResult = esBatchUpdateValidator.validate(obj);
		if (dataResult.isFailed()) {
			return dataResult;
		}

		return esNestedSearchService.batchUpdate(obj);
	}

	@RequestMapping(value = "/esBatchDeleteOMS", method = RequestMethod.PUT)
	public DataResult<Boolean> esBatchDeleteOMS(@RequestBody final BatchDeleteESObject obj) {
		LOGGER.info("es batch delete , param:{}.", obj);
		final DataResult<Boolean> dataResult = esBatchDeleteValidator.validate(obj);
		if (dataResult.isFailed()) {
			return dataResult;
		}

		return esNestedSearchService.batchDelete(obj);
	}

	@RequestMapping(value = "/conditionUpdateOMS", method = RequestMethod.PUT)
	public DataResult<Boolean> conditionUpdateOMS(@RequestBody final ConditionUpdateESObject obj) {
		LOGGER.info("es condition update , param:{}.", obj);
		final DataResult<Boolean> dataResult = esConditionUpdateValidator.validate(obj);
		if (dataResult.isFailed()) {
			return dataResult;
		}
		return esNestedSearchService.conditionUpdate(obj);
	}

	@RequestMapping(value = "/conditionDeleteOMS", method = RequestMethod.DELETE)
	public DataResult<Boolean> conditionDeleteOMS(@RequestBody final ConditionDeleteESObject obj) {
		LOGGER.info("es condition delete , param:{}.", obj);
		final DataResult<Boolean> dataResultResult = esConditionDeleteValidator.validate(obj);
		if (dataResultResult.isFailed()) {
			return dataResultResult;
		}

		return esNestedSearchService.conditionDelete(obj);
	}
}
