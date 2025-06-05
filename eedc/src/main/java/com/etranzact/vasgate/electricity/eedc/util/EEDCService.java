package com.etranzact.vasgate.electricity.eedc.util;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.net.SocketTimeoutException;
import java.util.Date;
@Slf4j
@Component
public class EEDCService {

    private Date validity;

    @Value("${ENU_API_KEY}")
    private String apikey;

    @Value("${ENU_BASEURL}")
    private String baseUrl;

    @Value("${ENU_ORIGIN}")
    private String origin;


    public EEDCService(String apikey, String baseUrl,String origin) {
        this.apikey = apikey;
        this.baseUrl = baseUrl;
        this.origin = origin;
    }

//

    public EEDCService() {
//        username = "09cc4f8d76f2_demo";
//            password = "bCG#v4*Mj0*TZb9_g7uW^";
//            baseUrl = "http://api.kvg.com.ng/";
//            hash = "0192572A2678DA5ADC5CD8B9B";
    }


//    private void getAccessCode() throws Exception{
//        String req = "{\"username\":\""+username+"\",\"password\":\""+password+"\"}";
//        if(accessCode == null || validity == null || validity.compareTo(new Date())<=0){
//            log.info("GETTING ACCESS CODE");
//            String resp = HttpUtil.sendPost(auth_url, req);
//            JsonParser parser = new JsonParser();
//            JsonObject obj = parser.parse(resp).getAsJsonObject();
//            accessCode = obj.get("accessCode").getAsString();
//            String valid = obj.get("validUntil").getAsString();
//            SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
//            validity = sdf.parse(valid);
//            log.info("SUCCESSFULLY GOT ACCESS CODE");
//        }
//    }

    public String[]validateMeter(String meter,String paymentPlan) throws Exception{
        //getAccessCode();
        String request =baseUrl+"customer/detail?accountNumber="+meter+"&paymentPlan="+paymentPlan;
        log.info("CALLING ENUGU VENDING GATEWAY FOR METER VALIDATION ");
        log.info("CALLED URL :: "+request);
        log.info(apikey);
        String[]resp = HttpUtil.sendGet(request, apikey,origin);
        log.info("RESPONSE FROM ENUGU VENDING GATEWAY :: "+resp[1]);
//        JsonParser parser = new JsonParser();
//        JsonObject obj = parser.parse(resp).getAsJsonObject();

        return resp;
    }

    public String[]requery(String unique_transid) throws Exception{
        //getAccessCode();
        String request = baseUrl+"payment/detail?transactionRef="+unique_transid;
        log.info("CALLING ENUGU VENDING GATEWAY FOR REQUERY");
        log.info("CALLED URL :: "+request);
        String[]resp = HttpUtil.sendGet(request, apikey,origin);
        log.info("RESPONSE FROM ENUGU VENDING GATEWAY :: "+resp[1]);
//        JsonParser parser = new JsonParser();
//        JsonObject obj = parser.parse(resp).getAsJsonObject();
        return resp;
    }

    public String[]reversal(String unique_transid) throws Exception{
        //getAccessCode();https://dev.myeedc.com/cashcollection/business/api/payment/reverse
        String url = baseUrl+"payment/reverse";
        String request = "{\"transactionRef\":\""+unique_transid+"\"}";
        log.info("CALLING ENUGU VENDING GATEWAY FOR TOKEN VENDING");
        log.info("CALLED URL :: "+request);
        String[]resp = HttpUtil.sendPost(url, request, apikey,origin,8);
        log.info("RESPONSE FROM ENUGU VENDING GATEWAY :: "+resp[1]);
//        JsonParser parser = new JsonParser();
//        JsonObject obj = parser.parse(resp).getAsJsonObject();
        return resp;
    }

    public String[] vendPin(JsonObject request, int timeout) throws SocketTimeoutException, Exception{
        //getAccessCode();
        String url = baseUrl+"payment/pay";
        log.info("CALLING ENUGU VENDING GATEWAY FOR TOKEN VENDING");
        log.info("CALLED URL :: "+url);
        String[]resp = HttpUtil.sendPost(url, request.toString(), apikey,origin, timeout);
        log.info("RESPONSE FROM ENUGU VENDING GATEWAY :: "+resp[1]);
//        JsonParser parser = new JsonParser();
//        JsonObject obj = parser.parse(resp).getAsJsonObject();
        return resp;
    }
}
