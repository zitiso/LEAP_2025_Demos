package com.example.restclassdemoapp

import android.util.Log
//import retrofit.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// const val BASE_URL = "YOUR SERVICE URI"
// In the emulator 'localhost' is the emulator so point to the host machine where the service is running
const val BASE_URL = "http://10.0.2.2:8000/"

interface TeamService {
    @GET("teams")
    suspend fun getAllTeams(): List<Team>

    @POST("teams")
    suspend fun insertTeam(@Body team: Team)

    companion object {
        var teamService: TeamService? = null
        fun getInstance(): TeamService {
            Log.wtf("Service", "Instance getting connected")
            if (teamService == null) {
                teamService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(TeamService::class.java)
            }
            return teamService!!
        }

    }
}