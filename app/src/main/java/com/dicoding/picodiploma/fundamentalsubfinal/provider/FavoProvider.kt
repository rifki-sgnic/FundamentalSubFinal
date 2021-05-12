package com.dicoding.picodiploma.fundamentalsubfinal.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.AUTHORITY
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.dicoding.picodiploma.fundamentalsubfinal.database.UserHelper

class FavoProvider : ContentProvider() {

    companion object {
        private const val FAVO = 1
        private const val FAVO_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favoHelper: UserHelper

        init {
            // content://com.dicoding.picodiploma.fundamentalsubfinal/favorite
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVO)
            // content://com.dicoding.picodiploma.fundamentalsubfinal/favorite/id
            sUriMatcher.addURI(AUTHORITY, "${TABLE_NAME}/#", FAVO_ID)
        }
    }

    override fun onCreate(): Boolean {
        favoHelper = UserHelper.getInstance(context as Context)
        favoHelper.open()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor {
        return when (sUriMatcher.match(uri)) {
            FAVO -> favoHelper.queryAll()
            FAVO_ID -> favoHelper.queryById(uri.lastPathSegment.toString())
            else -> throw IllegalArgumentException("This is an Unknown URI $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (FAVO) {
            sUriMatcher.match(uri) -> favoHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("${CONTENT_URI}/$added")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (FAVO_ID) {
            sUriMatcher.match(uri) -> favoHelper.deleteById(uri.lastPathSegment.toString())
            else -> throw IllegalArgumentException("This is an Unknown URI $uri")
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}