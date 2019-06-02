/**
 * @author liadkh	02-04-2019
 */
package smartspace.dao.rdb;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "database_sequences")
public class GenericIdGenerator {

	private String sequenceId;
	private long sequenceValue;

	public GenericIdGenerator(String sequenceId) {
		this.sequenceId = sequenceId;
	}

	@Id
	public String getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(String sequenceId) {
		this.sequenceId = sequenceId;
	}

	public long getSequenceValue() {
		return sequenceValue;
	}

	public void setSequenceValue(long sequenceValue) {
		this.sequenceValue = sequenceValue;
	}
	
	
	
}