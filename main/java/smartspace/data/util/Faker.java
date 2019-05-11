package smartspace.data.util;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class Faker {
	private static String[] domains = { ".com", ".co.uk", ".co.il" };
	private static String[] emailProviders = { "gmail", "yahoo", "hotmail.com", "aol.com", "mail.ru"};
	private static String[] Beginning = { "Kr", "Ca", "Ra", "Mrok", "Cru", "Ray", "Bre", "Zed", "Drak", "Mor", "Jag", "Mer", "Jar", "Mjol", "Zork", "Mad", "Cry", "Zur", "Creo", "Azak", "Azur", "Rei", "Cro", "Mar", "Luk" };
	private static String[] Middle = { "air", "ir", "mi", "sor", "mee", "clo", "red", "cra", "ark", "arc", "miri", "lori", "cres", "mur", "zer", "marac", "zoir", "slamar", "salmar", "urak" };
	private static String[] End = { "d", "ed", "ark", "arc", "es", "er", "der", "tron", "med", "ure", "zur", "cred", "mur" };

	public String appSmartspace = "dummySmartspace"; // TODO: load from constructor optional
	
	private Random random;
	private Long nextID;
	private EntityFaker entityFaker;
	private BoundaryFaker boundaryFaker;
	
	public Faker() {
		this.random = new Random();
		this.nextID = (long) generateNumber(1, 1000);
		this.entityFaker = new EntityFaker(this);
		this.boundaryFaker = new BoundaryFaker(this);
	}
	
	public EntityFaker entity() {
		return this.entityFaker;
	}
	
	public BoundaryFaker boundary() {
		return this.boundaryFaker;
	}
	
	public String generateEmail() {
		String emailDomain = emailProviders[random.nextInt(emailProviders.length)];
		if (emailDomain.indexOf('.') < 0) {
			emailDomain = emailDomain + domains[random.nextInt(domains.length)];
		}
		
		return generateName(6) + generateNumber(10, 100) + "." + generateId() + "@" + emailDomain;
	}

	public String generateName(int maxLength) {
		String name = Beginning[random.nextInt(Beginning.length)] + Middle[random.nextInt(Middle.length)] + End[random.nextInt(End.length)];
		
		if (name.length() <= maxLength) {
			return name;
		}
		
		return generateName(maxLength);
	}
	
	public int generateNumber(int low, int high) {
		return random.nextInt(high - low) + low;
	}

	public double generateDouble(double low, double high) {
		double num = random.nextDouble() * (high - low) + low; 
		return Math.round(num * 10.0) / 10.0;
	}
	
	public Map<String, Object> generateMap(boolean allowMap) {
		Map<String, Object> map = new TreeMap<>();
		
		if (diceThrow(0.5)) {
			map.put("serialNumber", String.valueOf(generateNumber(1, 10000)));
		}
		if (diceThrow(0.25)) {
			map.put("lang", "en");
		}
		if (diceThrow(0.6)) {
			map.put("lastUpdate", new Date());
		}
		if (diceThrow(0.5)) {
			map.put("maxItems", generateNumber(1, 100));
		}
		if (diceThrow(0.25)) {
			int[] arr = {generateNumber(1, 100), generateNumber(1, 100), generateNumber(1, 100)};
			map.put("numbers", arr);
		}
		if (diceThrow(0.2)) {
			map.put("latitude", generateDouble(-60, 60));
		}
		if (diceThrow(0.5)) {
			map.put("restoreAvailable", false);
		}
		if (diceThrow(0.15)) {
			map.put("important", true);
		}
		if (allowMap && diceThrow(0.3)) {
			map.put("json", generateMap(false));
		}
		
		return map;
	}
	
	// e.g throw a dice with 30% success chance
	private boolean diceThrow(double successProb) {
		double chance = random.nextDouble();
		if (chance <= successProb) {
			return true;
		}

		return false;
	}

	public Long generateId() {
		Long id = this.nextID;
		this.nextID = this.nextID + generateNumber(1, 10);

		return id;
	}

	public <T extends Enum<?>> T randomEnum(Class<T> clazz) {
		int x = random.nextInt(clazz.getEnumConstants().length);
		return clazz.getEnumConstants()[x];
	}
	
}
