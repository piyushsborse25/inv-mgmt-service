package com.cmp.inv.mgmt.service.engine;

import com.cmp.inv.mgmt.service.enums.Conjunction;
import com.cmp.inv.mgmt.service.enums.SearchFieldKey;
import com.cmp.inv.mgmt.service.enums.SearchOperator;
import com.cmp.inv.mgmt.service.records.FieldMetadata;
import com.cmp.inv.mgmt.service.records.SearchKey;
import com.cmp.inv.mgmt.service.registry.SearchMetadataRegistry;
import com.cmp.inv.mgmt.service.request.FilterCondition;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DynamicSpecificationBuilder {

    private final SearchMetadataRegistry registry;

    @Autowired
    public DynamicSpecificationBuilder(SearchMetadataRegistry registry) {
        this.registry = registry;
    }

    public <T> Specification<T> build(List<FilterCondition> filters) {
        if (filters == null || filters.isEmpty()) return null;

        Specification<T> specification = null;

        for (FilterCondition fc : filters) {
            SearchFieldKey fieldEnum = SearchFieldKey.valueOf(fc.getField());
            SearchKey searchKey = registry.findByField(fieldEnum.toField());
            FieldMetadata md = registry.get(searchKey);
            if (md == null) {
                throw new IllegalArgumentException("Field not searchable: " + fc.getField());
            }
            validateOperatorAllowed(fc.getOperator(), md);
            Specification<T> conditionSpec = buildCondition(fc, md);
            specification = (specification == null)
                    ? conditionSpec
                    : applyConjunction(specification, conditionSpec, fc.getNextConj());
        }

        return specification;
    }

    private <T> Specification<T> buildCondition(FilterCondition fc, FieldMetadata md) {

        return (root, query, cb) -> {
            // Validate consistent root entity
            if (!root.getJavaType().equals(md.rootEntityClass())) {
                throw new IllegalStateException(
                        "Mixed root entities detected — all filters must target same root"
                );
            }

            if (query != null) {
                query.distinct(true);
            }

            // Build JOIN path dynamically
            From<?, ?> joinHandle = root;
            for (String relation : md.joinPath()) {
                joinHandle = joinHandle.join(relation, JoinType.LEFT);
            }
            Path<?> attribute = joinHandle.get(md.attribute());

            // Delegate predicate building to strategy based on FieldType
            Predicate predicate;
            predicate = PredicateStrategyFactory
                    .get(md.fieldType())
                    .build(fc.getOperator(), cb, attribute, fc.getValue());

            return predicate;
        };
    }

    private void validateOperatorAllowed(SearchOperator op, FieldMetadata md) {
        if (!md.allowedOperators().contains(op)) {
            throw new IllegalArgumentException(
                    "Operator " + op + " not allowed for field: " + md.field()
            );
        }
    }

    private <T> Specification<T> applyConjunction(Specification<T> base,
                                                  Specification<T> next,
                                                  Conjunction conj) {
        return (conj == Conjunction.OR) ? base.or(next) : base.and(next);
    }
}
