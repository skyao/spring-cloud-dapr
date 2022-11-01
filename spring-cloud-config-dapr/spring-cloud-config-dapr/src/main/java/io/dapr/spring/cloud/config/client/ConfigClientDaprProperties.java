package io.dapr.spring.cloud.config.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

@ConfigurationProperties(ConfigClientDaprProperties.PREFIX)
public class ConfigClientDaprProperties {
    /**
     * Prefix for Spring Cloud Config properties.
     */
    public static final String PREFIX = "spring.cloud.config.dapr";

    private String profile = DEFAULT_PROFILE;

    /**
     * Default profile value.
     */
    public static final String DEFAULT_PROFILE = "default";

    /**
     * timeout on waiting to read data from the Config Server.
     */
    private int requestReadTimeout = (60 * 1000 * 3) + 5000;

    /**
     * timeout on waiting to connect to the Config Server.
     */
    private int requestConnectTimeout = 1000 * 10;

    /**
     * Additional headers used to create the client request.
     */
    private Map<String, String> headers = new HashMap<>();

    private String storeName;

    @Value("${spring.cloud.config.dapr.keys}")
    private List<String> keys;

    public ConfigClientDaprProperties() {
    }

    public ConfigClientDaprProperties(Environment environment) {
        String[] profiles = environment.getActiveProfiles();
        if (profiles.length == 0) {
            profiles = environment.getDefaultProfiles();
        }
        this.setProfile(StringUtils.arrayToCommaDelimitedString(profiles));
    }

    public String getProfile() {
        return this.profile;
    }

    public void setProfile(String env) {
        this.profile = env;
    }

    public int getRequestReadTimeout() {
        return this.requestReadTimeout;
    }

    public void setRequestReadTimeout(int requestReadTimeout) {
        this.requestReadTimeout = requestReadTimeout;
    }

    public int getRequestConnectTimeout() {
        return this.requestConnectTimeout;
    }

    public void setRequestConnectTimeout(int requestConnectTimeout) {
        this.requestConnectTimeout = requestConnectTimeout;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public List<String> getKeys() {
        return this.keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public String getStoreName() {
        return this.storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }


    	/**
	 * Name of application used to fetch remote properties.
	 */
	@Value("${spring.application.name:application}")
	private String name;
    
	public String getName() {
		return this.name;
	}

}
