package com.mymonas.ngobrol.io;

import com.mymonas.ngobrol.Config;
import com.mymonas.ngobrol.util.Clog;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Huteri on 10/16/2014.
 */
public class RestClient {

    private static Api REST_CLIENT;

    static {
        setupRestClient();
    }

    public RestClient() {
    }

    public static Api get() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(Config.SERVER_NAME)
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.NONE);

        if(Clog.isDebugable())
            builder.setLogLevel(RestAdapter.LogLevel.FULL);

        RestAdapter restAdapter = builder.build();

        REST_CLIENT = restAdapter.create(Api.class);
    }

}
