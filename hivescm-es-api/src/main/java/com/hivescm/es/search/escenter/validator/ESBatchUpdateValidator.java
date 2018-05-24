package com.hivescm.es.search.escenter.validator;

import com.hivescm.common.domain.DataResult;
import com.hivescm.common.domain.Status;
import com.hivescm.es.ESErrorCode;
import com.hivescm.es.common.BatchUpdateESObject;
import com.hivescm.es.common.UpdateESObject;
import com.hivescm.es.search.escenter.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by hanyf on 2017/8/16
 * <p>
 * 批量更新请求参数校验器
 */
@Component(value = "esBatchUpdateValidator")
public class ESBatchUpdateValidator extends BaseValidator {
	private static final Logger LOGGER = LoggerFactory.getLogger(ESBatchUpdateValidator.class);

	@Resource
	private ESUpdateValidator esUpdateValidator;

	public DataResult<Boolean> validate(BatchUpdateESObject obj) {
		DataResult<Boolean> dataResult = new DataResult<>();
		if (null == obj) {
			LOGGER.warn("批量更新请求参数为空");
			dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE,"批量更新请求参数为空"));
		}
		final List<UpdateESObject> updateDatas = obj.getUpdateDatas();
		if (CollectionUtil.isEmpty(updateDatas)) {
			LOGGER.warn("批量更新[updateDatas]数据为空.");
			dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE,"批量更新请求参数【updateDatas】为空"));
		}

		if (dataResult.isFailed()) {
			return dataResult;
		}

		for (UpdateESObject updateData : updateDatas) {
			final DataResult<Boolean> validateResult = esUpdateValidator.validate(updateData);
			if (validateResult.isFailed()) {
				return validateResult;
			}
		}

		return dataResult;
	}
}