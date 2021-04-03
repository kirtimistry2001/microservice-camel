package ca.kirti.microservicecamel.router.c;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ActiveMQSenderRouter extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		// create timer end point and another is queue endpoint
		// timer endpoint
		// here every 10000 milisecond generating message and putting on the queue
		from("timer:active-mq-timer?period=10000") // timer for 10000 miliseconds
		.transform().constant("Test Messase for Active MQ")
		//queue end point
		.to("activemq:my-activemq-queue"); //'my-activemq-queue' is the queue name
	}

}
