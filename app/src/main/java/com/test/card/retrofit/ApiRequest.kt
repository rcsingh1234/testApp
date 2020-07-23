package com.test.card.retrofit
import com.test.card.model.PersonsList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiRequest {

    @GET("/api/?")
    fun getPersonsList(
        @Query("results") results: Int
    ): Call<PersonsList>
}