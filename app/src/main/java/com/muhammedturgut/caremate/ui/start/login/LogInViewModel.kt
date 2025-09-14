package com.muhammedturgut.caremate.ui.start.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammedturgut.caremate.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(private val loginUseCase: LoginUseCase): ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(email: String,password:String){
        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            val result = loginUseCase(email,password)
            _uiState.value = if (result.isSuccess){
                LoginUiState.Success
            }
            else{
                LoginUiState.Error(result.exceptionOrNull()?.message ?: "UNKNOWN FAILURE")
            }
        }
    }
}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String): LoginUiState()
}