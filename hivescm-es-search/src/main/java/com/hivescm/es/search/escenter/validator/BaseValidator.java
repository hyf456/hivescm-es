package com.hivescm.es.search.escenter.validator;

import com.hivescm.common.domain.DataResult;
import com.hivescm.common.domain.Status;
import com.hivescm.es.ESErrorCode;
import com.hivescm.es.common.BaseESObject;
import com.hivescm.es.search.escenter.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by DongChunfu on 2017/7/27
 * <p>
 * 基础请求参数校验器
 */
@Component(value = "baseValidator")
public class BaseValidator {
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseValidator.class);

	public DataResult<Boolean> baseValidate(BaseESObject baseObj) {
		DataResult<Boolean> dataResult = new DataResult<>();
		if (null == baseObj) {
			dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "基础请求参数为空"));
		}

		final String systemName = baseObj.getSystemName();
		if (StringUtil.emptyString(systemName)) {
			LOGGER.warn("ES 基础请求参数 systemName 为空。");
			dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "请求参数【systemName】为空"));
		}

		final String indexName = baseObj.getIndexName();
		if (StringUtil.emptyString(indexName)) {
			LOGGER.warn("ES 基础请求参数 indexName 为空。");
			dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "请求参数【indexName】为空"));
		}
		final String typeName = baseObj.getTypeName();
		if (StringUtil.emptyString(typeName)) {
			LOGGER.warn("ES 基础请求参数 typeName 为空。");
			dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "请求参数【typeName】为空"));
		}
		return dataResult;
	}
}
