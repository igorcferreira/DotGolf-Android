package com.futureworkshops.dotgolf.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.futureworkshops.dotgolf.data.repository.GreetingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GreetingUiState(
    val message: String = ""
)

@HiltViewModel
class GreetingViewModel @Inject constructor(
    private val greetingRepository: GreetingRepository,
    @Named("greeting_loading") greetingLoading: String
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        GreetingUiState(message = greetingLoading)
    )
    val uiState: StateFlow<GreetingUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = GreetingUiState(
                message = greetingRepository.fetchGreeting()
            )
        }
    }
}
