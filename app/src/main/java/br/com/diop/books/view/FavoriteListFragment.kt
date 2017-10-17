package br.com.diop.books.view


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import br.com.diop.books.BookApp
import br.com.diop.books.R
import br.com.diop.books.adapters.BooksAdapter
import br.com.diop.books.database.BookDAO
import br.com.diop.books.listeners.ClickBookListener
import br.com.diop.books.model.Book
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnItemClick
import butterknife.Unbinder
import org.greenrobot.eventbus.Subscribe


class FavoriteListFragment : Fragment() {

    @BindView(R.id.lv_book)
    internal var listView: ListView? = null
    @BindView(R.id.ist_empty)
    internal var mListempty: View? = null

    internal lateinit var mBooks: MutableList<Book>
    internal lateinit var mAdapter: ArrayAdapter<Book>
    private var unbind: Unbinder? = null
    internal lateinit var mDAO: BookDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        this.mDAO = BookDAO(activity)
        this.mBooks = this.mDAO.list() as MutableList<Book>

        (activity.application as BookApp).bus.register(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_favorite_list, container, false)
        this.unbind = ButterKnife.bind(this, view)

        mAdapter = BooksAdapter(this.context, this.mBooks)

        listView!!.emptyView = mListempty

        listView!!.adapter = mAdapter

        return view
    }

    @OnItemClick(R.id.lv_book)
    internal fun onItemSelected(position: Int) {

        val book = this.mBooks[position]
        if (activity is ClickBookListener) {
            val listener = activity as ClickBookListener
            listener.clickedBook(book)
        }
    }

    @Subscribe
    fun update(book: Book) {
        this.mBooks.clear()
        this.mBooks.addAll(this.mDAO.list())
        this.mAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.unbind!!.unbind()
        (activity.application as BookApp).bus.unregister(this)
    }
}
