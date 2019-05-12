package smartspace.infra;

import smartspace.data.ElementEntity;

public interface UpdateElementService {

	public void updateElement(String managerSmartspace , String managerEmail ,ElementEntity elementEntity);
}
