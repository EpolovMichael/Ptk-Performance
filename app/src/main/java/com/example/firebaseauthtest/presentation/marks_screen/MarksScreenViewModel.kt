package com.example.firebaseauthtest.presentation.marks_screen

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseauthtest.data.datastore.StoreUserData
import com.example.firebaseauthtest.data.local.Mark
import com.example.firebaseauthtest.data.local.MarksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class MarksScreenViewModel @Inject constructor(
    private val roomRepository: MarksRepository
) : ViewModel() {

    private val _marks = MutableStateFlow<List<Mark>>(emptyList())
    val marks: StateFlow<List<Mark>> = _marks.asStateFlow()

    init {
        viewModelScope.launch {
            roomRepository.allMarks.collect{ listOfMarks ->
                _marks.value = listOfMarks
            }
        }
        Log.d("MyTag", "7777777777777777777777 ${marks.value}")
    }

    @Composable
    fun getUserRole(context: Context) =
        StoreUserData(context).getUserRole().collectAsState(initial = "")
}