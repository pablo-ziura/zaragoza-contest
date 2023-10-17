package com.zaragoza.contest.di

import com.google.firebase.auth.FirebaseAuth
import com.zaragoza.contest.data.question.QuestionDataRepository
import com.zaragoza.contest.data.question.remote.QuestionRemoteImpl
import com.zaragoza.contest.data.user.UserDataRepository
import com.zaragoza.contest.data.user.remote.UserRemoteImpl
import com.zaragoza.contest.data.user.storage.UserPreferencesManager
import com.zaragoza.contest.domain.QuestionRepository
import com.zaragoza.contest.domain.UserPreferences
import com.zaragoza.contest.domain.UserRepository
import com.zaragoza.contest.domain.usecase.question.GetQuestionListUseCase
import com.zaragoza.contest.domain.usecase.user.CheckUserUseCase
import com.zaragoza.contest.domain.usecase.user.CreateUserUseCase
import com.zaragoza.contest.domain.usecase.user.FetchUserIdUseCase
import com.zaragoza.contest.domain.usecase.user.GetUserInfoUseCase
import com.zaragoza.contest.domain.usecase.user.SaveUserIdUseCase
import com.zaragoza.contest.ui.viewmodel.QuestionViewModel
import com.zaragoza.contest.ui.viewmodel.UserViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val userModule = module {

    single { FirebaseAuth.getInstance() }

    factory {
        UserRemoteImpl(get())
    }

    single<UserPreferences> {
        UserPreferencesManager(androidContext())
    }

    factory<UserRepository> {
        UserDataRepository(get(), get())
    }

    factory { CreateUserUseCase(get()) }
    factory { CheckUserUseCase(get()) }
    factory { GetUserInfoUseCase(get()) }
    factory { SaveUserIdUseCase(get()) }
    factory { FetchUserIdUseCase(get()) }

    viewModel {
        UserViewModel(get(), get(), get(), get(), get())
    }
}


val questionModule = module {

    factory {
        QuestionRemoteImpl()
    }

    factory<QuestionRepository> {
        QuestionDataRepository(get())
    }

    factory { GetQuestionListUseCase(get()) }

    viewModel {
        QuestionViewModel(get())
    }
}