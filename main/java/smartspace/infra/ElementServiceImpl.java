package smartspace.infra;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sun.istack.internal.NotNull;

import smartspace.aop.AdminOnly;
import smartspace.dao.EnhancedElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;

@Service
public class ElementServiceImpl implements ElementService {

	Log logger = LogFactory.getLog(ElementServiceImpl.class);

	private EnhancedElementDao<ElementKey> elementDao; // used for element saving and reading

	@Value("${smartspace.name}")
	private String appSmartspace;

	@Autowired
	public ElementServiceImpl(EnhancedElementDao<ElementKey> elementDao) {
		this.elementDao = elementDao;
	}

	@Override
	@Transactional
	@AdminOnly
	public List<ElementEntity> importElements(String adminSmartspace, String adminEmail,
			@NotNull List<ElementEntity> entities) {
		logger.info("Try to import " + entities.size() + "elements by: " + adminSmartspace + "#" + adminEmail);
		return entities.stream().map(this::validate).map(this.elementDao::importElement).collect(Collectors.toList());
	}

	@Override
	@AdminOnly
	public List<ElementEntity> getUsingPagination(String adminSmartspace, String adminEmail, int size, int page) {
		logger.info("Get element using pagination by: " + adminSmartspace + "#" + adminEmail + " size: " + size
				+ ", page: " + page);
		return this.elementDao.readAllWithPaging("key", size, page);
	}

	private ElementEntity validate(ElementEntity entity) {
		logger.debug("Check if element is valid");
		if (!isValid(entity)) {
			logger.debug("Element isn't valid");
			throw new RuntimeException("one or more of the given elements are invalid");
		}
		logger.debug("Element is valid");
		return entity;
	}

	private boolean isValid(ElementEntity entity) {
		return entity.getElementSmartspace() != null && !entity.getElementSmartspace().equals(appSmartspace)
				&& entity.getCreationTimestamp() != null && notEmpty(entity.getCreatorEmail())
				&& notEmpty(entity.getElementId()) && notEmpty(entity.getElementSmartspace())
				&& notEmpty(entity.getName()) && notEmpty(entity.getType()) && entity.getKey() != null
				&& notEmpty(entity.getKey().getElementSmartspace()) && entity.getLocation() != null
				&& entity.getMoreAttributes() != null;
	}

	private boolean notEmpty(String str) {
		return (str != null && !str.trim().isEmpty());
	}

}
