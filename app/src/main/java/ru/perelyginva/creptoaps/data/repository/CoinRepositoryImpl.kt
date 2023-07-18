package ru.perelyginva.creptoaps.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import ru.perelyginva.creptoaps.data.database.AppDatabase
import ru.perelyginva.creptoaps.data.mapper.CoinMapper
import ru.perelyginva.creptoaps.domain.CoinInfo
import ru.perelyginva.creptoaps.domain.CoinRepository
import ru.perelyginva.creptoaps.workers.RefreshDataWorker.Companion.NAME
import ru.perelyginva.creptoaps.workers.RefreshDataWorker.Companion.makeRequest

class CoinRepositoryImpl(
    private val application: Application,
) : CoinRepository {

    private val coinInfoDao = AppDatabase.getInstance(application).coinPriceInfoDao()
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

    override fun loadData() {
        val workManager = WorkManager.getInstance(application)
        workManager.enqueueUniqueWork(
            NAME,
        ExistingWorkPolicy.REPLACE,
        makeRequest()
        )
    }
}
