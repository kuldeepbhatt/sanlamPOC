package com.sanlam.SanlamPOC.utility;

public class Condition {
    private String column;
    private String conditionOn;
    private String forValue;
    private String tableName;

    public Condition(String column, String conditionOn, String forValue, String tableName) {
        this.column = column;
        this.conditionOn = conditionOn;
        this.forValue = forValue;
        this.tableName = tableName;
    }

//    public Condition() {
//
//    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getConditionOn() {
        return conditionOn;
    }

    public void setConditionOn(String conditionOn) {
        this.conditionOn = conditionOn;
    }

    public String getForValue() {
        return this.forValue;
    }

    public void setForValue(String forValue) {
        this.forValue = forValue;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}