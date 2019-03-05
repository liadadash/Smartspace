package smartspace.layout;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import smartspace.infra.Message;
import smartspace.infra.MessageGenerator;

@Component
public class MessageLayout {
	private MessageGenerator messageGenerator;
	private String name;

	@Autowired
	public void setMessageGenerator(MessageGenerator messageGenerator) {
		this.messageGenerator = messageGenerator;
	}

	@Value("${name.for.message.generator:Anonymous}")
	public void setName(String name) {
		this.name = name;
	}
	
	@PostConstruct
	public void run() {
//		String name = JOptionPane.showInputDialog("type your name");

		Message output = this.messageGenerator.saySomething(name);
//		JOptionPane.showMessageDialog(null, output.getText());
		System.err.println(output.getText());
	}

}
