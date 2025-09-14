package com.muhammedturgut.caremate.ui.start.signUpScreens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammedturgut.caremate.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerUseCase: RegisterUseCase): ViewModel(){

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun register(email: String,password: String, userId: String,userName : String = ""){
        _uiState.value = RegisterUiState.Loading

        viewModelScope.launch {
            val result = registerUseCase(email,password,userId)

            _uiState.value = if (result.isSuccess){
                RegisterUiState.Success(userId = userId, email=email, userName = userName )
            }
            else{
               RegisterUiState.Error(result.exceptionOrNull()?.message ?: "UNKNOWN FAIL")
            }
        }
    }
}

sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    data class Success(val userId: String, val userName: String, val email: String) : RegisterUiState()
    data class Error(val message: String): RegisterUiState()
}