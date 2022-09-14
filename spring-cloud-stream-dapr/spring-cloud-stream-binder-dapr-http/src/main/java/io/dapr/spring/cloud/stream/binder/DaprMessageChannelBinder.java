package io.dapr.spring.cloud.stream.binder;

import org.springframework.cloud.stream.binder.AbstractMessageChannelBinder;
import org.springframework.cloud.stream.binder.BinderSpecificPropertiesProvider;
import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.binder.ExtendedPropertiesBinder;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.integration.core.MessageProducer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import io.dapr.client.DaprClient;
import io.dapr.spring.cloud.stream.binder.properties.DaprConsumerProperties;
import io.dapr.spring.cloud.stream.binder.properties.DaprExtendedBindingProperties;
import io.dapr.spring.cloud.stream.binder.properties.DaprProducerProperties;
import io.dapr.spring.cloud.stream.binder.provisioning.DaprBinderProvisioner;

public class DaprMessageChannelBinder extends
        AbstractMessageChannelBinder<
        ExtendedConsumerProperties<DaprConsumerProperties>,
        ExtendedProducerProperties<DaprProducerProperties>,
        DaprBinderProvisioner>
    implements
    ExtendedPropertiesBinder<MessageChannel, DaprConsumerProperties, DaprProducerProperties> {

    private final DaprExtendedBindingProperties bindingProperties;
    private final DaprClient daprClient;

    public DaprMessageChannelBinder(String[] headersToEmbed, 
            DaprBinderProvisioner provisioningProvider,
            DaprExtendedBindingProperties bindingProperties, 
            DaprClient daprClient) {
        super(headersToEmbed, provisioningProvider);
        this.bindingProperties = bindingProperties;
        this.daprClient = daprClient;
    }

    @Override
    public DaprConsumerProperties getExtendedConsumerProperties(String channelName) {
        return this.bindingProperties.getExtendedConsumerProperties(channelName);
    }

    @Override
    public DaprProducerProperties getExtendedProducerProperties(String channelName) {
        return this.bindingProperties.getExtendedProducerProperties(channelName);
    }

    @Override
    public String getDefaultsPrefix() {
        return this.bindingProperties.getDefaultsPrefix();
    }

    @Override
    public Class<? extends BinderSpecificPropertiesProvider> getExtendedPropertiesEntryClass() {
        return this.bindingProperties.getExtendedPropertiesEntryClass();
    }

    @Override
    protected MessageHandler createProducerMessageHandler(ProducerDestination destination,
            ExtendedProducerProperties<DaprProducerProperties> producerProperties, 
            MessageChannel errorChannel)
            throws Exception {
        DaprProducerProperties extension = producerProperties.getExtension();
        DaprMessageHandler daprMessageHandler = new DaprMessageHandler(
            destination.getName(), extension.getPubsubName(), daprClient);
        daprMessageHandler.setBeanFactory(getBeanFactory());
        return daprMessageHandler;
    }

    @Override
    protected MessageProducer createConsumerEndpoint(ConsumerDestination destination, String group,
            ExtendedConsumerProperties<DaprConsumerProperties> properties) throws Exception {
        return new DaprMessageProducer(properties.getExtension().getPubsubName(),
				destination.getName());
    }
    
}
