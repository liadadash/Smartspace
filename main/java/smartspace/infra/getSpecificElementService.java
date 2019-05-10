package smartspace.infra;


import smartspace.layout.ElementBoundary;

public interface getSpecificElementService {
	public ElementBoundary getElement(String userSmartspace,String userEmail,String elementSmartspace,String elementId);
}
