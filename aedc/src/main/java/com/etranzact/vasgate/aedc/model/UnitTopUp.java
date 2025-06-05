package com.etranzact.vasgate.aedc.model;

public class UnitTopUp {

    private String concept;
    private double units;
    private double  price;
    private double  amount;
    private String conceptName;

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public double getUnits() {
        return units;
    }

    public void setUnits(double units) {
        this.units = units;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getConceptName() {
        return conceptName;
    }

    public void setConceptName(String conceptName) {
        this.conceptName = conceptName;
    }

    @Override
    public String toString() {
        return "UnitTopUp{" + "concept=" + concept + ", units=" + units + ", price=" + price + ", amount=" + amount + '}';
    }



}
