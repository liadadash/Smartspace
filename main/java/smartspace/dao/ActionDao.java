package smartspace.dao;

import java.util.List;
import smartspace.data.ActionEntity;;

//Amit 13\03
public interface ActionDao {
	public ActionEntity create(ActionEntity actionEntity);
	public List<ActionEntity> readAll(ActionEntity actionEntity);	
	public void deleteAll();
}
//
