package smartspace.plugin;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import smartspace.dao.EnhancedElementDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;


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
	private ObjectMapper jackson;

	@Autowired
	public MarkItemActionPlugin(EnhancedElementDao<ElementKey> elementDao) {
		super();
		this.elementDao = elementDao;
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

			return action;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
