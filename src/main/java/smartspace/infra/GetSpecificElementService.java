package smartspace.infra;


import smartspace.data.ElementEntity;
import smartspace.data.UserRole;

public interface GetSpecificElementService {
	public ElementEntity getElement(UserRole role, String userSmartspace,String userEmail,String elementSmartspace,String elementId);
}
