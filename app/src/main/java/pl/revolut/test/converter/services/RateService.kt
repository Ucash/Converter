package pl.revolut.test.converter.services

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RateService {
    @GET("latest")
    fun getRatesForCurrency(@Query("base") currency: String): Call<RateDto>

    companion object {
        val instance : RateService by lazy {
            Retrofit.Builder()
                .baseUrl("https://revolut.duckdns.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RateService::class.java)
        }
    }
}
