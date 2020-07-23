package com.test.card.retrofit
import com.test.card.utils.AppConstants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitModule {

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun createApiRequest(retrofit: Retrofit): ApiRequest {
        return retrofit.create(ApiRequest::class.java)
    }
}