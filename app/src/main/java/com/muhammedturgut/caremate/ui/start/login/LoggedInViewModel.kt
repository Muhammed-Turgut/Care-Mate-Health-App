package com.muhammedturgut.caremate.ui.start.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammedturgut.caremate.domain.usecase.LoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoggedInViewModel @Inject constructor(private val loggdInUseCase: LoggedInUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginCheckUiState>(LoginCheckUiState.Loading)
    val uiState: StateFlow<LoginCheckUiState> = _uiState.asStateFlow()

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus(){
        viewModelScope.launch {
            try {
                val isLoggedIn = loggdInUseCase()
                _uiState.value = LoginCheckUiState.Success(isLoggedIn)
            }
            catch (e: Exception){
                _uiState.value = LoginCheckUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}


sealed class LoginCheckUiState {
    object Loading : LoginCheckUiState()
    data class Success(val isLoggedIn: Boolean) : LoginCheckUiState()
    data class Error(val message: String) : LoginCheckUiState()
}