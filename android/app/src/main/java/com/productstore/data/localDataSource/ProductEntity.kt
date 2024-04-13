package com.productstore.data.localDataSource

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class ProductEntity(
    @PrimaryKey
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val image: String = "",
    val price: Double = 0.0,
    val category: String = ""
) : Parcelable {
    fun formattedPrice(): String {
        return "S$ $price"
    }
}
