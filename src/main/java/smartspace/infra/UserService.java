package smartspace.infra;
import java.util.List;

import smartspace.data.UserEntity;

public interface UserService {
	
	// need to check if user exists and has ADMIN role when you get a request.
	public List<UserEntity> importUsers(String adminSmartspace, String adminEmail, List<UserEntity> entities);
	
	// need to check if user exists and has ADMIN role when you get a request.
	public List<UserEntity> getUsingPagination (String adminSmartspace, String adminEmail, int size, int page);

}


