package com.hivescm.es.common.conditions;


import com.hivescm.es.common.enums.SqlFunctionEnum;

import java.io.Serializable;

/**
 * Created by DongChunfu on 2017/8/7
 *
 * SQL 函数
 */
public class FunctionCondition implements Serializable{
	private static final int serialVersionUID = 1;

	/**
	 * 字段
	 */
	private String field;

	/**
	 * 函数
	 */
	private SqlFunctionEnum function;

	/**
	 * 函数的别名，响应结果中获取响应统计结果
	 */
	private String functionName;

	public FunctionCondition() {
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public SqlFunctionEnum getFunction() {
		return function;
	}

	public void setFunction(SqlFunctionEnum function) {
		this.function = function;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("FunctionCondition{");
		sb.append("field='").append(field).append('\'');
		sb.append(", function=").append(function);
		sb.append(", functionName=").append(functionName);
		sb.append('}');
		return sb.toString();
	}
}
