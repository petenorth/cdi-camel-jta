/*
 * Copyright 2005-2015 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.pfry.cdijta.route;

import io.fabric8.annotations.Factory;
import io.fabric8.annotations.ServiceName;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.component.jms.JmsConfiguration;

public class ActiveMQComponentFactory {

//    @Factory
//    @ServiceName
//    @Named("jms")
//    public ActiveMQComponent create(@ServiceName ActiveMQConnectionFactory factory) {
//        ActiveMQComponent component = new ActiveMQComponent();
//        component.setConnectionFactory(factory);
//        return component;
//    }

    /*
    @Factory
    @ServiceName
    @Named("jms")
    public ActiveMQComponent create(@ServiceName String url, @Configuration ActiveMQConfig config) {
        ActiveMQComponent component = new ActiveMQComponent();
        component.setBrokerURL(url);
        component.setConnectionFactory(new ActiveMQConnectionFactory(url));
        return component;
    }*/
	
	@Inject
	JmsConfiguration configuration;

	@Produces
	@Named("jmstx")
	@ApplicationScoped
	public ActiveMQComponent createActiveMQComponent()
	{
		ActiveMQComponent activeMQComponent = new ActiveMQComponent();
		activeMQComponent.setConfiguration(configuration);
		return activeMQComponent;
	}
	
}
