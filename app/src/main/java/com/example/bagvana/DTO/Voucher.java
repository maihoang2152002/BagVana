package com.example.bagvana.DTO;

public class Voucher {
    private String id;
    private String name;
    private int type;
    private String start;
    private String end;
    private int maxValueDiscount;
    private int minValueOfItem;
    private int range;
    private int amount;
    private int amountOnPerson;

    public Voucher() {}

    public Voucher(String id, String name, int type, String start, String end, int maxValueDiscount, int minValueOfItem, int range, int amount, int amountOnPerson) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.start = start;
        this.end = end;
        this.maxValueDiscount = maxValueDiscount;
        this.minValueOfItem = minValueOfItem;
        this.range = range;
        this.amount = amount;
        this.amountOnPerson = amountOnPerson;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getMaxValueDiscount() {
        return maxValueDiscount;
    }

    public void setMaxValueDiscount(int maxValueDiscount) {
        this.maxValueDiscount = maxValueDiscount;
    }

    public int getMinValueOfItem() {
        return minValueOfItem;
    }

    public void setMinValueOfItem(int minValueOfItem) {
        this.minValueOfItem = minValueOfItem;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmountOnPerson() {
        return amountOnPerson;
    }

    public void setAmountOnPerson(int amountOnPerson) {
        this.amountOnPerson = amountOnPerson;
    }
}
