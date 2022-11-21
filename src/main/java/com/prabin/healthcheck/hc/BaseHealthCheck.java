package com.prabin.healthcheck.hc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.prabin.healthcheck.util.PropertyFileReader;

public class BaseHealthCheck {

	HttpClientBuilder httpClientBuilder;

	static final String EXCEPTION_STRING = "Exception Occured";

	public BaseHealthCheck() {
		int timeout = 10;
		RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout * 1000)
				.setConnectionRequestTimeout(timeout * 1000).setSocketTimeout(timeout * 1000).build();
		httpClientBuilder = HttpClients.custom().disableCookieManagement()
				.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).setDefaultRequestConfig(config);

	}

	public String checkHttpPost(String url, String env, String serviceName, String req) {
		HttpPost httpPost = null;
		CloseableHttpResponse response = null;
		CloseableHttpClient httpClient = httpClientBuilder.build();
		try {
			httpPost = new HttpPost(url);
			httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
			httpPost.addHeader(HttpHeaders.AUTHORIZATION, PropertyFileReader.getProps(serviceName, env));
			httpPost.setEntity(new StringEntity(req));
			response = httpClient.execute(httpPost);
			String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				return responseString;
			} else {
				return EXCEPTION_STRING;
			}
		} catch (Exception e) {
			return EXCEPTION_STRING;
		} finally {
			closehttpResource(httpPost, response, httpClient);
		}

	}

	public String checkHttpGet(String url, String env, String serviceName) {
		HttpGet httpGet = null;
		CloseableHttpResponse response = null;
		CloseableHttpClient httpClient = httpClientBuilder.build();
		try {
			httpGet = new HttpGet(url);
			httpGet.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
			httpGet.addHeader(HttpHeaders.AUTHORIZATION, PropertyFileReader.getProps(serviceName, env));
			response = httpClient.execute(httpGet);
			String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				return responseString;
			} else {
				return EXCEPTION_STRING;
			}
		} catch (Exception e) {
			return EXCEPTION_STRING;
		} finally {
			closehttpResource(httpGet, response, httpClient);
		}

	}

	private void closehttpResource(HttpRequestBase httpMethod, CloseableHttpResponse response,
			CloseableHttpClient httpClient) {

		try {
			if (null != httpClient)
				httpClient.close();
			if (null != response)
				response.close();
			if (null != httpMethod)
				httpMethod.releaseConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
