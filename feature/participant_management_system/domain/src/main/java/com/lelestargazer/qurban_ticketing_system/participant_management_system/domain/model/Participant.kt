package com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.model

import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Participant(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val phoneNumber: String,
    val address: String,
    val householdSize: Int = 1,
    val description: String,
    val isActive: Boolean = true,
)

fun generateParticipantList(): List<Participant> {
    return listOf(
        Participant(
            id = UUID(2, 3),
            name = "John Doe",
            phoneNumber = "+62123456789",
            address = "Jl. Merdeka No. 12, Jakarta",
            householdSize = 4,
            description = "Prefers leg portion, halal certified",
            isActive = true
        ),
        Participant(
            id = UUID(2, 3),
            name = "Sarah Smith",
            phoneNumber = "+62876543210",
            address = "Jl. Sudirman Kav. 21, Jakarta",
            householdSize = 2,
            description = "Vegetarian - only wants non-meat shares"
        ),
        Participant(
            id = UUID(2, 3),
            name = "Ahmad Abdullah",
            phoneNumber = "+621122334455",
            address = "Jl. Thamrin No. 8, Jakarta",
            householdSize = 6,
            description = "Large family, needs extra shares",
            isActive = false
        ),
        Participant(
            id = UUID(2, 3),
            name = "Maria Garcia",
            phoneNumber = "+62988776655",
            address = "Jl. Asia Afrika No. 100, Bandung",
            householdSize = 3,
            description = ""
        ),
        Participant(
            id = UUID(2, 3),
            name = "Lee Min Ho",
            phoneNumber = "+62100112233",
            address = "Jl. Pakubuwono No. 45, Jakarta",
            householdSize = 1,
            description = "Single person household",
        )
    )
}