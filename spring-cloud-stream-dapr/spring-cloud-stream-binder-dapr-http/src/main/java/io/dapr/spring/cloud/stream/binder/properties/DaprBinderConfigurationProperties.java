package io.dapr.spring.cloud.stream.binder.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the Dapr binder.
 * 
 * The properties in this class are prefixed with <b>spring.cloud.stream.dapr.binder</b>.
 */
@ConfigurationProperties(prefix = "spring.cloud.stream.dapr.binder")
public class DaprBinderConfigurationProperties {
    /**
	 * Dapr's sidecar address.
	 */
	private String address = "127.0.0.1";

	/**
	 * Dapr's HTTP port.
	 */
	private int httpPort = 3501;

    /**
	 * Dapr's gRPC port.
	 */
	private int grpcPort = 50001;

    public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

    public int getHttpPort() {
		return httpPort;
	}

	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

    public int getGrpcPort() {
		return grpcPort;
	}

	public void setGrpcPort(int grpcPort) {
		this.grpcPort = grpcPort;
	}
}
