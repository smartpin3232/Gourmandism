package com.louis.gourmandism.event.create

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.louis.gourmandism.R
import com.louis.gourmandism.databinding.DialogNewEventBinding
import com.louis.gourmandism.extension.getEventVmFactory



class NewEventDialog : BottomSheetDialogFragment() {

    private val viewModel by viewModels<NewEventViewModel> { getEventVmFactory(NewEventDialogArgs.fromBundle(requireArguments()).shop) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.custom_dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogNewEventBinding.inflate(inflater,container,false)

        binding.textGo.setOnClickListener {
            viewModel.newEvent(binding.editContent.text.toString())
        }

        viewModel.event.observe(viewLifecycleOwner, Observer {
            dismiss()
        })
        return binding.root
    }

}