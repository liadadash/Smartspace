package smartspace.infra;
import java.util.List;

import smartspace.data.ElementEntity;

public interface ElementService {
	
	// need to check if user exists and has ADMIN role when you get a request.
	public List<ElementEntity> importElements(String adminSmartspace, String adminEmail, List<ElementEntity> entities);
	
	// need to check if user exists and has ADMIN role when you get a request.
	public List<ElementEntity> getUsingPagination (String adminSmartspace, String adminEmail, int size, int page);

}


