package br.com.diop.books.database


import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import br.com.diop.books.model.Book
import java.util.*

class BookDAO(private val mContext: Context) {

    fun insert(book: Book?): Long {
        val helper = BookDbHelper(mContext)
        val db = helper.writableDatabase

        val values = valuesFromBook(book)

        val id = db.insert(BookContract.TABLE_NAME, null, values)

        db.close()
        return id
    }

    fun update(book: Book): Int {
        val helper = BookDbHelper(mContext)
        val db = helper.writableDatabase

        val values = valuesFromBook(book)

        val rowsAffected = db.update(BookContract.TABLE_NAME, values, BaseColumns._ID + " =?",
                arrayOf(book.id.toString()))

        db.close()
        return rowsAffected
    }


    fun delete(book: Book?): Int {
        val helper = BookDbHelper(mContext)
        val db = helper.writableDatabase

        val rowsAffected = db.delete(BookContract.TABLE_NAME, BookContract.TITLE + " = ?", arrayOf(book?.title))

        db.close()

        return rowsAffected
    }

    fun list(): List<Book> {
        val helper = BookDbHelper(mContext)
        val db = helper.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM " + BookContract.TABLE_NAME, null)
        val books = ArrayList<Book>()

        if (cursor.count > 0) {

            val idIdx = cursor.getColumnIndex(BaseColumns._ID)
            val titleIdx = cursor.getColumnIndex(BookContract.TITLE)
            val authorIdx = cursor.getColumnIndex(BookContract.AUTHOR)
            val coverIdx = cursor.getColumnIndex(BookContract.COVER)
            val yearIdx = cursor.getColumnIndex(BookContract.YEAR)
            val pagesIdx = cursor.getColumnIndex(BookContract.PAGES)

            while (cursor.moveToNext()) {
                val book = Book()
                book.id = cursor.getLong(idIdx)
                book.title = cursor.getString(titleIdx)
                book.author = cursor.getString(authorIdx)
                book.cover = cursor.getString(coverIdx)
                book.year = cursor.getInt(yearIdx)
                book.pages = cursor.getInt(pagesIdx)

                books.add(book)
            }

            cursor.close()
        }
        db.close()
        return books

    }

    fun isFavorite(book: Book?): Boolean {
        val helper = BookDbHelper(this.mContext)
        val db = helper.readableDatabase

        val cursor = db.rawQuery("SELECT count(*) FROM " + BookContract.TABLE_NAME
                + " WHERE " + BookContract.TITLE + " = ?", arrayOf(book?.title))

        var exists = false

        if (cursor != null) {

            cursor.moveToNext()
            exists = cursor != null && cursor.getInt(0) > 0
            cursor.close()
        }

        db.close()
        return exists
    }

    private fun valuesFromBook(book: Book?): ContentValues {
        val values = ContentValues()
        values.put(BookContract.TITLE, book?.title)
        values.put(BookContract.AUTHOR, book?.author)
        values.put(BookContract.YEAR, book?.year)
        values.put(BookContract.PAGES, book?.pages)
        values.put(BookContract.COVER, book?.cover)

        return values
    }
}
