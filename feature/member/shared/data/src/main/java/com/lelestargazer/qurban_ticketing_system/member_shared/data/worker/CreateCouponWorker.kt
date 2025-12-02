package com.lelestargazer.qurban_ticketing_system.member_shared.data.worker

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lelestargazer.qurban_ticketing_system.common.Constant.CHANNEL_DESCRIPTION
import com.lelestargazer.qurban_ticketing_system.common.Constant.CHANNEL_ID
import com.lelestargazer.qurban_ticketing_system.common.Constant.CHANNEL_NAME
import com.lelestargazer.qurban_ticketing_system.member_shared.data.addon.QRGenerator
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.repository.MemberRepository
import org.koin.java.KoinJavaComponent.inject
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant

class CreateCouponWorker(
    context: Context,
    parameters: WorkerParameters
) : CoroutineWorker(context, parameters) {

    private val qrGenerator by inject<QRGenerator>(QRGenerator::class.java)
    private val memberRepository by inject<MemberRepository>(MemberRepository::class.java)

    @OptIn(ExperimentalTime::class)
    override suspend fun doWork(): Result {
        return try {
            val location: String = inputData
                .getString(KEY_LOCATION) ?: error("Location can't be fetched")

            val pickupDate = inputData.getLong(KEY_DATE, 0)

            val time = ZonedDateTime.ofInstant(
                Instant.fromEpochMilliseconds(pickupDate).toJavaInstant(),
                ZoneId.systemDefault()
            )
            val dateTimeFormatter =
                DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.forLanguageTag("id"))

            qrGenerator.saveCoupons(
                location = location,
                time = dateTimeFormatter.format(time),
                isQrEnabled = false,
                qrDataList = memberRepository.selectAllMembers().map {
                    QRGenerator.QRGeneratorData(
                        qrCode = "",
                        couponStatus = "",
                        couponName = it.name,
                        qurbanStatus = it.status,
                        qurbanType = it.type
                    )
                }
            )

            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESCRIPTION
            }

            val notificationManager: NotificationManager = applicationContext
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)

            if (
                ContextCompat.checkSelfPermission(
                    applicationContext,
                    POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {

            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    companion object {
        const val KEY_LOCATION = "key_location"
        const val KEY_DATE = "key_date"
    }
}