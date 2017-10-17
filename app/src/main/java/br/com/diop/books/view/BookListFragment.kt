package br.com.diop.books.view


import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import br.com.diop.books.R
import br.com.diop.books.adapters.BooksAdapter
import br.com.diop.books.listeners.ClickBookListener
import br.com.diop.books.model.Book
import br.com.diop.books.model.PublishingCompany
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnItemClick
import butterknife.Unbinder
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*


class BookListFragment : Fragment() {

    @BindView(R.id.lv_book)
    internal var listView: ListView? = null
    @BindView(R.id.swipe)
    internal var mSwipe: SwipeRefreshLayout? = null
    @BindView(R.id.ist_empty)
    internal var mListempty: View? = null

    internal lateinit var mBooks: MutableList<Book>
    internal lateinit var mAdapter: ArrayAdapter<Book>
    private var unbind: Unbinder? = null
    private var mBooksTask: BooksTask? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        this.mBooks = ArrayList<Book>()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_book_list, container, false)
        this.unbind = ButterKnife.bind(this, view)

        mAdapter = BooksAdapter(this.context, this.mBooks)

        listView!!.emptyView = mListempty

        listView!!.adapter = mAdapter

        this.mSwipe!!.setOnRefreshListener { getJson() }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (mBooks.size == 0 && this.mBooksTask == null) {
            getJson()
        } else if (mBooksTask != null && mBooksTask!!.status == AsyncTask.Status.RUNNING) {
            showProgress()
        }
    }

    private fun getJson() {

        val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo

        if (info != null && info.isConnected) {

            this.mBooksTask = BooksTask()
            this.mBooksTask!!.execute()
        } else {
            this.mSwipe!!.isRefreshing = false
            Toast.makeText(activity, R.string.error_connection, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showProgress() {
        this.mSwipe!!.post { mSwipe!!.isRefreshing = true }
    }

    @OnItemClick(R.id.lv_book)
    internal fun onItemSelected(position: Int) {

        val book = this.mBooks[position]
        if (activity is ClickBookListener) {
            val listener = activity as ClickBookListener
            listener.clickedBook(book)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.unbind!!.unbind()
    }

    private inner class BooksTask : AsyncTask<Void, Void, PublishingCompany>() {

        override fun onPreExecute() {
            super.onPreExecute()
            showProgress()
        }

        override fun doInBackground(vararg params: Void): PublishingCompany? {
            val client = OkHttpClient()
            val request = Request.Builder()
                    .url("https://www.dropbox.com/s/htt56rfq6xedian/livros_novatec.json?dl=1")
                    .build()


            try {

                val response = client.newCall(request).execute()
                val jsonString = response.body()!!.string()
                Log.d("BOOKTASK", jsonString)

                val gson = Gson()
                val company = gson.fromJson(jsonString, PublishingCompany::class.java)
                return company

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(company: PublishingCompany?) {
            super.onPostExecute(company)
            if (company != null) {
                mBooks.clear()
                for (category in company.categories) {
                    mBooks.addAll(category.books)
                }
                mAdapter.notifyDataSetChanged()

                if (resources.getBoolean(R.bool.tablet) && mBooks.size > 0) {
                    onItemSelected(0)
                }
            }
            mSwipe!!.isRefreshing = false
        }

    }
}
