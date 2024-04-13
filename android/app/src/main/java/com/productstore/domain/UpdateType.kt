package com.productstore.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class UpdateType : Parcelable {
    ADD_PRODUCT,
    EDIT_PRODUCT
}
