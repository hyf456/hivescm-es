package com.hivescm.es.common;


import com.hivescm.es.common.conditions.SearchCondition;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DongChunfu on 2017/8/16
 *
 * 按条件更新 请求参数
 */
public class ConditionUpdateESObject extends UpdateESObject implements Serializable{
	private static final long serialVersionUID = 1;

	/**
	 * 筛选条件
	 */
	private List<SearchCondition> conditions;

	public ConditionUpdateESObject() {
	}

	public List<SearchCondition> getConditions() {
		return conditions;
	}

	public void setConditions(List<SearchCondition> conditions) {
		this.conditions = conditions;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ConditionUpdateESObject{");
		sb.append(super.toString());
		sb.append("conditions=").append(conditions);
		sb.append('}');
		return sb.toString();
	}
}
