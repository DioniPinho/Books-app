package br.com.diop.books.database


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class BookDbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE " + BookContract.TABLE_NAME + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BookContract.TITLE + " TEXT NOT NULL, " +
                BookContract.AUTHOR + " TEXT NOT NULL, " +
                BookContract.YEAR + " INTEGER NOT NULL, " +
                BookContract.PAGES + " INTEGER NOT NULL, " +
                BookContract.COVER + " TEXT NOT NULL)")

    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {

    }

    companion object {

        val DB_VERSION = 1
        val DB_NAME = "bookd_db"
    }
}
