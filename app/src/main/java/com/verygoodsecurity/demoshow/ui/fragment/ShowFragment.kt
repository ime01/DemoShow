package com.verygoodsecurity.demoshow.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.verygoodsecurity.demoshow.R
import com.verygoodsecurity.demoshow.ui.MainActivity
import com.verygoodsecurity.demoshow.ui.activity.CollectAndShowActivity
import com.verygoodsecurity.demoshow.ui.activity.OnCardAliasChangeListener
import com.verygoodsecurity.demoshow.utils.extension.setVisible
import com.verygoodsecurity.vgsshow.VGSShow
import com.verygoodsecurity.vgsshow.core.listener.VGSOnResponseListener
import com.verygoodsecurity.vgsshow.core.network.client.VGSHttpMethod
import com.verygoodsecurity.vgsshow.core.network.model.VGSRequest
import com.verygoodsecurity.vgsshow.core.network.model.VGSResponse
import com.verygoodsecurity.vgsshow.widget.VGSTextView
import kotlinx.android.synthetic.main.show_layout.*

class ShowFragment : Fragment(R.layout.show_layout), OnCardAliasChangeListener {

    private val show: VGSShow by lazy {
        VGSShow.Builder(requireContext(), MainActivity.TENANT_ID)
            //.setHostname(MainActivity.COLLECT_CUSTOM_HOSTNAME)
            .build()
    }

    var cardNumberAlias: String = "tok_sandbox_uo5G4dyX5n5ekNVnoM29Rq"
    private var expirationDateAlias: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupShow()
    }

    override fun onAliasChange(cardNumberAlias: String, expirationDateAlias: String) {
        this.cardNumberAlias = cardNumberAlias
        this.expirationDateAlias = expirationDateAlias
    }

    @SuppressLint("SetTextI18n")
    private fun setupShow() {
        show.addOnResponseListener(object : VGSOnResponseListener {

            override fun onResponse(response: VGSResponse) {
                pbReveal?.setVisible(true)
                Log.d(CollectAndShowActivity::class.simpleName, response.toString())
            }
        })
        show.subscribe(tvCardNumber)
        show.subscribe(tvCardExpiration)

        tvCardNumber?.addTransformationRegex(
            "(\\d{4})(\\d{4})(\\d{4})(\\d{4})".toRegex(),
            "\$1-\$2-\$3-\$4"
        )
        tvCardNumber?.addTransformationRegex("-".toRegex(), " - ")


        tvCardNumber?.setOnTextChangeListener(object :
            VGSTextView.OnTextChangedListener {
            override fun onTextChange(view: VGSTextView, isEmpty: Boolean) {
                Log.d(MainActivity::class.simpleName, "textIsEmpty: $isEmpty")
            }
        })
        tvCardNumber?.addOnCopyTextListener(object : VGSTextView.OnTextCopyListener {

            override fun onTextCopied(view: VGSTextView, format: VGSTextView.CopyTextFormat) {
                Toast.makeText(
                    requireContext().applicationContext,
                    "Number text copied! format: $format",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        tvCardNumber?.setOnClickListener {
            tvCardNumber.copyToClipboard(VGSTextView.CopyTextFormat.RAW)
        }
        mbRequest?.setOnClickListener {
            println("REVEAL BUTTON CLICKED")
            Log.d("CLICKED", "REVEAL BUTTON CLICKED")
            pbReveal?.setVisible(true)
            show.requestAsync(
                VGSRequest.Builder("post", VGSHttpMethod.POST).body(
                    mapOf(
                        "payment_card_number" to cardNumberAlias,
                       // "tok_sandbox_uo5G4dyX5n5ekNVnoM29Rq" to cardNumberAlias,
                        "payment_card_expiration_date" to expirationDateAlias
                    )
                ).build()
            )
        }
        mbSetSecureText?.setOnClickListener {
            if (tvCardNumber?.isSecureText == true) {
                tvCardNumber?.isSecureText = false
                mbSetSecureText?.text = "Set secure"
            } else {
                tvCardNumber?.isSecureText = true
                mbSetSecureText?.text = "Reset secure"
            }
        }
    }
}