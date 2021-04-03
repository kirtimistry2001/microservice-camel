package ca.kirti.microservicecamel.router;

import java.time.LocalDateTime;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component   //commented to disable route
public class FirstTimerRouter extends RouteBuilder{

	@Autowired
	private GetCurrentTimeBean getCurrentTimeBean;
	
	@Autowired
	private LoggingProcessComponent loggingProcessComponent;
	
	@Override
	public void configure() throws Exception {
		//create routes (https://www.youtube.com/watch?v=eh9C0GyxtHE&ab_channel=in28minutesCloud%2CDevOpsandMicroservices)
		// listen to a queue/(timer and logs to save the message) , transformation(edit/update) and save to database
		// from where you start the route, , the endpoint name  (e.g from timer endpoint. here 'from' is keyword 
		//and "first-timer" is a nameof endpoint)
		//to is another endpoint (here 'to' is keyword and name of endpoint
		//without transform it gives the null message like (Exchange[ExchangePattern: InOnly, BodyType: null, Body: [Body is null]])
		//between from and to you can do the things like transform the message
		// Router 1 is from timer to log
		from("timer:first-timer") //e.g you can use queue in place of timer in from endpoint
		.log ("${body}") // print null
		.transform().constant("Some constant default message")// here it picked up null message and modify it constant message you will get ----  [r://first-timer] first-timer : Exchange[ExchangePattern: InOnly, BodyType: String, Body: Some constant default message]
		.log ("${body}") //print constant message
		//.transform().constant("Time now is:"+LocalDateTime.now())  // this will print the same time in each 
		//.bean(getCurrentTimeBean) //this works when you have one method in your bean (e.g getCurrentTimeBean)
		.bean(getCurrentTimeBean,"getCurrentTime") //use method name when you have more then 1 method in your bean
		.log ("${body}") // print some dynamic message
		.bean(loggingProcessComponent)
		.log ("${body}")  // no change in body
		/* 
		 * Two Type of operation in Routes
		 * 	1) Processing 2) Transformation
		 * 1) Processing : Operation that doesn't make a change the body of message
		 * .bean(beanName) can be used to processing
		 * 2) Transformation: Operation that change the body of message 
		 * (e.g Transformation can be done using transform, bean or Processor.
		 *   transform().constant(some modified message),.bean(getCurrentTimeBean) and  .bean(getCurrentTimeBean are the example of transform as we change the body of message)
		 * .transform() or .bean(beanName) can be used to do transformation operation
		 * 
		 * */
		 .process(new SimpleLoggingProcess())
		.to("log:first-timer"); //e.g. you can save it to database using to
	}

}

/**
 * This is used to Transformation 
 * 	i.e changing bosy og message
 * @author Kirti
 *
 */
@Component
class GetCurrentTimeBean {
	public String getCurrentTime() {
		return "Time now is:"+LocalDateTime.now();
	}
}

/**
 * This is used to do processing
 * 	i.e message body will not change
 *  type of process you can do here is like doing some logic/saving to databse)
 * @author Kirti
 *
 */
@Component
class LoggingProcessComponent{
	private Logger logger = LoggerFactory.getLogger(LoggingProcessComponent.class);
	/** this is void method that means we not modify message body
	 * simple printing log, but in a real time you do some logic 
	 * that would not change the message body like saving it to database
	 * */
	public void process(String message) {
			logger.info("Simple logging message using Component", message);
	}
}

/**
 * Processor Bean
 * @author Kirti
 *
 */
class SimpleLoggingProcess implements Processor {

	private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcess.class);
	@Override
	public void process(Exchange exchange) throws Exception {
		logger.info("Simple logging message using Processor",exchange.getMessage().getBody());

	}
}