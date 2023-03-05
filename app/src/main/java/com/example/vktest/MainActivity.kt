package com.example.vktest

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.vktest.databinding.ActivityMainBinding
import com.example.vktest.vm.NotesListVm
import com.example.vktest.vm.RecordingVm
import com.example.vktest.vm.RecordingVm.RecordingState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NameChooseDialogFragment.ResultListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var listVm: NotesListVm
    private lateinit var recordingVm: RecordingVm


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                recordingVm.onPermissionGranted()
            } else {
                showSnackBar(R.string.error_no_permission_audio_record)
            }
        }

    fun requestAudioRecordingPermission() {
        val permission = android.Manifest.permission.RECORD_AUDIO
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            recordingVm.onPermissionGranted()
        } else if (shouldShowRequestPermissionRationale(permission)) {
            //TODO
        } else {
            requestPermissionLauncher.launch(permission)
        }
    }

    private fun showSnackBar(@StringRes res: Int) {
        Snackbar.make(binding.root, res, Snackbar.LENGTH_SHORT).show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listVm = ViewModelProvider(this)[NotesListVm::class.java]

        listVm.error.observe(this) {error ->
            if(error == null) {
                return@observe

            }
            val resource = when(error) {
                NotesListVm.Error.CORRUPTED_AUDIO -> R.string.error_corrupted_audio_file
                NotesListVm.Error.UNEXPECTED_ERROR -> R.string.error_unexpected
            }
            showSnackBar(resource)
        }

        val notesAdapter = NotesListAdapter(listVm::play, listVm::pause)
        binding.records.adapter = notesAdapter
        listVm.notes.observe(this) {
            notesAdapter.submitList(it)
        }


        recordingVm = ViewModelProvider(this)[RecordingVm::class.java]

        binding.btnRecordStop.setOnClickListener {
            recordingVm.toggleRecording()
        }

        recordingVm.enabledButton.observe(this) {
            binding.btnRecordStop.isEnabled = it
        }

        recordingVm.state.observe(this) { state ->
            val microphoneIcon = AppCompatResources.getDrawable(this, R.drawable.ic_microphone)
            val stopIcon = AppCompatResources.getDrawable(this, R.drawable.ic_stop)
            when(state) {
                RecordingState.IDLE -> {
                    binding.btnRecordStop.setImageDrawable(microphoneIcon)
                }
                RecordingState.ERROR_UNEXPECTED -> {
                    showSnackBar(R.string.error_unexpected)
                    binding.btnRecordStop.setImageDrawable(microphoneIcon)
                }
                RecordingState.ERROR_CLICK_TOO_FAST -> {
                    showSnackBar(R.string.error_click_too_fast)
                    binding.btnRecordStop.setImageDrawable(microphoneIcon)
                }
                RecordingState.RECORDING -> {
                    binding.btnRecordStop.setImageDrawable(stopIcon)
                }
                RecordingState.PERMISSION_REQUEST -> {
                    requestAudioRecordingPermission()
                }
                RecordingState.NAMING -> {
                    //TODO naming dialog
                    NameChooseDialogFragment().show(supportFragmentManager, "name_choose")
                }
            }
        }
    }

    override fun onDialogPositiveClick(name: String) {
        recordingVm.namingComplete(name)
    }

    override fun onDialogNegativeClick() {
        recordingVm.namingCanceled()
    }
}