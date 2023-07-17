package ru.perelyginva.creptoaps.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.perelyginva.creptoaps.data.network.model.CoinNameDto

data class CoinNameContainerDto (
    @SerializedName("CoinInfo")
    @Expose
    val coinName: CoinNameDto? = null
)
