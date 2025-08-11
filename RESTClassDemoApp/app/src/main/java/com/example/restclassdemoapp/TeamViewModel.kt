package com.example.restclassdemoapp

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TeamViewModel : ViewModel() {
    private val _teamList = mutableStateListOf<Team>()
    val teamList: List<Team> get() = _teamList

    fun getTeams() {
        viewModelScope.launch { // coroutine to call getTeams
            val teamService = TeamService.getInstance()
            try {
                Log.wtf("VM", "Instance get all teams")
                _teamList.clear() // empty the list
                _teamList.addAll(teamService.getAllTeams()) // get the teams and add to list
            } catch (e: Exception) {
                Log.wtf("Service", "problem: ${e}")
            }
        }
    }

    fun addTeam(team: Team) {
        Log.wtf("addTeam", "vm calling service")
        viewModelScope.launch {
            val teamService = TeamService.getInstance()
            try {
                teamService.insertTeam(team) // adds the team to the REST service
                _teamList.clear() // update the data that the client
                _teamList.addAll(teamService.getAllTeams()) // observable is 'watching'
            } catch (e: Exception) {
                Log.wtf("Service", "problem: ${e}")
            }
        }
    }
}