package com.prabin.healthcheck.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFileReader {
	
	private static Properties propertyReader(String env) {
		
		Properties props = new Properties();
		
		InputStream p = null;
		
		try {
		p = PropertyFileReader.class.getClassLoader().getResourceAsStream(env+"/env.properties");
		props.load(p);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return props;
		
	}
	
	public static String getProps(String key,String env) {
		return PropertyFileReader.propertyReader(env).getProperty(key);
	}

}
