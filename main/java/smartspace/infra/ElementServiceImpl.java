package smartspace.infra;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
	public List<ElementEntity> importElements(List<ElementEntity> entities, String adminSmartspace, String adminEmail) {
		// check that the user has ADMIN privileges (code is okay)
		if (!userDao.userIsAdmin(new UserKey(adminSmartspace, adminEmail))) {
			throw new RuntimeException("this user is not allowed to import elements");
		}

		return entities.stream().map(this::validate).map(this.elementDao::importElement).collect(Collectors.toList());
	}

	@Override
	public List<ElementEntity> getUsingPagination(int size, int page, String adminSmartspace, String adminEmail) {

		// check that the user has ADMIN privileges (code is okay)
		if (!userDao.userIsAdmin(new UserKey(adminSmartspace, adminEmail))) {
			throw new RuntimeException("this user is not allowed to import elements");
		}

		return this.elementDao.readAllWithPaging("key", size, page);
	}

	private ElementEntity validate(ElementEntity entity) {
		if (!isValid(entity)) {
			throw new RuntimeException("one or more of the given elements are invalid");
		}
		
		return entity;
	}

	private boolean isValid(ElementEntity entity) {
		return entity.getElementSmartspace() != null && !entity.getElementSmartspace().equals(appSmartspace) 
				&& entity.getCreationTimestamp() != null && notEmpty(entity.getCreatorEmail()) 
				&& notEmpty(entity.getElementId()) && notEmpty(entity.getElementSmartspace()) 
				&& notEmpty(entity.getName()) && notEmpty(entity.getType()) 
				&& entity.getKey() != null && notEmpty(entity.getKey().getElementSmartspace()) 
				&& entity.getLocation() != null && entity.getMoreAttributes() != null;
	}
	
	private boolean notEmpty(String str) {
		return (str != null && !str.trim().isEmpty());
	}

}
