package com.hivescm.es.search.escenter.validator;

import com.hivescm.common.domain.DataResult;
import com.hivescm.es.common.DeleteESObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by hanyf on 2017/7/27
 * <p>
 * 查询请求参数校验器
 */
@Component(value = "esDeleteValidator")
public class ESDeleteValidator extends BaseValidator {
	private static final Logger LOGGER = LoggerFactory.getLogger(ESDeleteValidator.class);

	public DataResult<Boolean> validate(DeleteESObject obj) {
		final DataResult<Boolean> baseValidateResult = super.baseValidate(obj);

		return baseValidateResult;
	}
}
