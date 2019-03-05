package smartspace.infra;

import org.springframework.stereotype.Component;

@Component
public class HelloMessageGenerator implements MessageGenerator {

	@Override
	public Message saySomething(String name) {
		Message rv = new Message();
		rv.setName(name);
		return rv;
	}

}
