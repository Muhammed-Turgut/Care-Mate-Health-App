package com.muhammedturgut.caremate.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account
import io.appwrite.services.Databases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val account: Account,
    private val databases: Databases
) : ViewModel() {

    private val databasesId = "68c6757e0017e61d7302" //database Id

    private val collectionId = "68c6776c0012ac9a4799" //collect Id

    private val _userState = MutableStateFlow<UserData?>(null)
    val uiState: StateFlow<UserData?> = _userState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn : StateFlow<Boolean> = _isUserLoggedIn

    init {
        checkUserStatusAndLoad()
    }



    private fun checkUserStatusAndLoad(){
        viewModelScope.launch {
            try {
                val user = account.get()
                _isUserLoggedIn.value = true
                loadUserDate()
            }
            catch (e: AppwriteException){
                _isUserLoggedIn.value = false
            }catch (e: Exception){
                _isUserLoggedIn.value = false
            }
        }
    }

    private fun loadUserDate() {
        if (!_isUserLoggedIn.value){
            return
        }

        viewModelScope.launch {
            val user = account.get()
            val doc = databases.getDocument(databasesId,collectionId,user.id)

            _isLoading.value = true

            try {
                val user = account.get()

                try {
                    val doc = databases.getDocument(databasesId,collectionId,user.id)

                    _userState.value = UserData(
                        id = doc.id,
                        userName = doc.data["name"] as String,
                        gender = doc.data["gender"] as String,
                        dataofBirthday = doc.data["dataofBirthday"] as String,
                        profileImage = doc.data["profileImage"] as String

                    )
                }
                catch (e: AppwriteException){
                    if (e.code == 404){
                        createUserData(user.id, user.name ?: "Kullanıcı", user.email ?: "email@example.com")
                    }
                    else{

                    }
                }catch (e: Exception){

                }

            }
            catch (e: AppwriteException){

            }
            catch (e: Exception){

            }finally {
                _isLoading.value = false
            }
        }
    }

    fun createUserData(userId:String, userName:String, email: String){
        viewModelScope.launch {
            try {
                val userData = mapOf(
                    "name" to userName,
                    "gender" to "",
                    "dataofBirthday" to "",
                    "profileImage" to ""

                )

                val document = databases.createDocument(
                    databasesId,
                    collectionId,
                    userId,
                    userData
                )
            }catch (e: AppwriteException){

            }catch (e: Exception){

            }
        }
    }

    fun updateUserDate(
        userName: String?= null,
        profileImage: String? = null,
        gender: String? = null,
        dataOfBirthday: String?= null
    ){
        viewModelScope.launch {
            val currentUser = _userState.value ?: return@launch
            val updateData = mutableMapOf<String, Any>()

            userName?.let { updateData["name"] = it }
            profileImage?.let { updateData["profileImage"] = it }
            gender?.let { updateData["gender"] = it }
            dataOfBirthday?.let { updateData["dataofBirthday"] = it }

            if(updateData.isNotEmpty()) return@launch

            try {
                databases.updateDocument(
                    databasesId,
                    collectionId,
                    currentUser.id,
                    updateData
                )

                loadUserDate()
            }catch (e: AppwriteException){

            }catch (e: Exception){

            }

        }
    }

}

data class UserData(
    val id: String,
    val userName: String,
    val gender : String?,
    val dataofBirthday : String,
    val profileImage : String
)