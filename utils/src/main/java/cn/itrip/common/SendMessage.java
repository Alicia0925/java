package cn.itrip.common;

import com.cloopen.rest.sdk.CCPRestSmsSDK;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class SendMessage {
    public void sendMessage(String userCode,String activationCode){
        HashMap<String, Object> result = null;
        CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
        restAPI.init("app.cloopen.com", "8883");
        restAPI.setAccount("8aaf07086541761801655a28e4eb0edc", "f8809f8794b2400197a54abdcae0d8dd");
        restAPI.setAppId("8aaf07086541761801655a28e5450ee3");
        result = restAPI.sendTemplateSMS(userCode,"1" ,new String[]{activationCode,"2"});
        if("000000".equals(result.get("statusCode"))){
            HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for(String key:keySet){
                Object object = data.get(key);
                System.out.println(key +" = "+object);
            }
        }else{
            System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
        }
    }
}
