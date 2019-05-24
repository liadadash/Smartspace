package smartspace.dao.rdb;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RdbSequenceDao {
	private GenericIdGeneratorCrud<String> genericIdGeneratorCrud;

	@Autowired
	public RdbSequenceDao(GenericIdGeneratorCrud<String> genericIdGeneratorCrud) {
		super();
		this.genericIdGeneratorCrud = genericIdGeneratorCrud;
	}

	public long generateNextId(String sequenceName) {
		GenericIdGenerator sequence = new GenericIdGenerator(sequenceName);
		Optional<GenericIdGenerator> currentSeq = this.genericIdGeneratorCrud.findById(sequenceName);
		

		if (currentSeq.isPresent()) {
			sequence = currentSeq.get();
		}

		// increase id and save
		sequence.setSequenceValue(sequence.getSequenceValue() + 1);
		this.genericIdGeneratorCrud.save(sequence);
		
		return sequence.getSequenceValue();
	}

}
