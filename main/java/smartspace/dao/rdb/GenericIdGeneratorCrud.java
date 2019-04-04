/**
 * @author liadkh	02-04-2019
 */
package smartspace.dao.rdb;

import org.springframework.data.repository.CrudRepository;

/**
 * The Interface GenericIdGeneratorCrud.
 *
 * @author liadkh
 * @param <Key> the generic type
 */
public interface GenericIdGeneratorCrud<Key> extends CrudRepository<GenericIdGenerator, Key>{

}
