package smartspace.dao.rdb;

import org.springframework.data.repository.CrudRepository;

import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;

public interface ElementCrud extends CrudRepository<ElementEntity, ElementKey>{
}
