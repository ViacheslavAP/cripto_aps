package ru.perelyginva.creptoaps.domain

class LoadDataUseCase(
    private val repository: CoinRepository
) {

     operator fun invoke() = repository.loadData()
}
