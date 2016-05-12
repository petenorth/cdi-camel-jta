package org.pfry.cdijta.route;

import javax.inject.Inject;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.Endpoint;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.cdi.Uri;

import io.fabric8.annotations.Alias;
import io.fabric8.annotations.ServiceName;

/**
 * Configures all our Camel routes, components, endpoints and beans
 */
@ContextName("myCamel")
public class TomsRoutes extends RouteBuilder {

    @Inject
    @Uri("timer://foo?repeatCount=25&delay=1000&period=100")
    private Endpoint timerEndpoint;
    
//    @Inject
//    @Uri("jmstx:queue:TEST.OUT")
//    private Endpoint jmstxEndpoint;

    @Override
    public void configure() throws Exception {

        // Fire messages into the source queue
        from(timerEndpoint)
                .id("timer-route")
                .transform(simple("Hello, world!"))
                .transacted("PROPAGATION_REQUIRED")
                .log("Received message - ${body}")
                .to("jmstx:queue:TEST.OUT")
                .to("processorBean")
                .log("Published message - ${body}");

        // Consume messages from the queue in a transacted route
//        from("jms:queue:TEST.IN")
//                .id("cdi-camel-xa-tx-route")
//                .transacted("PROPAGATION_REQUIRED")
//                .log("Received message - ${body}")
//                .to("jmstx:queue:TEST.OUT")
//                .to("processorBean")
//                .log("Published message - ${body}");

    }

}

