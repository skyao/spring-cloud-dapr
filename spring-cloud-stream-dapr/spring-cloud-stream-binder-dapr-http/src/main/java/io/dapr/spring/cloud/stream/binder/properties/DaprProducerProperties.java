package io.dapr.spring.cloud.stream.binder.properties;

public class DaprProducerProperties {
    
    private String pubsubName;

    public String getPubsubName() {
		return pubsubName;
	}

	public void setPubsubName(String pubsubName) {
		this.pubsubName = pubsubName;
	}
}
