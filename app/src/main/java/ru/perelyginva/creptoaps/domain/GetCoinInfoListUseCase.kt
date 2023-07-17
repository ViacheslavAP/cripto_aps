package ru.perelyginva.creptoaps.domain

class GetCoinInfoListUseCase(
    private val repository: CoinRepository
) {

    operator fun invoke() = repository.getCoinInfoList()
}
