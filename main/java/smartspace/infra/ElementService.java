package smartspace.infra;
import java.util.List;

import smartspace.data.ElementEntity;

public interface ElementService {
	
	// need to check if user exists and has ADMIN role when you get a request.
	public List<ElementEntity> importElements(List<ElementEntity> entities, String adminSmartspace, String adminEmail);
	
	// need to check if user exists and has ADMIN role when you get a request.
	public List<ElementEntity> getUsingPagination (int size, int page, String adminSmartspace, String adminEmail);

}


