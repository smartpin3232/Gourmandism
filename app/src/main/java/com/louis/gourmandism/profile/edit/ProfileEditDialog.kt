package com.louis.gourmandism.profile.edit

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.louis.gourmandism.NavigationDirections
import com.louis.gourmandism.R
import com.louis.gourmandism.databinding.DialogProfileEditBinding
import com.louis.gourmandism.extension.getVmFactory
import com.louis.gourmandism.util.CameraUtil

import java.io.File

class ProfileEditDialog : BottomSheetDialogFragment() {

    private val viewModel by viewModels<ProfileEditViewModel> {
        getVmFactory()
    }

    private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0
    var localImageList = mutableListOf<String>()
    var filePath: String = ""
    lateinit var binding : DialogProfileEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.custom_dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogProfileEditBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.profile.value = ProfileEditDialogArgs.fromBundle(requireArguments()).user

        binding.buttonSend.setOnClickListener {
            if(!filePath.isBlank()){
                uploadPhoto(filePath)
            }else{
                val user = viewModel.profile.value
                user?.apply {
                    name = binding.editName.text.toString()
                    intro = binding.editIntroduction.text.toString()
                    viewModel.updateUserInfo(user)
                }
            }

        }

        binding.userImage.setOnClickListener {
            CameraUtil.checkPermissionAndGetLocalImg(requireContext(),requireActivity(),this)
        }

        viewModel.imageUri.observe(viewLifecycleOwner, Observer {
            val user = viewModel.profile.value
            user?.let { userInfo ->
                userInfo.apply {
                    image = it
                    name = binding.editName.text.toString()
                    intro = binding.editIntroduction.text.toString()
                }
                viewModel.updateUserInfo(user)
            }
        })

        viewModel.updateStatus.observe(viewLifecycleOwner, Observer {
            viewModel.profile.value?.let {
                findNavController().navigate(NavigationDirections.actionGlobalProfileFragment(it.id))
            }
        })

        return binding.root
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {
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
                if (filePath.isNotEmpty()) {
                    localImageList.add(filePath)
                    Glide.with(this.requireContext()).load(filePath).into(binding.imageUser)
                } else {
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
    private fun downloadImg(ref: StorageReference?) {
        if (ref == null) {
            Toast.makeText(this.requireContext(), "No file", Toast.LENGTH_SHORT).show()
            return
        }
        ref.downloadUrl.addOnSuccessListener {
            viewModel.imageUri.value = it.toString()
        }.addOnFailureListener { exception ->
            Toast.makeText(this.requireContext(), exception.message, Toast.LENGTH_SHORT).show()
        }
    }
    private fun uploadPhoto(localImage: String) {
        val storageRef = FirebaseStorage.getInstance().reference
        val file = Uri.fromFile(File(localImage))
        val eventsRef = storageRef.child(file.lastPathSegment ?: "")

        val metadata = StorageMetadata.Builder()
            .setContentDisposition("food")
            .setContentType("image")
            .build()

        val uploadTask = eventsRef.putFile(file, metadata)
        uploadTask
            .addOnSuccessListener {
                downloadImg(eventsRef)
                Log.i("Upload", "Success")
            }
            .addOnFailureListener { exception ->
                Log.i("Upload", exception.toString())
            }
    }

}