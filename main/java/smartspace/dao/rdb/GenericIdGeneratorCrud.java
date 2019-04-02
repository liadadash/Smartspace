/**
 * @author liadkh	02-04-2019
 */
package smartspace.dao.rdb;

import org.springframework.data.repository.CrudRepository;

/**
 * @author liadkh
 *
 */
public interface GenericIdGeneratorCrud<Key> extends CrudRepository<GenericIdGenerator, Key>{

}
