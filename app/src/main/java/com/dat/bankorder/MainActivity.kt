package com.dat.bankorder

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val listMessage = mutableListOf<SmsModels>()

    private val listMessageAdapter: ListMessageAdapter by lazy {
        ListMessageAdapter(context = this, dataSource = listMessage)

    }
    private val isReadMessagePermissionGranted
        get() = this.hasPermission(Manifest.permission.READ_SMS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }


    private fun initView() {
        list_message.adapter = listMessageAdapter
        bt_get_message.setOnClickListener {
            startGetSms(null)
        }
        bt_search.setOnClickListener {
            startGetSms(edt_search.text.toString())
        }
    }

    private fun String.removeAllWhiteSpace(): String {
        return this.replace("\\s".toRegex(), "")
    }

    private fun Context.hasPermission(permissionType: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permissionType) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun startGetSms(params: String?) {
        if (!isReadMessagePermissionGranted) {
            requestReadSMSPermissions()
        } else {
            listMessage.clear()
            listMessageAdapter.notifyDataSetChanged()
            val lst: List<SmsModels>? = getAllSms()
            val result: List<SmsModels>? = if (params != null) lst?.filter { smsModels ->
                (smsModels.address!!.removeAllWhiteSpace()
                    .contains(params.removeAllWhiteSpace(),ignoreCase = true) || smsModels.msg!!.contains(
                    params.removeAllWhiteSpace(),ignoreCase = true
                ))
            } else lst
            listMessage.addAll(result as Collection<SmsModels>)
            listMessageAdapter.notifyDataSetChanged()
            Log.d("TAG", listMessage.toString())
            tv_noti.visibility = if (listMessage.isNotEmpty()) View.INVISIBLE else View.VISIBLE
        }
    }


    fun String.convertDate(): String? {
        return DateFormat.format(" hh:mm dd/MM/yyyy", this.toLong()).toString()
    }

    private fun getAllSms(): List<SmsModels>? {
        val lstSms: MutableList<SmsModels> = ArrayList()
        val message: Uri = Uri.parse(INBOX)
        val cr: ContentResolver = this.contentResolver
        val c: Cursor? = cr.query(message, null, null, null, null)
        this.startManagingCursor(c)
        val totalSMS: Int? = c?.count
        if (c?.moveToFirst() == true) {
            for (i in 0 until totalSMS!!) {
                val objSms = SmsModels()
                objSms.id = c.getString(c.getColumnIndexOrThrow("_id"))
                objSms.address = (c.getString(
                    c.getColumnIndexOrThrow("address")
                ))
                objSms.msg = (c.getString(c.getColumnIndexOrThrow("body")))
                objSms.readState = (c.getString(c.getColumnIndex("read")))
                objSms.time = (c.getString(c.getColumnIndexOrThrow("date"))).convertDate()
                lstSms.add(objSms)
                c.moveToNext()
            }
        }
        this.stopManagingCursor(c)
        return lstSms
    }

    private fun requestReadSMSPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_SMS),
            REQUEST_ID_MULTIPLE_PERMISSIONS
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                if (grantResults.firstOrNull() != PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "C???p quy???n ?????c tin nh???n th??nh c??ng", Toast.LENGTH_SHORT)
                } else {
//                    startGetSms(edt_search.text.toString())
                }
            }
        }
    }


    companion object {
        const val INBOX: String = "content://sms/inbox"
        const val TAG: String = "MainActivity"
        const val REQUEST_ID_MULTIPLE_PERMISSIONS: Int = 1111
    }
}