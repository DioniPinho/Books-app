package br.com.diop.books.database

import android.provider.BaseColumns


interface BookContract : BaseColumns {
    companion object {
        val TABLE_NAME = "books"
        val TITLE = "title"
        val AUTHOR = "author"
        val YEAR = "year"
        val COVER = "cover"
        val PAGES = "pages"
    }
}
