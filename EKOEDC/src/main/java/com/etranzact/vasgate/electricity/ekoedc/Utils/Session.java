package com.etranzact.vasgate.electricity.ekoedc.Utils;

import com.etranzact.vasgate.electricity.ekoedc.Domain.Dto.SearchCriterion;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Session {

    private static List<SearchCriterion> searchCriteria;

    public List<SearchCriterion> getSearchCriteria() {
        if (searchCriteria == null) {
          String criteria = "[{\"codType\":\"MY003\",\"description\":\"Search by meter number(mr)\",\"default\":1},{\"codType\":\"MY001\",\"description\":\"Search by AccountNumber(ac)\",\"default\":0},{\"codType\":\"MY002\",\"description\":\"Search by customer identification(cu)\",\"default\":0},{\"codType\":\"MY004\",\"description\":\"Search by Driver Licence(dl)\",\"default\":0},{\"codType\":\"MY005\",\"description\":\"Search by Old Account(oa)\",\"default\":0}]";
            SearchCriterion[] searchCriterionList = new Gson().fromJson(criteria, SearchCriterion[].class);
           // SearchCriteria search = new SearchCriteria();

            ArrayList<SearchCriterion> list = new ArrayList<>();
            Collections.addAll(list, searchCriterionList);

            searchCriteria = list;
        }
        return searchCriteria;
    }

}
