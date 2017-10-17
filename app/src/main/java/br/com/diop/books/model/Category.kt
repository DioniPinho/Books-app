package br.com.diop.books.model

import com.google.gson.annotations.SerializedName

class Category {

    lateinit var name: String
    @SerializedName("livros")
    lateinit var books: List<Book>
}
