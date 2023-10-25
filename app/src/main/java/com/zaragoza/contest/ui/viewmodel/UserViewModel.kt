package com.zaragoza.contest.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.zaragoza.contest.domain.usecase.user.CheckUserUseCase
import com.zaragoza.contest.domain.usecase.user.CreateUserUseCase
import com.zaragoza.contest.domain.usecase.user.EditUserUseCase
import com.zaragoza.contest.domain.usecase.user.FetchUserIdUseCase
import com.zaragoza.contest.domain.usecase.user.GetUserInfoUseCase
import com.zaragoza.contest.domain.usecase.user.SaveUserIdUseCase
import com.zaragoza.contest.domain.usecase.user.UploadProfileImageUseCase
import com.zaragoza.contest.model.User
import com.zaragoza.contest.ui.common.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

typealias CreateUserState = ResourceState<Void?>
typealias CheckUserState = ResourceState<String?>
typealias GetUserInfoState = ResourceState<User>

class UserViewModel(
    private val createUserUseCase: CreateUserUseCase,
    private val checkUserUseCase: CheckUserUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val editUserUseCase: EditUserUseCase,
    private val saveUserIdUseCase: SaveUserIdUseCase,
    private val fetchUserIdUseCase: FetchUserIdUseCase,
    private val uploadProfileImageUseCase: UploadProfileImageUseCase
) : ViewModel() {

    private val _createUserLiveData = MutableLiveData<CreateUserState>()
    val createUserLiveData: LiveData<CreateUserState> get() = _createUserLiveData

    private val _checkUserLiveData = MutableLiveData<CheckUserState>()
    val checkUserLiveData: LiveData<CheckUserState> get() = _checkUserLiveData

    private val _getUserInfoLiveData = MutableLiveData<GetUserInfoState>()
    val getUserInfoLiveData: LiveData<GetUserInfoState> get() = _getUserInfoLiveData

    fun createUser(user: User) {
        _createUserLiveData.value = ResourceState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                createUserUseCase.execute(user)
                withContext(Dispatchers.Main) {
                    _createUserLiveData.value = ResourceState.Success(null)
                    _createUserLiveData.value = ResourceState.None()
                }
            } catch (e: FirebaseAuthUserCollisionException) {
                withContext(Dispatchers.Main) {
                    _createUserLiveData.value =
                        ResourceState.Error("El correo electrónico ya está en uso")
                    _createUserLiveData.value = ResourceState.None()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _createUserLiveData.value = ResourceState.Error(e.localizedMessage.orEmpty())
                    _createUserLiveData.value = ResourceState.None()
                }
            }
        }
    }

    fun checkUser(userEmail: String, userPassword: String) {

        _checkUserLiveData.value = ResourceState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userId = checkUserUseCase.execute(userEmail, userPassword)
                withContext(Dispatchers.Main) {
                    if (userId != null) {
                        _checkUserLiveData.value = ResourceState.Success(userId)
                    } else {
                        _checkUserLiveData.value =
                            ResourceState.Error("Usuario o contraseña incorrectos")
                    }
                    _checkUserLiveData.value = ResourceState.None()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _checkUserLiveData.value = ResourceState.Error(e.localizedMessage.orEmpty())
                    _checkUserLiveData.value = ResourceState.None()
                }
            }
        }
    }

    fun getUserInfo(userId: String) {

        _getUserInfoLiveData.value = ResourceState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = getUserInfoUseCase.execute(userId)

                withContext(Dispatchers.Main) {
                    _getUserInfoLiveData.value = ResourceState.Success(user!!)
                    _getUserInfoLiveData.value = ResourceState.None()
                }
            } catch (e: Exception) {
                _getUserInfoLiveData.value = ResourceState.Error(e.localizedMessage.orEmpty())
                _getUserInfoLiveData.value = ResourceState.None()
            }
        }
    }

    fun editUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                editUserUseCase.execute(user)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun uploadProfileImage(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                uploadProfileImageUseCase.execute(user)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun saveUserId(userId: String): ResourceState<Void?> {
        return try {
            saveUserIdUseCase.execute(userId)
            ResourceState.Success(null)
        } catch (e: Exception) {
            ResourceState.Error(e.localizedMessage.orEmpty())
        }
    }

    fun fetchUserId(): String? {
        return fetchUserIdUseCase.execute()
    }

}