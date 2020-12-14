package com.louis.gourmandism.add2comment

import android.Manifest
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
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.louis.gourmandism.R
import com.louis.gourmandism.databinding.DialogAdd2commentBinding
import com.louis.gourmandism.extension.getVmFactory
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import java.io.File

class Add2commentDialog : BottomSheetDialogFragment() {

    private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0
    lateinit var binding: DialogAdd2commentBinding
    private var filePath: String = ""

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

        val storageFirebase = FirebaseStorage.getInstance().reference

        binding.textSend.setOnClickListener {

            if(filePath == ""){
                val comment = binding.editCommentContent.text.toString()
                viewModel.setComment(comment,null)
            }else{
                uploadImg(storageFirebase)
            }

        }


        viewModel.imageUri.observe(viewLifecycleOwner, Observer {
            val comment = binding.editCommentContent.text.toString()
            viewModel.setComment(comment,it)
        })

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
            viewModel.setStar(images, it.toInt())
        })

        binding.imageCamera.setOnClickListener {
            checkPermission()
        }

        return binding.root
    }

    private fun uploadImg(storageFirebase: StorageReference) {

        val metadata = StorageMetadata.Builder()
            .setContentDisposition("food")
            .setContentType("image")
            .build()

        val file = Uri.fromFile(File(filePath))
        val storageRef = storageFirebase.child(file.lastPathSegment ?: "")
        val uploadTask = storageRef.putFile(file,metadata)

        uploadTask
            .addOnSuccessListener {
                downloadImg(storageRef)
                Log.i("upload", "Success")
            }
            .addOnFailureListener { exception ->
                Log.i("upload", "Failure")
            }
//            .addOnProgressListener { taskSnapshot ->
//                val progress =
//                    (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
//                upload_progress.progress = progress
//                if (progress >= 100) {
//                    upload_progress.visibility = View.GONE
//                }
//            }
    }

    private fun downloadImg(ref: StorageReference?) {

        if (ref == null) {
            Log.i("downloadImg","null")
            return
        }
        ref.downloadUrl.addOnSuccessListener {

            Log.i("downloadImg", "Success")
            viewModel.imageUri.value = it.toString()

        }.addOnFailureListener {
                exception -> Log.i("downloadImg", "${exception}")
        }
    }

    private fun checkPermission() {
        val permission = ActivityCompat.checkSelfPermission(
            this.requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //未取得權限，向使用者要求允許權限
            ActivityCompat.requestPermissions(
                this.requireActivity(), arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                MY_PERMISSIONS_REQUEST_READ_CONTACTS
            )
        }
        getLocalImg()
    }

    private fun getLocalImg() {
        ImagePicker.with(this)
            .crop()                    //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .start()
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
                    val imgPath = filePath
                    Toast.makeText(this.requireContext(), imgPath, Toast.LENGTH_SHORT).show()
//                    Glide.with(this.requireContext()).load(filePath).into(binding.imageTest)
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
}