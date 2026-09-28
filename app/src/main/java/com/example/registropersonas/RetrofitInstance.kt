package com.example.registropersonas

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance{
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://6ab5c3ce24ee9d3caa1c9b66.mockapi.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: PersonasApi = retrofit.create(PersonasApi::class.java)
}

