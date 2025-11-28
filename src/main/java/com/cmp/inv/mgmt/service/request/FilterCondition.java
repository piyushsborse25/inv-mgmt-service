package com.cmp.inv.mgmt.service.request;

import com.cmp.inv.mgmt.service.enums.Conjunction;
import com.cmp.inv.mgmt.service.enums.SearchOperator;
import lombok.Data;

@Data
public class FilterCondition {

    private String field;
    private SearchOperator operator;
    private Object value;
    private Conjunction nextConj;
}
