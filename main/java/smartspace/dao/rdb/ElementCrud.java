package smartspace.dao.rdb;

import org.springframework.data.repository.PagingAndSortingRepository;

import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;

public interface ElementCrud extends
//CrudRepository<ElementEntity, ElementKey>{
		PagingAndSortingRepository<ElementEntity, ElementKey> {
}
