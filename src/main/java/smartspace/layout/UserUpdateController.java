package smartspace.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import smartspace.infra.UpdateUserService;

@RestController
public class UserUpdateController {
	private UpdateUserService userService;

	@Autowired
	public UserUpdateController(UpdateUserService userService) {
		this.userService = userService;
	}

	@RequestMapping(path = "/smartspace/users/login/{userSmartspace}/{userEmail}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void newUser(@PathVariable("userSmartspace") String userSmartspace, @PathVariable("userEmail") String userEmail, @RequestBody UserBoundary userDetails) {
		this.userService.updateUser(userEmail, userSmartspace, userDetails.convertToEntity());
	}
}
