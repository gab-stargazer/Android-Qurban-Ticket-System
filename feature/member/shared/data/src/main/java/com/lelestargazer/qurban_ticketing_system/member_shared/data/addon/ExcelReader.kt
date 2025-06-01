package com.lelestargazer.qurban_ticketing_system.member_shared.data.addon

import android.content.Context
import android.net.Uri
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.MemberEntity
import io.retable.ExcelReadOptions
import io.retable.Retable
import java.io.File
import java.net.URI
import java.util.UUID

class ExcelReader(
    private val context: Context,
) {

    fun importMemberFromExcel(uri: Uri): List<MemberEntity> {

        val entities = mutableListOf<MemberEntity>()


        getFileUriFromContentUri(uri)?.let {
            File(it).inputStream().use {
                val hello = Retable.excel(
                    options = ExcelReadOptions(
                        trimValues = true,
                        ignoreEmptyLines = true,
                        firstRecordAsHeader = true
                    )
                ).read(it)

                entities.addAll(hello.records.map { record ->
                    MemberEntity(
                        id = UUID.randomUUID(),
                        name = record["Nama"].orEmpty(),
                        phone = null,
                        address = "",
                        rt = 0,
                        rw = 0,
                        description = "",
                        isParticipant = record["Status"] == "Peserta",
                        isActive = true
                    )
                }.toList())
            }
        }

        return entities
    }

    fun getFileUriFromContentUri(contentUri: Uri): URI? {
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
}