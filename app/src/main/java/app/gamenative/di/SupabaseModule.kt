package app.gamenative.di

import app.gamenative.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.ktor.client.plugins.HttpTimeout
import javax.inject.Singleton
import timber.log.Timber

@InstallIn(SingletonComponent::class)
@Module
class SupabaseModule {
    @OptIn(SupabaseInternal::class)
    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient? {
        Timber.d("Initializing Supabase client with URL: ${BuildConfig.SUPABASE_URL}")
        if (BuildConfig.SUPABASE_URL.isBlank() || BuildConfig.SUPABASE_KEY.isBlank()) {
            Timber.e(
                "Invalid Supabase URL or key - URL: ${BuildConfig.SUPABASE_URL}, key empty: ${BuildConfig.SUPABASE_KEY.isBlank()}",
            )
            return null
        }

        return createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_KEY,
        ) {
            Timber.d("Configuring Supabase client")
            httpConfig {
                Timber.d("Setting up HTTP timeouts")
                install(HttpTimeout) {
                    requestTimeoutMillis = 30_000   // overall call
                    connectTimeoutMillis = 15_000   // TCP handshake / TLS
                    socketTimeoutMillis = 30_000   // idle socket
                }
            }
            install(Postgrest)
            Timber.d("Postgrest plugin installed")
        }
    }
}
