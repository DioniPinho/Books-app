package br.com.diop.books

import android.app.Application

import org.greenrobot.eventbus.EventBus

class BookApp : Application() {

    lateinit var bus: EventBus
        internal set

    override fun onCreate() {
        super.onCreate()

        bus = EventBus()
    }
}
