package smartspace.infra;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import smartspace.dao.EnhancedElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.layout.ElementBoundary;

@Service
public class ElementServiceImpl implements ElementService {
	private EnhancedElementDao<ElementKey> elementDao;
	
	@Value("${smartspace.name}")
	private String appSmartspace;

	@Autowired
	public ElementServiceImpl(EnhancedElementDao<ElementKey> elementDao) {
		this.elementDao = elementDao;
	}

	@Override
	public List<ElementBoundary> importElements(List<ElementEntity> entities, String adminSmartspace, String adminEmail) {

		// check that the user has ADMIN privileges (code is okay)
		// if (not admin) {
		// throw new RuntimeException("this user is not allowed to import elements");
		// }

		boolean allValid = entities.stream().allMatch(this::valiadate);

		if (allValid) {
			return entities.stream().map(this.elementDao::importElement).map(ElementBoundary::new).collect(Collectors.toList());
		} else {
			throw new RuntimeException("one or more of the given elements are invalid");
		}
	}

	@Override
	public List<ElementBoundary> getUsingPagination(int size, int page, String adminSmartspace, String adminEmail) {

		// check that the user has ADMIN privileges (code is okay)
		// if (not admin) {
		// throw new RuntimeException("this user is not allowed to export elements");
		// }

		return this.elementDao.readAllWithPaging(size, page).stream().map(ElementBoundary::new).collect(Collectors.toList());
	}

	private boolean valiadate(ElementEntity entity) {
		return !entity.getElementSmartspace().equals(appSmartspace);
//		return entity.getAuthor() != null && entity.getAuthor().getFirst() != null
//				&& !entity.getAuthor().getFirst().trim().isEmpty() && entity.getAuthor().getLast() != null
//				&& !entity.getAuthor().getLast().trim().isEmpty() && entity.getDetails() != null
//				&& entity.getName() != null && !entity.getName().trim().isEmpty() && entity.getSeverity() != null;
	}

}
