package smartspace.dao;

import java.util.List;
import java.util.Optional;

import smartspace.data.ElementEntity;
//Amit 13/03
public interface ElementDao<ElementKey> {
	public ElementEntity create(ElementEntity elementEntity);
	public Optional<ElementEntity> readById(ElementKey elementKey);
	public List<ElementEntity> readAll();
	public void update(ElementEntity ElementEntity);
	public void deleteByKey(ElementKey elementKey);
	public void delete(ElementEntity elementEntity);
	public void deleteAll();
}
//
