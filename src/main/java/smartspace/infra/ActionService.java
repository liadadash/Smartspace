package smartspace.infra;
import java.util.List;

import smartspace.data.ActionEntity;

public interface ActionService {
	
	// need to check if user exists and has ADMIN role when you get a request.
	public List<ActionEntity> importActions(String adminSmartspace, String adminEmail, List<ActionEntity> entities);
	
	// need to check if user exists and has ADMIN role when you get a request.
	public List<ActionEntity> getUsingPagination (String adminSmartspace, String adminEmail, int size, int page);

}


