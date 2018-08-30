package me.keatsyh.compiler.utils

import javax.annotation.processing.Messager
import javax.tools.Diagnostic

class Logger {
    companion object {
        lateinit var messager: Messager
        fun init(messager: Messager): Logger {
            this.messager = messager
            return Logger()
        }
    }

    fun w(message: String) {
        messager.printMessage(Diagnostic.Kind.WARNING, message)
    }

}