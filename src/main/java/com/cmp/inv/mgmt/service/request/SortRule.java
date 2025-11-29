package com.cmp.inv.mgmt.service.request;

import com.cmp.inv.mgmt.service.enums.SortDirection;
import lombok.Data;

@Data
public class SortRule {
    private String field;
    private SortDirection direction;
}
