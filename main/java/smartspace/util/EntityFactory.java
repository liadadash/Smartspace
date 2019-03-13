package smartspace.util;

import java.util.Date;
import java.util.Map;

import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.Location;

//Amit 13\03
public interface EntityFactory {
	public UserEntity createNewUser(String userEmail, String userSmartSpace, String userName, String avatar,
			UserRole rule, long points);

	public ElementEntity createElement(String name, String type, Location location, Date creationTimestamp,
			String creatorEmail, String CreatorSmartSpace, boolean expiredBoolean, Map<String, Object> moreAtributes);

	public ActionEntity createAction(String elementId, String elementSmartSpace, String actionType,
			Date creationTimestamp, String playerEmail, String playerSmartSpace, Map<String, Object> moreAttributes);
}
//
