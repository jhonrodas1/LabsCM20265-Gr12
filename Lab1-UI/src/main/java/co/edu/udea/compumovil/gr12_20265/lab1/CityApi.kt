package co.edu.udea.compumovil.gr12_20265.lab1

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class City(
    val id: Int,
    val name: String,
    val departmentId: Int? = null
)

interface CityService {
    @GET("City")
    suspend fun getCities(): List<City>
}

object RetrofitClient {
    private const val BASE_URL = "https://api-colombia.com/api/v1/"

    val cityService: CityService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CityService::class.java)
    }
}
