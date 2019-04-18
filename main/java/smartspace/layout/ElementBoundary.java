package smartspace.layout;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.Location;

public class ElementBoundary {
	private Map<String, String> key;
	private String elementType;
	private String name;
	private boolean expired;
	private Date created;
	private Map<String, String> creator;
	private Map<String, Double> latlng;
	private Map<String, Object> elementProperties;
	
	public ElementBoundary() {
	}

	public ElementBoundary(ElementEntity entity) {
		this.key = new TreeMap<String, String>();
		this.key.put("id", entity.getElementId());
		this.key.put("smartspace", entity.getElementSmartspace());
		
		this.elementType = entity.getType();
		this.name = entity.getName();
		this.expired = entity.getExpired();
		this.created = entity.getCreationTimestamp();
		
		this.creator = new TreeMap<String, String>();
		this.creator.put("email", entity.getCreatorEmail());
		this.creator.put("smartspace", entity.getCreatorSmartspace());
		
		this.latlng = new TreeMap<String, Double>();
		this.latlng.put("lat", entity.getLocation().getX());
		this.latlng.put("lng", entity.getLocation().getY());
		
		this.elementProperties = entity.getMoreAttributes();
	}
	
	public Map<String, String> getKey() {
		return key;
	}

	public void setKey(Map<String, String> key) {
		this.key = key;
	}

	public String getElementType() {
		return elementType;
	}

	public void setElementType(String elementType) {
		this.elementType = elementType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Map<String, String> getCreator() {
		return creator;
	}

	public void setCreator(Map<String, String> creator) {
		this.creator = creator;
	}

	public Map<String, Double> getLatlng() {
		return latlng;
	}

	public void setLatlng(Map<String, Double> latlng) {
		this.latlng = latlng;
	}

	public Map<String, Object> getElementProperties() {
		return elementProperties;
	}

	public void setElementProperties(Map<String, Object> elementProperties) {
		this.elementProperties = elementProperties;
	}

	public ElementEntity convertToEntity() {
		ElementEntity entity = new ElementEntity();
		
		// need to check for nulls where it can happen like
		////		if (this.key != null) { give default value 
		
		// need to check parse long exception
		if (this.key != null && this.key.get("smartspace") != null && this.key.get("id") != null) {
			entity.setKey(new ElementKey(this.key.get("smartspace"), Long.parseLong(this.key.get("id"))));
		}
		
		entity.setType(this.elementType);
		entity.setName(this.name);
		entity.setExpired(this.expired);
		entity.setCreationTimestamp(this.created);
		
		if (this.creator != null && this.creator.get("email") != null && this.creator.get("smartspace") != null) {
			entity.setCreatorEmail(this.creator.get("email"));
			entity.setCreatorSmartspace(this.creator.get("smartspace"));
		}
		
		if (this.latlng != null && this.latlng.get("lat") != null && this.latlng.get("lng") != null) {
			entity.setLocation(new Location(this.latlng.get("lat"), this.latlng.get("lng")));
		}
		
		entity.setMoreAttributes(this.elementProperties);
		
		return entity;
	}
	
	
}
