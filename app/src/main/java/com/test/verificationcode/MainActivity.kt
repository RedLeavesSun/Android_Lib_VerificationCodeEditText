package com.test.verificationcode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.lib.verification.code.VerificationCodeEditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lineEdit = findViewById<VerificationCodeEditText>(R.id.rect_edit)
        lineEdit.textChangedListener = object : VerificationCodeEditText.TextChangedListener {
            /**
             * 输入/删除监听
             *
             * @param changeText 输入/删除的字符
             */
            override fun textChanged(changeText: CharSequence?) {
            }

            /**
             * 输入完成
             * @param text 输入的字符
             */
            override fun textCompleted(text: CharSequence?) {
                Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
            }

        }
    }
}
