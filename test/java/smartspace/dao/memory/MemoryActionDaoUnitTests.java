package smartspace.dao.memory;
//Amit 21/03

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.data.ActionEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "spring.profiles.active=default, test" })
public class MemoryActionDaoUnitTests {

	@Test
	public void testCreateMessageEntity() throws Exception {
		// GIVEN a Dao is available
		MemoryActionDao dao = new MemoryActionDao();

		// WHEN we create a new action
		// AND we invoke create on the dao

		String smartspace = "studentsList";
		Map<String, Object> moreAttributes = new HashMap<>();
		moreAttributes.put("studentName", "Amit");
		ActionEntity action = new ActionEntity("studentList#1", smartspace, "addStudent", new Date(), "amitp@gmail.com",
				smartspace, moreAttributes);
		ActionEntity rvAction = dao.create(action);

		// THEN the action was added to the dao
		// AND the rvAction has a key which greater than 0

		assertThat(rvAction).isNotNull().extracting("elementId", "elementSmartspace", "actionType", "playerEmail")
				.containsExactly("studentList#1", smartspace, "addStudent", "amitp@gmail.com");
		assertThat(rvAction.getKey().getId()).isNotNull().isGreaterThan(0);
	}

	@Test
	public void testActionEntityReadAllMethod() throws Exception {

		// GIVEN a Dao is available
		MemoryActionDao dao = new MemoryActionDao();

		// WHEN we create a new action entities
		// AND we invoke create on the dao

		String smartspace = "2019B.nadav.peleg";
		Map<String, Object> moreAttributes = new HashMap<>();
		moreAttributes.put("studentAge", "26");

		ActionEntity action1 = new ActionEntity(smartspace + "#1", smartspace, "editStudentName", new Date(),
				"amitp@gmail.com", smartspace, moreAttributes);

		ActionEntity action2 = new ActionEntity(smartspace + "#2", smartspace, "editStudentAge", new Date(),
				"nadavp@gmail.com", smartspace, moreAttributes);

		ActionEntity action3 = new ActionEntity(smartspace + "#3", smartspace, "editStudentPhone", new Date(),
				"liadk@gmail.com", smartspace, moreAttributes);

		ActionEntity rvAction1 = dao.create(action1);
		ActionEntity rvAction2 = dao.create(action2);
		ActionEntity rvAction3 = dao.create(action3);

		// THEN all actions were added to the dao

		assertThat(dao.readAll()).contains(action1).contains(action2).contains(action3);
	}
}
