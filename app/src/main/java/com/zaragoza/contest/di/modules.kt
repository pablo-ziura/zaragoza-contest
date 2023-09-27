package com.zaragoza.contest.di

import com.google.firebase.auth.FirebaseAuth
import com.zaragoza.contest.data.user.UserRepositoryImpl
import com.zaragoza.contest.data.user.remote.UserRemoteImpl
import com.zaragoza.contest.domain.UserRepository
import com.zaragoza.contest.domain.usecase.CheckUserUseCase
import com.zaragoza.contest.domain.usecase.CreateUserUseCase
import com.zaragoza.contest.domain.usecase.GetUserInfoUseCase
import com.zaragoza.contest.ui.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val userModule = module {
    single { FirebaseAuth.getInstance() }
    factory {
        UserRemoteImpl(get())
    }
    factory<UserRepository> {
        UserRepositoryImpl(get())
    }
    factory { CreateUserUseCase(get()) }
    factory { CheckUserUseCase(get()) }
    factory { GetUserInfoUseCase(get()) }
    viewModel {
        UserViewModel(get(), get(), get())
    }
}