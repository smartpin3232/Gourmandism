package com.louis.gourmandism.add2wish

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.louis.gourmandism.R
import com.louis.gourmandism.databinding.DialogAdd2wishBinding
import com.louis.gourmandism.event.EventFragmentDirections
import com.louis.gourmandism.event.EventJoinDialog
import com.louis.gourmandism.extension.add2wishVmFactory

class Add2wishDialog : BottomSheetDialogFragment() {

    private val viewModel by viewModels<Add2wishViewModel> { add2wishVmFactory(Add2wishDialogArgs.fromBundle(requireArguments()).shopId) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.custom_dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogAdd2wishBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        val adapter = Add2wishAdapter(viewModel)
        binding.recyclerViewAdd2wish.adapter = adapter

        viewModel.myFavorite.observe(viewLifecycleOwner, Observer {
           it?.let {
               adapter.submitList(viewModel.getMyFavorite(it))
           }
        })

        viewModel.addWishStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                dismiss()
                findNavController().navigate(
                    EventFragmentDirections.actionGlobalEventJoinDialog(EventJoinDialog.MessageType.JOIN)
                )
            }
        })

        return binding.root
    }


}