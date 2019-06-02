package smartspace.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import smartspace.infra.UserRegisterService;

@RestController
public class UserRegisterController {
	private UserRegisterService userService;

	@Autowired
	public UserRegisterController(UserRegisterService userService) {
		this.userService = userService;
	}

	@RequestMapping(path = "/smartspace/users", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary newUser(@RequestBody NewUserForm newUser) {
		return new UserBoundary(this.userService.registerNewUser(newUser.convertToEntity()));
	}
}
