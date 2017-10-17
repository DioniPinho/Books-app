package br.com.diop.books.model

import com.google.gson.annotations.SerializedName

class PublishingCompany {

    @SerializedName("novatec")
    lateinit var categories: List<Category>
}
