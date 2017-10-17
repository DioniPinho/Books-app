package br.com.diop.books.model

import com.google.gson.annotations.SerializedName

import org.parceler.Parcel

@Parcel
class Book {

    @SerializedName("titulo")
    lateinit var title: String
    @SerializedName("autor")
    lateinit var author: String
    @SerializedName("ano")
    var year: Int = 0
    @SerializedName("paginas")
    var pages: Int = 0
    @SerializedName("capa")
    lateinit var cover: String

    @SerializedName("id")
    var id: Long = 0

    constructor() {}

    constructor(title: String, author: String, year: Int, pages: Int, cover: String) {
        this.title = title
        this.author = author
        this.year = year
        this.pages = pages
        this.cover = cover
    }


    override fun toString(): String = this.title + " - " + this.author
}
