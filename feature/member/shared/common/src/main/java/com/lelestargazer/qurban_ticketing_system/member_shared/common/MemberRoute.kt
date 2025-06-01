package com.lelestargazer.qurban_ticketing_system.member_shared.common

import kotlinx.serialization.Serializable

@Serializable
sealed class MemberRoute {

    @Serializable
    data object Management : MemberRoute()

    @Serializable
    data object Ticketing : MemberRoute()

    @Serializable
    data object ImportExport : MemberRoute()

    @Serializable
    data object HomeScreen
}