package smartspace;

import java.util.List;

import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.UserEntity;
import smartspace.data.util.Faker;
import smartspace.layout.ActionBoundary;
import smartspace.layout.ElementBoundary;

public class TestProgram {
	public static void main(String[] args) throws Exception{
		
		Faker faker = new Faker();
	
		// create fake element entities
		ElementEntity entity1 = faker.entity().element();
		ElementEntity entity2 = faker.entity().element(new ElementKey("Smartspace12", 15)); // choose a specific key
		List<ElementEntity> elements1 = faker.entity().elementList(3);
		ElementEntity[] elements2 = faker.entity().elementArray(2);
		
		// create fake element boundary
		ElementBoundary boundary1 = faker.boundary().element();
		List<ElementBoundary> elements3 = faker.boundary().elementList(2);
		
		// create fake users
		UserEntity user1 = faker.entity().user();
		List<UserEntity> users = faker.entity().userList(3);
		
		// create fake actions (on existing elements)
		ActionEntity action1 = faker.entity().action(entity1);
		List<ActionEntity> actions1 = faker.entity().actionList(elements1, 3);
		List<ActionEntity> actions2 = faker.entity().actionList(elements2, 3);
		ActionEntity[] actions3 = faker.entity().actionArray(elements1, 3);
		
		// create fake action boundary
		ActionBoundary action4 = faker.boundary().action(boundary1);
		List<ActionBoundary> actions4 = faker.boundary().actionList(elements1, 4);
		
		System.out.println(elements1);
		System.out.println(actions4);
	

	}
}
