package com.futureprocessing.documentjuggler.query.command;


import com.futureprocessing.documentjuggler.query.operators.ComparisonOperators;
import com.futureprocessing.documentjuggler.query.operators.Comparison;
import com.mongodb.QueryBuilder;

public class ComparisonOperatorsCommand implements QueryCommand {

    final private String field;

    public ComparisonOperatorsCommand(String field) {
        this.field = field;
    }

    @Override
    public void query(QueryBuilder builder, Object[] args) {
        Comparison consumer = (Comparison) args[0];

        ComparisonOperators comparisonOperators = new ComparisonOperators(field, builder);
        consumer.apply(comparisonOperators);
    }


}
