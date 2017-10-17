package br.com.diop.books.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.ShareActionProvider
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import br.com.diop.books.BookApp
import br.com.diop.books.R
import br.com.diop.books.database.BookDAO
import br.com.diop.books.model.Book
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.bumptech.glide.Glide
import org.parceler.Parcels


class BookDetailFragment : Fragment() {

    @BindView(R.id.tv_detail_title)
    internal var mTextTitle: TextView? = null
    @BindView(R.id.tv_detail_author)
    internal var mTextAuthor: TextView? = null
    @BindView(R.id.tv_detail_pages)
    internal var mTextPages: TextView? = null
    @BindView(R.id.tv_detail_year)
    internal var mTextYear: TextView? = null

    @BindView(R.id.iv_cover)
    internal var mImageCover: ImageView? = null

    @BindView(R.id.fab_favorite)
    internal var mFab: FloatingActionButton? = null

    private var mBook: Book? = null
    private var unbinder: Unbinder? = null
    private var mDao: BookDAO? = null
    private var mShareActionProvider: ShareActionProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        this.mDao = BookDAO(activity)

        if (arguments != null) {
            val p = arguments.getParcelable<Parcelable>(EXTRA_BOOK)
            this.mBook = Parcels.unwrap<Book>(p)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_book_detail, container, false)

        this.unbinder = ButterKnife.bind(this, view)
        this.mTextTitle!!.text = this.mBook!!.title
        this.mTextAuthor!!.text = this.mBook!!.author
        this.mTextPages!!.text = getString(R.string.pages_format, this.mBook!!.pages)
        this.mTextYear!!.text = getString(R.string.year_format, this.mBook!!.year)

        Glide.with(activity).load(this.mBook!!.cover).into(this.mImageCover!!)

        togglefavorite()

        return view
    }

    private fun togglefavorite() {
        val favorite = this.mDao!!.isFavorite(mBook)
        this.mFab!!.scaleX = 1f
        this.mFab!!.scaleY = 1f

        this.mFab!!.setImageResource(if (favorite)
            R.drawable.ic_remove
        else
            R.drawable.ic_check)
        this.mFab!!.backgroundTintList = if (favorite)
            ColorStateList.valueOf(Color.RED)
        else
            ColorStateList.valueOf(Color.parseColor("#2E7D32"))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.unbinder!!.unbind()
    }


    @OnClick(R.id.fab_favorite)
    fun toFavorite() {
        if (this.mDao!!.isFavorite(this.mBook)) {
            this.mDao!!.delete(this.mBook)
        } else {
            this.mDao!!.insert(this.mBook)
        }
        this.mFab!!.animate()
                .scaleX(0f)
                .scaleY(0f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        togglefavorite()
                        mFab!!.animate().scaleX(1f).scaleY(1f).setListener(null)
                    }
                })
        togglefavorite()
        (activity.application as BookApp).bus.post(this.mBook)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.menu_detail, menu)

        val item = menu!!.findItem(R.id.menu_share)

        mShareActionProvider = MenuItemCompat.getActionProvider(item) as ShareActionProvider

        mShareActionProvider!!.setShareIntent(
                Intent(Intent.ACTION_SEND)
                        .putExtra(
                                Intent.EXTRA_TEXT,
                                this.mBook!!.title
                        )
                        .setType("text/plain"))
    }

    companion object {

        private val EXTRA_BOOK = "param1"


        fun newInstance(book: Book): BookDetailFragment {
            val fragment = BookDetailFragment()
            val args = Bundle()

            val p = Parcels.wrap(book)
            args.putParcelable(EXTRA_BOOK, p)
            fragment.arguments = args
            return fragment
        }
    }
}
