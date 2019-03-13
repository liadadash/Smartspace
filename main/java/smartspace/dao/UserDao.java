package smartspace.dao;

import java.util.List;
import java.util.Optional;

import smartspace.data.UserEntity;

//Amit 13\03
public interface UserDao<UserKey> {
	public UserEntity create(UserEntity userEntity);

	public List<UserEntity> readAll(UserEntity userEntity);

	public Optional<UserEntity> readbyId(UserKey userKey);

	public void update(UserEntity userEntity);

	public void deleteAll();
}
//
