package smartspace.plugin;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import smartspace.dao.EnhancedElementDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;


//{
//	"type": "CheckIn",
//	"element": {
//		"id": "1",
//		"smartspace": "2019B.nadav.peleg"
//	},
//	"player": {
//		"smartspace": "2019B.nadav.peleg",
//		"email": "player@gmail.com"
//	},
//	"properties": {
//	
//	}
//}


@Component
public class CheckOutActionPlugin implements Plugin {

	private EnhancedElementDao<ElementKey> elementDao;
	private ObjectMapper jackson;

	@Autowired
	public CheckOutActionPlugin(EnhancedElementDao<ElementKey> elementDao) {
		super();
		this.elementDao = elementDao;
		this.jackson = new ObjectMapper();
	}

	@Override
	public ActionEntity process(ActionEntity action) {
		try {
			long elementId = Long.parseLong(action.getElementId());
			ElementEntity onlineIndicator = elementDao.readById(new ElementKey(action.getElementSmartspace(), elementId)).get();
			
			String attributes = this.jackson.writeValueAsString(onlineIndicator.getMoreAttributes());
			OnlineIndicatorInput input = this.jackson.readValue(attributes, OnlineIndicatorInput.class);
			
			input.removePlayer(action.getPlayerSmartspace(), action.getPlayerEmail());
			onlineIndicator.getMoreAttributes().put("onlineMembers", input.getOnlineMembers());
			
			elementDao.update(onlineIndicator);
			return action;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
