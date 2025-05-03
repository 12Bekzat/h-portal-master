package com.example.demo.Utils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FilterRequest {
    private String logicalOperator; // "AND" или "OR"
    private List<FilterExpression> filterExpressions;
}
