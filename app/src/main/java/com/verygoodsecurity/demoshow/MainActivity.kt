package com.verygoodsecurity.demoshow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgsshow.VGSShow
import com.verygoodsecurity.vgsshow.core.Environment
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val showVgs: VGSShow by lazy {
        VGSShow("tntaq8uft80", Environment.SANDBOX)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        test?.setOnClickListener {
            thread(start = true) {
                showVgs.request("card_number_3", "tok_sandbox_nj9DWPJMFaP8U3HqXQ2DE")
            }
        }
    }
}