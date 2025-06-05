package com.etranzact.vasgate.electricity.ekoedc.Domain.Dto;

public class SearchCriterion {
    private String codType;
    private String description;
    private int defaults;

    public String getCodType() {
        return codType;
    }

    public SearchCriterion setCodType(String codType) {
        this.codType = codType;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public SearchCriterion setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getDefaults() {
        return defaults;
    }

    public SearchCriterion setDefaults(int defaults) {
        this.defaults = defaults;
        return this;
    }

    @Override
    public String toString() {
        return "SearchCriterion{" +
                "codType='" + codType + '\'' +
                ", description='" + description + '\'' +
                ", defaults='" + defaults + '\'' +
                '}';
    }

}
