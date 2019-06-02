package smartspace.infra;

import smartspace.data.UserEntity;

public interface GetSpecificUserService {
	public UserEntity getUser(String userSmartspace,String userEmail);
}

