package br.com.diop.books.listeners

import br.com.diop.books.model.Book

interface ClickBookListener {
    fun clickedBook(book: Book)

}