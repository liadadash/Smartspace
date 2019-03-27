	package smartspace.data;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import smartspace.dao.rdb.MapToJsonConverter;

//aviel
//Added DB - persistence by Amit 27/3
@Entity
@Table(name = "ACTIONS")
public class ActionEntity implements SmartspaceEntity<ActionKey> {

	private String actionSmartspace;
	private String actionId;
	private String elementSmartspace;
	private String elementId;
	private String playerSmartspace;
	private String playerEmail;
	private String actionType;
	private Date creationTimestamp;
	private Map<String, Object> moreAttributes;

	// default constructor
	public ActionEntity() {

	}

	public ActionEntity(String elementId, String elementSmartspace, String actionType, Date creationTimestamp,
			String playerEmail, String playerSmartspace, Map<String, Object> moreAttributes) {
		this.elementId = elementId;
		this.elementSmartspace = elementSmartspace;
		this.actionType = actionType;
		this.creationTimestamp = creationTimestamp;
		this.playerEmail = playerEmail;
		this.playerSmartspace = playerSmartspace;
		this.moreAttributes = moreAttributes;
	}

	@Column(name = "actionSmartSpaceaK")
	public String getActionSmartspace() {
		return actionSmartspace;
	}

	public void setActionSmartspace(String actionSmartspace) {
		this.actionSmartspace = actionSmartspace;
	}

	public String getElementSmartspace() {
		return elementSmartspace;
	}

	public void setElementSmartspace(String elementSmartspace) {
		this.elementSmartspace = elementSmartspace;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getPlayerSmartspace() {
		return playerSmartspace;
	}

	public void setPlayerSmartspace(String playerSmartspace) {
		this.playerSmartspace = playerSmartspace;
	}

	public String getPlayerEmail() {
		return playerEmail;
	}

	public void setPlayerEmail(String playerEmail) {
		this.playerEmail = playerEmail;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	@Lob
	@Convert(converter = MapToJsonConverter.class)
	public Map<String, Object> getMoreAttributes() {
		return moreAttributes;
	}

	public void setMoreAttributes(Map<String, Object> moreAttributes) {
		this.moreAttributes = moreAttributes;
	}

	@Override
	public String toString() {
		return "ActionEntity [actionSmartspace=" + actionSmartspace + ", actionId=" + actionId + ", elementSmartspace="
				+ elementSmartspace + ", elementId=" + elementId + ", playerSmartspace=" + playerSmartspace
				+ ", playerEmail=" + playerEmail + ", actionType=" + actionType + ", creationTimestamp="
				+ creationTimestamp + ", moreAttributes=" + moreAttributes + "]";
	}

	/*
	 * @author liadk
	 *
	 * @see smartspace.data.SmartspaceEntity#getKey()
	 * 
	 */
	@Override
	@EmbeddedId
	@Column(name = "ID")
	public ActionKey getKey() {
		// added this because otherwise calling getKey before create causes exception.
		long id = 0;

		try {
			id = Long.parseLong(this.actionId);
		} catch (Exception e) {
			return null;
		}
		return new ActionKey(actionSmartspace, id);
	}

	/*
	 * @author liadk
	 *
	 * @see smartspace.data.SmartspaceEntity#setKey(java.lang.Object)
	 * 
	 */
	@Override
	public void setKey(ActionKey k) {
		this.setActionSmartspace(k.getActionSmartspace());
		this.actionId = String.valueOf(k.getId());
	}

}
