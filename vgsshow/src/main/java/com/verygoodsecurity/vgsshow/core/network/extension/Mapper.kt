package com.verygoodsecurity.vgsshow.core.network.extension

import com.verygoodsecurity.vgsshow.core.exception.VGSException
import com.verygoodsecurity.vgsshow.core.network.client.VGSHttpBodyFormat
import com.verygoodsecurity.vgsshow.core.network.client.model.HttpRequest
import com.verygoodsecurity.vgsshow.core.network.client.model.HttpResponse
import com.verygoodsecurity.vgsshow.core.network.model.VGSRequest
import com.verygoodsecurity.vgsshow.core.network.model.VGSResponse
import com.verygoodsecurity.vgsshow.util.extension.plus
import okhttp3.Response
import org.json.JSONObject

internal fun VGSRequest.toHttpRequest(extraHeaders: Map<String, String>?) = HttpRequest(
    this.path,
    this.method,
    this.headers + extraHeaders,
    this.payload?.getData(),
    this.requestFormat
)

private const val APPLICATION_JSON = "application/json"

internal fun VGSHttpBodyFormat.toContentType() = when (this) {
    VGSHttpBodyFormat.JSON -> APPLICATION_JSON
}

internal fun Response.toHttpResponse() = HttpResponse(
    this.code,
    this.isSuccessful,
    this.message,
    this.body?.string()
)

internal fun VGSException.toVGSResponse() = VGSResponse.Error.create(this)

internal fun Map<String, Any>.toJsonByteArray(): ByteArray? {
    return try {
        JSONObject(this).toString().toByteArray(Charsets.UTF_8)
    } catch (e: Exception) {
        null
    }
}