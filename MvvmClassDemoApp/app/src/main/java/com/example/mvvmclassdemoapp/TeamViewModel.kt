package com.example.mvvmclassdemoapp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mymvvmapp.data.Team

class TeamViewModel : ViewModel() {
    // this is the observable that stores the state
    var teamListState: MutableState<List<Team>> = mutableStateOf(emptyList())
    fun addTeam( name :String, city : String ) {
        teamListState.value = teamListState.value + listOf<Team>( Team(name,city) )
    }
}
