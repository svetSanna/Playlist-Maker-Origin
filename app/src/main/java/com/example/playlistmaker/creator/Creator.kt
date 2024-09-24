package com.example.playlistmaker.creator

object Creator {
    // поле, которое отвечает за контекст
  //  private lateinit var application: Application    //private lateinit var application: AppCompatActivity //p2

    /*fun initApplication(application: Application) { // передаем сюда контекст приложения  //p2
        this.application = application
    }*/

    /*fun provideSharedPreferences(): SharedPreferences {
        // на основе контекста получаем SharedPreferences
        return application.getSharedPreferences(PLAYLISTMAKER_PREFERENCES, Context.MODE_PRIVATE)
    }*/

  /*  fun provideMediaPlayerInteractor(): MediaPlayerInteractor {//MediaPlayerInteractorImpl {

        return MediaPlayerInteractorImpl(provideMediaPlayer())
    }

    private fun provideMediaPlayer(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl()
    }
*/
   /* fun provideSharedPreferencesInteractor(): SharedPreferencesInteractor { //SharedPreferencesInteractorImpl {
        return SharedPreferencesInteractorImpl(provideSharedPreferencesRepository())
    }*/

   /* private fun provideSharedPreferencesRepository(): SharedPreferencesRepository {
        return SharedPreferencesRepositoryImpl()
    }*/

   /* fun provideGetTrackListUseCase(): GetTrackListUseCase {
        return GetTrackListUseCase(provideTrackRepository())
    }

    private fun provideTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(provideNetworkClient())
    }

    private fun provideNetworkClient(): NetworkClient {
        return RetrofitNetworkClient()
    }
*/
  /*  fun provideGetSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(provideSearchHistoryInteractor())
    }
*/
  /*  private fun provideSearchHistoryInteractor(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl()
    }*/
}