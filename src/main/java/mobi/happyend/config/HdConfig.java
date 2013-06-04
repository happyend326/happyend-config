package mobi.happyend.config;

import java.io.IOException;
import java.io.InputStream;
import java.security.AccessControlException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

public class HdConfig {
	public static String DEFAULT_PROPERTY_FILENAME = "appConfig.properties";
	private Properties defaultProperty;
	private static HdConfig config;
	
	public static HdConfig getInstance(){
		if(config == null){
			config = new HdConfig();
			config.loadProperties(HdConfig.class.getResourceAsStream(DEFAULT_PROPERTY_FILENAME));
		}
		return config;
	}
	
	private HdConfig(){
		defaultProperty = new Properties();
	}
	
	public HdConfig loadProperties(InputStream is){
		try {
			config.defaultProperty.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return config;
	}
	
	public HdConfig loadProperties(Properties props){
		Iterator<Entry<Object, Object>> it = props.entrySet().iterator();  
        while (it.hasNext()) {  
            Entry<Object, Object> entry = it.next();  
            String key = (String)entry.getKey();  
            String value = (String)entry.getValue();  
            config.defaultProperty.setProperty(key, value);
        } 
        return config;
	}
	
	
	/*
	 * 常用配置方法
	 */
	
	public String getLoggerTag(){
		return config.getProperty("logger.tag");
	}
	
	public boolean getDebuggable(){
		return config.getBoolean("logger.debug");
	}
	
	public String getDefaultServerHost() {
		return  config.getProperty("http.serverHost");
	}

	public String getDefaultCookieDomain() {
		return  config.getProperty("http.cookieDomain");
	}
	
	public long getDefaultCookieExpires() {
		return config.getLongProperty("http.cookieExpires");
	}

	public String getDefaultUserAgent() {
		return  config.getProperty("http.userAgent");
	}

	public String getDefaultEncode() {
		return  config.getProperty("http.encode");
	}

	public String getDefaultReferUrl() {
		return  config.getProperty("http.referURL");
	}

	public int getConnectionTimeoutMs() {
		return config.getIntProperty("http.connectTimeoutMs", 30000);
	}

	public int getSocketTimeoutMs() {
		return config.getIntProperty("http.socketTimeoutMs", 30000);
	}

	public int getTimeoutRetryTimes() {
		return config.getIntProperty("http.timeoutRetryTimes",3);
	}
	
	/*
	 * 常用方法
	 */
	public boolean getBoolean(String name) {
		String value = getProperty(name);
		return Boolean.valueOf(value);
	}

	public int getIntProperty(String name) {
		String value = getProperty(name);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			return -1;
		}
	}

	public int getIntProperty(String name, int fallbackValue) {
		String value = getProperty(name, String.valueOf(fallbackValue));
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			return -1;
		}
	}

	public long getLongProperty(String name) {
		String value = getProperty(name);
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException nfe) {
			return -1;
		}
	}

	public String getProperty(String name) {
		return getProperty(name, null);
	}

	public String getProperty(String name, String fallbackValue) {
		String value;
		try {
			value = System.getProperty(name, fallbackValue);
			if (null == value) {
				value = config.defaultProperty.getProperty(name);
			}
			if (null == value) {
				String fallback = config.defaultProperty.getProperty(name
						+ ".fallback");
				if (null != fallback) {
					value = System.getProperty(fallback);
				}
			}
		} catch (AccessControlException ace) {
			value = fallbackValue;
		}
		return value;
	}

}
