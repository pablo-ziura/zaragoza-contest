package com.zaragoza.contest.di

import com.google.firebase.auth.FirebaseAuth
import com.zaragoza.contest.data.question.QuestionDataRepository
import com.zaragoza.contest.data.question.remote.QuestionRemoteImpl
import com.zaragoza.contest.data.score.ScoreDataRepository
import com.zaragoza.contest.data.score.remote.ScoreRemoteImpl
import com.zaragoza.contest.data.score.storage.ScorePreferencesManager
import com.zaragoza.contest.data.user.UserDataRepository
import com.zaragoza.contest.data.user.remote.UserRemoteImpl
import com.zaragoza.contest.data.user.storage.UserPreferencesManager
import com.zaragoza.contest.domain.QuestionRepository
import com.zaragoza.contest.domain.ScoreRepository
import com.zaragoza.contest.domain.UserRepository
import com.zaragoza.contest.domain.usecase.question.GetQuestionListUseCase
import com.zaragoza.contest.domain.usecase.score.FetchCurrentScoreUseCase
import com.zaragoza.contest.domain.usecase.score.GetBestScoresUseCase
import com.zaragoza.contest.domain.usecase.score.UpdateCurrentUserScoreUseCase
import com.zaragoza.contest.domain.usecase.user.CheckUserUseCase
import com.zaragoza.contest.domain.usecase.user.CreateUserUseCase
import com.zaragoza.contest.domain.usecase.user.EditUserUseCase
import com.zaragoza.contest.domain.usecase.user.FetchUserIdUseCase
import com.zaragoza.contest.domain.usecase.user.GetUserInfoUseCase
import com.zaragoza.contest.domain.usecase.user.SaveUserIdUseCase
import com.zaragoza.contest.ui.viewmodel.QuestionViewModel
import com.zaragoza.contest.ui.viewmodel.ScoreViewModel
import com.zaragoza.contest.ui.viewmodel.UserViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val userModule = module {

    single { FirebaseAuth.getInstance() }

    factory {
        UserRemoteImpl(get())
    }

    factory {
        UserPreferencesManager(androidContext())
    }

    factory<UserRepository> {
        UserDataRepository(get(), get())
    }

    factory { CreateUserUseCase(get()) }
    factory { CheckUserUseCase(get()) }
    factory { GetUserInfoUseCase(get()) }
    factory { EditUserUseCase(get()) }
    factory { SaveUserIdUseCase(get()) }
    factory { FetchUserIdUseCase(get()) }

    viewModel {
        UserViewModel(get(), get(), get(), get(), get(), get())
    }
}

val scoreModule = module {

    factory {
        ScoreRemoteImpl()
    }

    factory {
        ScorePreferencesManager(androidContext())
    }

    factory<ScoreRepository> {
        ScoreDataRepository(get(), get())
    }

    factory { UpdateCurrentUserScoreUseCase(get()) }
    factory { FetchCurrentScoreUseCase(get()) }
    factory { GetBestScoresUseCase(get()) }

    viewModel {
        ScoreViewModel(get(), get(), get())
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