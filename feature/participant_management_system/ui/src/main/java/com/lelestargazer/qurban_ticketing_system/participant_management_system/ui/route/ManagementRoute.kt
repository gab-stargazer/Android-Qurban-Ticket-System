package com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.route

import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.Participant
import kotlinx.serialization.Serializable

@Serializable
sealed class ManagementRoute {

    @Serializable
    data object ParticipantManagement : ManagementRoute()

    @Serializable
    data class ParticipantAddOrEdit(
        val type: ParticipantAddOrEditType,
        val participant: Participant?
    ) : ManagementRoute() {

        enum class ParticipantAddOrEditType {
            ADD, EDIT
        }
    }
}