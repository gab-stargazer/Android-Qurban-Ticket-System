package com.lelestargazer.qurban_ticketing_system.member_shared.data.worker

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lelestargazer.qurban_ticketing_system.member_shared.data.addon.Excel
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.MemberEntity
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.toDomain
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.repository.MemberRepository
import org.koin.android.annotation.KoinWorker
import org.koin.java.KoinJavaComponent.inject

@KoinWorker
class ImportDataWorker(
    context: Context,
    parameters: WorkerParameters
) : CoroutineWorker(context, parameters) {

    private val excelReader: Excel by inject(clazz = Excel::class.java)
    private val repository by inject<MemberRepository>(clazz = MemberRepository::class.java)

    override suspend fun doWork(): Result {
        val input: String = inputData.getString(INPUT_DATA_URL)
            ?: return Result.failure()
        val memberData = excelReader.importMemberFromExcel(input.toUri())
        repository.importMember(memberData.map(MemberEntity::toDomain))

        if (
            ContextCompat.checkSelfPermission(
                applicationContext,
                POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {

        }

        return Result.success()
    }

    companion object {
        const val INPUT_DATA_URL = "input_url"
    }
}