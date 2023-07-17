package ru.perelyginva.creptoaps.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ru.perelyginva.creptoaps.data.database.AppDatabase
import ru.perelyginva.creptoaps.data.mapper.CoinMapper
import ru.perelyginva.creptoaps.data.network.ApiFactory
import ru.perelyginva.creptoaps.domain.CoinInfo
import ru.perelyginva.creptoaps.domain.CoinRepository
import kotlinx.coroutines.delay

class CoinRepositoryImpl(
    private val application: Application) : CoinRepository {

    private val coinInfoDao = AppDatabase.getInstance(application).coinPriceInfoDao()
    private val apiService = ApiFactory.apiService

    private val mapper = CoinMapper()

    override fun getCoinInfoList(): LiveData<List<CoinInfo>> {
        return Transformations.map(coinInfoDao.getPriceList()) {
            it.map {
                mapper.mapDbModelToEntity(it)
            }
        }
    }

    override fun getCoinInfo(fromSymbol: String): LiveData<CoinInfo> {
        return Transformations.map(coinInfoDao.getPriceInfoAboutCoin(fromSymbol)) {
            mapper.mapDbModelToEntity(it)
        }
    }

    override suspend fun loadData() {
        while (true) {
            try {
                val topCoins = apiService.getTopCoinsInfo(limit = 50)
                val fSyms = mapper.mapNamesListToString(topCoins)
                val jsonContainer = apiService.getFullPriceList(fSyms = fSyms)
                val coinInfoDtoList = mapper.mapJsonContainerToListCoinInfo(jsonContainer)
                val dbModelList = coinInfoDtoList.map { mapper.mapDtoToDbModel(it) }
                coinInfoDao.insertPriceList(dbModelList)
            } catch (e: Exception) {
            }
            delay(10000)
        }
    }
}
