package org.esmart.tale.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Endpoint {

	private static Properties endpoints;
	private static Properties endpointsJDBC;

    private static void loadProperties() {
        if (endpoints == null) {
            try {
                Properties properties = new Properties();
                InputStream inputStream = Endpoint.class.getClassLoader().getResourceAsStream("app.properties");
                properties.load(inputStream);
                endpoints = properties;
            } catch (IOException e) {
            	e.printStackTrace();
            }
        }
    }
    private static void loadPropertiesJDBC() {
    	if (endpointsJDBC == null) {
            try {
                Properties properties = new Properties();
                InputStream inputStream = Endpoint.class.getClassLoader().getResourceAsStream("jdbc.properties");
                properties.load(inputStream);
                endpointsJDBC = properties;
            } catch (IOException e) {
            	e.printStackTrace();
            }
    	}
    }

    public static String get(String key) {
        loadProperties();
        return endpoints.getProperty(key);
    }
    public static String getJDBC(String key) {
    	loadPropertiesJDBC();
        return endpointsJDBC.getProperty(key);
    }
}
