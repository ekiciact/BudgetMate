package com.example.budgetshare;

public class CustomItemList {
    private String itemName, itemDescription, itemDate, itemAmount;

    public CustomItemList(String item_name, String item_description, String item_date, String item_amount) {

        this.itemName = item_name;
        this.itemDescription = item_description;
        this.itemDate = item_date;
        this.itemAmount = item_amount;

    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemDate() {
        return itemDate;
    }

    public void setItemDate(String itemDate) {
        this.itemDate = itemDate;
    }

    public String getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(String itemAmount) {
        this.itemAmount = itemAmount;
    }


}