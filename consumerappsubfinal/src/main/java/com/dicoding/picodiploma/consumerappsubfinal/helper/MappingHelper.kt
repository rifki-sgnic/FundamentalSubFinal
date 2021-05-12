package com.dicoding.picodiploma.consumerappsubfinal.helper

import android.database.Cursor
import com.dicoding.picodiploma.consumerappsubfinal.database.DatabaseContract
import com.dicoding.picodiploma.consumerappsubfinal.model.FavItems

object MappingHelper {

    fun mapCursorToArrayList(favCursor: Cursor?) : ArrayList<FavItems> {
        val favList = ArrayList<FavItems>()
        favCursor?.apply {
            while(moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR_URL))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.FULLNAME))
                val followers = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWERS))
                val following = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWING))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.LOCATION))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COMPANY))
                favList.add(FavItems(id, username, avatar, name, followers, following, location, company))
            }
        }
        return favList
    }
}