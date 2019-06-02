package smartspace.layout;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import smartspace.data.UserEntity;
import smartspace.infra.UserService;

@RestController
public class UserController {
	private UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(
			path="/smartspace/admin/users/{adminSmartspace}/{adminEmail}",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	
	public UserBoundary[] newUser (@RequestBody UserBoundary[] boundaryUsers, @PathVariable("adminSmartspace") String adminSmartspace, @PathVariable("adminEmail") String adminEmail) {
		
		// convert UserBoundary Array to UserEntity List.
		List<UserEntity> entities = Stream.of(boundaryUsers).map(UserBoundary::convertToEntity).collect(Collectors.toList());
		
		// import the Users, converts response back to Boundary Array.
		return this.userService.importUsers(adminSmartspace, adminEmail, entities).stream()
				.map(UserBoundary::new).collect(Collectors.toList()).toArray(new UserBoundary[0]);
	}

	@RequestMapping(
			path="/smartspace/admin/users/{adminSmartspace}/{adminEmail}",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	
	public UserBoundary[] getUsingPagination (
			@PathVariable("adminSmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail,
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@RequestParam(name="page", required=false, defaultValue="0") int page) {
		
		// convert the Entity List to Boundary Array
		return this.userService.getUsingPagination(adminSmartspace, adminEmail, size, page).stream()
				.map(UserBoundary::new).collect(Collectors.toList()).toArray(new UserBoundary[0]);
	}
}
