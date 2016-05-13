package org.pfry.cdijta.mq;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.transaction.TransactionManager;

import org.apache.activemq.ActiveMQXAConnectionFactory;
import org.apache.activemq.pool.JcaPooledConnectionFactory;


public class JcaPooledConnectionFactoryProducer {
	
	@Inject
	@ActiveMQXA
	ActiveMQXAConnectionFactory jmsXaConnectionFactory;
	
	@Inject
	TransactionManager transactionManager;
	
	@Produces
	@Jca
	public JcaPooledConnectionFactory createJcaPooledConnectionFactory(){
		JcaPooledConnectionFactory jcaPooledConnectionFactory = new JcaPooledConnectionFactory();
		jcaPooledConnectionFactory.setName("MyXaResourceName");
		jcaPooledConnectionFactory.setMaxConnections(1);
		jcaPooledConnectionFactory.setConnectionFactory(jmsXaConnectionFactory);
		jcaPooledConnectionFactory.setTransactionManager(transactionManager);
		return jcaPooledConnectionFactory;
	}

}
