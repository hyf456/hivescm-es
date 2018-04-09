package com.hivescm.es.common.conditions;


import com.hivescm.es.common.enums.ConditionExpressionEnum;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by DongChunfu on 2017/7/28
 * <p>
 * es 检索条件
 */
public class SearchCondition implements Serializable {
	private static final int serialVersionUID = 1;

	/**
	 * 字段名称（扁平处理后的）
	 */
	private String fieldName;

	/**
	 * 仅在单一值查询时有效
	 */
	private String singleValue;

	/**
	 * 是否为多值
	 * <p>
	 * {@link SearchCondition#fieldName} 当时存储时是否为集合类型数值，若是，是否已经更改了映射，若已经更改映射类型为nested，请在此声明为true，以便精确匹配
	 */
	private boolean multipleValue;

	/**
	 * 仅在范围查询时有效
	 * 范围查询时的最小值
	 */
	private String minValue;

	/**
	 * 仅在范围查询时有效
	 * 范围查询时的最大值
	 */
	private String maxValue;

	/**
	 * 仅在 IN & NOT_IN 查询时有效
	 */
	private String[] fieldValues;

	/**
	 * 链接表达式 {@link ConditionExpressionEnum}
	 */
	private ConditionExpressionEnum conditionExpression;

	/**
	 * 无参构造器
	 */
	public SearchCondition() {
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getSingleValue() {
		return singleValue;
	}

	public void setSingleValue(String singleValue) {
		this.singleValue = singleValue;
	}

	public boolean multipleValue() {
		return multipleValue;
	}

	public void setMultipleValue(boolean multipleValue) {
		this.multipleValue = multipleValue;
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	public String[] getFieldValues() {
		return fieldValues;
	}

	public void setFieldValues(String[] fieldValues) {
		this.fieldValues = fieldValues;
	}

	public ConditionExpressionEnum getConditionExpression() {
		return conditionExpression;
	}

	public void setConditionExpression(ConditionExpressionEnum conditionExpression) {
		this.conditionExpression = conditionExpression;
	}

	public static class Builder {
		/**
		 * 字段名称
		 */
		private String fieldName;

		/**
		 * 仅在单一值查询时有效
		 * <p>
		 * 模糊查询时请拼接字符，？表示任一一个字符，*表示任意多个字符，若有特殊字符请使用'\'转义
		 */
		private String singleValue;

		/**
		 * 是否为多值
		 * <p>
		 * {@link SearchCondition#fieldName} 当时存储时是否为集合类型数值，若是，是否已经更改了映射，若已经更改映射类型，请在此声明为true，以便精确匹配
		 */
		private boolean multipleValue;

		/**
		 * 仅在范围查询时有效
		 * 范围查询时的最小值
		 */
		private String minValue;

		/**
		 * 仅在范围查询时有效
		 * 范围查询时的最大值
		 */
		private String maxValue;

		/**
		 * 仅在 IN & NOT_IN 查询时有效
		 */
		private String[] fieldValues;

		/**
		 * 链接表达式 {@link ConditionExpressionEnum}
		 */
		private ConditionExpressionEnum conditionExpression;

		public Builder() {
		}

		public SearchCondition build() {
			SearchCondition searchCondition = new SearchCondition();
			searchCondition.setFieldName(fieldName);
			searchCondition.setSingleValue(singleValue);
			searchCondition.setMultipleValue(multipleValue);
			searchCondition.setConditionExpression(conditionExpression);
			searchCondition.setFieldValues(fieldValues);
			searchCondition.setMinValue(minValue);
			searchCondition.setMaxValue(maxValue);
			return searchCondition;
		}

		public Builder setFieldName(String fieldName) {
			this.fieldName = fieldName;
			return this;
		}

		public Builder setSingleValue(String singleValue) {
			this.singleValue = singleValue;
			return this;
		}

		public Builder setMinValue(String minValue) {
			this.minValue = minValue;
			return this;
		}

		public Builder setMxValue(String maxValue) {
			this.maxValue = maxValue;
			return this;
		}

		public Builder setFeldValues(String[] fieldValues) {
			this.fieldValues = fieldValues;
			return this;
		}

		public Builder setMultipleValue(boolean multipleValue) {
			this.multipleValue = multipleValue;
			return this;
		}

		public Builder setConditionExpression(ConditionExpressionEnum conditionExpression) {
			this.conditionExpression = conditionExpression;
			return this;
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("SearchCondition{");
		sb.append("fieldName='").append(fieldName).append('\'');
		sb.append(", singleValue='").append(singleValue).append('\'');
		sb.append(", multipleValue=").append(multipleValue);
		sb.append(", minValue='").append(minValue).append('\'');
		sb.append(", maxValue='").append(maxValue).append('\'');
		sb.append(", fieldValues=").append(Arrays.toString(fieldValues));
		sb.append(", conditionExpression=").append(conditionExpression);
		sb.append('}');
		return sb.toString();
	}
}
