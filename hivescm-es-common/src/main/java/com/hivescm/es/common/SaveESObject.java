package com.hivescm.es.common;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by DongChunfu on 2017/7/26
 *
 * es 存储数请求参数
 * <p>
 * 注意：
 * 1、针对valueMap属性，value 为集合类型的实际类型需保持一致，否则会导致es索引失败；
 * 2、es 存储的浮点型数字会有精度缺失问题，建议以long或String存储相关数据；
 */
public class SaveESObject extends BaseESObject implements Serializable {
	private static final int serialVersionUID = 1;

	/**
	 * {@link SaveESObject#dataMap} 唯一标志
	 */
	private Map<Object, Object> ukMap;

	/**
	 * 文档数据内容
	 */
	private Map<Object, Object> dataMap;

	public SaveESObject() {
	}

	public Map<Object, Object> getUkMap() {
		return ukMap;
	}

	public void setUkMap(Map<Object, Object> ukMap) {
		this.ukMap = ukMap;
	}

	public Map<Object, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<Object, Object> dataMap) {
		this.dataMap = dataMap;
	}

	/**
	 * 格式未进行约束，调用方请勿尝试解析该字符串
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("SaveESObject{");
		sb.append(super.toString());
		sb.append(", ukMap=").append(ukMap);
		sb.append(", dataMap=").append(dataMap);
		sb.append('}');
		return sb.toString();
	}
}