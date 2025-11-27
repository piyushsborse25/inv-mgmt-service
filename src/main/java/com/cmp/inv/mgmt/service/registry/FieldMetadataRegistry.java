package com.cmp.inv.mgmt.service.registry;

import com.cmp.inv.mgmt.service.enums.SearchField;
import com.cmp.inv.mgmt.service.model.FieldMetadata;

public interface FieldMetadataRegistry {
    FieldMetadata get(SearchField field);
}
