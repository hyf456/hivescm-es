package com.hivescm.feign.controller;

import com.hivescm.es.common.SaveESObject;
import com.hivescm.es.convert.ESSearchConvertor;
import com.hivescm.es.service.ESSearchService;
import com.hivescm.feign.dto.Computer;
import com.hivescm.feign.dto.Memory;
import com.hivescm.feign.dto.Student;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CommonEsOperate {
	private static final String INDEX_NAME = "hivescm-tms";
	private static final String TYPE_NAME = "transport-order";

	@Resource
	private ESSearchService esSearchService;

	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public String save(int id, String name,int age) throws IOException {

		SaveESObject saveESObject = new SaveESObject();
		saveESObject.setIndexName(INDEX_NAME);
		saveESObject.setTypeName(TYPE_NAME);

		Memory memory = new Memory("三星", 512);
		Computer computer = new Computer("苹果", 2017, memory);
		Student student = new Student(id, name, "黑龙江", new Date(), age, computer);

		final Map<Object, Object> objectObjectMap = ESSearchConvertor.object2Map(student);
		Map<Object, Object> ukMap = new HashMap<Object, Object>();
		ukMap.put("id", id + "");
		ukMap.put("name", name);

		saveESObject.setDataMap(objectObjectMap);
		saveESObject.setUkMap(ukMap);

		esSearchService.esSaveOMS(saveESObject);
		return "success";
	}

	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public String querey(int age, String name, int city,int relation, int field) throws IOException {
//
//		QueryESObject queryESObject = new QueryESObject();
//		queryESObject.setIndexName(INDEX_NAME);
//		queryESObject.setTypeName(TYPE_NAME);
//
//		if (field == 2) {
//			queryESObject.setNeedFields(new String[] { "city", "name", "birth", "age" });
//		} else if (field == 3) {
//			queryESObject.setNeedFields(new String[] { "city", "name" });
//		} else if(field==1){
//			queryESObject.setNeedFields(new String[] { "name", "logal" ,"computer"});
//		}else {
//			// want all !
//		}
//
//		queryESObject.setQueryRelation(RelationExpressionEnum.AND);
//
//		List<SearchCondition> searchConditions = new ArrayList<>();
//
//		SearchCondition sc1 = new SearchCondition();
//		sc1.setExpression(ConditionExpressionEnum.LESSER);
//		sc1.setFieldName("age");
//		sc1.setFieldValue(age + "");
//		searchConditions.add(sc1);
//
//		SearchCondition sc2 = new SearchCondition();
//		sc2.setExpression(ConditionExpressionEnum.EQUAL);
//		sc2.setFieldName("name");
//		sc2.setFieldValue(name);
//		searchConditions.add(sc2);
//
//		if(city==1) {
//			SearchCondition sc3 = new SearchCondition();
//			sc3.setExpression(ConditionExpressionEnum.LIKE);
//			sc3.setFieldName("city");
//			sc3.setFieldValue("黑");
//			searchConditions.add(sc3);
//		}
//		if(city==2) {
//			SearchCondition sc3 = new SearchCondition();
//			sc3.setExpression(ConditionExpressionEnum.LIKE);
//			sc3.setFieldName("city");
//			sc3.setFieldValue("京");
//			searchConditions.add(sc3);
//		}
//
//		if(city==3) {
//			SearchCondition sc3 = new SearchCondition();
//			sc3.setExpression(ConditionExpressionEnum.LIKE);
//			sc3.setFieldName("city");
//			sc3.setFieldValue("上");
//			searchConditions.add(sc3);
//		}
//
//		queryESObject.setSearchConditions(searchConditions);
//
//		final DataResult<ESResponse> responseDataResult = esSearchService.esQuery(queryESObject);
//
//		return ESSearchConvertor.object2Json(responseDataResult.getResult().getEsDocuments());
		return null;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete(int id, String name) throws IOException {

		return null;
	}
}
