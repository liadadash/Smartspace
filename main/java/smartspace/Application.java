package smartspace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.println();
		
//		MessageLayout messageLayout = new MessageLayout ();
//		MessageGenerator messageGenerator = new HelloMessageGenerator();
//		messageLayout.setMessageGenerator (messageGenerator);
//		messageLayout.setName("World");
		
//		messageLayout.run();
	}
}