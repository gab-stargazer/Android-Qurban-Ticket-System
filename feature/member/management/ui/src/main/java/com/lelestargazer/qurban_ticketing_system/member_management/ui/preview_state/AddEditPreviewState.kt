package com.lelestargazer.qurban_ticketing_system.member_management.ui.preview_state

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.lelestargazer.qurban_ticketing_system.member_management.ui.route.MemberAddEdit
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit.state_event.AddEditUiState
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.QurbanStatus
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.QurbanType

class AddEditPreviewState : PreviewParameterProvider<AddEditUiState> {
    override val values: Sequence<AddEditUiState>
        get() = sequenceOf(
            AddEditUiState(
                screenType = MemberAddEdit.ScreenType.ADD,
                name = "John",
                phoneNumber = "628xxxxxxxxxx",
                address = "Jl. Asia Afrika",
                qurbanStatus = QurbanStatus.Recipient
            ),
            AddEditUiState(
                screenType = MemberAddEdit.ScreenType.ADD,
                name = "John",
                phoneNumber = "628xxxxxxxxxx",
                address = "Jl. Asia Afrika",
                qurbanStatus = QurbanStatus.Participant
            ),
            AddEditUiState(
                screenType = MemberAddEdit.ScreenType.EDIT,
                name = "John",
                phoneNumber = "628xxxxxxxxxx",
                address = "Jl. Asia Afrika",
                qurbanStatus = QurbanStatus.Recipient,
                qurbanType = QurbanType.Sheep
            ),
        )
}