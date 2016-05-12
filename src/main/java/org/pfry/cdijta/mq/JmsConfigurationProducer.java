package org.pfry.cdijta.mq;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;

import org.apache.camel.component.jms.JmsConfiguration;
import org.springframework.transaction.PlatformTransactionManager;

public class JmsConfigurationProducer {
	
	@Inject
	@Jca
	ConnectionFactory jmsXaPoolConnectionFactory;
	
	@Inject
	PlatformTransactionManager platformTransactionManager;
	
	@Produces
	public JmsConfiguration createJmsConfiguration(){
		JmsConfiguration jmsConfiguration = new JmsConfiguration();
		jmsConfiguration.setConnectionFactory(jmsXaPoolConnectionFactory);
		jmsConfiguration.setTransactionManager(platformTransactionManager);
		jmsConfiguration.setTransacted(false);
		jmsConfiguration.setCacheLevelName("CACHE_CONNECTION");
		return jmsConfiguration;
	}

}
