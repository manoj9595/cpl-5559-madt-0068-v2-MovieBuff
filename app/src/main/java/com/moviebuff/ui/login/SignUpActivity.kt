package com.moviebuff.ui.login

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.moviebuff.MainActivity
import com.moviebuff.R
import com.moviebuff.data.model.User
import com.moviebuff.data.repositories.AppPreferencesHelper
import com.moviebuff.db.AppDataBase
import com.moviebuff.utils.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

const val PERMISSIONS_REQUEST = 222
const val INTENT_CAPTURE = 231
class SignUpActivity : AppCompatActivity() {

    var selectedDate : String?=null

    var user : User?=null
    private var pictureFilePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        user = intent.getSerializableExtra("user") as? User
        setToolbar()

        if(user!=null){
            initData()
        }




    }

    private fun initData() {
        etFirstName.setText(user?.firstName)
        etLastName.setText(user?.lastName)
        etContact.setText(user?.contact)
        etEmailId.setText(user?.emailId)
        selectedDate = user?.dob

        supportActionBar!!.title="Profile"


        etEmailId.isEnabled = false

        inputLayoutPassword.setGone()
        bDob.text = user?.dob

        pictureFilePath = user?.profilePic


        Glide.with(ivUserImage)
            .load(user?.profilePic)
            .into(ivUserImage)



    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title="Sign Up"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        supportActionBar!!.setDisplayShowHomeEnabled(true);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    fun onSaveClicked(view: View) {

        if(user==null){
            saveUser()
        }else {
            updateUser()
        }



    }

    private fun updateUser() {
        val firstName = etFirstName.text.toString().trim()
        val lastName = etLastName.text.toString().trim()
        val contact = etContact.text.toString().trim()
        val emailId = etEmailId.text.toString().trim()

        if(firstName.isEmpty()){
            toast("Enter First Name")
            return
        }

        if(lastName.isEmpty()) {toast("Enter Last Name"); return}
        if(contact.length<10) {toast("Enter Contact"); return}
        if(emailId.isEmpty()) {toast("Enter Email Id"); return}
        if(!emailId.isValidEmail()) {toast("Enter Valid Email Id"); return}
        if(selectedDate.isNullOrEmpty()) {toast("Select Date Of Birth"); return}
        if(pictureFilePath.isNullOrEmpty()){toast("Select Profile Image"); return}



        Coroutines.io {
            AppDataBase.getAppDataBase(this)?.userDao()?.updateUser(firstName,lastName,contact,emailId,
                selectedDate!!,
                pictureFilePath!!,user?.id!!)
            Coroutines.main {
                finish()
            }

        }

    }

    private fun saveUser() {
        val firstName = etFirstName.text.toString().trim()
        val lastName = etLastName.text.toString().trim()
        val contact = etContact.text.toString().trim()
        val emailId = etEmailId.text.toString().trim()
        val pass = etPassword.text.toString().trim()


        if(firstName.isEmpty()){
            toast("Enter First Name")
            return
        }

        if(lastName.isEmpty()) {toast("Enter Last Name"); return}
        if(contact.length<10) {toast("Enter Contact"); return}
        if(emailId.isEmpty()) {toast("Enter Email Id"); return}
        if(!emailId.isValidEmail()) {toast("Enter Valid Email Id"); return}
        if(pass.length<8) {toast("Enter Password"); return}
        if(selectedDate.isNullOrEmpty()) {toast("Select Date Of Birth"); return}
        if(pictureFilePath.isNullOrEmpty()){toast("Select Profile Image"); return}

        val user = User(firstName = firstName,
            lastName = lastName,
            contact = contact,
            emailId = emailId,
            dob = selectedDate!!,
            pass = pass.md5(),
            profilePic = pictureFilePath)


        Coroutines.io {
            AppDataBase.getAppDataBase(this)?.userDao()?.addUser(user)
            AppPreferencesHelper(this).setEmailId(emailId)
            Coroutines.main {
                startActivity(Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
            }

        }
    }

    fun onDobClicked(view: View) {
            val c: Calendar = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    var sdf = SimpleDateFormat("yyyy-MM-dd")
                    val calendar = Calendar.getInstance()
                    calendar.set(year, monthOfYear, dayOfMonth)
                    val formattedDate = sdf.format(calendar.time)

                    selectedDate = formattedDate
                    bDob.text = selectedDate

                },
                mYear,
                mMonth,
                mDay
            ).apply {
                this.datePicker.maxDate = c.timeInMillis
            }
            datePickerDialog.show()
    }

    fun onImageSelectorClicked(view: View) {
        if(checkPermissions()){
            captureCameraImage()
        }
    }
    private fun checkPermissions() :Boolean{
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat
                .checkSelfPermission(this,
                    Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {


                ActivityCompat.requestPermissions(this, arrayOf(
                    Manifest.permission
                        .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                    PERMISSIONS_REQUEST
                )
        } else {
            return true
        }
        return false
    }
    fun captureCameraImage(){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, true)
        if (cameraIntent.resolveActivity(packageManager) != null) {
            var pictureFile: File? = null
            try {
                pictureFile = Utils.getPictureFile(this)
            } catch (ex: IOException) {
                Toast.makeText(this, "Photo file can't be created, please try again", Toast.LENGTH_SHORT).show()
                return
            }
            if (pictureFile != null) {
                pictureFilePath = pictureFile.absolutePath
                val photoURI = FileProvider.getUriForFile(this, "com.moviebuff.fileprovider", pictureFile
                )
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(cameraIntent,
                    INTENT_CAPTURE
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            INTENT_CAPTURE -> if (resultCode == Activity.RESULT_OK) {
                pictureFilePath?.let {
                    Glide.with(ivUserImage)
                        .load(it)
                        .into(ivUserImage)
                }
            }
        }
    }


}