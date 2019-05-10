/**
 * @author liadkh	09-05-2019
 */
package smartspace.infra;

import smartspace.data.ActionEntity;

/**
 * The Interface ActionInvokeService.
 */
public interface ActionInvokeService {

	/**
	 * Invoke action.
	 *
	 * @param actionEntity the action entity
	 * @return the action entity
	 */
	public ActionEntity invokeAction(ActionEntity actionEntity);
}
