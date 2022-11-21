package com.prabin.healthcheck.controller;

import java.util.Map;
import java.util.TreeMap;

import com.prabin.healthcheck.hc.Container1HC;
import com.prabin.healthcheck.hc.HCpojo;

public class HCController {

	Container1HC container1 = new Container1HC();

	public static void main(String[] args) {

	}

	public static void runHealthCHeck() {
		HCpojo hcp = new HCpojo();
		HCController hcc = new HCController();
		Map<String, Object> env1Status = hcc.getEnv1Status(hcp);
		GenerateReport.generateReport(env1Status, hcp);
	}

	public Map<String, Object> getEnv1Status(HCpojo hcp) {

		Map<String, Object> container1Status = container1.container1Status("LIVE");
		Map<String, Object> container2Status = container1.container1Status("LIVE");

		hcp.setEnvContainer1Value(container1Status.remove("CONTAINER_STATUS").toString());
		hcp.setEnvContainer2Value(container2Status.remove("CONTAINER_STATUS").toString());
		Map<String, Object> status = new TreeMap<>();

		status.putAll(container1Status);
		status.putAll(container2Status);

		return status;

	}

}
