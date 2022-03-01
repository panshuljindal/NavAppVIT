package edu.vit.vtop.navapp.NetworkUtils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtil {

        public static String baseURL = "https://mapappapi.herokuapp.com/";
        public static OkHttpClient getClientInstance() {
            OkHttpClient client = new OkHttpClient.Builder().build();
            return client;
        }
        public static Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(getClientInstance())
                .addConverterFactory(GsonConverterFactory.create()).build();

        public static NetworkAPIs networkAPI = retrofit.create(NetworkAPIs.class);

}
