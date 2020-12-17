package com.louis.gourmandism.search.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.louis.gourmandism.R
import com.louis.gourmandism.databinding.DialogNewTagBinding
import com.louis.gourmandism.extension.getVmFactory
import com.louis.gourmandism.search.SearchViewModel

class NewTagDialog : DialogFragment() {

    private val viewModel by viewModels<NewTagDialogViewModel> { getVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.custom_dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DialogNewTagBinding.inflate(inflater, container, false)

        binding.buttonAddTag.setOnClickListener {

            val tagName = binding.editTag.text.toString()
            viewModel.setUserTag(tagName)
        }

        viewModel.tagStatus.observe(viewLifecycleOwner, Observer {
            viewModel.getProfile()
            dismiss()
        })

        return binding.root
    }

}