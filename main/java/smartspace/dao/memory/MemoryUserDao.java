package smartspace.dao.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import smartspace.dao.UserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;

//Amit 13/03
//@Repository
public class MemoryUserDao implements UserDao<UserKey> {
	private List<UserEntity> users;


	public MemoryUserDao() {
		this.users = Collections.synchronizedList(new ArrayList<>());
	}

	@Override
	public UserEntity create(UserEntity userEntity) {
		userEntity.setKey(new UserKey(userEntity.getUserSmartspace(),
				userEntity.getUserEmail()));
		this.users.add(userEntity);
		return userEntity;
	}

	@Override
	public List<UserEntity> readAll() {
		return this.users;
	}

	@Override
	public Optional<UserEntity> readById(UserKey userKey) {
		UserEntity target = null;
		for (UserEntity current : this.users) {
			if (current.getKey().equals(userKey)) {
				target = current;
			}
		}
		if (target != null) {
			return Optional.of(target);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public void update(UserEntity update) {
		synchronized (this.users) {
			UserEntity existing = this.readById(update.getKey())
					.orElseThrow(() -> new RuntimeException("no user to update"));
			if (update.getAvatar() != null) {
				existing.setAvatar(update.getAvatar());
			}
			if (update.getUserEmail() != null) {
				existing.setUserEmail(update.getUserEmail());
			}
			if (update.getUsername() != null) {
				existing.setUsername(update.getUsername());
			}
			if (update.getUserSmartspace() != null) {
				existing.setUserSmartspace(update.getUserSmartspace());
			}
			if (update.getRole() != null) {
				existing.setRole(update.getRole());
			}
			existing.setPoints(update.getPoints());
		}
	}

	@Override
	public void deleteAll() {
		this.users.clear();
	}
	
	// for unit tests
	protected List<UserEntity> getUsers() {
		return this.users;
	}
	
	// for unit tests
	protected void setUsers(List<UserEntity> users) {
		this.users = users;
	}
}