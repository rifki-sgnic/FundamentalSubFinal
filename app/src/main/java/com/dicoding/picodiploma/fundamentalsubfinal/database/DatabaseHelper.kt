package com.dicoding.picodiploma.fundamentalsubfinal.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.AVATAR_URL
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.COMPANY
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.FOLLOWERS
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.FOLLOWING
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.FULLNAME
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.LOCATION
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.USERNAME
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion._ID

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dbgithubuserapp"
        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_FAVLIST = "CREATE TABLE $TABLE_NAME" +
                " (${_ID} INTEGER PRIMARY KEY," +
                " $USERNAME TEXT NOT NULL," +
                " $AVATAR_URL TEXT NOT NULL," +
                " $FULLNAME TEXT NOT NULL, " +
                " $FOLLOWERS INTEGER NOT NULL, " +
                " $FOLLOWING INTEGER NOT NULL, " +
                " $LOCATION TEXT NOT NULL, " +
                " $COMPANY TEXT NOT NULL) "
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAVLIST)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}