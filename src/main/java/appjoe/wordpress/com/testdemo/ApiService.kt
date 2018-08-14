package appjoe.wordpress.com.testdemo


import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    //    @GET("tutorial/jsonparsetutorial.txt")
    //    void getPopulationData(Callback<JsonResponse> callback) ;

    @get:GET("tutorial/jsonparsetutorial.txt")
    val populationData: Call<JsonResponse>

    companion object {
        val BASE_URL = "http://www.androidbegin.com/"
    }
}
