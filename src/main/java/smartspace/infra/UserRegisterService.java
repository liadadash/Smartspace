package smartspace.infra;

import smartspace.data.UserEntity;

public interface UserRegisterService {
	
	// need to check if user exists and has ADMIN role when you get a request.
	public UserEntity registerNewUser(UserEntity newUser);
	
}


