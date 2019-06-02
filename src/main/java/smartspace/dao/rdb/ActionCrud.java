package smartspace.dao.rdb;

import org.springframework.data.repository.PagingAndSortingRepository;

import smartspace.data.ActionEntity;
import smartspace.data.ActionKey;

public interface ActionCrud extends
//CrudRepository<ActionEntity, ActionKey>{
		PagingAndSortingRepository<ActionEntity, ActionKey> {

}
