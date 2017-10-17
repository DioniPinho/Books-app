package br.com.diop.books.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide

import br.com.diop.books.R
import br.com.diop.books.model.Book
import butterknife.BindView
import butterknife.ButterKnife

class BooksAdapter(context: Context, mBooks: List<Book>) : ArrayAdapter<Book>(context, 0, mBooks) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val book = getItem(position)
        val viewHolder: ViewHolder
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false)
            viewHolder = ViewHolder(convertView)
            convertView!!.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }


        Glide.with(context).load(book!!.cover).into(viewHolder.imageView!!)
        viewHolder.textTitle!!.text = book.title
        viewHolder.textAuthor!!.text = book.author

        return convertView
    }

    internal class ViewHolder(parent: View) {
        @BindView(R.id.iv_cover)
        var imageView: ImageView? = null
        @BindView(R.id.tv_title)
        var textTitle: TextView? = null
        @BindView(R.id.tv_author)
        var textAuthor: TextView? = null

        init {
            ButterKnife.bind(this, parent)
        }
    }
}
