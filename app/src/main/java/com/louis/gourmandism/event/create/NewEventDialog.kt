package com.louis.gourmandism.event.create

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.louis.gourmandism.R
import com.louis.gourmandism.databinding.DialogNewEventBinding
import com.louis.gourmandism.extension.getEventVmFactory


class NewEventDialog : Fragment() {

    private val viewModel by viewModels<NewEventViewModel> { getEventVmFactory(NewEventDialogArgs.fromBundle(requireArguments()).shop) }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.custom_dialog)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogNewEventBinding.inflate(inflater,container,false)

        binding.textGo.setOnClickListener {
            viewModel.newEvent(binding.editContent.text.toString(),binding.editNumber.text.toString())
        }

        binding.editTime.setOnClickListener {
//            val builder =
//                AlertDialog.Builder(requireContext())
//            builder.setMessage("喜歡的話給個好評吧！")
//            val dialog: AlertDialog = builder.create()
//            dialog.show()
            val builder = SingleDateAndTimePickerDialog.Builder(context)
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

                .build()
            builder.display()
        }

//        viewModel.event.observe(viewLifecycleOwner, Observer {
//            dismiss()
//        })

        return binding.root
    }

}