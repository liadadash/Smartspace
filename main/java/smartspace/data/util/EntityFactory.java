package smartspace.data.util;

import java.util.Date;
import java.util.Map;

import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.Location;

//Amit 13/03
public interface EntityFactory {
	public UserEntity createNewUser(String userEmail, String userSmartspace, String userName, String avatar,
			UserRole role, long points);

	public ElementEntity createNewElement(String name, String type, Location location, Date creationTimestamp,
			String creatorEmail, String CreatorSmartspace, boolean expiredBoolean, Map<String, Object> moreAtributes);

	public ActionEntity createNewAction(String elementId, String elementSmartspace, String actionType,
			Date creationTimestamp, String playerEmail, String playerSmartspace, Map<String, Object> moreAttributes);
}
//
