/*******************************************************************************
 * Copyright 2013 happyend
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package mobi.happyend.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.security.AccessControlException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * HdConfig can load configuration from ".properties" file.
 * Once you use it, you only need to load your properties once.
 * <pre>
 * {@code
 * InputSteam is = YourClass.class.getResourceAsStream(YOUR_PROPERTIES_FILENAME);
 * HdConfig.getInstance().loadProperties(is);
 * }
 * </pre>
 * OR
 * <pre>
 * {@code
 * Properties props = new Properties();
 * props.setProperty("app.version", "1.0");
 * HdConfig.getInstance().loadProperties(props);
 * }
 * </pre>
 * The latter loaded properties will overwrite the former one.
 * After load properties, you can use it as a normal singleton Class
 * <pre>
 * {@code
 * HdConfig.getInstance().getIntProperty("app.version");
 * }
 * </pre>
 */
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
