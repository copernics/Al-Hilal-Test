package xyz.coderes.ai_hilal_test.di

import androidx.room.Room
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import xyz.coderes.ai_hilal_test.core.data.HttpClientFactory
import xyz.coderes.ai_hilal_test.crypto.data.database.CoinDatabase
import xyz.coderes.ai_hilal_test.crypto.data.database.CoinFavoriteDao
import xyz.coderes.ai_hilal_test.crypto.data.networking.RemoteCoinDataSource
import xyz.coderes.ai_hilal_test.crypto.data.networking.RemoteCoinSource
import xyz.coderes.ai_hilal_test.crypto.data.repository.DefaultCoinRepository
import xyz.coderes.ai_hilal_test.crypto.domain.CoinRepository
import xyz.coderes.ai_hilal_test.crypto.presentation.CoinListViewModel
import xyz.coderes.ai_hilal_test.crypto.presentation.FavouriteCoinListViewModel
import kotlin.jvm.java

val appModule = module {
    single<HttpClient> { HttpClientFactory.create(CIO.create()) }
    single<CoinDatabase> {
        Room.databaseBuilder(
            get(), // applicationContext will be injected here
            CoinDatabase::class.java,
            CoinDatabase.DATABASE_NAME
        ).build()
    }

    single<CoinFavoriteDao> {
        get<CoinDatabase>().favoriteCoinDao
    }
    singleOf(::RemoteCoinSource).bind<RemoteCoinDataSource>()
    singleOf(::DefaultCoinRepository).bind<CoinRepository>()
    viewModel { CoinListViewModel(get()) }
    viewModel { FavouriteCoinListViewModel(get()) }
}
