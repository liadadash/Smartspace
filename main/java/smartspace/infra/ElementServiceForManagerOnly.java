package smartspace.infra;

import smartspace.data.ElementEntity;

public interface ElementServiceForManagerOnly {

	public ElementEntity createNewElement(String managerSmartspace,String managerEmail,ElementEntity newElement);
	

}
