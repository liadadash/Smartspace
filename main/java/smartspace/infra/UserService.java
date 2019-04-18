package smartspace.infra;
import java.util.List;

import smartspace.data.UserEntity;

public interface UserService {
	
	// need to check if user exists and has ADMIN role when you get a request.
	public List<UserEntity> importUsers(List<UserEntity> entities, String adminSmartspace, String adminEmail);
	
	// need to check if user exists and has ADMIN role when you get a request.
	public List<UserEntity> getUsingPagination (int size, int page, String adminSmartspace, String adminEmail);

}


