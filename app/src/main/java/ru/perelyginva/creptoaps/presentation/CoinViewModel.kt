package ru.perelyginva.creptoaps.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ru.perelyginva.creptoaps.data.repository.CoinRepositoryImpl
import ru.perelyginva.creptoaps.domain.GetCoinInfoListUseCase
import ru.perelyginva.creptoaps.domain.GetCoinInfoUseCase
import ru.perelyginva.creptoaps.domain.LoadDataUseCase
import kotlinx.coroutines.launch

class CoinViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CoinRepositoryImpl(application)

    private val getCoinInfoListUseCase = GetCoinInfoListUseCase(repository)
    private val getCoinInfoUseCase = GetCoinInfoUseCase(repository)
    private val loadDataUseCase = LoadDataUseCase(repository)

    val coinInfoList = getCoinInfoListUseCase()

    fun getDetailInfo(fSym: String) = getCoinInfoUseCase(fSym)

    init {
        viewModelScope.launch {
            loadDataUseCase()
        }
    }
}
