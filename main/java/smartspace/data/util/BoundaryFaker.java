package smartspace.data.util;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import smartspace.data.ActionEntity;
import smartspace.data.ActionKey;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.layout.ActionBoundary;
import smartspace.layout.ElementBoundary;
import smartspace.layout.UserBoundary;

public class BoundaryFaker {

	private Random random;
	private Faker faker;

	public BoundaryFaker(Faker faker) {
		this.random = new Random();
		this.faker = faker;
	}

	public ElementBoundary element() {
		return new ElementBoundary(faker.entity().element());
	}

	public ElementBoundary element(ElementKey key) {
		return new ElementBoundary(faker.entity().element(key));
	}

	public List<ElementBoundary> elementList(int size) {
		return IntStream.range(0, size).mapToObj(i -> element()).collect(Collectors.toList());
	}

	public ElementBoundary[] elementArray(int size) {
		return elementList(size).toArray(new ElementBoundary[0]);
	}
	
	public UserBoundary user() {
		return new UserBoundary(faker.entity().user());
	}

	public UserBoundary user(UserKey key) {
		return new UserBoundary(faker.entity().user(key));
	}

	public List<UserBoundary> userList(int size) {
		return IntStream.range(0, size).mapToObj(i -> user()).collect(Collectors.toList());
	}

	public UserBoundary[] userArray(int size) {
		return userList(size).toArray(new UserBoundary[0]);
	}
	
	// actions are always performed on elements
	public ActionBoundary action(ElementBoundary elementBoundary) {
		return new ActionBoundary(faker.entity().action(elementBoundary.convertToEntity()));
	}
	
	public ActionBoundary action(ElementBoundary elementBoundary, UserEntity user) {
		return new ActionBoundary(faker.entity().action(elementBoundary.convertToEntity(), user));
	}
	
	public ActionBoundary action(ElementBoundary elementBoundary, UserKey userKey) {
		return new ActionBoundary(faker.entity().action(elementBoundary.convertToEntity(), userKey));
	}
	
	public ActionBoundary action(ElementBoundary elementBoundary, ActionKey key) {
		return new ActionBoundary(faker.entity().action(elementBoundary.convertToEntity(), key));
	}
	
	public ActionBoundary action(ElementEntity elementEntity) {
		return new ActionBoundary(faker.entity().action(elementEntity));
	}
	
	public ActionBoundary action(ElementEntity elementEntity, UserEntity user) {
		return new ActionBoundary(faker.entity().action(elementEntity, user));
	}
	
	public ActionBoundary action(ElementEntity elementEntity, UserKey userKey) {
		return new ActionBoundary(faker.entity().action(elementEntity, userKey));
	}
	
	public ActionBoundary action(ElementEntity elementEntity, ActionKey key) {
		return new ActionBoundary(faker.entity().action(elementEntity, key));
	}

	public List<ActionBoundary> actionList(List<ElementEntity> elements, int size) {
		return IntStream.range(0, size)
				.mapToObj(i -> action( elements.get(random.nextInt(elements.size())) ))
				.collect(Collectors.toList());
	}
	
	public List<ActionBoundary> actionList(ElementEntity[] elements, int size) {
		return IntStream.range(0, size)
				.mapToObj(i -> action( elements[random.nextInt(elements.length)] ))
				.collect(Collectors.toList());
	}
	
	public List<ActionBoundary> actionList(ElementBoundary[] elements, int size) {
		return IntStream.range(0, size)
				.mapToObj(i -> action( elements[random.nextInt(elements.length)] ))
				.collect(Collectors.toList());
	}

	public ActionBoundary[] actionArray(List<ElementEntity> elements, int size) {
		return actionList(elements, size).toArray(new ActionBoundary[0]);
	}
	
	public ActionBoundary[] actionArray(ElementEntity[] elements, int size) {
		return actionList(elements, size).toArray(new ActionBoundary[0]);
	}
	
	public ActionBoundary[] actionArray(ElementBoundary[] elements, int size) {
		return actionList(elements, size).toArray(new ActionBoundary[0]);
	}

}
