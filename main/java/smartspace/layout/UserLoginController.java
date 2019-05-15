package smartspace.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import smartspace.infra.GetSpecificUserService;

@RestController
public class UserLoginController {
	private GetSpecificUserService userService;

	@Autowired
	public UserLoginController(GetSpecificUserService userService) {
		this.userService = userService;
	}

	@RequestMapping(path = "/smartspace/users/login/{userSmartspace}/{userEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary getUserDetails(@PathVariable("userSmartspace") String userSmartspace, @PathVariable("userEmail") String userEmail) {
		return new UserBoundary(this.userService.getUser(userSmartspace, userEmail));
	}
}
