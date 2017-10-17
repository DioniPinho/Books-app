package br.com.diop.books.view

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import br.com.diop.books.R
import br.com.diop.books.listeners.ClickBookListener
import br.com.diop.books.model.Book
import butterknife.BindView
import butterknife.ButterKnife
import org.parceler.Parcels

class BookActivity : AppCompatActivity(), ClickBookListener {

    @BindView(R.id.vp_main)
    internal var mViewPager: ViewPager? = null
    @BindView(R.id.tl_main)
    internal var mTabLayout: TabLayout? = null
    @BindView(R.id.toolbar_main)
    internal var mToolbar: Toolbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        ButterKnife.bind(this)

        setSupportActionBar(this.mToolbar)

        this.mViewPager!!.adapter = BookPager(supportFragmentManager)
        this.mTabLayout!!.setupWithViewPager(this.mViewPager)
    }

    override fun clickedBook(book: Book) {
        if (resources.getBoolean(R.bool.tablet)) {
            val detailFragment = BookDetailFragment.newInstance(book)
            supportFragmentManager.beginTransaction().replace(R.id.frame_detail_main, detailFragment, "detail")
                    .commit()
        } else {
            val p = Parcels.wrap(book)
            startActivity(Intent(this, BookDetailActivity::class.java)
                    .putExtra(BookDetailActivity.EXTRA_BOOK, p))

        }
    }

    internal inner class BookPager(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getPageTitle(position: Int): CharSequence {
            if (position == 0) return getString(R.string.web_tab)

            return getString(R.string.favorites_tab)

        }

        override fun getItem(position: Int): Fragment {
            if (position == 0) {
                return BookListFragment()
            }
            return FavoriteListFragment()
        }

        override fun getCount(): Int = 2
    }
}
