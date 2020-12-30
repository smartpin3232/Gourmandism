package com.louis.gourmandism.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.NumberPicker
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.louis.gourmandism.profile.edit.ProfileEditDialog
import java.io.File
import java.util.concurrent.ExecutionException

object CameraUtil {

    private const val myPermissionsRequestRead = 0

    fun checkPermissionAndGetLocalImg(
        context: Context,
        activity: Activity,
        fragment: Fragment
    ) {
        val permission = ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //if not having permission, ask for it
            ActivityCompat.requestPermissions(
                activity, arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                myPermissionsRequestRead
            )
            getLocalImg(fragment)
        } else {
            getLocalImg(fragment)
        }
    }
    private fun getLocalImg(fragment: Fragment) {
        ImagePicker.with(fragment)
            //Crop image(Optional), Check Customization for more option
            .crop()
            //Final image size will be less than 1 MB(Optional)
            .compress(1024)
            //Final image resolution will be less than 1080 x 1080(Optional)
            .maxResultSize(
                1080,
                1080
            )
            .start()
    }

//    fun uploadPhotoAndGetUri(localImage: String, context: Context) : String?{
//
//        try {
//            val storageRef = FirebaseStorage.getInstance().reference
//            val file = Uri.fromFile(File(localImage))
//            var uri : String? = ""
//            val eventsRef = storageRef.child(file.lastPathSegment ?: "")
//
//            val metadata = StorageMetadata.Builder()
//                .setContentDisposition("food")
//                .setContentType("image")
//                .build()
//
////            val uploadTask = eventsRef.putFile(file, metadata)
//
//            eventsRef.putFile(file, metadata)
//                .addOnSuccessListener {
//                    uri = downloadImg(eventsRef, context)
//                }
//                .addOnFailureListener { exception ->
//                    uri = downloadImg(eventsRef, context)
//                }
//
//
//
//        } catch (e: ExecutionException){
//            return ""
//        }
//    }
//
//    private fun downloadImg(ref: StorageReference?, context: Context) : String? {
//        var uri = ""
//        if (ref == null) {
//            Toast.makeText(context, "No file", Toast.LENGTH_SHORT).show()
//            return null
//        }
//        ref.downloadUrl.addOnSuccessListener {
//            uri = it.toString()
//        }.addOnFailureListener { exception ->
//            Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
//        }
//        return uri
//    }

}