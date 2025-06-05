package com.etranzact.vasgate.aedc.dto;

public class SearchCriteriaKey {
    private String key;
    private String state;
    private int index;

    public String getKey() {
        return key;
    }

    public SearchCriteriaKey setKey(String key) {
        this.key = key;
        return this;
    }

    public String getState() {
        return state;
    }

    public SearchCriteriaKey setState(String state) {
        this.state = state;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public SearchCriteriaKey setIndex(int index) {
        this.index = index;
        return this;
    }

    @Override
    public String toString() {
        return "SearchCriteriaKey{" +
                "key='" + key + '\'' +
                ", state='" + state + '\'' +
                ", index=" + index +
                '}';
    }
}
