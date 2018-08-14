package appjoe.wordpress.com.testdemo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class JsonResponse {
    @SerializedName("worldpopulation")
    @Expose
    internal var worldpopulation: List<worldPopulation>? = null
}
