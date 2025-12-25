package api.contract.diff;

import java.util.Set;

/**
 * Holds result of contract comparison.
 */
public class ContractDiffResult {

    // Fields removed from newer version
	private final Set<String> removedFields;
	
    // Fields newly added in newer version
	private final Set<String> addedFields;
	
	 // Fields with changed data types
	private final Set<String> typeChangedFields;
	
	public ContractDiffResult(Set<String> removed, Set<String> added, Set<String> typeChanged) {
		this.removedFields= removed;
		this.addedFields= added;
		this.typeChangedFields= typeChanged;
	}
	
	public boolean isBeakingChanges() {
		return !removedFields.isEmpty() || !typeChangedFields.isEmpty();
	}

	public Set<String> removed() {
		return removedFields;
	}

	public Set<String> added() {
		return addedFields;
	}

	public Set<String> typeChanged() {
		return typeChangedFields;
	}
	
	
}
