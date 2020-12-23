package com.louis.gourmandism.search.create

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.common.server.response.FastSafeParcelableJsonResponse
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.louis.gourmandism.NavigationDirections
import com.louis.gourmandism.databinding.FragmentNewShopBinding
import com.louis.gourmandism.detail.DetailFragmentArgs
import com.louis.gourmandism.extension.getVmFactory
import com.louis.gourmandism.extension.newShopVmFactory
import com.louis.gourmandism.wish.WishViewModel
import java.io.File

class NewShopFragment : Fragment() {

    private val viewModel by viewModels<NewShopViewModel> {
        newShopVmFactory(
            NewShopFragmentArgs.fromBundle(requireArguments()).location
        )
    }

    private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0
    lateinit var binding: FragmentNewShopBinding
    private var filePath: String = ""
    private val uris = mutableListOf<String>()
    private val localImageList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val storageFirebase = FirebaseStorage.getInstance().reference
        binding = FragmentNewShopBinding.inflate(inflater, container, false)

        val adapter = NewShopAdapter(viewModel)
        binding.recyclerViewAddCommentImage.adapter = adapter

        binding.buttonCreate.setOnClickListener {

            if(localImageList.isNullOrEmpty()){
            viewModel.newShop(
                binding.editName.text.toString(),
                binding.editAddress.text.toString(),
                binding.editPhone.text.toString()
            )}else{
                    for(localImage in viewModel.localImages.value!!){
                        uploadImg(storageFirebase, localImage)
                    }
                }
        }

        binding.imageCamera.setOnClickListener {
            checkPermission()
        }

        viewModel.imagesUri.observe(viewLifecycleOwner, Observer {
            if(it.size == viewModel.localImages.value?.size){
                viewModel.newShop(
                    binding.editName.text.toString(),
                    binding.editAddress.text.toString(),
                    binding.editPhone.text.toString()
                )
            }
        })

        viewModel.newStatus.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(NavigationDirections.actionGlobalSearchFragment())
        })

        viewModel.localImages.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })

        return binding.root
    }

    private fun uploadImg(storageFirebase: StorageReference, localFile: String) {

        val metadata = StorageMetadata.Builder()
            .setContentDisposition("food")
            .setContentType("image")
            .build()

        val file = Uri.fromFile(File(localFile))
        val storageRef = storageFirebase.child(file.lastPathSegment ?: "")
        val uploadTask = storageRef.putFile(file,metadata)
//        val uploadTask=storageRef.listAll()
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
            uris.add(it.toString())
            viewModel.imagesUri.value = uris
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
                localImageList.add(filePath)
                viewModel.localImages.value = localImageList
                if (filePath.isNotEmpty()) {

//                    Glide.with(this.requireContext()).load(filePath).into(binding.imagePhoto)
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