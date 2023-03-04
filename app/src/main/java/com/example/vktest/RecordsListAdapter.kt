package com.example.vktest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vktest.data.RecordVm
import com.example.vktest.databinding.ItemRecordBinding
import com.example.vktest.views.ToggleImageButton
import java.time.format.DateTimeFormatter

class NotesListAdapter(
    private val onPlay: (RecordVm, Int) -> Unit,
    private val onPause: (RecordVm, Int) -> Unit,
) : ListAdapter<RecordVm, NotesListAdapter.ViewHolder>(RecordDiffCallback) {

    class ViewHolder(
        val binding: ItemRecordBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemRecordBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )

        return ViewHolder(view)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val binding = viewHolder.binding
        val record = getItem(position)
        binding.title.text = record.title
        binding.creationTime.text = record.recordingTimestamp.format(DateTimeFormatter.ISO_TIME) //TODO: format
        binding.playStopButton.isChecked = record.selected


        viewHolder.binding.playStopButton.onCheckedChangeListener = object :
            ToggleImageButton.OnCheckedChangeListener {
            override fun onCheckedChanged(button: ToggleImageButton?, isChecked: Boolean) {
                if(isChecked) {
                    onPlay(record, viewHolder.adapterPosition)
                } else {
                    onPause(record, viewHolder.adapterPosition)
                }
            }
        }
    }

}


object RecordDiffCallback : DiffUtil.ItemCallback<RecordVm>() {
    override fun areItemsTheSame(old: RecordVm, new: RecordVm): Boolean {
        return old.audioSource == new.audioSource
    }

    override fun areContentsTheSame(old: RecordVm, new: RecordVm): Boolean {
        return areItemsTheSame(old, new) && old.selected == new.selected
    }
}
