package com.lelestargazer.qurban_ticketing_system.member_management.ui.import_export

import android.net.Uri

sealed class ImportExportEvent {
    data class CreateMembersByExcel(val uri: Uri) : ImportExportEvent()
}