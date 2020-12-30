package com.louis.gourmandism.wish.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.louis.gourmandism.NavigationDirections
import com.louis.gourmandism.R
import com.louis.gourmandism.databinding.DialogNewWishListBinding
import com.louis.gourmandism.extension.getVmFactory

class NewWishListDialog : BottomSheetDialogFragment() {

    private val viewModel by viewModels<NewWishListDialogViewModel> { getVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.custom_dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DialogNewWishListBinding.inflate(inflater, container, false)

        binding.buttonAdd.setOnClickListener {
            val folderName = binding.editListName.text.toString()
            viewModel.createWishList(folderName)
        }

        viewModel.setWishStatus.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context,"新增成功",Toast.LENGTH_SHORT).show()
            findNavController().navigate(NavigationDirections.actionGlobalWishFragment())
        })

        return binding.root
    }

}