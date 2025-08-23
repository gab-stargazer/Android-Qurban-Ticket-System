package com.lelestargazer.qurban_ticketing_system.member_shared.data.addon

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.lelestargazer.qurban_ticketing_system.member_shared.common.error.DataCorruptException
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.MemberEntity
import io.retable.Retable
import io.retable.RetableColumns
import java.io.File
import java.net.URI
import java.util.UUID.fromString
import java.util.UUID.randomUUID

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
            val rt = int(RT)
            val rw = int(RW)
            val phone = string(PHONE)
            val status = string(STATUS)
            val type = string(TYPE)
        }

        Retable(columns)
            .data(
                values = member
            ) {

                mapOf(
                    id to it.id,
                    name to it.name,
                    address to it.address,
                    rt to it.rt,
                    rw to it.rw,
                    phone to it.phone.orEmpty(),
                    status to if (it.isParticipant) "Peserta" else "Penerima",
                    type to if (it.isParticipant) {
                        if (it.isCow == true) {
                            "Sapi"
                        } else {
                            "Kambing"
                        }
                    } else ""
                )
            }
            .write(Retable.excel(columns) to file.outputStream())
    }

    fun importMemberFromExcel(uri: Uri): List<MemberEntity> {

        val entities = mutableListOf<MemberEntity>()
        getFileUriFromContentUri(uri)?.let { uri_ ->
            File(uri_).inputStream().use { fis ->
                val table = Retable
                    .excel()
                    .read(fis)

                table.columns.apply {
                    entities.addAll(
                        table.records.map { record ->

                            MemberEntity(
                                id = record[ID].orEmpty()
                                    .run {
                                        if (isNotBlank()) {
                                            fromString(this)
                                        } else randomUUID()
                                    },
                                name = record[NAME] ?: throw DataCorruptException(),
                                phone = record[PHONE],
                                address = record[ADDRESS].orEmpty(),
                                rt = record[RT]?.toIntOrNull() ?: 0,
                                rw = record[RW]?.toIntOrNull() ?: 0,
                                description = record[DESCRIPTION].orEmpty(),
                                isParticipant = record[STATUS] == PESERTA,
                                isCow =
                                    if (record[STATUS] == PESERTA) {
                                        record[TYPE] == COW
                                    } else null,
                                isActive = true
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
        private const val NAME = "nama"
        private const val ADDRESS = "alamat"
        private const val STATUS = "status"
        private const val DESCRIPTION = "deskripsi"
        private const val PHONE = "nomor"
        private const val RT = "rt"
        private const val RW = "rw"
        private const val TYPE = "jenis"
        private const val COW = "Sapi"
        private const val PESERTA = "Peserta"
    }
}