package com.example.debouncethrottleapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: CountryAdapter
    private lateinit var searchEditText: EditText
    private lateinit var countryRecyclerView: RecyclerView
    private lateinit var counterTextView: TextView
    private lateinit var clickButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchEditText = findViewById(R.id.searchEditText)
        countryRecyclerView = findViewById(R.id.countryRecyclerView)
        counterTextView = findViewById(R.id.counterTextView)
        clickButton = findViewById(R.id.clickButton)

        adapter = CountryAdapter()
        countryRecyclerView.layoutManager = LinearLayoutManager(this)
        countryRecyclerView.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.filteredCountries.collectLatest { countries ->
                    adapter.submitList(countries)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.clickCount.collectLatest { count ->
                    counterTextView.text = "Click Count: $count"
                }
            }
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onSearchQueryChanged(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        clickButton.setOnClickListener {
            viewModel.onButtonClick()
        }
    }
}