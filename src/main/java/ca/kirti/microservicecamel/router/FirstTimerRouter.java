package ca.kirti.microservicecamel.router;

import java.time.LocalDateTime;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class FirstTimerRouter extends RouteBuilder{

	@Autowired
	private GetCurrentTimeBean getCurrentTimeBean;
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
		//.transform().constant("Some constant default message")// here it picked up null message and modify it constant message you will get ----  [r://first-timer] first-timer : Exchange[ExchangePattern: InOnly, BodyType: String, Body: Some constant default message]
		//.transform().constant("Time now is:"+LocalDateTime.now())  // this will print the same time in each 
		//.bean(getCurrentTimeBean) //this works when you have one method in your bean (e.g getCurrentTimeBean)
		.bean(getCurrentTimeBean,"getCurrentTime") //use method name when you have more then 1 method in your bean
		.to("log:first-timer"); //e.g. you can save it to database using to
	}

}

@Component
class GetCurrentTimeBean {
	public String getCurrentTime() {
		return "Time now is:"+LocalDateTime.now();
	}
}