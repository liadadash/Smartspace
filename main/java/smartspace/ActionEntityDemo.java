package smartspace;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import smartspace.dao.ActionDao;
import smartspace.data.ActionEntity;
import smartspace.data.util.EntityFactoryImpl;

@Component
public class ActionEntityDemo implements CommandLineRunner {
	private EntityFactoryImpl factory;
	private ActionDao dao;

	public ActionEntityDemo() {
	}

	@Autowired
	public ActionEntityDemo(EntityFactoryImpl factory, ActionDao dao) {
		this.factory = factory;
		this.dao = dao;
	}

	@Override
	public void run(String... args) throws Exception {

		String smartspace = "ShoppingList";
		System.err.println("\n---------- ActionEntityDemo ----------");

		// # ------ action1 ------#

		Map<String, Object> moreAttributes = new HashMap<>();
		moreAttributes.put("oldName", "Food");
		moreAttributes.put("newName", "Drinks");

		ActionEntity action1 = this.factory.createNewAction("ShoppingList.category#1", smartspace, "editName", new Date(), "john123@gmail.com", smartspace, moreAttributes);
		action1.setActionSmartspace(smartspace);

		System.err.println("action1 before saving to database: " + action1);
		action1 = this.dao.create(action1);
		System.err.println("action1 after saving to database: " + action1);

		// # ------ action2 ------#

		moreAttributes = new HashMap<>();
		moreAttributes.put("categoryName", "Dairy");
		moreAttributes.put("deletedItems", "{Item#123, Item#64, Item#87}");
		moreAttributes.put("restoreAvailable", "true");

		ActionEntity action2 = this.factory.createNewAction("ShoppingList.category#2", smartspace, "deleteCategory", new Date(), "david80@gmail.com", smartspace, moreAttributes);
		action2.setActionSmartspace(smartspace);

		System.err.println("\naction2 before saving to database: " + action2);
		action1 = this.dao.create(action2);
		System.err.println("action2 after saving to database: " + action2);

		// # ------ read all actions ------#
		if (this.dao.readAll().size() == 2 && this.dao.readAll().contains(action1)
				&& this.dao.readAll().contains(action2)) {
			System.err.println("\nall actions were read successfully");

		} else {
			throw new RuntimeException("\nsome actions were not read");
		}

		// # ------ clear all actions ------#
		this.dao.deleteAll();
		if (this.dao.readAll().isEmpty()) {
			System.err.println("all actions deleted successfully");
		} else {
			throw new RuntimeException("\nsome actions were not deleted");
		}

	}

}
