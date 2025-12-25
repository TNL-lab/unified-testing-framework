package api.enums;

/**
 * Defines API contract validation strategy.
 *
 * Each mode represents a different strictness level
 * when validating API response against contract.
 */
public enum ContractMode {
    /**
     * Fail on ANY contract change
     * - Missing field
     * - Extra field
     * - Type change
     */
    STRICT,

    /**
     * Allow additive changes
     * - New fields allowed
     * - Existing fields must remain
     */
    LOOSE,

    /**
     * Validate response against JSON Schema
     */
    SCHEMA
}
