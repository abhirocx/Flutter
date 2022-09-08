package com.np.namasteyoga.modules


import com.np.namasteyoga.common.ApplicationSchedulerProvider
import com.np.namasteyoga.impl.*
import com.np.namasteyoga.interfaces.SchedulerProvider
import com.np.namasteyoga.repository.*
import com.np.namasteyoga.ui.asana.catagory.AsanaCatagoryViewModel
import com.np.namasteyoga.ui.ayushMerchandise.categoryList.AyushCatagoryViewModel
import com.np.namasteyoga.ui.ayushMerchandise.detailScreen.AyushProductDetailViewModel
import com.np.namasteyoga.ui.ayushMerchandise.subcategory.AyushSubCatagoryViewModel
import com.np.namasteyoga.ui.bhuvanapp.BhuvanAppViewModel
import com.np.namasteyoga.ui.celebrityTestimonial.CelebrityTestimonialViewModel
import com.np.namasteyoga.ui.center.centerList.CenterListViewModel
import com.np.namasteyoga.ui.center.searchCenters.CenterSearchListViewModel
import com.np.namasteyoga.ui.changeLangage.ChangeLanguageViewModel
import com.np.namasteyoga.ui.changePassword.ChangePasswordViewModel
import com.np.namasteyoga.ui.eventModule.event.EventViewModel
import com.np.namasteyoga.ui.eventModule.eventVideo.EventVideoViewModel
import com.np.namasteyoga.ui.eventModule.searchEvent.SearchEventViewModel
import com.np.namasteyoga.ui.eventModule.viewmodels.PastEventListViewModel
import com.np.namasteyoga.ui.fcmTestActivity.TestFCMViewModel
import com.np.namasteyoga.ui.feedback.FeedbackViewModel
import com.np.namasteyoga.ui.feedback.feedbackRating.FeedbackRatingViewModel
import com.np.namasteyoga.ui.forgotPassword.ForgotPasswordViewModel
import com.np.namasteyoga.ui.health.history.StepHistoryViewModel
import com.np.namasteyoga.ui.health.main.HealthTrackerViewModel
import com.np.namasteyoga.ui.health.settings.GoalSettingsViewModel
import com.np.namasteyoga.ui.joiningInstructions.JoiningInstructionsViewModel
import com.np.namasteyoga.ui.login.LoginViewModel
import com.np.namasteyoga.ui.main.MainViewModel
import com.np.namasteyoga.ui.myAccount.MyAccountViewModel
import com.np.namasteyoga.ui.onPaperBoard.OnPaperBoardViewModel
import com.np.namasteyoga.ui.otpVerify.OTPVerifyViewModel
import com.np.namasteyoga.ui.register.RegisterViewModel
import com.np.namasteyoga.ui.resetPassword.ResetPasswordViewModel
import com.np.namasteyoga.ui.settings.SettingsViewModel
import com.np.namasteyoga.ui.social.SocialViewModel
import com.np.namasteyoga.ui.splash.SplashViewModel
import com.np.namasteyoga.ui.trainer.searchtrainer.TrainerSearchListViewModel
import com.np.namasteyoga.ui.trainer.trainerList.TrainerListViewModel
import com.np.namasteyoga.ui.updateAccount.center.CenterAccountUpdateViewModel
import com.np.namasteyoga.ui.updateAccount.guest.GuestUserAccountUpdateViewModel
import com.np.namasteyoga.ui.updateAccount.trainer.TrainerAccountUpdateViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module


object ViewModelModules {

    val viewModels: Module = module {

        single<LoginRepository> { LoginImpl(get(named(NetworkModule.RETROFIT_API))) }
        single<SplashRepository> { SplashImpl(get(named(NetworkModule.RETROFIT_API))) }
        single<MainRepository> { MainImpl(get(named(NetworkModule.RETROFIT_API))) }
        single<RegisterRepository> { RegisterImpl(get(named(NetworkModule.RETROFIT_API))) }
        single<OTPVerifyRepository> { OTPVerifyImpl(get(named(NetworkModule.RETROFIT_API))) }
        single<ChangePasswordRepository> { ChangePasswordImpl(get(named(NetworkModule.RETROFIT_API))) }
        single<ForgotPasswordRepository> { ForgotPasswordImpl(get(named(NetworkModule.RETROFIT_API))) }
        single<ResetPasswordRepository> { ResetPasswordImpl(get(named(NetworkModule.RETROFIT_API))) }
        single<TestFCMRepository> { TestFCMImpl(get(named(NetworkModule.RETROFIT_API))) }
        single<TrainerListRepository> { TrainerListImpl(get(named(NetworkModule.RETROFIT_API))) }
        single<TrainerSearchListRepository> { TrainerSearchListImpl(get(named(NetworkModule.RETROFIT_API))) }
        single<AsanaCatagoryRepository> { AsanaCatagoryImpl(get(named(NetworkModule.RETROFIT_API))) }
        single<AyushCatagoryRepository> { AyushCatagoryImpl(get(named(NetworkModule.RETROFIT_API))) }
        single<SettingsRepository> { SettingsImpl(get(named(NetworkModule.RETROFIT_API))) }
        single<MyAccountRepository> { MyAccountImpl(get(named(NetworkModule.RETROFIT_API))) }
        single<AccountUpdateRepository> { AccountUpdateImpl(get(named(NetworkModule.RETROFIT_API))) }
        single<ChangeLanguageRepository> { ChangeLanguageImpl(get(named(NetworkModule.RETROFIT_API))) }
        single<SocialLinkRepository> { SocialLinkImpl(get(named(NetworkModule.RETROFIT_API))) }
        single<EventRepository> { EventImpl(get(named(NetworkModule.RETROFIT_API))) }
        single<FeedbackRepository> { FeedbackImpl(get(named(NetworkModule.RETROFIT_API))) }
        single<JoiningInstructionRepository> { JoiningIntructionImpl(get(named(NetworkModule.RETROFIT_API))) }
        single<SearchEventRepository> { SearchEventImpl(get(named(NetworkModule.RETROFIT_API))) }
        single<EventVideoRepository> { EventVideoImpl(get(named(NetworkModule.RETROFIT_API))) }
        single<CelebrityTestimonialRepository> { CelebrityTestimonialImpl(get(named(NetworkModule.RETROFIT_API))) }

//        single<GoogleRepository> { GoogleImpl(get(named(NetworkModule.RETROFIT_API_GOOGLE))) }

        viewModel { LoginViewModel(get(), get(named(SharedPreference.DataPrefs)), get()) }
        viewModel { SplashViewModel(get(), get(named(SharedPreference.DataPrefs)), get()) }
        viewModel { MainViewModel(get(), get(named(SharedPreference.DataPrefs)), get()) }
        viewModel { TestFCMViewModel(get(), get(named(SharedPreference.DataPrefs)), get()) }
        viewModel { HealthTrackerViewModel(get(named(SharedPreference.DataPrefs)))}
        viewModel { GoalSettingsViewModel(get(named(SharedPreference.DataPrefs)))}
        viewModel { StepHistoryViewModel(get(named(SharedPreference.DataPrefs)))}
        viewModel { RegisterViewModel(get(),get(named(SharedPreference.DataPrefs)), get())}
        viewModel { ChangePasswordViewModel(get(),get(named(SharedPreference.DataPrefs)), get())}
        viewModel { ForgotPasswordViewModel(get(),get())}
        viewModel { ResetPasswordViewModel(get(),get())}
        viewModel { TrainerListViewModel(get(),get(named(SharedPreference.DataPrefs)),get())}
        viewModel { TrainerSearchListViewModel(get(),get(named(SharedPreference.DataPrefs)),get()) }
        viewModel { OTPVerifyViewModel(get(),get(named(SharedPreference.DataPrefs)),get())}
        viewModel { CenterListViewModel(get(),get(named(SharedPreference.DataPrefs)),get())}
        viewModel { CenterSearchListViewModel(get(),get(named(SharedPreference.DataPrefs)),get())}
        viewModel { AsanaCatagoryViewModel(get(),get(named(SharedPreference.DataPrefs)),get())}
        viewModel { SocialViewModel(get(),get(named(SharedPreference.DataPrefs)),get())}
        viewModel { SettingsViewModel(get(),get(named(SharedPreference.DataPrefs)),get())}
        viewModel { OnPaperBoardViewModel(get(named(SharedPreference.DataPrefs)),get())}
        viewModel { MyAccountViewModel(get(),get(named(SharedPreference.DataPrefs)),get())}
        viewModel { ChangeLanguageViewModel(get(),get(named(SharedPreference.DataPrefs)),get())}
        viewModel { TrainerAccountUpdateViewModel(get(),get(named(SharedPreference.DataPrefs)),get())}
        viewModel { CenterAccountUpdateViewModel(get(),get(named(SharedPreference.DataPrefs)),get())}
        viewModel { GuestUserAccountUpdateViewModel(get(),get(named(SharedPreference.DataPrefs)),get())}
        viewModel { EventViewModel(get(),get(named(SharedPreference.DataPrefs)),get())}
        viewModel { PastEventListViewModel(get(),get(named(SharedPreference.DataPrefs)),get())}
        viewModel { FeedbackViewModel(get(),get(named(SharedPreference.DataPrefs)),get())}
        viewModel { FeedbackRatingViewModel(get(),get(named(SharedPreference.DataPrefs)),get())}
        viewModel { JoiningInstructionsViewModel(get(),get(named(SharedPreference.DataPrefs)),get())}
        viewModel { SearchEventViewModel(get(),get(named(SharedPreference.DataPrefs)),get())}
        viewModel { AyushCatagoryViewModel(get(),get(named(SharedPreference.DataPrefs)),get())}
        viewModel { AyushSubCatagoryViewModel(get(),get(named(SharedPreference.DataPrefs)),get())}
        viewModel { AyushProductDetailViewModel(get(),get(named(SharedPreference.DataPrefs)),get())}
        viewModel { EventVideoViewModel(get(),get(named(SharedPreference.DataPrefs)),get())}
        viewModel { CelebrityTestimonialViewModel(get(),get(named(SharedPreference.DataPrefs)),get())}
        viewModel { BhuvanAppViewModel(get(),get(named(SharedPreference.DataPrefs)),get())}

    }


    val rxModule: Module = module {
        // provided components
        single { ApplicationSchedulerProvider() as SchedulerProvider }
    }


}

