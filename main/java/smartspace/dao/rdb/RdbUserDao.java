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

	// TODO remove this
	private UserKey nextId;

	@Autowired
	public RdbUserDao(UserCrud userCrud) {
		super();
		this.userCrud = userCrud;
		// TODO remove this
		this.nextId = new UserKey();
	}

	@Override
	@Transactional
	public UserEntity create(UserEntity userEntity) {
		// SQL: INSERT INTO USERS (USERNAME,AVATAR ,ROLE,POINTS,KEY) VALUES (?,?,?,?,?);

		nextId.setUserSmartspace(userEntity.getUserSmartspace());
		nextId.setUserEmail(userEntity.getUserEmail());

		userEntity.setKey(nextId);

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

		// SQL: SELECT
		this.userCrud.findAll().forEach(rv::add);
		return rv;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<UserEntity> readById(UserKey userKey) {
		// SQL: SELECT
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

		// SQL: UPDATE
		this.userCrud.save(existing);
	}

	@Override
	@Transactional
	public void deleteAll() {
		// SQL: DELETE
		this.userCrud.deleteAll();
	}

}
