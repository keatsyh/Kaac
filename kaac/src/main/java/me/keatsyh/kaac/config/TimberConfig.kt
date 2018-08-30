package me.keatsyh.kaac.config

import me.keatsyh.kaac.BuildConfig
import timber.log.Timber


class TimberConfig {

    companion object {
        fun init():TimberConfig {
            if (BuildConfig.DEBUG) {
                Timber.plant(VarTree())
            } else {
                Timber.plant(CrashTree())
            }
            return TimberConfig()
        }
    }

}

class CrashTree: Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class VarTree: Timber.DebugTree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        var tag = tag
        if (tag != null) {
            val threadName = Thread.currentThread().name
            tag = "<$threadName> $tag"
        }

        super.log(priority, tag, message, t)
    }


    override fun createStackElementTag(element: StackTraceElement): String? {

        return super.createStackElementTag(element) + "(Line: ${element.lineNumber} -- Method: ${element.methodName} )"  //日志显示行号
    }


}