package com.louis.gourmandism.add2comment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.louis.gourmandism.R
import com.louis.gourmandism.databinding.DialogAdd2commentBinding
import com.louis.gourmandism.extension.getVmFactory
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.louis.gourmandism.NavigationDirections
import com.louis.gourmandism.util.CameraUtil
import java.io.File

class Add2commentDialog : BottomSheetDialogFragment() {

    private val permissionsRequestRead = 0
    lateinit var binding: DialogAdd2commentBinding
    private var filePath: String = ""
    private val uris = mutableListOf<String>()
    private val localImageList = mutableListOf<String>()

    private val viewModel by viewModels<Add2commentDialogViewModel> {
        getVmFactory(
            Add2commentDialogArgs.fromBundle(requireArguments()).shop
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.custom_dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogAdd2commentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = Add2commentAdapter(viewModel)
        binding.recyclerViewAddCommentImage.adapter = adapter

        binding.textSend.setOnClickListener {
            if (localImageList.isNullOrEmpty()) {
                val comment = binding.editCommentContent.text.toString()
                viewModel.setComment(comment, null)
            } else {
                for (localImage in viewModel.localImages.value!!) {
                    uploadImg(localImage)
                }
            }
        }

        binding.imageCamera.setOnClickListener {
            CameraUtil.checkPermissionAndGetLocalImg(requireContext(), requireActivity(), this)
        }

        viewModel.localImages.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.imagesUri.observe(viewLifecycleOwner, Observer {
            if (it.size == viewModel.localImages.value?.size) {
                val comment = binding.editCommentContent.text.toString()
                viewModel.setComment(comment, it)
            }
        })

        viewModel.setStatus.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(NavigationDirections.actionGlobalHomeFragment())
        })

        viewModel.star.observe(viewLifecycleOwner, Observer {
            val images = mutableListOf<ImageView>(
                binding.imageLevelOne,
                binding.imageLevelTwo,
                binding.imageLevelThree,
                binding.imageLevelFour,
                binding.imageLevelFive
            )
            viewModel.setStar(images, it.toInt())
        })

        return binding.root
    }

    private fun uploadImg(localFile: String) {

        val storageFirebase = FirebaseStorage.getInstance().reference
        val metadata = StorageMetadata.Builder()
            .setContentDisposition("food")
            .setContentType("image")
            .build()

        val file = Uri.fromFile(File(localFile))
        val storageRef = storageFirebase.child(file.lastPathSegment ?: "")
        val uploadTask = storageRef.putFile(file, metadata)

        uploadTask
            .addOnSuccessListener {
                downloadImg(storageRef)
                Log.i("upload", "Success")
            }
            .addOnFailureListener { exception ->
                Log.i("upload", "Failure")
            }
    }

    private fun downloadImg(ref: StorageReference?) {

        if (ref == null) {
            Log.i("downloadImg", "null")
            return
        }
        ref.downloadUrl.addOnSuccessListener {
            uris.add(it.toString())
            viewModel.imagesUri.value = uris
        }.addOnFailureListener { exception ->
            Log.i("downloadImg", "${exception}")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionsRequestRead -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //get image
                } else {
                    Toast.makeText(this.context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                filePath = ImagePicker.getFilePath(data) ?: ""
                localImageList.add(filePath)
                viewModel.localImages.value = localImageList
                if (filePath.isBlank()) {
                    Toast.makeText(this.requireContext(), "Upload failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            ImagePicker.RESULT_ERROR -> Toast.makeText(
                this.requireContext(),
                ImagePicker.getError(data),
                Toast.LENGTH_SHORT
            ).show()
            else -> Toast.makeText(this.requireContext(), "Task Cancelled", Toast.LENGTH_SHORT)
                .show()
        }
    }

}