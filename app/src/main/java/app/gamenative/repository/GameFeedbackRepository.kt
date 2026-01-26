package app.gamenative.repository

import android.content.Context
import app.gamenative.utils.GameFeedbackUtils
import app.gamenative.utils.KofiSupporter
import app.gamenative.utils.fetchKofiSupporters
import io.github.jan.supabase.SupabaseClient
import javax.inject.Inject

class SupabaseRepository @Inject constructor(
    private val supabaseClient: SupabaseClient?,
) {
    suspend fun submitGameFeedback(
        context: Context,
        appId: String,
        rating: Int,
        tags: List<String>,
        notes: String?,
    ): Boolean {
        val client = supabaseClient ?: return false
        return GameFeedbackUtils.submitGameFeedback(
            context = context,
            supabase = client,
            appId = appId,
            rating = rating,
            tags = tags,
            notes = notes,
        )
    }

    suspend fun fetchSupporters(): List<KofiSupporter> {
        val client = supabaseClient ?: return emptyList()
        return fetchKofiSupporters(client)
    }
}
