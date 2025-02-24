package com.example.debouncethrottleapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _searchQuery = MutableStateFlow("")

    private val allCountries = listOf(
        "Azerbaijan", "USA", "Canada", "Germany", "France", "India", "China", "Brazil", "Australia",
        "Japan", "South Korea", "Russia", "Italy", "Spain", "Mexico", "Argentina", "Chile",
        "South Africa", "Egypt", "Nigeria", "Kenya", "Saudi Arabia", "Turkey", "Thailand",
        "Vietnam", "Indonesia", "Malaysia", "Philippines", "Singapore", "New Zealand", "UK",
        "Ireland", "Netherlands", "Belgium", "Switzerland", "Austria", "Sweden", "Norway",
        "Finland", "Denmark", "Poland", "Greece", "Portugal", "Czech Republic", "Hungary",
        "Ukraine", "Israel", "United Arab Emirates", "Qatar", "Pakistan", "Bangladesh", "Sri Lanka"
    )

    private val _filteredCountries = MutableStateFlow<List<String>>(allCountries)
    val filteredCountries: StateFlow<List<String>> = _filteredCountries

    private var searchJob: Job? = null

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(1000)
            _filteredCountries.value = if (query.isBlank()) allCountries
            else allCountries.filter { it.contains(query, ignoreCase = true) }
        }
    }

    private val _clickCount = MutableStateFlow(0)
    val clickCount: StateFlow<Int> = _clickCount

    private var isClickable = true

    fun onButtonClick() {
        if (isClickable) {
            isClickable = false
            viewModelScope.launch {
                _clickCount.value += 1
                delay(1000)
                isClickable = true
            }
        }
    }
}