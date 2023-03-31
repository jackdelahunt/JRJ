import com.microsoft.kiota.authentication.BaseBearerTokenAuthenticationProvider;
import com.microsoft.kiota.http.OkHttpRequestAdapter;
import com.openshift.cloud.api.kas.ApiClient;
import com.openshift.cloud.api.kas.models.KafkaRequest;
import com.openshift.cloud.api.kas.models.KafkaRequestPayload;
import com.redhat.cloud.kiota.auth.RHAccessTokenProvider;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Main {

    public static String token = "OFFLINE_TOKEN";

    public static void main(String[] args) throws Exception {

        // setup the client to be used, this is being set to use the
        // production API
        OkHttpRequestAdapter adapter = new OkHttpRequestAdapter(
                new BaseBearerTokenAuthenticationProvider(
                        new RHAccessTokenProvider(token)
                )
        );

        adapter.setBaseUrl("https://api.openshift.com");
        var client = new ApiClient(adapter);


        KafkaRequestPayload payload = new KafkaRequestPayload();
        payload.setName("instance");
        createKafka(client, payload);

        listKafkas(client, "1", "99", "", "");
    }

    private static void createKafka(ApiClient client, KafkaRequestPayload payload) throws Exception {
        var kafka = client
                .api()
                .kafkas_mgmt()
                .v1()
                .kafkas()
                .post(payload, (config) -> {
                    assert config.queryParameters != null;
                    config.queryParameters.async = true;
                })
                .get(3, TimeUnit.SECONDS);

        System.out.printf("Name: %s :: ID %s\n", kafka.getName(), kafka.getId());
    }

    private static void listKafkas(ApiClient client, String page, String size, String orderBy, String search) throws Exception {
        var items = client
                .api()
                .kafkas_mgmt()
                .v1()
                .kafkas()
                .get((config) -> {
                    assert config.queryParameters != null;
                    config.queryParameters.page = page;
                    config.queryParameters.size = size;
                    config.queryParameters.orderBy = orderBy;
                    config.queryParameters.search = search;
                })
                .get(3, TimeUnit.SECONDS)
                .getItems();

        for(var item : items) {
            System.out.printf("Name: %s :: ID %s\n", item.getName(), item.getId());
        }
    }

}