package com.peerbitskuldeep.chatapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File

class SettingsActivity : AppCompatActivity() {

    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mCurrentUser: FirebaseUser
    private lateinit var mStorageReference: StorageReference
    private lateinit var uid: String

    private val GALLERY_ID : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        mCurrentUser = FirebaseAuth.getInstance().currentUser!!
        mStorageReference = FirebaseStorage.getInstance().reference

        uid = mCurrentUser.uid

        mDatabaseReference = FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(uid)

        mDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                var displayName = snapshot.child("display_name").value
                var image = snapshot.child("image").value.toString()
                var thumbImage = snapshot.child("thumb_img").value
                var status = snapshot.child("status").value

                etNameSettings.text = displayName.toString()
                etStatusSettings.text = status.toString()

                if(!image.equals("defalt"))
                {
                    Picasso.get().load(image).placeholder(R.drawable.lion)
                        .into(userImage)
                }


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        btnChangeStatus.setOnClickListener {

            var intent = Intent(this, StatusActivity::class.java)
            intent.putExtra("status", etStatusSettings.text.toString().trim())
            startActivity(intent)

        }

        btnChangePic.setOnClickListener {

            var galleryIntent = Intent()
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            galleryIntent.type = "image/*"
            startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"), GALLERY_ID)

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GALLERY_ID && resultCode == Activity.RESULT_OK)
        {
            var imageUri : Uri? = data!!.data

            CropImage.activity(imageUri)
                .setAspectRatio(1,1)
                .start(this)
        }

//        Structural equality (a check for equals()) -> ==
//Referential equality (two references point to the same object) -> ===
        // type nd result -> ===

        if(requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            val result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK)
            {
                val resultUri = result.uri
                var thumbFile = File(resultUri.path)

                var byteArray = ByteArrayOutputStream()

              GlobalScope.launch {

                  /*var thumbBitmap =*/ Compressor.compress(this@SettingsActivity, thumbFile) {

                      quality(80)
                      format(Bitmap.CompressFormat.JPEG)
                        byteArray
                  }

                var thumbByteArray : ByteArray
                thumbByteArray = byteArray.toByteArray()


                  //upload image to firebase
                  var filePath = mStorageReference!!.child("chat_profile_images")
                      .child(uid+".jpg")

                  //compressed image
                  var thumbFilePath = mStorageReference!!.child("chat_profile_images")
                      .child("thumbs")
                      .child(uid+".jpg")

                  filePath.putFile(resultUri)
                      .addOnCompleteListener {task: Task<UploadTask.TaskSnapshot> ->

                        if (task.isSuccessful)
                        {
                            var downloadUrl:String? = null
                            var downloadUrll = task.result?.metadata?.reference?.downloadUrl
                            downloadUrll?.addOnSuccessListener {
                                downloadUrl =  it.toString()
                            }

                            //upload Task
                            var uploadTask: UploadTask = thumbFilePath
                                .putBytes(thumbByteArray)

                            uploadTask.addOnCompleteListener{
                                task->
                                var thumbUrl:String? = null
                                var thumbUrll = task.result?.metadata?.reference?.downloadUrl
                                thumbUrll?.addOnSuccessListener {
                                   thumbUrl  = it.toString()
                                }

                                if (task.isSuccessful)
                                {
                                    var updateObj = HashMap<String,Any>()
                                    updateObj.put("image", downloadUrl!!)
                                    updateObj.put("thumb_image", thumbUrl!!)

                                    //save the profile image
                                    mDatabaseReference.updateChildren(updateObj)
                                        .addOnCompleteListener {
                                            task->

                                            if (task.isSuccessful)
                                            {
                                               Toast.makeText(this@SettingsActivity, "Image saved!",Toast.LENGTH_SHORT).show()

                                            }

                                        }

                                }
                            }
                        }


                      }

              }


            }

        }


    }
}