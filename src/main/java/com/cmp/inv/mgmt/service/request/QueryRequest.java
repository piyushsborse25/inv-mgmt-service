package com.cmp.inv.mgmt.service.request;

import lombok.Data;

import java.util.List;

@Data
public class QueryRequest {

    private List<FilterCondition> filters;
    private SortRule sort;
    private Integer page;
    private Integer size;
}
