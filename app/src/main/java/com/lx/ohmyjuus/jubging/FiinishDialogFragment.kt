package com.lx.ohmyjuus.jubging

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.lx.ohmyjuus.MainActivity

class FiinishDialogFragment(private val listener: OnClickDialogListener): DialogFragment() {

    interface OnClickDialogListener {
        fun onClickPositive(address: String)
        fun onClickNegative()
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("줍깅을 종료할까요?")
                .setPositiveButton("예") { dialog, id ->
                    MyUtils.stopJubging()

                    MyUtils.capture()

                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)


                }
                .setNegativeButton("아니요") { dialog, id ->
                    listener.onClickNegative()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}