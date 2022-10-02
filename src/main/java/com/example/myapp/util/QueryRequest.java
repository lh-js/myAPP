package com.example.myapp.util;

import lombok.Data;

@Data
public class QueryRequest<T> {
    private PageCondition pageCondition;
    private T condition;
}
