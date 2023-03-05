package com.example.vktest

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.vktest.databinding.DialogNameChooseBinding

class NameChooseDialogFragment : DialogFragment() {
    private lateinit var binding: DialogNameChooseBinding
    private lateinit var listener: ResultListener

    interface ResultListener {
        fun onDialogPositiveClick(name: String)
        fun onDialogNegativeClick()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ResultListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement ResultListener")
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = activity?.let {
        val inflater = requireActivity().layoutInflater;
        binding = DialogNameChooseBinding.inflate(inflater)

        binding.ok.setOnClickListener {
            listener.onDialogPositiveClick(binding.name.text.toString())
            dialog?.dismiss()
        }
        binding.cancel.setOnClickListener {
            listener.onDialogNegativeClick()
            dialog?.cancel()
        }

        return AlertDialog.Builder(it)
            .setView(binding.root)
            .setTitle(R.string.choose_name)
            .create()
    } ?: throw IllegalStateException("Activity cannot be null")

}