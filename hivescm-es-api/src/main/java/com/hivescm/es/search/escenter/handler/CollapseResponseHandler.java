package com.hivescm.es.search.escenter.handler;

import com.hivescm.es.common.CollapseQueryObject;
import com.hivescm.es.common.ESDocument;
import com.hivescm.es.common.ESResponse;
import com.hivescm.es.convert.ESSearchConvertor;
import com.hivescm.es.search.escenter.util.StringUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hanyf on 2017/9/7
 * <p>
 * 字段去重结果处理器
 */
@Component(value = "collapseResponseHandler")
public class CollapseResponseHandler {
	public Map<String, ESResponse> handler(final SearchResponse searchResponse, final CollapseQueryObject esObject) {
		final Map<String, ESResponse> responseMap = new HashMap<>();

		final SearchHits searchHits = searchResponse.getHits();
		if (searchHits == null) {
			return responseMap;
		}

		final SearchHit[] outHits = searchHits.getHits();
		if (outHits == null || outHits.length == 0) {
			return responseMap;
		}

		final List<String> hitNames = esObject.getHitNames();
		if (hitNames == null || hitNames.size() == 0) {
			return responseMap;
		}

		for (final SearchHit outHit : outHits) {
			final Map<String, SearchHits> innerHits = outHit.getInnerHits();
			for (final String hitName : hitNames) {
				final SearchHits innerSearchHits = innerHits.get(hitName);
				if (innerSearchHits == null) {
					continue;
				}

				final SearchHit[] hitResult = innerSearchHits.getHits();
				if (hitResult == null || hitResult.length == 0) {
					continue;
				}

				for (final SearchHit searchHitFields : hitResult) {
					Map dataMap = null;
					try {
						dataMap = ESSearchConvertor.json2Object(searchHitFields.getSourceAsString(), Map.class);
					} catch (IOException e) {
						e.printStackTrace();
					}

					if (dataMap != null && !dataMap.isEmpty()) {
						ESDocument esDocument = new ESDocument();
						esDocument.setDataMap(dataMap);
						ESResponse esResponse = responseMap.get(hitName);
						if (esResponse == null) {
							esResponse = new ESResponse();
							esResponse.setSystemName(esObject.getSystemName());
							esResponse.setIndexName(esObject.getIndexName());
							final String typeName = esObject.getTypeName();
							if (!StringUtil.emptyString(typeName)) {
								esResponse.setTypeName(typeName);
							}
							responseMap.put(hitName, esResponse);
						}
						List<ESDocument> esDocuments = esResponse.getEsDocuments();
						if (esDocuments == null) {
							esResponse.setEsDocuments(new ArrayList<>());
						}
						esResponse.getEsDocuments().add(esDocument);
					}
				}
			}
		}
		return responseMap;
	}
}
