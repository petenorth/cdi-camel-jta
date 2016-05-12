package org.pfry.cdijta.mq;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.transaction.TransactionManager;

import org.apache.activemq.pool.ActiveMQResourceManager;

public class ResourceManagerProducer {
	
	@Inject
	TransactionManager transactionManager;
	
	@Inject
	@Jca
	ConnectionFactory jmsXaPoolConnectionFactory;
	
	@Produces
	public ActiveMQResourceManager createActiveMQResourceManager()
	{
		ActiveMQResourceManager activeMQResourceManager = new ActiveMQResourceManager();
		activeMQResourceManager.setTransactionManager(transactionManager);
		activeMQResourceManager.setConnectionFactory(jmsXaPoolConnectionFactory);
		activeMQResourceManager.setResourceName("activemq.default");
		activeMQResourceManager.recoverResource();
		return activeMQResourceManager;
	}

}
