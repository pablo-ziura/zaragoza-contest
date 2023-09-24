package com.zaragoza.contest.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaragoza.contest.domain.usecase.CreateUserUseCase
import com.zaragoza.contest.model.ResourceState
import com.zaragoza.contest.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

typealias CreateUserState = ResourceState<Void?>

class UserViewModel(
    private val createUserUseCase: CreateUserUseCase
) : ViewModel() {

    private val _createUserLiveData = MutableLiveData<CreateUserState>()
    val createUserLiveData: LiveData<CreateUserState> get() = _createUserLiveData

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

}