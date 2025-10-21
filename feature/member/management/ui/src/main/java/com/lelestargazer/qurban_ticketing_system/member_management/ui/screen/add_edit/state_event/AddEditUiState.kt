package com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit.state_event

import android.telephony.PhoneNumberUtils
import arrow.core.Either
import arrow.core.left
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.right
import arrow.optics.optics
import com.lelestargazer.qurban_ticketing_system.common.UIText
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R
import com.lelestargazer.qurban_ticketing_system.member_management.ui.route.MemberAddEdit.ScreenType
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.QurbanStatus
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.QurbanType

@optics
data class AddEditUiState(
    val screenType: ScreenType,
    val name: String = "",
    val nameError: UIText? = null,
    val phoneNumber: String = "",
    val phoneNumberError: UIText? = null,
    val address: String = "",

    val qurbanStatus: QurbanStatus = QurbanStatus.Recipient,
    val qurbanType: QurbanType = QurbanType.Cow,


    val isLoading: Boolean = false,
) {

    fun validateName(): Either<UIText, Unit> = either {
        ensure(name.isNotBlank()) { UIText.ResourceID(R.string.error_name_is_blank, emptyList()) }
    }

    fun validatePhoneNumber(): Either<UIText, Unit> {

        if (phoneNumber.isNotBlank() && !PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
            return UIText.ResourceID(
                R.string.error_phone_number_is_not_valid,
                emptyList()
            ).left()
        }

        if (phoneNumber.isNotBlank() && phoneNumber.length < 11) {
            return UIText.ResourceID(
                R.string.error_phone_number_is_not_sufficient,
                emptyList()
            ).left()
        }

        return Unit.right()
    }

    companion object
}

