package com.abdulaziz.provalyutakurslari.utils

import android.content.ContentProvider
import android.content.ContentValues
import android.net.Uri
import androidx.work.Configuration
import androidx.work.WorkManager

class MyWorkManagerInitializer : DummyContentProvider() {
    override fun onCreate(): Boolean {
        WorkManager.initialize(context!!, Configuration.Builder().build())
        return true
    }
}

abstract class DummyContentProvider : ContentProvider() {
    override fun onCreate() = true

    override fun insert(uri: Uri, values: ContentValues?) = null
    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?) = null
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?) = 0
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?) = 0

    override fun getType(uri: Uri) = null
}