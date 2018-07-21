package appjoe.wordpress.com.testdemo;


import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    String BASE_URL = "http://www.androidbegin.com/";

//    @GET("tutorial/jsonparsetutorial.txt")
//    void getPopulationData(Callback<JsonResponse> callback) ;

    @GET("tutorial/jsonparsetutorial.txt")
    Call<JsonResponse> getPopulationData();
}
