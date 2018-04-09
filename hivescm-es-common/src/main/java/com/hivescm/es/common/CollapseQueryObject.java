package com.hivescm.es.common;

import com.hivescm.es.common.conditions.InnerHitsCondition;
import com.hivescm.es.common.conditions.OrderCondition;
import com.hivescm.es.common.conditions.PageCondition;
import com.hivescm.es.common.conditions.SearchCondition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DongChunfu on 2017/9/7
 * <p>
 * 瓦解查询,有点意思，来源es
 * <p>
 * 依据单一字段值进行分组，对每组文档进行筛选若干条组成结果集返回
 */
public class CollapseQueryObject extends BaseESObject implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 数据去重 域 名
	 * <p>
	 * 该字段映射类型为 keyword | numeric
	 */
	private String fieldName;

	/**
	 * 过滤条件，内部均使用 AND 链接
	 */
	private List<SearchCondition> searchConditions;

	/**
	 * 分页条件，作用与组，对于最终返回文档数目取决于，每组返回的数目
	 */
	private PageCondition pageCondition;

	/**
	 * 外部排序条件
	 */
	private List<OrderCondition> orderConditions;

	/**
	 * 组内控制条件，内部击中条件控制，目前用于去重使用
	 * <p>
	 * 默认值为取每组的第一个文档
	 */
	private List<InnerHitsCondition> innerHitsConditions;

	public CollapseQueryObject() {
	}

	public CollapseQueryObject(String systemName, String indexName, String typeName) {
		super(systemName, indexName, typeName);
	}

	/**
	 * 获取 内部查询所有名字
	 *
	 * @return hitNames
	 */
	public List<String> getHitNames() {
		List<String> hitNames = new ArrayList<>();

		if (innerHitsConditions == null || innerHitsConditions.size() == 0) {
			return hitNames;
		}

		for (InnerHitsCondition innerHitsCondition : innerHitsConditions) {
			hitNames.add(innerHitsCondition.getHitName());
		}
		return hitNames;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public List<SearchCondition> getSearchConditions() {
		return searchConditions;
	}

	public void setSearchConditions(List<SearchCondition> searchConditions) {
		this.searchConditions = searchConditions;
	}

	public PageCondition getPageCondition() {
		return pageCondition;
	}

	public void setPageCondition(PageCondition pageCondition) {
		this.pageCondition = pageCondition;
	}

	public List<OrderCondition> getOrderConditions() {
		return orderConditions;
	}

	public void setOrderConditions(List<OrderCondition> orderConditions) {
		this.orderConditions = orderConditions;
	}

	public List<InnerHitsCondition> getInnerHitsConditions() {
		return innerHitsConditions;
	}

	public void setInnerHitsConditions(List<InnerHitsCondition> innerHitsConditions) {
		this.innerHitsConditions = innerHitsConditions;
	}

	/**
	 * 格式未进行约束，调用方请勿尝试解析该字符串
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("CollapseQueryObject{");
		sb.append(super.toString()).append(",");
		sb.append("fieldName='").append(fieldName).append('\'');
		sb.append(", searchConditions=").append(searchConditions);
		sb.append(", pageCondition=").append(pageCondition);
		sb.append(", orderConditions=").append(orderConditions);
		sb.append(", innerHitsConditions=").append(innerHitsConditions);
		sb.append('}');
		return sb.toString();
	}
}