package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import smartspace.dao.UserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;

@Repository
public class RdbUserDao implements UserDao<UserKey> {
	private UserCrud userCrud;

	@Autowired
	public RdbUserDao(UserCrud userCrud) {
		super();
		this.userCrud = userCrud;
	}

	@Override
	@Transactional
	public UserEntity create(UserEntity userEntity) {
		userEntity.setUserSmartspace("2019B.nadav.peleg");
		userEntity.setKey(new UserKey(userEntity.getUserSmartspace(), userEntity.getUserEmail()));

		if (!this.userCrud.existsById(userEntity.getKey())) {
			UserEntity rv = this.userCrud.save(userEntity);
			return rv;
		} else {
			throw new RuntimeException("user already exists with key: " + userEntity.getKey());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readAll() {
		List<UserEntity> rv = new ArrayList<>();

		this.userCrud.findAll().forEach(rv::add);
		return rv;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<UserEntity> readById(UserKey userKey) {
		return this.userCrud.findById(userKey);
	}

	@Override
	@Transactional
	public void update(UserEntity update) {

		UserEntity existing = this.readById(update.getKey())
				.orElseThrow(() -> new RuntimeException("no user to update"));

		if (update.getAvatar() != null) {
			existing.setAvatar(update.getAvatar());
		}
//		if (update.getUserEmail() != null) {
//			existing.setUserEmail(update.getUserEmail());
//		}
		if (update.getUsername() != null) {
			existing.setUsername(update.getUsername());
		}
//		if (update.getUserSmartspace() != null) {
//			existing.setUserSmartspace(update.getUserSmartspace());
//		}
		if (update.getRole() != null) {
			existing.setRole(update.getRole());
		}
		existing.setPoints(update.getPoints());

		this.userCrud.save(existing);
	}

	@Override
	@Transactional
	public void deleteAll() {
		this.userCrud.deleteAll();
	}

}
