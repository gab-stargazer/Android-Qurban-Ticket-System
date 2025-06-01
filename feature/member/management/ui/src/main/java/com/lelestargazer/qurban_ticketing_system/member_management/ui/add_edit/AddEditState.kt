package com.lelestargazer.qurban_ticketing_system.member_management.ui.add_edit

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import com.lelestargazer.qurban_ticketing_system.common.UIText
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R
import com.lelestargazer.qurban_ticketing_system.member_management.ui.route.MemberAddEdit

data class AddEditState(
    val screenType: MemberAddEdit.Type,
    val name: String = "",
    val nameError: UIText? = null,
    val phoneNumber: String = "",
    val phoneNumberError: UIText? = null,
    val address: String = "",
    val addressError: UIText? = null,
    val description: String = "",
    val isParticipant: Boolean = false,
    val isActive: Boolean = true,
) {

    fun validateName(): Either<UIText, Unit> = either {
        ensure(name.isNotBlank()) { UIText.ResourceID(R.string.error_name_is_blank, emptyList()) }
    }

    fun validatePhoneNumber(): Either<UIText, Unit> = either {
        ensure(phoneNumber.isNotBlank()) {
            UIText.ResourceID(
                R.string.error_phone_number_is_blank,
                emptyList()
            )
        }

        ensure(phoneNumber.length >= 11) {
            UIText.ResourceID(
                R.string.error_phone_number_is_not_sufficient,
                emptyList()
            )
        }
    }

    fun validateAddress(): Either<UIText, Unit> = either {
        ensure(address.isNotBlank()) {
            UIText.ResourceID(
                R.string.error_address_is_blank,
                emptyList()
            )
        }
    }

//    fun validateHouseholdSize(): Either<UIText, Unit> = either {
//        ensure(householdSize != null && householdSize > 0) {
//            UIText.ResourceID(
//                R.string.error_household_size,
//                emptyList()
//            )
//        }
//    }
}

