package ca.kirti.microservicecamel.router;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
@Component
public class FirstTimerRouter extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		//create routes
		// listen to a queue/(timer and logs to save the message) , transformation(edit/update) and save to database
		// from where you start the route, , the endpoint name  (e.g from timer endpoint. here 'from' is keyword 
		//and "first-timer" is a nameof endpoint)
		//to is another endpoint (here 'to' is keyword and name of endpoint
		//without transform it gives the null message like (Exchange[ExchangePattern: InOnly, BodyType: null, Body: [Body is null]])
		//between from and to you can do the things like transform the message
		// Router 1 is from timer to log
		from("timer:first-timer") //e.g you can use queue in place of timer in from endpoint
		.transform().constant("Some constant default message")// here it picked up null message and modify it constant message you will get ----  [r://first-timer] first-timer : Exchange[ExchangePattern: InOnly, BodyType: String, Body: Some constant default message]
		.to("log:first-timer"); //e.g. you can save it to database using to
	}

}

