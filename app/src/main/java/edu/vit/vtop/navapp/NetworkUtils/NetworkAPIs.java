package edu.vit.vtop.navapp.NetworkUtils;

import java.util.List;

import edu.vit.vtop.navapp.Utils.DataModel;
import edu.vit.vtop.navapp.Utils.VersionModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkAPIs {

    @GET("api/getIndependent")
    Call<List<DataModel>> getIndependent();

    @GET("api/search")
    Call<List<VersionModel>> getVersion(@Query("sk") String sj);

    @GET("api/getByCategory/{id}")
    Call<List<DataModel>> getCategory(@Path("id") String category);
}
