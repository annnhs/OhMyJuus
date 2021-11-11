package com.lx.ohmyjuus.jubging
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
//import com.example.jubging.webapp.MainActivity
import com.lx.ohmyjuus.MainActivity
import com.lx.ohmyjuus.jubging.JubgingActivity
import com.lx.ohmyjuus.jubging.MyUtils

class myDialogFragment(private val listener: OnClickDialogListener): DialogFragment() {

    interface OnClickDialogListener {
        fun onClickPositive(address: String)
        fun onClickNegative()
    }

    private var jubging: JubgingActivity? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("줍깅을 종료할까요?")
                .setPositiveButton("예") { dialog, id ->
                    MyUtils.stopJubging()



                    //jubging?.mOnCaptureClick()//스크린샷?

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