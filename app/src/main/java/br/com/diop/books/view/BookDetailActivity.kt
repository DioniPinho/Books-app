package br.com.diop.books.view

import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import br.com.diop.books.R
import br.com.diop.books.model.Book
import org.parceler.Parcels.unwrap

class BookDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        val book = unwrap<Book>(intent.getParcelableExtra<Parcelable>(EXTRA_BOOK))

        val detailFragment = BookDetailFragment.newInstance(book)
        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_detail_book_fragment, detailFragment, "detail")
                .commit()
    }

    companion object {

        val EXTRA_BOOK = "book"
    }
}
