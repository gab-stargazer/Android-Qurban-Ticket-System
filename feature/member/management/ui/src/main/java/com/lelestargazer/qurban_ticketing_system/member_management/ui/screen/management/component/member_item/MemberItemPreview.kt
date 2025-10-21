package com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.component.member_item

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Member
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.QurbanStatus
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.QurbanType

class MemberItemPreview : PreviewParameterProvider<Member> {
    override val values: Sequence<Member>
        get() = sequenceOf(
            Member(
                id = 0,
                name = "Budi Arie",
                phoneNumber = null,
                address = null,
                status = QurbanStatus.Recipient,
                type = null
            ),
            Member(
                id = 0,
                name = "Budianto",
                phoneNumber = null,
                address = null,
                status = QurbanStatus.Participant,
                type = QurbanType.Goat
            ),
            Member(
                id = 0,
                name = "Gibran",
                phoneNumber = "0822xxxxxxxx",
                address = null,
                status = QurbanStatus.Recipient,
                type = null
            ),
            Member(
                id = 0,
                name = "Jokowi",
                phoneNumber = null,
                address = "Jl. Sakti",
                status = QurbanStatus.Participant,
                type = QurbanType.Sheep
            ),
            Member(
                id = 0,
                name = "Prabowo",
                phoneNumber = "0822xxxxxxxx",
                address = "Jl. Sakti",
                status = QurbanStatus.Participant,
                type = QurbanType.Cow
            )
        )
}