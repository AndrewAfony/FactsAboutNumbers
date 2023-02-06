package andrewafony.factsaboutnumbers.com.random

import andrewafony.factsaboutnumbers.com.numbers.domain.RandomNumberRepository
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class PeriodicRandomWorker(
    context: Context,
    workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) { // todo check WorkMananger factory

    override suspend fun doWork(): Result {

        return try {
            val repository = (applicationContext as ProvidePeriodicRepository).providePeriodicRepository()
            repository.randomNumberFact()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

interface ProvidePeriodicRepository {

    fun providePeriodicRepository(): RandomNumberRepository
}