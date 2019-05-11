package smartspace.infra;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartspace.aop.AdminOnly;
import smartspace.aop.LoggerService;
import smartspace.dao.EnhancedElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;

@Service
@LoggerService
public class ElementServiceImpl implements ElementService {

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
	public List<ElementEntity> importElements(String adminSmartspace, String adminEmail, List<ElementEntity> entities) {
		return entities.stream().map(this::validate).map(this.elementDao::importElement).collect(Collectors.toList());
	}

	@Override
	@AdminOnly
	public List<ElementEntity> getUsingPagination(String adminSmartspace, String adminEmail, int size, int page) {
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
				&& notEmpty(entity.getName()) && notEmpty(entity.getType()) && entity.getKey() != null
				&& notEmpty(entity.getKey().getElementSmartspace()) && entity.getLocation() != null
				&& entity.getMoreAttributes() != null;
	}

	private boolean notEmpty(String str) {
		return (str != null && !str.trim().isEmpty());
	}

}
