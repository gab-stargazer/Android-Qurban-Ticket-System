package com.lelestargazer.qurban_ticketing_system.member_shared.data.addon

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.crispinlab.Snowflake
import com.lelestargazer.qurban_ticketing_system.member_shared.common.error.DataCorruptException
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.MemberEntity
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.QurbanStatus
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.QurbanType
import io.retable.Retable
import io.retable.RetableColumns
import org.koin.core.annotation.Single
import java.io.File
import java.net.URI

@Single
class Excel(
    private val context: Context,
) {

    fun exportMemberToExcel(member: List<MemberEntity>) {
        val documentsDir = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val file = File(documentsDir, "daftar-qurban.xlsx")
        val columns = object : RetableColumns() {
            val id = string(ID)
            val name = string(NAME)
            val address = string(ADDRESS)
            val phone = string(PHONE)
            val status = string(STATUS)
            val type = string(TYPE)
        }

//        TODO: Require further investigation regarding APACHE POI capabilities not being able to export into Excel
//
//        Retable(columns)
//            .data(
//                values = member
//            ) {
//
//                mapOf(
//                    id to it.id,
//                    name to it.name,
//                    address to it.address,
//                    rt to it.rt,
//                    rw to it.rw,
//                    phone to it.phone.orEmpty(),
//                    status to if (it.isParticipant) "Peserta" else "Penerima",
//                    type to if (it.isParticipant) {
//                        if (it.isCow == true) {
//                            "Sapi"
//                        } else {
//                            "Kambing"
//                        }
//                    } else ""
//                )
//            }
//            .write(Retable.excel(columns) to file.outputStream())
    }

    fun importMemberFromExcel(uri: Uri): List<MemberEntity> {

        val entities = mutableListOf<MemberEntity>()

        getFileUriFromContentUri(uri)?.let { validUri ->
            File(validUri).inputStream().use { fis ->
                val table = Retable
                    .excel()
                    .read(fis)

                table.columns.apply {
                    entities.addAll(
                        table.records.map { record ->
                            MemberEntity(
                                id = record[ID].orEmpty().toLongOrNull() ?: Snowflake.create()
                                    .nextId(),
                                name = record[NAME] ?: throw DataCorruptException(),
                                phone = record[PHONE]
                                    ?.ifEmpty { null },
                                address = record[ADDRESS]
                                    ?.ifEmpty { null },
                                status =
                                    when (record[STATUS]) {
                                        PESERTA -> QurbanStatus.Participant
                                        else -> QurbanStatus.Recipient
                                    },
                                type =
                                    when (record[TYPE]) {
                                        COW -> QurbanType.Cow
                                        GOAT -> QurbanType.Goat
                                        SHEEP -> QurbanType.Sheep
                                        else -> null
                                    }
                            )
                        }.toList()
                    )
                }
            }
        }

        return entities
    }

    private fun getFileUriFromContentUri(contentUri: Uri): URI? {
        return try {
            // Query the ContentResolver to get the file path
            context.contentResolver.query(contentUri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val filePath = getFilePathFromCursor(contentUri)
                    if (filePath != null) {
                        File(filePath).toURI() // Convert to java.net.URI
                    } else {
                        null
                    }
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getFilePathFromCursor(uri: Uri): String? {
        // This is a simplified example; actual implementation depends on the URI
        // For documents, you may need to copy the file to a temporary location
        return when (uri.scheme) {
            "content" -> {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    // Copy to a temporary file
                    val tempFile = File.createTempFile("temp", null, context.cacheDir)
                    tempFile.outputStream().use { output ->
                        inputStream.copyTo(output)
                    }
                    tempFile.absolutePath
                }
            }

            else -> null
        }
    }

    companion object {
        private const val ID = "id"
        private const val NAME = "Nama"
        private const val ADDRESS = "Alamat"
        private const val PHONE = "No. Telepon"
        private const val TYPE = "Jenis"
        private const val STATUS = "Status"
        private const val COW = "Sapi"
        private const val GOAT = "Kambing"
        private const val SHEEP = "Domba"
        private const val PESERTA = "Peserta"
    }
}