package com.hivescm.es.search.escenter.validator;

import com.hivescm.common.domain.DataResult;
import com.hivescm.common.domain.Status;
import com.hivescm.es.ESErrorCode;
import com.hivescm.es.common.ConditionUpdateESObject;
import com.hivescm.es.common.conditions.SearchCondition;
import com.hivescm.es.search.escenter.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by hanyf on 2017/8/16
 * ES 按条件更新请求参数校验器
 */
@Component(value = "esConditionUpdateValidator")
public class ESConditionUpdateValidator extends BaseUpdateValidator{
	private static final Logger LOGGER = LoggerFactory.getLogger(ESConditionUpdateValidator.class);

	@Resource
	private ESUpdateValidator esUpdateValidator;

	public DataResult<Boolean> validate(ConditionUpdateESObject obj) {
		DataResult<Boolean> dataResult = new DataResult<>();
		if (null == obj) {
			LOGGER.warn("条件更新请求参数为空");
			dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE,"条件更新请求参数为空"));
		}

		final List<SearchCondition> conditions = obj.getConditions();
		if (CollectionUtil.isEmpty(conditions)) {
			LOGGER.warn("条件更新[conditions]数据为空.");
			dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE,"条件更新请求参数【conditions】为空"));
		}

		if (dataResult.isFailed()) {
			return dataResult;
		}

		return super.validate(obj);
	}
}
