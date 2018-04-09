package com.hivescm.es.common;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DongChunfu on 2017/8/16
 * 批量删除请求参数
 */
public class BatchDeleteESObject implements Serializable {
	private static final long serialVersionUID = 1;

	private List<DeleteESObject> deleteDatas;

	public BatchDeleteESObject() {
	}

	public List<DeleteESObject> getDeleteDatas() {
		return deleteDatas;
	}

	public void setDeleteDatas(List<DeleteESObject> deleteDatas) {
		this.deleteDatas = deleteDatas;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("BatchDeleteESObject{");
		sb.append("deleteDatas=").append(deleteDatas);
		sb.append('}');
		return sb.toString();
	}
}
