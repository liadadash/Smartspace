package smartspace.dao.rdb;

import org.springframework.data.repository.PagingAndSortingRepository;

import smartspace.data.UserEntity;
import smartspace.data.UserKey;

public interface UserCrud extends 
//CrudRepository<UserEntity,UserKey> {
		PagingAndSortingRepository<UserEntity, UserKey> {
	
}
