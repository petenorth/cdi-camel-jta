package org.pfry.cdijta.mq;

import javax.enterprise.inject.Produces;

import org.apache.activemq.ActiveMQXAConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;

import io.fabric8.annotations.ServiceName;

public class ActiveMQXAConnectionFactoryProducer {
	
	@Produces
	@ActiveMQXA  //@ServiceName( String url)
	public ActiveMQXAConnectionFactory createActiveMQXAConnectionFactory(){
		RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
		redeliveryPolicy.setMaximumRedeliveries(0);
		
		ActiveMQXAConnectionFactory activeMQXAConnectionFactory = new ActiveMQXAConnectionFactory();
		activeMQXAConnectionFactory.setBrokerURL("tcp://172.30.194.202:61616");
		activeMQXAConnectionFactory.setUserName("admin");
		activeMQXAConnectionFactory.setPassword("admin");
		activeMQXAConnectionFactory.setRedeliveryPolicy(redeliveryPolicy);
		return activeMQXAConnectionFactory;
	}

}
