package com.hivescm.es.common;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DongChunfu on 2017/8/15
 * 批量更新请求参数
 */
public class BatchUpdateESObject implements Serializable {
	private static final long serialVersionUID = 1;

	private List<UpdateESObject> updateDatas;

	public BatchUpdateESObject() {
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public List<UpdateESObject> getUpdateDatas() {
		return updateDatas;
	}

	public void setUpdateDatas(List<UpdateESObject> updateDatas) {
		this.updateDatas = updateDatas;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("BatchUpdateESObject{");
		sb.append("updateDatas=").append(updateDatas);
		sb.append('}');
		return sb.toString();
	}
}
