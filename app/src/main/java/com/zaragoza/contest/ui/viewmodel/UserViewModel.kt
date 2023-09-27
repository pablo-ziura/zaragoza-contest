package com.zaragoza.contest.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaragoza.contest.domain.model.User
import com.zaragoza.contest.domain.usecase.CheckUserUseCase
import com.zaragoza.contest.domain.usecase.CreateUserUseCase
import com.zaragoza.contest.domain.usecase.GetUserInfoUseCase
import com.zaragoza.contest.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

typealias CreateUserState = ResourceState<Void?>
typealias CheckUserState = ResourceState<Void?>
typealias GetUserInfoState = ResourceState<User>

class UserViewModel(
    private val createUserUseCase: CreateUserUseCase,
    private val checkUserUseCase: CheckUserUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
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
            } catch (e: Exception) {
                _createUserLiveData.value = ResourceState.Error(e.localizedMessage.orEmpty())
                _createUserLiveData.value = ResourceState.None()
            }
        }
    }

    fun checkUser(userEmail: String, userPassword: String) {

        _checkUserLiveData.value = ResourceState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                checkUserUseCase.execute(userEmail, userPassword)
                withContext(Dispatchers.Main) {
                    _checkUserLiveData.value = ResourceState.Success(null)
                    _checkUserLiveData.value = ResourceState.None()
                }
            } catch (e: Exception) {
                _checkUserLiveData.value = ResourceState.Error(e.localizedMessage.orEmpty())
                _checkUserLiveData.value = ResourceState.None()
            }
        }
    }

    fun getUserInfo(userId: String) {

        _getUserInfoLiveData.value = ResourceState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = getUserInfoUseCase.execute(userId)

                withContext(Dispatchers.Main) {
                    _getUserInfoLiveData.value = ResourceState.Success(user)
                    _getUserInfoLiveData.value = ResourceState.None()
                }
            } catch (e: Exception) {
                _getUserInfoLiveData.value = ResourceState.Error(e.localizedMessage.orEmpty())
                _getUserInfoLiveData.value = ResourceState.None()
            }
        }
    }

}