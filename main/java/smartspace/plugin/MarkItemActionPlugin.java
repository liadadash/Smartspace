package smartspace.plugin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;

//{
//	"type": "MarkItem",
//	"element": {
//		"id": "1",
//		"smartspace": "2019B.nadav.peleg"
//	},
//	"player": {
//		"smartspace": "2019B.nadav.peleg",
//		"email": "player@gmail.com"
//	},
//	"properties": {
//		markStatus: true;
//	}
//}

@Component
public class MarkItemActionPlugin implements Plugin {

	private EnhancedElementDao<ElementKey> elementDao;
	private EnhancedUserDao<UserKey> userDao;
	private ObjectMapper jackson;

	public static int POINTS_PER_MARK = 5;

	@Autowired
	public MarkItemActionPlugin(EnhancedElementDao<ElementKey> elementDao, EnhancedUserDao<UserKey> userDao) {
		super();
		this.elementDao = elementDao;
		this.userDao = userDao;
		this.jackson = new ObjectMapper();
	}

	@Override
	public ActionEntity process(ActionEntity action) {
		try {
			String attributes = this.jackson.writeValueAsString(action.getMoreAttributes());
			MarkItemInput markInput = this.jackson.readValue(attributes, MarkItemInput.class);

			long elementId = Long.parseLong(action.getElementId());
			ElementEntity item = elementDao.readById(new ElementKey(action.getElementSmartspace(), elementId)).get();

			item.getMoreAttributes().put("marked", markInput.getMarkStatus());
			elementDao.update(item);

			Optional<UserEntity> userData = userDao.readById(new UserKey(action.getPlayerSmartspace(), action.getPlayerEmail()));

			// + 5 points if bought item -5 points if removed marked
			int pointsGiven = POINTS_PER_MARK;
			if (!markInput.getMarkStatus()) {
				pointsGiven = POINTS_PER_MARK * -1;
			}

			// give points to user, can go below 0;
			if (userData.isPresent()) {
				UserEntity user = userData.get();
				user.setPoints(user.getPoints() + pointsGiven);
				userDao.addPoints(user);
			}

			return action;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
