package com.francisumeanozie.baymax.data.responses

import com.google.gson.annotations.SerializedName

data class ExchangeResponse(

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("terms")
    val terms: String,

    @SerializedName("privacy")
    val privacy: String,

    @SerializedName("timestamp")
    val timestamp: Long,

    @SerializedName("source")
    val source: String,

    @SerializedName("quotes")
    val quotes: HashMap<String, Float>
)
