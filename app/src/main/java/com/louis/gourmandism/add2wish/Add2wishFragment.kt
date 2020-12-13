package com.louis.gourmandism.add2wish

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
import com.louis.gourmandism.databinding.DialogAdd2wishBinding
import com.louis.gourmandism.extension.add2wishVmFactory
import com.louis.gourmandism.extension.getVmFactory


class Add2wishFragment : BottomSheetDialogFragment() {

    private val viewModel by viewModels<Add2wishViewModel> { add2wishVmFactory(Add2wishFragmentArgs.fromBundle(requireArguments()).shopId) }

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
            adapter.submitList(it)
        })

        viewModel.addWishStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                dismiss()
            }
        })

        return binding.root
    }


}