package com.francisumeanozie.baymax.data.responses

import com.google.gson.annotations.SerializedName
import kotlin.reflect.jvm.internal.pcollections.HashPMap

data class CurrencyResponse (

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("terms")
    val terms: String,

    @SerializedName("privacy")
    val privacy: String,

    @SerializedName("currencies")
    val currencies: HashMap<String, String>
)
