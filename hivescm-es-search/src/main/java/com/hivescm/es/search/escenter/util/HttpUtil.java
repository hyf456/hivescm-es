package com.hivescm.es.search.escenter.util;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.Map;

/**
 * Created by DongChunfu on 2017/8/21
 */
public class HttpUtil {

	private static final Gson GSON = new Gson();

	public static void httpPost(String url, Map<String, Object> param) throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost method = new HttpPost(url);
		//解决中文乱码问题
		StringEntity entity = new StringEntity(GSON.toJson(param), "utf-8");
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		method.setEntity(entity);
		HttpResponse result = httpClient.execute(method);
		url = URLDecoder.decode(url, "UTF-8");
	}

	//	public static void main(String[] args) throws Exception {
	//		String url = "http://10.12.31.110:9200/test_oll/ool/2/_update";
	//
	//		Map<String, Object> param = new HashMap<>();
	//		param.put("script",
	//				"if(ctx._source.student.books==null){ctx._source.student.books=listValue}else{ctx._source.student.books
	// .add"
	//						+ "(objectValue)}");
	//
	//		Map<String, Object> innerParam = new HashMap<>();
	//		Map<String, Object> realeParam = new HashMap<>();
	//		realeParam.put("name", "http");
	//		realeParam.put("id", 15);
	//		List<Map<String, Object>> list = new ArrayList<>();
	//		list.add(realeParam);
	//
	//		innerParam.put("listValue", GSON.toJson(list));
	//		innerParam.put("objectValue", GSON.toJson(realeParam));
	//
	//		param.put("params", innerParam);
	//
	//		try {
	//			httpPost(url, param);
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//	}

	public static void main(String[] args) {
		String[] cmds = { "curl", "-i", "-w", "状态%{http_code}；DNS时间%{time_namelookup}；"
				+ "等待时间%{time_pretransfer}TCP 连接%{time_connect}；发出请求%{time_starttransfer}；"
				+ "总时间%{time_total}", "http://www.baidu.com" };
		ProcessBuilder pb = new ProcessBuilder(cmds);
		pb.redirectErrorStream(true);
		Process p;
		try {
			p = pb.start();
			BufferedReader br = null;
			String line = null;

			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = br.readLine()) != null) {
				System.out.println("\t" + line);
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
