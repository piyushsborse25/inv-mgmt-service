package com.cmp.inv.mgmt.service.enums;

import lombok.Getter;

@Getter
public enum SearchOperator  {

    // Common for text, enums, IDs
    EQUALS("Equals"),
    NOT_EQUALS("Not Equals"),

    // String only
    CONTAINS("Contains"),
    NOT_CONTAINS("Does Not Contain"),
    STARTS_WITH("Starts With"),
    ENDS_WITH("Ends With"),

    // Numeric / Date only
    GREATER_THAN("Greater Than"),
    GREATER_THAN_OR_EQUAL("Greater Than or Equal"),
    LESS_THAN("Less Than"),
    LESS_THAN_OR_EQUAL("Less Than or Equal"),
    BETWEEN("Between"),

    // Set checks
    IN("In List"),
    NOT_IN("Not In List"),

    // Null checks
    IS_NULL("Is Null"),
    IS_NOT_NULL("Is Not Null"),
    BEFORE("Before"), AFTER("After"), ON("On");

    private final String label;

    SearchOperator(String label) {
        this.label = label;
    }
}