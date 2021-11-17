package com.lx.ohmyjuus.jubging

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.afollestad.assent.Permission
import com.afollestad.assent.askForPermissions
import com.lx.ohmyjuus.api.BasicClient
import com.lx.ohmyjuus.api.JUUSClient
import com.lx.ohmyjuus.databinding.ActivityMainBinding
import com.lx.ohmyjuus.databinding.ActivityPhotoBinding
import com.lx.ohmyjuus.response.CaptureUploadRes
import com.lx.ohmyjuus.response.FileUploadResponse
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PhotoActivity: AppCompatActivity() {
    lateinit var binding: ActivityPhotoBinding

    val outputFilename = "photo.jpg"
    lateinit var outputFile: File

    lateinit var takePictureLauncher:ActivityResultLauncher<Intent>

//    lateinit var pickAlbumLauncher:ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 위험 권한 요청
        askForPermissions(Permission.READ_EXTERNAL_STORAGE, Permission.CAMERA) { result ->
            println("askForPermissions result : ${result.granted().size}")
        }

        // 사진찍기 인텐트 등록
        takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.apply {
                    handleCaptureResult()
                }
            }
        }

//        // 앨범에서선택 인텐트 등록
//        pickAlbumLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                result.data?.apply {
//                    handlePickResult(this)
//                }
//            }
//        }

        // 사진찍기 버튼 클릭
        binding.takePictureButton.setOnClickListener {
            takePicture()
        }
//
//        // 앨범에서선택 버튼 클릭
//        binding.pickAlbumButton.setOnClickListener {
//            pickAlbum()
//        }

    }

    fun takePicture() {

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { captureIntent ->
            outputFile = getPhotoFile()

            val providerFile = FileProvider.getUriForFile(this, "com.lx.ohmyjuus", outputFile)
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerFile)

            captureIntent.resolveActivity(packageManager)?.also {
                takePictureLauncher.launch(captureIntent)
            } ?:run {
                Log.e("Main", "Cannot open camera app.")
            }
        }

        finish()
    }

    fun getPhotoFile(): File {
        val directoryStorage = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val file = File(directoryStorage, outputFilename)

        try {
            if (file.exists()) {
                file.delete()
            }
        } catch(e:Exception) {
            e.printStackTrace()
        }

        return file
    }

    fun handleCaptureResult() {
        loadImageFromFile(outputFile.absolutePath)

        // 파일 업로드 테스트
        uploadFile(outputFile.absolutePath)
    }
//
//    fun pickAlbum() {
//
//        Intent(ACTION_PICK).also { pickIntent ->
//            pickIntent.type = "image/*"
//
//            pickIntent.resolveActivity(packageManager)?.also {
//                pickAlbumLauncher.launch(pickIntent)
//            } ?:run {
//                Log.e("Main", "Cannot open album app.")
//            }
//        }
//
//    }

    @TargetApi(19)
    fun handlePickResult(data: Intent) {
        var imagePath: String? = null
        val uri = data.data

        if (uri != null) {
            if (DocumentsContract.isDocumentUri(this, uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                if (uri.authority == "com.android.providers.media.documents") {
                    val id = docId.split(":")[1]
                    val selection = MediaStore.Images.Media._ID + "=" + id
                    imagePath = getImagePath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        selection
                    )
                } else if (uri.authority == "com.android.providers.downloads.documents") {
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse(
                            "content://downloads/public_downloads"
                        ), docId.toLong()
                    )
                    imagePath = getImagePath(contentUri, null)
                }
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                imagePath = getImagePath(uri, null)
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                imagePath = uri.path
            }

            imagePath?.apply {
                loadImageFromFile(this)
                uploadFile(this)
            }
        }
    }

    @SuppressLint("Range")
    private fun getImagePath(uri: Uri, selection: String?): String? {
        var path: String? = null
        val cursor = contentResolver.query(uri, null, selection, null, null)
        if (cursor != null){
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }

        return path
    }

    fun loadImageFromFile(imagePath:String) {

        val targetWidth: Int = binding.selectedImageView.width
        val targetHeight: Int = binding.selectedImageView.height

        val bmOptions = BitmapFactory.Options().apply {
            inJustDecodeBounds = true

            val photoWidth: Int = outWidth
            val photoHeight: Int = outHeight

            val scaleFactor: Int = Math.min(photoWidth / targetWidth, photoHeight / targetHeight)

            inJustDecodeBounds = false
            inSampleSize = scaleFactor
        }

        BitmapFactory.decodeFile(imagePath, bmOptions)?.also { bitmap ->
            binding.selectedImageView.setImageBitmap(bitmap)
        }

    }

    fun uploadFile(filePath:String) {
        val outExtension = filePath.substringAfterLast(".").uppercase()
        val outFile = File(filePath)
        println("outExtension : $outExtension")

        var mediaTypeString:String = "image/*"
        when (outExtension) {
            "JPG", "JPEG" -> mediaTypeString = "image/jpg"
            "PNG" -> mediaTypeString = "image/png"
            "GIF" -> mediaTypeString = "image/gif"
            "TIF", "TIFF" -> mediaTypeString = "image/tif"
        }

        val filePart = MultipartBody.Part.createFormData(
            "photo",
            outputFilename,
            RequestBody.create(mediaTypeString.toMediaTypeOrNull(), outFile)
        )

        val params = hashMapOf<String,String>()
        params["userId"] = "ahs77"
        params["username"] = "안호성"

        BasicClient.api.uploadFile(
            file=filePart,
            params=params
        ).enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(call: Call<FileUploadResponse>, response: Response<FileUploadResponse>) {
                println("onResponse called : ${response.body().toString()}")

                JUUSClient.api.saveUpload(
                    userId= "ahs77",
                    filename=outputFilename
                ).enqueue(object : Callback<CaptureUploadRes> {
                    override fun onResponse(call: Call<CaptureUploadRes>, response: Response<CaptureUploadRes>) {
                        TODO("Not yet implemented")

                    }

                    override fun onFailure(call: Call<CaptureUploadRes>, t: Throwable) {
                        println("onFailure called : ${t.message}")
                    }
                })

            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                println("onFailure called : ${t.message}")

            }
        })

        println("uploadFile called.")

    }


}

