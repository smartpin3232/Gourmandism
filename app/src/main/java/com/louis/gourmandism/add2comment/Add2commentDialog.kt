package com.louis.gourmandism.add2comment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.louis.gourmandism.R
import com.louis.gourmandism.data.Comment
import com.louis.gourmandism.databinding.DialogAdd2commentBinding
import com.louis.gourmandism.detail.DetailViewModel
import com.louis.gourmandism.extension.getVmFactory

class Add2commentDialog : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.custom_dialog)
    }

    private val viewModel by viewModels<Add2commentDialogViewModel> {
        getVmFactory(
            Add2commentDialogArgs.fromBundle(requireArguments()).shop
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogAdd2commentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        binding.textSend.setOnClickListener {
            val comment = binding.editCommentContent.text.toString()
            viewModel.setComment(comment)
        }
        viewModel.setStatus.observe(viewLifecycleOwner, Observer {
            this.dismiss()
        })

        viewModel.star.observe(viewLifecycleOwner, Observer {
            val images = mutableListOf<ImageView>(
                binding.imageLevelOne,
                binding.imageLevelTwo,
                binding.imageLevelThree,
                binding.imageLevelFour,
                binding.imageLevelFive
            )
            viewModel.setStar(images,it.toInt())
        })

        return binding.root
    }

}