package com.example.vktest

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.vktest.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var vm: RecordsListVm


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = ViewModelProvider(this)[RecordsListVm::class.java]
        vm.errorStringId.observe(this) {
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }

        val notesAdapter = NotesListAdapter(vm::play, vm::pause)
        binding.records.adapter = notesAdapter
        vm.records.observe(this) {
            notesAdapter.submitList(it)
        }

    }
}