package api.contract.diff;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Detects breaking changes between two contract versions.
 */
public final class ContractDiffer {
	private ContractDiffer() {
		// TODO Auto-generated constructor stub
	}
	
    /**
     * Compare two contract field/type maps.
     */
	public static ContractDiffResult diff(Map<String, Class<?>> oldContract, Map<String, Class<?>> newContract) {
		
		Set<String> removed= new HashSet<>();
		Set<String> added= new HashSet<>();
		Set<String> typeChanged= new HashSet<>();
		
        // Detect removed & type-changed fields
		oldContract.forEach((field, oldType) ->{
			
            // Field removed
			if(!newContract.containsKey(field)) {
				removed.add(field);
				return;
			}
			
			// Field type changed
			Class<?> newType = newContract.get(field);
			if (!oldType.equals(newType)) {
				typeChanged.add(field);
			}
		});
		
		// Detect newly added fields
		newContract.keySet().forEach(field -> {
			if (!oldContract.containsKey(field)) {
				added.add(field);
			}
		});
		
		return new ContractDiffResult(removed, added, typeChanged);		
	}
}
