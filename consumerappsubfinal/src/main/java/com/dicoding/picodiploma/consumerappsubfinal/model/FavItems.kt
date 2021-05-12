package com.dicoding.picodiploma.consumerappsubfinal.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavItems(
    var id: Int = 0,
    var username: String? = null,
    var avatar: String? = null,
    var name: String? = null,
    var followers: Int = 0,
    var following: Int = 0,
    var location: String? = null,
    var company: String? = null
) : Parcelable