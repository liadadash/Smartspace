package smartspace.infra;

import java.util.List;

import smartspace.data.ActionEntity;

public interface ActionService {
	public ActionEntity newAction(ActionEntity entity, int code);
	
	public List<ActionEntity> getUsingPagination (int size, int page);
}
