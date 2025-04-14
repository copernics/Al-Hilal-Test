package xyz.coderes.ai_hilal_test

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import xyz.coderes.ai_hilal_test.di.appModule

class AiHilalApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AiHilalApp)
            androidLogger()

            modules(appModule)
        }
    }
}