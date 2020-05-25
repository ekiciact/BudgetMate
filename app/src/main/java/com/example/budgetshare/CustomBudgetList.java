package com.example.budgetshare;

import java.util.Date;

public class CustomBudgetList {
        private String firstChar;
        private  String schemaName;
        private String schemaDescription;
        private String schemaDate;


    public CustomBudgetList(String schemaName, String schemaDescription,String schemaDate) {
        this.firstChar = String.valueOf(schemaName.toUpperCase().charAt(0));
        this.schemaName = schemaName;
        this.schemaDescription = schemaDescription;
        this.schemaDate = schemaDate;


    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getSchemaDescription() {
        return schemaDescription;
    }

    public void setSchemaDescription(String schemaDescription) {
        this.schemaDescription = schemaDescription;
    }

    public String getSchemaDate() {
        return schemaDate;
    }

    public void setSchemaDate(String schemaDate) {
        this.schemaDate = schemaDate;
    }
}
