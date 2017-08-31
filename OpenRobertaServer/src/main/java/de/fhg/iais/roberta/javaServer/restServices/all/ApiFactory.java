package de.fhg.iais.roberta.javaServer.restServices.all;

import de.fhg.iais.roberta.util.RobertaProperties;
import io.swagger.client.stemweb.ApiClient;
import io.swagger.client.stemweb.api.UserApi;

/**
 * Created by tiantianwang on 2017/8/31.
 */
public class ApiFactory {
    public static ApiFactory factory = new ApiFactory();

    private ApiFactory(){

    }

    public static ApiFactory getInstance(){
        return factory;
    }

    public UserApi getUserApi(){
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(RobertaProperties.getStringProperty("stemweb.service_url"));
        UserApi u = new UserApi();
        u.setApiClient(apiClient);
        return u;
    }

    public UserApi getUserApi(String token){
        UserApi u = getUserApi();
        u.getApiClient().setApiKey(token);
        return u;
    }
}
