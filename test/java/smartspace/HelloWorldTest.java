package smartspace;

import smartspace.infra.HelloMessageGenerator;
import smartspace.infra.Message;
import smartspace.infra.MessageGenerator;

public class HelloWorldTest {
	public static void main(String[] args) throws Exception{
		// Given nothing
				
		
		// When the user provides the input "World"
		String name = "World";
		MessageGenerator messageGenerator = new HelloMessageGenerator();
		Message output = messageGenerator.saySomething(name);
		
		// Then the application responds with "Hello World!"
		String expected = "Hello World!";
		if(!output.getText().equals(expected)) {
			throw new Exception("Error while generating the message. expected: " + expected + ", while actual: " + output.getText());
		}
	}
}
