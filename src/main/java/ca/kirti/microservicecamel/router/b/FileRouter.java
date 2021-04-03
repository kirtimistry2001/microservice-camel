package ca.kirti.microservicecamel.router.b;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FileRouter extends RouteBuilder{

	
	@Override
	public void configure() throws Exception {
		// on frist execution it will create folder 'files/input'
		// add some files (e.g txt, csv , json files")
		//after copy-pasting/ some files in 'input' folder rerun the application
		// it will create 'files/output' folder copy all the files from input folder (1st end point) to the output folder (2nd end point)
		from("file:files/input")  //first endpoint, It will create folder called 'files/input'
		.log("${body}")	//processing , just printing body
		.to("file:files/output"); //second enpoint
	}

}
