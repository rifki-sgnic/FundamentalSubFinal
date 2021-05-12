package com.dicoding.picodiploma.fundamentalsubfinal.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.dicoding.picodiploma.fundamentalsubfinal"
    const val SCHEME = "content"

    internal class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val AVATAR_URL = "avatar_url"
            const val FULLNAME = "name"
            const val FOLLOWERS = "followers"
            const val FOLLOWING = "following"
            const val LOCATION = "location"
            const val COMPANY = "company"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}
