package edu.vit.vtop.navapp.NetworkUtils;

import java.util.List;

import edu.vit.vtop.navapp.Utils.DataModel;
import edu.vit.vtop.navapp.Utils.VersionModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkAPIs {

    @GET("api/getIndependent")
    @Headers({"auth-token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2NDYzMjg1NDN9.eoQ2xDM6InHlCyUtNeWvUD4YnqT7_8Df-LPWUqEmv6E"})
    Call<List<DataModel>> getIndependent();

    @GET("api/version")
    @Headers({"auth-token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2NDYzMjg1NDN9.eoQ2xDM6InHlCyUtNeWvUD4YnqT7_8Df-LPWUqEmv6E"})
    Call<VersionModel> getVersion();

    @GET("api/getByCategory/{id}")
    @Headers({"auth-token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2NDYzMjg1NDN9.eoQ2xDM6InHlCyUtNeWvUD4YnqT7_8Df-LPWUqEmv6E"})
    Call<List<DataModel>> getCategory(@Path("id") String category);

    @GET("api/search")
    @Headers({"auth-token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2NDYzMjg1NDN9.eoQ2xDM6InHlCyUtNeWvUD4YnqT7_8Df-LPWUqEmv6E"})
    Call<List<DataModel>> search(@Query("sk") String key);
}
