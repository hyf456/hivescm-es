package com.hivescm.es.common;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by DongChunfu on 2017/7/28
 *
 * es 删除请求参数
 */
public class DeleteESObject extends BaseESObject implements Serializable {
	private static final int serialVersionUID = 1;

	/**
	 * 文档唯一标志
	 */
	private Map<Object, Object> ukMap;

	/**
	 * 嵌套文档删除
	 */
	private Map<Object, Object> nestedMap;

	public DeleteESObject() {
	}

	public DeleteESObject(String systemName, String indexName, String typeName) {
		super(systemName,indexName,typeName);
	}

	public Map<Object, Object> getUkMap() {
		return ukMap;
	}

	public void setUkMap(Map<Object, Object> ukMap) {
		this.ukMap = ukMap;
	}

	/**
	 * 格式未进行约束，调用方请勿尝试解析该字符串
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("DeleteESObject{");
		sb.append(super.toString());
		sb.append("ukMap=").append(ukMap);
		sb.append('}');
		return sb.toString();
	}
}
