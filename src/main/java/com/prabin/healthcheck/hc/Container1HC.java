package com.prabin.healthcheck.hc;

import java.util.Map;
import java.util.TreeMap;

public class Container1HC extends BaseHealthCheck {

	public static void main(String[] args) {

	}

	public Map<String, Object> container1Status(String env) {
		Map<String, Object> container1statusMap = new TreeMap<>();

		boolean testapi1 = testAPI1(env);
		boolean testapi2 = testAPI2(env);
		container1statusMap.put(env + "API1NAME", testapi1);
		container1statusMap.put(env + "API2NAME", testapi2);
		container1statusMap.put("CONTAINER_STATUS", checkContainerStatus(testapi1,testapi2));

		return container1statusMap;

	}

	private Object checkContainerStatus(boolean testapi1, boolean testapi2) {
		if (testapi1 && testapi2)
			return "GREEN";
		else
			return "RED";
	}

	public boolean testAPI1(String env) {
		return checkHttpPost("api2Url", env, "api1", "APIRequest").contains("Keyword or success sentence from API");
	}

	public boolean testAPI2(String env) {
		return checkHttpGet("api2Url", env, "api2").contains("Keyword or success sentence from API");
	}
}
