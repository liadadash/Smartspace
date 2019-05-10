package smartspace.infra;


import smartspace.data.ElementEntity;
import smartspace.layout.ElementBoundary;

public interface getSpecificElementService {
	public ElementEntity getElement(String userSmartspace,String userEmail,String elementSmartspace,String elementId);
}
