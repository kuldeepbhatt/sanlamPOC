package com.sanlam.SanlamPOC.utility;

//Note: This is not a full-fleged generic QueryBuilder but just an idea to build a generic builder which can be shared across different business modules
public class QueryBuilder {
    private final Condition condition;
    public QueryBuilder(Condition condition) {
        this.condition = condition;
    }

    public String buildSelectQuery() {
        return "Select" + this.condition.getColumn() + " FROM " + this.condition.getTableName() + " WHERE " + this.condition.getConditionOn() + " = " + this.condition.getForValue();
    }

    public String buildUpdateQuery() {
        return "Update" + this.condition.getTableName() + " SET " + this.condition.getColumn() + " = " + this.condition.getForValue() + " WHERE " + this.condition.getConditionOn() + " = " + this.condition.getForValue();
    }
}