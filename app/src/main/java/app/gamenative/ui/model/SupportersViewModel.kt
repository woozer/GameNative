package app.gamenative.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.gamenative.repository.SupabaseRepository
import app.gamenative.utils.KofiSupporter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class SupportersViewModel @Inject constructor(
    private val supabaseRepository: SupabaseRepository,
) : ViewModel() {
    private val _supporters = MutableStateFlow<List<KofiSupporter>>(emptyList())
    val supporters: StateFlow<List<KofiSupporter>> = _supporters.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadSupporters() {
        if (_isLoading.value || _supporters.value.isNotEmpty()) {
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            val data = withContext(Dispatchers.IO) {
                supabaseRepository.fetchSupporters()
            }
            _supporters.value = data
            _isLoading.value = false
        }
    }
}
