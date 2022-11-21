package com.prabin.healthcheck.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

import com.prabin.healthcheck.hc.HCpojo;

public class GenerateReport {

	public static void main(String[] args) {

		Map<String, Object> status = new TreeMap<>();
		status.put("LIVEAPI1", false);
		status.put("LIVEAPI2", true);
		status.put("LIVEAPI3", false);
		status.put("LIVEAPI4", true);

		HCpojo hcp = new HCpojo();
		hcp.setEnvContainer1Value("RED");
		hcp.setEnvContainer2Value("GREEN");
		generateReport(status, hcp);
	}

	public static void generateReport(Map<String, Object> status, HCpojo hcp) {

		try {
			String template = FileUtils.readFileToString(new File("src/main/resources/HCTemplate.html"),
					StandardCharsets.UTF_8);
			template = String.format(template, hcp.getEnvContainer1Value(), hcp.getEnvContainer1Value(),
					hcp.getEnvContainer2Value(), hcp.getEnvContainer2Value());

			StringBuilder sb = new StringBuilder();
			sb.append("<table>");
			for (String key : status.keySet()) {
				String apiName = key.substring(4);
				if (key.subSequence(0, 4).equals("LIVE")) {
					String serviceStatusLive = (status.get("LIVE" + apiName) == (Object) true) ? "GREEN" : "RED";
					// add more env if required

					sb.append(String.format("<tr><td>%s</td>", apiName));
					sb.append(String.format("<td bgcolor=\"%s\"/>", serviceStatusLive));
					// add mode env if required

					sb.append("</tr>");
				}

			}
			sb.append("</table>");
			template = template.replace("##ServiceStatus##", sb.toString());

			FileUtils.writeStringToFile(new File("HealthCheckReport.html"), template, StandardCharsets.UTF_8);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
