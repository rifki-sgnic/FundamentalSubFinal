package com.dicoding.picodiploma.fundamentalsubfinal.widget

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.fundamentalsubfinal.R
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.fundamentalsubfinal.helper.MappingHelper

internal class StackRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private var cursor: Cursor? = null
    private val widgetItems = ArrayList<Bitmap>()

    override fun onCreate() {}

    override fun onDataSetChanged() {
        cursor?.close()

        val identityToken = Binder.clearCallingIdentity()
        cursor = context.contentResolver.query(CONTENT_URI, null, null, null, null)
        val fav = MappingHelper.mapCursorToArrayList(cursor)

        for (pos in 0 until fav.count()) {
            val bitmap: Bitmap = Glide.with(context)
                .asBitmap()
                .load(fav[pos].avatar)
                .submit()
                .get()
            widgetItems.add(bitmap)
        }
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = widgetItems.size

    override fun getViewAt(pos: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView, widgetItems[pos])
        val extras = bundleOf(
            StackWidget.EXTRA_ITEM to pos
        )

        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}