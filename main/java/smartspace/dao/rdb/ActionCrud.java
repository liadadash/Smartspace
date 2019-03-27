package smartspace.dao.rdb;

import org.springframework.data.repository.CrudRepository;

import smartspace.data.ActionEntity;
import smartspace.data.ActionKey;

public interface ActionCrud extends CrudRepository<ActionEntity, ActionKey>{
}
