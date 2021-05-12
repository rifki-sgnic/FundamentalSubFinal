package com.dicoding.picodiploma.fundamentalsubfinal.helper

import android.database.Cursor
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.AVATAR_URL
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.COMPANY
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.FOLLOWERS
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.FOLLOWING
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.FULLNAME
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.LOCATION
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.USERNAME
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion._ID
import com.dicoding.picodiploma.fundamentalsubfinal.model.FavItems

object MappingHelper {

    fun mapCursorToArrayList(favCursor: Cursor?): ArrayList<FavItems> {
        val favList = ArrayList<FavItems>()
        favCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val avatar = getString(getColumnIndexOrThrow(AVATAR_URL))
                val name = getString(getColumnIndexOrThrow(FULLNAME))
                val followers = getInt(getColumnIndexOrThrow(FOLLOWERS))
                val following = getInt(getColumnIndexOrThrow(FOLLOWING))
                val location = getString(getColumnIndexOrThrow(LOCATION))
                val company = getString(getColumnIndexOrThrow(COMPANY))
                favList.add(FavItems(id, username, avatar, name, followers, following, location, company))
            }
        }
        return favList
    }
}