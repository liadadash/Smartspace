package smartspace.infra;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.UserKey;

@Service
public class ElementServiceImpl implements ElementService {
	private EnhancedElementDao<ElementKey> elementDao; // used for element saving and reading
	private EnhancedUserDao<UserKey> userDao; // used for admin check
	
	@Value("${smartspace.name}")
	private String appSmartspace;

	@Autowired
	public ElementServiceImpl(EnhancedElementDao<ElementKey> elementDao, EnhancedUserDao<UserKey> userDao) {
		this.elementDao = elementDao;
		this.userDao = userDao;
	}

	@Override
	public List<ElementEntity> importElements(List<ElementEntity> entities, String adminSmartspace, String adminEmail) {

		// check that the user has ADMIN privileges (code is okay)
		if (!userDao.userIsAdmin(new UserKey(adminSmartspace, adminEmail))) {
			throw new RuntimeException("this user is not allowed to import elements");
		}

		// check that all elements are valid
		boolean allValid = entities.stream().allMatch(this::valiadate);

		// if all valid save to database
		if (allValid) {
			return entities.stream().map(this.elementDao::importElement).collect(Collectors.toList());
		} else {
			throw new RuntimeException("one or more of the given elements are invalid");
		}
	}

	@Override
	public List<ElementEntity> getUsingPagination(int size, int page, String adminSmartspace, String adminEmail) {

		// check that the user has ADMIN privileges (code is okay)
		if (!userDao.userIsAdmin(new UserKey(adminSmartspace, adminEmail))) {
			throw new RuntimeException("this user is not allowed to import elements");
		}

		return this.elementDao.readAllWithPaging(size, page).stream().collect(Collectors.toList());
	}

	private boolean valiadate(ElementEntity entity) {
		return !entity.getElementSmartspace().equals(appSmartspace) && entity.getCreationTimestamp() != null
				&& notEmpty(entity.getCreatorEmail()) && notEmpty(entity.getElementId())
				&& notEmpty(entity.getElementSmartspace()) && notEmpty(entity.getName())
				&& notEmpty(entity.getType()) && entity.getKey() != null
				&& notEmpty(entity.getKey().getElementSmartspace()) && entity.getLocation() != null
				&& entity.getMoreAttributes() != null;
	}
	
	private boolean notEmpty(String str) {
		return (str != null && !str.trim().isEmpty());
	}

}
