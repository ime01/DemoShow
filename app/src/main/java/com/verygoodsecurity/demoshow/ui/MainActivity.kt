package com.verygoodsecurity.demoshow.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.demoshow.R
import com.verygoodsecurity.demoshow.ui.activity.CollectAndShowActivity
import com.verygoodsecurity.demoshow.ui.activity.CollectAndShowFragmentActivity
import com.verygoodsecurity.demoshow.ui.activity.PDFActivity
import com.verygoodsecurity.demoshow.ui.activity.VGSShowViewPagerActivity
import com.verygoodsecurity.vgsshow.VGSShow
import com.verygoodsecurity.vgsshow.core.VGSEnvironment
import com.verygoodsecurity.vgsshow.core.listener.VGSOnResponseListener
import com.verygoodsecurity.vgsshow.core.network.client.VGSHttpMethod
import com.verygoodsecurity.vgsshow.core.network.model.VGSResponse
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var vgsShow: VGSShow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        //initListeners()


        vgsShow = VGSShow(this, "tnto06ue3ir", VGSEnvironment.Sandbox())
        vgsShow.subscribe(infoField)
        vgsShow.subscribe(infoField1)

        vgsShow.addOnResponseListener(object : VGSOnResponseListener {

            override fun onResponse(response: VGSResponse) {
                when (response) {
                    is VGSResponse.Success -> {
                        val successCode = response.code
                        Log.d("VALUE", "$response")
                    }
                    is VGSResponse.Error -> {
                        val errorCode = response.code
                        val message = response.message
                    }
                }
            }
        })
        vgsShow.requestAsync("/post", VGSHttpMethod.POST, createRequestPayload())

    }



    private fun createRequestPayload(): Map<String, String> {

        return mapOf("pan" to "tok_sandbox_uo5G4dyX5n5ekNVnoM29Rq",
            "expireDate" to "tok_sandbox_rHDfg2fQS4CaqcnBLzpVjm")
    }


    private fun initListeners() {
        btnStartActivityMain?.setOnClickListener {
            startActivity(Intent(this, CollectAndShowActivity::class.java))
        }
        btnStartFragmentMain?.setOnClickListener {
            startActivity(Intent(this, CollectAndShowFragmentActivity::class.java))
        }
        btnStartViewPagerMain?.setOnClickListener {
            startActivity(Intent(this, VGSShowViewPagerActivity::class.java))
        }
        btnStartRevelPDF?.setOnClickListener {
            startActivity(Intent(this, PDFActivity::class.java))
        }
    }

    companion object {

      //  const val TENANT_ID = "tntpszqgikn"
        const val TENANT_ID = "tnto06ue3ir"
        const val ENVIRONMENT = "sandbox"
        const val COLLECT_CUSTOM_HOSTNAME = "collect-android-testing.verygoodsecurity.io/test"
    }
}

typealias ShowResponse = com.verygoodsecurity.vgsshow.core.network.model.VGSResponse
typealias CollectResponse = com.verygoodsecurity.vgscollect.core.model.network.VGSResponse?
typealias CollectSuccessResponse = com.verygoodsecurity.vgscollect.core.model.network.VGSResponse.SuccessResponse?
