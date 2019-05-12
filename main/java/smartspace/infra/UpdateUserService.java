package smartspace.infra;

import smartspace.data.UserEntity;

public interface UpdateUserService {
	public void updateUser(String userEmail, String userSmartspace, UserEntity userEntity);
}
