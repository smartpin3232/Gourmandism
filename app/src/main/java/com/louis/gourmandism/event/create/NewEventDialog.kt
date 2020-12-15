package com.louis.gourmandism.event.create

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.louis.gourmandism.MainViewModel
import com.louis.gourmandism.R
import com.louis.gourmandism.databinding.DialogNewEventBinding
import com.louis.gourmandism.extension.getEventVmFactory
import com.louis.gourmandism.extension.getVmFactory


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
            viewModel.newEvent(binding.editContent.text.toString(),binding.editNumber.text.toString())
        }

        binding.editTime.setOnClickListener {
            SingleDateAndTimePickerDialog.Builder(binding.root.context)
                .bottomSheet()
                .curved()
                .backgroundColor(resources.getColor(R.color.mainStyleColor))
                .mainColor(Color.WHITE)
                .titleTextColor(Color.WHITE)
                //.stepSizeMinutes(15)
                //.todayText("aujourd'hui")
                .displayListener {}
                .title("Simple")
                .listener { date ->
                    binding.editTime.text = date.toString()
                    viewModel.date.value = date.time
                }
                .display()
        }

        viewModel.event.observe(viewLifecycleOwner, Observer {
            dismiss()
        })
        return binding.root
    }

}