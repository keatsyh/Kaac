package me.keatsyh.kaac.layout

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


open class KFragment : Fragment() {

    lateinit var ca: KActivity

    val kaac by lazy {
        ca.kaac
    }

    val kApp by lazy {
        ca.kApp
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ca = activity as KActivity

        return super.onCreateView(inflater, container, savedInstanceState)

    }
}