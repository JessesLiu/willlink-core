package com.willlink.wallet.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class HttpUtils {

	private static int CONNECT_TIME_OUT = 10000;

	private static int CONNECTION_REQUEST_TIME_OUT = 10000;

	private static int SOCKET_TIME_OUT = 10000;

	/**
	 * get
	 *
	 * @param host
	 * @param path
	 * @param method
	 * @param headers
	 * @param querys
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse doGet(String host, String path, String method,
                                     Map<String, String> headers,
                                     Map<String, String> querys)
			throws Exception {
		HttpClient httpClient = wrapClient(host);

		HttpGet request = new HttpGet(buildUrl(host, path, querys));

		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(CONNECT_TIME_OUT)
				.setConnectionRequestTimeout(CONNECTION_REQUEST_TIME_OUT)
				.setSocketTimeout(SOCKET_TIME_OUT).build();
		request.setConfig(requestConfig);

		for (Map.Entry<String, String> e : headers.entrySet()) {
			request.addHeader(e.getKey(), e.getValue());
		}

		return httpClient.execute(request);
	}


	/**
	 * Post String
	 *
	 * @param host
	 * @param path
	 * @param method
	 * @param headers
	 * @param querys
	 * @param body
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse doPost(String host, String path, String method,
                                      Map<String, String> headers,
                                      Map<String, String> querys,
                                      String body)
			throws Exception {
		HttpClient httpClient = wrapClient(host);

		HttpPost request = new HttpPost(buildUrl(host, path, querys));
		for (Map.Entry<String, String> e : headers.entrySet()) {
			request.addHeader(e.getKey(), e.getValue());
		}

		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(CONNECT_TIME_OUT)
				.setConnectionRequestTimeout(CONNECTION_REQUEST_TIME_OUT)
				.setSocketTimeout(SOCKET_TIME_OUT).build();
		request.setConfig(requestConfig);

		if (StringUtils.isNotBlank(body)) {
			request.setEntity(new StringEntity(body, "utf-8"));
		}

		return httpClient.execute(request);
	}



	private static String buildUrl(String host, String path, Map<String, String> querys) throws UnsupportedEncodingException {
		StringBuilder sbUrl = new StringBuilder();
		sbUrl.append(host);
		if (!StringUtils.isBlank(path)) {
			sbUrl.append(path);
		}
		if (null != querys) {
			StringBuilder sbQuery = new StringBuilder();
			for (Map.Entry<String, String> query : querys.entrySet()) {
				if (0 < sbQuery.length()) {
					sbQuery.append("&");
				}
				if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
					sbQuery.append(query.getValue());
				}
				if (!StringUtils.isBlank(query.getKey())) {
					sbQuery.append(query.getKey());
					if (!StringUtils.isBlank(query.getValue())) {
						sbQuery.append("=");
						sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
					}
				}
			}
			if (0 < sbQuery.length()) {
				sbUrl.append("?").append(sbQuery);
			}
		}

		return sbUrl.toString();
	}

	private static HttpClient wrapClient(String host) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		return httpClient;
	}
}
