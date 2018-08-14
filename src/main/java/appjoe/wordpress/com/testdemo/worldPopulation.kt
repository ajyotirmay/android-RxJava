package appjoe.wordpress.com.testdemo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal class worldPopulation {
    @SerializedName("rank")
    @Expose
    var rank: Int? = null
    @SerializedName("country")
    @Expose
    var country: String? = null
    @SerializedName("population")
    @Expose
    var population: String? = null
    @SerializedName("flag")
    @Expose
    var flag: String? = null
}
