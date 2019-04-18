package smartspace.infra;
import java.util.List;

import smartspace.data.ActionEntity;

public interface ActionService {
	
	// need to check if user exists and has ADMIN role when you get a request.
	public List<ActionEntity> importActions(List<ActionEntity> entities, String adminSmartspace, String adminEmail);
	
	// need to check if user exists and has ADMIN role when you get a request.
	public List<ActionEntity> getUsingPagination (int size, int page, String adminSmartspace, String adminEmail);

}


