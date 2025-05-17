package com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen

data class Participant(
    val pid: String,               // "P-001"
    val name: String,              // "John Doe"
    val phone: String,             // "+621234..."
    val address: String,           // "Jl. Qurban No. 123"
    val householdSize: Int = 1,    // 4 (people in family)
    val description: String? = null,  // "Prefers leg portion"
    val joinYear: Int,             // 2024
    val isActive: Boolean = true   // Can deactivate
)

fun generateParticipantList(): List<Participant> {
    return listOf(
        Participant(
            pid = "P-001",
            name = "John Doe",
            phone = "+62123456789",
            address = "Jl. Merdeka No. 12, Jakarta",
            householdSize = 4,
            description = "Prefers leg portion, halal certified",
            joinYear = 2024
        ),
        Participant(
            pid = "P-002",
            name = "Sarah Smith",
            phone = "+62876543210",
            address = "Jl. Sudirman Kav. 21, Jakarta",
            householdSize = 2,
            description = "Vegetarian - only wants non-meat shares",
            joinYear = 2024
        ),
        Participant(
            pid = "P-003",
            name = "Ahmad Abdullah",
            phone = "+621122334455",
            address = "Jl. Thamrin No. 8, Jakarta",
            householdSize = 6,
            description = "Large family, needs extra shares",
            joinYear = 2023,
            isActive = false
        ),
        Participant(
            pid = "P-004",
            name = "Maria Garcia",
            phone = "+62988776655",
            address = "Jl. Asia Afrika No. 100, Bandung",
            householdSize = 3,
            joinYear = 2025
        ),
        Participant(
            pid = "P-005",
            name = "Lee Min Ho",
            phone = "+62100112233",
            address = "Jl. Pakubuwono No. 45, Jakarta",
            householdSize = 1,
            description = "Single person household",
            joinYear = 2024
        )
    )
}