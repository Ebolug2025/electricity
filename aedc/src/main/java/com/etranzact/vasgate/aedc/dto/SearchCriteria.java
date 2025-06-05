package com.etranzact.vasgate.aedc.dto;



import java.util.List;

public class SearchCriteria {
    private List<SearchCriterion> searchCriteria;

    public List<SearchCriterion> getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(List<SearchCriterion> searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public String toString() {
        return "SearchCriteria{" +
                "searchCriteria=" + searchCriteria +
                '}';
    }
}
