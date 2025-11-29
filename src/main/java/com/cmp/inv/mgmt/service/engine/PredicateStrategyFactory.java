package com.cmp.inv.mgmt.service.engine;

import com.cmp.inv.mgmt.service.enums.FieldType;
import com.cmp.inv.mgmt.service.strategy.PredicateStrategy;
import com.cmp.inv.mgmt.service.strategy.impl.BooleanFieldStrategy;
import com.cmp.inv.mgmt.service.strategy.impl.DateFieldStrategy;
import com.cmp.inv.mgmt.service.strategy.impl.NumberFieldStrategy;
import com.cmp.inv.mgmt.service.strategy.impl.StringFieldStrategy;

public class PredicateStrategyFactory {

    private PredicateStrategyFactory() {
    }

    public static PredicateStrategy get(FieldType type) {

        return switch (type) {
            case STRING -> new StringFieldStrategy();
            case NUMBER -> new NumberFieldStrategy();
            case DATE -> new DateFieldStrategy();
            case BOOLEAN -> new BooleanFieldStrategy();
            default ->
                    throw new UnsupportedOperationException(
                            "Strategy not implemented for type: " + type
                    );
        };
    }
}
