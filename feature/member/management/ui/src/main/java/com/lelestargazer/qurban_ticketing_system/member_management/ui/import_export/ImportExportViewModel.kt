package com.lelestargazer.qurban_ticketing_system.member_management.ui.import_export

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.repository.MemberRepository
import kotlinx.coroutines.launch

class ImportExportViewModel(
    private val navController: NavController,
    private val memberRepository: MemberRepository,
) : ViewModel() {

    fun onEvent(event: ImportExportEvent) = viewModelScope.launch {
        when (event) {
            is ImportExportEvent.CreateMembersByExcel -> memberRepository.createMembersByExcel(event.uri)
        }
    }
}