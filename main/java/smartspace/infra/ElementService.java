package smartspace.infra;
import java.util.List;

import smartspace.data.ElementEntity;
import smartspace.layout.ElementBoundary;

public interface ElementService {
	
	// need to check if user exists and has ADMIN role when you get a request.
	public List<ElementBoundary> importElements(List<ElementEntity> entities, String adminSmartspace, String adminEmail);
	
	// need to check if user exists and has ADMIN role when you get a request.
	public List<ElementBoundary> getUsingPagination (int size, int page, String adminSmartspace, String adminEmail);

}


