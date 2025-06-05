package com.etranzact.vasgate.aedc.util;

import com.etranzact.vasgate.aedc.dto.SearchCriterion;
import com.etranzact.vasgate.aedc.dto.SearchCriteria;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Olugbenga.Falodun
 */
public class Session {
    private static Map<String, String> customers;
    private static Map<String, String> paymentReferences;
    private static List<SearchCriterion> searchCriteria;

    public Map<String, String> getCustomers() {
        return customers;
    }

    public void updateCustomers(String key, String value) {
        if (customers == null) {
            customers = new HashMap<String, String>();
        }
        customers.put(key, value);
    }

    public Map<String, String> getPaymentReference() {
        return paymentReferences;
    }

    public void updatePaymentReference(String key, String value) {
        if (paymentReferences == null) {
            paymentReferences = new HashMap<String, String>();
        }
        paymentReferences.put(key, value);
    }

    public List<SearchCriterion> getSearchCriteria() {

        if (searchCriteria == null) {
            String criteria = "[{\"codType\":\"MY003\",\"description\":\"Search by meter number(mr)\",\"default\":1},{\"codType\":\"MY001\",\"description\":\"Search by AccountNumber(ac)\",\"default\":0},{\"codType\":\"MY002\",\"description\":\"Search by customer identification(cu)\",\"default\":0},{\"codType\":\"MY004\",\"description\":\"Search by Driver Licence(dl)\",\"default\":0},{\"codType\":\"MY005\",\"description\":\"Search by Old Account(oa)\",\"default\":0}]";
            SearchCriterion[] searchCriterionList = new Gson().fromJson(criteria, SearchCriterion[].class);
            SearchCriteria search = new SearchCriteria();

            ArrayList<SearchCriterion> list = new ArrayList<>();
            for (SearchCriterion criterion : searchCriterionList) {
                list.add(criterion);
            }

            searchCriteria = list;
        }
        return  searchCriteria;
    }

}

