import com.openshift.cloud.api.kas.DefaultApi;
import com.openshift.cloud.api.kas.invoker.ApiClient;
import com.openshift.cloud.api.kas.invoker.ApiException;
import com.openshift.cloud.api.kas.invoker.Configuration;
import com.openshift.cloud.api.kas.invoker.auth.HttpBearerAuth;
import com.openshift.cloud.api.kas.models.KafkaRequest;
import com.openshift.cloud.api.kas.models.KafkaRequestPayload;

public class Main {

    public static String token = "ACCESS_TOKEN";
    public static void main(String[] args) {

        // setup the client to be used, this is being set to use the
        // production API
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://api.openshift.com");

        // set the token to be used, this needs to be the access token
        // for the account you want to use
        HttpBearerAuth Bearer = (HttpBearerAuth) defaultClient.getAuthentication("Bearer");
        Bearer.setBearerToken(token);

        DefaultApi apiInstance = new DefaultApi(defaultClient);

        KafkaRequestPayload kafkaRequestPayload = new KafkaRequestPayload().name("instance");
        createKafka(apiInstance, kafkaRequestPayload);

        listKafkas(apiInstance, "1", "99", "", "");
    }

    public static void createKafka(DefaultApi apiInstance, KafkaRequestPayload payload) {
        try {
            KafkaRequest result = apiInstance.createKafka(true, payload);
            System.out.println(result);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    public static void listKafkas(DefaultApi apiInstance, String page, String size, String orderBy, String search) {
        try {
            var result = apiInstance.getKafkas(page, size, orderBy, search);
            for(var item : result.getItems()) {
                System.out.printf("Name %s :: id %s%n", item.getName(), item.getId());
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
