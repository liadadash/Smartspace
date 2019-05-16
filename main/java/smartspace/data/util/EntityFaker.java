package smartspace.data.util;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import smartspace.data.ActionEntity;
import smartspace.data.ActionKey;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;

public class EntityFaker {

	private static String[] elementTypes = { "list", "item", "category" };
	private static String[] actionTypes = { "ECHO"};

	private Random random;
	private Faker faker;

	public EntityFaker(Faker faker) {
		this.random = new Random();
		this.faker = faker;
	}

	public ElementEntity element() {
		ElementEntity entity = new ElementEntity();

		entity.setCreationTimestamp(new Date());
		entity.setCreatorEmail(faker.generateEmail());
		entity.setCreatorSmartspace(faker.appSmartspace);
		entity.setElementId(faker.generateId().toString());
		entity.setElementSmartspace(faker.appSmartspace);
		entity.setExpired(false);
		entity.setLocation(new Location(faker.generateDouble(0, 100), faker.generateDouble(0, 100)));
		entity.setName("element" + faker.generateNumber(1, 1000));
		entity.setType(elementTypes[random.nextInt(elementTypes.length)]);
		entity.setMoreAttributes(faker.generateMap(true));

		return entity;
	}

	public ElementEntity element(ElementKey key) {
		ElementEntity entity = this.element();
		entity.setKey(key);
		return entity;
	}
	
	public ElementEntity element(boolean expired) {
		ElementEntity entity = this.element();
		entity.setExpired(expired);
		return entity;
	}

	public List<ElementEntity> elementList(int size) {
		return IntStream.range(0, size).mapToObj(i -> element()).collect(Collectors.toList());
	}

	public ElementEntity[] elementArray(int size) {
		return elementList(size).toArray(new ElementEntity[0]);
	}

	public UserEntity user() {
		UserEntity user = new UserEntity();

		user.setAvatar("https://images.com/" + faker.generateName(100) + ".jpg");
		user.setPoints(faker.generateNumber(0, 100));
		user.setRole(faker.randomEnum(UserRole.class));
		user.setUserEmail(faker.generateEmail());
		user.setUsername(faker.generateName(7) + faker.generateNumber(10, 100));
		user.setUserSmartspace(faker.appSmartspace);

		return user;
	}

	public UserEntity user(UserKey key) {
		UserEntity user = this.user();
		user.setKey(key);
		return user;
	}

	public UserEntity user(UserRole role) {
		UserEntity user = this.user();
		user.setRole(role);
		return user;
	}

	public UserEntity user(UserKey key, UserRole role) {
		UserEntity user = this.user();
		user.setKey(key);
		user.setRole(role);
		return user;
	}

	public List<UserEntity> userList(int size) {
		return IntStream.range(0, size).mapToObj(i -> user()).collect(Collectors.toList());
	}

	public UserEntity[] userArray(int size) {
		return userList(size).toArray(new UserEntity[0]);
	}

	// actions are always performed on elements
	public ActionEntity action(ElementEntity elementEntity) {
		ActionEntity actionEntity = new ActionEntity();

		actionEntity.setActionId(faker.generateId().toString());
		actionEntity.setActionSmartspace(faker.appSmartspace);
		actionEntity.setActionType(actionTypes[random.nextInt(actionTypes.length)]);
		actionEntity.setCreationTimestamp(new Date());
		actionEntity.setMoreAttributes(faker.generateMap(true));
		actionEntity.setPlayerEmail(faker.generateEmail());
		actionEntity.setPlayerSmartspace(faker.appSmartspace);

		// important make sure related element is in database or import will fail
		actionEntity.setElementId(elementEntity.getElementId());
		actionEntity.setElementSmartspace(elementEntity.getElementSmartspace());

		return actionEntity;
	}
	
	public ActionEntity action(ElementEntity elementEntity, UserKey userKey) {
		ActionEntity entity = this.action(elementEntity);
		entity.setPlayerSmartspace(userKey.getUserSmartspace());
		entity.setPlayerEmail(userKey.getUserEmail());
		return entity;
	}
	
	public ActionEntity action(ElementEntity elementEntity, UserEntity user) {
		ActionEntity entity = this.action(elementEntity);
		entity.setPlayerSmartspace(user.getUserSmartspace());
		entity.setPlayerEmail(user.getUserEmail());
		return entity;
	}

	public ActionEntity action(ElementEntity elementEntity, ActionKey key) {
		ActionEntity entity = this.action(elementEntity);
		entity.setKey(key);
		return entity;
	}

	public List<ActionEntity> actionList(List<ElementEntity> elements, int size) {
		return IntStream.range(0, size).mapToObj(i -> action(elements.get(random.nextInt(elements.size()))))
				.collect(Collectors.toList());
	}

	public List<ActionEntity> actionList(ElementEntity[] elements, int size) {
		return IntStream.range(0, size).mapToObj(i -> action(elements[random.nextInt(elements.length)]))
				.collect(Collectors.toList());
	}

	public ActionEntity[] actionArray(List<ElementEntity> elements, int size) {
		return actionList(elements, size).toArray(new ActionEntity[0]);
	}

	public ActionEntity[] actionArray(ElementEntity[] elements, int size) {
		return actionList(elements, size).toArray(new ActionEntity[0]);
	}

}
