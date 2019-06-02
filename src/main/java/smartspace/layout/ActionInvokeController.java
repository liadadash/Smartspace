/**
 * @author liadkh	09-05-2019
 */
package smartspace.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import smartspace.data.ActionEntity;
import smartspace.infra.ActionInvokeService;

/**
 * The Class ActionInvokeController.
 */
@RestController
public class ActionInvokeController {

	/** The base path. */
	private final String BASE_PATH = "/smartspace/actions";

	/** The action service. */
	private ActionInvokeService actionService;

	/**
	 * Instantiates a new action invoke controller.
	 *
	 * @param actionService the action service
	 */
	@Autowired
	public ActionInvokeController(ActionInvokeService actionService) {
		this.actionService = actionService;
	}

	/**
	 * New action.
	 *
	 * @param boundaryActions the boundary actions
	 * @return the action boundary
	 */
	@RequestMapping(path = BASE_PATH, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary newAction(@RequestBody ActionBoundary boundaryActions) {

		// convert ActionsBoundary to ActionsEntity.
		ActionEntity actionEntity = boundaryActions.convertToEntity();

		return new ActionBoundary(this.actionService.invokeAction(actionEntity));
	}
}
