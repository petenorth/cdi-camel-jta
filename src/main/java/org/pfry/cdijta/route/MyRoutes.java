/**
 *  Copyright 2005-2015 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package org.pfry.cdijta.route;

import javax.inject.Inject;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.Endpoint;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.cdi.Uri;
import org.jboss.narayana.quickstarts.jta.jpa.TestEntityRepository;

import io.fabric8.annotations.Alias;
import io.fabric8.annotations.ServiceName;

/**
 * Configures all our Camel routes, components, endpoints and beans
 */
@ContextName("myCamel")
public class MyRoutes extends RouteBuilder {

    @Inject
    @Uri("timer:foo?period=5000")
    private Endpoint inputEndpoint;
    
    @Inject
    @Uri("timer:bar?period=5000")
    private Endpoint inputEndpointQuery;

    @Inject
    @Uri("log:output")
    private Endpoint resultEndpoint;
    
    @Inject
    @ServiceName("broker-amq-tcp")
    @Alias("jms")
    ActiveMQComponent activeMQComponent;

    @Override
    public void configure() throws Exception {
        
        from(inputEndpoint)
       .transacted("PROPAGATION_REQUIRED")
       .bean(JTAProcessor.class)
       .to("jms:queue:TEST.ENTITY?transacted=true&transactionManager=#transactionManager")
       .to(resultEndpoint);
        
        from(inputEndpointQuery)
       .bean(TestEntityRepository.class, "findAll")
       .bean(CountProcessor.class)
       .log("RESULT: ${body}");
    }

}
